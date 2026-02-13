package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Manutenzione.Stato;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.CampoDAO;
import it.tennis_club.orm.ManutenzioneDAO;
import it.tennis_club.orm.PrenotazioneDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Servizio per la gestione dei campi da tennis e delle loro manutenzioni.
 * Implementa la business logic e il controllo dei permessi.
 */
public class CampoService {

    private CampoDAO campoDAO;
    private ManutenzioneDAO manutenzioneDAO;
    private PrenotazioneDAO prenotazioneDAO;
    private NotificationService notificationService;

    public CampoService() {
        this.campoDAO = new CampoDAO();
        this.manutenzioneDAO = new ManutenzioneDAO();
        this.prenotazioneDAO = new PrenotazioneDAO();
        this.notificationService = NotificationService.getInstance();
    }

    // ========== OPERAZIONI PUBBLICHE (accessibili a tutti) ==========

    /**
     * Recupera tutti i campi disponibili.
     * 
     * @return lista di tutti i campi
     * @throws CampoException se si verifica un errore durante il recupero
     */
    public List<Campo> getCampi() throws CampoException {
        try {
            return campoDAO.getAllCampi();
        } catch (SQLException e) {
            throw new CampoException("Errore durante il recupero dei campi: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera un campo specifico tramite ID.
     * 
     * @param idCampo l'ID del campo
     * @return il campo richiesto
     * @throws CampoException se il campo non esiste o si verifica un errore
     */
    public Campo getCampoPerId(Integer idCampo) throws CampoException {
        if (idCampo == null || idCampo <= 0) {
            throw new CampoException("ID campo non valido");
        }

        try {
            Campo campo = campoDAO.getCampoById(idCampo);
            if (campo == null) {
                throw new CampoException("Campo con ID " + idCampo + " non trovato");
            }
            return campo;
        } catch (SQLException e) {
            throw new CampoException("Errore durante il recupero del campo: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera i campi coperti.
     * 
     * @return lista dei campi coperti
     * @throws CampoException se si verifica un errore durante il recupero
     */
    public List<Campo> getCampiCoperti() throws CampoException {
        try {
            return campoDAO.getCampiCoperti();
        } catch (SQLException e) {
            throw new CampoException("Errore durante il recupero dei campi coperti: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera i campi per tipo di superficie.
     * 
     * @param tipoSuperficie il tipo di superficie (es. "Terra", "Cemento", "Erba")
     * @return lista dei campi con il tipo di superficie specificato
     * @throws CampoException se si verifica un errore durante il recupero
     */
    public List<Campo> getCampiPerTipoSuperficie(String tipoSuperficie) throws CampoException {
        if (tipoSuperficie == null || tipoSuperficie.trim().isEmpty()) {
            throw new CampoException("Tipo superficie non valido");
        }

        try {
            return campoDAO.getCampiByTipoSuperficie(tipoSuperficie);
        } catch (SQLException e) {
            throw new CampoException("Errore durante il recupero dei campi: " + e.getMessage(), e);
        }
    }

    // ========== OPERAZIONI RISERVATE (solo ADMIN e MANUTENTORE) ==========

    /**
     * Crea una nuova manutenzione su un campo.
     * Richiede permessi di ADMIN o MANUTENTORE.
     * 
     * @param utente      l'utente che richiede l'operazione
     * @param idCampo     l'ID del campo da manutenere
     * @param dataInizio  la data di inizio manutenzione
     * @param descrizione la descrizione della manutenzione
     * @return l'ID della manutenzione creata
     * @throws CampoException se l'utente non ha i permessi o si verifica un errore
     */
    public Integer creaManutenzione(Utente utente, Integer idCampo, LocalDate dataInizio, String descrizione)
            throws CampoException {

        // Controllo permessi
        verificaPermessiManutenzione(utente);

        // Validazione input
        if (idCampo == null || idCampo <= 0) {
            throw new CampoException("ID campo non valido");
        }

        if (dataInizio == null) {
            throw new CampoException("Data inizio non può essere null");
        }

        // Validazione: la manutenzione non può essere programmata nel passato
        if (dataInizio.isBefore(LocalDate.now())) {
            throw new CampoException("Non è possibile programmare una manutenzione per una data passata");
        }

        if (descrizione == null || descrizione.trim().isEmpty()) {
            throw new CampoException("Descrizione manutenzione obbligatoria");
        }

        try {
            // Verifica che il campo esista
            Campo campo = campoDAO.getCampoById(idCampo);
            if (campo == null) {
                throw new CampoException("Campo con ID " + idCampo + " non trovato");
            }

            // Elimina tutte le prenotazioni esistenti per quel campo e data
            // Le lezioni associate verranno eliminate automaticamente via CASCADE
            List<Prenotazione> prenotazioniDaEliminare = prenotazioneDAO.getPrenotazioniByDataAndCampo(
                    dataInizio, idCampo);

            for (Prenotazione prenotazione : prenotazioniDaEliminare) {
                // Notifica l'utente che la sua prenotazione è stata cancellata
                String messaggio = String.format(
                        "La tua prenotazione del %s sul %s è stata cancellata " +
                                "a causa di una manutenzione programmata.",
                        prenotazione.getData(), campo.getNome());
                notificationService.addNotification(prenotazione.getSocio().getId(), messaggio);

                // Elimina la prenotazione (le lezioni vengono eliminate in cascade)
                prenotazioneDAO.deletePrenotazione(prenotazione.getId());
            }

            // Crea la manutenzione
            Manutenzione manutenzione = new Manutenzione();
            manutenzione.setCampo(campo);
            manutenzione.setManutentore(utente);
            manutenzione.setDataInizio(dataInizio);
            manutenzione.setDescrizione(descrizione);
            manutenzione.setStato(Stato.IN_CORSO);

            return manutenzioneDAO.createManutenzione(manutenzione);

        } catch (SQLException e) {
            throw new CampoException("Errore durante la creazione della manutenzione: " + e.getMessage(), e);
        }
    }

    /**
     * Completa una manutenzione esistente.
     * Richiede permessi di ADMIN o MANUTENTORE.
     * 
     * @param utente         l'utente che richiede l'operazione
     * @param idManutenzione l'ID della manutenzione da completare
     * @param dataFine       la data di completamento
     * @throws CampoException se l'utente non ha i permessi o si verifica un errore
     */
    public void completaManutenzione(Utente utente, Integer idManutenzione, LocalDate dataFine)
            throws CampoException {

        // Controllo permessi
        verificaPermessiManutenzione(utente);

        // Validazione input
        if (idManutenzione == null || idManutenzione <= 0) {
            throw new CampoException("ID manutenzione non valido");
        }

        if (dataFine == null) {
            throw new CampoException("Data fine non può essere null");
        }

        if (dataFine.isBefore(LocalDate.now())) {
            throw new CampoException("La data di fine manutenzione non può essere passata");
        }

        try {
            if (manutenzioneDAO.getManutenzioneById(idManutenzione) == null) {
                throw new CampoException("Manutenzione con ID " + idManutenzione + " non trovata");
            }
            manutenzioneDAO.completaManutenzione(idManutenzione, dataFine);

            Manutenzione manutenzione = manutenzioneDAO.getManutenzioneById(idManutenzione);

            // Elimina tutte le prenotazioni esistenti per quel campo nel range [dataInizio,
            // dataFine]
            List<Prenotazione> prenotazioniDaEliminare = prenotazioneDAO.getPrenotazioniByDateRangeAndCampo(
                    manutenzione.getDataInizio(), manutenzione.getDataFine(), manutenzione.getCampo().getId());

            for (Prenotazione prenotazione : prenotazioniDaEliminare) {
                // Notifica l'utente che la sua prenotazione è stata cancellata
                String messaggio = String.format(
                        "La tua prenotazione del %s sul %s è stata cancellata " +
                                "a causa di una manutenzione programmata.",
                        prenotazione.getData(), manutenzione.getCampo().getNome());
                notificationService.addNotification(prenotazione.getSocio().getId(), messaggio);

                // Elimina la prenotazione (le lezioni associate vengono eliminate in CASCADE)
                prenotazioneDAO.deletePrenotazione(prenotazione.getId());
            }

        } catch (SQLException e) {
            throw new CampoException("Errore durante il completamento della manutenzione: " + e.getMessage(), e);
        }
    }

    /**
     * Annulla una manutenzione esistente.
     * Richiede permessi di ADMIN o MANUTENTORE.
     * 
     * @param utente         l'utente che richiede l'operazione
     * @param idManutenzione l'ID della manutenzione da annullare
     * @throws CampoException se l'utente non ha i permessi o si verifica un errore
     */
    public void annullaManutenzione(Utente utente, Integer idManutenzione) throws CampoException {

        // Controllo permessi
        verificaPermessiManutenzione(utente);

        // Validazione input
        if (idManutenzione == null || idManutenzione <= 0) {
            throw new CampoException("ID manutenzione non valido");
        }

        try {

            if (manutenzioneDAO.getManutenzioneById(idManutenzione) == null) {
                throw new CampoException("Manutenzione con ID " + idManutenzione + " non trovata");
            }

            manutenzioneDAO.updateStatoManutenzione(idManutenzione, Stato.ANNULLATA);
        } catch (SQLException e) {
            throw new CampoException("Errore durante l'annullamento della manutenzione: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutte le manutenzioni di un campo.
     * Richiede permessi di ADMIN o MANUTENTORE.
     * 
     * @param utente  l'utente che richiede l'operazione
     * @param idCampo l'ID del campo
     * @return lista delle manutenzioni del campo
     * @throws CampoException se l'utente non ha i permessi o si verifica un errore
     */
    public List<Manutenzione> getManutenzioniPerCampo(Utente utente, Integer idCampo) throws CampoException {

        // Controllo permessi
        verificaPermessiManutenzione(utente);

        // Validazione input
        if (idCampo == null || idCampo <= 0) {
            throw new CampoException("ID campo non valido");
        }

        try {
            return manutenzioneDAO.getManutenzioniByCampo(idCampo);
        } catch (SQLException e) {
            throw new CampoException("Errore durante il recupero delle manutenzioni: " + e.getMessage(), e);
        }
    }

    public List<Manutenzione> getAllManutenzioni() throws CampoException {
        try {
            return manutenzioneDAO.getAllManutenzioni();
        } catch (SQLException e) {
            throw new CampoException("Errore durante il recupero delle manutenzioni: " + e.getMessage(), e);
        }
    }

    /**
     * Metodo helper per verifica che l'utente abbia i permessi per gestire le
     * manutenzioni.
     * Solo ADMIN e MANUTENTORE possono accedere a queste funzionalità.
     * 
     * @param utente l'utente da verificare
     * @throws CampoException se l'utente non ha i permessi necessari
     */
    private void verificaPermessiManutenzione(Utente utente) throws CampoException {
        if (utente == null) {
            throw new CampoException("Utente non autenticato");
        }

        if (utente.getRuolo() == null) {
            throw new CampoException("Ruolo utente non definito");
        }

        // Solo ADMIN e MANUTENTORE possono gestire le manutenzioni
        if (utente.getRuolo() != Ruolo.ADMIN &&
                utente.getRuolo() != Ruolo.MANUTENTORE) {
            throw new CampoException(
                    "Permessi insufficienti. Solo ADMIN e MANUTENTORE possono gestire le manutenzioni.");
        }
    }

}
