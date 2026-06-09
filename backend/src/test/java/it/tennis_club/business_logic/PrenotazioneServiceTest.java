package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.ManutenzioneDAO;
import it.tennis_club.orm.PrenotazioneDAO;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test unitari per PrenotazioneService.
 * Usa Mockito per isolare la business logic dai DAO.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PrenotazioneService - Unit Test")
class PrenotazioneServiceTest {

        @Mock
        private PrenotazioneDAO prenotazioneDAO;

        @Mock
        private ManutenzioneDAO manutenzioneDAO;

        @Mock
        private NotificationService notificationService;

        @InjectMocks
        private PrenotazioneService prenotazioneService;

        private Campo campoTest;
        private Utente socioTest;
        private LocalDate dataFutura;
        private LocalTime oraValida;

        @BeforeEach
        void setUp() {
                campoTest = new Campo();
                campoTest.setId(1);
                campoTest.setNome("Campo Centrale");

                socioTest = new Utente();
                socioTest.setId(1);
                socioTest.setNome("Mario");
                socioTest.setCognome("Rossi");
                socioTest.setEmail("mario@tennis.it");
                socioTest.setRuolo(Utente.Ruolo.SOCIO);

                dataFutura = LocalDate.now().plusDays(10);
                oraValida = LocalTime.of(10, 0);
        }

        // ========== TEST VALIDAZIONE INPUT ==========

        @Test
        @DisplayName("Crea prenotazione con parametri null lancia eccezione")
        void testCreaPrenotazioneParametriNull() {
                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(null, oraValida, campoTest, socioTest));
                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataFutura, null, campoTest, socioTest));
                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataFutura, oraValida, null, socioTest));
                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataFutura, oraValida, campoTest, null));
        }

        @Test
        @DisplayName("Crea prenotazione con data passata lancia eccezione")
        void testCreaPrenotazioneDataPassata() {
                LocalDate dataPassata = LocalDate.now().minusDays(1);
                PrenotazioneException ex = assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataPassata, oraValida, campoTest,
                                                socioTest));
                assertTrue(ex.getMessage().contains("passata"));
        }

        @Test
        @DisplayName("Crea prenotazione con orario non valido lancia eccezione")
        void testCreaPrenotazioneOrarioNonValido() {
                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataFutura, LocalTime.of(7, 0), campoTest,
                                                socioTest));
                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataFutura, LocalTime.of(23, 0), campoTest,
                                                socioTest));
        }

        // ========== TEST BUSINESS LOGIC ==========

        @Test
        @DisplayName("Crea prenotazione valida con successo")
        void testCreaPrenotazioneValida() throws Exception {
                when(manutenzioneDAO.getManutenzioneAttivaByDataAndCampo(dataFutura, campoTest.getId()))
                                .thenReturn(null);
                when(prenotazioneDAO.getPrenotazioniByDataAndCampo(dataFutura, campoTest.getId()))
                                .thenReturn(Collections.emptyList());
                when(prenotazioneDAO.createPrenotazione(any(Prenotazione.class)))
                                .thenReturn(42);

                Integer id = prenotazioneService.creaPrenotazione(dataFutura, oraValida, campoTest, socioTest);

                assertEquals(42, id);
                verify(prenotazioneDAO).createPrenotazione(any(Prenotazione.class));
        }

        @Test
        @DisplayName("Crea prenotazione bloccata da manutenzione attiva")
        void testCreaPrenotazioneManutenzioneAttiva() throws Exception {
                Manutenzione manutenzione = new Manutenzione();
                manutenzione.setId(1);
                when(manutenzioneDAO.getManutenzioneAttivaByDataAndCampo(dataFutura, campoTest.getId()))
                                .thenReturn(manutenzione);

                PrenotazioneException ex = assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataFutura, oraValida, campoTest,
                                                socioTest));
                assertTrue(ex.getMessage().contains("manutenzione"));
        }

        @Test
        @DisplayName("Crea prenotazione duplicata lancia eccezione")
        void testCreaPrenotazioneDuplicata() throws Exception {
                Prenotazione esistente = new Prenotazione();
                esistente.setOraInizio(oraValida);
                when(manutenzioneDAO.getManutenzioneAttivaByDataAndCampo(dataFutura, campoTest.getId()))
                                .thenReturn(null);
                when(prenotazioneDAO.getPrenotazioniByDataAndCampo(dataFutura, campoTest.getId()))
                                .thenReturn(List.of(esistente));

                PrenotazioneException ex = assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.creaPrenotazione(dataFutura, oraValida, campoTest,
                                                socioTest));
                assertTrue(ex.getMessage().contains("già prenotato"));
        }

        @Test
        @DisplayName("Cancella prenotazione esistente con successo")
        void testCancellaPrenotazione() throws Exception {
                Prenotazione prenotazione = new Prenotazione();
                prenotazione.setId(1);
                prenotazione.setSocio(socioTest);
                when(prenotazioneDAO.getPrenotazioneById(1)).thenReturn(prenotazione);
                when(prenotazioneDAO.deletePrenotazione(1)).thenReturn(true);

                boolean risultato = prenotazioneService.cancellaPrenotazione(1);
                assertTrue(risultato);
        }

        @Test
        @DisplayName("Cancella prenotazione inesistente lancia eccezione")
        void testCancellaPrenotazioneInesistente() throws Exception {
                when(prenotazioneDAO.getPrenotazioneById(999)).thenReturn(null);

                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.cancellaPrenotazione(999));
        }

        @Test
        @DisplayName("Recupera prenotazione per ID esistente")
        void testGetPrenotazioneById() throws Exception {
                Prenotazione prenotazione = new Prenotazione();
                prenotazione.setId(1);
                when(prenotazioneDAO.getPrenotazioneById(1)).thenReturn(prenotazione);

                Prenotazione risultato = prenotazioneService.getPrenotazionePerId(1);
                assertNotNull(risultato);
                assertEquals(1, risultato.getId());
        }

        @Test
        @DisplayName("Recupera prenotazione per ID inesistente lancia eccezione")
        void testGetPrenotazioneByIdInesistente() throws Exception {
                when(prenotazioneDAO.getPrenotazioneById(999)).thenReturn(null);

                assertThrows(PrenotazioneException.class,
                                () -> prenotazioneService.getPrenotazionePerId(999));
        }

        @Test
        @DisplayName("Verifica disponibilità campo occupato")
        void testCampoNonDisponibile() throws Exception {
                Prenotazione esistente = new Prenotazione();
                esistente.setOraInizio(oraValida);
                when(prenotazioneDAO.getPrenotazioniByDataAndCampo(dataFutura, campoTest.getId()))
                                .thenReturn(List.of(esistente));

                boolean disponibile = prenotazioneService.isCampoDisponibile(dataFutura, oraValida, campoTest);
                assertFalse(disponibile);
        }

        @Test
        @DisplayName("Verifica disponibilità campo libero")
        void testCampoDisponibile() throws Exception {
                when(prenotazioneDAO.getPrenotazioniByDataAndCampo(dataFutura, campoTest.getId()))
                                .thenReturn(Collections.emptyList());

                boolean disponibile = prenotazioneService.isCampoDisponibile(dataFutura, oraValida, campoTest);
                assertTrue(disponibile);
        }

        @Test
        @DisplayName("Recupera tutte le prenotazioni")
        void testGetAllPrenotazioni() throws Exception {
                when(prenotazioneDAO.getAllPrenotazioni()).thenReturn(List.of(new Prenotazione()));

                List<Prenotazione> risultato = prenotazioneService.getPrenotazioni();
                assertNotNull(risultato);
                assertFalse(risultato.isEmpty());
        }
}
