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
     * Registra un nuovo utente nel sistema.
     * Valida i dati in input, salva l'utente nel database e crea una sessione.
     * 
     * @param nuovoUtente l'oggetto Utente da registrare
     * @return l'ID generato per il nuovo utente
     * @throws AuthenticationException se i dati non sono validi o si verifica un
     *                                 errore
     */
    public Integer registrazione(Utente nuovoUtente) throws AuthenticationException {
        return registrazione(nuovoUtente, true);
    }

    /**
     * Registra un nuovo utente nel sistema senza creare una sessione.
     * Utile quando un admin crea utenti - non vogliamo cambiare la sessione
     * corrente.
     * 
     * @param nuovoUtente l'oggetto Utente da registrare
     * @return l'ID generato per il nuovo utente
     * @throws AuthenticationException se i dati non sono validi o si verifica un
     *                                 errore
     */
    public Integer registrazioneSenzaSessione(Utente nuovoUtente) throws AuthenticationException {
        return registrazione(nuovoUtente, false);
    }

    /**
     * Registra un nuovo utente nel sistema.
     * Valida i dati in input e salva l'utente nel database.
     * Se creaSessione è true, crea anche una sessione per l'utente.
     * 
     * @param nuovoUtente  l'oggetto Utente da registrare
     * @param creaSessione se true, crea una sessione per l'utente dopo la
     *                     registrazione
     * @return l'ID generato per il nuovo utente
     * @throws AuthenticationException se i dati non sono validi o si verifica un
     *                                 errore
     */
    private Integer registrazione(Utente nuovoUtente, boolean creaSessione) throws AuthenticationException {
        // Validazione input
        if (nuovoUtente == null) {
            throw new AuthenticationException("I dati dell'utente non possono essere nulli");
        }
        if (nuovoUtente.getNome() == null || nuovoUtente.getNome().trim().isEmpty()) {
            throw new AuthenticationException("Il nome non può essere vuoto");
        }
        if (nuovoUtente.getCognome() == null || nuovoUtente.getCognome().trim().isEmpty()) {
            throw new AuthenticationException("Il cognome non può essere vuoto");
        }
        if (nuovoUtente.getEmail() == null || nuovoUtente.getEmail().trim().isEmpty()) {
            throw new AuthenticationException("L'email non può essere vuota");
        }
        if (nuovoUtente.getPassword() == null || nuovoUtente.getPassword().isEmpty()) {
            throw new AuthenticationException("La password non può essere vuota");
        }
        if (nuovoUtente.getRuolo() == null) {
            throw new AuthenticationException("Il ruolo non può essere nullo");
        }

        try {
            // Salva l'utente nel database
            Integer idGenerato = utenteDAO.registrazione(nuovoUtente);

            // Crea la sessione solo se richiesto
            if (creaSessione) {
                SessionManager sessionManager = SessionManager.getInstance();
                String sessionId = sessionManager.createSession(nuovoUtente);
                System.out.println("Registrazione completata per: " + nuovoUtente.getEmail() +
                        " (ID: " + idGenerato + ", Session ID: " + sessionId + ")");
            } else {
                System.out.println("Utente creato: " + nuovoUtente.getEmail() +
                        " (ID: " + idGenerato + ") - senza sessione");
            }

            return idGenerato;

        } catch (SQLException e) {
            // Controlla se è un errore di email duplicata (violazione unique constraint)
            if (e.getMessage() != null && e.getMessage().contains("duplicate key")
                    || e.getMessage().contains("unique constraint")) {
                throw new AuthenticationException("L'email è già registrata nel sistema", e);
            }
            throw new AuthenticationException("Errore durante la registrazione: " + e.getMessage(), e);
        }
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
            System.out.println();
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
            System.out.println("\n" + "Logout effettuato per: " + utente.getEmail());
            System.out.println();

        }

        return success;
    }

    public boolean deleteUtente(Integer id) throws AuthenticationException {

        if (id == null) {
            throw new AuthenticationException("L'ID dell'utente non può essere vuoto");
        }

        try {

            boolean success = utenteDAO.deleteUtente(id);
            if (success) {
                System.out.println("Cancellazione effettuata per l'utente con ID: " + id);

            }

            return success;
        } catch (SQLException e) {
            throw new AuthenticationException("Errore durante la cancellazione dell'utente " + e.getMessage(), e);
        }

    }
}
