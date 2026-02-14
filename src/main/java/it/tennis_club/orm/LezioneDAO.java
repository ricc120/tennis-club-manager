package it.tennis_club.orm;

import it.tennis_club.domain_model.Lezione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle lezioni di tennis nel database.
 * Implementa le operazioni CRUD e query specifiche per recuperare lezioni per
 * maestro o prenotazione.
 */
@Repository
public class LezioneDAO {

    private final UtenteDAO utenteDAO;
    private final PrenotazioneDAO prenotazioneDAO;

    /**
     * Costruttore che inizializza i DAO dipendenti.
     */
    public LezioneDAO() {
        this.utenteDAO = new UtenteDAO();
        this.prenotazioneDAO = new PrenotazioneDAO();
    }

    /**
     * Costruttore per Dependency Injection di Spring.
     */
    @Autowired
    public LezioneDAO(UtenteDAO utenteDAO, PrenotazioneDAO prenotazioneDAO) {
        this.utenteDAO = utenteDAO;
        this.prenotazioneDAO = prenotazioneDAO;
    }

    /**
     * Crea una nuova lezione nel database.
     * 
     * @param lezione l'oggetto Lezione da inserire
     * @return l'ID generato per la nuova lezione
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Integer createLezione(Lezione lezione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Integer generatedId = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO lezione (id_prenotazione, id_maestro, descrizione) " +
                    "VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, lezione.getPrenotazione().getId());
            statement.setInt(2, lezione.getMaestro().getId());
            statement.setString(3, lezione.getDescrizione());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creazione lezione fallita, nessuna riga inserita.");
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
                lezione.setId(generatedId);
            } else {
                throw new SQLException("Creazione lezione fallita, nessun ID ottenuto.");
            }

        } catch (SQLException e) {
            System.err.println("Errore durante la creazione della lezione: " + e.getMessage());
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return generatedId;
    }

    /**
     * Elimina una lezione dal database.
     * 
     * @param id l'ID della lezione da eliminare
     * @return true se la cancellazione è andata a buon fine, false altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public boolean deleteLezione(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "DELETE FROM lezione WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante la cancellazione della lezione: " + e.getMessage());
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Aggiorna una lezione esistente nel database.
     * 
     * @param lezione l'oggetto Lezione con i dati aggiornati
     * @return true se l'aggiornamento è andato a buon fine, false altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public boolean updateLezione(Lezione lezione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "UPDATE lezione SET descrizione = ?, " +
                    "id_prenotazione = ?, id_maestro = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, lezione.getDescrizione());
            statement.setInt(2, lezione.getPrenotazione().getId());
            statement.setInt(3, lezione.getMaestro().getId());
            statement.setInt(4, lezione.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento della lezione: " + e.getMessage());
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Recupera tutte le lezioni dal database.
     * 
     * @return una lista di tutte le lezioni
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Lezione> getAllLezioni() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Lezione> lezioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id, id_prenotazione, id_maestro, descrizione " +
                    "FROM lezione ORDER BY id_prenotazione";

            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                lezioni.add(mapResultSetToLezione(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle lezioni: " + e.getMessage());
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return lezioni;
    }

    /**
     * Recupera una lezione specifica tramite il suo ID.
     * 
     * @param id l'ID della lezione
     * @return l'oggetto Lezione se trovato, null altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Lezione getLezioneById(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Lezione lezione = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id, id_prenotazione, id_maestro, descrizione " +
                    "FROM lezione WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lezione = mapResultSetToLezione(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero della lezione per ID: " + e.getMessage());
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return lezione;
    }

    /**
     * Recupera la lezione associata a una specifica prenotazione.
     * 
     * @param idPrenotazione l'ID della prenotazione
     * @return l'oggetto Lezione se trovato, null altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Lezione getLezioneByPrenotazione(Integer idPrenotazione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Lezione lezione = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id, id_prenotazione, id_maestro, descrizione " +
                    "FROM lezione WHERE id_prenotazione = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idPrenotazione);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lezione = mapResultSetToLezione(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero della lezione per prenotazione: " + e.getMessage());
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return lezione;
    }

    /**
     * Recupera tutte le lezioni tenute da un specifico maestro.
     * 
     * @param idMaestro l'ID del maestro
     * @return una lista di lezioni
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Lezione> getLezioniByMaestro(Integer idMaestro) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Lezione> lezioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id, id_prenotazione, id_maestro, descrizione " +
                    "FROM lezione WHERE id_maestro = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idMaestro);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                lezioni.add(mapResultSetToLezione(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle lezioni per maestro: " + e.getMessage());
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return lezioni;
    }

    /**
     * Metodo helper per mappare un ResultSet a un oggetto Lezione.
     * 
     * @param rs il ResultSet
     * @return l'oggetto Lezione
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    private Lezione mapResultSetToLezione(ResultSet rs) throws SQLException {
        Lezione lezione = new Lezione();
        lezione.setId(rs.getInt("id"));
        lezione.setDescrizione(rs.getString("descrizione"));

        // Recupero oggetti completi tramite gli ID
        int idPrenotazione = rs.getInt("id_prenotazione");
        lezione.setPrenotazione(prenotazioneDAO.getPrenotazioneById(idPrenotazione));

        int idMaestro = rs.getInt("id_maestro");
        lezione.setMaestro(utenteDAO.getUtenteById(idMaestro));

        return lezione;
    }

    /**
     * Metodo helper per chiudere le risorse JDBC.
     */
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /* ignorato */ }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                /* ignorato */ }
        }
        ConnectionManager.closeConnection(conn);
    }
}
