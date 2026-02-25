package it.tennis_club.orm;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.flywaydb.core.Flyway;

/**
 * Gestisce la connessione al database PostgreSQL.
 * Priorità di configurazione:
 * 1. Variabili d'ambiente (DB_URL, DB_USER, DB_PASSWORD) - per Docker/Cloud
 * 2. File db.properties - per sviluppo locale
 * 3. Valori di default hardcoded - come ultimo fallback
 *
 * Integra Flyway per le migrazioni automatiche dello schema.
 */
public class ConnectionManager {

    // Valori di default (ultimo fallback)
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/tennis_club";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    private static final Properties properties = new Properties();
    private static volatile boolean flywayExecuted = false;

    static {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Restituisce la URL di connessione al database.
     * Priorità: variabile d'ambiente > db.properties > default.
     */
    private static String getDbUrl() {
        String url = System.getenv("DB_URL");
        if (url != null && !url.isEmpty())
            return url;
        String prop = properties.getProperty("db.url");
        if (prop != null && !prop.isEmpty())
            return prop;
        return DEFAULT_URL;
    }

    /**
     * Restituisce l'utente di connessione al database.
     * Priorità: variabile d'ambiente > db.properties > default.
     */
    private static String getDbUser() {
        String user = System.getenv("DB_USER");
        if (user != null && !user.isEmpty())
            return user;
        String prop = properties.getProperty("db.user");
        if (prop != null && !prop.isEmpty())
            return prop;
        return DEFAULT_USER;
    }

    /**
     * Restituisce la password di connessione al database.
     * Priorità: variabile d'ambiente > db.properties > default.
     */
    private static String getDbPassword() {
        String password = System.getenv("DB_PASSWORD");
        if (password != null && !password.isEmpty())
            return password;
        String prop = properties.getProperty("db.password");
        if (prop != null && !prop.isEmpty())
            return prop;
        return DEFAULT_PASSWORD;
    }

    /**
     * Esegue le migrazioni Flyway una sola volta all'avvio.
     * I file di migrazione devono trovarsi in src/main/resources/db/migration.
     */
    public static synchronized void runFlywayMigrations() {
        if (!flywayExecuted) {
            System.out.println("[Flyway] Avvio migrazioni database...");
            Flyway flyway = Flyway.configure()
                    .dataSource(getDbUrl(), getDbUser(), getDbPassword())
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();
            flyway.repair();
            flyway.migrate();
            flywayExecuted = true;
            System.out.println("[Flyway] Migrazioni completate con successo.");
        }
    }

    /**
     * Ottiene una nuova connessione al database.
     * Alla prima chiamata, esegue automaticamente le migrazioni Flyway.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL non trovato", e);
        }

        // Esegui le migrazioni Flyway alla prima connessione
        runFlywayMigrations();

        return DriverManager.getConnection(getDbUrl(), getDbUser(), getDbPassword());
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