package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.UtenteDAO;

import java.sql.SQLException;

/**
 * Servizio di autenticazione che gestisce la logica di business
 * relativa al login e alla gestione delle sessioni utente.
 * 
 * Questo servizio funge da intermediario tra il livello di presentazione (view)
 * e il livello di accesso ai dati (DAO).
 */
public class AuthService {

    private final UtenteDAO utenteDAO;

    /**
     * Costruttore che inizializza il DAO.
     */
    public AuthService() {
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Costruttore per dependency injection (utile per i test).
     * 
     * @param utenteDAO istanza del DAO da utilizzare
     */
    public AuthService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /**
     * Autentica un utente verificando le sue credenziali.
     * 
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @return l'oggetto Utente se l'autenticazione ha successo, null altrimenti
     * @throws AuthenticationException se si verifica un errore durante
     *                                 l'autenticazione
     */
    public Utente login(String email, String password) throws AuthenticationException {
        // Validazione input
        if (email == null || email.trim().isEmpty()) {
            throw new AuthenticationException("L'email non può essere vuota");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("La password non può essere vuota");
        }

        try {
            // Delega al DAO la ricerca dell'utente
            Utente utente = utenteDAO.login(email.trim(), password);

            if (utente == null) {
                // Credenziali non valide
                return null;
            }

            // Crea una sessione per l'utente autenticato
            SessionManager sessionManager = SessionManager.getInstance();
            String sessionId = sessionManager.createSession(utente);

            // Log dell'accesso (opzionale)
            System.out.println("Sessione creata per utente: " + utente.getEmail() +
                    " (Session ID: " + sessionId + ")");

            return utente;

        } catch (SQLException e) {
            // Trasforma l'eccezione SQL in un'eccezione di business
            throw new AuthenticationException("Errore durante l'autenticazione: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica se un utente ha un determinato ruolo.
     * 
     * @param utente l'utente da verificare
     * @param ruolo  il ruolo richiesto
     * @return true se l'utente ha il ruolo specificato, false altrimenti
     */
    public boolean hasRole(Utente utente, Utente.Ruolo ruolo) {
        if (utente == null || ruolo == null) {
            return false;
        }
        return utente.getRuolo() == ruolo;
    }

    /**
     * Verifica se un utente è un amministratore.
     * 
     * @param utente l'utente da verificare
     * @return true se l'utente è un admin, false altrimenti
     */
    public boolean isAdmin(Utente utente) {
        return hasRole(utente, Utente.Ruolo.ADMIN);
    }

    /**
     * Effettua il logout di un utente.
     * Invalida la sessione corrente e pulisce le informazioni di autenticazione.
     * 
     * @param utente l'utente che sta effettuando il logout
     * @return true se il logout è stato effettuato con successo, false altrimenti
     */
    public boolean logout(Utente utente) {
        if (utente == null) {
            return false;
        }

        // Invalida la sessione corrente
        SessionManager sessionManager = SessionManager.getInstance();
        boolean success = sessionManager.logout();

        if (success) {
            System.out.println("Logout effettuato per: " + utente.getEmail());
        }

        return success;
    }
}
