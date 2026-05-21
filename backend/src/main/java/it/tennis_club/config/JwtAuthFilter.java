package it.tennis_club.config;

import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.UtenteDAO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro JWT — Intercetta ogni richiesta HTTP e valida il token.
 * 
 * STEP 8: Questo è il CUORE dell'autenticazione JWT.
 * 
 * CONCETTO: Filter Chain di Spring Security
 * 
 * Quando arriva una richiesta HTTP, attraversa una CATENA di filtri
 * prima di raggiungere il controller:
 * 
 *   Richiesta HTTP
 *       ↓
 *   [CorsFilter]      → gestisce CORS
 *       ↓
 *   [JwtAuthFilter]   → ★ QUESTO FILTRO ★ valida il token JWT
 *       ↓
 *   [AuthorizationFilter] → controlla se l'utente ha i permessi
 *       ↓
 *   [Controller]      → esegue la logica (ApiCampoController, etc.)
 * 
 * ANALOGIA: è come il buttafuori di un club:
 * - Controlla se hai il braccialetto (token nell'header)
 * - Verifica che il braccialetto sia autentico (firma valida)
 * - Se ok, ti lascia passare; se no, ti blocca
 * 
 * CONCETTO: OncePerRequestFilter
 * 
 * Garantisce che il filtro viene eseguito UNA SOLA VOLTA per richiesta.
 * Senza questo, in alcuni casi Spring potrebbe eseguirlo più volte
 * (es. quando c'è un forward interno).
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UtenteDAO utenteDAO;

    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils, UtenteDAO utenteDAO) {
        this.jwtUtils = jwtUtils;
        this.utenteDAO = utenteDAO;
    }

    /**
     * Metodo principale del filtro — eseguito per OGNI richiesta HTTP.
     * 
     * FLUSSO:
     * 1. Legge l'header "Authorization" dalla richiesta
     * 2. Se presente e inizia con "Bearer ", estrae il token
     * 3. Valida il token (firma + scadenza)
     * 4. Estrae l'email dal token
     * 5. Cerca l'utente nel database
     * 6. Imposta l'autenticazione nel SecurityContext
     * 7. La richiesta continua verso il controller
     * 
     * Se il token non c'è o non è valido, la richiesta prosegue
     * MA senza autenticazione → il SecurityConfig la bloccherà con 401.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Leggi l'header Authorization
        // Il frontend invia: Authorization: Bearer eyJhbGciOiJI...
        String authHeader = request.getHeader("Authorization");

        // 2. Se non c'è o non è un Bearer token, passa al filtro successivo
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Estrai il token (rimuovi il prefisso "Bearer ")
        String token = authHeader.substring(7);  // "Bearer ".length() == 7

        // 4. Valida il token
        if (jwtUtils.validateToken(token)) {
            // 5. Estrai l'email dal token
            String email = jwtUtils.getEmailFromToken(token);

            try {
                // 6. Cerca l'utente nel database
                Utente utente = utenteDAO.getUtenteByEmail(email);

                if (utente != null) {
                    // 7. Crea l'oggetto di autenticazione per Spring Security
                    //
                    // UsernamePasswordAuthenticationToken contiene:
                    // - principal: l'utente autenticato
                    // - credentials: null (non servono, abbiamo già il token)
                    // - authorities: i permessi (es: ROLE_ADMIN, ROLE_SOCIO)
                    //
                    // Le authorities permettono di proteggere endpoint per ruolo:
                    //   .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    var authentication = new UsernamePasswordAuthenticationToken(
                            utente,                                              // CHI è
                            null,                                                // credenziali (non servono)
                            List.of(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo().name()))
                    );

                    // 8. Imposta l'autenticazione nel SecurityContext
                    // Da questo momento, Spring Security sa che c'è un utente autenticato.
                    // I controller possono accedervi con SecurityContextHolder.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Se il DB non è raggiungibile, la richiesta prosegue senza autenticazione
                // → verrà bloccata dal SecurityConfig con 401
                System.err.println("Errore nel filtro JWT: " + e.getMessage());
            }
        }

        // 9. Passa al filtro successivo (o al controller se è l'ultimo filtro)
        filterChain.doFilter(request, response);
    }
}
