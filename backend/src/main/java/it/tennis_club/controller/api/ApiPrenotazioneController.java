package it.tennis_club.controller.api;

import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.business_logic.PrenotazioneService;
import it.tennis_club.business_logic.PrenotazioneException;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.orm.UtenteDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * REST Controller per le Prenotazioni — CRUD completo.
 * 
 * STEP 6: Aggiunto POST (crea) e DELETE (elimina).
 * 
 * Riepilogo endpoint:
 *   GET    /api/prenotazioni       → lista tutte
 *   GET    /api/prenotazioni/{id}  → una specifica
 *   POST   /api/prenotazioni       → crea nuova
 *   DELETE /api/prenotazioni/{id}  → elimina
 */
@RestController
@RequestMapping("/api")
public class ApiPrenotazioneController {

    private final PrenotazioneService prenotazioneService;
    private final CampoService campoService;
    private final UtenteDAO utenteDAO;

    @Autowired
    public ApiPrenotazioneController(
            PrenotazioneService prenotazioneService,
            CampoService campoService,
            UtenteDAO utenteDAO) {
        this.prenotazioneService = prenotazioneService;
        this.campoService = campoService;
        this.utenteDAO = utenteDAO;
    }

    // ==================== READ ====================

    /**
     * GET /api/prenotazioni — Restituisce tutte le prenotazioni.
     */
    @GetMapping("/prenotazioni")
    public ResponseEntity<?> getPrenotazioni() {
        try {
            List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioni();
            return ResponseEntity.ok(prenotazioni);
        } catch (PrenotazioneException e) {
            return ResponseEntity
                    .status(500)
                    .body("{\"errore\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * GET /api/prenotazioni/{id} — Restituisce una singola prenotazione.
     */
    @GetMapping("/prenotazioni/{id}")
    public ResponseEntity<?> getPrenotazioneById(@PathVariable Integer id) {
        try {
            Prenotazione prenotazione = prenotazioneService.getPrenotazionePerId(id);
            return ResponseEntity.ok(prenotazione);
        } catch (PrenotazioneException e) {
            return ResponseEntity
                    .status(404)
                    .body("{\"errore\": \"" + e.getMessage() + "\"}");
        }
    }

    // ==================== CREATE ====================

    /**
     * POST /api/prenotazioni — Crea una nuova prenotazione.
     * 
     * Il frontend invia un JSON nel body:
     *   {"data": "2026-04-15", "oraInizio": "14:00", "idCampo": 2, "idSocio": 5}
     * 
     * Il flusso:
     * 1. Jackson deserializza il JSON → PrenotazioneRequest (DTO)
     * 2. Il controller recupera Campo e Utente dal DB usando gli ID
     * 3. prenotazioneService.creaPrenotazione() fa le validazioni e salva
     * 4. HTTP 201 Created + la prenotazione creata
     */
    @PostMapping("/prenotazioni")
    public ResponseEntity<?> creaPrenotazione(@RequestBody PrenotazioneRequest request) {
        try {
            LocalDate data = LocalDate.parse(request.getData());
            LocalTime oraInizio = LocalTime.parse(request.getOraInizio());

            Campo campo = campoService.getCampoPerId(request.getIdCampo());

            Utente socio = utenteDAO.getUtenteById(request.getIdSocio());
            if (socio == null) {
                return ResponseEntity
                        .status(400)
                        .body("{\"errore\": \"Utente non trovato\"}");
            }

            Integer idCreato = prenotazioneService.creaPrenotazione(data, oraInizio, campo, socio);
            Prenotazione nuova = prenotazioneService.getPrenotazionePerId(idCreato);
            return ResponseEntity.status(201).body(nuova);

        } catch (PrenotazioneException | CampoException e) {
            return ResponseEntity
                    .status(400)
                    .body("{\"errore\": \"" + e.getMessage() + "\"}");
        } catch (SQLException e) {
            return ResponseEntity
                    .status(500)
                    .body("{\"errore\": \"Errore del database: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity
                    .status(400)
                    .body("{\"errore\": \"Formato dati non valido: " + e.getMessage() + "\"}");
        }
    }

    // ==================== DELETE ====================

    /**
     * DELETE /api/prenotazioni/{id} — Elimina una prenotazione.
     * 
     * DELETE non ha body. L'ID è nell'URL.
     * HTTP 204 "No Content" = eliminazione riuscita, nulla da restituire.
     */
    @DeleteMapping("/prenotazioni/{id}")
    public ResponseEntity<?> cancellaPrenotazione(@PathVariable Integer id) {
        try {
            boolean eliminata = prenotazioneService.cancellaPrenotazione(id);

            if (eliminata) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(404)
                        .body("{\"errore\": \"Prenotazione non trovata\"}");
            }

        } catch (PrenotazioneException e) {
            return ResponseEntity
                    .status(400)
                    .body("{\"errore\": \"" + e.getMessage() + "\"}");
        }
    }
}
