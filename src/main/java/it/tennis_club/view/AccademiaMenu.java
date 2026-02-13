package it.tennis_club.view;

import it.tennis_club.business_logic.AccademiaService;
import it.tennis_club.business_logic.AccademiaException;
import it.tennis_club.business_logic.PrenotazioneException;
import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.business_logic.SessionManager;
import it.tennis_club.domain_model.AllievoLezione;
import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Menu CLI per la gestione dell'accademia (lezioni e allievi).
 */
public class AccademiaMenu {

    private final AccademiaService accademiaService;
    private final CampoService campoService;
    private final SessionManager sessionManager;

    public AccademiaMenu() {
        this.accademiaService = new AccademiaService();
        this.campoService = new CampoService();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Mostra il menu accademia.
     */
    public void show() {
        boolean running = true;

        while (running) {
            CLIUtils.printHeader("GESTIONE ACCADEMIA");

            Utente utenteCorrente = sessionManager.getCurrentUser();
            Utente.Ruolo ruolo = utenteCorrente != null ? utenteCorrente.getRuolo() : null;

            if (ruolo == Utente.Ruolo.MAESTRO) {
                System.out.println("═══ LEZIONI ═══");
                System.out.println("1. Crea nuova lezione");
                System.out.println("2. Visualizza tutte le lezioni");
                System.out.println("3. Le mie lezioni");
                System.out.println("4. Dettaglio lezione");
                System.out.println("5. Modifica descrizione lezione");
                System.out.println("6. Elimina lezione");
                System.out.println();
                System.out.println("═══ ALLIEVI ═══");
                System.out.println("7. Aggiungi allievo a lezione");
                System.out.println("8. Rimuovi allievo da lezione");
                System.out.println("9. Visualizza allievi di una lezione");
                System.out.println("10. Segna presenza allievo");
                System.out.println("11. Aggiungi feedback allievo");
                System.out.println();
                System.out.println("0. Torna al menu principale");
                System.out.println();

                int scelta = CLIUtils.readInt("Scelta: ");

                switch (scelta) {
                    case 1 -> creaLezione();
                    case 2 -> visualizzaTutteLeLezioni();
                    case 3 -> lezioniMaestro();
                    case 4 -> dettaglioLezione();
                    case 5 -> modificaDescrizione();
                    case 6 -> eliminaLezione();
                    case 7 -> aggiungiAllievo();
                    case 8 -> rimuoviAllievo();
                    case 9 -> visualizzaAllieviLezione();
                    case 10 -> segnaPresenza();
                    case 11 -> aggiungiFeedback();
                    case 0 -> running = false;
                    default -> CLIUtils.printError("Opzione non valida");
                }
            } else if (ruolo == Utente.Ruolo.ADMIN) {
                System.out.println("1. Visualizza tutte le lezioni");
                System.out.println("2. Dettaglio lezione");
                System.out.println("3. Visualizza allievi di una lezione");
                System.out.println();
                System.out.println("0. Torna al menu principale");
                System.out.println();

                int scelta = CLIUtils.readInt("Scelta: ");
                switch (scelta) {
                    case 1 -> visualizzaTutteLeLezioni();
                    case 2 -> dettaglioLezione();
                    case 3 -> visualizzaAllieviLezione();
                    case 0 -> running = false;
                    default -> CLIUtils.printError("Opzione non valida");
                }
            } else if (ruolo == Utente.Ruolo.ALLIEVO) {

                System.out.println("1. Le mie lezioni");
                System.out.println("2. Visualizza feedback");
                System.out.println();
                System.out.println("0. Torna al menu principale");
                System.out.println();

                int scelta = CLIUtils.readInt("Scelta: ");
                switch (scelta) {
                    case 1 -> lezioniAllievo();
                    case 2 -> visualizzaFeedback();
                    case 0 -> running = false;
                    default -> CLIUtils.printError("Opzione non valida");
                }

            }

        }
    }

    /**
     * Crea una nuova lezione.
     */
    private void creaLezione() {
        CLIUtils.printSubHeader("Crea Nuova Lezione");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            // Mostra campi disponibili
            List<Campo> campi = campoService.getCampi();
            System.out.println();
            CLIUtils.printTableHeader("ID", "Nome", "Superficie", "Coperto");
            for (Campo c : campi) {
                CLIUtils.printTableRow(
                        String.valueOf(c.getId()),
                        c.getNome(),
                        c.getTipoSuperficie(),
                        c.getIsCoperto() ? "Sì" : "No");
            }
            CLIUtils.printTableFooter(4);
            CLIUtils.printInfo("Totale: " + campi.size() + " campi");

            System.out.println();
            Integer idCampo = CLIUtils.readIntOptional("ID Campo (vuoto per annullare): ");
            if (idCampo == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }
            Campo campo = campoService.getCampoPerId(idCampo);

            LocalDate data = CLIUtils.readDate("Data lezione");
            LocalTime ora = CLIUtils.readTime("Ora inizio");
            String descrizione = CLIUtils.readString("Descrizione (opzionale): ");

            Integer idPrenotazione = accademiaService.creaLezione(data, ora, campo, utente, descrizione);
            CLIUtils.printSuccess("Lezione creata! Prenotazione ID: " + idPrenotazione);

        } catch (CampoException | AccademiaException | PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Visualizza tutte le lezioni.
     */
    private void visualizzaTutteLeLezioni() {
        CLIUtils.printSubHeader("Tutte le Lezioni");
        try {
            List<Lezione> lezioni = accademiaService.getLezioni();
            stampaListaLezioni(lezioni);
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Visualizza le lezioni del maestro corrente.
     */
    private void lezioniMaestro() {
        CLIUtils.printSubHeader("Le Mie Lezioni");
        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            List<Lezione> lezioni = accademiaService.getLezionePerMaestro(utente);
            stampaListaLezioni(lezioni);
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Mostra il dettaglio di una lezione.
     */
    private void dettaglioLezione() {
        CLIUtils.printSubHeader("Dettaglio Lezione");
        try {
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();
            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            Lezione lezione = accademiaService.getLezionePerId(idLezione);
            System.out.println();
            System.out.println("  ID:          " + lezione.getId());
            System.out.println(
                    "  Maestro:     " + lezione.getMaestro().getNome() + " "
                            + (lezione.getMaestro().getCognome() != null ? lezione.getMaestro().getCognome() : ""));
            System.out.println("  Data:        " + CLIUtils.formatDate(lezione.getPrenotazione().getData()));
            System.out.println("  Ora:         " + CLIUtils.formatTime(lezione.getPrenotazione().getOraInizio()));
            System.out.println("  Campo:       " + lezione.getPrenotazione().getCampo().getNome());
            System.out.println("  Descrizione: " + (lezione.getDescrizione() != null ? lezione.getDescrizione() : "-"));

            // Conta allievi
            int numAllievi = accademiaService.contaAllievi(idLezione);
            System.out.println("  Allievi:     " + numAllievi);

        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Modifica la descrizione di una lezione.
     */
    private void modificaDescrizione() {
        CLIUtils.printSubHeader("Modifica Descrizione Lezione");
        try {
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();
            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }
            String nuovaDescrizione = CLIUtils.readString("Nuova descrizione: ");

            boolean success = accademiaService.inserisciDescrizione(idLezione, nuovaDescrizione);
            if (success) {
                CLIUtils.printSuccess("Descrizione aggiornata.");
            } else {
                CLIUtils.printError("Impossibile aggiornare la descrizione.");
            }
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Elimina una lezione.
     */
    private void eliminaLezione() {
        CLIUtils.printSubHeader("Elimina Lezione");
        try {
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();
            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            if (CLIUtils.readConfirm("Confermi l'eliminazione?")) {
                boolean success = accademiaService.cancellaLezione(idLezione);
                if (success) {
                    CLIUtils.printSuccess("Lezione eliminata.");
                } else {
                    CLIUtils.printWarning("Lezione non trovata o già eliminata.");
                }
            }
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Aggiunge un allievo a una lezione (operazione svolta dal maestro).
     */
    private void aggiungiAllievo() {
        CLIUtils.printSubHeader("Aggiungi Allievo a Lezione");
        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        // Solo i maestri possono aggiungere allievi alle lezioni
        if (utente.getRuolo() != Utente.Ruolo.MAESTRO && utente.getRuolo() != Utente.Ruolo.ADMIN) {
            CLIUtils.printError("Solo i maestri o gli admin possono aggiungere allievi alle lezioni.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            // Mostra la lista delle lezioni
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();

            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            // Mostra la lista degli allievi disponibili
            List<Utente> allievi = accademiaService.getUtentiAllievi();
            if (allievi.isEmpty()) {
                CLIUtils.printInfo("Nessun allievo registrato nel sistema.");
                CLIUtils.waitForEnter();
                return;
            }

            stampaListaUtenti(allievi, "Allievi disponibili");
            System.out.println();
            int idAllievo = CLIUtils.readInt("ID Allievo da aggiungere: ");

            Utente allievoDaAggiungere = accademiaService.getUtentePerId(idAllievo);
            CLIUtils.printInfo("Stai aggiungendo " + allievoDaAggiungere.getNome() + " " +
                    allievoDaAggiungere.getCognome() + " alla lezione " + idLezione + ".");

            if (CLIUtils.readConfirm("Confermi?")) {
                accademiaService.aggiungiAllievo(idLezione, allievoDaAggiungere);
                CLIUtils.printSuccess("Allievo aggiunto alla lezione.");
            }
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Rimuovi un allievo da una lezione.
     */
    private void rimuoviAllievo() {
        CLIUtils.printSubHeader("Rimuovi Allievo da Lezione");

        try {
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();

            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            List<Utente> allieviLezione = accademiaService.getAllievi(idLezione);
            if (allieviLezione.isEmpty()) {
                CLIUtils.printInfo("Nessun allievo iscritto a questa lezione.");
                CLIUtils.waitForEnter();
                return;
            }

            stampaListaUtenti(allieviLezione, "Allievi iscritti alla lezione");
            System.out.println();

            int idAllievo = CLIUtils.readInt("ID Allievo: ");

            if (CLIUtils.readConfirm("Confermi la rimozione?")) {
                boolean success = accademiaService.rimuoviAllievo(idLezione, idAllievo);
                if (success) {
                    CLIUtils.printSuccess("Allievo rimosso dalla lezione.");
                } else {
                    CLIUtils.printError("Impossibile rimuovere l'allievo.");
                }
            }
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Visualizza gli allievi di una lezione.
     */
    private void visualizzaAllieviLezione() {
        CLIUtils.printSubHeader("Allievi della Lezione");
        try {
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();
            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            List<Utente> allievi = accademiaService.getAllievi(idLezione);

            if (allievi.isEmpty()) {
                CLIUtils.printInfo("Nessun allievo iscritto a questa lezione.");
            } else {
                stampaListaUtenti(allievi, "Allievi della lezione");
                CLIUtils.printInfo("Totale: " + allievi.size() + " allievi");
            }
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Visualizza le lezioni a cui l'utente è iscritto come allievo.
     */
    private void lezioniAllievo() {
        CLIUtils.printSubHeader("Le Mie Lezioni");
        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            List<Lezione> lezioni = accademiaService.getLezioniAllievo(utente);
            stampaListaLezioni(lezioni);
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Visualizza i feedback delle lezioni per l'allievo corrente.
     */
    private void visualizzaFeedback() {
        CLIUtils.printSubHeader("I Miei Feedback");
        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            List<Lezione> lezioni = accademiaService.getLezioniAllievo(utente);
            stampaListaLezioni(lezioni);

            if (lezioni.isEmpty()) {
                CLIUtils.printInfo("Non sei iscritto a nessuna lezione.");
                CLIUtils.waitForEnter();
                return;
            }

            System.out.println();
            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            boolean haFeedback = false;
            System.out.println();

            AllievoLezione dettagli = accademiaService.getAllievoLezione(
                    idLezione, utente.getId());

            if (dettagli != null && dettagli.getFeedback() != null && !dettagli.getFeedback().isEmpty()) {
                haFeedback = true;
                CLIUtils.printSuccess("Feedback per la lezione: ");
                System.out.println(dettagli.getFeedback());

            } else if (!haFeedback) {
                CLIUtils.printInfo("Nessun feedback ricevuto per la lezione.");
            }

        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Segna la presenza di un allievo a una lezione.
     */
    private void segnaPresenza() {
        CLIUtils.printSubHeader("Segna Presenza Allievo");
        try {
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();
            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            List<Utente> allieviLezione = accademiaService.getAllievi(idLezione);
            if (allieviLezione.isEmpty()) {
                CLIUtils.printInfo("Nessun allievo iscritto a questa lezione.");
                CLIUtils.waitForEnter();
                return;
            }

            stampaListaUtenti(allieviLezione, "Allievi iscritti alla lezione");
            System.out.println();

            int idAllievo = CLIUtils.readInt("ID Allievo: ");
            boolean presente = CLIUtils.readConfirm("L'allievo è presente?");

            boolean success = accademiaService.segnaPresenza(idLezione, idAllievo, presente);
            if (success) {
                CLIUtils.printSuccess("Presenza registrata: " + (presente ? "PRESENTE" : "ASSENTE"));
            } else {
                CLIUtils.printError("Impossibile registrare la presenza.");
            }
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Aggiunge feedback per un allievo in una lezione.
     */
    private void aggiungiFeedback() {
        CLIUtils.printSubHeader("Aggiungi Feedback Allievo");
        try {
            stampaListaLezioni(accademiaService.getLezioni());
            System.out.println();
            Integer idLezione = CLIUtils.readIntOptional("ID Lezione (vuoto per annullare): ");
            if (idLezione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            List<Utente> allieviLezione = accademiaService.getAllievi(idLezione);
            if (allieviLezione.isEmpty()) {
                CLIUtils.printInfo("Nessun allievo iscritto a questa lezione.");
                CLIUtils.waitForEnter();
                return;
            }

            stampaListaUtenti(allieviLezione, "Allievi iscritti alla lezione");
            System.out.println();

            int idAllievo = CLIUtils.readInt("ID Allievo: ");
            String feedback = CLIUtils.readString("Feedback: ");

            boolean success = accademiaService.aggiungiFeedback(idLezione, idAllievo, feedback);
            if (success) {
                CLIUtils.printSuccess("Feedback aggiunto.");
            } else {
                CLIUtils.printError("Impossibile aggiungere il feedback.");
            }
        } catch (AccademiaException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Helper per stampare lista lezioni.
     */
    private void stampaListaLezioni(List<Lezione> lezioni) {
        if (lezioni.isEmpty()) {
            CLIUtils.printInfo("Nessuna lezione trovata.");
            return;
        }

        System.out.println();
        CLIUtils.printTableHeader("ID", "Data", "Ora", "Campo", "Maestro");
        for (Lezione l : lezioni) {
            String nomeM = l.getMaestro().getNome() + " "
                    + (l.getMaestro().getCognome() != null ? l.getMaestro().getCognome() : "");
            CLIUtils.printTableRow(
                    String.valueOf(l.getId()),
                    CLIUtils.formatDate(l.getPrenotazione().getData()),
                    CLIUtils.formatTime(l.getPrenotazione().getOraInizio()),
                    l.getPrenotazione().getCampo().getNome(),
                    nomeM);
        }
        CLIUtils.printTableFooter(5);
        CLIUtils.printInfo("Totale: " + lezioni.size() + " lezioni");
    }

    /**
     * Helper per stampare lista utenti (allievi).
     */
    private void stampaListaUtenti(List<Utente> utenti, String titolo) {
        if (utenti.isEmpty()) {
            CLIUtils.printInfo("Nessun utente trovato.");
            return;
        }

        System.out.println("\n" + titolo + ":");
        CLIUtils.printTableHeader("ID", "Nome", "Cognome", "Email");
        for (Utente u : utenti) {
            CLIUtils.printTableRow(
                    String.valueOf(u.getId()),
                    u.getNome(),
                    u.getCognome(),
                    u.getEmail());
        }
        CLIUtils.printTableFooter(4);
    }

}
