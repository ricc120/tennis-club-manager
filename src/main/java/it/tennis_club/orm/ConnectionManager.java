package it.tennis_club.orm;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestisce la connessione al database PostgreSQL caricando i parametri
 * da un file di configurazione esterno.
 */
public class ConnectionManager {
    
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Spiacente, non riesco a trovare db.properties. Assicurati che sia in src/main/resources/");
            } else {
                // Carica il file properties
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Ottiene una nuova connessione al database usando i parametri del file properties.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL non trovato", e);
        }
        
        return DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.user"),
            properties.getProperty("db.password")
        );
    }
    
    /**
     * Chiude la connessione in modo sicuro.
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