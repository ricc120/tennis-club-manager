package it.tennis_club.controller.api;

import it.tennis_club.business_logic.AuthService;
import it.tennis_club.business_logic.AuthenticationException;
import it.tennis_club.config.JwtUtils;
import it.tennis_club.domain_model.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST Controller per l'autenticazione — STEP 8: JWT.
 * 
 * PRIMA (Step 5):
 *   POST /api/auth/login → risposta: { utente }
 *   Il frontend salvava l'utente in localStorage, ma le API erano pubbliche.
 * 
 * ADESSO (Step 8):
 *   POST /api/auth/login → risposta: { "token": "eyJ...", "utente": {...} }
 *   Il frontend salva il TOKEN e lo invia in ogni richiesta successiva.
 *   Le API sono protette — senza token, ricevi 401.
 * 
 * CONCETTO: Perché usiamo Map.of() per la risposta?
 * 
 * Map.of("token", jwt, "utente", utente) crea un oggetto immutabile
 * che Jackson serializza come:
 *   { "token": "eyJhbGci...", "utente": { "id": 1, "nome": "Mario", ... } }
 * 
 * Un'alternativa sarebbe creare una classe LoginResponse DTO,
 * ma Map.of() è più conciso per un caso semplice come questo.
 */
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @Autowired
    public ApiAuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * POST /api/auth/login — Autentica l'utente e restituisce un JWT token.
     * 
     * Richiesta:
     *   POST /api/auth/login
     *   Content-Type: application/json
     *   {"email": "mario@email.com", "password": "secret123"}
     * 
     * Risposta (successo — 200):
     *   {
     *     "token": "eyJhbGciOiJIUzI1NiJ9...",
     *     "utente": { "id": 1, "nome": "Mario", "cognome": "Rossi", "email": "mario@email.com", "ruolo": "ADMIN" }
     *   }
     * 
     * Risposta (credenziali errate — 401):
     *   {"errore": "Credenziali non valide"}
     * 
     * NOTA: questa rotta è PUBBLICA (/api/auth/** → permitAll in SecurityConfig).
     * È l'unico modo per ottenere un token — tutte le altre API richiedono il token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Utente utente = authService.login(request.getEmail(), request.getPassword());

            if (utente != null) {
                // STEP 8: Genera il JWT token per l'utente autenticato
                String token = jwtUtils.generateToken(utente);

                // Restituisce sia il token che i dati dell'utente
                // Il frontend salverà entrambi: token per le richieste API,
                // utente per mostrare nome/ruolo nella UI
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "utente", utente
                ));
            } else {
                return ResponseEntity
                        .status(401)
                        .body(Map.of("errore", "Credenziali non valide"));
            }

        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(400)
                    .body(Map.of("errore", e.getMessage()));
        }
    }
}
