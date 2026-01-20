package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.LezioneDAO;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class AccademiaService {

    private final LezioneDAO lezioneDAO;
    private final PrenotazioneService prenotazioneService;

    /**
     * Costruttore che inzializza il DAO
     */
    public AccademiaService() {
        this.lezioneDAO = new LezioneDAO();
        this.prenotazioneService = new PrenotazioneService();
    }

    /**
     * Crea una nuova lezione e la prenotazione associata.
     * Il metodo crea automaticamente una prenotazione per il campo specificato
     * e poi crea la lezione collegata. Il feedback potrà essere inserito
     * successivamente
     * tramite il metodo inserisciFeedback().
     * 
     * @param data        la data della lezione
     * @param ora         l'ora di inizio della lezione
     * @param campo       il campo da prenotare per la lezione
     * @param maestro     il maestro che terrà la lezione
     * @param descrizione la descrizione della lezione (es. "Perfezionamento del
     *                    rovescio")
     * @return l'ID della lezione creata
     * @throws AccademiaException    se il maestro non è valido o si verifica un
     *                               errore
     * @throws PrenotazioneException se la prenotazione non può essere creata
     */
    public Integer createLezione(LocalDate data, LocalTime ora, Campo campo, Utente maestro, String descrizione)
            throws AccademiaException, PrenotazioneException {

        // Validazione input PRIMA di creare la prenotazione
        if (maestro == null) {
            throw new AccademiaException("Il maestro non può essere null");
        }

        if (maestro.getRuolo() != Ruolo.MAESTRO) {
            throw new AccademiaException("Solo i maestri possono creare una lezione");
        }

        if (data == null || ora == null || campo == null) {
            throw new AccademiaException("Data, ora e campo sono obbligatori");
        }

        try {
            // Crea la prenotazione per la lezione
            Integer idPrenotazione = prenotazioneService.creaPrenotazione(data, ora, campo, maestro);

            if (idPrenotazione == null) {
                throw new AccademiaException("Errore durante la creazione della prenotazione");
            }

            // Recupera l'oggetto Prenotazione completo
            Prenotazione prenotazione = prenotazioneService.getPrenotazioneById(idPrenotazione);

            // Crea la lezione
            Lezione lezione = new Lezione();
            lezione.setPrenotazione(prenotazione);
            lezione.setMaestro(maestro);
            lezione.setDescrizione(descrizione);
            // Il feedback non viene impostato alla creazione

            return lezioneDAO.createLezione(lezione);

        } catch (SQLException e) {
            throw new AccademiaException("Errore durante la creazione della lezione: " + e.getMessage(), e);
        }
    }

    /**
     * Inserisce o aggiorna il feedback per una lezione esistente.
     * 
     * @param idLezione l'ID della lezione
     * @param feedback  il feedback da inserire
     * @return true se l'aggiornamento è andato a buon fine
     * @throws AccademiaException se la lezione non esiste o si verifica un errore
     */
    public boolean inserisciFeedback(Integer idLezione, String feedback) throws AccademiaException {

        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("L'ID della lezione non può essere null o negativo");
        }

        try {
            Lezione lezione = lezioneDAO.getLezioneById(idLezione);
            if (lezione == null) {
                throw new AccademiaException("Lezione con ID " + idLezione + " non trovata");
            }
            lezione.setFeedback(feedback);
            return lezioneDAO.updateLezione(lezione);

        } catch (SQLException e) {
            throw new AccademiaException("Errore durante l'inserimento del feedback: " + e.getMessage(), e);
        }
    }

    /**
     * Inserisce o aggiorna la descrizione per una lezione esistente.
     * 
     * @param idLezione   l'ID della lezione
     * @param descrizione la descrizione da inserire
     * @return true se l'aggiornamento è andato a buon fine
     * @throws AccademiaException se la lezione non esiste o si verifica un errore
     */
    public boolean inserisciDescrizione(Integer idLezione, String descrizione) throws AccademiaException {

        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("L'ID della lezione non può essere nullo o negativo");
        }

        try {
            Lezione lezione = lezioneDAO.getLezioneById(idLezione);
            if (lezione == null) {
                throw new AccademiaException("Lezione con ID " + idLezione + " non trovata");
            }
            lezione.setDescrizione(descrizione);
            return lezioneDAO.updateLezione(lezione);
        } catch (SQLException e) {
            throw new AccademiaException(
                    "Errore durante l'inserimento della descrizione: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una lezione dal database.
     * 
     * @param idLezione l'ID della lezione da eliminare
     * @return true se la cancellazione è andata a buon fine
     * @throws AccademiaException se la lezione non esiste o si verifica un errore
     */
    public boolean deleteLezione(Integer idLezione) throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("L'ID della lezione non può essere nullo o negativo");
        }

        try {
            return lezioneDAO.deleteLezione(idLezione);
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante la cancellazione della lezione: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera una lezione specifica tramite il suo ID.
     * 
     * @param idLezione l'ID della lezione
     * @return la lezione richiesta
     * @throws AccademiaException se la lezione non esiste o si verifica un errore
     */
    public Lezione getLezioneById(Integer idLezione) throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("L'ID della lezione non può essere null o negativo");
        }

        try {
            Lezione lezione = lezioneDAO.getLezioneById(idLezione);
            if (lezione == null) {
                throw new AccademiaException("Lezione con ID " + idLezione + " non trovata");
            }
            return lezione;
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero della lezione: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le lezioni dal database.
     * 
     * @return lista di tutte le lezioni
     * @throws AccademiaException se si verifica un errore durante il recupero
     */
    public List<Lezione> getAllLezioni() throws AccademiaException {
        try {
            return lezioneDAO.getAllLezioni();
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero di tutte le lezioni: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le lezioni tenute da un maestro specifico.
     * 
     * @param maestro il maestro di cui recuperare le lezioni
     * @return lista di lezioni del maestro
     * @throws AccademiaException se il maestro non è valido o si verifica un errore
     */
    public List<Lezione> getLezioneByMaestro(Utente maestro) throws AccademiaException {

        if (maestro == null) {
            throw new AccademiaException("Il maestro non può essere null");
        }

        if (maestro.getId() == null) {
            throw new AccademiaException("L'ID del maestro non può essere null");
        }

        if (maestro.getRuolo() != Ruolo.MAESTRO) {
            throw new AccademiaException("Solo i maestri possono recuperare le proprie lezioni");
        }

        try {
            return lezioneDAO.getLezioniByMaestro(maestro.getId());

        } catch (SQLException e) {
            throw new AccademiaException(
                    "Errore durante il recupero delle lezioni per il maestro " + maestro.getNome() + ": "
                            + e.getMessage(),
                    e);
        }
    }

    /**
     * Recupera la lezione associata a una specifica prenotazione.
     * 
     * @param prenotazione la prenotazione di cui recuperare la lezione
     * @return la lezione associata alla prenotazione
     * @throws AccademiaException se la prenotazione non è valida o si verifica un
     *                            errore
     */
    public Lezione getLezioneByPrenotazione(Prenotazione prenotazione) throws AccademiaException {

        if (prenotazione == null) {
            throw new AccademiaException("La prenotazione non può essere null");
        }

        if (prenotazione.getId() == null) {
            throw new AccademiaException("L'ID della prenotazione non può essere null");
        }

        try {
            return lezioneDAO.getLezioneByPrenotazione(prenotazione.getId());
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero della lezione per la prenotazione "
                    + prenotazione.getId() + ": " + e.getMessage(), e);
        }
    }
}
