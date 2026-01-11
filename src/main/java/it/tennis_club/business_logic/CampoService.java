package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.CampoDAO;
import it.tennis_club.orm.ManutenzioneDAO;

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
    
    public CampoService() {
        this.campoDAO = new CampoDAO();
        this.manutenzioneDAO = new ManutenzioneDAO();
    }
    
    // ========== OPERAZIONI PUBBLICHE (accessibili a tutti) ==========
    
    /**
     * Recupera tutti i campi disponibili.
     * 
     * @return lista di tutti i campi
     * @throws CampoException se si verifica un errore durante il recupero
     */
    public List<Campo> getAllCampi() throws CampoException {
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
    public Campo getCampoById(Integer idCampo) throws CampoException {
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
    public List<Campo> getCampiByTipoSuperficie(String tipoSuperficie) throws CampoException {
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
     * @param utente l'utente che richiede l'operazione
     * @param idCampo l'ID del campo da manutenere
     * @param dataInizio la data di inizio manutenzione
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
        
        if (descrizione == null || descrizione.trim().isEmpty()) {
            throw new CampoException("Descrizione manutenzione obbligatoria");
        }
        
        try {
            // Verifica che il campo esista
            Campo campo = campoDAO.getCampoById(idCampo);
            if (campo == null) {
                throw new CampoException("Campo con ID " + idCampo + " non trovato");
            }
            
            // Crea la manutenzione
            Manutenzione manutenzione = new Manutenzione();
            manutenzione.setCampo(campo);
            manutenzione.setManutentore(utente);
            manutenzione.setDataInizio(dataInizio);
            manutenzione.setDescrizione(descrizione);
            manutenzione.setStato(Manutenzione.Stato.IN_CORSO);
            
            return manutenzioneDAO.createManutenzione(manutenzione);
            
        } catch (SQLException e) {
            throw new CampoException("Errore durante la creazione della manutenzione: " + e.getMessage(), e);
        }
    }
    
    /**
     * Completa una manutenzione esistente.
     * Richiede permessi di ADMIN o MANUTENTORE.
     * 
     * @param utente l'utente che richiede l'operazione
     * @param idManutenzione l'ID della manutenzione da completare
     * @param dataFine la data di completamento
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
        
        try {
            manutenzioneDAO.completaManutenzione(idManutenzione, dataFine);
        } catch (SQLException e) {
            throw new CampoException("Errore durante il completamento della manutenzione: " + e.getMessage(), e);
        }
    }
    
    /**
     * Annulla una manutenzione esistente.
     * Richiede permessi di ADMIN o MANUTENTORE.
     * 
     * @param utente l'utente che richiede l'operazione
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
            manutenzioneDAO.updateStatoManutenzione(idManutenzione, Manutenzione.Stato.ANNULLATA);
        } catch (SQLException e) {
            throw new CampoException("Errore durante l'annullamento della manutenzione: " + e.getMessage(), e);
        }
    }
    
    /**
     * Recupera tutte le manutenzioni di un campo.
     * Richiede permessi di ADMIN o MANUTENTORE.
     * 
     * @param utente l'utente che richiede l'operazione
     * @param idCampo l'ID del campo
     * @return lista delle manutenzioni del campo
     * @throws CampoException se l'utente non ha i permessi o si verifica un errore
     */
    public List<Manutenzione> getManutenzioniCampo(Utente utente, Integer idCampo) throws CampoException {
        
        // Controllo permessi
        verificaPermessiManutenzione(utente);
        
        // Validazione input
        if (idCampo == null || idCampo <= 0) {
            throw new CampoException("ID campo non valido");
        }
        
        try {
            return manutenzioneDAO.getManutenzioniByIdCampo(idCampo);
        } catch (SQLException e) {
            throw new CampoException("Errore durante il recupero delle manutenzioni: " + e.getMessage(), e);
        }
    }
    
    // ========== METODI PRIVATI DI UTILITÀ ==========
    
    /**
     * Verifica che l'utente abbia i permessi per gestire le manutenzioni.
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
        if (utente.getRuolo() != Utente.Ruolo.ADMIN && 
            utente.getRuolo() != Utente.Ruolo.MANUTENTORE) {
            throw new CampoException("Permessi insufficienti. Solo ADMIN e MANUTENTORE possono gestire le manutenzioni.");
        }
    }
}
