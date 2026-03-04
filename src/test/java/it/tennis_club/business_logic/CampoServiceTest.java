package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Manutenzione.Stato;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.CampoDAO;
import it.tennis_club.orm.ManutenzioneDAO;
import it.tennis_club.orm.PrenotazioneDAO;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test unitari per CampoService.
 * Usa Mockito per isolare la business logic dai DAO.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CampoService - Unit Test")
class CampoServiceTest {

    @Mock
    private CampoDAO campoDAO;

    @Mock
    private ManutenzioneDAO manutenzioneDAO;

    @Mock
    private PrenotazioneDAO prenotazioneDAO;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CampoService campoService;

    private Utente adminTest;
    private Utente manutentoreTest;
    private Utente socioTest;

    @BeforeEach
    void setUp() {
        adminTest = new Utente();
        adminTest.setId(1);
        adminTest.setRuolo(Ruolo.ADMIN);

        manutentoreTest = new Utente();
        manutentoreTest.setId(2);
        manutentoreTest.setRuolo(Ruolo.MANUTENTORE);

        socioTest = new Utente();
        socioTest.setId(3);
        socioTest.setRuolo(Ruolo.SOCIO);
    }

    // ========== TEST QUERY CAMPI ==========

    @Test
    @DisplayName("Recupera tutti i campi")
    void testGetCampi() throws Exception {
        Campo campo = new Campo();
        campo.setId(1);
        campo.setNome("Campo 1");
        when(campoDAO.getAllCampi()).thenReturn(List.of(campo));

        List<Campo> campi = campoService.getCampi();
        assertNotNull(campi);
        assertEquals(1, campi.size());
    }

    @Test
    @DisplayName("Recupera campo per ID esistente")
    void testGetCampoById() throws Exception {
        Campo campo = new Campo();
        campo.setId(1);
        campo.setNome("Campo Centrale");
        when(campoDAO.getCampoById(1)).thenReturn(campo);

        Campo risultato = campoService.getCampoPerId(1);
        assertNotNull(risultato);
        assertEquals("Campo Centrale", risultato.getNome());
    }

    @Test
    @DisplayName("Recupera campo per ID inesistente lancia eccezione")
    void testGetCampoByIdInesistente() throws Exception {
        when(campoDAO.getCampoById(999)).thenReturn(null);
        assertThrows(CampoException.class, () -> campoService.getCampoPerId(999));
    }

    // ========== TEST MANUTENZIONE ==========

    @Test
    @DisplayName("Crea manutenzione con permessi admin")
    void testCreaManutenzioneAdmin() throws Exception {
        Campo campo = new Campo();
        campo.setId(1);
        when(campoDAO.getCampoById(1)).thenReturn(campo);
        when(prenotazioneDAO.getPrenotazioniByDataAndCampo(any(), eq(1)))
                .thenReturn(Collections.emptyList());
        when(manutenzioneDAO.createManutenzione(any(Manutenzione.class))).thenReturn(1);

        LocalDate dataFutura = LocalDate.now().plusDays(5);
        Integer id = campoService.creaManutenzione(adminTest, 1, dataFutura, "Riparazione rete");

        assertNotNull(id);
        assertEquals(1, id);
    }

    @Test
    @DisplayName("Crea manutenzione senza permessi lancia eccezione")
    void testCreaManutenzioneNoPermessi() {
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(socioTest, 1, LocalDate.now().plusDays(5), "Test"));
    }

    @Test
    @DisplayName("Crea manutenzione con data passata lancia eccezione")
    void testCreaManutenzioneDataPassata() {
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(adminTest, 1, LocalDate.now().minusDays(1), "Test"));
    }

    @Test
    @DisplayName("Recupera manutenzioni per campo")
    void testGetManutenzioniPerCampo() throws Exception {
        when(manutenzioneDAO.getManutenzioniByCampo(1)).thenReturn(List.of(new Manutenzione()));

        List<Manutenzione> result = campoService.getManutenzioniPerCampo(adminTest, 1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Annulla manutenzione con successo")
    void testAnnullaManutenzione() throws Exception {
        Manutenzione manutenzione = new Manutenzione();
        manutenzione.setId(1);
        when(manutenzioneDAO.getManutenzioneById(1)).thenReturn(manutenzione);

        assertDoesNotThrow(() -> campoService.annullaManutenzione(adminTest, 1));
        verify(manutenzioneDAO).updateStatoManutenzione(1, Stato.ANNULLATA);
    }

    @Test
    @DisplayName("Verifica permessi: utente null lancia eccezione")
    void testPermessiUtenteNull() {
        assertThrows(CampoException.class,
                () -> campoService.creaManutenzione(null, 1, LocalDate.now().plusDays(5), "Test"));
    }
}
