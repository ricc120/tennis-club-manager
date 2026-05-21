package it.tennis_club.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import it.tennis_club.domain_model.Utente;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility per gestire i token JWT — STEP 8.
 * 
 * CONCETTO: JWT (JSON Web Token)
 * 
 * Un JWT è composto da 3 parti separate da punti:
 *   eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJpb0BlbWFpbC5jb20ifQ.abc123
 *   ↑ Header                ↑ Payload (Claims)                    ↑ Signature
 * 
 * Header:    algoritmo di firma (HS256 = HMAC-SHA256)
 * Payload:   i dati (chi è l'utente, quando scade il token)
 * Signature: la firma che garantisce che nessuno ha modificato il token
 * 
 * ANALOGIA: è come un biglietto del cinema con un timbro:
 * - Il biglietto contiene: "Mario Rossi, fila 3, posto 5" (payload)
 * - Il timbro garantisce che il biglietto è autentico (signature)
 * - Se qualcuno cambia "fila 3" in "fila 1", il timbro non corrisponde più
 * 
 * CONCETTO: @Value
 * 
 * @Value("${jwt.secret}") inietta il valore da application.properties.
 * È come @Autowired ma per valori semplici (stringhe, numeri) invece di bean.
 */
@Component
public class JwtUtils {

    private final SecretKey key;
    private final long expirationMs;

    /**
     * Costruttore — riceve le proprietà da application.properties.
     * 
     * @Value inietta valori dal file di configurazione:
     *   jwt.secret → la chiave segreta per firmare i token
     *   jwt.expiration → la durata del token in millisecondi
     * 
     * La chiave viene convertita in un oggetto SecretKey usando HMAC-SHA.
     * HMAC-SHA è l'algoritmo crittografico che "firma" il token.
     */
    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Genera un token JWT per un utente autenticato.
     * 
     * Il token contiene:
     * - subject:  l'email dell'utente (identifica CHI è)
     * - claim "ruolo": il ruolo dell'utente (ADMIN, SOCIO, etc.)
     * - claim "id": l'ID dell'utente nel database
     * - issuedAt: quando è stato creato il token
     * - expiration: quando scade (issuedAt + 24 ore)
     * 
     * ANALOGIA con Spring Session:
     *   Session: session.setAttribute("utente", utente)  → dati sul SERVER
     *   JWT:     claims.put("email", email)               → dati NEL TOKEN
     * 
     * La differenza fondamentale: la sessione vive sul server,
     * il JWT vive nel client. Il server è STATELESS.
     */
    public String generateToken(Utente utente) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(utente.getEmail())                    // CHI è l'utente
                .claim("id", utente.getId())                   // ID nel database
                .claim("ruolo", utente.getRuolo().name())      // ruolo come stringa
                .claim("nome", utente.getNome())               // nome (per il frontend)
                .issuedAt(now)                                 // QUANDO è stato creato
                .expiration(expiration)                        // QUANDO scade
                .signWith(key)                                 // FIRMA con la chiave segreta
                .compact();                                    // → produce la stringa JWT
    }

    /**
     * Valida un token JWT — verifica che sia autentico e non scaduto.
     * 
     * La validazione controlla:
     * 1. La firma è corretta? (nessuno ha modificato il token)
     * 2. Il token è scaduto? (la data di expiration è passata)
     * 
     * Se una di queste condizioni fallisce, lancia un'eccezione
     * e il metodo ritorna false.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)       // usa la stessa chiave per verificare
                .build()
                .parseSignedClaims(token);  // se fallisce, lancia eccezione
            return true;
        } catch (Exception e) {
            // Token invalido, scaduto, o manomesso
            return false;
        }
    }

    /**
     * Estrae l'email (subject) dal token.
     * Usata dal filtro per identificare l'utente ad ogni richiesta.
     * 
     * FLUSSO:
     * 1. Il frontend invia: Authorization: Bearer eyJhbGci...
     * 2. Il filtro estrae il token dall'header
     * 3. Chiama getEmailFromToken(token) → "mario@email.com"
     * 4. Cerca l'utente nel database per email
     * 5. Imposta l'autenticazione nel SecurityContext
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();  // il "subject" è l'email
    }
}
