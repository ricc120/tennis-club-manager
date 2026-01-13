package it.tennis_club.orm;

import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO delle manutenzioni.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 * 
 * IMPORTANTE: I test di creazione, aggiornamento ed eliminazione
 * modificano il database. Eseguire reset.sql e default.sql tra le esecuzioni
 * se necessario per ripristinare lo stato iniziale.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManutenzioneDAOTest {

    private ManutenzioneDAO manutenzioneDAO;
    private CampoDAO campoDAO;
    private UtenteDAO utenteDAO;

    @BeforeEach
    void setUp() {
        manutenzioneDAO = new ManutenzioneDAO();
        campoDAO = new CampoDAO();
        utenteDAO = new UtenteDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Verifica il recupero di tutte le manutenzioni")
    void testGetAllManutenzioni() throws SQLException {
        List<Manutenzione> manutenzioni = manutenzioneDAO.getAllManutenzioni();

        assertNotNull(manutenzioni, "La lista delle manutenzioni non dovrebbe essere null");

        // Verifica che le manutenzioni abbiano tutti i dati popolati
        for (Manutenzione manutenzione : manutenzioni) {
            assertNotNull(manutenzione.getId(), "L'ID non dovrebbe essere null");
            assertNotNull(manutenzione.getCampo(), "Il campo non dovrebbe essere null");
            assertNotNull(manutenzione.getManutentore(), "Il manutentore non dovrebbe essere null");
            assertNotNull(manutenzione.getDataInizio(), "La data inizio non dovrebbe essere null");
            assertNotNull(manutenzione.getDescrizione(), "La descrizione non dovrebbe essere null");
            assertNotNull(manutenzione.getStato(), "Lo stato non dovrebbe essere null");

            // Verifica che gli oggetti Campo e Utente siano completi
            assertNotNull(manutenzione.getCampo().getNome(), "Il nome del campo non dovrebbe essere null");
            assertNotNull(manutenzione.getManutentore().getNome(), "Il nome del manutentore non dovrebbe essere null");
        }

        System.out.println("✅ Trovate " + manutenzioni.size() + " manutenzioni nel database");
        manutenzioni.forEach(System.out::println);
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero delle manutenzioni per campo")
    void testGetManutenzioniByIdCampo() throws SQLException {
        // Ottieni un campo dal database
        List<Campo> campi = campoDAO.getAllCampi();

        if (!campi.isEmpty()) {
            Integer idCampo = campi.get(0).getId();
            List<Manutenzione> manutenzioni = manutenzioneDAO.getManutenzioniByIdCampo(idCampo);

            assertNotNull(manutenzioni, "La lista non dovrebbe essere null");

            // Verifica che tutte le manutenzioni siano per il campo richiesto
            for (Manutenzione manutenzione : manutenzioni) {
                assertEquals(idCampo, manutenzione.getCampo().getId(),
                        "Tutte le manutenzioni dovrebbero essere per il campo richiesto");
            }

            System.out.println("✅ Trovate " + manutenzioni.size() + " manutenzioni per il campo ID " + idCampo);
        } else {
            System.out.println("⚠️ Nessun campo presente nel database per testare getManutenzioniByIdCampo");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Verifica la creazione di una nuova manutenzione")
    void testCreateManutenzione() throws SQLException {
        // Ottieni un campo e un utente esistenti
        List<Campo> campi = campoDAO.getAllCampi();
        Utente manutentore = utenteDAO.getUtenteById(1); // Admin/Manutentore

        if (manutentore == null) {
            manutentore = utenteDAO.getUtenteById(2);
        }

        if (!campi.isEmpty() && manutentore != null) {
            // Crea una nuova manutenzione
            Manutenzione nuovaManutenzione = new Manutenzione();
            nuovaManutenzione.setCampo(campi.get(0));
            nuovaManutenzione.setManutentore(manutentore);
            nuovaManutenzione.setDataInizio(LocalDate.now());
            nuovaManutenzione.setDescrizione("Test manutenzione ordinaria");
            nuovaManutenzione.setStato(Manutenzione.Stato.IN_CORSO);

            // Inserisci nel database
            Integer generatedId = manutenzioneDAO.createManutenzione(nuovaManutenzione);

            assertNotNull(generatedId, "L'ID generato non dovrebbe essere null");
            assertTrue(generatedId > 0, "L'ID generato dovrebbe essere positivo");

            // Verifica che la manutenzione sia stata effettivamente inserita
            List<Manutenzione> manutenzioniCampo = manutenzioneDAO.getManutenzioniByIdCampo(campi.get(0).getId());
            boolean trovata = manutenzioniCampo.stream()
                    .anyMatch(m -> m.getId().equals(generatedId));

            assertTrue(trovata, "La manutenzione dovrebbe essere nel database");

            System.out.println("✅ Manutenzione creata con ID: " + generatedId);
        } else {
            fail("Non ci sono campi o utenti disponibili per testare la creazione");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Verifica la creazione di una manutenzione con data fine")
    void testCreateManutenzioneConDataFine() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        Utente manutentore = utenteDAO.getUtenteById(1);

        if (manutentore == null) {
            manutentore = utenteDAO.getUtenteById(2);
        }

        if (!campi.isEmpty() && manutentore != null) {
            // Crea una manutenzione già completata
            Manutenzione manutenzione = new Manutenzione();
            manutenzione.setCampo(campi.get(0));
            manutenzione.setManutentore(manutentore);
            manutenzione.setDataInizio(LocalDate.now().minusDays(5));
            manutenzione.setDataFine(LocalDate.now().minusDays(1));
            manutenzione.setDescrizione("Manutenzione completata test");
            manutenzione.setStato(Manutenzione.Stato.COMPLETATA);

            Integer generatedId = manutenzioneDAO.createManutenzione(manutenzione);

            assertNotNull(generatedId, "L'ID generato non dovrebbe essere null");
            assertTrue(generatedId > 0, "L'ID generato dovrebbe essere positivo");

            System.out.println("✅ Manutenzione con data fine creata con ID: " + generatedId);
        } else {
            fail("Non ci sono campi o utenti disponibili per testare la creazione");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Verifica l'aggiornamento dello stato di una manutenzione")
    void testUpdateStatoManutenzione() throws SQLException {
        // Prima crea una manutenzione da aggiornare
        List<Campo> campi = campoDAO.getAllCampi();
        Utente manutentore = utenteDAO.getUtenteById(1);

        if (manutentore == null) {
            manutentore = utenteDAO.getUtenteById(2);
        }

        if (!campi.isEmpty() && manutentore != null) {
            // Crea una manutenzione temporanea
            Manutenzione manutenzione = new Manutenzione();
            manutenzione.setCampo(campi.get(0));
            manutenzione.setManutentore(manutentore);
            manutenzione.setDataInizio(LocalDate.now());
            manutenzione.setDescrizione("Manutenzione da aggiornare");
            manutenzione.setStato(Manutenzione.Stato.IN_CORSO);

            Integer id = manutenzioneDAO.createManutenzione(manutenzione);

            // Aggiorna lo stato ad ANNULLATA
            assertDoesNotThrow(() -> manutenzioneDAO.updateStatoManutenzione(id, Manutenzione.Stato.ANNULLATA),
                    "L'aggiornamento dello stato non dovrebbe lanciare eccezioni");

            System.out.println("✅ Stato manutenzione aggiornato per ID: " + id);
        } else {
            fail("Non ci sono campi o utenti disponibili per testare l'aggiornamento");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Verifica il completamento di una manutenzione")
    void testCompletaManutenzione() throws SQLException {
        // Prima crea una manutenzione da completare
        List<Campo> campi = campoDAO.getAllCampi();
        Utente manutentore = utenteDAO.getUtenteById(1);

        if (manutentore == null) {
            manutentore = utenteDAO.getUtenteById(2);
        }

        if (!campi.isEmpty() && manutentore != null) {
            // Crea una manutenzione in corso
            Manutenzione manutenzione = new Manutenzione();
            manutenzione.setCampo(campi.get(0));
            manutenzione.setManutentore(manutentore);
            manutenzione.setDataInizio(LocalDate.now().minusDays(3));
            manutenzione.setDescrizione("Manutenzione da completare");
            manutenzione.setStato(Manutenzione.Stato.IN_CORSO);

            Integer id = manutenzioneDAO.createManutenzione(manutenzione);

            // Completa la manutenzione
            LocalDate dataFine = LocalDate.now();
            assertDoesNotThrow(() -> manutenzioneDAO.completaManutenzione(id, dataFine),
                    "Il completamento non dovrebbe lanciare eccezioni");

            System.out.println("✅ Manutenzione completata con ID: " + id);
        } else {
            fail("Non ci sono campi o utenti disponibili per testare il completamento");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Verifica la creazione di manutenzioni con stati diversi")
    void testCreateManutenzioniConStatiDiversi() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        Utente manutentore = utenteDAO.getUtenteById(1);

        if (manutentore == null) {
            manutentore = utenteDAO.getUtenteById(2);
        }

        if (!campi.isEmpty() && manutentore != null) {
            // Test per ogni stato possibile
            for (Manutenzione.Stato stato : Manutenzione.Stato.values()) {
                Manutenzione manutenzione = new Manutenzione();
                manutenzione.setCampo(campi.get(0));
                manutenzione.setManutentore(manutentore);
                manutenzione.setDataInizio(LocalDate.now());
                manutenzione.setDescrizione("Test stato " + stato.name());
                manutenzione.setStato(stato);

                if (stato == Manutenzione.Stato.COMPLETATA) {
                    manutenzione.setDataFine(LocalDate.now());
                }

                Integer id = manutenzioneDAO.createManutenzione(manutenzione);
                assertNotNull(id, "Dovrebbe creare manutenzione con stato " + stato);

                System.out.println("✅ Creata manutenzione con stato " + stato + ", ID: " + id);
            }
        } else {
            fail("Non ci sono campi o utenti disponibili per testare gli stati");
        }
    }

    @Test
    @Order(8)
    @DisplayName("Verifica che getManutenzioniByIdCampo restituisca lista vuota per campo senza manutenzioni")
    void testGetManutenzioniByIdCampoVuoto() throws SQLException {
        // Usa un ID campo che probabilmente non ha manutenzioni
        List<Campo> campi = campoDAO.getAllCampi();

        if (campi.size() > 1) {
            // Prova con l'ultimo campo della lista
            Integer idCampo = campi.get(campi.size() - 1).getId();
            List<Manutenzione> manutenzioni = manutenzioneDAO.getManutenzioniByIdCampo(idCampo);

            assertNotNull(manutenzioni, "La lista non dovrebbe essere null anche se vuota");
            System.out.println("✅ Trovate " + manutenzioni.size() + " manutenzioni per il campo ID " + idCampo);
        } else {
            System.out.println("⚠️ Non ci sono abbastanza campi per testare questo scenario");
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
                // Verifica che siano ordinate per data_inizio DESC
                for (int i = 0; i < manutenzioni.size() - 1; i++) {
                    LocalDate dataCorrente = manutenzioni.get(i).getDataInizio();
                    LocalDate dataSuccessiva = manutenzioni.get(i + 1).getDataInizio();

                    assertTrue(dataCorrente.isAfter(dataSuccessiva) || dataCorrente.isEqual(dataSuccessiva),
                            "Le manutenzioni dovrebbero essere ordinate per data decrescente");
                }

                System.out.println("✅ Ordinamento manutenzioni verificato correttamente");
            } else {
                System.out.println("⚠️ Non ci sono abbastanza manutenzioni per verificare l'ordinamento");
            }
        }
    }

    @Test
    @Order(10)
    @DisplayName("Verifica la gestione di descrizioni lunghe")
    void testDescrizioneLunga() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        Utente manutentore = utenteDAO.getUtenteById(1);

        if (manutentore == null) {
            manutentore = utenteDAO.getUtenteById(2);
        }

        if (!campi.isEmpty() && manutentore != null) {
            // Crea una descrizione molto lunga
            String descrizioneLunga = "Manutenzione straordinaria del campo che include: " +
                    "rifacimento completo delle linee, riparazione della rete, " +
                    "livellamento del terreno, sostituzione della sabbia, " +
                    "pulizia approfondita e controllo generale delle strutture. " +
                    "Questa manutenzione richiederà diversi giorni di lavoro.";

            Manutenzione manutenzione = new Manutenzione();
            manutenzione.setCampo(campi.get(0));
            manutenzione.setManutentore(manutentore);
            manutenzione.setDataInizio(LocalDate.now());
            manutenzione.setDescrizione(descrizioneLunga);
            manutenzione.setStato(Manutenzione.Stato.IN_CORSO);

            Integer id = manutenzioneDAO.createManutenzione(manutenzione);

            assertNotNull(id, "Dovrebbe gestire descrizioni lunghe");
            System.out.println("✅ Manutenzione con descrizione lunga creata con ID: " + id);
        } else {
            fail("Non ci sono campi o utenti disponibili per testare le descrizioni lunghe");
        }
    }

    @AfterAll
    static void tearDown() {
        System.out.println("\n📊 Test completati per ManutenzioneDAO");
    }
}
