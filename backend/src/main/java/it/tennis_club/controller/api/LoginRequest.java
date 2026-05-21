package it.tennis_club.controller.api;

/**
 * DTO (Data Transfer Object) per la richiesta di login.
 * 
 * CONCETTO: DTO vs Domain Model
 * 
 * Il domain model "Utente" ha TUTTI i campi (id, nome, cognome, email, password, ruolo).
 * Ma per il login il frontend invia SOLO email e password.
 * 
 * Un DTO è un oggetto "leggero" che rappresenta solo i dati necessari
 * per una specifica operazione. In Spring Boot:
 * 
 *   1. Il frontend invia JSON: {"email": "mario@email.com", "password": "123"}
 *   2. Jackson lo deserializza in questo DTO (unmarshaling)
 *   3. Il controller usa il DTO per chiamare il service
 * 
 * Perché non usare direttamente Utente?
 * - Sicurezza: non vogliamo che il frontend possa impostare "ruolo": "ADMIN"
 * - Chiarezza: il DTO documenta esattamente cosa si aspetta l'endpoint
 */
public class LoginRequest {
    private String email;
    private String password;

    // Costruttore vuoto necessario per Jackson (deserializzazione)
    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
