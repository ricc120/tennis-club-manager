package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.AllievoLezioneDAO;
import it.tennis_club.orm.LezioneDAO;
import it.tennis_club.orm.UtenteDAO;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test unitari per AccademiaService.
 * Usa Mockito per isolare la business logic dai DAO.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccademiaService - Unit Test")
class AccademiaServiceTest {

    @Mock
    private LezioneDAO lezioneDAO;

    @Mock
    private PrenotazioneService prenotazioneService;

    @Mock
    private AllievoLezioneDAO allievoLezioneDAO;

    @Mock
    private UtenteDAO utenteDAO;

    @InjectMocks
    private AccademiaService accademiaService;

    private Campo campoTest;
    private Utente maestroTest;
    private Utente allievoTest;
    private LocalDate dataFutura;
    private LocalTime oraValida;

    @BeforeEach
    void setUp() {
        campoTest = new Campo();
        campoTest.setId(1);
        campoTest.setNome("Campo Centrale");

        maestroTest = new Utente();
        maestroTest.setId(1);
        maestroTest.setNome("Paolo");
        maestroTest.setRuolo(Ruolo.MAESTRO);

        allievoTest = new Utente();
        allievoTest.setId(2);
        allievoTest.setNome("Luca");
        allievoTest.setRuolo(Ruolo.ALLIEVO);

        dataFutura = LocalDate.now().plusDays(10);
        oraValida = LocalTime.of(10, 0);
    }

    // ========== TEST CREAZIONE LEZIONE ==========

    @Test
    @DisplayName("Crea lezione valida con successo")
    void testCreaLezioneValida() throws Exception {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(1);
        when(prenotazioneService.creaPrenotazione(dataFutura, oraValida, campoTest, maestroTest))
                .thenReturn(1);
        when(prenotazioneService.getPrenotazionePerId(1)).thenReturn(prenotazione);
        when(lezioneDAO.createLezione(any(Lezione.class))).thenReturn(10);

        Integer id = accademiaService.creaLezione(dataFutura, oraValida, campoTest, maestroTest, "Test");

        assertNotNull(id);
        assertEquals(10, id);
    }

    @Test
    @DisplayName("Crea lezione con maestro null lancia eccezione")
    void testCreaLezioneMaestroNull() {
        assertThrows(AccademiaException.class,
                () -> accademiaService.creaLezione(dataFutura, oraValida, campoTest, null, "Test"));
    }

    @Test
    @DisplayName("Crea lezione con utente non maestro lancia eccezione")
    void testCreaLezioneNonMaestro() {
        Utente socio = new Utente();
        socio.setRuolo(Ruolo.SOCIO);
        assertThrows(AccademiaException.class,
                () -> accademiaService.creaLezione(dataFutura, oraValida, campoTest, socio, "Test"));
    }

    @Test
    @DisplayName("Crea lezione con parametri null lancia eccezione")
    void testCreaLezioneParametriNull() {
        assertThrows(AccademiaException.class,
                () -> accademiaService.creaLezione(null, oraValida, campoTest, maestroTest, "Test"));
        assertThrows(AccademiaException.class,
                () -> accademiaService.creaLezione(dataFutura, null, campoTest, maestroTest, "Test"));
        assertThrows(AccademiaException.class,
                () -> accademiaService.creaLezione(dataFutura, oraValida, null, maestroTest, "Test"));
    }

    // ========== TEST GESTIONE ALLIEVI ==========

    @Test
    @DisplayName("Aggiunge allievo a lezione con successo")
    void testAggiungiAllievo() throws Exception {
        Lezione lezione = new Lezione();
        lezione.setId(1);
        when(lezioneDAO.getLezioneById(1)).thenReturn(lezione);
        when(allievoLezioneDAO.contaAllievi(1)).thenReturn(0);

        assertDoesNotThrow(() -> accademiaService.aggiungiAllievo(1, allievoTest));
        verify(allievoLezioneDAO).aggiungiAllievoLezione(1, allievoTest.getId());
    }

    @Test
    @DisplayName("Aggiunge allievo a lezione inesistente lancia eccezione")
    void testAggiungiAllievoLezioneInesistente() throws Exception {
        when(lezioneDAO.getLezioneById(999)).thenReturn(null);
        assertThrows(AccademiaException.class,
                () -> accademiaService.aggiungiAllievo(999, allievoTest));
    }

    @Test
    @DisplayName("Rimuove allievo con successo")
    void testRimuoviAllievo() throws Exception {
        when(allievoLezioneDAO.rimuoviAllievoLezione(1, 2)).thenReturn(true);
        assertTrue(accademiaService.rimuoviAllievo(1, 2));
    }

    @Test
    @DisplayName("Rimuove allievo non iscritto lancia eccezione")
    void testRimuoviAllievoNonIscritto() throws Exception {
        when(allievoLezioneDAO.rimuoviAllievoLezione(1, 2)).thenReturn(false);
        assertThrows(AccademiaException.class,
                () -> accademiaService.rimuoviAllievo(1, 2));
    }

    // ========== TEST PRESENZA E FEEDBACK ==========

    @Test
    @DisplayName("Segna presenza con successo")
    void testSegnaPresenza() throws Exception {
        when(allievoLezioneDAO.segnaPresenza(1, 2, true)).thenReturn(true);
        assertTrue(accademiaService.segnaPresenza(1, 2, true));
    }

    @Test
    @DisplayName("Aggiunge feedback con successo")
    void testAggiungiFeedback() throws Exception {
        when(allievoLezioneDAO.aggiungiFeedback(1, 2, "Bravo")).thenReturn(true);
        assertTrue(accademiaService.aggiungiFeedback(1, 2, "Bravo"));
    }

    // ========== TEST QUERY ==========

    @Test
    @DisplayName("Recupera tutte le lezioni")
    void testGetLezioni() throws Exception {
        when(lezioneDAO.getAllLezioni()).thenReturn(List.of(new Lezione()));
        List<Lezione> lezioni = accademiaService.getLezioni();
        assertNotNull(lezioni);
        assertFalse(lezioni.isEmpty());
    }

    @Test
    @DisplayName("Recupera lezione per ID inesistente lancia eccezione")
    void testGetLezioneByIdInesistente() throws Exception {
        when(lezioneDAO.getLezioneById(999)).thenReturn(null);
        assertThrows(AccademiaException.class,
                () -> accademiaService.getLezionePerId(999));
    }

    @Test
    @DisplayName("Conta allievi in una lezione")
    void testContaAllievi() throws Exception {
        when(allievoLezioneDAO.contaAllievi(1)).thenReturn(3);
        assertEquals(3, accademiaService.contaAllievi(1));
    }
}
