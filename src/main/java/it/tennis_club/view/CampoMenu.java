package it.tennis_club.view;

import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.business_logic.SessionManager;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Utente;

import java.time.LocalDate;
import java.util.List;

/**
 * Menu CLI per la gestione dei campi e delle manutenzioni.
 */
public class CampoMenu {

    private final CampoService campoService;
    private final SessionManager sessionManager;

    public CampoMenu() {
        this.campoService = new CampoService();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Mostra il menu campi.
     */
    public void show() {
        boolean running = true;

        while (running) {
            CLIUtils.printHeader("GESTIONE CAMPI");

            System.out.println("1. Visualizza tutti i campi");
            System.out.println("2. Cerca campo per ID");
            System.out.println("3. Campi coperti");
            System.out.println("4. Campi per tipo superficie");
            System.out.println("───────────────────────────────");
            System.out.println("5. Crea manutenzione (Admin/Manutentore)");
            System.out.println("6. Completa manutenzione (Admin/Manutentore)");
            System.out.println("7. Annulla manutenzione (Admin/Manutentore)");
            System.out.println("8. Visualizza manutenzioni campo");
            System.out.println("0. Torna al menu principale");
            System.out.println();

            int scelta = CLIUtils.readInt("Scelta: ");

            switch (scelta) {
                case 1 -> visualizzaTuttiCampi();
                case 2 -> cercaCampoPerID();
                case 3 -> campiCoperti();
                case 4 -> campiPerTipoSuperficie();
                case 5 -> creaManutenzione();
                case 6 -> completaManutenzione();
                case 7 -> annullaManutenzione();
                case 8 -> visualizzaManutenzioni();
                case 0 -> running = false;
                default -> CLIUtils.printError("Opzione non valida");
            }
        }
    }

    /**
     * Visualizza tutti i campi.
     */
    private void visualizzaTuttiCampi() {
        CLIUtils.printSubHeader("Tutti i Campi");

        try {
            List<Campo> campi = campoService.getAllCampi();
            stampaListaCampi(campi);
        } catch (CampoException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Cerca un campo per ID.
     */
    private void cercaCampoPerID() {
        CLIUtils.printSubHeader("Cerca Campo per ID");

        int id = CLIUtils.readInt("ID Campo: ");

        try {
            Campo campo = campoService.getCampoById(id);
            System.out.println();
            System.out.println("  ID:         " + campo.getId());
            System.out.println("  Nome:       " + campo.getNome());
            System.out.println("  Superficie: " + campo.getTipoSuperficie());
            System.out.println("  Coperto:    " + (campo.getIsCoperto() ? "Sì" : "No"));
        } catch (CampoException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Mostra solo i campi coperti.
     */
    private void campiCoperti() {
        CLIUtils.printSubHeader("Campi Coperti");

        try {
            List<Campo> campi = campoService.getCampiCoperti();
            stampaListaCampi(campi);
        } catch (CampoException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Filtra campi per tipo superficie.
     */
    private void campiPerTipoSuperficie() {
        CLIUtils.printSubHeader("Campi per Tipo Superficie");

        System.out.println("Tipi disponibili: Terra, Cemento, Erba, Sintetico");
        String tipo = CLIUtils.readString("Tipo superficie: ");

        try {
            List<Campo> campi = campoService.getCampiByTipoSuperficie(tipo);
            stampaListaCampi(campi);
        } catch (CampoException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Crea una nuova manutenzione.
     */
    private void creaManutenzione() {
        CLIUtils.printSubHeader("Crea Manutenzione");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            // Mostra campi
            List<Campo> campi = campoService.getAllCampi();
            stampaListaCampi(campi);

            int idCampo = CLIUtils.readInt("ID Campo: ");
            LocalDate dataInizio = CLIUtils.readDate("Data inizio manutenzione");
            String descrizione = CLIUtils.readString("Descrizione: ");

            Integer idManutenzione = campoService.creaManutenzione(utente, idCampo, dataInizio, descrizione);
            CLIUtils.printSuccess("Manutenzione creata con ID: " + idManutenzione);

        } catch (CampoException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Completa una manutenzione.
     */
    private void completaManutenzione() {
        CLIUtils.printSubHeader("Completa Manutenzione");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        int idManutenzione = CLIUtils.readInt("ID Manutenzione: ");
        LocalDate dataFine = CLIUtils.readDate("Data fine");

        try {
            campoService.completaManutenzione(utente, idManutenzione, dataFine);
            CLIUtils.printSuccess("Manutenzione completata.");
        } catch (CampoException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Annulla una manutenzione.
     */
    private void annullaManutenzione() {
        CLIUtils.printSubHeader("Annulla Manutenzione");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        int idManutenzione = CLIUtils.readInt("ID Manutenzione: ");

        if (CLIUtils.readConfirm("Confermi l'annullamento?")) {
            try {
                campoService.annullaManutenzione(utente, idManutenzione);
                CLIUtils.printSuccess("Manutenzione annullata.");
            } catch (CampoException e) {
                CLIUtils.printError(e.getMessage());
            }
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Visualizza manutenzioni di un campo.
     */
    private void visualizzaManutenzioni() {
        CLIUtils.printSubHeader("Manutenzioni Campo");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printError("Devi effettuare il login.");
            CLIUtils.waitForEnter();
            return;
        }

        try {
            List<Campo> campi = campoService.getAllCampi();
            stampaListaCampi(campi);

            int idCampo = CLIUtils.readInt("ID Campo: ");
            List<Manutenzione> manutenzioni = campoService.getManutenzioniCampo(utente, idCampo);

            if (manutenzioni.isEmpty()) {
                CLIUtils.printInfo("Nessuna manutenzione trovata per questo campo.");
            } else {
                System.out.println();
                CLIUtils.printTableHeader("ID", "Data Inizio", "Data Fine", "Stato", "Descrizione");
                for (Manutenzione m : manutenzioni) {
                    CLIUtils.printTableRow(
                            String.valueOf(m.getId()),
                            CLIUtils.formatDate(m.getDataInizio()),
                            CLIUtils.formatDate(m.getDataFine()),
                            m.getStato().toString(),
                            truncate(m.getDescrizione(), 15));
                }
                CLIUtils.printTableFooter(5);
            }
        } catch (CampoException e) {
            CLIUtils.printError(e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Helper per stampare lista campi.
     */
    private void stampaListaCampi(List<Campo> campi) {
        if (campi.isEmpty()) {
            CLIUtils.printInfo("Nessun campo trovato.");
            return;
        }

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
    }

    /**
     * Tronca una stringa se troppo lunga.
     */
    private String truncate(String s, int maxLen) {
        if (s == null)
            return "-";
        return s.length() > maxLen ? s.substring(0, maxLen - 2) + ".." : s;
    }
}
