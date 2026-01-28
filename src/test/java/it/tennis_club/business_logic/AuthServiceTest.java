package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il servizio di autenticazione.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 */
class AuthServiceTest {

    private AuthService authService;
    private List<Integer> utentiCreatiIds;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        utentiCreatiIds = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        // Rimuove tutti gli utenti creati durante i test
        for (Integer id : utentiCreatiIds) {
            try {
                deleteUtenteById(id);
            } catch (SQLException e) {
                System.err.println("Errore durante la pulizia dell'utente ID " + id + ": " + e.getMessage());
            }
        }
        // Pulisce la sessione
        SessionManager.getInstance().logout();
    }

    private void deleteUtenteById(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "DELETE FROM utente WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } finally {
            if (statement != null) {
                statement.close();
            }
            ConnectionManager.closeConnection(connection);
        }
    }

    // ===== TEST REGISTRAZIONE =====

    @Test
    @DisplayName("Registrazione nuovo utente con successo")
    void testRegistrazioneSuccess() throws AuthenticationException {
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome("Test");
        nuovoUtente.setCognome("Service");
        nuovoUtente.setEmail("test.service@tennis.it");
        nuovoUtente.setPassword("password123");
        nuovoUtente.setRuolo(Utente.Ruolo.SOCIO);

        Integer idGenerato = authService.registrazione(nuovoUtente);
        utentiCreatiIds.add(idGenerato);

        assertNotNull(idGenerato, "L'ID generato non dovrebbe essere null");
        assertTrue(idGenerato > 0, "L'ID generato dovrebbe essere positivo");

        // Verifica che la sessione sia stata creata
        assertTrue(SessionManager.getInstance().isUserLoggedIn(),
                "L'utente dovrebbe essere loggato dopo la registrazione");
        assertEquals(nuovoUtente, SessionManager.getInstance().getCurrentUser(),
                "L'utente nella sessione dovrebbe essere quello registrato");
    }

    @Test
    @DisplayName("Registrazione fallisce con utente null")
    void testRegistrazioneUtenteNull() {
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.registrazione(null),
                "Dovrebbe lanciare eccezione per utente null");

        assertTrue(exception.getMessage().contains("nulli"));
    }

    @Test
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
    @DisplayName("Login con credenziali corrette (Admin)")
    void testLoginSuccessAdmin() throws AuthenticationException {
        Utente utente = authService.login("admin@tennis.it", "admin123");

        assertNotNull(utente, "L'utente admin dovrebbe essere autenticato");
        assertEquals("Mario", utente.getNome());
        assertEquals("Rossi", utente.getCognome());
        assertEquals(Utente.Ruolo.ADMIN, utente.getRuolo());
        assertTrue(authService.isAdmin(utente), "L'utente dovrebbe essere un admin");
    }

    @Test
    @DisplayName("Login con credenziali corrette (Socio)")
    void testLoginSuccessSocio() throws AuthenticationException {
        Utente utente = authService.login("socio@tennis.it", "socio123");

        assertNotNull(utente, "L'utente socio dovrebbe essere autenticato");
        assertEquals("Luigi", utente.getNome());
        assertEquals("Verdi", utente.getCognome());
        assertEquals(Utente.Ruolo.SOCIO, utente.getRuolo());
        assertFalse(authService.isAdmin(utente), "Il socio non dovrebbe essere un admin");
    }

    @Test
    @DisplayName("Login fallisce con password errata")
    void testLoginWrongPassword() throws AuthenticationException {
        Utente utente = authService.login("admin@tennis.it", "password_sbagliata");

        assertNull(utente, "Il login dovrebbe fallire con password errata");
    }

    @Test
    @DisplayName("Login fallisce con email inesistente")
    void testLoginUserNotFound() throws AuthenticationException {
        Utente utente = authService.login("nonexist@tennis.it", "password");

        assertNull(utente, "Il login dovrebbe fallire per utente inesistente");
    }

    @Test
    @DisplayName("Login lancia eccezione con email vuota")
    void testLoginEmptyEmail() {
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.login("", "password"),
                "Dovrebbe lanciare un'eccezione con email vuota");

        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    @DisplayName("Login lancia eccezione con password vuota")
    void testLoginEmptyPassword() {
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> authService.login("admin@tennis.it", ""),
                "Dovrebbe lanciare un'eccezione con password vuota");

        assertTrue(exception.getMessage().contains("password"));
    }

    @Test
    @DisplayName("Login lancia eccezione con email null")
    void testLoginNullEmail() {
        assertThrows(
                AuthenticationException.class,
                () -> authService.login(null, "password"));
    }

    @Test
    @DisplayName("Verifica ruolo utente")
    void testHasRole() throws AuthenticationException {
        Utente admin = authService.login("admin@tennis.it", "admin123");
        Utente socio = authService.login("socio@tennis.it", "socio123");

        assertTrue(authService.hasRole(admin, Utente.Ruolo.ADMIN));
        assertFalse(authService.hasRole(admin, Utente.Ruolo.SOCIO));

        assertTrue(authService.hasRole(socio, Utente.Ruolo.SOCIO));
        assertFalse(authService.hasRole(socio, Utente.Ruolo.ADMIN));
    }

    @Test
    @DisplayName("Logout non lancia eccezioni")
    void testLogout() throws AuthenticationException {
        Utente utente = authService.login("admin@tennis.it", "admin123");

        assertDoesNotThrow(() -> authService.logout(utente));
        assertDoesNotThrow(() -> authService.logout(null));
    }
}
