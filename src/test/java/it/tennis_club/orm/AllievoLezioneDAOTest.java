package it.tennis_club.orm;

import it.tennis_club.domain_model.AllievoLezione;
import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO della relazione allievo-lezione.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 * 
 * 
 * NOTA: AllievoLezioneDAO gestisce la relazione molti-a-molti tra
 * lezioni e allievi, tracciando presenze e feedback personalizzati.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllievoLezioneDAOTest {

    private AllievoLezioneDAO allievoLezioneDAO;
    private LezioneDAO lezioneDAO;
    private UtenteDAO utenteDAO;
    private PrenotazioneDAO prenotazioneDAO;
    private CampoDAO campoDAO;

    private List<Integer> idsLezioniCreate;
    private List<Integer> idsPrenotazioniCreate;

    // Contatore per generare date uniche per le prenotazioni di test
    private static int testCounter = 0;

    @BeforeEach
    void setUp() throws SQLException {
        allievoLezioneDAO = new AllievoLezioneDAO();
        lezioneDAO = new LezioneDAO();
        utenteDAO = new UtenteDAO();
        prenotazioneDAO = new PrenotazioneDAO();
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
     * Crea una nuova lezione di test con prenotazione associata.
     * La lezione viene automaticamente registrata per la pulizia nel tearDown.
     * 
     * @return La lezione creata con ID popolato
     */
    private Lezione createTestLezione() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        List<Utente> maestri = utenteDAO.getUtentiByRuolo(Utente.Ruolo.MAESTRO);

        if (campi.isEmpty() || maestri.isEmpty()) {
            fail("Non ci sono campi o utenti disponibili per creare una lezione");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData(LocalDate.now().plusDays(testCounter + 50));
        prenotazione.setOraInizio(LocalTime.of(10 + (testCounter % 8), 0));
        prenotazione.setCampo(campi.get(testCounter % campi.size()));
        prenotazione.setSocio(maestri.get(testCounter % maestri.size()));

        Integer idPrenotazione = prenotazioneDAO.createPrenotazione(prenotazione);
        if (idPrenotazione != null) {
            idsPrenotazioniCreate.add(idPrenotazione);
        }
        Prenotazione nuovaPrenotazione = prenotazioneDAO.getPrenotazioneById(idPrenotazione);

        Lezione lezione = new Lezione();
        lezione.setPrenotazione(nuovaPrenotazione);
        lezione.setMaestro(maestri.get(testCounter % maestri.size()));
        lezione.setDescrizione("Lezione autogenerata test " + testCounter);

        Integer idLezione = lezioneDAO.createLezione(lezione);
        if (idLezione != null) {
            idsLezioniCreate.add(idLezione);
        }

        testCounter++;
        return lezioneDAO.getLezioneById(idLezione);
    }

    /**
     * Aggiunge un allievo a una lezione in modo sicuro, ignorando l'eccezione
     * se l'allievo è già iscritto.
     * 
     * @param idLezione ID della lezione
     * @param idAllievo ID dell'allievo
     * @return true se l'aggiunta ha avuto successo, false se l'allievo era già
     *         iscritto
     */
    private boolean aggiungiAllievoSafe(Integer idLezione, Integer idAllievo) {
        try {
            allievoLezioneDAO.aggiungiAllievoLezione(idLezione, idAllievo);
            return true;
        } catch (SQLException e) {
            // L'allievo potrebbe essere già iscritto alla lezione
            System.out.println("Allievo ID " + idAllievo + " già iscritto alla lezione ID " + idLezione);
            return false;
        }
    }

    /**
     * Ottiene la lista degli allievi disponibili.
     * Fallisce il test se non ci sono allievi.
     */
    private List<Utente> getAllievi() throws SQLException {
        List<Utente> allievi = utenteDAO.getUtentiByRuolo(Utente.Ruolo.ALLIEVO);
        if (allievi.isEmpty()) {
            fail("Non ci sono allievi disponibili per eseguire il test");
        }
        return allievi;
    }

    // ==================== TEST ====================

    @Test
    @Order(1)
    @DisplayName("Verifica l'aggiunta di un allievo a più lezioni diverse")
    void testAggiungiAllievoAMolteLezioni() throws SQLException {
        // Sono necessarie due lezioni per questo test
        Lezione lezione1 = createTestLezione();
        Lezione lezione2 = createTestLezione();
        assertNotNull(lezione1);
        assertNotNull(lezione2);

        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        assertNotNull(lezioni, "Le lezioni non dovrebbero essere nulle");
        assertTrue(lezioni.size() >= 2, "Servono almeno 2 lezioni nel database per questo test");

        // Prendiamo la lista degli allievi
        List<Utente> allievi = utenteDAO.getUtentiByRuolo(Utente.Ruolo.ALLIEVO);
        assertNotNull(allievi, "Gli allievi non dovrebbero essere nulli");
        assertFalse(allievi.isEmpty(), "Dovrebbe esserci almeno un allievo");

        // Aggiungiamo l'allievo alla prima lezione
        aggiungiAllievoSafe(lezioni.get(0).getId(), allievi.get(0).getId());

        // Aggiungiamo lo stesso allievo alla seconda lezione
        aggiungiAllievoSafe(lezioni.get(1).getId(), allievi.get(0).getId());

        // Verifica: l'allievo deve risultare in almeno 2 lezioni
        List<Lezione> lezioniAllievo = allievoLezioneDAO.getLezioniByAllievo(allievi.get(0).getId());
        assertTrue(lezioniAllievo.size() >= 2, "L'allievo dovrebbe partecipare ad almeno 2 lezioni");

        System.out.println("Test molti-a-molti (1 allievo -> N lezioni) superato.");
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero di più allievi per la stessa lezione")
    void testGetMoltiAllieviPerLezione() throws SQLException {
        Lezione lezione = createTestLezione();
        if (lezione != null) {
            Integer idLezione = lezione.getId();

            List<Utente> allievi = getAllievi();
            assertTrue(allievi.size() >= 2, "Servono almeno 2 allievi per questo test");

            aggiungiAllievoSafe(idLezione, allievi.get(0).getId());
            aggiungiAllievoSafe(idLezione, allievi.get(1).getId());

            List<Utente> allieviLezione = allievoLezioneDAO.getAllieviByLezione(idLezione);
            assertNotNull(allieviLezione);
            assertTrue(allieviLezione.size() >= 2, "La lezione dovrebbe avere almeno 2 allievi");

            System.out.println("Test molti-a-molti (N allievi -> 1 lezione) superato.");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Verifica il recupero delle lezioni per allievo con controlli di integrità")
    void testGetLezioniByAllievoDettagliato() throws SQLException {
        Lezione lezione = createTestLezione();
        List<Utente> allievi = getAllievi();

        for (Utente allievo : allievi) {
            aggiungiAllievoSafe(lezione.getId(), allievo.getId());
        }

        // Verifica l'integrità dei dati per il primo allievo
        List<Lezione> lezioni = allievoLezioneDAO.getLezioniByAllievo(allievi.get(0).getId());
        assertNotNull(lezioni, "La lista delle lezioni non dovrebbe essere null");
        assertFalse(lezioni.isEmpty(), "L'allievo dovrebbe avere almeno una lezione");

        for (Lezione l : lezioni) {
            assertNotNull(l.getId(), "L'ID della lezione non dovrebbe essere null");
            assertNotNull(l.getPrenotazione(), "La lezione deve avere la prenotazione popolata");
            assertNotNull(l.getMaestro(), "La lezione deve avere il maestro popolato");
        }

        System.out.println("Verifica integrità dati lezioni allievo superata.");
    }

    @Test
    @Order(4)
    @DisplayName("Verifica la segnatura della presenza per più allievi e l'isolamento")
    void testSegnaPresenzaMultiplo() throws SQLException {
        Lezione lezione = createTestLezione();
        List<Utente> allievi = getAllievi();
        assertTrue(allievi.size() >= 2, "Servono almeno 2 allievi per questo test");

        // Aggiungi entrambi gli allievi alla lezione
        aggiungiAllievoSafe(lezione.getId(), allievi.get(0).getId());
        aggiungiAllievoSafe(lezione.getId(), allievi.get(1).getId());

        // Allievo 1 -> Assente, Allievo 2 -> Presente
        allievoLezioneDAO.segnaPresenza(lezione.getId(), allievi.get(0).getId(), false);
        allievoLezioneDAO.segnaPresenza(lezione.getId(), allievi.get(1).getId(), true);

        // Verifica isolamento
        AllievoLezione dett1 = allievoLezioneDAO.getAllievoLezione(lezione.getId(), allievi.get(0).getId());
        AllievoLezione dett2 = allievoLezioneDAO.getAllievoLezione(lezione.getId(), allievi.get(1).getId());

        assertFalse(dett1.getPresente(), "Allievo 1 dovrebbe essere assente");
        assertTrue(dett2.getPresente(), "Allievo 2 dovrebbe essere presente");

        System.out.println("Test isolamento presenze superato.");
    }

    @Test
    @Order(5)
    @DisplayName("Verifica l'aggiunta di feedback personalizzati per diversi allievi")
    void testAggiungiFeedbackMultiplo() throws SQLException {
        Lezione lezione = createTestLezione();
        List<Utente> allievi = getAllievi();
        assertTrue(allievi.size() >= 2, "Servono almeno 2 allievi per questo test");

        // Aggiungi gli allievi alla lezione
        for (Utente allievo : allievi) {
            aggiungiAllievoSafe(lezione.getId(), allievo.getId());
        }

        String f1 = "Ottimo dritto per Allievo 1";
        String f2 = "Migliorare il servizio per Allievo 2";

        allievoLezioneDAO.aggiungiFeedback(lezione.getId(), allievi.get(0).getId(), f1);
        allievoLezioneDAO.aggiungiFeedback(lezione.getId(), allievi.get(1).getId(), f2);

        // Recupera i dettagli dal DB per verificare i feedback
        AllievoLezione dett1 = allievoLezioneDAO.getAllievoLezione(lezione.getId(), allievi.get(0).getId());
        AllievoLezione dett2 = allievoLezioneDAO.getAllievoLezione(lezione.getId(), allievi.get(1).getId());

        assertEquals(f1, dett1.getFeedback(), "Il feedback dell'allievo 1 dovrebbe corrispondere");
        assertEquals(f2, dett2.getFeedback(), "Il feedback dell'allievo 2 dovrebbe corrispondere");

        System.out.println("Test feedback multipli e distinti superato.");
    }

    @Test
    @Order(6)
    @DisplayName("Verifica il conteggio degli allievi in una lezione")
    void testContaAllievi() throws SQLException {
        Lezione lezione = createTestLezione();
        List<Utente> allievi = getAllievi();

        // Aggiungi tutti gli allievi alla lezione
        for (Utente allievo : allievi) {
            aggiungiAllievoSafe(lezione.getId(), allievo.getId());
        }

        int count = allievoLezioneDAO.contaAllievi(lezione.getId());
        assertTrue(count >= 0, "Il conteggio dovrebbe essere non negativo");

        // Verifica che corrisponda al numero effettivo
        List<Utente> allieviLezione = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertEquals(allieviLezione.size(), count,
                "Il conteggio dovrebbe corrispondere al numero di allievi recuperati");

        System.out.println("Numero di allievi nella lezione ID " + lezione.getId() + ": " + count);
    }

    @Test
    @Order(7)
    @DisplayName("Verifica il recupero dei dettagli completi di un allievo in una lezione")
    void testGetDettagliAllievoLezione() throws SQLException {
        Lezione lezione = createTestLezione();
        List<Utente> allievi = getAllievi();

        // Aggiungi tutti gli allievi alla lezione
        for (Utente allievo : allievi) {
            aggiungiAllievoSafe(lezione.getId(), allievo.getId());
        }

        List<Utente> allieviLezione = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertFalse(allieviLezione.isEmpty(), "Dovrebbe esserci almeno un allievo nella lezione");

        for (Utente allievoLezione : allieviLezione) {
            AllievoLezione dettagli = allievoLezioneDAO.getAllievoLezione(
                    lezione.getId(), allievoLezione.getId());

            assertNotNull(dettagli, "I dettagli non dovrebbero essere null");
            assertNotNull(dettagli.getId(), "L'ID non dovrebbe essere null");
            assertNotNull(dettagli.getLezione(), "La lezione non dovrebbe essere null");
            assertNotNull(dettagli.getAllievo(), "L'allievo non dovrebbe essere null");
            assertNotNull(dettagli.getPresente(), "Il campo presente non dovrebbe essere null");

            assertEquals(lezione.getId(), dettagli.getLezione().getId(),
                    "L'ID della lezione dovrebbe corrispondere");
            assertEquals(allievoLezione.getId(), dettagli.getAllievo().getId(),
                    "L'ID dell'allievo dovrebbe corrispondere");

            System.out.println("Dettagli allievo-lezione: " + dettagli);
        }
    }

    @Test
    @Order(8)
    @DisplayName("Verifica che getDettagliAllievoLezione restituisca null per combinazione inesistente")
    void testGetDettagliAllievoLezioneNotFound() throws SQLException {
        AllievoLezione dettagli = allievoLezioneDAO.getAllievoLezione(9999, 9999);

        assertNull(dettagli, "Non dovrebbe trovare dettagli per una combinazione inesistente");
    }

    @Test
    @Order(9)
    @DisplayName("Verifica il recupero di tutti gli allievi con dettagli completi")
    void testGetAllieviLezioneConDettagli() throws SQLException {
        Lezione lezione = createTestLezione();
        List<Utente> allievi = getAllievi();

        // Aggiungi tutti gli allievi alla lezione
        for (Utente allievo : allievi) {
            aggiungiAllievoSafe(lezione.getId(), allievo.getId());
        }

        List<AllievoLezione> allieviConDettagli = allievoLezioneDAO
                .getAllieviLezione(lezione.getId());

        assertNotNull(allieviConDettagli, "La lista non dovrebbe essere null");

        // Verifica che ogni elemento sia completo
        for (AllievoLezione al : allieviConDettagli) {
            assertNotNull(al.getId(), "L'ID non dovrebbe essere null");
            assertNotNull(al.getLezione(), "La lezione non dovrebbe essere null");
            assertNotNull(al.getAllievo(), "L'allievo non dovrebbe essere null");
            assertNotNull(al.getPresente(), "Il campo presente non dovrebbe essere null");

            assertEquals(lezione.getId(), al.getLezione().getId(),
                    "Tutti gli elementi dovrebbero riferirsi alla stessa lezione");
        }

        System.out.println("Trovati " + allieviConDettagli.size() +
                " allievi con dettagli per la lezione ID " + lezione.getId());
        allieviConDettagli.forEach(System.out::println);
    }

    @Test
    @Order(10)
    @DisplayName("Verifica la rimozione di un allievo da una lezione")
    void testRimuoviAllievoLezione() throws SQLException {
        Lezione lezione = createTestLezione();
        List<Utente> allievi = getAllievi();

        // Aggiungi tutti gli allievi alla lezione
        for (Utente allievo : allievi) {
            aggiungiAllievoSafe(lezione.getId(), allievo.getId());
        }

        // Conta gli allievi prima della rimozione
        int countPrima = allievoLezioneDAO.contaAllievi(lezione.getId());
        assertTrue(countPrima > 0, "Dovrebbe esserci almeno un allievo da rimuovere");

        // Rimuovi il primo allievo
        boolean success = allievoLezioneDAO.rimuoviAllievoLezione(lezione.getId(), allievi.get(0).getId());
        assertTrue(success, "La rimozione dovrebbe avere successo");

        // Verifica che sia stato effettivamente rimosso
        int countDopo = allievoLezioneDAO.contaAllievi(lezione.getId());
        assertEquals(countPrima - 1, countDopo,
                "Il numero di allievi dovrebbe essere diminuito di 1");

        // Verifica che i dettagli non siano più recuperabili
        AllievoLezione dettagli = allievoLezioneDAO.getAllievoLezione(
                lezione.getId(), allievi.get(0).getId());
        assertNull(dettagli, "I dettagli non dovrebbero più esistere dopo la rimozione");

        System.out.println("Allievo rimosso con successo dalla lezione");
    }

    @Test
    @Order(11)
    @DisplayName("Verifica che la rimozione di un allievo inesistente fallisca")
    void testRimuoviAllievoLezioneNotFound() throws SQLException {
        boolean success = allievoLezioneDAO.rimuoviAllievoLezione(9999, 9999);

        assertFalse(success, "La rimozione di una relazione inesistente dovrebbe fallire");
    }

    @Test
    @Order(12)
    @DisplayName("Verifica che segnaPresenza fallisca per combinazione inesistente")
    void testSegnaPresenzaNotFound() throws SQLException {
        boolean success = allievoLezioneDAO.segnaPresenza(9999, 9999, true);

        assertFalse(success, "La segnatura della presenza dovrebbe fallire per combinazione inesistente");
    }

    @Test
    @Order(13)
    @DisplayName("Verifica che aggiungiFeedback fallisca per combinazione inesistente")
    void testAggiungiFeedbackNotFound() throws SQLException {
        boolean success = allievoLezioneDAO.aggiungiFeedback(9999, 9999, "Feedback test");

        assertFalse(success, "L'aggiunta del feedback dovrebbe fallire per combinazione inesistente");
    }
}
