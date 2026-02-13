package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.PrenotazioneDAO;
import it.tennis_club.orm.ManutenzioneDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Servizio per la gestione della business logic delle prenotazioni.
 * Questo layer si occupa di validare i dati e gestire le regole di business
 * prima di delegare le operazioni al DAO.
 */
public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final ManutenzioneDAO manutenzioneDAO;

    /**
     * Costruttore che inizializza il DAO.
     */
    public PrenotazioneService() {
        this.prenotazioneDAO = new PrenotazioneDAO();
        this.manutenzioneDAO = new ManutenzioneDAO();
    }

    /**
     * Crea una nuova prenotazione con validazione delle regole di business.
     * 
     * @param data      la data della prenotazione
     * @param oraInizio l'ora di inizio
     * @param campo     il campo da prenotare
     * @param socio     il socio che effettua la prenotazione
     * @return l'ID della prenotazione creata
     * @throws PrenotazioneException se la prenotazione non è valida o il campo non
     *                               è disponibile
     */
    public Integer creaPrenotazione(LocalDate data, LocalTime oraInizio, Campo campo, Utente socio)
            throws PrenotazioneException {

        // Validazione input
        if (data == null || oraInizio == null || campo == null || socio == null) {
            throw new PrenotazioneException("Tutti i campi sono obbligatori per creare una prenotazione");
        }

        // Validazione data (non può essere nel passato)
        if (data.isBefore(LocalDate.now())) {
            throw new PrenotazioneException("Non è possibile prenotare un campo per una data passata");
        }

        // Validazione orario (esempio: dalle 8:00 alle 22:00)
        if (oraInizio.isBefore(LocalTime.of(8, 0)) || oraInizio.isAfter(LocalTime.of(22, 0))) {
            throw new PrenotazioneException("L'orario di prenotazione deve essere tra le 8:00 e le 22:00");
        }

        // Validazione orario passato (solo se la data è oggi)
        if (data.equals(LocalDate.now()) && oraInizio.isBefore(LocalTime.now())) {
            throw new PrenotazioneException("Non è possibile prenotare un campo per un orario passato");
        }

        try {
            // Validazione manutenzione esistente in quella data
            if (manutenzioneDAO.getManutenzioneAttivaByDataAndCampo(data, campo.getId()) != null) {
                throw new PrenotazioneException(
                        "Non è possibile prenotare il campo perché è in corso una manutenzione");
            }
            // Verifica disponibilità del campo
            List<Prenotazione> prenotazioniEsistenti = prenotazioneDAO.getPrenotazioniByDataAndCampo(data,
                    campo.getId());

            // Controlla se c'è già una prenotazione alla stessa ora
            for (Prenotazione p : prenotazioniEsistenti) {
                if (p.getOraInizio().equals(oraInizio)) {
                    throw new PrenotazioneException(
                            String.format("Il campo %s è già prenotato per il %s alle ore %s",
                                    campo.getNome(), data, oraInizio));
                }
            }

            // Crea l'oggetto Prenotazione
            Prenotazione nuovaPrenotazione = new Prenotazione();
            nuovaPrenotazione.setData(data);
            nuovaPrenotazione.setOraInizio(oraInizio);
            nuovaPrenotazione.setCampo(campo);
            nuovaPrenotazione.setSocio(socio);

            // Salva nel database
            return prenotazioneDAO.createPrenotazione(nuovaPrenotazione);

        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante la creazione della prenotazione: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le prenotazioni per una data specifica.
     * 
     * @param data la data
     * @return lista di prenotazioni
     * @throws PrenotazioneException in caso di errore
     */
    public List<Prenotazione> getPrenotazioniPerData(LocalDate data) throws PrenotazioneException {
        if (data == null) {
            throw new PrenotazioneException("La data non può essere null");
        }

        if (data.isBefore(LocalDate.now())) {
            throw new PrenotazioneException("Non è possibile ricercare la prenotazione per una data passata");
        }

        try {
            return prenotazioneDAO.getPrenotazioniByData(data);
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante il recupero delle prenotazioni: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le prenotazioni per un campo specifico.
     * 
     * @param campo il campo
     * @return lista di prenotazioni
     * @throws PrenotazioneException in caso di errore
     */
    public List<Prenotazione> getPrenotazioniPerCampo(Campo campo) throws PrenotazioneException {
        if (campo == null || campo.getId() == null) {
            throw new PrenotazioneException("Il campo non può essere null");
        }

        try {
            return prenotazioneDAO.getPrenotazioniByCampo(campo.getId());
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante il recupero delle prenotazioni: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le prenotazioni per un socio specifico.
     * 
     * @param socio il socio
     * @return lista di prenotazioni
     * @throws PrenotazioneException in caso di errore
     */
    public List<Prenotazione> getPrenotazioniPerSocio(Utente socio) throws PrenotazioneException {
        if (socio == null || socio.getId() == null) {
            throw new PrenotazioneException("Il socio non può essere null");
        }

        try {
            return prenotazioneDAO.getPrenotazioniBySocio(socio.getId());
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante il recupero delle prenotazioni: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera le prenotazioni per una data e un campo specifici.
     * 
     * @param data  la data
     * @param campo il campo
     * @return lista di prenotazioni
     * @throws PrenotazioneException in caso di errore
     */
    public List<Prenotazione> getPrenotazioniPerDataECampo(LocalDate data, Campo campo)
            throws PrenotazioneException {

        if (data == null || campo == null || campo.getId() == null) {
            throw new PrenotazioneException("Data e campo non possono essere null");
        }

        if (data.isBefore(LocalDate.now())) {
            throw new PrenotazioneException("Non è possibile ricercare la prenotazione per una data passata");
        }

        try {
            return prenotazioneDAO.getPrenotazioniByDataAndCampo(data, campo.getId());
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante il recupero delle prenotazioni: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica se un campo è disponibile in una data e ora specifiche.
     * 
     * @param data      la data
     * @param oraInizio l'ora di inizio
     * @param campo     il campo
     * @return true se il campo è disponibile, false altrimenti
     * @throws PrenotazioneException in caso di errore
     */
    public boolean isCampoDisponibile(LocalDate data, LocalTime oraInizio, Campo campo)
            throws PrenotazioneException {

        if (data == null || oraInizio == null || campo == null || campo.getId() == null) {
            throw new PrenotazioneException("Tutti i parametri sono obbligatori");
        }

        if (data.isBefore(LocalDate.now())) {
            throw new PrenotazioneException("Non è possibile ricercare la prenotazione per una data passata");
        }

        if (data.isEqual(LocalDate.now()) && oraInizio.isBefore(LocalTime.now())) {
            throw new PrenotazioneException("Non è possibile ricercare la prenotazione per un orario passato");
        }

        try {
            List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByDataAndCampo(data, campo.getId());

            // Controlla se esiste già una prenotazione alla stessa ora
            for (Prenotazione p : prenotazioni) {
                if (p.getOraInizio().equals(oraInizio)) {
                    return false; // Campo non disponibile
                }
            }

            return true; // Campo disponibile
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante la verifica della disponibilità: " + e.getMessage(), e);
        }
    }

    /**
     * Cancella una prenotazione.
     * 
     * @param idPrenotazione l'ID della prenotazione da cancellare
     * @return true se la cancellazione è andata a buon fine
     * @throws PrenotazioneException in caso di errore
     */
    public boolean cancellaPrenotazione(Integer idPrenotazione) throws PrenotazioneException {
        if (idPrenotazione == null) {
            throw new PrenotazioneException("L'ID della prenotazione non può essere null");
        }

        try {
            // Verifica che la prenotazione esista
            Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(idPrenotazione);
            if (prenotazione == null) {
                throw new PrenotazioneException("Prenotazione non trovata con ID: " + idPrenotazione);
            }

            // Cancella la prenotazione
            return prenotazioneDAO.deletePrenotazione(idPrenotazione);
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante la cancellazione della prenotazione: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera una prenotazione specifica tramite il suo ID.
     * 
     * @param idPrenotazione l'ID della prenotazione
     * @return la prenotazione richiesta
     * @throws PrenotazioneException se la prenotazione non esiste o si verifica un
     *                               errore
     */
    public Prenotazione getPrenotazionePerId(Integer idPrenotazione) throws PrenotazioneException {
        if (idPrenotazione == null || idPrenotazione <= 0) {
            throw new PrenotazioneException("L'ID della prenotazione non può essere null o negativo");
        }

        try {
            Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(idPrenotazione);
            if (prenotazione == null) {
                throw new PrenotazioneException("Prenotazione con ID " + idPrenotazione + " non trovata");
            }
            return prenotazione;
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante il recupero della prenotazione: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le prenotazioni.
     * 
     * @return lista di tutte le prenotazioni
     * @throws PrenotazioneException in caso di errore
     */
    public List<Prenotazione> getPrenotazioni() throws PrenotazioneException {
        try {
            return prenotazioneDAO.getAllPrenotazioni();
        } catch (SQLException e) {
            throw new PrenotazioneException("Errore durante il recupero di tutte le prenotazioni: " + e.getMessage(),
                    e);
        }
    }
}
