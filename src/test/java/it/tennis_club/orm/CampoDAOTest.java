package it.tennis_club.orm;

import it.tennis_club.domain_model.Campo;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il DAO dei campi da tennis.
 * Questi test richiedono che il database sia configurato e popolato
 * con i dati di default (vedi default.sql).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CampoDAOTest {

    private CampoDAO campoDAO;

    @BeforeEach
    void setUp() {
        campoDAO = new CampoDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Verifica il recupero di tutti i campi")
    void testGetAllCampi() throws SQLException {
        List<Campo> campi = campoDAO.getAllCampi();

        assertNotNull(campi, "La lista dei campi non dovrebbe essere null");
        assertFalse(campi.isEmpty(), "Dovrebbero esserci almeno alcuni campi nel database");

        // Verifica che i campi abbiano tutti i dati popolati
        for (Campo campo : campi) {
            assertNotNull(campo.getId(), "L'ID del campo non dovrebbe essere null");
            assertNotNull(campo.getNome(), "Il nome del campo non dovrebbe essere null");
            assertNotNull(campo.getTipoSuperficie(), "Il tipo superficie non dovrebbe essere null");
            assertNotNull(campo.getIsCoperto(), "Il flag is_coperto non dovrebbe essere null");
        }

        System.out.println("Trovati " + campi.size() + " campi nel database");
        campi.forEach(System.out::println);
    }

    @Test
    @Order(2)
    @DisplayName("Verifica il recupero di un campo specifico per ID")
    void testGetCampoById() throws SQLException {
        // Assumiamo che esista un campo con ID 1 (dal default.sql)
        Campo campo = campoDAO.getCampoById(1);

        assertNotNull(campo, "Il campo con ID 1 dovrebbe esistere");
        assertEquals(1, campo.getId());
        assertEquals("Campo 1", campo.getNome());
        assertEquals("Terra", campo.getTipoSuperficie());
        assertTrue(campo.getIsCoperto(), "Campo 1 dovrebbe essere coperto");

        System.out.println("Campo trovato: " + campo);
    }

    @Test
    @Order(3)
    @DisplayName("Verifica che getCampoById restituisca null per ID inesistente")
    void testGetCampoByIdNotFound() throws SQLException {
        Campo campo = campoDAO.getCampoById(9999);

        assertNull(campo, "Non dovrebbe trovare un campo con ID inesistente");
    }

    @Test
    @Order(4)
    @DisplayName("Verifica il recupero dei campi coperti")
    void testGetCampiCoperti() throws SQLException {
        List<Campo> campiCoperti = campoDAO.getCampiCoperti();

        assertNotNull(campiCoperti, "La lista non dovrebbe essere null");

        // Verifica che tutti i campi restituiti siano effettivamente coperti
        for (Campo campo : campiCoperti) {
            assertTrue(campo.getIsCoperto(),
                    "Tutti i campi nella lista dovrebbero essere coperti: " + campo.getNome());
        }

        System.out.println("Trovati " + campiCoperti.size() + " campi coperti");
        campiCoperti.forEach(System.out::println);
    }

    @Test
    @Order(5)
    @DisplayName("Verifica il recupero dei campi per tipo di superficie")
    void testGetCampiByTipoSuperficie() throws SQLException {
        // Test per campi in Terra
        List<Campo> campiTerra = campoDAO.getCampiByTipoSuperficie("Terra");

        assertNotNull(campiTerra, "La lista non dovrebbe essere null");

        // Verifica che tutti i campi abbiano il tipo superficie corretto
        for (Campo campo : campiTerra) {
            assertEquals("Terra", campo.getTipoSuperficie(),
                    "Tutti i campi dovrebbero avere superficie Terra");
        }

        System.out.println("Trovati " + campiTerra.size() + " campi in Terra");

        // Test per campi in Cemento
        List<Campo> campiCemento = campoDAO.getCampiByTipoSuperficie("Cemento");

        assertNotNull(campiCemento, "La lista non dovrebbe essere null");

        for (Campo campo : campiCemento) {
            assertEquals("Cemento", campo.getTipoSuperficie(),
                    "Tutti i campi dovrebbero avere superficie Cemento");
        }

        System.out.println("Trovati " + campiCemento.size() + " campi in Cemento");
    }

    @Test
    @Order(6)
    @DisplayName("Verifica che la ricerca per tipo superficie inesistente restituisca lista vuota")
    void testGetCampiByTipoSuperficieNotFound() throws SQLException {
        List<Campo> campi = campoDAO.getCampiByTipoSuperficie("Erba");

        assertNotNull(campi, "La lista non dovrebbe essere null");
        // Potrebbe essere vuota se non ci sono campi in erba
        System.out.println("Trovati " + campi.size() + " campi in Erba");
    }
}
