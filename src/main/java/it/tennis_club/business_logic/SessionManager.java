package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gestore delle sessioni utente che implementa il pattern Singleton.
 * Mantiene traccia degli utenti autenticati e delle loro sessioni attive.
 * 
 * Questa classe fornisce funzionalità per:
 * - Creare nuove sessioni dopo il login
 * - Recuperare l'utente corrente dalla sessione
 * - Invalidare sessioni (logout)
 * - Verificare la validità delle sessioni
 */
public class SessionManager {

    // Istanza singleton
    private static SessionManager instance;

    // Mappa che associa sessionId all'oggetto Session
    private final Map<String, Session> sessions;

    // Session ID dell'utente corrente (per applicazioni single-user)
    private String currentSessionId;

    /**
     * Costruttore privato per implementare il pattern Singleton.
     */
    private SessionManager() {
        this.sessions = new HashMap<>();
        this.currentSessionId = null;
    }

    /**
     * Ottiene l'istanza singleton del SessionManager.
     * 
     * @return l'unica istanza di SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Crea una nuova sessione per un utente autenticato.
     * 
     * @param utente l'utente per cui creare la sessione
     * @return l'ID della sessione creata
     */
    public String createSession(Utente utente) {
        if (utente == null) {
            throw new IllegalArgumentException("L'utente non può essere null");
        }

        // Genera un ID univoco per la sessione
        String sessionId = UUID.randomUUID().toString();

        // Crea la sessione
        Session session = new Session(sessionId, utente);

        // Salva la sessione
        sessions.put(sessionId, session);

        // Imposta come sessione corrente (per applicazioni single-user)
        currentSessionId = sessionId;

        return sessionId;
    }

    /**
     * Ottiene l'utente associato a una sessione specifica.
     * 
     * @param sessionId l'ID della sessione
     * @return l'utente associato alla sessione, o null se la sessione non esiste o
     *         è scaduta
     */
    public Utente getUser(String sessionId) {
        Session session = sessions.get(sessionId);

        if (session == null) {
            return null;
        }

        // Verifica se la sessione è ancora valida
        if (session.isExpired()) {
            // Rimuovi la sessione scaduta
            sessions.remove(sessionId);
            if (sessionId.equals(currentSessionId)) {
                currentSessionId = null;
            }
            return null;
        }

        // Aggiorna il timestamp dell'ultimo accesso
        session.updateLastAccess();

        return session.getUtente();
    }

    /**
     * Ottiene l'utente corrente (per applicazioni single-user).
     * 
     * @return l'utente corrente, o null se nessun utente è loggato
     */
    public Utente getCurrentUser() {
        if (currentSessionId == null) {
            return null;
        }
        return getUser(currentSessionId);
    }

    /**
     * Verifica se esiste una sessione attiva.
     * 
     * @return true se c'è un utente loggato, false altrimenti
     */
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    /**
     * Invalida una sessione specifica (logout).
     * 
     * @param sessionId l'ID della sessione da invalidare
     * @return true se la sessione è stata invalidata, false se non esisteva
     */
    public boolean invalidateSession(String sessionId) {
        if (sessionId == null) {
            return false;
        }

        Session removed = sessions.remove(sessionId);

        if (sessionId.equals(currentSessionId)) {
            currentSessionId = null;
        }

        return removed != null;
    }

    /**
     * Invalida la sessione corrente (logout dell'utente corrente).
     * 
     * @return true se la sessione è stata invalidata, false se non c'era nessuna
     *         sessione attiva
     */
    public boolean logout() {
        if (currentSessionId == null) {
            return false;
        }

        String sessionToRemove = currentSessionId;
        currentSessionId = null;
        sessions.remove(sessionToRemove);

        return true;
    }

    /**
     * Ottiene il numero di sessioni attive.
     * 
     * @return il numero di sessioni attive
     */
    public int getActiveSessionsCount() {
        // Rimuovi le sessioni scadute prima di contare
        cleanExpiredSessions();
        return sessions.size();
    }

    /**
     * Pulisce tutte le sessioni scadute.
     */
    private void cleanExpiredSessions() {
        sessions.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    /**
     * Pulisce tutte le sessioni (utile per testing).
     */
    public void clearAllSessions() {
        sessions.clear();
        currentSessionId = null;
    }

    /**
     * Classe interna che rappresenta una sessione utente.
     */
    private static class Session {
        private final String sessionId;
        private final Utente utente;
        private final LocalDateTime createdAt;
        private LocalDateTime lastAccessedAt;

        // Timeout della sessione in minuti (default: 30 minuti)
        private static final long SESSION_TIMEOUT_MINUTES = 30;

        public Session(String sessionId, Utente utente) {
            this.sessionId = sessionId;
            this.utente = utente;
            this.createdAt = LocalDateTime.now();
            this.lastAccessedAt = LocalDateTime.now();
        }

        public String getSessionId() {
            return sessionId;
        }

        public Utente getUtente() {
            return utente;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getLastAccessedAt() {
            return lastAccessedAt;
        }

        public void updateLastAccess() {
            this.lastAccessedAt = LocalDateTime.now();
        }

        /**
         * Verifica se la sessione è scaduta.
         * 
         * @return true se la sessione è scaduta, false altrimenti
         */
        public boolean isExpired() {
            LocalDateTime expirationTime = lastAccessedAt.plusMinutes(SESSION_TIMEOUT_MINUTES);
            return LocalDateTime.now().isAfter(expirationTime);
        }
    }
}
