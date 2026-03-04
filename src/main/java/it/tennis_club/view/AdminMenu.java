package it.tennis_club.view;

import it.tennis_club.business_logic.SessionManager;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.UtenteDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Menu CLI per la gestione degli utenti (solo Admin).
 * Permette di visualizzare, modificare ruoli e creare nuovi utenti.
 */
public class AdminMenu {

    private final UtenteDAO utenteDAO;
    private final AuthMenu authMenu;
    private final SessionManager sessionManager;

    public AdminMenu() {
        this.utenteDAO = new UtenteDAO();
        this.authMenu = new AuthMenu();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Mostra il menu di gestione utenti.
     */
    public void show() {
        // Verifica che l'utente sia Admin
        Utente utenteCorrente = sessionManager.getCurrentUser();
        if (utenteCorrente == null || utenteCorrente.getRuolo() != Utente.Ruolo.ADMIN) {
            CLIUtils.printError("Accesso negato. Solo gli amministratori possono accedere a questa sezione.");
            CLIUtils.waitForEnter();
            return;
        }

        boolean running = true;

        while (running) {
            CLIUtils.printHeader("GESTIONE UTENTI");

            System.out.println("1. Lista tutti gli utenti");
            System.out.println("2. Modifica ruolo utente");
            System.out.println("3. Crea nuovo utente");
            System.out.println("4. Cancella utente");
            System.out.println();
            System.out.println("0. Torna al menu principale");
            System.out.println();

            int scelta = CLIUtils.readInt("Scelta: ");

            switch (scelta) {
                case 1 -> listaUtenti();
                case 2 -> modificaRuolo();
                case 3 -> creaNuovoUtente();
                case 4 -> cancellaUtente();
                case 0 -> running = false;
                default -> CLIUtils.printError("Opzione non valida");
            }
        }
    }

    /**
     * Mostra la lista di tutti gli utenti del sistema.
     */
    private void listaUtenti() {
        CLIUtils.printSubHeader("Lista Utenti");

        try {
            List<Utente> utenti = utenteDAO.getAllUtenti();
            stampaListaUtenti(utenti);
        } catch (SQLException e) {
            CLIUtils.printError("Errore durante il recupero degli utenti: " + e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Modifica il ruolo di un utente esistente.
     */
    private void modificaRuolo() {
        CLIUtils.printSubHeader("Modifica Ruolo Utente");

        // Prima mostra la lista degli utenti
        try {
            List<Utente> utenti = utenteDAO.getAllUtenti();
            stampaListaUtenti(utenti);
        } catch (SQLException e) {
            CLIUtils.printError("Errore durante il recupero degli utenti: " + e.getMessage());
            CLIUtils.waitForEnter();
            return;
        }

        // Chiedi quale utente modificare
        Integer idUtente = CLIUtils.readIntOptional("ID utente da modificare (vuoto per annullare): ");
        if (idUtente == null) {
            CLIUtils.printWarning("Operazione annullata.");
            CLIUtils.waitForEnter();
            return;
        }

        // Verifica che non stia modificando se stesso
        Utente utenteCorrente = sessionManager.getCurrentUser();
        if (idUtente.equals(utenteCorrente.getId())) {
            CLIUtils.printError("Non puoi modificare il tuo stesso ruolo.");
            CLIUtils.waitForEnter();
            return;
        }

        // Verifica che l'utente esista
        try {
            Utente utenteDaModificare = utenteDAO.getUtenteById(idUtente);
            if (utenteDaModificare == null) {
                CLIUtils.printError("Utente con ID " + idUtente + " non trovato.");
                CLIUtils.waitForEnter();
                return;
            }

            CLIUtils.printInfo("Utente selezionato: " + utenteDaModificare.getNome() + " " +
                    utenteDaModificare.getCognome() + " [" + utenteDaModificare.getRuolo() + "]");

            // Selezione nuovo ruolo
            System.out.println();
            System.out.println("Seleziona il nuovo ruolo (vuoto per annullare): ");
            System.out.println("1. Socio");
            System.out.println("2. Allievo");
            System.out.println("3. Maestro");
            System.out.println("4. Manutentore");
            System.out.println("5. Admin");

            Integer sceltaRuolo = CLIUtils.readIntOptional("Nuovo ruolo: ");
            if (sceltaRuolo == null) {
                CLIUtils.printWarning("Operazione annullata.");
                CLIUtils.waitForEnter();
                return;
            }

            Utente.Ruolo nuovoRuolo;
            switch (sceltaRuolo) {
                case 1 -> nuovoRuolo = Utente.Ruolo.SOCIO;
                case 2 -> nuovoRuolo = Utente.Ruolo.ALLIEVO;
                case 3 -> nuovoRuolo = Utente.Ruolo.MAESTRO;
                case 4 -> nuovoRuolo = Utente.Ruolo.MANUTENTORE;
                case 5 -> nuovoRuolo = Utente.Ruolo.ADMIN;
                case 0 -> {
                    CLIUtils.printWarning("Operazione annullata.");
                    CLIUtils.waitForEnter();
                    return;
                }
                default -> {
                    CLIUtils.printError("Ruolo non valido.");
                    CLIUtils.waitForEnter();
                    return;
                }
            }

            // Conferma
            if (!CLIUtils.readConfirm("Confermi il cambio da " + utenteDaModificare.getRuolo() +
                    " a " + nuovoRuolo + "?")) {
                CLIUtils.printWarning("Operazione annullata.");
                CLIUtils.waitForEnter();
                return;
            }

            // Esegui aggiornamento
            boolean success = utenteDAO.updateRuolo(idUtente, nuovoRuolo);
            if (success) {
                CLIUtils.printSuccess("Ruolo aggiornato con successo!");
            } else {
                CLIUtils.printError("Aggiornamento fallito.");
            }

        } catch (SQLException e) {
            CLIUtils.printError("Errore durante la modifica: " + e.getMessage());
        }

        CLIUtils.waitForEnter();
    }

    /**
     * Crea un nuovo utente con ruolo specifico.
     */
    private void creaNuovoUtente() {
        CLIUtils.printSubHeader("Crea Nuovo Utente");

        Integer idGenerato = authMenu.registrazioneAdmin();

        if (idGenerato != null) {
            CLIUtils.printSuccess("Utente creato con ID: " + idGenerato);
        }

        CLIUtils.waitForEnter();
    }

    private void cancellaUtente() {
        CLIUtils.printSubHeader("Cancella Utente");

        // Prima mostra la lista degli utenti
        try {
            List<Utente> utenti = utenteDAO.getAllUtenti();
            stampaListaUtenti(utenti);
        } catch (SQLException e) {
            CLIUtils.printError("Errore durante il recupero degli utenti: " + e.getMessage());
            CLIUtils.waitForEnter();
            return;
        }

        Integer idUtente = CLIUtils.readIntOptional("ID utente da cancellare (vuoto per annullare): ");
        if (idUtente == null) {
            CLIUtils.printWarning("Operazione annullata.");
            CLIUtils.waitForEnter();
            return;
        }

        // Verifica che non stia cancellando se stesso
        Utente utenteCorrente = sessionManager.getCurrentUser();
        if (idUtente.equals(utenteCorrente.getId())) {
            CLIUtils.printError("Non puoi cancellare il tuo stesso utente.");
            CLIUtils.waitForEnter();
            return;
        }

        // Conferma
        if (!CLIUtils.readConfirm("Confermi la cancellazione dell'utente con ID " + idUtente + "?")) {
            CLIUtils.printWarning("Operazione annullata.");
            CLIUtils.waitForEnter();
            return;
        }

        // Esegui cancellazione
        boolean success;
        try {
            success = utenteDAO.deleteUtente(idUtente);
        } catch (SQLException e) {
            CLIUtils.printError("Errore durante la cancellazione: " + e.getMessage());
            return;
        }
        if (success) {
            CLIUtils.printSuccess("Utente cancellato con successo!");
        } else {
            CLIUtils.printError("Cancellazione fallita.");
        }

        CLIUtils.waitForEnter();
    }

    private void stampaListaUtenti(List<Utente> utenti) {
        if (utenti.isEmpty()) {
            CLIUtils.printInfo("Nessun utente trovato.");
            return;
        }

        System.out.println();
        CLIUtils.printTableHeader("ID", "Nome", "Cognome", "Email", "Ruolo");
        for (Utente u : utenti) {
            CLIUtils.printTableRow(
                    String.valueOf(u.getId()),
                    CLIUtils.truncate(u.getNome(), 15),
                    CLIUtils.truncate(u.getCognome(), 15),
                    CLIUtils.truncate(u.getEmail(), 25),
                    u.getRuolo().toString());
        }
        CLIUtils.printTableFooter(5);
        CLIUtils.printInfo("Totale utenti: " + utenti.size());
    }
}
