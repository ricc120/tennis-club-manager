package it.tennis_club.business_logic;

public class CampoException extends Exception {

    CampoException(String message) {
        super(message);
    }

    CampoException(String message, Throwable cause) {
        super(message, cause);
    }

}
