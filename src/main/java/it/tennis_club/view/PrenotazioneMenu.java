package it.tennis_club.view;

import it.tennis_club.business_logic.PrenotazioneService;
import it.tennis_club.business_logic.PrenotazioneException;
import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.business_logic.SessionManager;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Menu CLI per la gestione delle prenotazioni.
 */
public class PrenotazioneMenu {

    private final PrenotazioneService prenotazioneService;
    private final CampoService campoService;
    private final SessionManager sessionManager;

    public PrenotazioneMenu() {
        this.prenotazioneService = new PrenotazioneService();
        this.campoService = new CampoService();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Mostra il menu prenotazioni.
     * Le opzioni sono filtrate in base al ruolo dell'utente.
     */
    public void show() {
        boolean running = true;

        while (running) {
            CLIUtils.printHeader("GESTIONE PRENOTAZIONI");

            Utente utenteCorrente = sessionManager.getCurrentUser();
            Utente.Ruolo ruolo = utenteCorrente != null ? utenteCorrente.getRuolo() : null;

            if (ruolo.equals(Utente.Ruolo.ALLIEVO) || ruolo.equals(Utente.Ruolo.SOCIO)) {

                System.out.println("1. Nuova prenotazione");
                System.out.println("2. Le mie prenotazioni");
                System.out.println("3. Verifica disponibilità campo");
                System.out.println("4. Cancella prenotazione");

                System.out.println();
                System.out.println("0. Torna al menu principale");
                System.out.println();

                int scelta = CLIUtils.readInt("Scelta: ");

                switch (scelta) {
                    case 1 -> nuovaPrenotazione();
                    case 2 -> miePrenotazioni();
                    case 3 -> verificaDisponibilita();
                    case 4 -> cancellaPrenotazione();

                    case 0 -> running = false;
                    default -> CLIUtils.printError("Opzione non valida");
                }
            } else {

                System.out.println("1. Prenotazioni per data");
                System.out.println("2. Prenotazioni per campo");
                System.out.println("3. Tutte le prenotazioni");
                System.out.println();
                System.out.println("0. Torna al menu principale");
                System.out.println();

                int scelta = CLIUtils.readInt("Scelta: ");
                switch (scelta) {
                    case 1 -> prenotazioniPerData();
                    case 2 -> prenotazioniPerCampo();
                    case 3 -> tutteLePrenotazioni();

                    case 0 -> running = false;
                    default -> CLIUtils.printError("Opzione non valida");
                }
            }
        }
    }

    /**
     * Crea una nuova prenotazione.
     */
    private void nuovaPrenotazione() {
        CLIUtils.printSubHeader("Nuova Prenotazione");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login per prenotare.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            // Mostra campi disponibili
            List<Campo> campi = campoService.getCampi();
            stampaListaCampi(campi);

            Integer idCampo = CLIUtils.readIntOptional("ID Campo (vuoto per annullare): ");
            if (idCampo == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }
            Campo campo = campoService.getCampoPerId(idCampo);

            LocalDate data = CLIUtils.readDate("Data prenotazione");
            LocalTime ora = CLIUtils.readTime("Ora inizio");

            // Verifica disponibilità prima di prenotare
            if (!prenotazioneService.isCampoDisponibile(data, ora, campo)) {
                CLIUtils.printError("Il campo non è disponibile in quella data/ora.");
                CLIUtils.waitForEnter();
                return;
            }

            Integer idPrenotazione = prenotazioneService.creaPrenotazione(data, ora, campo, utente);
            CLIUtils.printSuccess("Prenotazione creata con ID: " + idPrenotazione);

        } catch (CampoException | PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Mostra le prenotazioni dell'utente corrente.
     */
    private void miePrenotazioni() {
        CLIUtils.printSubHeader("Le Mie Prenotazioni");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerSocio(utente);
            stampaListaPrenotazioni(prenotazioni);
        } catch (PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Mostra prenotazioni per una data specifica.
     */
    private void prenotazioniPerData() {
        CLIUtils.printSubHeader("Prenotazioni per Data");

        LocalDate data = CLIUtils.readDateOptional("Data");
        if (data == null) {
            CLIUtils.printWarning("Operazione annullata.");
            return;
        }

        try {
            List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerData(data);
            stampaListaPrenotazioni(prenotazioni);
        } catch (PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Mostra prenotazioni per un campo specifico.
     */
    private void prenotazioniPerCampo() {
        CLIUtils.printSubHeader("Prenotazioni per Campo");

        try {
            List<Campo> campi = campoService.getCampi();
            stampaListaCampi(campi);
            Integer idCampo = CLIUtils.readIntOptional("ID Campo (vuoto per annullare): ");
            if (idCampo == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }
            Campo campo = campoService.getCampoPerId(idCampo);

            List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniPerCampo(campo);
            stampaListaPrenotazioni(prenotazioni);
        } catch (CampoException | PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Verifica la disponibilità di un campo.
     */
    private void verificaDisponibilita() {
        CLIUtils.printSubHeader("Verifica Disponibilità");

        try {
            List<Campo> campi = campoService.getCampi();
            stampaListaCampi(campi);
            Integer idCampo = CLIUtils.readIntOptional("ID Campo (vuoto per annullare): ");
            if (idCampo == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }
            Campo campo = campoService.getCampoPerId(idCampo);

            LocalDate data = CLIUtils.readDate("Data");
            LocalTime ora = CLIUtils.readTime("Ora");

            boolean disponibile = prenotazioneService.isCampoDisponibile(data, ora, campo);
            if (disponibile) {
                CLIUtils.printSuccess("Il campo " + campo.getNome() + " è DISPONIBILE il " +
                        CLIUtils.formatDate(data) + " alle " + CLIUtils.formatTime(ora));
            } else {
                CLIUtils.printWarning(
                        "Il campo " + campo.getNome() + " NON è disponibile in quella data per quell'ora.");
            }
        } catch (CampoException | PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Cancella una prenotazione.
     */
    private void cancellaPrenotazione() {
        CLIUtils.printSubHeader("Cancella Prenotazione");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            // Mostra le prenotazioni dell'utente
            List<Prenotazione> miePrenotazioni = prenotazioneService.getPrenotazioniPerSocio(utente);
            if (miePrenotazioni.isEmpty()) {
                CLIUtils.printInfo("Non hai prenotazioni da cancellare.");
                CLIUtils.waitForEnter();
                return;
            }

            stampaListaPrenotazioni(miePrenotazioni);

            Integer idPrenotazione = CLIUtils.readIntOptional("ID Prenotazione da cancellare (vuoto per annullare): ");
            if (idPrenotazione == null) {
                CLIUtils.printWarning("Operazione annullata.");
                return;
            }

            if (CLIUtils.readConfirm("Confermi la cancellazione?")) {
                boolean success = prenotazioneService.cancellaPrenotazione(idPrenotazione);
                if (success) {
                    CLIUtils.printSuccess("Prenotazione cancellata.");
                } else {
                    CLIUtils.printError("Impossibile cancellare la prenotazione.");
                }
            }
        } catch (PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Mostra tutte le prenotazioni.
     */
    private void tutteLePrenotazioni() {
        CLIUtils.printSubHeader("Tutte le Prenotazioni");

        try {
            List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioni();
            stampaListaPrenotazioni(prenotazioni);
        } catch (PrenotazioneException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Helper per stampare una lista di campi.
     */
    private void stampaListaCampi(List<Campo> campi) {
        if (campi.isEmpty()) {
            CLIUtils.printInfo("Nessun campo trovato.");
            return;
        }

        System.out.println();
        CLIUtils.printTableHeader("ID", "Nome", "Superficie", "Tipologia");
        for (Campo c : campi) {
            CLIUtils.printTableRow(
                    String.valueOf(c.getId()),
                    c.getNome(),
                    c.getTipoSuperficie(),
                    c.getIsCoperto() ? "Coperto" : "Scoperto");
        }
        CLIUtils.printTableFooter(4);
        CLIUtils.printInfo("Totale: " + campi.size() + " campi");
    }

    /**
     * Helper per stampare una lista di prenotazioni.
     */
    private void stampaListaPrenotazioni(List<Prenotazione> prenotazioni) {
        if (prenotazioni.isEmpty()) {
            CLIUtils.printInfo("Nessuna prenotazione trovata.");
            return;
        }

        System.out.println();
        CLIUtils.printTableHeader("ID", "Data", "Ora", "Campo", "Socio");
        for (Prenotazione p : prenotazioni) {
            CLIUtils.printTableRow(
                    String.valueOf(p.getId()),
                    CLIUtils.formatDate(p.getData()),
                    CLIUtils.formatTime(p.getOraInizio()),
                    p.getCampo().getNome(),
                    p.getSocio().getNome());
        }
        CLIUtils.printTableFooter(5);
        CLIUtils.printInfo("Totale: " + prenotazioni.size() + " prenotazioni");
    }
}
