package it.tennis_club.orm;

import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO delle lezioni.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 * 
 * IMPORTANTE: I test di creazione, aggiornamento ed eliminazione
 * modificano il database. Eseguire reset.sql e default.sql tra le esecuzioni
 * se necessario per ripristinare lo stato iniziale.
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

    // Contatore per generare date uniche per le prenotazioni di test
    private static int testCounter = 0;

    @BeforeEach
    void setUp() {
        lezioneDAO = new LezioneDAO();
        prenotazioneDAO = new PrenotazioneDAO();
        utenteDAO = new UtenteDAO();
        campoDAO = new CampoDAO();
    }

    /**
     * Metodo helper per creare una nuova prenotazione di test.
     * Ogni chiamata crea una prenotazione con data e ora uniche per evitare
     * conflitti.
     */
    private Prenotazione createTestPrenotazione() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        Utente socio = utenteDAO.getUtenteById(1);
        if (socio == null) {
            socio = utenteDAO.getUtenteById(2);
        }

        if (campi.isEmpty() || socio == null) {
            fail("Non ci sono campi o utenti disponibili per creare prenotazioni di test");
        }

        // Genera una prenotazione con data/ora uniche
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData(LocalDate.now().plusDays(testCounter + 50)); // +50 per evitare conflitti con dati
                                                                          // esistenti
        prenotazione.setOraInizio(LocalTime.of(10 + (testCounter % 8), 0)); // Varia l'ora
        prenotazione.setCampo(campi.get(testCounter % campi.size())); // Varia il campo
        prenotazione.setSocio(socio);

        Integer id = prenotazioneDAO.createPrenotazione(prenotazione);
        testCounter++;

        return prenotazioneDAO.getPrenotazioneById(id);
    }

    @Test
    @Order(1)
    @DisplayName("Verifica il recupero di tutte le lezioni")
    void testGetAllLezioni() throws SQLException {
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

        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        if (!lezioni.isEmpty()) {
            int id = lezioni.get(0).getId();
            Lezione lezione = lezioneDAO.getLezioneById(id);

            assertNotNull(lezione, "La lezione dovrebbe esistere");
            assertEquals(id, lezione.getId());
            assertNotNull(lezione.getPrenotazione(), "La prenotazione non dovrebbe essere null");
            assertNotNull(lezione.getMaestro(), "Il maestro non dovrebbe essere null");

            System.out.println("Trovata lezione " + lezione);

        } else {
            System.out.println("Nessuna lezione presente nel database per testare getLezioneById");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Verifica che getLezioniById restituisca null per ID inesistente")
    void testGetLezioneByIdNotFound() throws SQLException {
        Lezione lezione = lezioneDAO.getLezioneById(9999);

        assertNull(lezione, "Non dovrebbe trovare una lezione con ID inesistente");

    }

    @Test
    @Order(4)
    @DisplayName("Verifica il recupero di una lezione specificata per una prenotazione")
    void testGetLezioneByPrenotazione() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        if (!lezioni.isEmpty()) {
            int id = lezioni.get(0).getId();
            Lezione lezione = lezioneDAO.getLezioneById(id);
            assertNotNull(lezione, "La lezione dovrebbe esistere");

            for (Lezione l : lezioni) {
                Integer idPrenotazione = l.getPrenotazione().getId();
                l = lezioneDAO.getLezioneByPrenotazione(idPrenotazione);
                assertNotNull(l, "La lezione dovrebbe avere una prenotazione");
                assertEquals(idPrenotazione, l.getPrenotazione().getId());
            }
            System.out.println("Trovata lezioni " + lezione);
        } else {
            System.out.println("Nessuna lezione presente nel database per testare getLezioneByPrenotazione");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Verifica il recupero delle lezioni per maestro")
    void testGetLezioniByMaestro() throws SQLException {
        List<Lezione> tutteLezioni = lezioneDAO.getAllLezioni();

        if (!tutteLezioni.isEmpty()) {
            Integer idMaestro = tutteLezioni.get(0).getMaestro().getId();
            List<Lezione> lezioni = lezioneDAO.getLezioniByMaestro(idMaestro);

            assertNotNull(lezioni, "La lista non dovrebbe essere null");
            assertFalse(lezioni.isEmpty(), "Dovrebbe esserci almeno una lezione");

            // Verifica che tutte le lezioni siano del maestro richiesto
            for (Lezione lezione : lezioni) {
                assertEquals(idMaestro, lezione.getMaestro().getId(),
                        "Tutte le lezioni dovrebbero essere del maestro richiesto");
            }

            System.out.println("Trovate " + lezioni.size() + " lezioni per il maestro ID " + idMaestro);
        } else {
            System.out.println("Nessuna lezione presente nel database per testare getLezioniByMaestro");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Verifica la creazione di una nuova lezione")
    void testCreateLezione() throws SQLException {
        // Crea una nuova prenotazione per evitare violazioni del vincolo UNIQUE
        Prenotazione prenotazione = createTestPrenotazione();
        Utente maestro = utenteDAO.getUtenteById(1);

        if (maestro == null) {
            maestro = utenteDAO.getUtenteById(2);
        }

        if (maestro != null) {
            // Crea una nuova lezione
            Lezione nuovaLezione = new Lezione();
            nuovaLezione.setPrenotazione(prenotazione);
            nuovaLezione.setMaestro(maestro);
            nuovaLezione.setDescrizione("Test Descrizione");

            // Inserisci nel database
            Integer generatedId = lezioneDAO.createLezione(nuovaLezione);

            assertNotNull(generatedId, "L'ID generato non dovrebbe essere null");
            assertTrue(generatedId > 0, "L'ID generato dovrebbe essere positivo");
            assertEquals(generatedId, nuovaLezione.getId(),
                    "L'ID dovrebbe essere stato assegnato all'oggetto");

            // Verifica che la lezione sia stata effettivamente inserita
            Lezione lezioneInserita = lezioneDAO.getLezioneById(generatedId);
            assertNotNull(lezioneInserita, "La lezione dovrebbe essere nel database");
            assertEquals(nuovaLezione.getPrenotazione().getId(), lezioneInserita.getPrenotazione().getId());
            assertEquals(nuovaLezione.getMaestro().getId(), lezioneInserita.getMaestro().getId());
            assertEquals(nuovaLezione.getDescrizione(), lezioneInserita.getDescrizione());

            System.out.println("Lezione creata con ID: " + generatedId);
            System.out.println(lezioneInserita);
        } else {
            fail("Non ci sono maestri disponibili per testare la creazione");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Verifica l'aggiornamento di una lezione")
    void testUpdateLezione() throws SQLException {
        // Crea una nuova prenotazione per evitare violazioni del vincolo UNIQUE
        Prenotazione prenotazione = createTestPrenotazione();
        Utente maestro = utenteDAO.getUtenteById(1);
        if (maestro == null)
            maestro = utenteDAO.getUtenteById(2);

        if (maestro != null) {
            // Crea una lezione temporanea
            Lezione lezione = new Lezione();
            lezione.setPrenotazione(prenotazione);
            lezione.setMaestro(maestro);
            lezione.setDescrizione("Descrizione originale");

            Integer id = lezioneDAO.createLezione(lezione);

            // Modifica i dati
            lezione.setDescrizione("Descrizione aggiornata");

            // Aggiorna nel database
            boolean success = lezioneDAO.updateLezione(lezione);

            assertTrue(success, "L'aggiornamento dovrebbe avere successo");

            // Verifica che le modifiche siano state salvate
            Lezione lezioneAggiornata = lezioneDAO.getLezioneById(id);
            assertEquals("Descrizione aggiornata", lezioneAggiornata.getDescrizione(),
                    "La descrizione dovrebbe essere stata aggiornata");

            System.out.println("Lezione aggiornata: " + lezioneAggiornata);
        } else {
            fail("Non ci sono maestri disponibili per testare l'aggiornamento");
        }
    }

    @Test
    @Order(8)
    @DisplayName("Verifica l'eliminazione di una lezione")
    void testDeleteLezione() throws SQLException {
        // Crea una nuova prenotazione per evitare violazioni del vincolo UNIQUE
        Prenotazione prenotazione = createTestPrenotazione();
        Utente maestro = utenteDAO.getUtenteById(1);
        if (maestro == null)
            maestro = utenteDAO.getUtenteById(2);

        if (maestro != null) {
            // Crea una lezione temporanea
            Lezione lezione = new Lezione();
            lezione.setPrenotazione(prenotazione);
            lezione.setMaestro(maestro);
            lezione.setDescrizione("Questa lezione sarà eliminata");

            Integer id = lezioneDAO.createLezione(lezione);

            // Elimina la lezione
            boolean success = lezioneDAO.deleteLezione(id);

            assertTrue(success, "L'eliminazione dovrebbe avere successo");

            // Verifica che la lezione sia stata davvero eliminata
            Lezione lezioneEliminata = lezioneDAO.getLezioneById(id);
            assertNull(lezioneEliminata,
                    "La lezione dovrebbe essere stata eliminata dal database");

            System.out.println("Lezione con ID " + id + " eliminata con successo");
        } else {
            fail("Non ci sono maestri disponibili per testare l'eliminazione");
        }
    }

    @Test
    @Order(9)
    @DisplayName("Verifica che l'aggiornamento di una lezione inesistente fallisca")
    void testUpdateLezioneNotFound() throws SQLException {
        Lezione lezioneFake = new Lezione();
        lezioneFake.setId(9999);
        lezioneFake.setDescrizione("Fake descrizione");

        // Crea una prenotazione di test e recupera un maestro
        Prenotazione prenotazione = createTestPrenotazione();
        Utente maestro = utenteDAO.getUtenteById(1);

        if (maestro != null) {
            lezioneFake.setPrenotazione(prenotazione);
            lezioneFake.setMaestro(maestro);

            boolean success = lezioneDAO.updateLezione(lezioneFake);

            assertFalse(success, "L'aggiornamento di una lezione inesistente dovrebbe fallire");
        }
    }

    @Test
    @Order(10)
    @DisplayName("Verifica che l'eliminazione di una lezione inesistente fallisca")
    void testDeleteLezioneNotFound() throws SQLException {
        boolean success = lezioneDAO.deleteLezione(9999);

        assertFalse(success, "L'eliminazione di una lezione inesistente dovrebbe fallire");
    }
}
