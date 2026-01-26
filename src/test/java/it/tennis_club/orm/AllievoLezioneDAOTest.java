package it.tennis_club.orm;

import it.tennis_club.domain_model.AllievoLezione;
import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Utente;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO della relazione allievo-lezione.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 * 
 * IMPORTANTE: I test di creazione, aggiornamento ed eliminazione
 * modificano il database. Eseguire reset.sql e default.sql tra le esecuzioni
 * se necessario per ripristinare lo stato iniziale.
 * 
 * NOTA: AllievoLezioneDAO gestisce la relazione molti-a-molti tra
 * lezioni e allievi, tracciando presenze e feedback personalizzati.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllievoLezioneDAOTest {

    private AllievoLezioneDAO allievoLezioneDAO;
    private LezioneDAO lezioneDAO;
    private UtenteDAO utenteDAO;

    @BeforeEach
    void setUp() {
        allievoLezioneDAO = new AllievoLezioneDAO();
        lezioneDAO = new LezioneDAO();
        utenteDAO = new UtenteDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Verifica l'aggiunta di un allievo a più lezioni diverse")
    void testAggiungiAllievoAMolteLezioni() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        assertNotNull(lezioni, "Le lezioni non dovrebbero essere nulle");
        assertTrue(lezioni.size() >= 2, "Servono almeno 2 lezioni nel database per questo test");

        Utente allievo = utenteDAO.getUtenteById(1);
        if (allievo == null)
            allievo = utenteDAO.getUtenteById(2);
        assertNotNull(allievo, "L'allievo non dovrebbe essere nullo");

        // Aggiungiamo l'allievo alla prima lezione
        Integer id1 = allievoLezioneDAO.aggiungiAllievoALezione(lezioni.get(0).getId(), allievo.getId());
        assertNotNull(id1);

        // Aggiungiamo lo STESSO allievo a una SECONDA lezione
        try {
            Integer id2 = allievoLezioneDAO.aggiungiAllievoALezione(lezioni.get(1).getId(), allievo.getId());
            assertNotNull(id2);
        } catch (SQLException e) {
            // Se è già iscritto va bene, il test passa lo stesso perché verifichiamo il
            // conteggio sotto
        }

        // Verifica: l'allievo deve risultare in almeno 2 lezioni
        List<Lezione> lezioniAllievo = allievoLezioneDAO.getLezioniByAllievo(allievo.getId());
        assertTrue(lezioniAllievo.size() >= 2, "L'allievo dovrebbe partecipare ad almeno 2 lezioni");

        System.out.println("Test molti-a-molti (1 allievo -> N lezioni) superato.");
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero di più allievi per la stessa lezione")
    void testGetMoltiAllieviPerLezione() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        if (lezioni.size() > 0) {
            Integer idLezione = lezioni.get(0).getId();

            // Proviamo ad aggiungere due allievi diversi
            Utente allievo1 = utenteDAO.getUtenteById(1);
            Utente allievo2 = utenteDAO.getUtenteById(2);

            if (allievo1 != null && allievo2 != null) {
                try {
                    allievoLezioneDAO.aggiungiAllievoALezione(idLezione, allievo1.getId());
                } catch (Exception e) {
                }
                try {
                    allievoLezioneDAO.aggiungiAllievoALezione(idLezione, allievo2.getId());
                } catch (Exception e) {
                }

                List<Utente> allievi = allievoLezioneDAO.getAllieviByLezione(idLezione);
                assertNotNull(allievi);
                assertTrue(allievi.size() >= 2, "La lezione dovrebbe avere almeno 2 allievi");

                System.out.println("Test molti-a-molti (N allievi -> 1 lezione) superato.");
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Verifica il recupero delle lezioni per allievo con controlli di integrità")
    void testGetLezioniByAllievoDettagliato() throws SQLException {
        Utente allievo = utenteDAO.getUtenteById(1);
        if (allievo == null)
            allievo = utenteDAO.getUtenteById(2);

        if (allievo != null) {
            List<Lezione> lezioni = allievoLezioneDAO.getLezioniByAllievo(allievo.getId());
            assertNotNull(lezioni);

            for (Lezione lezione : lezioni) {
                assertNotNull(lezione.getId());
                assertNotNull(lezione.getPrenotazione(), "La lezione deve avere la prenotazione popolata");
                assertNotNull(lezione.getMaestro(), "La lezione deve avere il maestro popolato");
            }

            System.out.println("Verifica integrità dati lezioni allievo superata.");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Verifica la segnatura della presenza per più allievi e l'isolamento")
    void testSegnaPresenzaMultiplo() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        if (lezioni.size() > 0) {
            Integer idLezione = lezioni.get(0).getId();

            // Prepariamo almeno 2 allievi per la lezione
            Utente a1 = utenteDAO.getUtenteById(1);
            Utente a2 = utenteDAO.getUtenteById(2);
            if (a1 == null || a2 == null)
                return;

            try {
                allievoLezioneDAO.aggiungiAllievoALezione(idLezione, a1.getId());
            } catch (Exception e) {
            }
            try {
                allievoLezioneDAO.aggiungiAllievoALezione(idLezione, a2.getId());
            } catch (Exception e) {
            }

            // 1. Allievo 1 -> Assente, Allievo 2 -> Presente
            allievoLezioneDAO.segnaPresenza(idLezione, a1.getId(), false);
            allievoLezioneDAO.segnaPresenza(idLezione, a2.getId(), true);

            // Verifica isolamento
            AllievoLezione dett1 = allievoLezioneDAO.getDettagliAllievoLezione(idLezione, a1.getId());
            AllievoLezione dett2 = allievoLezioneDAO.getDettagliAllievoLezione(idLezione, a2.getId());

            assertFalse(dett1.getPresente(), "Allievo 1 dovrebbe essere assente");
            assertTrue(dett2.getPresente(), "Allievo 2 dovrebbe essere presente");

            System.out.println("Test isolamento presenze superato.");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Verifica l'aggiunta di feedback personalizzati per diversi allievi")
    void testAggiungiFeedbackMultiplo() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        if (lezioni.size() > 0) {
            Integer idLezione = lezioni.get(0).getId();
            Utente a1 = utenteDAO.getUtenteById(1);
            Utente a2 = utenteDAO.getUtenteById(2);
            if (a1 == null || a2 == null)
                return;

            String f1 = "Ottimo dritto per Allievo 1";
            String f2 = "Migliorare il servizio per Allievo 2";

            allievoLezioneDAO.aggiungiFeedback(idLezione, a1.getId(), f1);
            allievoLezioneDAO.aggiungiFeedback(idLezione, a2.getId(), f2);

            // Verifica che i feedback siano rimasti distinti
            assertEquals(f1, allievoLezioneDAO.getDettagliAllievoLezione(idLezione, a1.getId()).getFeedback());
            assertEquals(f2, allievoLezioneDAO.getDettagliAllievoLezione(idLezione, a2.getId()).getFeedback());

            System.out.println("Test feedback multipli e distinti superato.");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Verifica il conteggio degli allievi in una lezione")
    void testContaAllievi() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();

        if (!lezioni.isEmpty()) {
            Integer idLezione = lezioni.get(0).getId();
            int count = allievoLezioneDAO.contaAllievi(idLezione);

            assertTrue(count >= 0, "Il conteggio dovrebbe essere non negativo");

            // Verifica che corrisponda al numero effettivo
            List<Utente> allievi = allievoLezioneDAO.getAllieviByLezione(idLezione);
            assertEquals(allievi.size(), count,
                    "Il conteggio dovrebbe corrispondere al numero di allievi recuperati");

            System.out.println("Numero di allievi nella lezione ID " + idLezione + ": " + count);
        } else {
            System.out.println("Nessuna lezione presente nel database per testare contaAllievi");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Verifica il recupero dei dettagli completi di un allievo in una lezione")
    void testGetDettagliAllievoLezione() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();

        if (!lezioni.isEmpty()) {
            Integer idLezione = lezioni.get(0).getId();
            List<Utente> allievi = allievoLezioneDAO.getAllieviByLezione(idLezione);

            if (!allievi.isEmpty()) {
                Integer idAllievo = allievi.get(0).getId();

                AllievoLezione dettagli = allievoLezioneDAO.getDettagliAllievoLezione(idLezione, idAllievo);

                assertNotNull(dettagli, "I dettagli non dovrebbero essere null");
                assertNotNull(dettagli.getId(), "L'ID non dovrebbe essere null");
                assertNotNull(dettagli.getLezione(), "La lezione non dovrebbe essere null");
                assertNotNull(dettagli.getAllievo(), "L'allievo non dovrebbe essere null");
                assertNotNull(dettagli.getPresente(), "Il campo presente non dovrebbe essere null");

                assertEquals(idLezione, dettagli.getLezione().getId(),
                        "L'ID della lezione dovrebbe corrispondere");
                assertEquals(idAllievo, dettagli.getAllievo().getId(),
                        "L'ID dell'allievo dovrebbe corrispondere");

                System.out.println("Dettagli allievo-lezione: " + dettagli);
            } else {
                System.out.println("Nessun allievo nella lezione per testare getDettagliAllievoLezione");
            }
        } else {
            System.out.println("Nessuna lezione presente nel database per testare getDettagliAllievoLezione");
        }
    }

    @Test
    @Order(8)
    @DisplayName("Verifica che getDettagliAllievoLezione restituisca null per combinazione inesistente")
    void testGetDettagliAllievoLezioneNotFound() throws SQLException {
        AllievoLezione dettagli = allievoLezioneDAO.getDettagliAllievoLezione(9999, 9999);

        assertNull(dettagli, "Non dovrebbe trovare dettagli per una combinazione inesistente");
    }

    @Test
    @Order(9)
    @DisplayName("Verifica il recupero di tutti gli allievi con dettagli completi")
    void testGetAllieviLezioneConDettagli() throws SQLException {
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();

        if (!lezioni.isEmpty()) {
            Integer idLezione = lezioni.get(0).getId();
            List<AllievoLezione> allieviConDettagli = allievoLezioneDAO.getAllieviLezioneConDettagli(idLezione);

            assertNotNull(allieviConDettagli, "La lista non dovrebbe essere null");

            // Verifica che ogni elemento sia completo
            for (AllievoLezione al : allieviConDettagli) {
                assertNotNull(al.getId(), "L'ID non dovrebbe essere null");
                assertNotNull(al.getLezione(), "La lezione non dovrebbe essere null");
                assertNotNull(al.getAllievo(), "L'allievo non dovrebbe essere null");
                assertNotNull(al.getPresente(), "Il campo presente non dovrebbe essere null");

                assertEquals(idLezione, al.getLezione().getId(),
                        "Tutti gli elementi dovrebbero riferirsi alla stessa lezione");
            }

            System.out.println("Trovati " + allieviConDettagli.size() +
                    " allievi con dettagli per la lezione ID " + idLezione);
            allieviConDettagli.forEach(System.out::println);
        } else {
            System.out.println("Nessuna lezione presente nel database per testare getAllieviLezioneConDettagli");
        }
    }

    @Test
    @Order(10)
    @DisplayName("Verifica la rimozione di un allievo da una lezione")
    void testRimuoviAllievoLezione() throws SQLException {
        // Prima aggiungi un allievo a una lezione
        List<Lezione> lezioni = lezioneDAO.getAllLezioni();
        Utente allievo = utenteDAO.getUtenteById(1);
        if (allievo == null) {
            allievo = utenteDAO.getUtenteById(2);
        }

        if (!lezioni.isEmpty() && allievo != null) {
            Integer idLezione = lezioni.get(0).getId();
            Integer idAllievo = allievo.getId();

            // Aggiungi l'allievo (potrebbe già esistere, ma non è un problema per il test)
            try {
                allievoLezioneDAO.aggiungiAllievoALezione(idLezione, idAllievo);
            } catch (SQLException e) {
                // Ignora se già esiste
            }

            // Conta gli allievi prima della rimozione
            int countPrima = allievoLezioneDAO.contaAllievi(idLezione);

            // Rimuovi l'allievo
            boolean success = allievoLezioneDAO.rimuoviAllievoLezione(idLezione, idAllievo);
            assertTrue(success, "La rimozione dovrebbe avere successo");

            // Verifica che sia stato effettivamente rimosso
            int countDopo = allievoLezioneDAO.contaAllievi(idLezione);
            assertEquals(countPrima - 1, countDopo,
                    "Il numero di allievi dovrebbe essere diminuito di 1");

            // Verifica che i dettagli non siano più recuperabili
            AllievoLezione dettagli = allievoLezioneDAO.getDettagliAllievoLezione(idLezione, idAllievo);
            assertNull(dettagli, "I dettagli non dovrebbero più esistere dopo la rimozione");

            System.out.println("Allievo rimosso con successo dalla lezione");
        } else {
            fail("Non ci sono lezioni o allievi disponibili per testare la rimozione");
        }
    }

    @Test
    @Order(11)
    @DisplayName("Verifica che la rimozione di un allievo inesistente fallisca")
    void testRimuoviAllievoLezioneNotFound() throws SQLException {
        boolean success = allievoLezioneDAO.rimuoviAllievoLezione(9999, 9999);

        assertFalse(success, "La rimozione di una relazione inesistente dovrebbe fallire");
    }

    @Test
    @Order(12)
    @DisplayName("Verifica che segnaPresenza fallisca per combinazione inesistente")
    void testSegnaPresenzaNotFound() throws SQLException {
        boolean success = allievoLezioneDAO.segnaPresenza(9999, 9999, true);

        assertFalse(success, "La segnatura della presenza dovrebbe fallire per combinazione inesistente");
    }

    @Test
    @Order(13)
    @DisplayName("Verifica che aggiungiFeedback fallisca per combinazione inesistente")
    void testAggiungiFeedbackNotFound() throws SQLException {
        boolean success = allievoLezioneDAO.aggiungiFeedback(9999, 9999, "Feedback test");

        assertFalse(success, "L'aggiunta del feedback dovrebbe fallire per combinazione inesistente");
    }
}
