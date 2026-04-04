package it.tennis_club.controller.api;

import it.tennis_club.business_logic.PrenotazioneService;
import it.tennis_club.business_logic.PrenotazioneException;
import it.tennis_club.domain_model.Prenotazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller per le Prenotazioni.
 * 
 * NOTA DIDATTICA: confronta questo controller con PrenotazioneController (in controller/).
 * 
 * PrenotazioneController (@Controller):
 *   @GetMapping("/prenotazioni")
 *   public String listaPrenotazioni(Model model) {
 *       model.addAttribute("prenotazioni", prenotazioni);  // passa dati al template
 *       return "prenotazioni";                              // nome del template HTML
 *   }
 * 
 * ApiPrenotazioneController (@RestController):
 *   @GetMapping("/prenotazioni")
 *   public ResponseEntity<?> getPrenotazioni() {
 *       return ResponseEntity.ok(prenotazioni);  // l'oggetto DIVENTA il JSON
 *   }
 * 
 * La differenza fondamentale:
 * - @Controller → "dammi il template con questo nome e riempi i buchi con questi dati"
 * - @RestController → "prendi questo oggetto e trasformalo in JSON"
 */
@RestController
@RequestMapping("/api")
public class ApiPrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    @Autowired
    public ApiPrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    /**
     * GET /api/prenotazioni — Restituisce tutte le prenotazioni.
     * 
     * Ogni Prenotazione contiene oggetti nested:
     * - campo: {id, nome, tipoSuperficie, isCoperto}
     * - socio: {id, nome, cognome, email, ruolo}  (password esclusa grazie a @JsonIgnore!)
     * 
     * Il JSON risultante avrà questa struttura:
     * [
     *   {
     *     "id": 1,
     *     "data": "2026-03-30",
     *     "oraInizio": "09:00",
     *     "campo": {"id": 1, "nome": "Campo Centrale", ...},
     *     "socio": {"id": 2, "nome": "Laura", "cognome": "Bianchi", ...}
     *   },
     *   ...
     * ]
     * 
     * NOTA: Jackson serializza automaticamente LocalDate come "2026-03-30" 
     * e LocalTime come "09:00:00". Non devi fare nulla di speciale!
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
}
