package it.tennis_club.domain_model;

/**
 * Rappresenta un campo da tennis del club.
 * Corrisponde alla tabella 'campo' nel database.
 */
public class Campo {

    private Integer id;
    private String nome;
    private String tipoSuperficie;
    private Boolean isCoperto;

    // Costruttore vuoto
    public Campo() {
    }

    // Costruttore completo
    public Campo(Integer id, String nome, String tipoSuperficie, Boolean isCoperto) {
        this.id = id;
        this.nome = nome;
        this.tipoSuperficie = tipoSuperficie;
        this.isCoperto = isCoperto;
    }

    // Getter e Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoSuperficie() {
        return tipoSuperficie;
    }

    public void setTipoSuperficie(String tipoSuperficie) {
        this.tipoSuperficie = tipoSuperficie;
    }

    public Boolean getIsCoperto() {
        return isCoperto;
    }

    public void setIsCoperto(Boolean isCoperto) {
        this.isCoperto = isCoperto;
    }

    @Override
    public String toString() {
        return "Campo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipoSuperficie='" + tipoSuperficie + '\'' +
                ", isCoperto=" + isCoperto +
                '}';
    }
}
