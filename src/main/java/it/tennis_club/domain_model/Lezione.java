package it.tennis_club.domain_model;

/**
 * Rappresenta una lezione di tennis creata da un Maestro.
 * Corrisponde alla tabella 'lezione' nel database.
 */
public class Lezione {

    private Integer id;
    private Prenotazione prenotazione;
    private Utente maestro;
    private String feedback;
    private String descrizione;

    // Costruttore vuoto
    public Lezione() {
    }

    // Costruttore completo
    public Lezione(Integer id, Prenotazione prenotazione, Utente maestro, String feedback, String descrizione) {
        this.id = id;
        this.prenotazione = prenotazione;
        this.maestro = maestro;
        this.feedback = feedback;
        this.descrizione = descrizione;
    }

    // Getter e Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public Utente getMaestro() {
        return maestro;
    }

    public void setMaestro(Utente maestro) {
        this.maestro = maestro;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return "Lezione{" +
                "id=" + id +
                ", prenotazione=" + (prenotazione != null ? prenotazione.getId() : "null") +
                ", maestro=" + (maestro != null ? maestro.getNome() + " " + maestro.getCognome() : "null") +
                ", feedback='" + feedback + '\'' +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }
}
