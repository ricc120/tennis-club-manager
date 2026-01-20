package it.tennis_club.business_logic;

/**
 * Eccezione personalizzata per gestire gli errori relativi alle
 * lezioni dell'Accademia.
 * Viene lanciata quando si verificano errori durante la creazione,
 * aggiornamento, cancellazione o recupero di una lezione.
 */
public class AccademiaException extends Exception {

    /**
     * Costruttore con il solo messaggio di errore
     * 
     * @param message
     */
    public AccademiaException(String message) {
        super(message);
    }

    /**
     * Costruttore con il messaggio di errore e la causa dell'errore
     * 
     * @param message
     * @param cause
     */
    public AccademiaException(String message, Throwable cause) {
        super(message, cause);
    }

}
