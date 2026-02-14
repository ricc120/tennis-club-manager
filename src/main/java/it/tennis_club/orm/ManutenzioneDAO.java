package it.tennis_club.orm;

import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle manutenzioni nel database.
 */
@Repository
public class ManutenzioneDAO {

    private final CampoDAO campoDAO;
    private final UtenteDAO utenteDAO;

    /**
     * Costruttore predefinito per compatibilità con CLI e test.
     */
    public ManutenzioneDAO() {
        this.campoDAO = new CampoDAO();
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Costruttore per Dependency Injection di Spring.
     */
    @Autowired
    public ManutenzioneDAO(CampoDAO campoDAO, UtenteDAO utenteDAO) {
        this.campoDAO = campoDAO;
        this.utenteDAO = utenteDAO;
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
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creazione manutenzione fallita, nessun ID ottenuto.");
            }

        } catch (SQLException e) {
            System.err.println("Errore durante la creazione della manutenzione: " + e.getMessage());
            throw e;

        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
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

    /**
     * Recupera tutte le manutenzioni di un campo specifico.
     * 
     * @param idCampo l'ID del campo
     * @return lista delle manutenzioni del campo
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public List<Manutenzione> getManutenzioniByIdCampo(Integer idCampo) throws SQLException {
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
                Manutenzione manutenzione = new Manutenzione();
                manutenzione.setId(resultSet.getInt("id"));

                // Recupera il campo
                Campo campo = campoDAO.getCampoById(resultSet.getInt("id_campo"));
                manutenzione.setCampo(campo);

                // Recupera il manutentore
                Utente manutentore = utenteDAO.getUtenteById(resultSet.getInt("id_manutentore"));
                manutenzione.setManutentore(manutentore);

                manutenzione.setDataInizio(resultSet.getDate("data_inizio").toLocalDate());

                Date dataFine = resultSet.getDate("data_fine");
                if (dataFine != null) {
                    manutenzione.setDataFine(dataFine.toLocalDate());
                }

                manutenzione.setDescrizione(resultSet.getString("descrizione"));
                manutenzione.setStato(Manutenzione.Stato.valueOf(resultSet.getString("stato")));

                manutenzioni.add(manutenzione);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle manutenzioni: " + e.getMessage());
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

        return manutenzioni;
    }

    /**
     * Aggiorna lo stato di una manutenzione.
     * 
     * @param idManutenzione l'ID della manutenzione
     * @param nuovoStato     il nuovo stato
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public boolean updateStatoManutenzione(Integer idManutenzione, Manutenzione.Stato nuovoStato) throws SQLException {
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
                Manutenzione manutenzione = new Manutenzione();
                manutenzione.setId(resultSet.getInt("id"));

                // Recupera il campo
                Campo campo = campoDAO.getCampoById(resultSet.getInt("id_campo"));
                manutenzione.setCampo(campo);

                // Recupera il manutentore
                Utente manutentore = utenteDAO.getUtenteById(resultSet.getInt("id_manutentore"));
                manutenzione.setManutentore(manutentore);

                manutenzione.setDataInizio(resultSet.getDate("data_inizio").toLocalDate());

                Date dataFine = resultSet.getDate("data_fine");
                if (dataFine != null) {
                    manutenzione.setDataFine(dataFine.toLocalDate());
                }

                manutenzione.setDescrizione(resultSet.getString("descrizione"));
                manutenzione.setStato(Manutenzione.Stato.valueOf(resultSet.getString("stato")));

                manutenzioni.add(manutenzione);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero di tutte le manutenzioni: " + e.getMessage());
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
            if (statement != null)
                statement.close();
            ConnectionManager.closeConnection(connection);
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

            while (resultSet.next()) {
                manutenzione = new Manutenzione();
                manutenzione.setId(resultSet.getInt("id"));

                Campo campo = campoDAO.getCampoById(resultSet.getInt("id_campo"));
                manutenzione.setCampo(campo);

                Utente utente = utenteDAO.getUtenteById(resultSet.getInt("id_manutentore"));
                manutenzione.setManutentore(utente);

                manutenzione.setDataInizio(resultSet.getDate("data_inizio").toLocalDate());
                Date dataFine = resultSet.getDate("data_fine");
                if (dataFine != null) {
                    manutenzione.setDataFine(dataFine.toLocalDate());
                }

                manutenzione.setDescrizione(resultSet.getString("descrizione"));
                manutenzione.setStato(Manutenzione.Stato.valueOf(resultSet.getString("stato")));

            }
        } catch (Exception e) {
            System.out.println("Errore durante il recupero della manutenzione: " + e.getMessage());
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
        return manutenzione;

    }
}
