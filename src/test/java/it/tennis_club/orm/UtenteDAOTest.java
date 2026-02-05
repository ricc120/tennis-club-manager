package it.tennis_club.orm;

import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UtenteDAOTest {

    private UtenteDAO utenteDAO;
    private List<Integer> idsUtentiCreati;

    @BeforeEach
    void setUp() {
        utenteDAO = new UtenteDAO();
        idsUtentiCreati = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        for (Integer id : idsUtentiCreati) {
            try {
                utenteDAO.deleteUtente(id);
            } catch (SQLException e) {
                System.err.println("Errore durante la pulizia dell'utente ID " + id + ": " + e.getMessage());
            }
        }
    }

    private Utente createTestUtente() throws SQLException {
        Utente utente = new Utente();
        utente.setNome("Test");
        utente.setCognome("Utente");
        utente.setEmail("test.registrazione@tennis.it");
        utente.setPassword("password123");
        utente.setRuolo(Utente.Ruolo.SOCIO);

        Integer id = utenteDAO.registrazione(utente);
        idsUtentiCreati.add(id);

        return utenteDAO.getUtenteById(id);
    }

    private Utente createTestUtente(String nome, String cognome, String email, String password, Utente.Ruolo ruolo)
            throws SQLException {
        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPassword(password);
        utente.setRuolo(ruolo);

        Integer id = utenteDAO.registrazione(utente);
        idsUtentiCreati.add(id);

        return utenteDAO.getUtenteById(id);
    }

    @Test
    @Order(1)
    @DisplayName("Registrazione nuovo utente con successo")
    void testRegistrazioneSuccess() throws SQLException {

        Utente nuovoUtente = createTestUtente();
        Integer id = nuovoUtente.getId();

        assertNotNull(id, "L'ID generato non dovrebbe essere null");
        assertTrue(id > 0, "L'ID generato dovrebbe essere positivo");
        assertEquals(id, nuovoUtente.getId(), "L'ID dovrebbe essere impostato nell'oggetto Utente");

        // Verifica che l'utente sia realmente nel database
        Utente utenteRecuperato = utenteDAO.getUtenteById(id);
        assertNotNull(utenteRecuperato, "L'utente dovrebbe essere recuperabile dal DB");
        assertEquals("Test", utenteRecuperato.getNome());
        assertEquals("Utente", utenteRecuperato.getCognome());
        assertEquals("test.registrazione@tennis.it", utenteRecuperato.getEmail());
        assertEquals(Utente.Ruolo.SOCIO, utenteRecuperato.getRuolo());

        System.out.println("Utente test " + id + " registrato con successo.");
    }

    @Test
    @Order(2)
    @DisplayName("Registrazione fallisce con email duplicata")
    void testRegistrazioneEmailDuplicata() throws SQLException {
        Utente utente1 = createTestUtente("Primo", "Utente", "email.duplicata@tennis.it", "password123",
                Utente.Ruolo.SOCIO);
        assertNotNull(utente1.getId(), "L'ID generato non dovrebbe essere null");

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
    @Order(3)
    @DisplayName("Registrazione con tutti i ruoli")
    void testRegistrazioneRuoliDiversi() throws SQLException {
        Utente.Ruolo[] ruoli = { Utente.Ruolo.SOCIO, Utente.Ruolo.MAESTRO, Utente.Ruolo.ALLIEVO };

        for (Utente.Ruolo ruolo : ruoli) {
            Utente utente = createTestUtente("Test" + ruolo.name(), "Utente",
                    "test." + ruolo.name().toLowerCase() + "@tennis.it", "password123", ruolo);

            Integer id = utente.getId();

            assertNotNull(id, "ID non dovrebbe essere null per ruolo " + ruolo);

            Utente recuperato = utenteDAO.getUtenteById(id);
            assertEquals(ruolo, recuperato.getRuolo(), "Il ruolo dovrebbe corrispondere");
        }

        System.out.println("Utenti test generati: " + idsUtentiCreati.size());

    }

    @Test
    @Order(4)
    @DisplayName("Verifica il login con credenziali corrette")
    void testLoginSuccessAdmin() throws SQLException {
        Utente nuovoUtente = createTestUtente();

        Utente utenteLoggato = utenteDAO.login(nuovoUtente.getEmail(), nuovoUtente.getPassword());

        assertNotNull(utenteLoggato, "L'utente dovrebbe essere trovato");
        assertEquals(nuovoUtente.getNome(), utenteLoggato.getNome());
        assertEquals(nuovoUtente.getCognome(), utenteLoggato.getCognome());
        assertEquals(nuovoUtente.getEmail(), utenteLoggato.getEmail());
        assertEquals(nuovoUtente.getRuolo(), utenteLoggato.getRuolo());
    }

    @Test
    @Order(5)
    @DisplayName("Verifica il login con password errata")
    void testLoginWrongPassword() throws SQLException {
        Utente nuovoUtente = createTestUtente();
        Utente utenteLoggato = utenteDAO.login(nuovoUtente.getEmail(), "wrong_password");

        assertNull(utenteLoggato, "Il login dovrebbe fallire con password errata");
    }

    @Test
    @Order(6)
    @DisplayName("Verifica il login con email inesistente")
    void testLoginUserNotFound() throws SQLException {
        Utente nuovoUtente = createTestUtente();
        Utente utenteLoggato = utenteDAO.login("non_esiste@tennis.it", nuovoUtente.getPassword());

        assertNull(utenteLoggato, "Il login dovrebbe fallire per utente inesistente");
    }

    @Test
    @Order(8)
    @DisplayName("Recupera utente per ID esistente")
    void testGetUtenteByIdEsistente() throws SQLException {
        // Crea un utente e poi lo recupera
        Utente nuovoUtente = createTestUtente();

        Integer id = nuovoUtente.getId();

        Utente recuperato = utenteDAO.getUtenteById(id);

        assertNotNull(recuperato, "L'utente dovrebbe essere trovato");
        assertEquals(id, recuperato.getId());
        assertEquals(nuovoUtente.getNome(), recuperato.getNome());

        System.out.println("Utente test " + id + " recuperato con successo.");
    }

    @Test
    @Order(9)
    @DisplayName("GetUtenteById restituisce null per ID inesistente")
    void testGetUtenteByIdNonEsistente() throws SQLException {
        Utente utente = utenteDAO.getUtenteById(999999);

        assertNull(utente, "Dovrebbe restituire null per ID inesistente");
    }

    @Test
    @Order(10)
    @DisplayName("Cancellazione utente esistente con successo")
    void testDeleteUtenteSuccess() throws SQLException {
        // Crea un utente da cancellare
        Utente nuovoUtente = createTestUtente();

        Integer id = nuovoUtente.getId();

        // Verifica che esista
        assertNotNull(utenteDAO.getUtenteById(id), "L'utente dovrebbe esistere prima della cancellazione");

        // Cancella
        boolean risultato = utenteDAO.deleteUtente(id);

        assertTrue(risultato, "La cancellazione dovrebbe avere successo");
        assertNull(utenteDAO.getUtenteById(id), "L'utente non dovrebbe più esistere dopo la cancellazione");

        System.out.println("Utente test " + id + " cancellato con successo.");
    }

    @Test
    @Order(11)
    @DisplayName("Cancellazione utente inesistente restituisce false")
    void testDeleteUtenteNonEsistente() throws SQLException {
        boolean risultato = utenteDAO.deleteUtente(999999);

        assertFalse(risultato, "La cancellazione di un utente inesistente dovrebbe restituire false");
    }
}
