package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.UtenteDAO;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test unitari per AuthService.
 * Usa Mockito per isolare la business logic dai DAO.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Unit Test")
class AuthServiceTest {

    @Mock
    private UtenteDAO utenteDAO;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Utente utenteValido;

    @BeforeEach
    void setUp() {
        utenteValido = new Utente();
        utenteValido.setNome("Mario");
        utenteValido.setCognome("Rossi");
        utenteValido.setEmail("mario@tennis.it");
        utenteValido.setPassword("password123");
        utenteValido.setRuolo(Ruolo.SOCIO);
    }

    // ========== TEST REGISTRAZIONE ==========

    @Test
    @DisplayName("Registrazione con successo")
    void testRegistrazioneSuccesso() throws Exception {
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashedpassword");
        when(utenteDAO.registrazione(any(Utente.class))).thenReturn(1);

        Integer id = authService.registrazioneSenzaSessione(utenteValido);

        assertNotNull(id);
        assertEquals(1, id);
        verify(passwordEncoder).encode("password123");
        verify(utenteDAO).registrazione(any(Utente.class));
    }

    @Test
    @DisplayName("Registrazione con utente null lancia eccezione")
    void testRegistrazioneUtenteNull() {
        assertThrows(AuthenticationException.class,
                () -> authService.registrazioneSenzaSessione(null));
    }

    @Test
    @DisplayName("Registrazione con nome vuoto lancia eccezione")
    void testRegistrazioneNomeVuoto() {
        utenteValido.setNome("");
        assertThrows(AuthenticationException.class,
                () -> authService.registrazioneSenzaSessione(utenteValido));
    }

    @Test
    @DisplayName("Registrazione con email vuota lancia eccezione")
    void testRegistrazioneEmailVuota() {
        utenteValido.setEmail("");
        assertThrows(AuthenticationException.class,
                () -> authService.registrazioneSenzaSessione(utenteValido));
    }

    @Test
    @DisplayName("Registrazione con password vuota lancia eccezione")
    void testRegistrazionePasswordVuota() {
        utenteValido.setPassword("");
        assertThrows(AuthenticationException.class,
                () -> authService.registrazioneSenzaSessione(utenteValido));
    }

    @Test
    @DisplayName("Registrazione con email duplicata lancia eccezione")
    void testRegistrazioneEmailDuplicata() throws Exception {
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(utenteDAO.registrazione(any(Utente.class)))
                .thenThrow(new SQLException("duplicate key value violates unique constraint"));

        assertThrows(AuthenticationException.class,
                () -> authService.registrazioneSenzaSessione(utenteValido));
    }

    // ========== TEST LOGIN ==========

    @Test
    @DisplayName("Login con credenziali corrette")
    void testLoginSuccesso() throws Exception {
        Utente utenteDB = new Utente();
        utenteDB.setId(1);
        utenteDB.setEmail("mario@tennis.it");
        utenteDB.setPassword("$2a$10$hashedpassword");
        utenteDB.setRuolo(Ruolo.SOCIO);
        utenteDB.setNome("Mario");
        utenteDB.setCognome("Rossi");

        when(utenteDAO.getUtenteByEmail("mario@tennis.it")).thenReturn(utenteDB);
        when(passwordEncoder.matches("password123", "$2a$10$hashedpassword")).thenReturn(true);

        Utente risultato = authService.login("mario@tennis.it", "password123");

        assertNotNull(risultato);
        assertEquals("mario@tennis.it", risultato.getEmail());
    }

    @Test
    @DisplayName("Login con password errata restituisce null")
    void testLoginPasswordErrata() throws Exception {
        Utente utenteDB = new Utente();
        utenteDB.setEmail("mario@tennis.it");
        utenteDB.setPassword("$2a$10$hashedpassword");

        when(utenteDAO.getUtenteByEmail("mario@tennis.it")).thenReturn(utenteDB);
        when(passwordEncoder.matches("wrong", "$2a$10$hashedpassword")).thenReturn(false);

        Utente risultato = authService.login("mario@tennis.it", "wrong");
        assertNull(risultato);
    }

    @Test
    @DisplayName("Login con email inesistente restituisce null")
    void testLoginEmailInesistente() throws Exception {
        when(utenteDAO.getUtenteByEmail("nonexiste@tennis.it")).thenReturn(null);

        Utente risultato = authService.login("nonexiste@tennis.it", "password123");
        assertNull(risultato);
    }

    // ========== TEST RUOLI ==========

    @Test
    @DisplayName("Verifica ruolo utente")
    void testHasRole() {
        Utente admin = new Utente();
        admin.setRuolo(Ruolo.ADMIN);
        assertTrue(authService.hasRole(admin, Ruolo.ADMIN));
        assertFalse(authService.hasRole(admin, Ruolo.SOCIO));
    }

    @Test
    @DisplayName("Verifica isAdmin")
    void testIsAdmin() {
        Utente admin = new Utente();
        admin.setRuolo(Ruolo.ADMIN);
        assertTrue(authService.isAdmin(admin));

        Utente socio = new Utente();
        socio.setRuolo(Ruolo.SOCIO);
        assertFalse(authService.isAdmin(socio));
    }

    // ========== TEST ELIMINAZIONE ==========

    @Test
    @DisplayName("Elimina utente con successo")
    void testEliminaUtente() throws Exception {
        when(utenteDAO.deleteUtente(1)).thenReturn(true);
        assertTrue(authService.eliminaUtente(1));
    }
}
