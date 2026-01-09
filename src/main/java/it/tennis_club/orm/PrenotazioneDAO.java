package it.tennis_club.orm;

import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle prenotazioni nel database.
 * Implementa le operazioni CRUD complete e query specifiche per il business.
 */
public class PrenotazioneDAO {

    // Istanze dei DAO necessari per recuperare oggetti completi
    private final CampoDAO campoDAO;
    private final UtenteDAO utenteDAO;

    /**
     * Costruttore che inizializza i DAO necessari.
     */
    public PrenotazioneDAO() {
        this.campoDAO = new CampoDAO();
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Recupera tutte le prenotazioni dal database.
     * Per ogni prenotazione, carica anche gli oggetti Campo e Utente completi.
     * 
     * @return una lista di tutte le prenotazioni
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Prenotazione> getAllPrenotazioni() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, data, ora_inizio, id_campo, id_socio " +
                    "FROM prenotazione ORDER BY data, ora_inizio";

            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Prenotazione prenotazione = mapResultSetToPrenotazione(resultSet);
                prenotazioni.add(prenotazione);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return prenotazioni;
    }

    /**
     * Recupera una prenotazione specifica tramite il suo ID.
     * 
     * @param id l'ID della prenotazione da cercare
     * @return l'oggetto Prenotazione se trovato, null altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Prenotazione getPrenotazioneById(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Prenotazione prenotazione = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, data, ora_inizio, id_campo, id_socio " +
                    "FROM prenotazione WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prenotazione = mapResultSetToPrenotazione(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero della prenotazione: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return prenotazione;
    }

    /**
     * Recupera tutte le prenotazioni per una specifica data.
     * 
     * @param data la data per cui cercare le prenotazioni
     * @return una lista di prenotazioni per la data specificata
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Prenotazione> getPrenotazioniByData(LocalDate data) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, data, ora_inizio, id_campo, id_socio " +
                    "FROM prenotazione WHERE data = ? ORDER BY ora_inizio";

            statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(data));

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Prenotazione prenotazione = mapResultSetToPrenotazione(resultSet);
                prenotazioni.add(prenotazione);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni per data: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return prenotazioni;
    }

    /**
     * Recupera tutte le prenotazioni per un campo specifico.
     * 
     * @param idCampo l'ID del campo
     * @return una lista di prenotazioni per il campo specificato
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Prenotazione> getPrenotazioniByCampo(Integer idCampo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, data, ora_inizio, id_campo, id_socio " +
                    "FROM prenotazione WHERE id_campo = ? ORDER BY data, ora_inizio";

            statement = connection.prepareStatement(query);
            statement.setInt(1, idCampo);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Prenotazione prenotazione = mapResultSetToPrenotazione(resultSet);
                prenotazioni.add(prenotazione);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni per campo: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return prenotazioni;
    }

    /**
     * Recupera tutte le prenotazioni di un socio specifico.
     * 
     * @param idSocio l'ID del socio
     * @return una lista di prenotazioni del socio specificato
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Prenotazione> getPrenotazioniBySocio(Integer idSocio) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, data, ora_inizio, id_campo, id_socio " +
                    "FROM prenotazione WHERE id_socio = ? ORDER BY data, ora_inizio";

            statement = connection.prepareStatement(query);
            statement.setInt(1, idSocio);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Prenotazione prenotazione = mapResultSetToPrenotazione(resultSet);
                prenotazioni.add(prenotazione);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni per socio: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return prenotazioni;
    }

    /**
     * Recupera le prenotazioni per una data e un campo specifici.
     * Utile per verificare la disponibilità di un campo in una data.
     * 
     * @param data    la data
     * @param idCampo l'ID del campo
     * @return una lista di prenotazioni
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Prenotazione> getPrenotazioniByDataAndCampo(LocalDate data, Integer idCampo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, data, ora_inizio, id_campo, id_socio " +
                    "FROM prenotazione WHERE data = ? AND id_campo = ? ORDER BY ora_inizio";

            statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(data));
            statement.setInt(2, idCampo);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Prenotazione prenotazione = mapResultSetToPrenotazione(resultSet);
                prenotazioni.add(prenotazione);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni per data e campo: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return prenotazioni;
    }

    /**
     * Crea una nuova prenotazione nel database.
     * 
     * @param prenotazione l'oggetto Prenotazione da inserire (l'ID verrà generato
     *                     automaticamente)
     * @return l'ID generato per la nuova prenotazione
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Integer createPrenotazione(Prenotazione prenotazione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Integer generatedId = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "INSERT INTO prenotazione (data, ora_inizio, id_campo, id_socio) " +
                    "VALUES (?, ?, ?, ?)";

            statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setDate(1, Date.valueOf(prenotazione.getData()));
            statement.setTime(2, Time.valueOf(prenotazione.getOraInizio()));
            statement.setInt(3, prenotazione.getCampo().getId());
            statement.setInt(4, prenotazione.getSocio().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creazione prenotazione fallita, nessuna riga inserita.");
            }

            // Recupera l'ID generato
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
                prenotazione.setId(generatedId);
            } else {
                throw new SQLException("Creazione prenotazione fallita, nessun ID ottenuto.");
            }

        } catch (SQLException e) {
            System.err.println("Errore durante la creazione della prenotazione: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return generatedId;
    }

    /**
     * Aggiorna una prenotazione esistente nel database.
     * 
     * @param prenotazione l'oggetto Prenotazione con i dati aggiornati
     * @return true se l'aggiornamento è andato a buon fine, false altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public boolean updatePrenotazione(Prenotazione prenotazione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "UPDATE prenotazione SET data = ?, ora_inizio = ?, " +
                    "id_campo = ?, id_socio = ? WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(prenotazione.getData()));
            statement.setTime(2, Time.valueOf(prenotazione.getOraInizio()));
            statement.setInt(3, prenotazione.getCampo().getId());
            statement.setInt(4, prenotazione.getSocio().getId());
            statement.setInt(5, prenotazione.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento della prenotazione: " + e.getMessage());
            throw e;

        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Elimina una prenotazione dal database.
     * 
     * @param id l'ID della prenotazione da eliminare
     * @return true se la cancellazione è andata a buon fine, false altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public boolean deletePrenotazione(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "DELETE FROM prenotazione WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante la cancellazione della prenotazione: " + e.getMessage());
            throw e;

        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Metodo helper per mappare un ResultSet a un oggetto Prenotazione.
     * QUESTO È IL PUNTO CHIAVE: Non fare cast da int a Campo o Utente!
     * Invece, recupera gli ID e poi usa i DAO per ottenere gli oggetti completi.
     * 
     * @param resultSet il ResultSet da mappare
     * @return l'oggetto Prenotazione
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    private Prenotazione mapResultSetToPrenotazione(ResultSet resultSet) throws SQLException {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(resultSet.getInt("id"));
        prenotazione.setData(resultSet.getDate("data").toLocalDate());
        prenotazione.setOraInizio(resultSet.getTime("ora_inizio").toLocalTime());

        // ✅ SOLUZIONE CORRETTA: Non fare cast da int a Campo!
        // Recupera l'ID e poi usa il DAO per ottenere l'oggetto completo
        int idCampo = resultSet.getInt("id_campo");
        Campo campo = campoDAO.getCampoById(idCampo);
        prenotazione.setCampo(campo);

        // Stesso procedimento per l'utente
        int idSocio = resultSet.getInt("id_socio");
        Utente socio = utenteDAO.getUtenteById(idSocio);
        prenotazione.setSocio(socio);

        return prenotazione;
    }

    /**
     * Metodo helper per chiudere le risorse JDBC.
     * 
     * @param resultSet  il ResultSet da chiudere
     * @param statement  il PreparedStatement da chiudere
     * @param connection la Connection da chiudere
     */
    private void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
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
}
