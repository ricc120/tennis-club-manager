package it.tennis_club.domain_model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Rappresenta una prenotazione di un campo da tennis.
 * Corrisponde alla tabella 'prenotazione' nel database.
 */
public class Prenotazione {

    private Integer id;
    private LocalDate data;
    private LocalTime oraInizio;
    private Campo campo;       // Oggetto Campo completo, non solo l'ID
    private Utente socio;       // Oggetto Utente completo, non solo l'ID

    // Costruttore vuoto
    public Prenotazione() {
    }

    // Costruttore completo
    public Prenotazione(Integer id, LocalDate data, LocalTime oraInizio, Campo campo, Utente socio) {
        this.id = id;
        this.data = data;
        this.oraInizio = oraInizio;
        this.campo = campo;
        this.socio = socio;
    }

    // Getter e Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    public Utente getSocio() {
        return socio;
    }

    public void setSocio(Utente socio) {
        this.socio = socio;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", data=" + data +
                ", oraInizio=" + oraInizio +
                ", campo=" + (campo != null ? campo.getNome() : "null") +
                ", socio=" + (socio != null ? socio.getNome() + " " + socio.getCognome() : "null") +
                '}';
    }
}
