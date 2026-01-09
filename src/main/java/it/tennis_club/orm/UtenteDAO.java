package it.tennis_club.orm;

import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object per la gestione degli utenti nel database.
 */
public class UtenteDAO {

    /**
     * Esegue il login di un utente verificando email e password.
     * 
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @return l'oggetto Utente se le credenziali sono corrette, null altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Utente login(String email, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Utente utente = null;

        try {
            // Ottiene la connessione dal ConnectionManager
            connection = ConnectionManager.getConnection();

            // Prepara la query SQL con parametri per prevenire SQL injection
            String query = "SELECT id, nome, cognome, email, password, ruolo " +
                    "FROM utente " +
                    "WHERE email = ? AND password = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            // Esegue la query
            resultSet = statement.executeQuery();

            // Se trova un risultato, crea l'oggetto Utente
            if (resultSet.next()) {
                utente = new Utente();
                utente.setId(resultSet.getInt("id"));
                utente.setNome(resultSet.getString("nome"));
                utente.setCognome(resultSet.getString("cognome"));
                utente.setEmail(resultSet.getString("email"));
                utente.setPassword(resultSet.getString("password"));

                // Converte la stringa del ruolo nell'enum Ruolo
                String ruoloStr = resultSet.getString("ruolo");
                utente.setRuolo(Ruolo.valueOf(ruoloStr));
            }

        } catch (SQLException e) {
            // Rilancia l'eccezione dopo averla loggata
            System.err.println("Errore durante il login: " + e.getMessage());
            throw e;

        } finally {
            // Chiude le risorse in ordine inverso di apertura
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Errore durante la chiusura del ResultSet: " + e.getMessage());
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Errore durante la chiusura del PreparedStatement: " + e.getMessage());
                }
            }

            // Chiude la connessione usando il ConnectionManager
            ConnectionManager.closeConnection(connection);
        }

        return utente;
    }

    /**
     * Recupera un utente specifico tramite il suo ID.
     * 
     * @param id l'ID dell'utente da cercare
     * @return l'oggetto Utente se trovato, null altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Utente getUtenteById(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Utente utente = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, nome, cognome, email, password, ruolo " +
                    "FROM utente WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                utente = new Utente();
                utente.setId(resultSet.getInt("id"));
                utente.setNome(resultSet.getString("nome"));
                utente.setCognome(resultSet.getString("cognome"));
                utente.setEmail(resultSet.getString("email"));
                utente.setPassword(resultSet.getString("password"));

                String ruoloStr = resultSet.getString("ruolo");
                utente.setRuolo(Ruolo.valueOf(ruoloStr));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dell'utente: " + e.getMessage());
            throw e;

        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Errore durante la chiusura del ResultSet: " + e.getMessage());
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Errore durante la chiusura del PreparedStatement: " + e.getMessage());
                }
            }

            ConnectionManager.closeConnection(connection);
        }

        return utente;
    }
}
