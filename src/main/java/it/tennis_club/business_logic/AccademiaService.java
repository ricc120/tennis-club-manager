package it.tennis_club.business_logic;

import it.tennis_club.domain_model.AllievoLezione;
import it.tennis_club.domain_model.Lezione;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.LezioneDAO;
import it.tennis_club.orm.AllievoLezioneDAO;
import it.tennis_club.orm.UtenteDAO;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Servizio per la gestione delle attività dell'accademia di tennis.
 * Gestisce la creazione di lezioni, l'iscrizione degli allievi,
 * le presenze e i feedback didattici.
 */
public class AccademiaService {

    private final LezioneDAO lezioneDAO;
    private final PrenotazioneService prenotazioneService;
    private final AllievoLezioneDAO allievoLezioneDAO;
    private final UtenteDAO utenteDAO;
    private static final int MAX_ALLIEVI_PER_LEZIONE = 8;

    /**
     * Costruttore che inizializza i DAO e i servizi necessari.
     */
    public AccademiaService() {
        this.lezioneDAO = new LezioneDAO();
        this.prenotazioneService = new PrenotazioneService();
        this.allievoLezioneDAO = new AllievoLezioneDAO();
        this.utenteDAO = new UtenteDAO();
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
    public Integer creaLezione(LocalDate data, LocalTime ora, Campo campo, Utente maestro, String descrizione)
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
            Prenotazione prenotazione = prenotazioneService.getPrenotazionePerId(idPrenotazione);

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
     * Inserisce o aggiorna la descrizione per una lezione esistente.
     * 
     * @param idLezione   l'ID della lezione
     * @param descrizione la descrizione da inserire
     * @return true se l'aggiornamento è andato a buon fine
     * @throws AccademiaException se la lezione non esiste o si verifica un errore
     */
    public boolean inserisciDescrizione(Integer idLezione, String descrizione) throws AccademiaException {

        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID lezione non valido");
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
    public boolean cancellaLezione(Integer idLezione) throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
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
    public Lezione getLezionePerId(Integer idLezione) throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
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
    public List<Lezione> getLezioni() throws AccademiaException {
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
    public List<Lezione> getLezionePerMaestro(Utente maestro) throws AccademiaException {

        if (maestro == null) {
            throw new AccademiaException("Il maestro non può essere null");
        }

        if (maestro.getId() == null) {
            throw new AccademiaException("ID del maestro non valido");
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
    public Lezione getLezionePerPrenotazione(Prenotazione prenotazione) throws AccademiaException {

        if (prenotazione == null) {
            throw new AccademiaException("La prenotazione non può essere null");
        }

        if (prenotazione.getId() == null) {
            throw new AccademiaException("ID della prenotazione non valido");
        }

        try {
            return lezioneDAO.getLezioneByPrenotazione(prenotazione.getId());
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero della lezione per la prenotazione "
                    + prenotazione.getId() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Aggiunge un allievo a una lezione esistente.
     * 
     * @param idLezione l'ID della lezione
     * @param allievo   l'allievo da aggiungere
     * @throws AccademiaException se si verifica un errore o la lezione è piena
     */
    public void aggiungiAllievo(Integer idLezione, Utente allievo) throws AccademiaException {
        // Validazione
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
        }
        if (allievo == null) {
            throw new AccademiaException("L'allievo non può essere null");
        }
        if (allievo.getRuolo() != Ruolo.ALLIEVO && allievo.getRuolo() != Ruolo.SOCIO) {
            throw new AccademiaException("Solo gli allievi o i soci possono partecipare a una lezione");
        }

        try {
            // Verifica che la lezione esista
            Lezione lezione = lezioneDAO.getLezioneById(idLezione);
            if (lezione == null) {
                throw new AccademiaException("Lezione con ID " + idLezione + " non trovata");
            }

            // Verifica il numero massimo di allievi
            int numeroAllievi = allievoLezioneDAO.contaAllievi(idLezione);
            if (numeroAllievi >= MAX_ALLIEVI_PER_LEZIONE) {
                throw new AccademiaException("La lezione ha raggiunto il numero massimo di allievi ("
                        + MAX_ALLIEVI_PER_LEZIONE + ")");
            }

            // Aggiunge l'allievo
            allievoLezioneDAO.aggiungiAllievoLezione(idLezione, allievo.getId());

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key") || e.getMessage().contains("UNIQUE")) {
                throw new AccademiaException("L'allievo è già iscritto a questa lezione");
            }
            throw new AccademiaException("Errore durante l'aggiunta dell'allievo: " + e.getMessage(), e);
        }
    }

    /**
     * Rimuove un allievo da una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo da rimuovere
     * @return true se la rimozione è andata a buon fine
     * @throws AccademiaException se l'allievo non è iscritto o si verifica un
     *                            errore durante la rimozione
     */
    public boolean rimuoviAllievo(Integer idLezione, Integer idAllievo) throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
        }
        if (idAllievo == null || idAllievo <= 0) {
            throw new AccademiaException("ID dell'allievo non valido");
        }

        try {
            boolean rimosso = allievoLezioneDAO.rimuoviAllievoLezione(idLezione, idAllievo);
            if (!rimosso) {
                throw new AccademiaException("L'allievo non è iscritto a questa lezione");
            }
            return true;
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante la rimozione dell'allievo: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutti gli allievi di una lezione.
     * Questo metodo è separato da getLezioneById per performance:
     * puoi recuperare la lezione senza caricare gli allievi se non servono.
     * 
     * @param idLezione l'ID della lezione
     * @return lista di utenti iscritti alla lezione
     * @throws AccademiaException se si verifica un errore durante il recupero
     */
    public List<Utente> getAllievi(Integer idLezione) throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
        }

        try {
            return allievoLezioneDAO.getAllieviByLezione(idLezione);
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero degli allievi: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le lezioni a cui è iscritto un allievo.
     * 
     * @param allievo l'allievo di cui recuperare le lezioni
     * @return lista di lezioni dell'allievo
     * @throws AccademiaException se l'allievo non è valido o si verifica un errore
     */
    public List<Lezione> getLezioniAllievo(Utente allievo) throws AccademiaException {
        if (allievo == null || allievo.getId() == null) {
            throw new AccademiaException("L'allievo non può essere null");
        }

        try {
            return allievoLezioneDAO.getLezioniByAllievo(allievo.getId());
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero delle lezioni: " + e.getMessage(), e);
        }
    }

    /**
     * Conta il numero di allievi iscritti a una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @return il numero di allievi iscritti
     * @throws AccademiaException se si verifica un errore durante il conteggio
     */
    public int contaAllievi(Integer idLezione) throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
        }

        try {
            return allievoLezioneDAO.contaAllievi(idLezione);
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il conteggio degli allievi: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica se una lezione ha ancora posti disponibili (max 8 allievi).
     * 
     * @param idLezione l'ID della lezione
     * @return true se ci sono posti disponibili, false altrimenti
     * @throws AccademiaException se si verifica un errore durante la verifica
     */
    public boolean haPostiDisponibili(Integer idLezione) throws AccademiaException {
        return contaAllievi(idLezione) < MAX_ALLIEVI_PER_LEZIONE;
    }

    /**
     * Segna la presenza o assenza di un allievo a una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @param presente  true per segnare presente, false per assente
     * @return true se l'operazione è andata a buon fine
     * @throws AccademiaException se si verifica un errore durante l'aggiornamento
     */
    public boolean segnaPresenza(Integer idLezione, Integer idAllievo, boolean presente)
            throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
        }
        if (idAllievo == null || idAllievo <= 0) {
            throw new AccademiaException("ID dell'allievo non valido");
        }

        try {
            return allievoLezioneDAO.segnaPresenza(idLezione, idAllievo, presente);
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante la segnatura della presenza: " + e.getMessage(), e);
        }
    }

    /**
     * Aggiunge un feedback specifico per un allievo in una lezione.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @param feedback  il testo del feedback/nota
     * @return true se l'inserimento è andato a buon fine
     * @throws AccademiaException se si verifica un errore durante l'inserimento
     */
    public boolean aggiungiFeedback(Integer idLezione, Integer idAllievo, String feedback)
            throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
        }
        if (idAllievo == null || idAllievo <= 0) {
            throw new AccademiaException("ID dell'allievo non valido");
        }

        try {
            return allievoLezioneDAO.aggiungiFeedback(idLezione, idAllievo, feedback);
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante l'aggiunta del feedback: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera i dettagli completi di un allievo in una lezione specifica.
     * Include informazioni su presenza e feedback.
     * 
     * @param idLezione l'ID della lezione
     * @param idAllievo l'ID dell'allievo
     * @return l'oggetto AllievoLezione con i dettagli, null se non trovato
     * @throws AccademiaException se si verifica un errore durante il recupero
     */
    public AllievoLezione getAllievoLezione(Integer idLezione, Integer idAllievo)
            throws AccademiaException {
        if (idLezione == null || idLezione <= 0) {
            throw new AccademiaException("ID della lezione non valido");
        }
        if (idAllievo == null || idAllievo <= 0) {
            throw new AccademiaException("ID dell'allievo non valido");
        }

        try {
            return allievoLezioneDAO.getAllievoLezione(idLezione, idAllievo);
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero dei dettagli: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutti gli utenti con ruolo ALLIEVO.
     * Utile per permettere al maestro di selezionare un allievo da aggiungere a una
     * lezione.
     * 
     * @return lista di utenti con ruolo ALLIEVO
     * @throws AccademiaException se si verifica un errore durante il recupero
     */
    public List<Utente> getUtentiAllievi() throws AccademiaException {
        try {
            return utenteDAO.getUtentiByRuolo(Ruolo.ALLIEVO);
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero degli allievi: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera un utente tramite ID.
     * Utile per selezionare un allievo specifico da aggiungere a una lezione.
     * 
     * @param idUtente l'ID dell'utente
     * @return l'utente richiesto
     * @throws AccademiaException se l'utente non esiste o si verifica un errore
     */
    public Utente getUtentePerId(Integer idUtente) throws AccademiaException {
        if (idUtente == null || idUtente <= 0) {
            throw new AccademiaException("ID dell'utente non valido");
        }

        try {
            Utente utente = utenteDAO.getUtenteById(idUtente);
            if (utente == null) {
                throw new AccademiaException("Utente con ID " + idUtente + " non trovato");
            }
            return utente;
        } catch (SQLException e) {
            throw new AccademiaException("Errore durante il recupero dell'utente: " + e.getMessage(), e);
        }
    }
}
