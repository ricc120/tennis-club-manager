package it.tennis_club.controller.api;

/**
 * DTO per la creazione di una prenotazione.
 * 
 * CONCETTO: perché il frontend invia ID e non oggetti interi?
 * 
 * Il frontend non ha l'oggetto Campo completo — ha solo il suo ID
 * (scelto dall'utente in un menu <select>).
 * 
 * Esempio di JSON inviato dal frontend:
 * {
 *   "data": "2026-04-15",
 *   "oraInizio": "14:00",
 *   "idCampo": 2,
 *   "idSocio": 5
 * }
 * 
 * Il controller userà questi ID per recuperare gli oggetti dal database
 * tramite campoService.getCampoPerId(idCampo) e authService/utenteDAO.
 */
public class PrenotazioneRequest {
    private String data;        // "2026-04-15" — stringa ISO date
    private String oraInizio;   // "14:00" — stringa HH:mm
    private Integer idCampo;    // ID del campo scelto
    private Integer idSocio;    // ID dell'utente che prenota

    public PrenotazioneRequest() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Integer getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(Integer idCampo) {
        this.idCampo = idCampo;
    }

    public Integer getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Integer idSocio) {
        this.idSocio = idSocio;
    }
}
