package it.tennis_club.orm;

import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Manutenzione.Stato;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test di integrazione per i DAO.
 * Si connette al PostgreSQL avviato da docker-compose (localhost:5432),
 * consentendo di verificare le query SQL su un database identico a quello di
 * produzione.
 * Le migrazioni Flyway vengono applicate automaticamente.
 * 
 * Prerequisito: docker-compose up -d
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("DAO - Test di Integrazione con PostgreSQL")
class DAOIntegrationTest {

    @Autowired
    private UtenteDAO utenteDAO;

    @Autowired
    private CampoDAO campoDAO;

    @Autowired
    private PrenotazioneDAO prenotazioneDAO;

    @Autowired
    private ManutenzioneDAO manutenzioneDAO;

    // Contatore per generare email uniche
    private static final AtomicInteger counter = new AtomicInteger((int) (System.nanoTime() % 100000));

    private String uniqueEmail(String prefix) {
        return prefix + counter.incrementAndGet() + "@test.tennis.it";
    }

    // ========== UtenteDAO ==========

    @Test
    @Order(1)
    @DisplayName("UtenteDAO - Registrazione nuovo utente con successo")
    void testRegistrazioneUtente() throws SQLException {
        Utente utente = creaUtente("Test", "User", uniqueEmail("reg"), "password123", Ruolo.SOCIO);
        Integer id = utenteDAO.registrazione(utente);

        assertNotNull(id, "L'ID generato non dovrebbe essere null");
        assertTrue(id > 0, "L'ID generato dovrebbe essere positivo");

        // Pulizia
        utenteDAO.deleteUtente(id);
    }

    @Test
    @Order(2)
    @DisplayName("UtenteDAO - Registrazione con email duplicata lancia eccezione")
    void testRegistrazioneEmailDuplicata() throws SQLException {
        String email = uniqueEmail("dup");
        Utente utente1 = creaUtente("Primo", "Utente", email, "pwd1", Ruolo.SOCIO);
        Integer id1 = utenteDAO.registrazione(utente1);

        Utente utente2 = creaUtente("Secondo", "Utente", email, "pwd2", Ruolo.SOCIO);
        assertThrows(SQLException.class, () -> utenteDAO.registrazione(utente2));

        // Pulizia
        utenteDAO.deleteUtente(id1);
    }

    @Test
    @Order(3)
    @DisplayName("UtenteDAO - Recupera utente per ID")
    void testGetUtenteById() throws SQLException {
        Utente utente = creaUtente("Mario", "Rossi", uniqueEmail("byid"), "pwd", Ruolo.SOCIO);
        Integer id = utenteDAO.registrazione(utente);

        Utente recuperato = utenteDAO.getUtenteById(id);
        assertNotNull(recuperato);
        assertEquals("Mario", recuperato.getNome());
        assertEquals("Rossi", recuperato.getCognome());

        // Pulizia
        utenteDAO.deleteUtente(id);
    }

    @Test
    @Order(4)
    @DisplayName("UtenteDAO - GetUtenteById restituisce null per ID inesistente")
    void testGetUtenteByIdInesistente() throws SQLException {
        assertNull(utenteDAO.getUtenteById(999999));
    }

    @Test
    @Order(5)
    @DisplayName("UtenteDAO - Recupera utenti per ruolo")
    void testGetUtentiByRuolo() throws SQLException {
        Utente maestro = creaUtente("Prof", "Tennis", uniqueEmail("maestro"), "pwd", Ruolo.MAESTRO);
        Integer id = utenteDAO.registrazione(maestro);

        List<Utente> maestri = utenteDAO.getUtentiByRuolo(Ruolo.MAESTRO);
        assertNotNull(maestri);
        assertTrue(maestri.stream().anyMatch(u -> u.getId().equals(id)));

        // Pulizia
        utenteDAO.deleteUtente(id);
    }

    @Test
    @Order(6)
    @DisplayName("UtenteDAO - Cancellazione utente")
    void testDeleteUtente() throws SQLException {
        Utente utente = creaUtente("Da", "Cancellare", uniqueEmail("del"), "pwd", Ruolo.SOCIO);
        Integer id = utenteDAO.registrazione(utente);

        assertTrue(utenteDAO.deleteUtente(id));
        assertNull(utenteDAO.getUtenteById(id));
    }

    // ========== CampoDAO ==========

    @Test
    @Order(10)
    @DisplayName("CampoDAO - Recupera tutti i campi (dati iniziali Flyway)")
    void testGetAllCampi() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        assertNotNull(campi);
        // I dati iniziali vengono inseriti dalla migrazione V2
        assertFalse(campi.isEmpty(), "Dovrebbero esserci campi dai dati iniziali Flyway");
    }

    @Test
    @Order(11)
    @DisplayName("CampoDAO - Recupera campo per ID")
    void testGetCampoById() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();
        assertFalse(campi.isEmpty());

        Campo primo = campi.get(0);
        Campo recuperato = campoDAO.getCampoById(primo.getId());
        assertNotNull(recuperato);
        assertEquals(primo.getNome(), recuperato.getNome());
    }

    // ========== PrenotazioneDAO ==========

    @Test
    @Order(20)
    @DisplayName("PrenotazioneDAO - Crea e recupera prenotazione")
    void testCreaPrenotazione() throws SQLException {
        // Setup: crea un utente socio
        Utente socio = creaUtente("Socio", "Prenota", uniqueEmail("prenota"), "pwd", Ruolo.SOCIO);
        Integer idSocio = utenteDAO.registrazione(socio);
        socio.setId(idSocio);

        Campo campo = campoDAO.getAllCampi().get(0);

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData(LocalDate.now().plusDays(30));
        prenotazione.setOraInizio(LocalTime.of(10, 0));
        prenotazione.setCampo(campo);
        prenotazione.setSocio(socio);

        Integer idPrenotazione = prenotazioneDAO.createPrenotazione(prenotazione);
        assertNotNull(idPrenotazione);
        assertTrue(idPrenotazione > 0);

        Prenotazione recuperata = prenotazioneDAO.getPrenotazioneById(idPrenotazione);
        assertNotNull(recuperata);
        assertEquals(LocalDate.now().plusDays(30), recuperata.getData());

        // Pulizia
        prenotazioneDAO.deletePrenotazione(idPrenotazione);
        utenteDAO.deleteUtente(idSocio);
    }

    @Test
    @Order(21)
    @DisplayName("PrenotazioneDAO - Query per data e campo")
    void testGetPrenotazioniByDataAndCampo() throws SQLException {
        Utente socio = creaUtente("Socio", "Query", uniqueEmail("query"), "pwd", Ruolo.SOCIO);
        Integer idSocio = utenteDAO.registrazione(socio);
        socio.setId(idSocio);

        Campo campo = campoDAO.getAllCampi().get(0);
        LocalDate data = LocalDate.now().plusDays(40);

        Prenotazione p = new Prenotazione();
        p.setData(data);
        p.setOraInizio(LocalTime.of(14, 0));
        p.setCampo(campo);
        p.setSocio(socio);
        Integer idP = prenotazioneDAO.createPrenotazione(p);

        List<Prenotazione> risultati = prenotazioneDAO.getPrenotazioniByDataAndCampo(data, campo.getId());
        assertNotNull(risultati);
        assertFalse(risultati.isEmpty());

        // Pulizia
        prenotazioneDAO.deletePrenotazione(idP);
        utenteDAO.deleteUtente(idSocio);
    }

    // ========== ManutenzioneDAO ==========

    @Test
    @Order(30)
    @DisplayName("ManutenzioneDAO - Crea e recupera manutenzione")
    void testCreaManutenzione() throws SQLException {
        Utente manutentore = creaUtente("Man", "Utore", uniqueEmail("man"), "pwd", Ruolo.MANUTENTORE);
        Integer idManutentore = utenteDAO.registrazione(manutentore);
        manutentore.setId(idManutentore);

        Campo campo = campoDAO.getAllCampi().get(0);

        Manutenzione manutenzione = new Manutenzione();
        manutenzione.setCampo(campo);
        manutenzione.setManutentore(manutentore);
        manutenzione.setDataInizio(LocalDate.now().plusDays(50));
        manutenzione.setDescrizione("Riparazione rete");
        manutenzione.setStato(Stato.IN_CORSO);

        Integer idManutenzione = manutenzioneDAO.createManutenzione(manutenzione);
        assertNotNull(idManutenzione);

        Manutenzione recuperata = manutenzioneDAO.getManutenzioneById(idManutenzione);
        assertNotNull(recuperata);
        assertEquals("Riparazione rete", recuperata.getDescrizione());

        // Pulizia
        manutenzioneDAO.deleteManutenzioni(idManutenzione);
        utenteDAO.deleteUtente(idManutentore);
    }

    @Test
    @Order(31)
    @DisplayName("ManutenzioneDAO - Verifica manutenzione attiva per data e campo")
    void testGetManutenzioneAttivaByDataAndCampo() throws SQLException {
        Utente manutentore = creaUtente("Man2", "Utore2", uniqueEmail("man2"), "pwd", Ruolo.MANUTENTORE);
        Integer idManutentore = utenteDAO.registrazione(manutentore);
        manutentore.setId(idManutentore);

        Campo campo = campoDAO.getAllCampi().get(0);
        LocalDate dataInizio = LocalDate.now().plusDays(60);

        Manutenzione manutenzione = new Manutenzione();
        manutenzione.setCampo(campo);
        manutenzione.setManutentore(manutentore);
        manutenzione.setDataInizio(dataInizio);
        manutenzione.setDescrizione("Test attiva");
        manutenzione.setStato(Stato.IN_CORSO);

        Integer idManutenzione = manutenzioneDAO.createManutenzione(manutenzione);

        // Verifica che la manutenzione blocca le prenotazioni per quella data
        Manutenzione attiva = manutenzioneDAO.getManutenzioneAttivaByDataAndCampo(dataInizio, campo.getId());
        assertNotNull(attiva, "Dovrebbe trovare una manutenzione attiva");

        // Verifica che una data diversa non è bloccata
        Manutenzione nonAttiva = manutenzioneDAO.getManutenzioneAttivaByDataAndCampo(
                dataInizio.plusDays(1), campo.getId());
        assertNull(nonAttiva, "Non dovrebbe trovare manutenzione per un'altra data");

        // Pulizia
        manutenzioneDAO.deleteManutenzioni(idManutenzione);
        utenteDAO.deleteUtente(idManutentore);
    }

    // ========== Helper ==========

    private Utente creaUtente(String nome, String cognome, String email, String password, Ruolo ruolo) {
        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPassword(password);
        utente.setRuolo(ruolo);
        return utente;
    }
}
