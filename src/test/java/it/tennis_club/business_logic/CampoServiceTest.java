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
import java.util.ArrayList;
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
        private static ManutenzioneDAO manutenzioneDAO;

        // Oggetti di test che useremo in più test
        private static Campo campoTest;
        private static Utente utenteTest;
        private static Integer idManutenzioneTest;
        private static List<Integer> idsManutenzioniTest;

        private static int testCounter = 0;

        @BeforeEach
        void setUp() throws SQLException {
                // Inizializza i servizi e i DAO
                campoService = new CampoService();
                campoDAO = new CampoDAO();
                manutenzioneDAO = new ManutenzioneDAO();
                utenteDAO = new UtenteDAO();
                idsManutenzioniTest = new ArrayList<>();

                List<Campo> campi = campoDAO.getAllCampi();
                campoTest = campi.get(testCounter % campi.size());

                List<Utente> utentiAutorizzati = new ArrayList<>();
                utentiAutorizzati.addAll(utenteDAO.getUtentiByRuolo(Utente.Ruolo.ADMIN));
                utentiAutorizzati.addAll(utenteDAO.getUtentiByRuolo(Utente.Ruolo.MANUTENTORE));
                utenteTest = utentiAutorizzati.get(testCounter % utentiAutorizzati.size());

                testCounter++;

        }

        @AfterEach
        void tearDown() throws SQLException {
                for (Integer id : idsManutenzioniTest) {
                        manutenzioneDAO.deleteManutenzioni(id);
                }
                idsManutenzioniTest.clear();
        }

        // ========== TEST OPERAZIONI PUBBLICHE (accessibili a tutti) ==========

        @Test
        @Order(1)
        @DisplayName("Test recupero di tutti i campi")
        public void testGetAllCampi() throws CampoException {
                List<Campo> campi = campoService.getCampi();

                assertNotNull(campi, "La lista dei campi non dovrebbe essere null");
                assertFalse(campi.isEmpty(), "Dovrebbero esserci almeno alcuni campi nel database");

                for (Campo campo : campi) {
                        assertNotNull(campo.getId(), "L'ID del campo non dovrebbe essere null");
                        assertNotNull(campo.getNome(), "Il nome del campo non dovrebbe essere null");
                        assertNotNull(campo.getTipoSuperficie(), "Il tipo superficie non dovrebbe essere null");
                        assertNotNull(campo.getIsCoperto(), "Il flag is_coperto non dovrebbe essere null");
                }

                System.out.println("Recuperati " + campi.size() + " campi dal database");
        }

        @Test
        @Order(2)
        @DisplayName("Test recupero campo per ID")
        public void testGetCampoById() throws CampoException {
                Campo campo = campoService.getCampoPerId(campoTest.getId());

                assertNotNull(campo, "Il campo dovrebbe essere trovato");
                assertEquals(campoTest.getId(), campo.getId(), "L'ID dovrebbe corrispondere");
                assertEquals(campoTest.getNome(), campo.getNome(), "Il nome dovrebbe corrispondere");

                System.out.println("Recuperato campo: " + campo.getNome());
        }

        @Test
        @Order(3)
        @DisplayName("Test recupero campo con ID inesistente")
        public void testGetCampoByIdNotFound() {
                CampoException exception = assertThrows(
                                CampoException.class,
                                () -> campoService.getCampoPerId(9999),
                                "Dovrebbe lanciare un'eccezione per ID inesistente");

                assertTrue(exception.getMessage().contains("non trovato"),
                                "Il messaggio dovrebbe indicare che il campo non è stato trovato");

                System.out.println("Eccezione correttamente lanciata: " + exception.getMessage());
        }

        @Test
        @Order(4)
        @DisplayName("Test recupero campi coperti")
        public void testGetCampiCoperti() throws CampoException {
                List<Campo> campiCoperti = campoService.getCampiCoperti();

                assertNotNull(campiCoperti, "La lista non dovrebbe essere null");

                for (Campo campo : campiCoperti) {
                        assertTrue(campo.getIsCoperto(),
                                        "Tutti i campi nella lista dovrebbero essere coperti: " + campo.getNome());
                }

                System.out.println("Trovati " + campiCoperti.size() + " campi coperti");
        }

        @Test
        @Order(5)
        @DisplayName("Test recupero campi per tipo superficie")
        public void testGetCampiByTipoSuperficie() throws CampoException {
                List<Campo> campiTerra = campoService.getCampiPerTipoSuperficie("Terra");

                assertNotNull(campiTerra, "La lista non dovrebbe essere null");

                for (Campo campo : campiTerra) {
                        assertEquals("Terra", campo.getTipoSuperficie(),
                                        "Tutti i campi dovrebbero avere superficie Terra");
                }

                System.out.println("Trovati " + campiTerra.size() + " campi in Terra");
        }

        @Test
        @Order(6)
        @DisplayName("Test validazione ID campo non valido")
        public void testGetCampoByIdInvalido() {
                // Test con ID null
                CampoException exceptionNull = assertThrows(
                                CampoException.class,
                                () -> campoService.getCampoPerId(null));
                assertTrue(exceptionNull.getMessage().contains("non valido"));

                // Test con ID negativo
                CampoException exceptionNegativo = assertThrows(
                                CampoException.class,
                                () -> campoService.getCampoPerId(-1));
                assertTrue(exceptionNegativo.getMessage().contains("non valido"));

                // Test con ID zero
                CampoException exceptionZero = assertThrows(
                                CampoException.class,
                                () -> campoService.getCampoPerId(0));
                assertTrue(exceptionZero.getMessage().contains("non valido"));

                System.out.println("Validazione ID campo funziona correttamente");
        }

        // ========== TEST OPERAZIONI RISERVATE (solo ADMIN e MANUTENTORE) ==========

        @Test
        @Order(7)
        @DisplayName("Test creazione manutenzione con utente")
        public void testCreaManutenzioneAdmin() throws CampoException {
                LocalDate dataInizio = LocalDate.now();
                String descrizione = "Rifacimento linee campo";

                Integer id = campoService.creaManutenzione(
                                utenteTest, campoTest.getId(), dataInizio, descrizione);
                idManutenzioneTest = id;

                assertNotNull(id, "La manutenzione dovrebbe essere stata creata con successo");
                assertTrue(id > 0, "L'ID della manutenzione dovrebbe essere positivo");

                System.out.println("Manutenzione creata con ID: " + id);
        }

        @Test
        @Order(8)
        @DisplayName("Test errore creazione manutenzione con utente SOCIO")
        public void testCreaManutenzionePermessiInsufficienti() {
                LocalDate dataInizio = LocalDate.now();
                String descrizione = "Tentativo da socio";
                utenteTest.setRuolo(Utente.Ruolo.SOCIO);

                CampoException exception = assertThrows(
                                CampoException.class,
                                () -> campoService.creaManutenzione(utenteTest, campoTest.getId(), dataInizio,
                                                descrizione),
                                "Un socio non dovrebbe poter creare manutenzioni");

                assertTrue(exception.getMessage().contains("Permessi insufficienti"),
                                "Il messaggio dovrebbe indicare permessi insufficienti");

                System.out.println("Eccezione correttamente lanciata: " + exception.getMessage());
        }

        @Test
        @Order(9)
        @DisplayName("Test completamento manutenzione")
        public void testCompletaManutenzione() throws CampoException {
                assertNotNull(idManutenzioneTest, "Serve un ID manutenzione dai test precedenti");
                LocalDate dataFine = LocalDate.now().plusDays(1);

                assertDoesNotThrow(() -> campoService.completaManutenzione(
                                utenteTest, idManutenzioneTest, dataFine));

                System.out.println("Manutenzione " + idManutenzioneTest + " completata con successo");

                idsManutenzioniTest.add(idManutenzioneTest);

        }

        @Test
        @Order(10)
        @DisplayName("Test annullamento manutenzione")
        public void testAnnullaManutenzione() throws CampoException {
                LocalDate dataInizio = LocalDate.now();
                Integer id = campoService.creaManutenzione(
                                utenteTest, campoTest.getId(), dataInizio, "Manutenzione da annullare");
                idsManutenzioniTest.add(id);

                assertDoesNotThrow(() -> campoService.annullaManutenzione(utenteTest, id),
                                "L'annullamento non dovrebbe lanciare eccezioni");

                System.out.println("Manutenzione " + id + " annullata con successo");
        }

        @Test
        @Order(11)
        @DisplayName("Test recupero manutenzioni per campo")
        public void testGetManutenzioniCampo() throws CampoException {
                LocalDate data1 = LocalDate.now().plusDays(10);
                LocalDate data2 = LocalDate.now().plusDays(11);
                Integer id1 = campoService.creaManutenzione(utenteTest, campoTest.getId(), data1,
                                "Manutenzione 1");
                Integer id2 = campoService.creaManutenzione(utenteTest, campoTest.getId(), data2,
                                "Manutenzione 2");

                idsManutenzioniTest.add(id1);
                idsManutenzioniTest.add(id2);

                List<Manutenzione> manutenzioni = campoService.getManutenzioniPerCampo(utenteTest, campoTest.getId());

                assertNotNull(manutenzioni);
                assertTrue(manutenzioni.size() >= 2, "Dovrebbero esserci almeno 2 manutenzioni");

                for (Manutenzione m : manutenzioni) {
                        assertEquals(campoTest.getId(), m.getCampo().getId(),
                                        "Tutte le manutenzioni dovrebbero essere per il campo " + campoTest.getNome());
                }

                System.out.println(
                                "Trovate " + manutenzioni.size() + " manutenzioni per il campo " + campoTest.getNome());
        }

        @Test
        @Order(12)
        @DisplayName("Test errore recupero manutenzioni con utente SOCIO")
        public void testGetManutenzioniCampoPermessiInsufficienti() {
                utenteTest.setRuolo(Utente.Ruolo.SOCIO);

                CampoException exception = assertThrows(
                                CampoException.class,
                                () -> campoService.getManutenzioniPerCampo(utenteTest, campoTest.getId()),
                                "Un socio non dovrebbe poter visualizzare le manutenzioni");

                assertTrue(exception.getMessage().contains("Permessi insufficienti"));

                System.out.println("Eccezione correttamente lanciata: " + exception.getMessage());
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
                                () -> campoService.creaManutenzione(utenteTest, null, LocalDate.now(), "Test"));

                // Test con data null
                assertThrows(CampoException.class,
                                () -> campoService.creaManutenzione(utenteTest, campoTest.getId(), null, "Test"));

                // Test con descrizione null
                assertThrows(CampoException.class,
                                () -> campoService.creaManutenzione(utenteTest, campoTest.getId(), LocalDate.now(),
                                                null));

                // Test con descrizione vuota
                assertThrows(CampoException.class,
                                () -> campoService.creaManutenzione(utenteTest, campoTest.getId(), LocalDate.now(),
                                                ""));

                System.out.println("Validazione parametri null funziona correttamente");
        }

        @Test
        @Order(14)
        @DisplayName("Test errore completamento manutenzione con ID non valido")
        public void testCompletaManutenzioneIdInvalido() {
                // Test con ID null
                CampoException exceptionNull = assertThrows(
                                CampoException.class,
                                () -> campoService.completaManutenzione(utenteTest, null, LocalDate.now()));
                assertTrue(exceptionNull.getMessage().contains("ID manutenzione non valido"));

                // Test con ID negativo
                CampoException exceptionNegativo = assertThrows(
                                CampoException.class,
                                () -> campoService.completaManutenzione(utenteTest, -1, LocalDate.now()));
                assertTrue(exceptionNegativo.getMessage().contains("ID manutenzione non valido"));

                // Test con data fine null
                assertThrows(
                                CampoException.class,
                                () -> campoService.completaManutenzione(utenteTest, 1, null));

                System.out.println("Validazione parametri completamento funziona correttamente");
        }

        @Test
        @Order(15)
        @DisplayName("Test validazione tipo superficie")
        public void testValidazioneTipoSuperficie() {
                // Test con tipo superficie null
                assertThrows(CampoException.class,
                                () -> campoService.getCampiPerTipoSuperficie(null));

                // Test con tipo superficie vuoto
                assertThrows(CampoException.class,
                                () -> campoService.getCampiPerTipoSuperficie(""));

                // Test con tipo superficie solo spazi
                assertThrows(CampoException.class,
                                () -> campoService.getCampiPerTipoSuperficie("   "));

                System.out.println("Validazione tipo superficie funziona correttamente");
        }

        @Test
        @Order(16)
        @DisplayName("Test recupero di ogni manutenzione")
        public void testGetAllManutenzioni() throws CampoException {
                Integer id = campoService.creaManutenzione(utenteTest, campoTest.getId(), LocalDate.now(),
                                "Descrizione");
                idsManutenzioniTest.add(id);

                List<Manutenzione> manutenzioni = campoService.getAllManutenzioni();

                assertNotNull(manutenzioni, "La lista delle manutenzioni non dovrebbe essere null");
                assertFalse(manutenzioni.isEmpty(), "Dovrebbero esserci almeno alcune manutenzioni nel database");

                for (Manutenzione manutenzione : manutenzioni) {
                        assertNotNull(manutenzione.getId(), "L'ID della manutenzione non dovrebbe essere null");
                        assertNotNull(manutenzione.getDataInizio(), "La data di inizio non dovrebbe essere null");
                        assertNotNull(manutenzione.getDescrizione(), "La descrizione non dovrebbe essere null");
                        assertNotNull(manutenzione.getCampo(), "Il campo non dovrebbe essere null");
                }

                System.out.println("Recuperate " + manutenzioni.size() + " manutenzioni dal database");
        }

}
