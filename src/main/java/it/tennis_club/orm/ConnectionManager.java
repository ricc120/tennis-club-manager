package it.tennis_club.orm;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Gestisce la connessione al database PostgreSQL.
 * 
 * La configurazione è centralizzata in application.properties e gestita
 * interamente da Spring Boot. Il DataSource viene iniettato automaticamente
 * da Spring (tramite spring-boot-starter-jdbc).
 * 
 * Le migrazioni Flyway sono gestite da Spring Boot (spring.flyway.*),
 * non più manualmente da questa classe.
 */
@Component
public class ConnectionManager {

    private static DataSource dataSource;

    /**
     * Costruttore usato da Spring per iniettare il DataSource configurato
     * in application.properties.
     * Il DataSource viene salvato in un campo statico per mantenere la
     * compatibilità con i DAO legacy che chiamano getConnection() staticamente.
     * 
     * @param dataSource il DataSource configurato da Spring Boot
     */
    @Autowired
    public ConnectionManager(DataSource dataSource) {
        ConnectionManager.dataSource = dataSource;
    }

    /**
     * Ottiene una nuova connessione al database dal DataSource gestito da Spring.
     * 
     * @return una connessione al database
     * @throws SQLException se non è possibile ottenere la connessione
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException(
                    "DataSource non inizializzato. Assicurarsi che Spring Boot sia avviato correttamente.");
        }
        return dataSource.getConnection();
    }

    /**
     * Chiude la connessione in modo sicuro.
     * 
     * @param connection la connessione da chiudere (può essere null)
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }
}