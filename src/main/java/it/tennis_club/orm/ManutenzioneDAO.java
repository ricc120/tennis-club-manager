package it.tennis_club.orm;

import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Manutenzione.Stato;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle manutenzioni nel database.
 */
public class ManutenzioneDAO {

    private final CampoDAO campoDAO;
    private final UtenteDAO utenteDAO;

    public ManutenzioneDAO() {
        this.campoDAO = new CampoDAO();
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Crea una nuova manutenzione nel database.
     * 
     * @param manutenzione l'oggetto Manutenzione da inserire
     * @return l'ID della manutenzione creata
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Integer createManutenzione(Manutenzione manutenzione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "INSERT INTO manutenzione (id_campo, id_manutentore, data_inizio, data_fine, descrizione, stato) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, manutenzione.getCampo().getId());
            statement.setInt(2, manutenzione.getManutentore().getId());
            statement.setDate(3, Date.valueOf(manutenzione.getDataInizio()));

            if (manutenzione.getDataFine() != null) {
                statement.setDate(4, Date.valueOf(manutenzione.getDataFine()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, manutenzione.getDescrizione());
            statement.setString(6, manutenzione.getStato().name());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creazione manutenzione fallita, nessuna riga inserita.");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Integer id = generatedKeys.getInt(1);
                manutenzione.setId(id);
                return id;
            } else {
                throw new SQLException("Creazione manutenzione fallita, nessun ID ottenuto.");
            }

        } catch (SQLException e) {
            System.err.println("Errore durante la creazione della manutenzione: " + e.getMessage());
            throw e;

        } finally {
            closeResources(generatedKeys, statement, connection);
        }
    }

    /**
     * Recupera tutte le manutenzioni di un campo specifico.
     * 
     * @param idCampo l'ID del campo
     * @return lista delle manutenzioni del campo
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Manutenzione> getManutenzioniByCampo(Integer idCampo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Manutenzione> manutenzioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, id_campo, id_manutentore, data_inizio, data_fine, descrizione, stato " +
                    "FROM manutenzione WHERE id_campo = ? ORDER BY data_inizio DESC";

            statement = connection.prepareStatement(query);
            statement.setInt(1, idCampo);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                manutenzioni.add(mapResultSetToManutenzione(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle manutenzioni: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return manutenzioni;
    }

    /**
     * Aggiorna lo stato di una manutenzione.
     * 
     * @param idManutenzione l'ID della manutenzione
     * @param nuovoStato     il nuovo stato
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public boolean updateStatoManutenzione(Integer idManutenzione, Stato nuovoStato) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "UPDATE manutenzione SET stato = ? WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, nuovoStato.name());
            statement.setInt(2, idManutenzione);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento dello stato: " + e.getMessage());
            throw e;

        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Completa una manutenzione impostando la data di fine.
     * 
     * @param idManutenzione l'ID della manutenzione
     * @param dataFine       la data di completamento
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void completaManutenzione(Integer idManutenzione, LocalDate dataFine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();

            String query = "UPDATE manutenzione SET data_fine = ?, stato = 'COMPLETATA' WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(dataFine));
            statement.setInt(2, idManutenzione);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore durante il completamento della manutenzione: " + e.getMessage());
            throw e;

        } finally {
            closeResources(null, statement, connection);
        }
    }

    /**
     * Recupera tutte le manutenzioni presenti nel database.
     * 
     * @return lista di tutte le manutenzioni
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Manutenzione> getAllManutenzioni() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Manutenzione> manutenzioni = new ArrayList<>();

        try {
            connection = ConnectionManager.getConnection();

            String query = "SELECT id, id_campo, id_manutentore, data_inizio, data_fine, descrizione, stato " +
                    "FROM manutenzione ORDER BY data_inizio DESC";

            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                manutenzioni.add(mapResultSetToManutenzione(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero di tutte le manutenzioni: " + e.getMessage());
            throw e;

        } finally {
            closeResources(resultSet, statement, connection);
        }

        return manutenzioni;
    }

    public boolean deleteManutenzioni(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "DELETE FROM manutenzione WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione della manutenzione: " + e.getMessage());
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    public Manutenzione getManutenzioneById(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Manutenzione manutenzione = null;

        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT id, id_campo, id_manutentore, data_inizio, data_fine, descrizione, stato " +
                    " FROM manutenzione WHERE id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                manutenzione = mapResultSetToManutenzione(resultSet);
            }
        } catch (Exception e) {
            System.out.println("Errore durante il recupero della manutenzione: " + e.getMessage());
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return manutenzione;
    }

    /**
     * Recupera una manutenzione bloccante per un campo specifico che copre una
     * data.
     * Blocca le prenotazioni per manutenzioni IN_CORSO o COMPLETATA.
     * Permette le prenotazioni se la manutenzione è ANNULLATA.
     * 
     * @param data    la data da verificare
     * @param idCampo l'ID del campo
     * @return la manutenzione bloccante se presente, null altrimenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public Manutenzione getManutenzioneAttivaByDataAndCampo(LocalDate data, Integer idCampo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Manutenzione manutenzione = null;

        try {
            connection = ConnectionManager.getConnection();

            // Query: cerca manutenzioni IN_CORSO o COMPLETATA che coprono la data
            // richiesta:
            // - Se data_fine è NULL, blocca solo data_inizio (singolo giorno)
            // - Se data_fine è definita, blocca il range [data_inizio, data_fine]
            // - Ignora manutenzioni ANNULLATA (permettono prenotazioni)
            String query = "SELECT id, id_campo, id_manutentore, data_inizio, data_fine, descrizione, stato " +
                    " FROM manutenzione WHERE id_campo = ? AND stato IN ('IN_CORSO', 'COMPLETATA') " +
                    " AND ((data_fine IS NULL AND data_inizio = ?) " +
                    "      OR (data_fine IS NOT NULL AND ? >= data_inizio AND ? <= data_fine))";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idCampo);
            statement.setDate(2, Date.valueOf(data));
            statement.setDate(3, Date.valueOf(data));
            statement.setDate(4, Date.valueOf(data));

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                manutenzione = mapResultSetToManutenzione(resultSet);
            }
        } catch (Exception e) {
            System.out.println("Errore durante il recupero della manutenzione: " + e.getMessage());
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return manutenzione;

    }

    /**
     * Metodo helper per mappare un ResultSet a un oggetto Manutenzione.
     * 
     * @param resultSet il ResultSet da mappare
     * @return l'oggetto Manutenzione
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    private Manutenzione mapResultSetToManutenzione(ResultSet resultSet) throws SQLException {
        Manutenzione manutenzione = new Manutenzione();
        manutenzione.setId(resultSet.getInt("id"));

        // Recupero oggetti completi tramite gli ID
        int idCampo = resultSet.getInt("id_campo");
        manutenzione.setCampo(campoDAO.getCampoById(idCampo));

        int idManutentore = resultSet.getInt("id_manutentore");
        manutenzione.setManutentore(utenteDAO.getUtenteById(idManutentore));

        manutenzione.setDataInizio(resultSet.getDate("data_inizio").toLocalDate());

        Date dataFine = resultSet.getDate("data_fine");
        if (dataFine != null) {
            manutenzione.setDataFine(dataFine.toLocalDate());
        }

        manutenzione.setDescrizione(resultSet.getString("descrizione"));
        manutenzione.setStato(Manutenzione.Stato.valueOf(resultSet.getString("stato")));

        return manutenzione;
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
