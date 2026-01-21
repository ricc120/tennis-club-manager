package it.tennis_club.domain_model;

/**
 * Rappresenta la partecipazione di un allievo a una lezione.
 * Questa classe gestisce la relazione molti-a-molti tra Lezione e Utente
 * (allievi).
 * 
 * NOTA: Il feedback è specifico per ogni allievo, permettendo al maestro
 * di dare valutazioni personalizzate.
 */
public class AllievoLezione {

    private Integer id;
    private Lezione lezione;
    private Utente allievo;
    private Boolean presente; // Per segnare le assenze
    private String feedback; // Feedback specifico del maestro per questo allievo

    // Costruttore vuoto
    public AllievoLezione() {
    }

    // Costruttore completo
    public AllievoLezione(Integer id, Lezione lezione, Utente allievo, Boolean presente, String feedback) {
        this.id = id;
        this.lezione = lezione;
        this.allievo = allievo;
        this.presente = presente;
        this.feedback = feedback;
    }

    // Getter e Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Lezione getLezione() {
        return lezione;
    }

    public void setLezione(Lezione lezione) {
        this.lezione = lezione;
    }

    public Utente getAllievo() {
        return allievo;
    }

    public void setAllievo(Utente allievo) {
        this.allievo = allievo;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "AllievoLezione{" +
                "id=" + id +
                ", lezione=" + (lezione != null ? lezione.getId() : "null") +
                ", allievo=" + (allievo != null ? allievo.getNome() + " " + allievo.getCognome() : "null") +
                ", presente=" + presente +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
