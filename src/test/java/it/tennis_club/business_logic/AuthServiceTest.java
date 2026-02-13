package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il servizio di autenticazione.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {

    private AuthService authService;
    private List<Integer> idsUtentiCreati;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        idsUtentiCreati = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        // Rimuove tutti gli utenti creati durante i test
        for (Integer id : idsUtentiCreati) {
            try {
                authService.eliminaUtente(id);
            } catch (AuthenticationException e) {
                System.err.println("Errore durante la pulizia dell'utente ID " + id + ": " + e.getMessage());
            }
        }
        // Pulisce la sessione
        SessionManager.getInstance().logout();
    }

    // ===== TEST REGISTRAZIONE =====

    @Test
    @Order(1)
    @DisplayName("Registrazione nuovo utente con successo")
    void testRegistrazioneSuccess() throws AuthenticationException {
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome("Test");
        nuovoUtente.setCognome("Service");
        nuovoUtente.setEmail("test.service@tennis.it");
        nuovoUtente.setPassword("password123");
        nuovoUtente.setRuolo(Utente.Ruolo.SOCIO);

        Integer idGenerato = authService.registrazione(nuovoUtente);
        idsUtentiCreati.add(idGenerato);

        // Verifica che la sessione sia stata creata
        assertTrue(SessionManager.getInstance().isUserLoggedIn(),
                "L'utente dovrebbe essere loggato dopo la registrazione");
        assertEquals(nuovoUtente, SessionManager.getInstance().getCurrentUser(),
                "L'utente nella sessione dovrebbe essere quello registrato");

        System.out.println("Utente test " + idGenerato + " registrato con successo");
    }

    @Test
    @Order(2)
    @DisplayName("Registrazione fallisce con utente null")
    void testRegistrazioneUtenteNull() {
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.registrazione(null),
                "Dovrebbe lanciare eccezione per utente null");

        assertTrue(exception.getMessage().contains("nulli"));
    }

    @Test
    @Order(3)
    @DisplayName("Registrazione fallisce con nome vuoto")
    void testRegistrazioneNomeVuoto() {
        Utente utente = new Utente();
        utente.setNome("");
        utente.setCognome("Test");
        utente.setEmail("test@email.it");
        utente.setPassword("password");
        utente.setRuolo(Utente.Ruolo.SOCIO);

        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.registrazione(utente));

        assertTrue(exception.getMessage().contains("nome"));
    }

    @Test
    @Order(4)
    @DisplayName("Registrazione fallisce con email vuota")
    void testRegistrazioneEmailVuota() {
        Utente utente = new Utente();
        utente.setNome("Test");
        utente.setCognome("Test");
        utente.setEmail("");
        utente.setPassword("password");
        utente.setRuolo(Utente.Ruolo.SOCIO);

        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.registrazione(utente));

        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    @Order(5)
    @DisplayName("Registrazione fallisce con password vuota")
    void testRegistrazionePasswordVuota() {
        Utente utente = new Utente();
        utente.setNome("Test");
        utente.setCognome("Test");
        utente.setEmail("test@email.it");
        utente.setPassword("");
        utente.setRuolo(Utente.Ruolo.SOCIO);

        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.registrazione(utente));

        assertTrue(exception.getMessage().contains("password"));
    }

    @Test
    @Order(6)
    @DisplayName("Registrazione fallisce con ruolo null")
    void testRegistrazioneRuoloNull() {
        Utente utente = new Utente();
        utente.setNome("Test");
        utente.setCognome("Test");
        utente.setEmail("test@email.it");
        utente.setPassword("password");
        utente.setRuolo(null);

        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.registrazione(utente));

        assertTrue(exception.getMessage().contains("ruolo"));
    }

    // ===== TEST LOGIN =====

    @Test
    @Order(7)
    @DisplayName("Login con credenziali corrette")
    void testLoginSuccessSocio() throws AuthenticationException {
        Utente utente = authService.login("socio@tennis.it", "socio123");

        assertNotNull(utente, "L'utente socio dovrebbe essere autenticato");
        assertEquals("Luigi", utente.getNome());
        assertEquals("Verdi", utente.getCognome());
        assertEquals(Utente.Ruolo.SOCIO, utente.getRuolo());
        assertFalse(authService.isAdmin(utente), "Il socio non dovrebbe essere un admin");
    }

    @Test
    @Order(8)
    @DisplayName("Login fallisce con password errata")
    void testLoginWrongPassword() throws AuthenticationException {
        Utente utente = authService.login("socio@tennis.it", "password_sbagliata");

        assertNull(utente, "Il login dovrebbe fallire con password errata");
    }

    @Test
    @Order(9)
    @DisplayName("Login fallisce con email inesistente")
    void testLoginUserNotFound() throws AuthenticationException {
        Utente utente = authService.login("nonexist@tennis.it", "password");

        assertNull(utente, "Il login dovrebbe fallire per utente inesistente");
    }

    @Test
    @Order(10)
    @DisplayName("Login lancia eccezione con email vuota")
    void testLoginEmptyEmail() {
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.login("", "password"),
                "Dovrebbe lanciare un'eccezione con email vuota");

        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    @Order(11)
    @DisplayName("Login lancia eccezione con password vuota")
    void testLoginEmptyPassword() {
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.login("admin@tennis.it", ""),
                "Dovrebbe lanciare un'eccezione con password vuota");

        assertTrue(exception.getMessage().contains("password"));
    }

    @Test
    @Order(12)
    @DisplayName("Login lancia eccezione con email null")
    void testLoginNullEmail() {
        assertThrows(
                AuthenticationException.class,
                () -> authService.login(null, "password"));
    }

    @Test
    @Order(13)
    @DisplayName("Verifica ruolo utente")
    void testHasRole() throws AuthenticationException {
        Utente socio = authService.login("socio@tennis.it", "socio123");

        assertTrue(authService.hasRole(socio, Utente.Ruolo.SOCIO));
        assertFalse(authService.hasRole(socio, Utente.Ruolo.ADMIN));
    }

    @Test
    @Order(14)
    @DisplayName("Logout non lancia eccezioni")
    void testLogout() throws AuthenticationException {
        Utente utente = authService.login("admin@tennis.it", "admin123");

        assertDoesNotThrow(() -> authService.logout(utente));
        assertDoesNotThrow(() -> authService.logout(null));
    }

    @Test
    @Order(15)
    @DisplayName("Verifica che un utente sia l'admin")
    void testIsAdmin() throws AuthenticationException {
        Utente utente = new Utente();
        utente.setNome("Admin");
        utente.setCognome("Test");
        utente.setEmail("testAdmin@email.it");
        utente.setPassword("password");
        utente.setRuolo(Utente.Ruolo.ADMIN);

        assertTrue(authService.isAdmin(utente), "L'utente dovrebbe essere un admin");
    }

    @Test
    @Order(16)
    @DisplayName("Verifica eliminazione di un utente")
    void testDeleteUtente() throws AuthenticationException {
        Utente utente = new Utente();
        utente.setNome("Utente");
        utente.setCognome("Test");
        utente.setEmail("test@email.it");
        utente.setPassword("password");
        utente.setRuolo(Utente.Ruolo.SOCIO);

        authService.registrazione(utente);

        assertTrue(authService.eliminaUtente(utente.getId()), "L'utente dovrebbe essere eliminato");
    }

}
