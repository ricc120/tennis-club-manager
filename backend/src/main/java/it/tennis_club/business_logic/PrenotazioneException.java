package it.tennis_club.business_logic;

/**
 * Eccezione personalizzata per gestire gli errori relativi alle prenotazioni.
 * Viene lanciata quando si verificano problemi durante la creazione, modifica
 * o cancellazione di una prenotazione.
 */
public class PrenotazioneException extends Exception {

    /**
     * Costruttore con solo il messaggio di errore.
     * 
     * @param message il messaggio di errore
     */
    public PrenotazioneException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa dell'errore.
     * 
     * @param message il messaggio di errore
     * @param cause   la causa dell'errore (tipicamente un'altra eccezione)
     */
    public PrenotazioneException(String message, Throwable cause) {
        super(message, cause);
    }
}
