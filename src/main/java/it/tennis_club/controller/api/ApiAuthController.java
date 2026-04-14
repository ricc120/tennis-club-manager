package it.tennis_club.controller.api;

import it.tennis_club.business_logic.AuthService;
import it.tennis_club.business_logic.AuthenticationException;
import it.tennis_club.domain_model.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller per l'autenticazione — login via JSON.
 * 
 * CONCETTO CHIAVE: @RequestBody
 * 
 * Nei controller Thymeleaf (AuthController) usi @RequestParam:
 *   il browser invia i dati come "form data" (email=xxx&password=yyy)
 * 
 * Nei REST controller usi @RequestBody:
 *   il frontend invia i dati come JSON nel CORPO della richiesta
 *   {"email": "mario@email.com", "password": "secret123"}
 * 
 * Jackson deserializza automaticamente il JSON nel DTO (LoginRequest).
 * È il processo inverso della serializzazione:
 *   Serializzazione:     Utente → JSON  (risposta al frontend)
 *   Deserializzazione:   JSON → LoginRequest  (richiesta dal frontend)
 * 
 * CONFRONTO:
 *   AuthController (@Controller):
 *     @PostMapping("/login")
 *     public String login(@RequestParam String email, @RequestParam String password)
 *     → riceve form data, restituisce redirect a pagina HTML
 * 
 *   ApiAuthController (@RestController):
 *     @PostMapping("/api/auth/login")
 *     public ResponseEntity<?> login(@RequestBody LoginRequest request)
 *     → riceve JSON, restituisce JSON
 */
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthService authService;

    @Autowired
    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login — Verifica le credenziali e restituisce l'utente.
     * 
     * Richiesta (dal frontend):
     *   POST /api/auth/login
     *   Content-Type: application/json
     *   {"email": "mario@email.com", "password": "secret123"}
     * 
     * Risposta (successo):
     *   HTTP 200
     *   {"id": 1, "nome": "Mario", "cognome": "Rossi", "email": "mario@email.com", "ruolo": "ADMIN"}
     *   (nota: nessun campo "password" grazie a @JsonIgnore!)
     * 
     * Risposta (errore):
     *   HTTP 401 Unauthorized
     *   {"errore": "Credenziali non valide"}
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Utente utente = authService.login(request.getEmail(), request.getPassword());

            if (utente != null) {
                // Login riuscito — restituisce l'utente come JSON
                // @JsonIgnore su password garantisce che non venga inclusa
                return ResponseEntity.ok(utente);
            } else {
                // Credenziali non valide (email o password errate)
                return ResponseEntity
                        .status(401)
                        .body("{\"errore\": \"Credenziali non valide\"}");
            }

        } catch (AuthenticationException e) {
            // Errore di validazione (email vuota, etc.)
            return ResponseEntity
                    .status(400)
                    .body("{\"errore\": \"" + e.getMessage() + "\"}");
        }
    }
}
