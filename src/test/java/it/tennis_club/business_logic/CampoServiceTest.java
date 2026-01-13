package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.CampoDAO;
import it.tennis_club.orm.ManutenzioneDAO;
import it.tennis_club.orm.UtenteDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di integrazione per CampoService.
 * Questi test verificano che la business logic funzioni correttamente
 * insieme al database reale.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CampoServiceTest {

    private static CampoService campoService;
    private static CampoDAO campoDAO;
    private static UtenteDAO utenteDAO;

    // Oggetti di test che useremo in più test
    private static Campo campoTest;
    private static Utente utenteAdmin;
    private static Utente utenteSocio;
    private static Integer idManutenzioneTest;

    @BeforeAll
    public static void setUp() throws SQLException {
        // Inizializza i servizi e i DAO
        campoService = new CampoService();
        campoDAO = new CampoDAO();
        new ManutenzioneDAO();
        utenteDAO = new UtenteDAO();

        // Recupera un campo esistente dal database
        List<Campo> campi = campoDAO.getAllCampi();
        assertFalse(campi.isEmpty(), "Il database dovrebbe contenere almeno un campo");
        campoTest = campi.get(0);

        // Recupera utenti con ruoli diversi per testare i permessi
        utenteAdmin = utenteDAO.getUtenteById(1); // Admin dal default.sql
        assertNotNull(utenteAdmin, "Il database dovrebbe contenere l'utente admin con ID 1");
        assertEquals(Utente.Ruolo.ADMIN, utenteAdmin.getRuolo(), "L'utente con ID 1 dovrebbe essere ADMIN");

        utenteSocio = utenteDAO.getUtenteById(2); // Socio dal default.sql
        assertNotNull(utenteSocio, "Il database dovrebbe contenere l'utente socio con ID 2");
        assertEquals(Utente.Ruolo.SOCIO, utenteSocio.getRuolo(), "L'utente con ID 2 dovrebbe essere SOCIO");
    }

    @BeforeEach
    public void cleanupFutureManutenzioni() throws SQLException {
        // Pulisce tutte le manutenzioni future per evitare conflitti tra esecuzioni
        // multiple dei test
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        try {
            conn = it.tennis_club.orm.ConnectionManager.getConnection();
            String query = "DELETE FROM manutenzione WHERE data_inizio >= CURRENT_DATE";
            stmt = conn.prepareStatement(query);
            int deleted = stmt.executeUpdate();
            System.out.println("🧹 Pulite " + deleted + " manutenzioni future prima del test");
        } finally {
            if (stmt != null)
                stmt.close();
            it.tennis_club.orm.ConnectionManager.closeConnection(conn);
        }
    }

    // ========== TEST OPERAZIONI PUBBLICHE (accessibili a tutti) ==========

    @Test
    @Order(1)
    @DisplayName("Test recupero di tutti i campi")
    public void testGetAllCampi() throws CampoException {
        // Act
        List<Campo> campi = campoService.getAllCampi();

        // Assert
        assertNotNull(campi, "La lista dei campi non dovrebbe essere null");
        assertFalse(campi.isEmpty(), "Dovrebbero esserci almeno alcuni campi nel database");

        // Verifica che i campi abbiano tutti i dati popolati
        for (Campo campo : campi) {
            assertNotNull(campo.getId(), "L'ID del campo non dovrebbe essere null");
            assertNotNull(campo.getNome(), "Il nome del campo non dovrebbe essere null");
            assertNotNull(campo.getTipoSuperficie(), "Il tipo superficie non dovrebbe essere null");
            assertNotNull(campo.getIsCoperto(), "Il flag is_coperto non dovrebbe essere null");
        }

        System.out.println("✅ Recuperati " + campi.size() + " campi dal database");
    }

    @Test
    @Order(2)
    @DisplayName("Test recupero campo per ID")
    public void testGetCampoById() throws CampoException {
        // Act
        Campo campo = campoService.getCampoById(campoTest.getId());

        // Assert
        assertNotNull(campo, "Il campo dovrebbe essere trovato");
        assertEquals(campoTest.getId(), campo.getId(), "L'ID dovrebbe corrispondere");
        assertEquals(campoTest.getNome(), campo.getNome(), "Il nome dovrebbe corrispondere");

        System.out.println("✅ Recuperato campo: " + campo.getNome());
    }

    @Test
    @Order(3)
    @DisplayName("Test recupero campo con ID inesistente")
    public void testGetCampoByIdNotFound() {
        // Act & Assert
        CampoException exception = assertThrows(
                CampoException.class,
                () -> campoService.getCampoById(9999),
                "Dovrebbe lanciare un'eccezione per ID inesistente");

        assertTrue(exception.getMessage().contains("non trovato"),
                "Il messaggio dovrebbe indicare che il campo non è stato trovato");

        System.out.println("✅ Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Test recupero campi coperti")
    public void testGetCampiCoperti() throws CampoException {
        // Act
        List<Campo> campiCoperti = campoService.getCampiCoperti();

        // Assert
        assertNotNull(campiCoperti, "La lista non dovrebbe essere null");

        // Verifica che tutti i campi restituiti siano effettivamente coperti
        for (Campo campo : campiCoperti) {
            assertTrue(campo.getIsCoperto(),
                    "Tutti i campi nella lista dovrebbero essere coperti: " + campo.getNome());
        }

        System.out.println("✅ Trovati " + campiCoperti.size() + " campi coperti");
    }

    @Test
    @Order(5)
    @DisplayName("Test recupero campi per tipo superficie")
    public void testGetCampiByTipoSuperficie() throws CampoException {
        // Act
        List<Campo> campiTerra = campoService.getCampiByTipoSuperficie("Terra");

        // Assert
        assertNotNull(campiTerra, "La lista non dovrebbe essere null");

        // Verifica che tutti i campi abbiano il tipo superficie corretto
        for (Campo campo : campiTerra) {
            assertEquals("Terra", campo.getTipoSuperficie(),
                    "Tutti i campi dovrebbero avere superficie Terra");
        }

        System.out.println("✅ Trovati " + campiTerra.size() + " campi in Terra");
    }

    @Test
    @Order(6)
    @DisplayName("Test validazione ID campo non valido")
    public void testGetCampoByIdInvalido() {
        // Test con ID null
        CampoException exceptionNull = assertThrows(
                CampoException.class,
                () -> campoService.getCampoById(null));
        assertTrue(exceptionNull.getMessage().contains("non valido"));

        // Test con ID negativo
        CampoException exceptionNegativo = assertThrows(
                CampoException.class,
                () -> campoService.getCampoById(-1));
        assertTrue(exceptionNegativo.getMessage().contains("non valido"));

        // Test con ID zero
        CampoException exceptionZero = assertThrows(
                CampoException.class,
                () -> campoService.getCampoById(0));
        assertTrue(exceptionZero.getMessage().contains("non valido"));

        System.out.println("✅ Validazione ID campo funziona correttamente");
    }

    // ========== TEST OPERAZIONI RISERVATE (solo ADMIN e MANUTENTORE) ==========

    @Test
    @Order(7)
    @DisplayName("Test creazione manutenzione con utente ADMIN")
    public void testCreaManutenzioneAdmin() throws CampoException {
        // Arrange
        LocalDate dataInizio = LocalDate.now();
        String descrizione = "Rifacimento linee campo";

        // Act
        idManutenzioneTest = campoService.creaManutenzione(
                utenteAdmin, campoTest.getId(), dataInizio, descrizione);

        // Assert
        assertNotNull(idManutenzioneTest, "La manutenzione dovrebbe essere stata creata con successo");
        assertTrue(idManutenzioneTest > 0, "L'ID della manutenzione dovrebbe essere positivo");

        System.out.println("✅ Manutenzione creata con ID: " + idManutenzioneTest);
    }

    @Test
    @Order(8)
    @DisplayName("Test errore creazione manutenzione con utente SOCIO")
    public void testCreaManutenzionePermessiInsufficienti() {
        // Arrange
        LocalDate dataInizio = LocalDate.now();
        String descrizione = "Tentativo da socio";

        // Act & Assert
        CampoException exception = assertThrows(
                CampoException.class,
                () -> campoService.creaManutenzione(utenteSocio, campoTest.getId(), dataInizio, descrizione),
                "Un socio non dovrebbe poter creare manutenzioni");

        assertTrue(exception.getMessage().contains("Permessi insufficienti"),
                "Il messaggio dovrebbe indicare permessi insufficienti");

        System.out.println("✅ Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("Test completamento manutenzione")
    public void testCompletaManutenzione() throws CampoException {
        // Arrange
        assertNotNull(idManutenzioneTest, "Serve un ID manutenzione dai test precedenti");
        LocalDate dataFine = LocalDate.now().plusDays(1);

        // Act
        assertDoesNotThrow(() -> campoService.completaManutenzione(
                utenteAdmin, idManutenzioneTest, dataFine));

        System.out.println("✅ Manutenzione " + idManutenzioneTest + " completata con successo");
    }

    @Test
    @Order(10)
    @DisplayName("Test annullamento manutenzione")
    public void testAnnullaManutenzione() throws CampoException {
        // Arrange - Crea una nuova manutenzione specifica per il test di annullamento
        LocalDate dataInizio = LocalDate.now();
        Integer idDaAnnullare = campoService.creaManutenzione(
                utenteAdmin, campoTest.getId(), dataInizio, "Manutenzione da annullare");

        // Act
        assertDoesNotThrow(() -> campoService.annullaManutenzione(utenteAdmin, idDaAnnullare),
                "L'annullamento non dovrebbe lanciare eccezioni");

        System.out.println("✅ Manutenzione " + idDaAnnullare + " annullata con successo");
    }

    @Test
    @Order(11)
    @DisplayName("Test recupero manutenzioni per campo")
    public void testGetManutenzioniCampo() throws CampoException {
        // Arrange - Crea alcune manutenzioni
        LocalDate data1 = LocalDate.now().plusDays(10);
        LocalDate data2 = LocalDate.now().plusDays(11);
        campoService.creaManutenzione(utenteAdmin, campoTest.getId(), data1, "Manutenzione 1");
        campoService.creaManutenzione(utenteAdmin, campoTest.getId(), data2, "Manutenzione 2");

        // Act
        List<Manutenzione> manutenzioni = campoService.getManutenzioniCampo(utenteAdmin, campoTest.getId());

        // Assert
        assertNotNull(manutenzioni);
        assertTrue(manutenzioni.size() >= 2, "Dovrebbero esserci almeno 2 manutenzioni");

        // Verifica che tutte le manutenzioni siano effettivamente per questo campo
        for (Manutenzione m : manutenzioni) {
            assertEquals(campoTest.getId(), m.getCampo().getId(),
                    "Tutte le manutenzioni dovrebbero essere per il campo " + campoTest.getNome());
        }

        System.out.println("✅ Trovate " + manutenzioni.size() + " manutenzioni per il campo " + campoTest.getNome());
    }

    @Test
    @Order(12)
    @DisplayName("Test errore recupero manutenzioni con utente SOCIO")
    public void testGetManutenzioniCampoPermessiInsufficienti() {
        // Act & Assert
        CampoException exception = assertThrows(
                CampoException.class,
                () -> campoService.getManutenzioniCampo(utenteSocio, campoTest.getId()),
                "Un socio non dovrebbe poter visualizzare le manutenzioni");

        assertTrue(exception.getMessage().contains("Permessi insufficienti"));

        System.out.println("✅ Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("Test validazione parametri null in creazione manutenzione")
    public void testCreaManutenzioneParametriNull() {
        // Test con utente null
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(null, campoTest.getId(), LocalDate.now(), "Test"));

        // Test con ID campo null
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(utenteAdmin, null, LocalDate.now(), "Test"));

        // Test con data null
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(utenteAdmin, campoTest.getId(), null, "Test"));

        // Test con descrizione null
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(utenteAdmin, campoTest.getId(), LocalDate.now(), null));

        // Test con descrizione vuota
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(utenteAdmin, campoTest.getId(), LocalDate.now(), ""));

        System.out.println("✅ Validazione parametri null funziona correttamente");
    }

    @Test
    @Order(14)
    @DisplayName("Test errore completamento manutenzione con ID non valido")
    public void testCompletaManutenzioneIdInvalido() {
        // Test con ID null
        CampoException exceptionNull = assertThrows(
                CampoException.class,
                () -> campoService.completaManutenzione(utenteAdmin, null, LocalDate.now()));
        assertTrue(exceptionNull.getMessage().contains("ID manutenzione non valido"));

        // Test con ID negativo
        CampoException exceptionNegativo = assertThrows(
                CampoException.class,
                () -> campoService.completaManutenzione(utenteAdmin, -1, LocalDate.now()));
        assertTrue(exceptionNegativo.getMessage().contains("ID manutenzione non valido"));

        // Test con data fine null
        assertThrows(
                CampoException.class,
                () -> campoService.completaManutenzione(utenteAdmin, 1, null));

        System.out.println("✅ Validazione parametri completamento funziona correttamente");
    }

    @Test
    @Order(15)
    @DisplayName("Test validazione tipo superficie")
    public void testValidazioneTipoSuperficie() {
        // Test con tipo superficie null
        assertThrows(CampoException.class,
                () -> campoService.getCampiByTipoSuperficie(null));

        // Test con tipo superficie vuoto
        assertThrows(CampoException.class,
                () -> campoService.getCampiByTipoSuperficie(""));

        // Test con tipo superficie solo spazi
        assertThrows(CampoException.class,
                () -> campoService.getCampiByTipoSuperficie("   "));

        System.out.println("✅ Validazione tipo superficie funziona correttamente");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("\n📊 Test completati per CampoService");
    }
}
