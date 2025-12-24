package it.tennis_club.orm;

import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UtenteDAOTest {

    private UtenteDAO utenteDAO;

    @BeforeEach
    void setUp() {
        utenteDAO = new UtenteDAO();
    }

    @Test
    @DisplayName("Verifica il login con credenziali corrette (Admin)")
    void testLoginSuccessAdmin() throws SQLException {
        // Questi dati devono essere presenti nel DB (es. caricati tramite default.sql)
        Utente utente = utenteDAO.login("admin@tennis.it", "admin123");

        assertNotNull(utente, "L'utente dovrebbe essere trovato");
        assertEquals("Mario", utente.getNome());
        assertEquals(Utente.Ruolo.ADMIN, utente.getRuolo());
    }

    @Test
    @DisplayName("Verifica il login con credenziali corrette (Socio)")
    void testLoginSuccessSocio() throws SQLException {
        Utente utente = utenteDAO.login("socio@tennis.it", "socio123");

        assertNotNull(utente, "L'utente dovrebbe essere trovato");
        assertEquals("Luigi", utente.getNome());
        assertEquals(Utente.Ruolo.SOCIO, utente.getRuolo());
    }

    @Test
    @DisplayName("Verifica il login con password errata")
    void testLoginWrongPassword() throws SQLException {
        Utente utente = utenteDAO.login("admin@tennis.it", "wrong_password");

        assertNull(utente, "Il login dovrebbe fallire con password errata");
    }

    @Test
    @DisplayName("Verifica il login con email inesistente")
    void testLoginUserNotFound() throws SQLException {
        Utente utente = utenteDAO.login("non_esiste@tennis.it", "password");

        assertNull(utente, "Il login dovrebbe fallire per utente inesistente");
    }
}
