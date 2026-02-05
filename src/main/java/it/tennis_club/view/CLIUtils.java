package it.tennis_club.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility condivise per la CLI del Tennis Club Manager.
 * Fornisce metodi per input, formattazione e visualizzazione.
 */
public class CLIUtils {

    private static final Scanner scanner = new Scanner(System.in);

    // Formattatori per date e ore
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // Codici ANSI per colori (funzionano su terminali compatibili)
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    /**
     * Legge una stringa dall'input.
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Legge una stringa opzionale dall'input (premere INVIO per annullare).
     */
    public static String readStringOptional(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    /**
     * Legge una stringa di testo che deve contenere almeno una lettera.
     * Rifiuta input vuoti o composti solo da numeri.
     * Utile per campi come nome e cognome.
     */
    public static String readTextString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                printError("Il campo non può essere vuoto.");
                continue;
            }
            // Rifiuta input composti solo da numeri
            if (input.matches("^[0-9]+$")) {
                printError("Il valore deve contenere almeno una lettera.");
                continue;
            }
            return input;
        }
    }

    /**
     * Legge una stringa di testo opzionale (premere INVIO per annullare).
     * Se non vuota, deve contenere almeno una lettera.
     */
    public static String readTextStringOptional(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            // Rifiuta input composti solo da numeri
            if (input.matches("^[0-9]+$")) {
                printError("Il valore deve contenere almeno una lettera.");
                continue;
            }
            return input;
        }
    }

    /**
     * Legge un intero dall'input con gestione errori.
     * Rifiuta input con zeri iniziali superflui (es. "001").
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                // Verifica che l'input non abbia zeri iniziali superflui
                // "0" è valido, "-0" diventa 0, ma "00", "01", "-01" non sono validi
                if (!input.equals(String.valueOf(value))) {
                    printError("Inserisci un numero valido senza zeri iniziali.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                printError("Inserisci un numero valido.");
            }
        }
    }

    /**
     * Legge un intero opzionale (permette input vuoto per annullare).
     * Rifiuta input con zeri iniziali superflui (es. "001").
     */
    public static Integer readIntOptional(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            int value = Integer.parseInt(input);
            // Verifica che l'input non abbia zeri iniziali superflui
            if (!input.equals(String.valueOf(value))) {
                printError("Numero non valido (evita zeri iniziali).");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            printError("Numero non valido.");
            return null;
        }
    }

    /**
     * Legge una data dall'input nel formato dd/MM/yyyy.
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (dd/MM/yyyy): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                printError("Formato data non valido. Usa dd/MM/yyyy.");
            }
        }
    }

    /**
     * Legge una data opzionale (premere INVIO per annullare).
     */
    public static LocalDate readDateOptional(String prompt) {
        while (true) {
            System.out.print(prompt + " (dd/MM/yyyy, vuoto per annullare): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                printError("Formato data non valido. Usa dd/MM/yyyy.");
            }
        }
    }

    /**
     * Legge un'ora dall'input nel formato HH:mm.
     */
    public static LocalTime readTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (HH:mm): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalTime.parse(input, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                printError("Formato ora non valido. Usa HH:mm.");
            }
        }
    }

    /**
     * Legge un'ora opzionale (premere INVIO per annullare).
     */
    public static LocalTime readTimeOptional(String prompt) {
        while (true) {
            System.out.print(prompt + " (HH:mm, vuoto per annullare): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return LocalTime.parse(input, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                printError("Formato ora non valido. Usa HH:mm.");
            }
        }
    }

    /**
     * Legge una conferma booleana (s/n).
     */
    public static boolean readConfirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (s/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("s") || input.equals("si") || input.equals("sì")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            printError("Inserisci 's' per sì o 'n' per no.");
        }
    }

    /**
     * Stampa un messaggio di successo in verde.
     */
    public static void printSuccess(String message) {
        System.out.println(GREEN + message + RESET);
    }

    /**
     * Stampa un messaggio di errore in rosso.
     */
    public static void printError(String message) {
        System.out.println(RED + message + RESET);
    }

    /**
     * Stampa un avviso in giallo.
     */
    public static void printWarning(String message) {
        System.out.println(YELLOW + message + RESET);
    }

    /**
     * Stampa un'informazione in ciano.
     */
    public static void printInfo(String message) {
        System.out.println(CYAN + message + RESET);
    }

    /**
     * Stampa un header formattato.
     */
    public static void printHeader(String title) {
        String border = "═".repeat(title.length() + 4);
        System.out.println();
        System.out.println(BLUE + BOLD + "╔" + border + "╗" + RESET);
        System.out.println(BLUE + BOLD + "║  " + title + "  ║" + RESET);
        System.out.println(BLUE + BOLD + "╚" + border + "╝" + RESET);
        System.out.println();
    }

    /**
     * Stampa un sotto-header.
     */
    public static void printSubHeader(String title) {
        System.out.println();
        System.out.println(CYAN + "── " + title + " ──" + RESET);
        System.out.println();
    }

    /**
     * Stampa un separatore.
     */
    public static void printSeparator() {
        System.out.println("─".repeat(50));
    }

    /**
     * Pulisce lo schermo (tentativo - dipende dal terminale).
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Pausa in attesa che l'utente prema Invio.
     */
    public static void waitForEnter() {
        System.out.print("\nPremi " + BOLD + "INVIO" + RESET + " per continuare...");
        scanner.nextLine();
    }

    /**
     * Formatta una data per la visualizzazione.
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "-";
    }

    /**
     * Formatta un'ora per la visualizzazione.
     */
    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "-";
    }

    /**
     * Stampa una riga di tabella formattata.
     */
    public static void printTableRow(String... columns) {
        StringBuilder sb = new StringBuilder("│");
        for (String col : columns) {
            sb.append(" ").append(String.format("%-15s", col)).append(" │");
        }
        System.out.println(sb.toString());
    }

    /**
     * Stampa l'header di una tabella.
     */
    public static void printTableHeader(String... columns) {
        int totalWidth = (columns.length * 17) + 1;
        System.out.println("┌" + "─".repeat(totalWidth - 2) + "┐");
        printTableRow(columns);
        System.out.println("├" + "─".repeat(totalWidth - 2) + "┤");
    }

    /**
     * Stampa il footer di una tabella.
     */
    public static void printTableFooter(int columnCount) {
        int totalWidth = (columnCount * 17) + 1;
        System.out.println("└" + "─".repeat(totalWidth - 2) + "┘");
    }

    /**
     * Chiude lo scanner (da chiamare alla fine dell'applicazione).
     */
    public static void closeScanner() {
        scanner.close();
    }
}
