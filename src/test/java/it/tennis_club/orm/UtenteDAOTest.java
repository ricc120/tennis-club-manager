package it.tennis_club.orm;

import it.tennis_club.domain_model.Utente;
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

class UtenteDAOTest {

    private UtenteDAO utenteDAO;
    private List<Integer> utentiCreatiIds;

    @BeforeEach
    void setUp() {
        utenteDAO = new UtenteDAO();
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
    }

    /**
     * Elimina un utente dal database tramite il suo ID.
     */
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
    void testRegistrazioneSuccess() throws SQLException {
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome("Test");
        nuovoUtente.setCognome("Utente");
        nuovoUtente.setEmail("test.registrazione@tennis.it");
        nuovoUtente.setPassword("password123");
        nuovoUtente.setRuolo(Utente.Ruolo.SOCIO);

        Integer idGenerato = utenteDAO.registrazione(nuovoUtente);
        utentiCreatiIds.add(idGenerato);

        assertNotNull(idGenerato, "L'ID generato non dovrebbe essere null");
        assertTrue(idGenerato > 0, "L'ID generato dovrebbe essere positivo");
        assertEquals(idGenerato, nuovoUtente.getId(), "L'ID dovrebbe essere impostato nell'oggetto Utente");

        // Verifica che l'utente sia realmente nel database
        Utente utenteRecuperato = utenteDAO.getUtenteById(idGenerato);
        assertNotNull(utenteRecuperato, "L'utente dovrebbe essere recuperabile dal DB");
        assertEquals("Test", utenteRecuperato.getNome());
        assertEquals("Utente", utenteRecuperato.getCognome());
        assertEquals("test.registrazione@tennis.it", utenteRecuperato.getEmail());
        assertEquals(Utente.Ruolo.SOCIO, utenteRecuperato.getRuolo());
    }

    @Test
    @DisplayName("Registrazione fallisce con email duplicata")
    void testRegistrazioneEmailDuplicata() throws SQLException {
        // Prima registrazione
        Utente utente1 = new Utente();
        utente1.setNome("Primo");
        utente1.setCognome("Utente");
        utente1.setEmail("email.duplicata@tennis.it");
        utente1.setPassword("password123");
        utente1.setRuolo(Utente.Ruolo.SOCIO);

        Integer id1 = utenteDAO.registrazione(utente1);
        utentiCreatiIds.add(id1);

        // Seconda registrazione con stessa email dovrebbe fallire
        Utente utente2 = new Utente();
        utente2.setNome("Secondo");
        utente2.setCognome("Utente");
        utente2.setEmail("email.duplicata@tennis.it");
        utente2.setPassword("altrapwd");
        utente2.setRuolo(Utente.Ruolo.SOCIO);

        assertThrows(SQLException.class, () -> utenteDAO.registrazione(utente2),
                "Dovrebbe lanciare SQLException per email duplicata");
    }

    @Test
    @DisplayName("Registrazione con tutti i ruoli")
    void testRegistrazioneRuoliDiversi() throws SQLException {
        Utente.Ruolo[] ruoli = { Utente.Ruolo.SOCIO, Utente.Ruolo.MAESTRO, Utente.Ruolo.ALLIEVO };

        for (Utente.Ruolo ruolo : ruoli) {
            Utente utente = new Utente();
            utente.setNome("Test" + ruolo.name());
            utente.setCognome("Ruolo");
            utente.setEmail("test." + ruolo.name().toLowerCase() + "@tennis.it");
            utente.setPassword("password123");
            utente.setRuolo(ruolo);

            Integer id = utenteDAO.registrazione(utente);
            utentiCreatiIds.add(id);

            assertNotNull(id, "ID non dovrebbe essere null per ruolo " + ruolo);

            Utente recuperato = utenteDAO.getUtenteById(id);
            assertEquals(ruolo, recuperato.getRuolo(), "Il ruolo dovrebbe corrispondere");
        }
    }

    // ===== TEST LOGIN =====

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

    // ===== TEST GET UTENTE BY ID =====

    @Test
    @DisplayName("Recupera utente per ID esistente")
    void testGetUtenteByIdExistente() throws SQLException {
        // Crea un utente e poi lo recupera
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome("GetById");
        nuovoUtente.setCognome("Test");
        nuovoUtente.setEmail("getbyid.test@tennis.it");
        nuovoUtente.setPassword("password");
        nuovoUtente.setRuolo(Utente.Ruolo.SOCIO);

        Integer id = utenteDAO.registrazione(nuovoUtente);
        utentiCreatiIds.add(id);

        Utente recuperato = utenteDAO.getUtenteById(id);

        assertNotNull(recuperato, "L'utente dovrebbe essere trovato");
        assertEquals(id, recuperato.getId());
        assertEquals("GetById", recuperato.getNome());
    }

    @Test
    @DisplayName("GetUtenteById restituisce null per ID inesistente")
    void testGetUtenteByIdNonEsistente() throws SQLException {
        Utente utente = utenteDAO.getUtenteById(999999);

        assertNull(utente, "Dovrebbe restituire null per ID inesistente");
    }

    // ===== TEST DELETE UTENTE =====

    @Test
    @DisplayName("Cancellazione utente esistente con successo")
    void testDeleteUtenteSuccess() throws SQLException {
        // Crea un utente da cancellare
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome("DaCancellare");
        nuovoUtente.setCognome("Test");
        nuovoUtente.setEmail("da.cancellare@tennis.it");
        nuovoUtente.setPassword("password");
        nuovoUtente.setRuolo(Utente.Ruolo.SOCIO);

        Integer id = utenteDAO.registrazione(nuovoUtente);

        // Verifica che esista
        assertNotNull(utenteDAO.getUtenteById(id), "L'utente dovrebbe esistere prima della cancellazione");

        // Cancella
        boolean risultato = utenteDAO.deleteUtente(id);

        assertTrue(risultato, "La cancellazione dovrebbe avere successo");
        assertNull(utenteDAO.getUtenteById(id), "L'utente non dovrebbe più esistere dopo la cancellazione");
    }

    @Test
    @DisplayName("Cancellazione utente inesistente restituisce false")
    void testDeleteUtenteNonEsistente() throws SQLException {
        boolean risultato = utenteDAO.deleteUtente(999999);

        assertFalse(risultato, "La cancellazione di un utente inesistente dovrebbe restituire false");
    }
}
