package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il servizio di autenticazione.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 */
class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

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
