package it.tennis_club.orm;

import it.tennis_club.domain_model.Campo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione dei campi da tennis nel database.
 */
@Repository
public class CampoDAO {

    /**
     * Recupera tutti i campi disponibili nel database.
     * 
     * @return una lista di tutti i campi
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Campo> getAllCampi() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Campo> campi = new ArrayList<>();

        try {
            // Ottiene la connessione dal ConnectionManager
            connection = ConnectionManager.getConnection();

            // Prepara la query SQL
            String query = "SELECT id, nome, tipo_superficie, is_coperto FROM campo ORDER BY nome";

            statement = connection.prepareStatement(query);

            // Esegue la query
            resultSet = statement.executeQuery();

            // Itera sui risultati e crea gli oggetti Campo
            while (resultSet.next()) {
                Campo campo = new Campo();
                campo.setId(resultSet.getInt("id"));
                campo.setNome(resultSet.getString("nome"));
                campo.setTipoSuperficie(resultSet.getString("tipo_superficie"));
                campo.setIsCoperto(resultSet.getBoolean("is_coperto"));

                campi.add(campo);
            }

        } catch (SQLException e) {
            // Rilancia l'eccezione dopo averla loggata
            System.err.println("Errore durante il recupero dei campi: " + e.getMessage());
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

        return campi;
    }

    /**
     * Recupera un campo specifico tramite il suo ID.
     * 
     * @param id l'ID del campo da cercare
     * @return l'oggetto Campo se trovato, null altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Campo getCampoById(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Campo campo = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, nome, tipo_superficie, is_coperto FROM campo WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                campo = new Campo();
                campo.setId(resultSet.getInt("id"));
                campo.setNome(resultSet.getString("nome"));
                campo.setTipoSuperficie(resultSet.getString("tipo_superficie"));
                campo.setIsCoperto(resultSet.getBoolean("is_coperto"));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del campo: " + e.getMessage());
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

        return campo;
    }

    /**
     * Recupera tutti i campi coperti.
     * 
     * @return una lista dei campi coperti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Campo> getCampiCoperti() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Campo> campi = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, nome, tipo_superficie, is_coperto " +
                    "FROM campo WHERE is_coperto = true ORDER BY nome";

            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Campo campo = new Campo();
                campo.setId(resultSet.getInt("id"));
                campo.setNome(resultSet.getString("nome"));
                campo.setTipoSuperficie(resultSet.getString("tipo_superficie"));
                campo.setIsCoperto(resultSet.getBoolean("is_coperto"));

                campi.add(campo);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei campi coperti: " + e.getMessage());
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

        return campi;
    }

    /**
     * Recupera tutti i campi per tipo di superficie.
     * 
     * @param tipoSuperficie il tipo di superficie da cercare (es. "Terra",
     *                       "Cemento", "Erba")
     * @return una lista dei campi con il tipo di superficie specificato
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Campo> getCampiByTipoSuperficie(String tipoSuperficie) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Campo> campi = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, nome, tipo_superficie, is_coperto " +
                    "FROM campo WHERE tipo_superficie = ? ORDER BY nome";

            statement = connection.prepareStatement(query);
            statement.setString(1, tipoSuperficie);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Campo campo = new Campo();
                campo.setId(resultSet.getInt("id"));
                campo.setNome(resultSet.getString("nome"));
                campo.setTipoSuperficie(resultSet.getString("tipo_superficie"));
                campo.setIsCoperto(resultSet.getBoolean("is_coperto"));

                campi.add(campo);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei campi per tipo superficie: " + e.getMessage());
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

        return campi;
    }
}
