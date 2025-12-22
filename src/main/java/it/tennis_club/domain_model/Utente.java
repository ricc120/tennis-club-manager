package it.tennis_club.domain_model;

/**
 * Rappresenta un utente del tennis club.
 * Corrisponde alla tabella 'utente' nel database.
 */
public class Utente {
    
    // Enum per il ruolo dell'utente
    public enum Ruolo {
        ADMIN, MAESTRO, SOCIO, ALLIEVO, MANUTENTORE;
    }
    
    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Ruolo ruolo;
    
    // Costruttore vuoto
    public Utente() {
    }
    
    // Costruttore completo
    public Utente(Integer id, String nome, String cognome, String email, String password, Ruolo ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
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
    
    public String getCognome() {
        return cognome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Ruolo getRuolo() {
        return ruolo;
    }
    
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
    
    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", ruolo=" + ruolo +
                '}';
    }
}
