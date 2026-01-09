package it.tennis_club.orm;

import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO delle prenotazioni.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 * 
 * IMPORTANTE: I test di creazione, aggiornamento ed eliminazione
 * modificano il database. Eseguire reset.sql e default.sql tra le esecuzioni
 * se necessario per ripristinare lo stato iniziale.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PrenotazioneDAOTest {

    private PrenotazioneDAO prenotazioneDAO;
    private CampoDAO campoDAO;
    private UtenteDAO utenteDAO;

    @BeforeEach
    void setUp() {
        prenotazioneDAO = new PrenotazioneDAO();
        campoDAO = new CampoDAO();
        utenteDAO = new UtenteDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Verifica il recupero di tutte le prenotazioni")
    void testGetAllPrenotazioni() throws SQLException {
        List<Prenotazione> prenotazioni = prenotazioneDAO.getAllPrenotazioni();

        assertNotNull(prenotazioni, "La lista delle prenotazioni non dovrebbe essere null");

        // Verifica che le prenotazioni abbiano tutti i dati popolati
        for (Prenotazione prenotazione : prenotazioni) {
            assertNotNull(prenotazione.getId(), "L'ID non dovrebbe essere null");
            assertNotNull(prenotazione.getData(), "La data non dovrebbe essere null");
            assertNotNull(prenotazione.getOraInizio(), "L'ora di inizio non dovrebbe essere null");
            assertNotNull(prenotazione.getCampo(), "Il campo non dovrebbe essere null");
            assertNotNull(prenotazione.getSocio(), "Il socio non dovrebbe essere null");

            // Verifica che gli oggetti Campo e Utente siano completi
            assertNotNull(prenotazione.getCampo().getNome(), "Il nome del campo non dovrebbe essere null");
            assertNotNull(prenotazione.getSocio().getNome(), "Il nome del socio non dovrebbe essere null");
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni nel database");
        prenotazioni.forEach(System.out::println);
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero di una prenotazione specifica per ID")
    void testGetPrenotazioneById() throws SQLException {
        // Prima otteniamo tutte le prenotazioni per trovare un ID valido
        List<Prenotazione> prenotazioni = prenotazioneDAO.getAllPrenotazioni();

        if (!prenotazioni.isEmpty()) {
            Integer idDaCercare = prenotazioni.get(0).getId();
            Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(idDaCercare);

            assertNotNull(prenotazione, "La prenotazione dovrebbe esistere");
            assertEquals(idDaCercare, prenotazione.getId());
            assertNotNull(prenotazione.getCampo());
            assertNotNull(prenotazione.getSocio());

            System.out.println("Prenotazione trovata: " + prenotazione);
        } else {
            System.out.println("Nessuna prenotazione presente nel database per testare getPrenotazioneById");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Verifica che getPrenotazioneById restituisca null per ID inesistente")
    void testGetPrenotazioneByIdNotFound() throws SQLException {
        Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(9999);

        assertNull(prenotazione, "Non dovrebbe trovare una prenotazione con ID inesistente");
    }

    @Test
    @Order(4)
    @DisplayName("Verifica il recupero delle prenotazioni per data")
    void testGetPrenotazioniByData() throws SQLException {
        // Usa una data specifica o quella di una prenotazione esistente
        LocalDate data = LocalDate.now();
        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByData(data);

        assertNotNull(prenotazioni, "La lista non dovrebbe essere null");

        // Verifica che tutte le prenotazioni abbiano la data corretta
        for (Prenotazione prenotazione : prenotazioni) {
            assertEquals(data, prenotazione.getData(),
                    "Tutte le prenotazioni dovrebbero avere la data richiesta");
        }

        System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per la data " + data);
    }

    @Test
    @Order(5)
    @DisplayName("Verifica il recupero delle prenotazioni per campo")
    void testGetPrenotazioniByCampo() throws SQLException {
        // Ottieni un campo dal database
        List<Campo> campi = campoDAO.getAllCampi();

        if (!campi.isEmpty()) {
            Integer idCampo = campi.get(0).getId();
            List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByCampo(idCampo);

            assertNotNull(prenotazioni, "La lista non dovrebbe essere null");

            // Verifica che tutte le prenotazioni siano per il campo richiesto
            for (Prenotazione prenotazione : prenotazioni) {
                assertEquals(idCampo, prenotazione.getCampo().getId(),
                        "Tutte le prenotazioni dovrebbero essere per il campo richiesto");
            }

            System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per il campo ID " + idCampo);
        } else {
            System.out.println("Nessun campo presente nel database per testare getPrenotazioniByCampo");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Verifica il recupero delle prenotazioni per socio")
    void testGetPrenotazioniBySocio() throws SQLException {
        // Prima otteniamo tutte le prenotazioni per trovare un socio
        List<Prenotazione> tuttePrenotazioni = prenotazioneDAO.getAllPrenotazioni();

        if (!tuttePrenotazioni.isEmpty()) {
            Integer idSocio = tuttePrenotazioni.get(0).getSocio().getId();
            List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniBySocio(idSocio);

            assertNotNull(prenotazioni, "La lista non dovrebbe essere null");
            assertFalse(prenotazioni.isEmpty(), "Dovrebbe esserci almeno una prenotazione");

            // Verifica che tutte le prenotazioni siano del socio richiesto
            for (Prenotazione prenotazione : prenotazioni) {
                assertEquals(idSocio, prenotazione.getSocio().getId(),
                        "Tutte le prenotazioni dovrebbero essere del socio richiesto");
            }

            System.out.println("Trovate " + prenotazioni.size() + " prenotazioni per il socio ID " + idSocio);
        } else {
            System.out.println("Nessuna prenotazione presente nel database per testare getPrenotazioniBySocio");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Verifica il recupero delle prenotazioni per data e campo")
    void testGetPrenotazioniByDataAndCampo() throws SQLException {
        List<Prenotazione> tuttePrenotazioni = prenotazioneDAO.getAllPrenotazioni();

        if (!tuttePrenotazioni.isEmpty()) {
            Prenotazione esempio = tuttePrenotazioni.get(0);
            LocalDate data = esempio.getData();
            Integer idCampo = esempio.getCampo().getId();

            List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByDataAndCampo(data, idCampo);

            assertNotNull(prenotazioni, "La lista non dovrebbe essere null");
            assertFalse(prenotazioni.isEmpty(), "Dovrebbe esserci almeno una prenotazione");

            // Verifica che tutte abbiano data e campo corretti
            for (Prenotazione prenotazione : prenotazioni) {
                assertEquals(data, prenotazione.getData());
                assertEquals(idCampo, prenotazione.getCampo().getId());
            }

            System.out.println("Trovate " + prenotazioni.size() +
                    " prenotazioni per data " + data + " e campo ID " + idCampo);
        } else {
            System.out.println("Nessuna prenotazione presente per testare getPrenotazioniByDataAndCampo");
        }
    }

    @Test
    @Order(8)
    @DisplayName("Verifica la creazione di una nuova prenotazione")
    void testCreatePrenotazione() throws SQLException {
        // Ottieni un campo e un utente esistenti
        List<Campo> campi = campoDAO.getAllCampi();

        // Prova a fare login o usa getUtenteById se conosci un ID
        // Per semplicità, assumiamo che esista un utente con ID 1 o 2
        Utente socio = utenteDAO.getUtenteById(1);

        if (socio == null) {
            // Prova con ID 2
            socio = utenteDAO.getUtenteById(2);
        }

        if (!campi.isEmpty() && socio != null) {
            // Crea una nuova prenotazione
            Prenotazione nuovaPrenotazione = new Prenotazione();
            nuovaPrenotazione.setData(LocalDate.now().plusDays(7)); // Una settimana da oggi
            nuovaPrenotazione.setOraInizio(LocalTime.of(10, 0)); // Ore 10:00
            nuovaPrenotazione.setCampo(campi.get(0));
            nuovaPrenotazione.setSocio(socio);

            // Inserisci nel database
            Integer generatedId = prenotazioneDAO.createPrenotazione(nuovaPrenotazione);

            assertNotNull(generatedId, "L'ID generato non dovrebbe essere null");
            assertTrue(generatedId > 0, "L'ID generato dovrebbe essere positivo");
            assertEquals(generatedId, nuovaPrenotazione.getId(),
                    "L'ID dovrebbe essere stato assegnato all'oggetto");

            // Verifica che la prenotazione sia stata effettivamente inserita
            Prenotazione prenotazioneInserita = prenotazioneDAO.getPrenotazioneById(generatedId);
            assertNotNull(prenotazioneInserita, "La prenotazione dovrebbe essere nel database");
            assertEquals(nuovaPrenotazione.getData(), prenotazioneInserita.getData());
            assertEquals(nuovaPrenotazione.getOraInizio(), prenotazioneInserita.getOraInizio());

            System.out.println("Prenotazione creata con ID: " + generatedId);
            System.out.println(prenotazioneInserita);
        } else {
            fail("Non ci sono campi o utenti disponibili per testare la creazione");
        }
    }

    @Test
    @Order(9)
    @DisplayName("Verifica l'aggiornamento di una prenotazione")
    void testUpdatePrenotazione() throws SQLException {
        // Prima crea una prenotazione da aggiornare
        List<Campo> campi = campoDAO.getAllCampi();
        Utente socio = utenteDAO.getUtenteById(1);
        if (socio == null)
            socio = utenteDAO.getUtenteById(2);

        if (!campi.isEmpty() && socio != null) {
            // Crea una prenotazione temporanea
            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setData(LocalDate.now().plusDays(10));
            prenotazione.setOraInizio(LocalTime.of(14, 0));
            prenotazione.setCampo(campi.get(0));
            prenotazione.setSocio(socio);

            Integer id = prenotazioneDAO.createPrenotazione(prenotazione);

            // Modifica i dati
            prenotazione.setOraInizio(LocalTime.of(15, 0)); // Cambia l'ora

            // Aggiorna nel database
            boolean success = prenotazioneDAO.updatePrenotazione(prenotazione);

            assertTrue(success, "L'aggiornamento dovrebbe avere successo");

            // Verifica che le modifiche siano state salvate
            Prenotazione prenotazioneAggiornata = prenotazioneDAO.getPrenotazioneById(id);
            assertEquals(LocalTime.of(15, 0), prenotazioneAggiornata.getOraInizio(),
                    "L'ora dovrebbe essere stata aggiornata");

            System.out.println("Prenotazione aggiornata: " + prenotazioneAggiornata);
        } else {
            fail("Non ci sono campi o utenti disponibili per testare l'aggiornamento");
        }
    }

    @Test
    @Order(10)
    @DisplayName("Verifica l'eliminazione di una prenotazione")
    void testDeletePrenotazione() throws SQLException {
        // Prima crea una prenotazione da eliminare
        List<Campo> campi = campoDAO.getAllCampi();
        Utente socio = utenteDAO.getUtenteById(1);
        if (socio == null)
            socio = utenteDAO.getUtenteById(2);

        if (!campi.isEmpty() && socio != null) {
            // Crea una prenotazione temporanea
            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setData(LocalDate.now().plusDays(15));
            prenotazione.setOraInizio(LocalTime.of(16, 0));
            prenotazione.setCampo(campi.get(0));
            prenotazione.setSocio(socio);

            Integer id = prenotazioneDAO.createPrenotazione(prenotazione);

            // Elimina la prenotazione
            boolean success = prenotazioneDAO.deletePrenotazione(id);

            assertTrue(success, "L'eliminazione dovrebbe avere successo");

            // Verifica che la prenotazione sia stata davvero eliminata
            Prenotazione prenotazioneEliminata = prenotazioneDAO.getPrenotazioneById(id);
            assertNull(prenotazioneEliminata,
                    "La prenotazione dovrebbe essere stata eliminata dal database");

            System.out.println("Prenotazione con ID " + id + " eliminata con successo");
        } else {
            fail("Non ci sono campi o utenti disponibili per testare l'eliminazione");
        }
    }

    @Test
    @Order(11)
    @DisplayName("Verifica che l'aggiornamento di una prenotazione inesistente fallisca")
    void testUpdatePrenotazioneNotFound() throws SQLException {
        Prenotazione prenotazioneFake = new Prenotazione();
        prenotazioneFake.setId(9999);
        prenotazioneFake.setData(LocalDate.now());
        prenotazioneFake.setOraInizio(LocalTime.of(10, 0));

        // Usa campi e utenti fittizi (ma con ID validi se esistono)
        List<Campo> campi = campoDAO.getAllCampi();
        Utente socio = utenteDAO.getUtenteById(1);

        if (!campi.isEmpty() && socio != null) {
            prenotazioneFake.setCampo(campi.get(0));
            prenotazioneFake.setSocio(socio);

            boolean success = prenotazioneDAO.updatePrenotazione(prenotazioneFake);

            assertFalse(success, "L'aggiornamento di una prenotazione inesistente dovrebbe fallire");
        }
    }

    @Test
    @Order(12)
    @DisplayName("Verifica che l'eliminazione di una prenotazione inesistente fallisca")
    void testDeletePrenotazioneNotFound() throws SQLException {
        boolean success = prenotazioneDAO.deletePrenotazione(9999);

        assertFalse(success, "L'eliminazione di una prenotazione inesistente dovrebbe fallire");
    }
}
