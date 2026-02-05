package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.AllievoLezioneDAO;
import it.tennis_club.orm.CampoDAO;
import it.tennis_club.orm.LezioneDAO;
import it.tennis_club.orm.PrenotazioneDAO;
import it.tennis_club.orm.UtenteDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di integrazione per AccademiaService.
 * Questi test verificano che la business logic funzioni correttamente
 * insieme al database reale
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccademiaServiceTest {

    private static AccademiaService accademiaService;
    private static CampoDAO campoDAO;
    private static PrenotazioneDAO prenotazioneDAO;
    private static UtenteDAO utenteDAO;
    private static LezioneDAO lezioneDAO;
    private static AllievoLezioneDAO allievoLezioneDAO;

    private static Campo campoTest;
    private static Utente utenteMaestroTest;
    private static Utente utenteAllievoTest;
    private static List<Integer> idsLezioniTest;
    private static int testCounter = 0;
    private static LocalDate dataTest;
    private static LocalTime oraTest;
    private static Prenotazione prenotazioneCreate;

    @BeforeEach
    void setUp() throws SQLException {
        accademiaService = new AccademiaService();
        campoDAO = new CampoDAO();
        prenotazioneDAO = new PrenotazioneDAO();
        utenteDAO = new UtenteDAO();
        lezioneDAO = new LezioneDAO();
        allievoLezioneDAO = new AllievoLezioneDAO();
        idsLezioniTest = new ArrayList<Integer>();
        prenotazioneCreate = new Prenotazione();

        List<Campo> campi = campoDAO.getAllCampi();
        assertNotNull(campi, "Il database dovrebbe contenere almeno un campo");

        campoTest = campi.get(testCounter % campi.size());

        List<Utente> utentiMaestro = utenteDAO.getUtentiByRuolo(Utente.Ruolo.MAESTRO);

        assertNotNull(utentiMaestro, "Il database dovrebbe contenere almeno un utente per maestro");
        utenteMaestroTest = utentiMaestro.get(testCounter % utentiMaestro.size());

        List<Utente> utentiAllievo = utenteDAO.getUtentiByRuolo(Utente.Ruolo.ALLIEVO);
        assertNotNull(utentiAllievo, "Il database dovrebbe contenere almeno un utente per allievo");

        utenteAllievoTest = utentiAllievo.get(testCounter % utentiAllievo.size());

        dataTest = LocalDate.now().plusDays(50 + testCounter);
        oraTest = LocalTime.of(10 + (testCounter % 8), 0);

        testCounter++;

    }

    @AfterEach
    void tearDown() throws SQLException {
        for (Integer integer : idsLezioniTest) {
            if (prenotazioneCreate.getId() == null) {
                prenotazioneCreate = prenotazioneDAO.getPrenotazioneByLezione(integer);
                prenotazioneDAO.deletePrenotazione(prenotazioneCreate.getId());

            } else {
                prenotazioneDAO.deletePrenotazione(prenotazioneCreate.getId());
            }

            lezioneDAO.deleteLezione(integer);
        }
        idsLezioniTest.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Test creazione lezione valida")
    void testCreateLezione() throws AccademiaException, PrenotazioneException {

        Integer id = accademiaService.createLezione(dataTest, oraTest, campoTest, utenteMaestroTest,
                "Descrizione test");
        idsLezioniTest.add(id);
        assertNotNull(id,
                "L'ID della prenotazione inerente alla lezione creata non dovrebbe essere null");
        assertTrue(id > 0,
                "L'ID della prenotazione inerente alla lezione creata dovrebbe essere positivo");

        System.out.println("Lezione creata con ID: " + id);

    }

    @Test
    @Order(2)
    @DisplayName("Test inserimento descrizione lezione valida")
    void testInserisciDescrizione() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(dataTest, oraTest, campoTest, utenteMaestroTest,
                "Descrzione Test");
        idsLezioniTest.add(id);
        List<Lezione> lezioni = accademiaService.getAllLezioni();
        if (!lezioni.isEmpty()) {
            for (Lezione l : lezioni) {
                assertNotNull(l.getId(), "L'ID della lezione non dovrebbe essere null");
                l.setDescrizione("Descrizione test");
                assertTrue(accademiaService.inserisciDescrizione(l.getId(), "Descrizione Test"),
                        "La descrizione dovrebbe essere inserita con successo");
            }
        }

        System.out.println(lezioni.size() + " descrizioni inserite con successo");
    }

    @Test
    @Order(3)
    @DisplayName("Test eliminazione lezione")
    void testDeleteLezione() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(dataTest, oraTest, campoTest, utenteMaestroTest,
                "Descrzione Test");
        idsLezioniTest.add(id);

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        if (!lezioni.isEmpty()) {
            for (Lezione l : lezioni) {
                // Necessario per prendere la prenotazione prima che l'oggetto venga cancellato
                prenotazioneCreate = l.getPrenotazione();
                assertNotNull(l.getId(), "L'ID della lezione non dovrebbe essere nullo");
                assertTrue(accademiaService.deleteLezione(l.getId()),
                        "La lezione dovrebbe essere stata eliminata correttamente");
            }
        }

        System.out.println(lezioni.size() + " lezioni eliminate correttamente");
    }

    @Test
    @Order(4)
    @DisplayName("Test recupero lezioni per ID")
    void testGetLezioniById() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(dataTest, oraTest, campoTest, utenteMaestroTest,
                "Descrzione Test");
        idsLezioniTest.add(id);
        List<Lezione> lezioni = accademiaService.getAllLezioni();
        for (Lezione l : lezioni) {
            Integer idLezione = l.getId();
            Lezione lezione = accademiaService.getLezioneById(idLezione);

            assertNotNull(lezione, "La lezione non dovrebbe essere null");
            assertEquals(idLezione, lezione.getId(), "L'ID della lezione non dovrebbe essere null");
        }
        System.out.println("Lezione recuperata con succeso");
    }

    @Test
    @Order(5)
    @DisplayName("Test recupero di tutte le lezioni esistenti")
    void testGetAllLezioni() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(dataTest, oraTest, campoTest, utenteMaestroTest,
                "Descrzione Test");
        idsLezioniTest.add(id);
        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertNotNull(lezioni, "La lista di lezioni non dovrebbe essere null");
        assertFalse(lezioni.isEmpty(), "La lista di lezioni non dovrebbe essere vuota");

        System.out.println("Tutte le lezioni recuperate con successo");
    }

    @Test
    @Order(6)
    @DisplayName("Test recupero di una lezione per Maestro")
    void testGetLezioneByMaestro() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(dataTest, oraTest, campoTest, utenteMaestroTest,
                "Descrzione Test");
        idsLezioniTest.add(id);
        List<Lezione> lezioni = accademiaService.getLezioneByMaestro(utenteMaestroTest);
        assertNotNull(lezioni, "La lista di lezioni non dovrebbe essere null");
        for (Lezione l : lezioni) {
            assertNotNull(l.getMaestro(), "La lezione non dovrebbe avere il maestro associato null");
            assertEquals(utenteMaestroTest.getId(), l.getMaestro().getId());
        }

        System.out.println(lezioni.size() + " lezioni con maestro recuperate correttamente");

    }

    @Test
    @Order(7)
    @DisplayName("Test recupero di una lezione per Prenotazione")
    void testGetLezioneByPrenotazione() throws AccademiaException, PrenotazioneException, SQLException {
        Integer id = accademiaService.createLezione(dataTest, oraTest, campoTest, utenteMaestroTest,
                "Descrzione Test");
        idsLezioniTest.add(id);
        List<Lezione> lezioni = accademiaService.getAllLezioni();
        Integer idPrenotazione = lezioni.get(0).getPrenotazione().getId();
        for (Lezione l : lezioni) {
            l = accademiaService.getLezioneByPrenotazione(prenotazioneDAO.getPrenotazioneById(idPrenotazione));
            assertNotNull(l, "La lezione non dovrebbe essere nulla");
            assertEquals(idPrenotazione, l.getPrenotazione().getId());
        }

        System.out.println("Lezione con id prenotazione " + idPrenotazione + " recuperata con successo");

    }

    @Test
    @Order(8)
    @DisplayName("Test aggiunta di un allievo a una lezione")
    void testAggiungiAllievo() throws AccademiaException, PrenotazioneException, SQLException {
        Integer id = accademiaService.createLezione(
                dataTest, oraTest, campoTest, utenteMaestroTest, "Lezione per test allievo");
        idsLezioniTest.add(id);
        assertNotNull(id, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertNotNull(lezioni, "La lista di lezioni non dovrebbe essere null");
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");

        Lezione lezione = lezioni.get(0);

        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievoTest);

        List<Utente> allievi = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertNotNull(allievi, "La lista degli allievi non dovrebbe essere null");
        assertFalse(allievi.isEmpty(), "L'allievo dovrebbe essere stato aggiunto alla lezione");

        System.out.println(
                "Allievo " + utenteAllievoTest.getId() + " aggiunto alla lezione " + lezione.getId() + " con successo");
    }

    @Test
    @Order(9)
    @DisplayName("Test rimozione allievo da una lezione")
    void testRimuoviAllievo() throws AccademiaException, PrenotazioneException, SQLException {
        Integer id = accademiaService.createLezione(
                dataTest, oraTest, campoTest, utenteMaestroTest,
                "Lezione per test rimozione allievo");
        idsLezioniTest.add(id);
        assertNotNull(id, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievoTest);

        List<?> allieviPrima = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertFalse(allieviPrima.isEmpty(), "L'allievo dovrebbe essere presente prima della rimozione");

        boolean risultato = accademiaService.rimuoviAllievo(lezione.getId(), utenteAllievoTest.getId());

        assertTrue(risultato, "La rimozione dovrebbe avere successo");
        List<?> allieviDopo = allievoLezioneDAO.getAllieviByLezione(lezione.getId());

        assertTrue(allieviDopo.isEmpty(), "L'allievo dovrebbe essere stato rimosso dalla lezione");

        System.out.println(
                "Allievo " + utenteAllievoTest.getId() + " rimosso dalla lezione " + lezione.getId() + " con successo");
    }

    @Test
    @Order(10)
    @DisplayName("Test recupero di tutte le lezioni di un allievo")
    void testGetLezioniAllievo() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(
                dataTest, oraTest, campoTest, utenteMaestroTest,
                "Lezione per test getLezioniAllievo");
        idsLezioniTest.add(id);
        assertNotNull(id, "La lezione dovrebbe essere stata creata");

        List<Lezione> tutte = accademiaService.getAllLezioni();
        assertFalse(tutte.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = tutte.get(0);

        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievoTest);

        List<Lezione> lezioniAllievo = accademiaService.getLezioniAllievo(utenteAllievoTest);

        assertNotNull(lezioniAllievo, "La lista di lezioni per l'allievo non dovrebbe essere null");
        assertFalse(lezioniAllievo.isEmpty(), "L'allievo dovrebbe avere almeno una lezione");

        System.out
                .println("Recuperate " + lezioniAllievo.size() + " lezioni per l'allievo " + utenteAllievoTest.getId());
    }

    @Test
    @Order(11)
    @DisplayName("Test conteggio di allievi in una lezione")
    void testContaAllievi() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(
                dataTest, oraTest, campoTest, utenteMaestroTest,
                "Lezione per test contaAllievi");
        idsLezioniTest.add(id);
        assertNotNull(id, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        int countPrima = accademiaService.contaAllievi(lezione.getId());
        assertEquals(0, countPrima, "La lezione dovrebbe avere 0 allievi inizialmente");

        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievoTest);

        int countDopo = accademiaService.contaAllievi(lezione.getId());
        assertEquals(1, countDopo, "La lezione dovrebbe avere 1 allievo dopo l'aggiunta");

        System.out.println("Conteggio allievi eseguito con successo: " + countDopo + " allievo/i");
    }

    @Test
    @Order(12)
    @DisplayName("Test per segnare la presenza di un allievo a lezione")
    void testSegnaPresenza() throws AccademiaException, SQLException, PrenotazioneException {
        Integer id = accademiaService.createLezione(
                dataTest, oraTest, campoTest, utenteMaestroTest,
                "Lezione per test segnaPresenza");
        idsLezioniTest.add(id);
        assertNotNull(id, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievoTest);

        List<?> allievi = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertNotNull(allievi, "La lista degli allievi non dovrebbe essere null");
        assertFalse(allievi.isEmpty(), "L'allievo dovrebbe essere stato aggiunto alla lezione");

        boolean risultatoPresente = accademiaService.segnaPresenza(lezione.getId(), utenteAllievoTest.getId(), true);

        assertTrue(risultatoPresente, "La presenza dovrebbe essere stata segnata con successo");

        boolean risultatoAssente = accademiaService.segnaPresenza(lezione.getId(), utenteAllievoTest.getId(), false);
        assertTrue(risultatoAssente, "L'assenza dovrebbe essere stata segnata con successo");

        System.out.println("Presenza/assenza segnata con successo per l'allievo " + utenteAllievoTest.getId());
    }

    // ========== TEST CASI DI ERRORE E VALIDAZIONE ==========

    @Test
    @Order(13)
    @DisplayName("Test creazione lezione con parametri null")
    void testCreateLezioneParametriNull() {
        LocalDate dataFutura = LocalDate.now().plusDays(80);
        LocalTime oraValida = LocalTime.of(10, 0);

        // Test con data null
        assertThrows(AccademiaException.class,
                () -> accademiaService.createLezione(null, oraValida, campoTest, utenteMaestroTest, "Descrizione"),
                "Dovrebbe lanciare eccezione con data null");

        // Test con ora null
        assertThrows(AccademiaException.class,
                () -> accademiaService.createLezione(dataFutura, null, campoTest, utenteMaestroTest, "Descrizione"),
                "Dovrebbe lanciare eccezione con ora null");

        // Test con campo null
        assertThrows(AccademiaException.class,
                () -> accademiaService.createLezione(dataFutura, oraValida, null, utenteMaestroTest, "Descrizione"),
                "Dovrebbe lanciare eccezione con campo null");

        // Test con maestro null
        assertThrows(AccademiaException.class,
                () -> accademiaService.createLezione(dataFutura, oraValida, campoTest, null, "Descrizione"),
                "Dovrebbe lanciare eccezione con maestro null");

        System.out.println("Validazione parametri null per createLezione funziona correttamente");
    }

    @Test
    @Order(14)
    @DisplayName("Test recupero lezione con ID inesistente")
    void testGetLezioneByIdInesistente() {
        Integer idInesistente = 999999;

        assertThrows(AccademiaException.class,
                () -> accademiaService.getLezioneById(idInesistente),
                "Dovrebbe lanciare un'eccezione per ID inesistente");

        System.out.println("Eccezione correttamente lanciata per lezione inesistente");
    }

    @Test
    @Order(15)
    @DisplayName("Test eliminazione lezione con ID inesistente")
    void testDeleteLezioneInesistente() throws AccademiaException {
        Integer idInesistente = 999999;

        boolean risultato = accademiaService.deleteLezione(idInesistente);

        assertFalse(risultato, "La cancellazione di una lezione inesistente dovrebbe restituire false");

        System.out.println("Cancellazione lezione inesistente restituisce false correttamente");
    }

    @Test
    @Order(16)
    @DisplayName("Test aggiunta allievo con ID lezione non valido")
    void testAggiungiAllievoLezioneInesistente() {
        Integer idLezioneInesistente = 999999;

        assertThrows(AccademiaException.class,
                () -> accademiaService.aggiungiAllievo(idLezioneInesistente, utenteAllievoTest),
                "Dovrebbe lanciare un'eccezione per ID lezione inesistente");

        System.out.println("Eccezione correttamente lanciata per aggiunta allievo a lezione inesistente");
    }

    @Test
    @Order(17)
    @DisplayName("Test rimozione allievo non iscritto alla lezione")
    void testRimuoviAllievoNonIscritto() throws AccademiaException, PrenotazioneException {
        Integer id = accademiaService.createLezione(
                dataTest, oraTest, campoTest, utenteMaestroTest, "Lezione senza allievi");
        idsLezioniTest.add(id);
        assertNotNull(id, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        AccademiaException exception = assertThrows(AccademiaException.class,
                () -> accademiaService.rimuoviAllievo(lezione.getId(), utenteAllievoTest.getId()),
                "Dovrebbe lanciare eccezione per allievo non iscritto");

        assertTrue(exception.getMessage().contains("non è iscritto"),
                "Il messaggio dovrebbe indicare che l'allievo non è iscritto");

        System.out.println(
                "Eccezione correttamente lanciata per rimozione allievo non iscritto: " + exception.getMessage());
    }

    @Test
    @Order(18)
    @DisplayName("Test inserimento descrizione con ID lezione null")
    void testInserisciDescrizioneIdNull() {
        assertThrows(AccademiaException.class,
                () -> accademiaService.inserisciDescrizione(null, "Descrizione test"),
                "Dovrebbe lanciare un'eccezione per ID null");

        System.out.println("Validazione ID null per inserisciDescrizione funziona correttamente");
    }

}
