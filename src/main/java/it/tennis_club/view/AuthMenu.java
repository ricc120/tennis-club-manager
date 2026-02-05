package it.tennis_club.view;

import it.tennis_club.business_logic.AuthService;
import it.tennis_club.business_logic.AuthenticationException;
import it.tennis_club.business_logic.SessionManager;
import it.tennis_club.domain_model.Utente;

/**
 * Menu CLI per la gestione dell'autenticazione.
 * Gestisce login, logout, registrazione e visualizzazione utente corrente.
 */
public class AuthMenu {

    private final AuthService authService;
    private final SessionManager sessionManager;

    public AuthMenu() {
        this.authService = new AuthService();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Mostra il menu di autenticazione (versione legacy, non più usata dal menu
     * principale).
     */
    public void show() {
        boolean running = true;

        while (running) {
            CLIUtils.printHeader("AUTENTICAZIONE");

            Utente utenteCorrente = sessionManager.getCurrentUser();
            if (utenteCorrente != null) {
                CLIUtils.printInfo("Utente connesso: " + utenteCorrente.getNome() + " " +
                        utenteCorrente.getCognome() + " (" + utenteCorrente.getRuolo() + ")");
            } else {
                CLIUtils.printWarning("Nessun utente connesso");
            }

            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("3. Logout");
            System.out.println("4. Visualizza utente corrente");
            System.out.println();
            System.out.println("0. Torna al menu principale");
            System.out.println();

            int scelta = CLIUtils.readInt("Scelta: ");

            switch (scelta) {
                case 1 -> loginSingolo();
                case 2 -> registrazionePubblica();
                case 3 -> logout();
                case 4 -> visualizzaUtenteCorrente();
                case 0 -> running = false;
                default -> CLIUtils.printError("Opzione non valida");
            }
        }
    }

    /**
     * Effettua il login di un utente.
     * Metodo pubblico chiamato dalla schermata di autenticazione obbligatoria.
     */
    public void loginSingolo() {
        CLIUtils.printSubHeader("Login");

        if (sessionManager.getCurrentUser() != null) {
            CLIUtils.printWarning("Sei già autenticato. Effettua prima il logout.");
            CLIUtils.waitForEnter();
            return;
        }

        String email = CLIUtils.readStringOptional("Email (vuoto per annullare): ");
        if (email == null)
            return;

        String password = CLIUtils.readString("Password: ");

        try {
            Utente utente = authService.login(email, password);
            System.out.println();
            if (utente != null) {
                CLIUtils.printSuccess("Login effettuato con successo!");
                CLIUtils.printInfo("Benvenuto, " + utente.getNome() + " " + utente.getCognome());
                CLIUtils.printInfo("Ruolo: " + utente.getRuolo());
            } else {
                CLIUtils.printError("Credenziali non valide.");
            }
        } catch (AuthenticationException e) {
            CLIUtils.printError("Errore di autenticazione: " + e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Registrazione pubblica: assegna automaticamente il ruolo SOCIO.
     * Chiamata dalla schermata di autenticazione obbligatoria.
     */
    public void registrazionePubblica() {
        CLIUtils.printSubHeader("Registrazione Nuovo Socio");

        if (sessionManager.getCurrentUser() != null) {
            CLIUtils.printWarning("Sei già autenticato. Effettua prima il logout per registrare un nuovo utente.");
            CLIUtils.waitForEnter();
            return;
        }

        CLIUtils.printInfo("Sarai registrato come SOCIO del Tennis Club.");
        System.out.println("Inserisci i tuoi dati per la registrazione:");
        System.out.println();

        String nome = CLIUtils.readTextStringOptional("Nome (vuoto per annullare): ");
        if (nome == null)
            return;

        String cognome = CLIUtils.readTextString("Cognome: ");
        String email = CLIUtils.readString("Email: ");
        String password = CLIUtils.readString("Password: ");

        // Ruolo assegnato automaticamente
        Utente.Ruolo ruolo = Utente.Ruolo.SOCIO;

        // Riepilogo e conferma
        System.out.println();
        CLIUtils.printSubHeader("Riepilogo");
        System.out.println("  Nome:    " + nome + " " + cognome);
        System.out.println("  Email:   " + email);
        System.out.println("  Ruolo:   " + ruolo + " (assegnato automaticamente)");
        System.out.println();

        if (!CLIUtils.readConfirm("Confermi la registrazione?")) {
            CLIUtils.printWarning("Registrazione annullata.");
            CLIUtils.waitForEnter();
            return;
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setEmail(email);
        nuovoUtente.setPassword(password);
        nuovoUtente.setRuolo(ruolo);

        try {
            Integer idGenerato = authService.registrazione(nuovoUtente);
            CLIUtils.printSuccess("Registrazione completata con successo!");
            CLIUtils.printInfo("ID utente: " + idGenerato);
            CLIUtils.printInfo("Benvenuto, " + nome + "! Sei ora autenticato.");
        } catch (AuthenticationException e) {
            CLIUtils.printError("Errore durante la registrazione: " + e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Registrazione con ruolo specifico: solo per Admin.
     * Permette di creare utenti con ruoli diversi da SOCIO.
     * 
     * @return l'ID del nuovo utente, o null se la registrazione fallisce
     */
    public Integer registrazioneAdmin() {

        System.out.println("Inserisci i dati del nuovo utente:");
        System.out.println();

        String nome = CLIUtils.readTextStringOptional("Nome (vuoto per annullare): ");
        if (nome == null) {
            return null;
        }

        String cognome = CLIUtils.readTextString("Cognome: ");
        String email = CLIUtils.readString("Email: ");
        String password = CLIUtils.readString("Password: ");

        // Validazione email e password (nome e cognome sono già validati da
        // readTextString)

        if (email == null || email.trim().isEmpty()) {
            CLIUtils.printError("L'email è obbligatoria.");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            CLIUtils.printError("La password è obbligatoria.");
            return null;
        }

        // Selezione ruolo
        System.out.println();
        System.out.println("Seleziona il ruolo:");
        System.out.println("1. Socio");
        System.out.println("2. Allievo");
        System.out.println("3. Maestro");
        System.out.println("4. Manutentore");
        System.out.println("5. Admin");

        int sceltaRuolo = CLIUtils.readInt("Ruolo: ");

        Utente.Ruolo ruolo;
        switch (sceltaRuolo) {
            case 1 -> ruolo = Utente.Ruolo.SOCIO;
            case 2 -> ruolo = Utente.Ruolo.ALLIEVO;
            case 3 -> ruolo = Utente.Ruolo.MAESTRO;
            case 4 -> ruolo = Utente.Ruolo.MANUTENTORE;
            case 5 -> ruolo = Utente.Ruolo.ADMIN;
            default -> {
                CLIUtils.printError("Ruolo non valido. Operazione annullata.");
                return null;
            }
        }

        // Riepilogo e conferma
        System.out.println();
        CLIUtils.printSubHeader("Riepilogo");
        System.out.println("  Nome:    " + nome + " " + cognome);
        System.out.println("  Email:   " + email);
        System.out.println("  Ruolo:   " + ruolo);
        System.out.println();

        if (!CLIUtils.readConfirm("Confermi la creazione dell'utente?")) {
            CLIUtils.printWarning("Creazione annullata.");
            return null;
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setEmail(email);
        nuovoUtente.setPassword(password);
        nuovoUtente.setRuolo(ruolo);

        try {
            // Usa registrazioneSenzaSessione per non cambiare la sessione dell'admin
            Integer idGenerato = authService.registrazioneSenzaSessione(nuovoUtente);
            CLIUtils.printSuccess("Utente creato con successo!");
            CLIUtils.printInfo("ID utente: " + idGenerato);
            return idGenerato;
        } catch (AuthenticationException e) {
            CLIUtils.printError("Errore durante la creazione: " + e.getMessage());
            return null;
        }
    }

    /**
     * Effettua il logout dell'utente corrente.
     * Metodo pubblico chiamato dal menu principale.
     */
    public void logout() {
        CLIUtils.printSubHeader("Logout");

        Utente utenteCorrente = sessionManager.getCurrentUser();
        if (utenteCorrente == null) {
            CLIUtils.printWarning("Nessun utente connesso.");
            CLIUtils.waitForEnter();
            return;
        }

        if (CLIUtils.readConfirm("Confermi il logout?")) {
            boolean success = authService.logout(utenteCorrente);
            if (success) {
                CLIUtils.printSuccess("Logout effettuato con successo.");
            } else {
                CLIUtils.printError("Errore durante il logout.");
            }
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Visualizza le informazioni dell'utente corrente.
     */
    private void visualizzaUtenteCorrente() {
        CLIUtils.printSubHeader("Utente Corrente");

        Utente utente = sessionManager.getCurrentUser();
        if (utente == null) {
            CLIUtils.printWarning("Nessun utente connesso.");
        } else {
            System.out.println();
            System.out.println("  ID:      " + utente.getId());
            System.out.println("  Nome:    " + utente.getNome() + " " + utente.getCognome());
            System.out.println("  Email:   " + utente.getEmail());
            System.out.println("  Ruolo:   " + utente.getRuolo());
            System.out.println();

            if (sessionManager.isUserLoggedIn()) {
                CLIUtils.printInfo("Sessione attiva");
            }
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Restituisce l'utente attualmente loggato.
     */
    public Utente getUtenteCorrente() {
        return sessionManager.getCurrentUser();
    }

    /**
     * Verifica se c'è un utente loggato.
     */
    public boolean isLoggedIn() {
        return sessionManager.getCurrentUser() != null;
    }
}
