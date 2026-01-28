package it.tennis_club.view;

import it.tennis_club.business_logic.SessionManager;
import it.tennis_club.domain_model.Utente;

/**
 * Interfaccia CLI principale per il Tennis Club Manager.
 * Gestisce la navigazione tra i vari menu dell'applicazione.
 */
public class TennisClubCLI {

    private final AuthMenu authMenu;
    private final PrenotazioneMenu prenotazioneMenu;
    private final CampoMenu campoMenu;
    private final AccademiaMenu accademiaMenu;
    private final SessionManager sessionManager;

    public TennisClubCLI() {
        this.authMenu = new AuthMenu();
        this.prenotazioneMenu = new PrenotazioneMenu();
        this.campoMenu = new CampoMenu();
        this.accademiaMenu = new AccademiaMenu();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Avvia l'applicazione CLI.
     */
    public void start() {
        mostraBenvenuto();

        boolean running = true;
        while (running) {
            running = mostraMenuPrincipale();
        }

        mostraArrivederci();
        CLIUtils.closeScanner();
    }

    /**
     * Mostra il messaggio di benvenuto.
     */
    private void mostraBenvenuto() {
        CLIUtils.clearScreen();
        System.out.println();
        System.out.println(CLIUtils.CYAN + "╔══════════════════════════════════════════════════════╗" + CLIUtils.RESET);
        System.out
                .println(CLIUtils.CYAN + "║" + CLIUtils.BOLD + "        🎾 TENNIS CLUB MANAGER 🎾                     "
                        + CLIUtils.RESET + CLIUtils.CYAN + "║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "║                                                      ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "║   Sistema di gestione prenotazioni campi             ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "║   e accademia tennis                                 ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "╚══════════════════════════════════════════════════════╝" + CLIUtils.RESET);
        System.out.println();
    }

    /**
     * Mostra il menu principale e gestisce la navigazione.
     * 
     * @return true per continuare, false per uscire
     */
    private boolean mostraMenuPrincipale() {
        CLIUtils.printHeader("MENU PRINCIPALE");

        // Mostra stato utente
        Utente utenteCorrente = sessionManager.getCurrentUser();
        if (utenteCorrente != null) {
            CLIUtils.printInfo("Utente: " + utenteCorrente.getNome() + " " +
                    utenteCorrente.getCognome() + " [" + utenteCorrente.getRuolo() + "]");
        } else {
            CLIUtils.printWarning("Non autenticato - alcune funzioni potrebbero non essere disponibili");
        }

        System.out.println();
        System.out.println("1. 🔐 Autenticazione (Login/Logout)");
        System.out.println("2. 📅 Prenotazioni");
        System.out.println("3. 🏟️  Campi");
        System.out.println("4. 🎓 Accademia (Lezioni)");
        System.out.println("───────────────────────────────────");
        System.out.println("0. 🚪 Esci");
        System.out.println();

        int scelta = CLIUtils.readInt("Seleziona un'opzione: ");

        switch (scelta) {
            case 1 -> authMenu.show();
            case 2 -> prenotazioneMenu.show();
            case 3 -> campoMenu.show();
            case 4 -> accademiaMenu.show();
            case 0 -> {
                if (CLIUtils.readConfirm("Vuoi davvero uscire?")) {
                    return false;
                }
            }
            default -> CLIUtils.printError("Opzione non valida");
        }

        return true;
    }

    /**
     * Mostra il messaggio di arrivederci.
     */
    private void mostraArrivederci() {
        System.out.println();
        System.out.println(CLIUtils.GREEN + "╔══════════════════════════════════════════╗" + CLIUtils.RESET);
        System.out.println(CLIUtils.GREEN + "║     Grazie per aver usato                ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.GREEN + "║     Tennis Club Manager! 🎾             ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.GREEN + "║                                          ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.GREEN + "║     Arrivederci!                         ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.GREEN + "╚══════════════════════════════════════════╝" + CLIUtils.RESET);
        System.out.println();
    }

    /**
     * Entry point per avviare la CLI.
     */
    public static void main(String[] args) {
        TennisClubCLI cli = new TennisClubCLI();
        cli.start();
    }
}
