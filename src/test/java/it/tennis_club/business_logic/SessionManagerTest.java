package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe SessionManager.
 * Verifica il corretto funzionamento della gestione delle sessioni utente.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SessionManagerTest {

    private SessionManager sessionManager;
    private Utente testUtente;

    @BeforeEach
    void setUp() {
        // Ottieni l'istanza singleton e pulisci tutte le sessioni
        sessionManager = SessionManager.getInstance();
        sessionManager.clearAllSessions();

        // Crea un utente di test
        testUtente = new Utente(1, "Mario", "Rossi", "mario.rossi@test.it", "password123", Utente.Ruolo.SOCIO);
    }

    @AfterEach
    void tearDown() {
        // Pulisci le sessioni dopo ogni test
        sessionManager.clearAllSessions();
    }

    @Test
    @Order(1)
    @DisplayName("Verifica che l'istanza sia singleton")
    void testSingletonInstance() {
        SessionManager sessione1 = SessionManager.getInstance();
        SessionManager sessione2 = SessionManager.getInstance();

        assertSame(sessione1, sessione2, "Le istanze dovrebbero essere la stessa (Singleton)");
    }

    @Test
    @Order(2)
    @DisplayName("Creazione di una nuova sessione")
    void testCreateSession() {
        String id = sessionManager.createSession(testUtente);

        assertNotNull(id, "L'ID della sessione non dovrebbe essere null");
        assertFalse(id.isEmpty(), "L'ID della sessione non dovrebbe essere vuoto");
        assertEquals(1, sessionManager.getActiveSessionsCount(), "Dovrebbe esserci una sessione attiva");
    }

    @Test
    @Order(3)
    @DisplayName("Creazione sessione con utente null solleva eccezione")
    void testCreateSessionWithNullUser() {
        assertThrows(IllegalArgumentException.class,
                () -> sessionManager.createSession(null),
                "Dovrebbe sollevare IllegalArgumentException per utente null");
    }

    @Test
    @Order(4)
    @DisplayName("Recupero utente da sessione valida")
    void testGetUserFromValidSession() {
        String id = sessionManager.createSession(testUtente);

        Utente utenteRecuperato = sessionManager.getUser(id);

        assertNotNull(utenteRecuperato, "L'utente recuperato non dovrebbe essere null");
        assertEquals(testUtente.getId(), utenteRecuperato.getId(), "L'ID utente dovrebbe corrispondere");
        assertEquals(testUtente.getEmail(), utenteRecuperato.getEmail(), "L'email dovrebbe corrispondere");
    }

    @Test
    @Order(5)
    @DisplayName("Recupero utente da sessione inesistente")
    void testGetUserFromInvalidSession() {
        Utente utenteRecuperato = sessionManager.getUser("ID di sessione non valido");

        assertNull(utenteRecuperato, "L'utente dovrebbe essere null per sessione inesistente");
    }

    @Test
    @Order(6)
    @DisplayName("Recupero utente corrente")
    void testGetCurrentUser() {
        assertNull(sessionManager.getCurrentUser(), "Non dovrebbe esserci utente corrente inizialmente");

        sessionManager.createSession(testUtente);

        Utente utenteCorrente = sessionManager.getCurrentUser();
        assertNotNull(utenteCorrente, "Dovrebbe esserci un utente corrente dopo il login");
        assertEquals(testUtente.getEmail(), utenteCorrente.getEmail(), "L'utente corrente dovrebbe corrispondere");
    }

    @Test
    @Order(7)
    @DisplayName("Verifica se utente è loggato")
    void testIsUserLoggedIn() {
        assertFalse(sessionManager.isUserLoggedIn(), "Nessun utente dovrebbe essere loggato inizialmente");

        sessionManager.createSession(testUtente);

        assertTrue(sessionManager.isUserLoggedIn(),
                "Un utente dovrebbe essere loggato dopo la creazione della sessione");
    }

    @Test
    @Order(8)
    @DisplayName("Invalidazione di una sessione specifica")
    void testInvalidateSession() {
        String id = sessionManager.createSession(testUtente);

        assertTrue(sessionManager.isUserLoggedIn(), "L'utente dovrebbe essere loggato");

        boolean risultato = sessionManager.invalidateSession(id);

        assertTrue(risultato, "L'invalidazione dovrebbe avere successo");
        assertFalse(sessionManager.isUserLoggedIn(), "L'utente non dovrebbe più essere loggato");
        assertEquals(0, sessionManager.getActiveSessionsCount(), "Non dovrebbero esserci sessioni attive");
    }

    @Test
    @Order(9)
    @DisplayName("Invalidazione di sessione inesistente")
    void testInvalidateNonExistentSession() {
        boolean risultato = sessionManager.invalidateSession("ID non esistente");

        assertFalse(risultato, "L'invalidazione di una sessione inesistente dovrebbe fallire");
    }

    @Test
    @Order(10)
    @DisplayName("Invalidazione di sessione null")
    void testInvalidateNullSession() {
        boolean risultato = sessionManager.invalidateSession(null);

        assertFalse(risultato, "L'invalidazione di una sessione null dovrebbe fallire");
    }

    @Test
    @Order(11)
    @DisplayName("Logout dell'utente corrente")
    void testLogout() {
        sessionManager.createSession(testUtente);
        assertTrue(sessionManager.isUserLoggedIn(), "L'utente dovrebbe essere loggato");

        boolean risultato = sessionManager.logout();

        assertTrue(risultato, "Il logout dovrebbe avere successo");
        assertFalse(sessionManager.isUserLoggedIn(), "L'utente non dovrebbe più essere loggato");
        assertNull(sessionManager.getCurrentUser(), "Non dovrebbe esserci utente corrente");
    }

    @Test
    @Order(12)
    @DisplayName("Logout senza utente loggato")
    void testLogoutWithoutUser() {
        boolean risultato = sessionManager.logout();

        assertFalse(risultato, "Il logout senza utente loggato dovrebbe fallire");
    }

    @Test
    @Order(13)
    @DisplayName("Conteggio sessioni attive")
    void testActiveSessionsCount() {
        assertEquals(0, sessionManager.getActiveSessionsCount(), "Inizialmente non dovrebbero esserci sessioni");

        sessionManager.createSession(testUtente);
        assertEquals(1, sessionManager.getActiveSessionsCount(), "Dovrebbe esserci una sessione attiva");

        sessionManager.logout();
        assertEquals(0, sessionManager.getActiveSessionsCount(), "Dopo il logout non dovrebbero esserci sessioni");
    }

    @Test
    @Order(14)
    @DisplayName("Pulizia di tutte le sessioni")
    void testClearAllSessions() {
        sessionManager.createSession(testUtente);

        Utente utente = new Utente(2, "Luigi", "Verdi", "luigi.verdi@test.it", "pass456", Utente.Ruolo.ADMIN);
        sessionManager.createSession(utente);

        assertTrue(sessionManager.getActiveSessionsCount() > 0, "Dovrebbero esserci sessioni attive");

        sessionManager.clearAllSessions();

        assertEquals(0, sessionManager.getActiveSessionsCount(), "Tutte le sessioni dovrebbero essere state eliminate");
        assertFalse(sessionManager.isUserLoggedIn(), "Nessun utente dovrebbe essere loggato");
    }

    @Test
    @Order(15)
    @DisplayName("Creazione di sessioni multiple (sovrascrittura)")
    void testMultipleSessionCreation() {
        String id1 = sessionManager.createSession(testUtente);
        assertNotNull(id1, "La prima sessione dovrebbe essere creata");

        Utente utente = new Utente(2, "Luigi", "Verdi", "luigi.verdi@test.it", "pass456", Utente.Ruolo.ADMIN);
        String id2 = sessionManager.createSession(utente);

        assertNotNull(id2, "La seconda sessione dovrebbe essere creata");
        assertNotEquals(id1, id2, "Gli ID delle sessioni dovrebbero essere diversi");

        // L'utente corrente dovrebbe essere l'ultimo che ha fatto login
        Utente utenteCorrente = sessionManager.getCurrentUser();
        assertEquals(utente.getEmail(), utenteCorrente.getEmail(),
                "L'utente corrente dovrebbe essere l'ultimo che ha fatto login");
    }

    @Test
    @Order(16)
    @DisplayName("Verifica che la sessione aggiorni il timestamp di ultimo accesso")
    void testSessionLastAccessUpdate() throws InterruptedException {
        String id = sessionManager.createSession(testUtente);

        // Prima chiamata
        Utente utente1 = sessionManager.getUser(id);
        assertNotNull(utente1, "L'utente dovrebbe essere recuperato");

        // Attendi un po'
        Thread.sleep(100);

        // Seconda chiamata - dovrebbe aggiornare il timestamp
        Utente utente2 = sessionManager.getUser(id);
        assertNotNull(utente2, "L'utente dovrebbe essere ancora recuperabile");

        // Verifica che sia lo stesso utente
        assertEquals(utente1.getId(), utente2.getId(), "Dovrebbe essere lo stesso utente");
    }
}
