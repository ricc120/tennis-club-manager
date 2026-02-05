package it.tennis_club.orm;

import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO delle prenotazioni.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 * 
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PrenotazioneDAOTest {

    private PrenotazioneDAO prenotazioneDAO;
    private CampoDAO campoDAO;
    private UtenteDAO utenteDAO;

    private List<Integer> idsPrenotazioniCreate;
    private static int testCounter = 0;

    @BeforeEach
    void setUp() {
        prenotazioneDAO = new PrenotazioneDAO();
        campoDAO = new CampoDAO();
        utenteDAO = new UtenteDAO();
        idsPrenotazioniCreate = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws SQLException {
        for (Integer id : idsPrenotazioniCreate) {
            prenotazioneDAO.deletePrenotazione(id);
        }
        idsPrenotazioniCreate.clear();
    }

    private Prenotazione createTestPrenotazione() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        List<Utente> soci = utenteDAO.getUtentiByRuolo(Utente.Ruolo.SOCIO);

        if (campi.isEmpty() || soci.isEmpty()) {
            fail("Non ci sono campi o utenti disponibili per creare prenotazioni di test");
        }

        // Genera una prenotazione con data/ora uniche
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData(LocalDate.now().plusDays(testCounter + 50)); // +50 per evitare conflitti con dati
                                                                          // esistenti
        prenotazione.setOraInizio(LocalTime.of(10 + (testCounter % 8), 0)); // Varia l'ora
        prenotazione.setCampo(campi.get(testCounter % campi.size())); // Varia il campo
        prenotazione.setSocio(soci.get(testCounter % soci.size()));

        Integer id = prenotazioneDAO.createPrenotazione(prenotazione);
        if (id != null) {
            idsPrenotazioniCreate.add(id);
        }
        testCounter++;

        return prenotazioneDAO.getPrenotazioneById(id);
    }

    @Test
    @Order(1)
    @DisplayName("Verifica il recupero di tutte le prenotazioni")
    void testGetAllPrenotazioni() throws SQLException {
        Prenotazione nuovaPrenotazione = createTestPrenotazione();
        assertNotNull(nuovaPrenotazione, "La prenotazione non dovrebbe essere null");

        List<Prenotazione> prenotazioni = prenotazioneDAO.getAllPrenotazioni();

        assertNotNull(prenotazioni, "La lista delle prenotazioni non dovrebbe essere null");

        // Verifica che le prenotazioni abbiano tutti i dati popolati
        for (Prenotazione prenotazione : prenotazioni) {
            assertNotNull(prenotazione.getId(), "L'ID non dovrebbe essere null");
            assertNotNull(prenotazione.getData(), "La data non dovrebbe essere null");
            assertNotNull(prenotazione.getOraInizio(), "L'ora di inizio non dovrebbe essere null");
            assertNotNull(prenotazione.getCampo(), "Il campo non dovrebbe essere null");
            assertNotNull(prenotazione.getSocio(), "Il socio non dovrebbe essere null");

            // Verifica che gli oggetti Campo e Utente siano completi
            assertNotNull(prenotazione.getCampo().getNome(), "Il nome del campo non dovrebbe essere null");
            assertNotNull(prenotazione.getSocio().getNome(), "Il nome del socio non dovrebbe essere null");
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni nel database");
        prenotazioni.forEach(System.out::println);
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero di una prenotazione specifica per ID")
    void testGetPrenotazioneById() throws SQLException {
        Prenotazione nuovaPrenotazione = createTestPrenotazione();
        assertNotNull(nuovaPrenotazione, "La prenotazione non dovrebbe essere null");

        Integer idDaCercare = nuovaPrenotazione.getId();
        Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(idDaCercare);

        assertNotNull(prenotazione, "La prenotazione dovrebbe esistere");
        assertEquals(idDaCercare, prenotazione.getId());
        assertNotNull(prenotazione.getCampo());
        assertNotNull(prenotazione.getSocio());

        System.out.println("Prenotazione trovata: " + prenotazione);

    }

    @Test
    @Order(3)
    @DisplayName("Verifica che getPrenotazioneById restituisca null per ID inesistente")
    void testGetPrenotazioneByIdNotFound() throws SQLException {
        Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(9999);

        assertNull(prenotazione, "Non dovrebbe trovare una prenotazione con ID inesistente");
    }

    @Test
    @Order(4)
    @DisplayName("Verifica il recupero delle prenotazioni per data")
    void testGetPrenotazioniByData() throws SQLException {
        Prenotazione nuovaPrenotazione = createTestPrenotazione();
        LocalDate data = nuovaPrenotazione.getData();
        assertNotNull(nuovaPrenotazione, "La prenotazione non dovrebbe essere null");

        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByData(data);

        assertNotNull(prenotazioni, "La lista non dovrebbe essere null");

        // Verifica che tutte le prenotazioni abbiano la data corretta
        for (Prenotazione prenotazione : prenotazioni) {
            assertEquals(data, prenotazione.getData(),
                    "Tutte le prenotazioni dovrebbero avere la data richiesta");
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per la data " + data);
    }

    @Test
    @Order(5)
    @DisplayName("Verifica il recupero delle prenotazioni per campo")
    void testGetPrenotazioniByCampo() throws SQLException {
        Prenotazione nuovaPrenotazione = createTestPrenotazione();
        Campo campo = nuovaPrenotazione.getCampo();

        assertNotNull(nuovaPrenotazione, "La prenotazione non dovrebbe essere null");

        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByCampo(campo.getId());

        assertNotNull(prenotazioni, "La lista non dovrebbe essere null");

        // Verifica che tutte le prenotazioni siano per il campo richiesto
        for (Prenotazione prenotazione : prenotazioni) {
            assertEquals(campo.getId(), prenotazione.getCampo().getId(),
                    "Tutte le prenotazioni dovrebbero essere per il campo richiesto");
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per il campo ID " + campo.getId());

    }

    @Test
    @Order(6)
    @DisplayName("Verifica il recupero delle prenotazioni per socio")
    void testGetPrenotazioniBySocio() throws SQLException {
        Prenotazione nuovPrenotazione = createTestPrenotazione();
        Utente socio = nuovPrenotazione.getSocio();

        assertNotNull(nuovPrenotazione, "La prenotazione non dovrebbe essere null");

        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniBySocio(socio.getId());

        assertNotNull(prenotazioni, "La lista non dovrebbe essere null");
        assertFalse(prenotazioni.isEmpty(), "Dovrebbe esserci almeno una prenotazione");

        // Verifica che tutte le prenotazioni siano del socio richiesto
        for (Prenotazione prenotazione : prenotazioni) {
            assertEquals(socio.getId(), prenotazione.getSocio().getId(),
                    "Tutte le prenotazioni dovrebbero essere del socio richiesto");
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per il socio ID " + socio.getId());

    }

    @Test
    @Order(7)
    @DisplayName("Verifica il recupero delle prenotazioni per data e campo")
    void testGetPrenotazioniByDataAndCampo() throws SQLException {
        Prenotazione nuovaPrenotazione = createTestPrenotazione();
        Campo campo = nuovaPrenotazione.getCampo();
        LocalDate data = nuovaPrenotazione.getData();

        assertNotNull(nuovaPrenotazione, "La prenotazione non dovrebbe essere null");

        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByDataAndCampo(data, campo.getId());

        assertNotNull(prenotazioni, "La lista non dovrebbe essere null");
        assertFalse(prenotazioni.isEmpty(), "Dovrebbe esserci almeno una prenotazione");

        // Verifica che tutte abbiano data e campo corretti
        for (Prenotazione prenotazione : prenotazioni) {
            assertEquals(data, prenotazione.getData());
            assertEquals(campo.getId(), prenotazione.getCampo().getId());
        }

        System.out.println("Trovate " + prenotazioni.size() +
                " prenotazioni per data " + data + " e campo ID " + campo.getId());

    }

    @Test
    @Order(8)
    @DisplayName("Verifica la creazione di una nuova prenotazione")
    void testCreatePrenotazione() throws SQLException {
        Prenotazione nuovaPrenotazione = createTestPrenotazione();
        Integer id = nuovaPrenotazione.getId();
        assertNotNull(id, "L'ID generato non dovrebbe essere null");
        assertTrue(id > 0, "L'ID generato dovrebbe essere positivo");
        assertEquals(id, nuovaPrenotazione.getId(),
                "L'ID dovrebbe essere stato assegnato all'oggetto");

        Prenotazione prenotazioneInserita = prenotazioneDAO.getPrenotazioneById(id);
        assertNotNull(prenotazioneInserita, "La prenotazione dovrebbe essere nel database");
        assertEquals(nuovaPrenotazione.getData(), prenotazioneInserita.getData());
        assertEquals(nuovaPrenotazione.getOraInizio(), prenotazioneInserita.getOraInizio());

        System.out.println("Prenotazione creata con ID: " + id);
        System.out.println(prenotazioneInserita);

    }

    @Test
    @Order(9)
    @DisplayName("Verifica l'aggiornamento di una prenotazione")
    void testUpdatePrenotazione() throws SQLException {
        Prenotazione nuovaPrenotazione = createTestPrenotazione();

        Integer id = nuovaPrenotazione.getId();
        assertNotNull(id, "L'ID generato non dovrebbe essere null");
        assertTrue(id > 0, "L'ID generato dovrebbe essere positivo");
        assertEquals(id, nuovaPrenotazione.getId(),
                "L'ID dovrebbe essere stato assegnato all'oggetto");

        nuovaPrenotazione.setOraInizio(LocalTime.of(15, 0));

        boolean success = prenotazioneDAO.updatePrenotazione(nuovaPrenotazione);

        assertTrue(success, "L'aggiornamento dovrebbe avere successo");

        Prenotazione prenotazioneAggiornata = prenotazioneDAO.getPrenotazioneById(id);
        assertEquals(LocalTime.of(15, 0), prenotazioneAggiornata.getOraInizio(),
                "L'ora dovrebbe essere stata aggiornata");

        System.out.println("Prenotazione aggiornata: " + prenotazioneAggiornata);

    }

    @Test
    @Order(10)
    @DisplayName("Verifica l'eliminazione di una prenotazione")
    void testDeletePrenotazione() throws SQLException {
        Prenotazione nuPrenotazione = createTestPrenotazione();
        Integer id = nuPrenotazione.getId();

        assertNotNull(id, "L'ID generato non dovrebbe essere null");
        assertTrue(id > 0, "L'ID generato dovrebbe essere positivo");
        assertEquals(id, nuPrenotazione.getId(),
                "L'ID dovrebbe essere stato assegnato all'oggetto");

        boolean success = prenotazioneDAO.deletePrenotazione(id);

        assertTrue(success, "L'eliminazione dovrebbe avere successo");

        Prenotazione prenotazioneEliminata = prenotazioneDAO.getPrenotazioneById(id);
        assertNull(prenotazioneEliminata,
                "La prenotazione dovrebbe essere stata eliminata dal database");

        System.out.println("Prenotazione con ID " + id + " eliminata con successo");

    }

    @Test
    @Order(11)
    @DisplayName("Verifica che l'aggiornamento di una prenotazione inesistente fallisca")
    void testUpdatePrenotazioneNotFound() throws SQLException {
        Prenotazione prenotazioneFake = new Prenotazione();
        prenotazioneFake.setId(9999);
        prenotazioneFake.setData(LocalDate.now());
        prenotazioneFake.setOraInizio(LocalTime.of(10, 0));

        // Usa campi e utenti fittizi (ma con ID validi se esistono)
        List<Campo> campi = campoDAO.getAllCampi();
        Utente socio = utenteDAO.getUtenteById(1);

        if (!campi.isEmpty() && socio != null) {
            prenotazioneFake.setCampo(campi.get(0));
            prenotazioneFake.setSocio(socio);

            boolean success = prenotazioneDAO.updatePrenotazione(prenotazioneFake);

            assertFalse(success, "L'aggiornamento di una prenotazione inesistente dovrebbe fallire");
        }
    }

    @Test
    @Order(12)
    @DisplayName("Verifica che l'eliminazione di una prenotazione inesistente fallisca")
    void testDeletePrenotazioneNotFound() throws SQLException {
        boolean success = prenotazioneDAO.deletePrenotazione(9999);

        assertFalse(success, "L'eliminazione di una prenotazione inesistente dovrebbe fallire");
    }
}
