package it.tennis_club.business_logic;

/**
 * Eccezione personalizzata per gli errori sul servizio dei campi.
 * Viene lanciata quando si verificano problemi durante il processo di gestione
 * dei campi.
 */

public class CampoException extends Exception {

    /**
     * Costruttore con un messaggio di errore
     * 
     * @param message il messaggio di errore
     */
    CampoException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio di errore e causa.
     * 
     * @param message il messaggio di errore
     * @param cause   la causa dell'eccezione
     */
    CampoException(String message, Throwable cause) {
        super(message, cause);
    }

}
