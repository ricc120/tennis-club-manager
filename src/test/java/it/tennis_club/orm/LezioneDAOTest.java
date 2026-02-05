package it.tennis_club.orm;

import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO delle lezioni.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 * 
 * NOTA: I test che creano lezioni generano anche nuove prenotazioni per evitare
 * violazioni del vincolo UNIQUE su id_prenotazione nella tabella lezione.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LezioneDAOTest {

    private LezioneDAO lezioneDAO;
    private PrenotazioneDAO prenotazioneDAO;
    private UtenteDAO utenteDAO;
    private CampoDAO campoDAO;

    private List<Integer> idsLezioniCreate;
    private List<Integer> idsPrenotazioniCreate;

    // Contatore per generare date uniche per le prenotazioni di test
    private static int testCounter = 0;

    @BeforeEach
    void setUp() {
        lezioneDAO = new LezioneDAO();
        prenotazioneDAO = new PrenotazioneDAO();
        utenteDAO = new UtenteDAO();
        campoDAO = new CampoDAO();
        idsLezioniCreate = new ArrayList<>();
        idsPrenotazioniCreate = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws SQLException {
        for (Integer id : idsLezioniCreate) {
            lezioneDAO.deleteLezione(id);
        }
        for (Integer id : idsPrenotazioniCreate) {
            prenotazioneDAO.deletePrenotazione(id);
        }
        idsLezioniCreate.clear();
        idsPrenotazioniCreate.clear();
    }

    // ==================== METODI HELPER ====================

    /**
     * Crea una nuova prenotazione di test.
     * Ogni chiamata crea una prenotazione con data e ora uniche per evitare
     * conflitti.
     * La prenotazione viene automaticamente registrata per la pulizia nel tearDown.
     * 
     * @return La prenotazione creata
     */
    private Prenotazione createTestPrenotazione() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        List<Utente> soci = utenteDAO.getUtentiByRuolo(Utente.Ruolo.SOCIO);

        if (campi.isEmpty() || soci.isEmpty()) {
            fail("Non ci sono campi o utenti disponibili per creare prenotazioni di test");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData(LocalDate.now().plusDays(testCounter + 50));
        prenotazione.setOraInizio(LocalTime.of(10 + (testCounter % 8), 0));
        prenotazione.setCampo(campi.get(testCounter % campi.size()));
        prenotazione.setSocio(soci.get(testCounter % soci.size()));

        Integer id = prenotazioneDAO.createPrenotazione(prenotazione);
        if (id != null) {
            idsPrenotazioniCreate.add(id);
        }
        testCounter++;

        return prenotazioneDAO.getPrenotazioneById(id);
    }

    /**
     * Crea una nuova lezione di test con descrizione di default.
     * La lezione viene automaticamente registrata per la pulizia nel tearDown.
     * 
     * @param maestro Il maestro associato alla lezione
     * @return La lezione creata con ID popolato
     */
    private Lezione createTestLezione(Utente maestro) throws SQLException {
        return createTestLezione(maestro, "Test Descrizione");
    }

    /**
     * Crea una nuova lezione di test con descrizione personalizzata.
     * La lezione viene automaticamente registrata per la pulizia nel tearDown.
     * 
     * @param maestro     Il maestro associato alla lezione
     * @param descrizione La descrizione della lezione
     * @return La lezione creata con ID popolato
     */
    private Lezione createTestLezione(Utente maestro, String descrizione) throws SQLException {
        Prenotazione prenotazione = createTestPrenotazione();

        Lezione lezione = new Lezione();
        lezione.setPrenotazione(prenotazione);
        lezione.setMaestro(maestro);
        lezione.setDescrizione(descrizione);

        Integer id = lezioneDAO.createLezione(lezione);
        assertNotNull(id, "L'ID della lezione non dovrebbe essere null");
        assertTrue(id > 0, "L'ID della lezione dovrebbe essere positivo");

        if (id != null) {
            idsLezioniCreate.add(id);
        }

        return lezioneDAO.getLezioneById(id);
    }

    /**
     * Ottiene la lista dei maestri disponibili.
     * Fallisce il test se non ci sono maestri.
     */
    private List<Utente> getMaestri() throws SQLException {
        List<Utente> maestri = utenteDAO.getUtentiByRuolo(Utente.Ruolo.MAESTRO);
        if (maestri.isEmpty()) {
            fail("Non ci sono maestri disponibili per eseguire il test");
        }
        return maestri;
    }

    // ==================== TEST ====================

    @Test
    @Order(1)
    @DisplayName("Verifica il recupero di tutte le lezioni")
    void testGetAllLezioni() throws SQLException {
        List<Utente> maestri = getMaestri();

        // Crea almeno una lezione di test
        createTestLezione(maestri.get(0));

        List<Lezione> lezioni = lezioneDAO.getAllLezioni();

        assertNotNull(lezioni, "La lista delle lezioni non dovrebbe essere null");

        for (Lezione lezione : lezioni) {
            assertNotNull(lezione.getId(), "L'ID non dovrebbe essere null");
            assertNotNull(lezione.getPrenotazione(), "La prenotazione non dovrebbe essere null");
            assertNotNull(lezione.getMaestro(), "Il maestro non dovrebbe essere null");
        }

        System.out.println("Trovate " + lezioni.size() + " lezioni nel database");
        lezioni.forEach(System.out::println);
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero di una lezione specificata per ID")
    void testGetLezioneById() throws SQLException {
        List<Utente> maestri = getMaestri();
        Lezione lezioneCreata = createTestLezione(maestri.get(0));

        Lezione lezioneRecuperata = lezioneDAO.getLezioneById(lezioneCreata.getId());

        assertNotNull(lezioneRecuperata, "La lezione dovrebbe esistere");
        assertEquals(lezioneCreata.getId(), lezioneRecuperata.getId());
        assertNotNull(lezioneRecuperata.getPrenotazione(), "La prenotazione non dovrebbe essere null");
        assertNotNull(lezioneRecuperata.getMaestro(), "Il maestro non dovrebbe essere null");

        System.out.println("Trovata lezione " + lezioneRecuperata);
    }

    @Test
    @Order(3)
    @DisplayName("Verifica che getLezioneById restituisca null per ID inesistente")
    void testGetLezioneByIdNotFound() throws SQLException {
        Lezione lezione = lezioneDAO.getLezioneById(9999);
        assertNull(lezione, "Non dovrebbe trovare una lezione con ID inesistente");
    }

    @Test
    @Order(4)
    @DisplayName("Verifica il recupero di una lezione specificata per una prenotazione")
    void testGetLezioneByPrenotazione() throws SQLException {
        List<Utente> maestri = getMaestri();
        Lezione lezioneCreata = createTestLezione(maestri.get(0));

        Integer idPrenotazione = lezioneCreata.getPrenotazione().getId();
        Lezione lezioneRecuperata = lezioneDAO.getLezioneByPrenotazione(idPrenotazione);

        assertNotNull(lezioneRecuperata, "La lezione dovrebbe avere una prenotazione");
        assertEquals(idPrenotazione, lezioneRecuperata.getPrenotazione().getId());

        System.out.println("Trovata lezione " + lezioneRecuperata);
    }

    @Test
    @Order(5)
    @DisplayName("Verifica il recupero delle lezioni per maestro")
    void testGetLezioniByMaestro() throws SQLException {
        List<Utente> maestri = getMaestri();
        Utente maestro = maestri.get(0);

        // Crea una lezione per il maestro
        createTestLezione(maestro);

        List<Lezione> lezioni = lezioneDAO.getLezioniByMaestro(maestro.getId());

        assertNotNull(lezioni, "La lista non dovrebbe essere null");
        assertFalse(lezioni.isEmpty(), "Dovrebbe esserci almeno una lezione");

        for (Lezione lezione : lezioni) {
            assertEquals(maestro.getId(), lezione.getMaestro().getId(),
                    "Tutte le lezioni dovrebbero essere del maestro richiesto");
        }

        System.out.println("Trovate " + lezioni.size() + " lezioni per il maestro ID " + maestro.getId());
    }

    @Test
    @Order(6)
    @DisplayName("Verifica la creazione di una nuova lezione")
    void testCreateLezione() throws SQLException {
        List<Utente> maestri = getMaestri();

        for (Utente maestro : maestri) {
            Lezione lezione = createTestLezione(maestro);

            assertNotNull(lezione, "La lezione non dovrebbe essere null");
            assertNotNull(lezione.getId(), "L'ID non dovrebbe essere null");
            assertTrue(lezione.getId() > 0, "L'ID dovrebbe essere positivo");

            Lezione lezioneInserita = lezioneDAO.getLezioneById(lezione.getId());
            assertNotNull(lezioneInserita, "La lezione dovrebbe essere nel database");
            assertEquals(lezione.getPrenotazione().getId(), lezioneInserita.getPrenotazione().getId());
            assertEquals(lezione.getMaestro().getId(), lezioneInserita.getMaestro().getId());
            assertEquals(lezione.getDescrizione(), lezioneInserita.getDescrizione());

            System.out.println("Lezione creata con ID: " + lezione.getId());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Verifica l'aggiornamento di una lezione")
    void testUpdateLezione() throws SQLException {
        List<Utente> maestri = getMaestri();
        Lezione lezione = createTestLezione(maestri.get(0));

        // Modifica i dati
        lezione.setDescrizione("Descrizione aggiornata");

        // Aggiorna nel database
        boolean success = lezioneDAO.updateLezione(lezione);
        assertTrue(success, "L'aggiornamento dovrebbe avere successo");

        // Verifica che le modifiche siano state salvate
        Lezione lezioneAggiornata = lezioneDAO.getLezioneById(lezione.getId());
        assertEquals("Descrizione aggiornata", lezioneAggiornata.getDescrizione(),
                "La descrizione dovrebbe essere stata aggiornata");

        System.out.println("Lezione aggiornata: " + lezioneAggiornata);
    }

    @Test
    @Order(8)
    @DisplayName("Verifica l'eliminazione di una lezione")
    void testDeleteLezione() throws SQLException {
        List<Utente> maestri = getMaestri();
        Lezione lezione = createTestLezione(maestri.get(0));
        Integer id = lezione.getId();

        // Rimuovi dalla lista di cleanup poiché verrà eliminato nel test
        idsLezioniCreate.remove(id);

        // Elimina la lezione
        boolean success = lezioneDAO.deleteLezione(id);
        assertTrue(success, "L'eliminazione dovrebbe avere successo");

        // Verifica che la lezione sia stata davvero eliminata
        Lezione lezioneEliminata = lezioneDAO.getLezioneById(id);
        assertNull(lezioneEliminata, "La lezione dovrebbe essere stata eliminata dal database");

        System.out.println("Lezione con ID " + id + " eliminata con successo");
    }

    @Test
    @Order(9)
    @DisplayName("Verifica che l'aggiornamento di una lezione inesistente fallisca")
    void testUpdateLezioneNotFound() throws SQLException {
        List<Utente> maestri = getMaestri();
        Prenotazione prenotazione = createTestPrenotazione();

        Lezione lezioneFake = new Lezione();
        lezioneFake.setId(9999);
        lezioneFake.setDescrizione("Fake descrizione");
        lezioneFake.setPrenotazione(prenotazione);
        lezioneFake.setMaestro(maestri.get(0));

        boolean success = lezioneDAO.updateLezione(lezioneFake);
        assertFalse(success, "L'aggiornamento di una lezione inesistente dovrebbe fallire");
    }

    @Test
    @Order(10)
    @DisplayName("Verifica che l'eliminazione di una lezione inesistente fallisca")
    void testDeleteLezioneNotFound() throws SQLException {
        boolean success = lezioneDAO.deleteLezione(9999);
        assertFalse(success, "L'eliminazione di una lezione inesistente dovrebbe fallire");
    }
}
