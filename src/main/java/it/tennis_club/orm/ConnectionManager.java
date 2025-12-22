package it.tennis_club.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la connessione al database PostgreSQL.
 */
public class ConnectionManager {
    
    // Configurazione database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/tennis_club";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    
    /**
     * Ottiene una nuova connessione al database.
     * 
     * @return Connection al database
     * @throws SQLException se la connessione fallisce
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Carica il driver PostgreSQL (opzionale per JDBC 4.0+)
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL non trovato", e);
        }
        
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Chiude la connessione in modo sicuro.
     * 
     * @param connection la connessione da chiudere
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