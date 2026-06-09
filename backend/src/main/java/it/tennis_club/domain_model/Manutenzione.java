package it.tennis_club.domain_model;

import java.time.LocalDate;

/**
 * Rappresenta una manutenzione su un campo da tennis.
 * Corrisponde alla tabella 'manutenzione' nel database.
 */
public class Manutenzione {

    // Enum per lo stato della manutenzione
    public enum Stato {
        IN_CORSO, COMPLETATA, ANNULLATA
    }

    private Integer id;
    private Campo campo;
    private Utente manutentore;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private String descrizione;
    private Stato stato;

    // Costruttore vuoto
    public Manutenzione() {
    }

    // Costruttore completo
    public Manutenzione(Integer id, Campo campo, Utente manutentore, LocalDate dataInizio,
            LocalDate dataFine, String descrizione, Stato stato) {
        this.id = id;
        this.campo = campo;
        this.manutentore = manutentore;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.descrizione = descrizione;
        this.stato = stato;
    }

    // Getter e Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    public Utente getManutentore() {
        return manutentore;
    }

    public void setManutentore(Utente manutentore) {
        this.manutentore = manutentore;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return "Manutenzione{" +
                "id=" + id +
                ", campo=" + campo +
                ", manutentore=" + manutentore +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", descrizione='" + descrizione + '\'' +
                ", stato=" + stato +
                '}';
    }
}
