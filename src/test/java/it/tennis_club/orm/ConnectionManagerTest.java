package it.tennis_club.orm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test per verificare il corretto funzionamento di ConnectionManager.
 * Questi test richiedono che il database PostgreSQL sia in esecuzione
 * e configurato correttamente in db.properties.
 */
class ConnectionManagerTest {

    @Test
    @DisplayName("Verifica che la connessione al database venga stabilita correttamente")
    void testGetConnection() {
        Connection connection = null;

        try {
            // Tenta di ottenere una connessione
            connection = ConnectionManager.getConnection();

            // Verifica che la connessione non sia null
            assertNotNull(connection, "La connessione non dovrebbe essere null");

            // Verifica che la connessione sia valida (timeout di 2 secondi)
            assertTrue(connection.isValid(2), "La connessione dovrebbe essere valida");

            // Verifica che la connessione non sia chiusa
            assertFalse(connection.isClosed(), "La connessione non dovrebbe essere chiusa");

            System.out.println("✓ Connessione al database stabilita con successo!");

        } catch (SQLException e) {
            fail("Errore durante la connessione al database: " + e.getMessage());
        } finally {
            // Chiude la connessione
            ConnectionManager.closeConnection(connection);
        }
    }

    @Test
    @DisplayName("Verifica che la connessione venga chiusa correttamente")
    void testCloseConnection() {
        Connection connection = null;

        try {
            // Ottiene una connessione
            connection = ConnectionManager.getConnection();
            assertNotNull(connection, "La connessione non dovrebbe essere null");

            // Chiude la connessione
            ConnectionManager.closeConnection(connection);

            // Verifica che la connessione sia effettivamente chiusa
            assertTrue(connection.isClosed(), "La connessione dovrebbe essere chiusa");

            System.out.println("✓ Connessione chiusa correttamente!");

        } catch (SQLException e) {
            fail("Errore durante il test di chiusura: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Verifica che closeConnection gestisca correttamente una connessione null")
    void testCloseNullConnection() {
        // Non dovrebbe lanciare eccezioni
        assertDoesNotThrow(() -> {
            ConnectionManager.closeConnection(null);
            System.out.println("✓ Gestione corretta di connessione null");
        });
    }

    @Test
    @DisplayName("Verifica che sia possibile ottenere multiple connessioni")
    void testMultipleConnections() {
        Connection conn1 = null;
        Connection conn2 = null;

        try {
            // Ottiene due connessioni separate
            conn1 = ConnectionManager.getConnection();
            conn2 = ConnectionManager.getConnection();

            assertNotNull(conn1, "La prima connessione non dovrebbe essere null");
            assertNotNull(conn2, "La seconda connessione non dovrebbe essere null");

            // Verifica che siano connessioni diverse
            assertNotSame(conn1, conn2, "Le connessioni dovrebbero essere oggetti diversi");

            // Verifica che entrambe siano valide
            assertTrue(conn1.isValid(2), "La prima connessione dovrebbe essere valida");
            assertTrue(conn2.isValid(2), "La seconda connessione dovrebbe essere valida");

            System.out.println("Multiple connessioni gestite correttamente!");

        } catch (SQLException e) {
            fail("Errore durante il test di connessioni multiple: " + e.getMessage());
        } finally {
            ConnectionManager.closeConnection(conn1);
            ConnectionManager.closeConnection(conn2);
        }
    }
}
