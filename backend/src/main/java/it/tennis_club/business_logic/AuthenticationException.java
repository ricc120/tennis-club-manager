package it.tennis_club.business_logic;

/**
 * Eccezione personalizzata per gli errori di autenticazione.
 * Viene lanciata quando si verificano problemi durante il processo di login.
 */
public class AuthenticationException extends Exception {

    /**
     * Costruttore con messaggio di errore.
     * 
     * @param message il messaggio di errore
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio di errore e causa.
     * 
     * @param message il messaggio di errore
     * @param cause   la causa dell'eccezione
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
