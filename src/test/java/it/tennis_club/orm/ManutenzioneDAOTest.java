package it.tennis_club.orm;

import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO delle manutenzioni.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManutenzioneDAOTest {

    private ManutenzioneDAO manutenzioneDAO;
    private CampoDAO campoDAO;
    private UtenteDAO utenteDAO;

    private List<Integer> idsManutenzioniCreate;

    @BeforeEach
    void setUp() {
        manutenzioneDAO = new ManutenzioneDAO();
        campoDAO = new CampoDAO();
        utenteDAO = new UtenteDAO();
        idsManutenzioniCreate = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws SQLException {
        for (Integer id : idsManutenzioniCreate) {
            manutenzioneDAO.deleteManutenzioni(id);
        }
        idsManutenzioniCreate.clear();
    }

    // ==================== METODI HELPER ====================

    /**
     * Crea una nuova manutenzione di test con stato IN_CORSO.
     * La manutenzione viene automaticamente registrata per la pulizia nel tearDown.
     * 
     * @param manutentore L'utente che esegue la manutenzione (MANUTENTORE o ADMIN)
     * @return La manutenzione creata con ID popolato
     */
    private Manutenzione createTestManutenzione(Utente manutentore) throws SQLException {
        return createTestManutenzione(manutentore, Manutenzione.Stato.IN_CORSO, null);
    }

    /**
     * Crea una nuova manutenzione di test con stato e descrizione personalizzati.
     * La manutenzione viene automaticamente registrata per la pulizia nel tearDown.
     * 
     * @param manutentore L'utente che esegue la manutenzione (MANUTENTORE o ADMIN)
     * @param stato       Lo stato della manutenzione
     * @param descrizione La descrizione (se null, usa una descrizione di default)
     * @return La manutenzione creata con ID popolato
     */
    private Manutenzione createTestManutenzione(Utente manutentore, Manutenzione.Stato stato, String descrizione)
            throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        if (campi.isEmpty()) {
            fail("Non ci sono campi disponibili per creare manutenzioni di test");
        }

        Manutenzione manutenzione = new Manutenzione();
        manutenzione.setCampo(campi.get(0));
        manutenzione.setManutentore(manutentore);
        manutenzione.setDataInizio(LocalDate.now());
        manutenzione.setDescrizione(descrizione != null ? descrizione : "Test manutenzione");
        manutenzione.setStato(stato);

        if (stato == Manutenzione.Stato.COMPLETATA) {
            manutenzione.setDataFine(LocalDate.now());
        }

        Integer id = manutenzioneDAO.createManutenzione(manutenzione);
        assertNotNull(id, "L'ID generato non dovrebbe essere null");
        assertTrue(id > 0, "L'ID generato dovrebbe essere positivo");

        idsManutenzioniCreate.add(id);
        return manutenzioneDAO.getManutenzioneById(id);
    }

    /**
     * Ottiene la lista di tutti gli utenti autorizzati a creare manutenzioni
     * (MANUTENTORE e ADMIN).
     */
    private List<Utente> getUtentiAutorizzati() throws SQLException {
        List<Utente> autorizzati = new ArrayList<>();
        autorizzati.addAll(utenteDAO.getUtentiByRuolo(Utente.Ruolo.MANUTENTORE));
        autorizzati.addAll(utenteDAO.getUtentiByRuolo(Utente.Ruolo.ADMIN));

        if (autorizzati.isEmpty()) {
            fail("Non ci sono manutentori o admin disponibili per eseguire il test");
        }

        return autorizzati;
    }

    // ==================== TEST ====================

    @Test
    @Order(1)
    @DisplayName("Verifica il recupero di tutte le manutenzioni")
    void testGetAllManutenzioni() throws SQLException {
        List<Manutenzione> manutenzioni = manutenzioneDAO.getAllManutenzioni();

        assertNotNull(manutenzioni, "La lista delle manutenzioni non dovrebbe essere null");

        for (Manutenzione manutenzione : manutenzioni) {
            assertNotNull(manutenzione.getId(), "L'ID non dovrebbe essere null");
            assertNotNull(manutenzione.getCampo(), "Il campo non dovrebbe essere null");
            assertNotNull(manutenzione.getManutentore(), "Il manutentore non dovrebbe essere null");
            assertNotNull(manutenzione.getDataInizio(), "La data inizio non dovrebbe essere null");
            assertNotNull(manutenzione.getDescrizione(), "La descrizione non dovrebbe essere null");
            assertNotNull(manutenzione.getStato(), "Lo stato non dovrebbe essere null");
            assertNotNull(manutenzione.getCampo().getNome(), "Il nome del campo non dovrebbe essere null");
            assertNotNull(manutenzione.getManutentore().getNome(), "Il nome del manutentore non dovrebbe essere null");
        }

        System.out.println("Trovate " + manutenzioni.size() + " manutenzioni nel database");
        manutenzioni.forEach(System.out::println);
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero delle manutenzioni per campo")
    void testGetManutenzioniByIdCampo() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();

        if (!campi.isEmpty()) {
            Integer idCampo = campi.get(0).getId();
            List<Manutenzione> manutenzioni = manutenzioneDAO.getManutenzioniByIdCampo(idCampo);

            assertNotNull(manutenzioni, "La lista non dovrebbe essere null");

            for (Manutenzione manutenzione : manutenzioni) {
                assertEquals(idCampo, manutenzione.getCampo().getId(),
                        "Tutte le manutenzioni dovrebbero essere per il campo richiesto");
            }

            System.out.println("Trovate " + manutenzioni.size() + " manutenzioni per il campo ID " + idCampo);
        } else {
            System.out.println("Nessun campo presente nel database per testare getManutenzioniByIdCampo");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Verifica la creazione di una nuova manutenzione")
    void testCreateManutenzione() throws SQLException {
        List<Utente> utentiAutorizzati = getUtentiAutorizzati();

        for (Utente manutentore : utentiAutorizzati) {
            Manutenzione manutenzione = createTestManutenzione(manutentore);

            assertNotNull(manutenzione, "La manutenzione non dovrebbe essere null");
            assertNotNull(manutenzione.getId(), "L'ID non dovrebbe essere null");

            // Verifica che sia nel database
            List<Manutenzione> manutenzioniCampo = manutenzioneDAO.getManutenzioniByIdCampo(
                    manutenzione.getCampo().getId());
            boolean trovata = manutenzioniCampo.stream()
                    .anyMatch(m -> m.getId().equals(manutenzione.getId()));
            assertTrue(trovata, "La manutenzione dovrebbe essere nel database");

            System.out.println("Manutenzione creata con ID: " + manutenzione.getId() +
                    " (ruolo: " + manutentore.getRuolo() + ")");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Verifica la creazione di una manutenzione con data fine")
    void testCreateManutenzioneConDataFine() throws SQLException {
        List<Utente> utentiAutorizzati = getUtentiAutorizzati();

        for (Utente manutentore : utentiAutorizzati) {
            Manutenzione manutenzione = createTestManutenzione(
                    manutentore,
                    Manutenzione.Stato.COMPLETATA,
                    "Test manutenzione completata");

            assertNotNull(manutenzione, "La manutenzione non dovrebbe essere null");
            assertEquals(Manutenzione.Stato.COMPLETATA, manutenzione.getStato(),
                    "Lo stato dovrebbe essere COMPLETATA");

            System.out.println("Manutenzione con data fine creata con ID: " + manutenzione.getId());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Verifica l'aggiornamento dello stato di una manutenzione")
    void testUpdateStatoManutenzione() throws SQLException {
        List<Utente> utentiAutorizzati = getUtentiAutorizzati();

        for (Utente manutentore : utentiAutorizzati) {
            Manutenzione manutenzione = createTestManutenzione(manutentore);

            boolean success = manutenzioneDAO.updateStatoManutenzione(
                    manutenzione.getId(), Manutenzione.Stato.ANNULLATA);
            assertTrue(success, "L'aggiornamento dello stato dovrebbe avere successo");

            Manutenzione manutenzioneAggiornata = manutenzioneDAO.getManutenzioneById(manutenzione.getId());
            assertEquals(Manutenzione.Stato.ANNULLATA, manutenzioneAggiornata.getStato(),
                    "Lo stato della manutenzione dovrebbe essere ANNULLATA");

            System.out.println("Stato manutenzione aggiornato per ID: " + manutenzione.getId());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Verifica il completamento di una manutenzione")
    void testCompletaManutenzione() throws SQLException {
        List<Utente> utentiAutorizzati = getUtentiAutorizzati();

        for (Utente manutentore : utentiAutorizzati) {
            Manutenzione manutenzione = createTestManutenzione(manutentore);

            LocalDate dataFine = LocalDate.now();
            assertDoesNotThrow(() -> manutenzioneDAO.completaManutenzione(manutenzione.getId(), dataFine),
                    "Il completamento non dovrebbe lanciare eccezioni");

            Manutenzione manutenzioneCompletata = manutenzioneDAO.getManutenzioneById(manutenzione.getId());
            assertEquals(Manutenzione.Stato.COMPLETATA, manutenzioneCompletata.getStato(),
                    "Lo stato della manutenzione dovrebbe essere COMPLETATA");

            System.out.println("Manutenzione completata per ID: " + manutenzione.getId());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Verifica la creazione di manutenzioni con stati diversi")
    void testCreateManutenzioniConStatiDiversi() throws SQLException {
        List<Utente> utentiAutorizzati = getUtentiAutorizzati();

        // Usa il primo utente autorizzato per testare tutti gli stati
        Utente manutentore = utentiAutorizzati.get(0);

        for (Manutenzione.Stato stato : Manutenzione.Stato.values()) {
            Manutenzione manutenzione = createTestManutenzione(
                    manutentore, stato, "Test stato " + stato.name());

            assertNotNull(manutenzione, "La manutenzione non dovrebbe essere null");
            assertEquals(stato, manutenzione.getStato(), "Lo stato dovrebbe corrispondere");

            System.out.println("Creata manutenzione con stato " + stato + ", ID: " + manutenzione.getId());
        }
    }

    @Test
    @Order(8)
    @DisplayName("Verifica che getManutenzioniByIdCampo restituisca lista vuota per campo senza manutenzioni")
    void testGetManutenzioniByIdCampoVuoto() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();

        if (!campi.isEmpty()) {
            // Prova con l'ultimo campo della lista
            Integer idCampo = campi.get(campi.size() - 1).getId();
            List<Manutenzione> manutenzioni = manutenzioneDAO.getManutenzioniByIdCampo(idCampo);

            assertNotNull(manutenzioni, "La lista non dovrebbe essere null anche se vuota");
            System.out.println("Trovate " + manutenzioni.size() + " manutenzioni per il campo ID " + idCampo);
        } else {
            System.out.println("Non ci sono abbastanza campi per testare questo scenario");
        }
    }

    @Test
    @Order(9)
    @DisplayName("Verifica l'ordinamento delle manutenzioni per data")
    void testOrdinamentoManutenzioni() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();

        if (!campi.isEmpty()) {
            Integer idCampo = campi.get(0).getId();
            List<Manutenzione> manutenzioni = manutenzioneDAO.getManutenzioniByIdCampo(idCampo);

            if (manutenzioni.size() > 1) {
                for (int i = 0; i < manutenzioni.size() - 1; i++) {
                    LocalDate dataCorrente = manutenzioni.get(i).getDataInizio();
                    LocalDate dataSuccessiva = manutenzioni.get(i + 1).getDataInizio();

                    assertTrue(dataCorrente.isAfter(dataSuccessiva) || dataCorrente.isEqual(dataSuccessiva),
                            "Le manutenzioni dovrebbero essere ordinate per data decrescente");
                }

                System.out.println("Ordinamento manutenzioni verificato correttamente");
            } else {
                System.out.println("Non ci sono abbastanza manutenzioni per verificare l'ordinamento");
            }
        }
    }

    @Test
    @Order(10)
    @DisplayName("Verifica la gestione di descrizioni lunghe")
    void testDescrizioneLunga() throws SQLException {
        List<Utente> utentiAutorizzati = getUtentiAutorizzati();

        String descrizioneLunga = "Manutenzione straordinaria del campo che include: " +
                "rifacimento completo delle linee, riparazione della rete, " +
                "livellamento del terreno, sostituzione della sabbia, " +
                "pulizia approfondita e controllo generale delle strutture. " +
                "Questa manutenzione richiederà diversi giorni di lavoro.";

        Utente manutentore = utentiAutorizzati.get(0);
        Manutenzione manutenzione = createTestManutenzione(manutentore, Manutenzione.Stato.IN_CORSO, descrizioneLunga);

        assertNotNull(manutenzione, "La manutenzione non dovrebbe essere null");
        assertEquals(descrizioneLunga, manutenzione.getDescrizione(), "La descrizione dovrebbe corrispondere");

        System.out.println("Manutenzione con descrizione lunga creata con ID: " + manutenzione.getId());
    }

    @Test
    @Order(11)
    @DisplayName("Verifica il corretto recupero di una manutenzione per il suo ID")
    void testGetManutenzioneById() throws SQLException {
        List<Utente> utentiAutorizzati = getUtentiAutorizzati();

        for (Utente manutentore : utentiAutorizzati) {
            Manutenzione manutenzioneCreata = createTestManutenzione(manutentore);

            Manutenzione manutenzioneRecuperata = manutenzioneDAO.getManutenzioneById(manutenzioneCreata.getId());

            assertNotNull(manutenzioneRecuperata, "La manutenzione non dovrebbe essere null");
            assertEquals(manutenzioneCreata.getId(), manutenzioneRecuperata.getId(),
                    "L'ID della manutenzione dovrebbe corrispondere");

            System.out.println("Manutenzione con ID: " + manutenzioneCreata.getId() + " recuperata con successo");
        }
    }

    @Test
    @Order(12)
    @DisplayName("Verifica che getManutenzioneById restituisca null per ID inesistente")
    void testGetManutenzioneByIdInesistente() throws SQLException {
        Manutenzione manutenzione = manutenzioneDAO.getManutenzioneById(9999);
        assertNull(manutenzione, "La manutenzione dovrebbe essere null");
    }
}
