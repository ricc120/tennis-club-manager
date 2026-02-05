package it.tennis_club.view;

import it.tennis_club.business_logic.SessionManager;
import it.tennis_club.domain_model.Utente;

/**
 * Interfaccia CLI principale per il Tennis Club Manager.
 * Gestisce la navigazione tra i vari menu dell'applicazione.
 * Richiede autenticazione obbligatoria prima di accedere alle funzionalità.
 */
public class TennisClubCLI {

    private final AuthMenu authMenu;
    private final PrenotazioneMenu prenotazioneMenu;
    private final CampoMenu campoMenu;
    private final AccademiaMenu accademiaMenu;
    private AdminMenu adminMenu;
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
     * Richiede autenticazione obbligatoria prima di mostrare il menu principale.
     */
    public void start() {
        mostraBenvenuto();

        // Richiede autenticazione obbligatoria
        boolean authenticated = richiestaAutenticazione();
        if (!authenticated) {
            mostraArrivederci();
            CLIUtils.closeScanner();
            return;
        }

        // Inizializza AdminMenu solo dopo aver verificato che esiste l'utente
        this.adminMenu = new AdminMenu();

        // Loop del menu principale
        boolean running = true;
        while (running) {
            // Se l'utente si disconnette, richiedi nuova autenticazione
            if (!sessionManager.isUserLoggedIn()) {
                CLIUtils.printWarning("Sessione terminata. Effettua nuovamente il login.");
                authenticated = richiestaAutenticazione();
                if (!authenticated) {
                    running = false;
                    continue;
                }
            }
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
                .println(CLIUtils.CYAN + "║" + CLIUtils.BOLD + "         TENNIS CLUB MANAGER                          "
                        + CLIUtils.RESET
                        + CLIUtils.CYAN + "║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "║                                                      ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "║   Sistema di gestione prenotazioni campi             ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "║   e accademia tennis                                 ║" + CLIUtils.RESET);
        System.out.println(CLIUtils.CYAN + "╚══════════════════════════════════════════════════════╝" + CLIUtils.RESET);
        System.out.println();
    }

    /**
     * Schermata di autenticazione obbligatoria.
     * L'utente deve effettuare login o registrarsi prima di accedere all'app.
     * 
     * @return true se l'utente si è autenticato, false se ha scelto di uscire
     */
    private boolean richiestaAutenticazione() {
        while (!sessionManager.isUserLoggedIn()) {
            CLIUtils.printHeader("ACCESSO RICHIESTO");
            CLIUtils.printWarning("Devi autenticarti per accedere al sistema.");
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("───────────────────────────────────");
            System.out.println("0. Esci");
            System.out.println();

            int scelta = CLIUtils.readInt("Seleziona un'opzione: ");

            switch (scelta) {
                case 1 -> authMenu.loginSingolo();
                case 2 -> authMenu.registrazionePubblica();
                case 0 -> {
                    if (CLIUtils.readConfirm("Vuoi davvero uscire?")) {
                        return false;
                    }
                }
                default -> CLIUtils.printError("Opzione non valida");
            }
        }
        return true;
    }

    /**
     * Mostra il menu principale e gestisce la navigazione.
     * Le opzioni sono filtrate in base al ruolo dell'utente.
     * 
     * @return true per continuare, false per uscire
     */
    private boolean mostraMenuPrincipale() {
        CLIUtils.printHeader("MENU PRINCIPALE");

        Utente utenteCorrente = sessionManager.getCurrentUser();
        Utente.Ruolo ruolo = utenteCorrente.getRuolo();

        CLIUtils.printInfo("Utente: " + utenteCorrente.getNome() + " " +
                utenteCorrente.getCognome() + " [" + ruolo + "]");

        System.out.println();

        // Costruisci menu dinamico in base al ruolo
        int opzioneCorrente = 1;

        // Opzione Prenotazioni (SOCIO, ALLIEVO, MAESTRO, MANUTENTORE, ADMIN)
        boolean puoPrenotare = ruolo == Utente.Ruolo.SOCIO ||
                ruolo == Utente.Ruolo.ALLIEVO ||
                ruolo == Utente.Ruolo.MAESTRO ||
                ruolo == Utente.Ruolo.MANUTENTORE ||
                ruolo == Utente.Ruolo.ADMIN;
        int opzionePrenotazioni = puoPrenotare ? opzioneCorrente++ : -1;
        if (puoPrenotare) {
            System.out.println(opzionePrenotazioni + ". Prenotazioni");
        }

        // Opzione Campi (tutti)
        int opzioneCampi = opzioneCorrente++;
        System.out.println(opzioneCampi + ". Campi");

        // Opzione Accademia (ALLIEVO, MAESTRO, ADMIN)
        boolean puoAccademia = ruolo == Utente.Ruolo.ALLIEVO ||
                ruolo == Utente.Ruolo.MAESTRO ||
                ruolo == Utente.Ruolo.ADMIN;
        int opzioneAccademia = puoAccademia ? opzioneCorrente++ : -1;
        if (puoAccademia) {
            System.out.println(opzioneAccademia + ". Accademia");
        }

        // Opzione Gestione Utenti (solo ADMIN)
        int opzioneAdmin = ruolo == Utente.Ruolo.ADMIN ? opzioneCorrente++ : -1;
        if (ruolo == Utente.Ruolo.ADMIN) {
            System.out.println(opzioneAdmin + ". Gestione Utenti");
        }

        // Opzione Logout
        int opzioneLogout = opzioneCorrente++;
        System.out.println(opzioneLogout + ". Logout");

        System.out.println("───────────────────────────────────");

        System.out.println("0. Esci");
        System.out.println();

        int scelta = CLIUtils.readInt("Seleziona un'opzione: ");

        // Gestione navigazione
        if (scelta == 0) {
            if (CLIUtils.readConfirm("Vuoi davvero uscire?")) {
                return false;
            }
        } else if (scelta == opzionePrenotazioni && puoPrenotare) {
            prenotazioneMenu.show();
        } else if (scelta == opzioneCampi) {
            campoMenu.show();
        } else if (scelta == opzioneAccademia && puoAccademia) {
            accademiaMenu.show();
        } else if (scelta == opzioneAdmin && ruolo == Utente.Ruolo.ADMIN) {
            adminMenu.show();
        } else if (scelta == opzioneLogout) {
            authMenu.logout();
        } else {
            CLIUtils.printError("Opzione non valida");
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
        System.out.println(CLIUtils.GREEN + "║     Tennis Club Manager!                 ║" + CLIUtils.RESET);
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
