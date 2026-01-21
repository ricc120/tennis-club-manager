package it.tennis_club.orm;

import it.tennis_club.domain_model.AllievoLezione;
import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione della partecipazione degli allievi alle
 * lezioni.
 */
public class AllievoLezioneDAO {

    private final LezioneDAO lezioneDAO;
    private final UtenteDAO utenteDAO;

    public AllievoLezioneDAO() {
        this.lezioneDAO = new LezioneDAO();
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Aggiunge un allievo a una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @return l'ID della partecipazione creata
     * @throws SQLException se si verifica un errore
     */
    public Integer aggiungiAllievoALezione(Integer idLezione, Integer idAllievo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Integer generatedId = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO allievo_lezione (id_lezione, id_allievo, presente) VALUES (?, ?, TRUE)";
            statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, idLezione);
            statement.setInt(2, idAllievo);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Aggiunta allievo fallita, nessuna riga inserita.");
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
            }

        } finally {
            closeResources(resultSet, statement, connection);
        }
        return generatedId;
    }

    /**
     * Rimuove un allievo da una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @return true se la rimozione è andata a buon fine
     * @throws SQLException se si verifica un errore
     */
    public boolean rimuoviAllievoLezione(Integer idLezione, Integer idAllievo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "DELETE FROM allievo_lezione WHERE id_lezione = ? AND id_allievo = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idLezione);
            statement.setInt(2, idAllievo);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Recupera tutti gli allievi di una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @return lista di utenti (allievi)
     * @throws SQLException se si verifica un errore
     */
    public List<Utente> getAllieviByLezione(Integer idLezione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Utente> allievi = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id_allievo FROM allievo_lezione WHERE id_lezione = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idLezione);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idAllievo = resultSet.getInt("id_allievo");
                Utente allievo = utenteDAO.getUtenteById(idAllievo);
                if (allievo != null) {
                    allievi.add(allievo);
                }
            }

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return allievi;
    }

    /**
     * Recupera tutte le lezioni di un allievo.
     * 
     * @param idAllievo l'ID dell'allievo
     * @return lista di lezioni
     * @throws SQLException se si verifica un errore
     */
    public List<Lezione> getLezioniByAllievo(Integer idAllievo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Lezione> lezioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id_lezione FROM allievo_lezione WHERE id_allievo = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idAllievo);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idLezione = resultSet.getInt("id_lezione");
                Lezione lezione = lezioneDAO.getLezioneById(idLezione);
                if (lezione != null) {
                    lezioni.add(lezione);
                }
            }

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return lezioni;
    }

    /**
     * Segna la presenza/assenza di un allievo a una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @param presente  true se presente, false se assente
     * @return true se l'aggiornamento è andato a buon fine
     * @throws SQLException se si verifica un errore
     */
    public boolean segnaPresenza(Integer idLezione, Integer idAllievo, boolean presente) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "UPDATE allievo_lezione SET presente = ? WHERE id_lezione = ? AND id_allievo = ?";
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, presente);
            statement.setInt(2, idLezione);
            statement.setInt(3, idAllievo);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Aggiunge feedback specifico per un allievo in una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @param feedback  il feedback da aggiungere
     * @return true se l'aggiornamento è andato a buon fine
     * @throws SQLException se si verifica un errore
     */
    public boolean aggiungiFeedback(Integer idLezione, Integer idAllievo, String feedback) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "UPDATE allievo_lezione SET feedback = ? WHERE id_lezione = ? AND id_allievo = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, feedback);
            statement.setInt(2, idLezione);
            statement.setInt(3, idAllievo);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Conta il numero di allievi in una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @return il numero di allievi
     * @throws SQLException se si verifica un errore
     */
    public int contaAllievi(Integer idLezione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT COUNT(*) as totale FROM allievo_lezione WHERE id_lezione = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idLezione);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("totale");
            }

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return 0;
    }

    /**
     * Recupera i dettagli completi di un allievo in una lezione specifica.
     * Restituisce un oggetto AllievoLezione con tutte le informazioni (presente,
     * note).
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @return l'oggetto AllievoLezione completo, null se non trovato
     * @throws SQLException se si verifica un errore
     */
    public AllievoLezione getDettagliAllievoLezione(Integer idLezione, Integer idAllievo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id, id_lezione, id_allievo, presente, note " +
                    "FROM allievo_lezione WHERE id_lezione = ? AND id_allievo = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idLezione);
            statement.setInt(2, idAllievo);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToAllievoLezione(resultSet);
            }

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return null;
    }

    /**
     * Recupera tutti gli allievi di una lezione con i loro dettagli completi.
     * Utile quando serve sapere chi era presente, le note, ecc.
     * 
     * @param idLezione l'ID della lezione
     * @return lista di oggetti AllievoLezione completi
     * @throws SQLException se si verifica un errore
     */
    public List<AllievoLezione> getAllieviLezioneConDettagli(Integer idLezione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<AllievoLezione> allieviLezione = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id, id_lezione, id_allievo, presente, note " +
                    "FROM allievo_lezione WHERE id_lezione = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idLezione);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                allieviLezione.add(mapResultSetToAllievoLezione(resultSet));
            }

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return allieviLezione;
    }

    /**
     * Metodo helper per mappare un ResultSet a un oggetto AllievoLezione.
     * 
     * @param rs il ResultSet
     * @return l'oggetto AllievoLezione
     * @throws SQLException se si verifica un errore
     */
    private AllievoLezione mapResultSetToAllievoLezione(ResultSet rs) throws SQLException {
        AllievoLezione allievoLezione = new AllievoLezione();
        allievoLezione.setId(rs.getInt("id"));
        allievoLezione.setPresente(rs.getBoolean("presente"));
        allievoLezione.setFeedback(rs.getString("feedback"));

        // Recupera gli oggetti completi
        int idLezione = rs.getInt("id_lezione");
        allievoLezione.setLezione(lezioneDAO.getLezioneById(idLezione));

        int idAllievo = rs.getInt("id_allievo");
        allievoLezione.setAllievo(utenteDAO.getUtenteById(idAllievo));

        return allievoLezione;
    }

    /**
     * Metodo helper per chiudere le risorse JDBC.
     */
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /* ignorato */
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                /* ignorato */
            }
        }
        ConnectionManager.closeConnection(conn);
    }
}
