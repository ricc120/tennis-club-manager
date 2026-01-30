package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.AllievoLezioneDAO;
import it.tennis_club.orm.CampoDAO;
import it.tennis_club.orm.PrenotazioneDAO;
import it.tennis_club.orm.UtenteDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private static AllievoLezioneDAO allievoLezioneDAO;

    private static Campo campoTest;
    private static Utente utenteTest;
    private static Utente utenteAllievo;

    @BeforeAll
    public static void setUp() throws SQLException {
        accademiaService = new AccademiaService();
        campoDAO = new CampoDAO();
        prenotazioneDAO = new PrenotazioneDAO();
        utenteDAO = new UtenteDAO();
        allievoLezioneDAO = new AllievoLezioneDAO();

        List<Campo> campi = campoDAO.getAllCampi();
        assertFalse(campi.isEmpty(), "Il database dovrebbe contenere una lista di campi");
        campoTest = campi.get(0);

        utenteTest = utenteDAO.getUtenteById(1);
        assertNotNull(utenteTest, "Il database dovrebbe contenere l'utente con ID 1");
        utenteTest.setRuolo(Ruolo.MAESTRO);

        // Recupera un secondo utente per usarlo come allievo
        utenteAllievo = utenteDAO.getUtenteById(2);
        if (utenteAllievo == null) {
            utenteAllievo = utenteTest; // Fallback al primo utente se non esiste il secondo
        }
        assertNotNull(utenteAllievo, "Il database dovrebbe contenere almeno un utente per allievo");
        // Imposta il ruolo ALLIEVO per poter partecipare alle lezioni
        utenteAllievo.setRuolo(Ruolo.ALLIEVO);
    }

    @BeforeEach
    public void cleanupBeforeTest() throws SQLException {
        // Pulisce tutte le associazioni allievo-lezione e prenotazioni future
        // per evitare conflitti tra esecuzioni multiple dei test
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        try {
            conn = it.tennis_club.orm.ConnectionManager.getConnection();

            // Prima elimina le associazioni allievo-lezione
            String queryAllievi = "DELETE FROM allievo_lezione";
            stmt = conn.prepareStatement(queryAllievi);
            int deletedAllievi = stmt.executeUpdate();
            System.out.println("🧹 Pulite " + deletedAllievi + " associazioni allievo-lezione prima del test");
            stmt.close();

            // Poi elimina le lezioni future
            String queryLezioni = "DELETE FROM lezione WHERE id IN (SELECT l.id FROM lezione l JOIN prenotazione p ON l.id_prenotazione = p.id WHERE p.data >= CURRENT_DATE)";
            stmt = conn.prepareStatement(queryLezioni);
            int deletedLezioni = stmt.executeUpdate();
            System.out.println("🧹 Pulite " + deletedLezioni + " lezioni future prima del test");
            stmt.close();

            // Infine elimina le prenotazioni future
            String queryPrenotazioni = "DELETE FROM prenotazione WHERE data >= CURRENT_DATE";
            stmt = conn.prepareStatement(queryPrenotazioni);
            int deletedPrenotazioni = stmt.executeUpdate();
            System.out.println("🧹 Pulite " + deletedPrenotazioni + " prenotazioni future prima del test");
        } finally {
            if (stmt != null)
                stmt.close();
            it.tennis_club.orm.ConnectionManager.closeConnection(conn);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test creazione lezione valida")
    void testCreateLezione() throws AccademiaException, PrenotazioneException {
        LocalDate dataFutura = LocalDate.now().plusDays(100);
        LocalTime oraFutura = LocalTime.now();

        Integer idPrenotazioneLezione = accademiaService.createLezione(dataFutura, oraFutura, campoTest, utenteTest,
                "Descrizione test");
        assertNotNull(idPrenotazioneLezione,
                "L'ID della prenotazione inerente alla lezione creata non dovrebbe essere null");
        assertTrue(idPrenotazioneLezione > 0,
                "L'ID della prenotazione inerente alla lezione creata dovrebbe essere positivo");

        System.out.println("Lezione creata con ID: " + idPrenotazioneLezione);

    }

    @Test
    @Order(2)
    @DisplayName("Test inserimento descrizione lezione valida")
    void testInserisciDescrizione() throws AccademiaException, PrenotazioneException {
        accademiaService.createLezione(LocalDate.now().plusDays(10), LocalTime.now(), campoTest, utenteTest,
                "Descrzione Test");
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
        accademiaService.createLezione(LocalDate.now().plusDays(10), LocalTime.now(), campoTest, utenteTest,
                "Descrzione Test");
        List<Lezione> lezioni = accademiaService.getAllLezioni();
        if (!lezioni.isEmpty()) {
            for (Lezione l : lezioni) {
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
        accademiaService.createLezione(LocalDate.now().plusDays(10), LocalTime.now(), campoTest, utenteTest,
                "Descrzione Test");
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
        accademiaService.createLezione(LocalDate.now().plusDays(10), LocalTime.now(), campoTest, utenteTest,
                "Descrzione Test");
        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertNotNull(lezioni, "La lista di lezioni non dovrebbe essere null");
        assertFalse(lezioni.isEmpty(), "La lista di lezioni non dovrebbe essere vuota");

        System.out.println("Tutte le lezioni recuperate con successo");
    }

    @Test
    @Order(6)
    @DisplayName("Test recupero di una lezione per Maestro")
    void testGetLezioneByMaestro() throws AccademiaException, PrenotazioneException {
        accademiaService.createLezione(LocalDate.now().plusDays(10), LocalTime.now(), campoTest, utenteTest,
                "Descrzione Test");
        List<Lezione> lezioni = accademiaService.getLezioneByMaestro(utenteTest);
        assertNotNull(lezioni, "La lista di lezioni non dovrebbe essere null");
        for (Lezione l : lezioni) {
            assertNotNull(l.getMaestro(), "La lezione non dovrebbe avere il maestro associato null");
            assertEquals(utenteTest.getId(), l.getMaestro().getId());
        }

        System.out.println(lezioni.size() + " lezioni con maestro recuperate correttamente");

    }

    @Test
    @Order(7)
    @DisplayName("Test recupero di una lezione per Prenotazione")
    void testGetLezioneByPrenotazione() throws AccademiaException, PrenotazioneException, SQLException {
        accademiaService.createLezione(LocalDate.now().plusDays(10), LocalTime.now(),
                campoTest, utenteTest,
                "Descrzione Test");
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
        // Arrange - Crea una nuova lezione per questo test
        Integer idPrenotazione = accademiaService.createLezione(
                LocalDate.now().plusDays(50), LocalTime.now(), campoTest, utenteTest, "Lezione per test allievo");
        assertNotNull(idPrenotazione, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertNotNull(lezioni, "La lista di lezioni non dovrebbe essere null");
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");

        Lezione lezione = lezioni.get(0);

        // Act - Aggiungi l'allievo
        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievo);

        // Assert - Verifica che l'allievo sia stato aggiunto
        List<Utente> allievi = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertNotNull(allievi, "La lista degli allievi non dovrebbe essere null");
        assertFalse(allievi.isEmpty(), "L'allievo dovrebbe essere stato aggiunto alla lezione");

        System.out.println(
                "✅ Allievo " + utenteAllievo.getId() + " aggiunto alla lezione " + lezione.getId() + " con successo");
    }

    @Test
    @Order(9)
    @DisplayName("Test rimozione allievo da una lezione")
    void testRimuoviAllievo() throws AccademiaException, PrenotazioneException, SQLException {
        // Arrange - Crea una lezione e aggiungi un allievo
        Integer idPrenotazione = accademiaService.createLezione(
                LocalDate.now().plusDays(55), LocalTime.now(), campoTest, utenteTest,
                "Lezione per test rimozione allievo");
        assertNotNull(idPrenotazione, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        // Aggiungi l'allievo prima di rimuoverlo
        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievo);

        // Verifica che l'allievo sia presente
        List<?> allieviPrima = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertFalse(allieviPrima.isEmpty(), "L'allievo dovrebbe essere presente prima della rimozione");

        // Act - Rimuovi l'allievo
        boolean risultato = accademiaService.rimuoviAllievo(lezione.getId(), utenteAllievo.getId());

        // Assert
        assertTrue(risultato, "La rimozione dovrebbe avere successo");

        // Verifica che l'allievo sia stato rimosso
        List<?> allieviDopo = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertTrue(allieviDopo.isEmpty(), "L'allievo dovrebbe essere stato rimosso dalla lezione");

        System.out.println(
                "✅ Allievo " + utenteAllievo.getId() + " rimosso dalla lezione " + lezione.getId() + " con successo");
    }

    @Test
    @Order(10)
    @DisplayName("Test recupero di tutte le lezioni di un allievo")
    void testGetLezioniAllievo() throws AccademiaException, PrenotazioneException {
        // Arrange - Crea una lezione e aggiungi l'allievo
        Integer idPrenotazione = accademiaService.createLezione(
                LocalDate.now().plusDays(60), LocalTime.now(), campoTest, utenteTest,
                "Lezione per test getLezioniAllievo");
        assertNotNull(idPrenotazione, "La lezione dovrebbe essere stata creata");

        List<Lezione> tutte = accademiaService.getAllLezioni();
        assertFalse(tutte.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = tutte.get(0);

        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievo);

        // Act
        List<Lezione> lezioniAllievo = accademiaService.getLezioniAllievo(utenteAllievo);

        // Assert
        assertNotNull(lezioniAllievo, "La lista di lezioni per l'allievo non dovrebbe essere null");
        assertFalse(lezioniAllievo.isEmpty(), "L'allievo dovrebbe avere almeno una lezione");

        System.out.println("✅ Recuperate " + lezioniAllievo.size() + " lezioni per l'allievo " + utenteAllievo.getId());
    }

    @Test
    @Order(11)
    @DisplayName("Test conteggio di allievi in una lezione")
    void testContaAllievi() throws AccademiaException, PrenotazioneException {
        // Arrange - Crea una lezione e aggiungi un allievo
        Integer idPrenotazione = accademiaService.createLezione(
                LocalDate.now().plusDays(65), LocalTime.now(), campoTest, utenteTest,
                "Lezione per test contaAllievi");
        assertNotNull(idPrenotazione, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        // Conta prima di aggiungere (dovrebbe essere 0)
        int countPrima = accademiaService.contaAllievi(lezione.getId());
        assertEquals(0, countPrima, "La lezione dovrebbe avere 0 allievi inizialmente");

        // Aggiungi un allievo
        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievo);

        // Act
        int countDopo = accademiaService.contaAllievi(lezione.getId());

        // Assert
        assertEquals(1, countDopo, "La lezione dovrebbe avere 1 allievo dopo l'aggiunta");

        System.out.println("✅ Conteggio allievi eseguito con successo: " + countDopo + " allievo/i");
    }

    @Test
    @Order(12)
    @DisplayName("Test per segnare la presenza di un allievo a lezione")
    void testSegnaPresenza() throws AccademiaException, SQLException, PrenotazioneException {
        // Arrange - Crea una lezione e aggiungi un allievo
        Integer idPrenotazione = accademiaService.createLezione(
                LocalDate.now().plusDays(70), LocalTime.now(), campoTest, utenteTest,
                "Lezione per test segnaPresenza");
        assertNotNull(idPrenotazione, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        // Aggiungi l'allievo alla lezione
        accademiaService.aggiungiAllievo(lezione.getId(), utenteAllievo);

        // Verifica che l'allievo sia stato aggiunto correttamente
        List<?> allievi = allievoLezioneDAO.getAllieviByLezione(lezione.getId());
        assertNotNull(allievi, "La lista degli allievi non dovrebbe essere null");
        assertFalse(allievi.isEmpty(), "L'allievo dovrebbe essere stato aggiunto alla lezione");

        // Act - Segna la presenza come true
        boolean risultatoPresente = accademiaService.segnaPresenza(lezione.getId(), utenteAllievo.getId(), true);

        // Assert
        assertTrue(risultatoPresente, "La presenza dovrebbe essere stata segnata con successo");

        // Act - Segna la presenza come false (assente)
        boolean risultatoAssente = accademiaService.segnaPresenza(lezione.getId(), utenteAllievo.getId(), false);
        assertTrue(risultatoAssente, "L'assenza dovrebbe essere stata segnata con successo");

        System.out.println("✅ Presenza/assenza segnata con successo per l'allievo " + utenteAllievo.getId());
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
                () -> accademiaService.createLezione(null, oraValida, campoTest, utenteTest, "Descrizione"),
                "Dovrebbe lanciare eccezione con data null");

        // Test con ora null
        assertThrows(AccademiaException.class,
                () -> accademiaService.createLezione(dataFutura, null, campoTest, utenteTest, "Descrizione"),
                "Dovrebbe lanciare eccezione con ora null");

        // Test con campo null
        assertThrows(AccademiaException.class,
                () -> accademiaService.createLezione(dataFutura, oraValida, null, utenteTest, "Descrizione"),
                "Dovrebbe lanciare eccezione con campo null");

        // Test con maestro null
        assertThrows(AccademiaException.class,
                () -> accademiaService.createLezione(dataFutura, oraValida, campoTest, null, "Descrizione"),
                "Dovrebbe lanciare eccezione con maestro null");

        System.out.println("✅ Validazione parametri null per createLezione funziona correttamente");
    }

    @Test
    @Order(14)
    @DisplayName("Test recupero lezione con ID inesistente")
    void testGetLezioneByIdInesistente() {
        // Arrange - ID che sicuramente non esiste
        Integer idInesistente = 999999;

        // Act & Assert
        assertThrows(AccademiaException.class,
                () -> accademiaService.getLezioneById(idInesistente),
                "Dovrebbe lanciare un'eccezione per ID inesistente");

        System.out.println("✅ Eccezione correttamente lanciata per lezione inesistente");
    }

    @Test
    @Order(15)
    @DisplayName("Test eliminazione lezione con ID inesistente")
    void testDeleteLezioneInesistente() throws AccademiaException {
        // Arrange - ID che sicuramente non esiste
        Integer idInesistente = 999999;

        // Act - Il DAO restituisce false per ID inesistente, non lancia eccezione
        boolean risultato = accademiaService.deleteLezione(idInesistente);

        // Assert - Verifica che restituisca false per ID inesistente
        assertFalse(risultato, "La cancellazione di una lezione inesistente dovrebbe restituire false");

        System.out.println("✅ Cancellazione lezione inesistente restituisce false correttamente");
    }

    @Test
    @Order(16)
    @DisplayName("Test aggiunta allievo con ID lezione non valido")
    void testAggiungiAllievoLezioneInesistente() {
        // Arrange - ID lezione che sicuramente non esiste
        Integer idLezioneInesistente = 999999;

        // Act & Assert
        assertThrows(AccademiaException.class,
                () -> accademiaService.aggiungiAllievo(idLezioneInesistente, utenteAllievo),
                "Dovrebbe lanciare un'eccezione per ID lezione inesistente");

        System.out.println("✅ Eccezione correttamente lanciata per aggiunta allievo a lezione inesistente");
    }

    @Test
    @Order(17)
    @DisplayName("Test rimozione allievo non iscritto alla lezione")
    void testRimuoviAllievoNonIscritto() throws AccademiaException, PrenotazioneException {
        // Arrange - Crea una lezione senza allievi
        Integer idPrenotazione = accademiaService.createLezione(
                LocalDate.now().plusDays(90), LocalTime.now(), campoTest, utenteTest, "Lezione senza allievi");
        assertNotNull(idPrenotazione, "La lezione dovrebbe essere stata creata");

        List<Lezione> lezioni = accademiaService.getAllLezioni();
        assertFalse(lezioni.isEmpty(), "Dovrebbe esistere almeno una lezione");
        Lezione lezione = lezioni.get(0);

        // Act & Assert - Il servizio lancia eccezione se l'allievo non è iscritto
        AccademiaException exception = assertThrows(AccademiaException.class,
                () -> accademiaService.rimuoviAllievo(lezione.getId(), utenteAllievo.getId()),
                "Dovrebbe lanciare eccezione per allievo non iscritto");

        assertTrue(exception.getMessage().contains("non è iscritto"),
                "Il messaggio dovrebbe indicare che l'allievo non è iscritto");

        System.out.println(
                "✅ Eccezione correttamente lanciata per rimozione allievo non iscritto: " + exception.getMessage());
    }

    @Test
    @Order(18)
    @DisplayName("Test inserimento descrizione con ID lezione null")
    void testInserisciDescrizioneIdNull() {
        // Act & Assert
        assertThrows(AccademiaException.class,
                () -> accademiaService.inserisciDescrizione(null, "Descrizione test"),
                "Dovrebbe lanciare un'eccezione per ID null");

        System.out.println("✅ Validazione ID null per inserisciDescrizione funziona correttamente");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("\n📊 Test completati per AccademiaService");
    }
}
