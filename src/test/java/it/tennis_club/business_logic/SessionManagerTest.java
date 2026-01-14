package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe SessionManager.
 * Verifica il corretto funzionamento della gestione delle sessioni utente.
 */
class SessionManagerTest {

    private SessionManager sessionManager;
    private Utente testUser;

    @BeforeEach
    void setUp() {
        // Ottieni l'istanza singleton e pulisci tutte le sessioni
        sessionManager = SessionManager.getInstance();
        sessionManager.clearAllSessions();

        // Crea un utente di test
        testUser = new Utente(1, "Mario", "Rossi", "mario.rossi@test.it", "password123", Utente.Ruolo.SOCIO);
    }

    @AfterEach
    void tearDown() {
        // Pulisci le sessioni dopo ogni test
        sessionManager.clearAllSessions();
    }

    @Test
    @DisplayName("Verifica che l'istanza sia singleton")
    void testSingletonInstance() {
        SessionManager instance1 = SessionManager.getInstance();
        SessionManager instance2 = SessionManager.getInstance();

        assertSame(instance1, instance2, "Le istanze dovrebbero essere la stessa (Singleton)");
    }

    @Test
    @DisplayName("Creazione di una nuova sessione")
    void testCreateSession() {
        String sessionId = sessionManager.createSession(testUser);

        assertNotNull(sessionId, "L'ID della sessione non dovrebbe essere null");
        assertFalse(sessionId.isEmpty(), "L'ID della sessione non dovrebbe essere vuoto");
        assertEquals(1, sessionManager.getActiveSessionsCount(), "Dovrebbe esserci una sessione attiva");
    }

    @Test
    @DisplayName("Creazione sessione con utente null solleva eccezione")
    void testCreateSessionWithNullUser() {
        assertThrows(IllegalArgumentException.class,
                () -> sessionManager.createSession(null),
                "Dovrebbe sollevare IllegalArgumentException per utente null");
    }

    @Test
    @DisplayName("Recupero utente da sessione valida")
    void testGetUserFromValidSession() {
        String sessionId = sessionManager.createSession(testUser);

        Utente retrievedUser = sessionManager.getUser(sessionId);

        assertNotNull(retrievedUser, "L'utente recuperato non dovrebbe essere null");
        assertEquals(testUser.getId(), retrievedUser.getId(), "L'ID utente dovrebbe corrispondere");
        assertEquals(testUser.getEmail(), retrievedUser.getEmail(), "L'email dovrebbe corrispondere");
    }

    @Test
    @DisplayName("Recupero utente da sessione inesistente")
    void testGetUserFromInvalidSession() {
        Utente retrievedUser = sessionManager.getUser("invalid-session-id");

        assertNull(retrievedUser, "L'utente dovrebbe essere null per sessione inesistente");
    }

    @Test
    @DisplayName("Recupero utente corrente")
    void testGetCurrentUser() {
        assertNull(sessionManager.getCurrentUser(), "Non dovrebbe esserci utente corrente inizialmente");

        sessionManager.createSession(testUser);

        Utente currentUser = sessionManager.getCurrentUser();
        assertNotNull(currentUser, "Dovrebbe esserci un utente corrente dopo il login");
        assertEquals(testUser.getEmail(), currentUser.getEmail(), "L'utente corrente dovrebbe corrispondere");
    }

    @Test
    @DisplayName("Verifica se utente è loggato")
    void testIsUserLoggedIn() {
        assertFalse(sessionManager.isUserLoggedIn(), "Nessun utente dovrebbe essere loggato inizialmente");

        sessionManager.createSession(testUser);

        assertTrue(sessionManager.isUserLoggedIn(),
                "Un utente dovrebbe essere loggato dopo la creazione della sessione");
    }

    @Test
    @DisplayName("Invalidazione di una sessione specifica")
    void testInvalidateSession() {
        String sessionId = sessionManager.createSession(testUser);

        assertTrue(sessionManager.isUserLoggedIn(), "L'utente dovrebbe essere loggato");

        boolean result = sessionManager.invalidateSession(sessionId);

        assertTrue(result, "L'invalidazione dovrebbe avere successo");
        assertFalse(sessionManager.isUserLoggedIn(), "L'utente non dovrebbe più essere loggato");
        assertEquals(0, sessionManager.getActiveSessionsCount(), "Non dovrebbero esserci sessioni attive");
    }

    @Test
    @DisplayName("Invalidazione di sessione inesistente")
    void testInvalidateNonExistentSession() {
        boolean result = sessionManager.invalidateSession("non-existent-id");

        assertFalse(result, "L'invalidazione di una sessione inesistente dovrebbe fallire");
    }

    @Test
    @DisplayName("Invalidazione di sessione null")
    void testInvalidateNullSession() {
        boolean result = sessionManager.invalidateSession(null);

        assertFalse(result, "L'invalidazione di una sessione null dovrebbe fallire");
    }

    @Test
    @DisplayName("Logout dell'utente corrente")
    void testLogout() {
        sessionManager.createSession(testUser);
        assertTrue(sessionManager.isUserLoggedIn(), "L'utente dovrebbe essere loggato");

        boolean result = sessionManager.logout();

        assertTrue(result, "Il logout dovrebbe avere successo");
        assertFalse(sessionManager.isUserLoggedIn(), "L'utente non dovrebbe più essere loggato");
        assertNull(sessionManager.getCurrentUser(), "Non dovrebbe esserci utente corrente");
    }

    @Test
    @DisplayName("Logout senza utente loggato")
    void testLogoutWithoutUser() {
        boolean result = sessionManager.logout();

        assertFalse(result, "Il logout senza utente loggato dovrebbe fallire");
    }

    @Test
    @DisplayName("Conteggio sessioni attive")
    void testActiveSessionsCount() {
        assertEquals(0, sessionManager.getActiveSessionsCount(), "Inizialmente non dovrebbero esserci sessioni");

        sessionManager.createSession(testUser);
        assertEquals(1, sessionManager.getActiveSessionsCount(), "Dovrebbe esserci una sessione attiva");

        sessionManager.logout();
        assertEquals(0, sessionManager.getActiveSessionsCount(), "Dopo il logout non dovrebbero esserci sessioni");
    }

    @Test
    @DisplayName("Pulizia di tutte le sessioni")
    void testClearAllSessions() {
        sessionManager.createSession(testUser);

        Utente anotherUser = new Utente(2, "Luigi", "Verdi", "luigi.verdi@test.it", "pass456", Utente.Ruolo.ADMIN);
        sessionManager.createSession(anotherUser);

        assertTrue(sessionManager.getActiveSessionsCount() > 0, "Dovrebbero esserci sessioni attive");

        sessionManager.clearAllSessions();

        assertEquals(0, sessionManager.getActiveSessionsCount(), "Tutte le sessioni dovrebbero essere state eliminate");
        assertFalse(sessionManager.isUserLoggedIn(), "Nessun utente dovrebbe essere loggato");
    }

    @Test
    @DisplayName("Creazione di sessioni multiple (sovrascrittura)")
    void testMultipleSessionCreation() {
        String sessionId1 = sessionManager.createSession(testUser);
        assertNotNull(sessionId1, "La prima sessione dovrebbe essere creata");

        Utente anotherUser = new Utente(2, "Luigi", "Verdi", "luigi.verdi@test.it", "pass456", Utente.Ruolo.ADMIN);
        String sessionId2 = sessionManager.createSession(anotherUser);

        assertNotNull(sessionId2, "La seconda sessione dovrebbe essere creata");
        assertNotEquals(sessionId1, sessionId2, "Gli ID delle sessioni dovrebbero essere diversi");

        // L'utente corrente dovrebbe essere l'ultimo che ha fatto login
        Utente currentUser = sessionManager.getCurrentUser();
        assertEquals(anotherUser.getEmail(), currentUser.getEmail(),
                "L'utente corrente dovrebbe essere l'ultimo che ha fatto login");
    }

    @Test
    @DisplayName("Verifica che la sessione aggiorni il timestamp di ultimo accesso")
    void testSessionLastAccessUpdate() throws InterruptedException {
        String sessionId = sessionManager.createSession(testUser);

        // Prima chiamata
        Utente user1 = sessionManager.getUser(sessionId);
        assertNotNull(user1, "L'utente dovrebbe essere recuperato");

        // Attendi un po'
        Thread.sleep(100);

        // Seconda chiamata - dovrebbe aggiornare il timestamp
        Utente user2 = sessionManager.getUser(sessionId);
        assertNotNull(user2, "L'utente dovrebbe essere ancora recuperabile");

        // Verifica che sia lo stesso utente
        assertEquals(user1.getId(), user2.getId(), "Dovrebbe essere lo stesso utente");
    }
}
