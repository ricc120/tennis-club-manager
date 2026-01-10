package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.CampoDAO;
import it.tennis_club.orm.UtenteDAO;
import it.tennis_club.orm.PrenotazioneDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di integrazione per PrenotazioneService.
 * Questi test verificano che la business logic funzioni correttamente
 * insieme al database reale.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrenotazioneServiceTest {

    private static PrenotazioneService prenotazioneService;
    private static CampoDAO campoDAO;
    private static UtenteDAO utenteDAO;
    private static PrenotazioneDAO prenotazioneDAO;

    // Oggetti di test che useremo in più test
    private static Campo campoTest;
    private static Utente utenteTest;
    private static Integer idPrenotazioneCreata;

    @BeforeAll
    public static void setUp() throws SQLException {
        // Inizializza i servizi e i DAO
        prenotazioneService = new PrenotazioneService();
        campoDAO = new CampoDAO();
        utenteDAO = new UtenteDAO();
        prenotazioneDAO = new PrenotazioneDAO();

        // Recupera un campo e un utente esistenti dal database
        // (assumendo che ci siano dati di default dal file default.sql)
        List<Campo> campi = campoDAO.getAllCampi();
        assertFalse(campi.isEmpty(), "Il database dovrebbe contenere almeno un campo");
        campoTest = campi.get(0);

        // Recupera l'utente con ID 1 (Mario Rossi - ADMIN dal default.sql)
        utenteTest = utenteDAO.getUtenteById(1);
        assertNotNull(utenteTest, "Il database dovrebbe contenere l'utente con ID 1");
    }

    @BeforeEach
    public void cleanupFuturePrenotazioni() throws SQLException {
        // Pulisce tutte le prenotazioni future per evitare conflitti tra esecuzioni
        // multiple dei test
        // Mantiene solo le prenotazioni storiche (prima di oggi)
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        try {
            conn = it.tennis_club.orm.ConnectionManager.getConnection();
            String query = "DELETE FROM prenotazione WHERE data >= CURRENT_DATE";
            stmt = conn.prepareStatement(query);
            int deleted = stmt.executeUpdate();
            System.out.println("🧹 Pulite " + deleted + " prenotazioni future prima del test");
        } finally {
            if (stmt != null)
                stmt.close();
            it.tennis_club.orm.ConnectionManager.closeConnection(conn);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test creazione prenotazione valida")
    public void testCreaPrenotazioneValida() throws PrenotazioneException {
        // Arrange - Usa una data lontana nel futuro per evitare conflitti
        LocalDate dataFutura = LocalDate.now().plusDays(100);
        LocalTime oraValida = LocalTime.of(10, 0);

        // Act
        idPrenotazioneCreata = prenotazioneService.creaPrenotazione(
                dataFutura, oraValida, campoTest, utenteTest);

        // Assert
        assertNotNull(idPrenotazioneCreata, "L'ID della prenotazione non dovrebbe essere null");
        assertTrue(idPrenotazioneCreata > 0, "L'ID dovrebbe essere positivo");

        System.out.println("✅ Prenotazione creata con ID: " + idPrenotazioneCreata);
    }

    @Test
    @Order(2)
    @DisplayName("Test creazione prenotazione con data nel passato")
    public void testCreaPrenotazioneDataPassata() {
        // Arrange
        LocalDate dataPassata = LocalDate.now().minusDays(1);
        LocalTime oraValida = LocalTime.of(10, 0);

        // Act & Assert
        PrenotazioneException exception = assertThrows(
                PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(dataPassata, oraValida, campoTest, utenteTest));

        assertTrue(exception.getMessage().contains("passata"),
                "Il messaggio dovrebbe menzionare che la data è nel passato");

        System.out.println("✅ Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("Test creazione prenotazione con orario non valido")
    public void testCreaPrenotazioneOrarioNonValido() {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(7);
        LocalTime oraTroppoPresto = LocalTime.of(6, 0); // Prima delle 8:00

        // Act & Assert
        PrenotazioneException exception = assertThrows(
                PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(dataFutura, oraTroppoPresto, campoTest, utenteTest));

        assertTrue(exception.getMessage().contains("orario"),
                "Il messaggio dovrebbe menzionare l'orario non valido");

        System.out.println("✅ Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Test creazione prenotazione duplicata (stesso campo, stessa ora)")
    public void testCreaPrenotazioneDuplicata() throws PrenotazioneException {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(110);
        LocalTime ora = LocalTime.of(14, 0);

        // Crea la prima prenotazione
        Integer primaPrenotazione = prenotazioneService.creaPrenotazione(
                dataFutura, ora, campoTest, utenteTest);
        assertNotNull(primaPrenotazione);

        // Act & Assert - Prova a creare una seconda prenotazione alla stessa ora
        PrenotazioneException exception = assertThrows(
                PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(dataFutura, ora, campoTest, utenteTest));

        assertTrue(exception.getMessage().contains("già prenotato"),
                "Il messaggio dovrebbe indicare che il campo è già prenotato");

        System.out.println("✅ Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("Test verifica disponibilità campo")
    public void testIsCampoDisponibile() throws PrenotazioneException {
        LocalTime oraLibera = LocalTime.of(16, 0);
        LocalTime oraOccupata = LocalTime.of(10, 0); // Usata nel primo test

        // Crea una prenotazione
        LocalDate dataTest = LocalDate.now().plusDays(120);
        prenotazioneService.creaPrenotazione(dataTest, oraOccupata, campoTest, utenteTest);

        // Act & Assert
        boolean disponibileOraLibera = prenotazioneService.isCampoDisponibile(
                dataTest, oraLibera, campoTest);
        assertTrue(disponibileOraLibera, "Il campo dovrebbe essere disponibile alle 16:00");

        boolean disponibileOraOccupata = prenotazioneService.isCampoDisponibile(
                dataTest, oraOccupata, campoTest);
        assertFalse(disponibileOraOccupata, "Il campo NON dovrebbe essere disponibile alle 10:00");

        System.out.println("✅ Verifica disponibilità funziona correttamente");
    }

    @Test
    @Order(6)
    @DisplayName("Test recupero prenotazioni per data")
    public void testGetPrenotazioniPerData() throws PrenotazioneException {
        // Arrange
        LocalDate dataTest = LocalDate.now().plusDays(125);
        LocalTime ora1 = LocalTime.of(9, 0);
        LocalTime ora2 = LocalTime.of(11, 0);

        // Crea due prenotazioni nella stessa data
        prenotazioneService.creaPrenotazione(dataTest, ora1, campoTest, utenteTest);
        prenotazioneService.creaPrenotazione(dataTest, ora2, campoTest, utenteTest);

        // Act
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerData(dataTest);

        // Assert
        assertNotNull(prenotazioni);
        assertTrue(prenotazioni.size() >= 2, "Dovrebbero esserci almeno 2 prenotazioni");

        System.out.println("✅ Trovate " + prenotazioni.size() + " prenotazioni per la data " + dataTest);
    }

    @Test
    @Order(7)
    @DisplayName("Test recupero prenotazioni per campo")
    public void testGetPrenotazioniPerCampo() throws PrenotazioneException {
        // Arrange - Crea alcune prenotazioni per il campo
        LocalDate data1 = LocalDate.now().plusDays(140);
        LocalDate data2 = LocalDate.now().plusDays(141);
        prenotazioneService.creaPrenotazione(data1, LocalTime.of(9, 0), campoTest, utenteTest);
        prenotazioneService.creaPrenotazione(data2, LocalTime.of(10, 0), campoTest, utenteTest);

        // Act
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerCampo(campoTest);

        // Assert
        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbero esserci prenotazioni per questo campo");
        assertTrue(prenotazioni.size() >= 2, "Dovrebbero esserci almeno 2 prenotazioni");

        // Verifica che tutte le prenotazioni siano effettivamente per questo campo
        for (Prenotazione p : prenotazioni) {
            assertEquals(campoTest.getId(), p.getCampo().getId(),
                    "Tutte le prenotazioni dovrebbero essere per il campo " + campoTest.getNome());
        }

        System.out.println("✅ Trovate " + prenotazioni.size() + " prenotazioni per il campo " + campoTest.getNome());
    }

    @Test
    @Order(8)
    @DisplayName("Test recupero prenotazioni per socio")
    public void testGetPrenotazioniPerSocio() throws PrenotazioneException {
        // Arrange - Crea alcune prenotazioni per il socio
        LocalDate data1 = LocalDate.now().plusDays(145);
        LocalDate data2 = LocalDate.now().plusDays(146);
        prenotazioneService.creaPrenotazione(data1, LocalTime.of(11, 0), campoTest, utenteTest);
        prenotazioneService.creaPrenotazione(data2, LocalTime.of(12, 0), campoTest, utenteTest);

        // Act
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerSocio(utenteTest);

        // Assert
        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbero esserci prenotazioni per questo socio");
        assertTrue(prenotazioni.size() >= 2, "Dovrebbero esserci almeno 2 prenotazioni");

        // Verifica che tutte le prenotazioni siano effettivamente per questo socio
        for (Prenotazione p : prenotazioni) {
            assertEquals(utenteTest.getId(), p.getSocio().getId(),
                    "Tutte le prenotazioni dovrebbero essere per il socio " + utenteTest.getEmail());
        }

        System.out.println("✅ Trovate " + prenotazioni.size() + " prenotazioni per il socio " + utenteTest.getEmail());
    }

    @Test
    @Order(9)
    @DisplayName("Test recupero prenotazioni per data e campo")
    public void testGetPrenotazioniPerDataECampo() throws PrenotazioneException {
        // Arrange
        LocalDate dataTest = LocalDate.now().plusDays(130);
        LocalTime ora = LocalTime.of(15, 0);

        // Crea una prenotazione specifica
        prenotazioneService.creaPrenotazione(dataTest, ora, campoTest, utenteTest);

        // Act
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerDataECampo(
                dataTest, campoTest);

        // Assert
        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbe esserci almeno una prenotazione");

        // Verifica che tutte le prenotazioni siano per la data e il campo corretti
        for (Prenotazione p : prenotazioni) {
            assertEquals(dataTest, p.getData(), "La data dovrebbe corrispondere");
            assertEquals(campoTest.getId(), p.getCampo().getId(), "Il campo dovrebbe corrispondere");
        }

        System.out.println("✅ Trovate " + prenotazioni.size() + " prenotazioni per " + dataTest + " sul campo "
                + campoTest.getNome());
    }

    @Test
    @Order(10)
    @DisplayName("Test validazione parametri null")
    public void testValidazioneParametriNull() {
        // Test con data null
        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(null, LocalTime.of(10, 0), campoTest, utenteTest));

        // Test con ora null
        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(LocalDate.now().plusDays(1), null, campoTest, utenteTest));

        // Test con campo null
        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(LocalDate.now().plusDays(1), LocalTime.of(10, 0), null,
                        utenteTest));

        // Test con socio null
        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(LocalDate.now().plusDays(1), LocalTime.of(10, 0), campoTest,
                        null));

        System.out.println("✅ Validazione parametri null funziona correttamente");
    }

    @Test
    @Order(11)
    @DisplayName("Test cancellazione prenotazione")
    public void testCancellaPrenotazione() throws PrenotazioneException, SQLException {
        // Arrange - Crea una prenotazione da cancellare
        LocalDate dataFutura = LocalDate.now().plusDays(135);
        LocalTime ora = LocalTime.of(18, 0);
        Integer idDaCancellare = prenotazioneService.creaPrenotazione(
                dataFutura, ora, campoTest, utenteTest);

        // Verifica che esista
        Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(idDaCancellare);
        assertNotNull(prenotazione, "La prenotazione dovrebbe esistere prima della cancellazione");

        // Act - Cancella la prenotazione
        boolean risultato = prenotazioneService.cancellaPrenotazione(idDaCancellare);

        // Assert
        assertTrue(risultato, "La cancellazione dovrebbe avere successo");

        // Verifica che non esista più
        Prenotazione prenotazioneCancellata = prenotazioneDAO.getPrenotazioneById(idDaCancellare);
        assertNull(prenotazioneCancellata, "La prenotazione non dovrebbe più esistere");

        System.out.println("✅ Prenotazione cancellata con successo");
    }

    @Test
    @Order(12)
    @DisplayName("Test cancellazione prenotazione inesistente")
    public void testCancellaPrenotazioneInesistente() {
        // Arrange - ID che sicuramente non esiste
        Integer idInesistente = 999999;

        // Act & Assert
        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.cancellaPrenotazione(idInesistente),
                "Dovrebbe lanciare un'eccezione per ID inesistente");

        System.out.println("✅ Eccezione correttamente lanciata per prenotazione inesistente");
    }

    @Test
    @Order(13)
    @DisplayName("Test recupero tutte le prenotazioni")
    public void testGetAllPrenotazioni() throws PrenotazioneException {
        // Arrange - Crea almeno una prenotazione
        LocalDate data = LocalDate.now().plusDays(150);
        prenotazioneService.creaPrenotazione(data, LocalTime.of(13, 0), campoTest, utenteTest);

        // Act
        List<Prenotazione> prenotazioni = prenotazioneService.getAllPrenotazioni();

        // Assert
        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbero esserci prenotazioni nel database");

        System.out.println("✅ Recuperate " + prenotazioni.size() + " prenotazioni totali");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("\n📊 Test completati per PrenotazioneService");
    }
}
