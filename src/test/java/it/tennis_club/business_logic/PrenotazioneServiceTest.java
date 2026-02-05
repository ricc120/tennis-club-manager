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
import java.util.ArrayList;
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

    private static Campo campoTest;
    private static Utente utenteTest;
    private static List<Integer> idsPrenotazioniTest;
    private static LocalDate dataTest;
    private static LocalTime oraTest;
    private static int testCounter = 0;

    @BeforeEach
    void setUp() throws SQLException {
        // Inizializza i servizi e i DAO
        prenotazioneService = new PrenotazioneService();
        campoDAO = new CampoDAO();
        utenteDAO = new UtenteDAO();
        prenotazioneDAO = new PrenotazioneDAO();
        idsPrenotazioniTest = new ArrayList<>();

        List<Campo> campi = campoDAO.getAllCampi();
        assertNotNull(campi, "Il database dovrebbe contenere almeno un campo");

        campoTest = campi.get(testCounter % campi.size());

        List<Utente> utentiAutorizzati = utenteDAO.getUtentiByRuolo(Utente.Ruolo.SOCIO);
        assertNotNull(utentiAutorizzati, "Il database dovrebbe contenere almeno un utente");

        utenteTest = utentiAutorizzati.get(testCounter % utentiAutorizzati.size());

        dataTest = LocalDate.now().plusDays(50 + testCounter);
        oraTest = LocalTime.of(10 + (testCounter % 8), 0);

        testCounter++;

    }

    @AfterEach
    void tearDown() throws SQLException {
        for (Integer integer : idsPrenotazioniTest) {
            prenotazioneDAO.deletePrenotazione(integer);
        }
        idsPrenotazioniTest.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Test creazione prenotazione valida")
    public void testCreaPrenotazioneValida() throws PrenotazioneException {
        Integer id = prenotazioneService.creaPrenotazione(
                dataTest, oraTest, campoTest, utenteTest);

        idsPrenotazioniTest.add(id);

        assertNotNull(id, "L'ID della prenotazione non dovrebbe essere null");
        assertTrue(id > 0, "L'ID dovrebbe essere positivo");

        System.out.println("Prenotazione creata con ID: " + id);
    }

    @Test
    @Order(2)
    @DisplayName("Test creazione prenotazione con data nel passato")
    public void testCreaPrenotazioneDataPassata() throws PrenotazioneException {
        LocalDate dataPassata = LocalDate.now().minusDays(1);

        PrenotazioneException exception = assertThrows(
                PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(dataPassata, oraTest, campoTest, utenteTest));

        assertTrue(exception.getMessage().contains("passata"),
                "Il messaggio dovrebbe menzionare che la data è nel passato");

        System.out.println("Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("Test creazione prenotazione con orario non valido")
    public void testCreaPrenotazioneOrarioNonValido() throws PrenotazioneException {
        LocalTime oraTroppoPresto = LocalTime.of(7, 0);

        PrenotazioneException exception = assertThrows(
                PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(dataTest, oraTroppoPresto, campoTest, utenteTest));

        assertTrue(exception.getMessage().contains("orario"),
                "Il messaggio dovrebbe menzionare l'orario non valido");

        System.out.println("Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Test creazione prenotazione duplicata (stesso campo, stessa ora)")
    public void testCreaPrenotazioneDuplicata() throws PrenotazioneException, SQLException {

        Integer id1 = prenotazioneService.creaPrenotazione(
                dataTest, oraTest, campoTest, utenteTest);
        assertNotNull(id1);

        idsPrenotazioniTest.add(id1);

        PrenotazioneException exception = assertThrows(
                PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(dataTest, oraTest, campoTest, utenteTest));

        assertTrue(exception.getMessage().contains("già prenotato"),
                "Il messaggio dovrebbe indicare che il campo è già prenotato");

        System.out.println("Eccezione correttamente lanciata: " + exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("Test verifica disponibilità campo")
    public void testIsCampoDisponibile() throws PrenotazioneException {

        Integer id = prenotazioneService.creaPrenotazione(dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id);

        boolean disponibileOraLibera = prenotazioneService.isCampoDisponibile(
                dataTest, oraTest.minusHours(1), campoTest);
        assertTrue(disponibileOraLibera, "Il campo dovrebbe essere disponibile alle " + oraTest.minusHours(1));

        boolean disponibileOraOccupata = prenotazioneService.isCampoDisponibile(
                dataTest, oraTest, campoTest);
        assertFalse(disponibileOraOccupata, "Il campo NON dovrebbe essere disponibile alle " + oraTest);

        System.out.println("Verifica disponibilità funziona correttamente");
    }

    @Test
    @Order(6)
    @DisplayName("Test recupero prenotazioni per data")
    public void testGetPrenotazioniPerData() throws PrenotazioneException {

        Integer id1 = prenotazioneService.creaPrenotazione(dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id1);

        Integer id2 = prenotazioneService.creaPrenotazione(dataTest, oraTest.plusHours(1), campoTest, utenteTest);
        idsPrenotazioniTest.add(id2);

        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerData(dataTest);

        assertNotNull(prenotazioni);
        assertTrue(prenotazioni.size() >= 2, "Dovrebbero esserci almeno 2 prenotazioni");

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per la data " + dataTest);
    }

    @Test
    @Order(7)
    @DisplayName("Test recupero prenotazioni per campo")
    public void testGetPrenotazioniPerCampo() throws PrenotazioneException {

        Integer id1 = prenotazioneService.creaPrenotazione(dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id1);

        Integer id2 = prenotazioneService.creaPrenotazione(dataTest, oraTest.plusHours(1), campoTest, utenteTest);
        idsPrenotazioniTest.add(id2);

        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerCampo(campoTest);

        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbero esserci prenotazioni per questo campo");
        assertTrue(prenotazioni.size() >= 2, "Dovrebbero esserci almeno 2 prenotazioni");

        for (Prenotazione p : prenotazioni) {
            assertEquals(campoTest.getId(), p.getCampo().getId(),
                    "Tutte le prenotazioni dovrebbero essere per il campo " + campoTest.getNome());
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per il campo " + campoTest.getNome());
    }

    @Test
    @Order(8)
    @DisplayName("Test recupero prenotazioni per socio")
    public void testGetPrenotazioniPerSocio() throws PrenotazioneException {

        Integer id1 = prenotazioneService.creaPrenotazione(dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id1);

        Integer id2 = prenotazioneService.creaPrenotazione(dataTest.plusDays(1), oraTest.plusHours(1), campoTest,
                utenteTest);
        idsPrenotazioniTest.add(id2);

        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerSocio(utenteTest);

        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbero esserci prenotazioni per questo socio");
        assertTrue(prenotazioni.size() >= 2, "Dovrebbero esserci almeno 2 prenotazioni");

        // Verifica che tutte le prenotazioni siano effettivamente per questo socio
        for (Prenotazione p : prenotazioni) {
            assertEquals(utenteTest.getId(), p.getSocio().getId(),
                    "Tutte le prenotazioni dovrebbero essere per il socio " + utenteTest.getEmail());
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per il socio " + utenteTest.getEmail());
    }

    @Test
    @Order(9)
    @DisplayName("Test recupero prenotazioni per data e campo")
    public void testGetPrenotazioniPerDataECampo() throws PrenotazioneException {

        Integer id = prenotazioneService.creaPrenotazione(dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id);

        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerDataECampo(
                dataTest, campoTest);

        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbe esserci almeno una prenotazione");

        for (Prenotazione p : prenotazioni) {
            assertEquals(dataTest, p.getData(), "La data dovrebbe corrispondere");
            assertEquals(campoTest.getId(), p.getCampo().getId(), "Il campo dovrebbe corrispondere");
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per " + dataTest + " sul campo "
                + campoTest.getNome());
    }

    @Test
    @Order(10)
    @DisplayName("Test validazione parametri null")
    public void testValidazioneParametriNull() {
        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(null, LocalTime.now(), campoTest, utenteTest));

        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(LocalDate.now().plusDays(1), null, campoTest, utenteTest));

        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(LocalDate.now().plusDays(1), LocalTime.now(), null,
                        utenteTest));

        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.creaPrenotazione(LocalDate.now().plusDays(1), LocalTime.now(), campoTest,
                        null));

        System.out.println("Validazione parametri null funziona correttamente");
    }

    @Test
    @Order(11)
    @DisplayName("Test cancellazione prenotazione")
    public void testCancellaPrenotazione() throws PrenotazioneException, SQLException {
        Integer id = prenotazioneService.creaPrenotazione(
                dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id);

        Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(id);
        assertNotNull(prenotazione, "La prenotazione dovrebbe esistere prima della cancellazione");

        boolean risultato = prenotazioneService.cancellaPrenotazione(id);

        assertTrue(risultato, "La cancellazione dovrebbe avere successo");

        Prenotazione prenotazioneCancellata = prenotazioneDAO.getPrenotazioneById(id);
        assertNull(prenotazioneCancellata, "La prenotazione non dovrebbe più esistere");

        System.out.println("Prenotazione cancellata con successo");
    }

    @Test
    @Order(12)
    @DisplayName("Test cancellazione prenotazione inesistente")
    public void testCancellaPrenotazioneInesistente() {
        Integer idInesistente = 999999;

        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.cancellaPrenotazione(idInesistente),
                "Dovrebbe lanciare un'eccezione per ID inesistente");

        System.out.println("Eccezione correttamente lanciata per prenotazione inesistente");
    }

    @Test
    @Order(13)
    @DisplayName("Test recupero prenotazione esistente")
    public void testGetPrenotazioneByIdEsistente() throws PrenotazioneException {
        Integer id = prenotazioneService.creaPrenotazione(
                dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id);
        Integer idEsistente = prenotazioneService.getAllPrenotazioni().get(0).getId();

        Prenotazione prenotazione = prenotazioneService.getPrenotazioneById(idEsistente);

        assertNotNull(prenotazione, "La prenotazione dovrebbe esistere");
        assertEquals(idEsistente, prenotazione.getId(), "L'ID dovrebbe corrispondere");

        System.out.println("Prenotazione " + idEsistente + " recuperata con successo");
    }

    @Test
    @Order(14)
    @DisplayName("Test recupero prenotazione inesistente")
    public void testGetPrenotazioneByIdInesistente() {
        Integer idInesistente = 999999;

        assertThrows(PrenotazioneException.class,
                () -> prenotazioneService.getPrenotazioneById(idInesistente),
                "Dovrebbe lanciare un'eccezione per ID inesistente");

        System.out.println("Eccezione correttamente lanciata per prenotazione inesistente");
    }

    @Test
    @Order(15)
    @DisplayName("Test recupero tutte le prenotazioni")
    public void testGetAllPrenotazioni() throws PrenotazioneException {
        Integer id = prenotazioneService.creaPrenotazione(dataTest, oraTest, campoTest, utenteTest);
        idsPrenotazioniTest.add(id);

        List<Prenotazione> prenotazioni = prenotazioneService.getAllPrenotazioni();

        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty(), "Dovrebbero esserci prenotazioni nel database");

        System.out.println("Recuperate " + prenotazioni.size() + " prenotazioni totali");
    }

}
