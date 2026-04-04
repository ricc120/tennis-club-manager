package it.tennis_club.controller.api;

import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.domain_model.Campo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller per i Campi — restituisce JSON, non HTML.
 * 
 * CONCETTO CHIAVE: @RestController vs @Controller
 * 
 * Il CampoController esistente (in controller/) usa @Controller:
 *   - Restituisce il NOME di un template Thymeleaf (es: return "campi")
 *   - Spring cerca un file campi.html e lo renderizza
 * 
 * Questo controller usa @RestController:
 *   - Restituisce direttamente un OGGETTO Java
 *   - Spring usa Jackson per convertirlo automaticamente in JSON
 *   - return campi → [{"id":1,"nome":"Campo Centrale",...}, ...]
 * 
 * @RequestMapping("/api") = prefisso per tutti gli endpoint di questo controller.
 * Così tutti gli URL inizieranno con /api/campi, separandoli dalle pagine HTML.
 */
@RestController
@RequestMapping("/api")
public class ApiCampoController {

    /**
     * DEPENDENCY INJECTION con @Autowired (costruttore).
     * 
     * Spring crea automaticamente un'istanza di CampoService (perché ha @Service)
     * e la "inietta" qui tramite il costruttore. Non serve fare "new CampoService()".
     * 
     * È lo stesso pattern usato nei controller Thymeleaf esistenti
     * (es: PrenotazioneController, CampoController).
     */
    private final CampoService campoService;

    @Autowired
    public ApiCampoController(CampoService campoService) {
        this.campoService = campoService;
    }

    /**
     * GET /api/campi — Restituisce la lista di tutti i campi.
     * 
     * CONCETTO: ResponseEntity<T>
     * È un wrapper che ti permette di controllare:
     * - Il CORPO della risposta (l'oggetto da convertire in JSON)
     * - Lo STATUS HTTP (200 OK, 404 Not Found, 500 Error)
     * - Gli HEADERS (Content-Type, etc.)
     * 
     * ResponseEntity.ok(body) = status 200 + il body come JSON
     * ResponseEntity.status(500).body(errore) = status 500 + messaggio errore
     * 
     * Perché non restituire direttamente List<Campo>?
     * Potresti farlo! Ma ResponseEntity ti dà più controllo sugli errori.
     * Se restituisci List<Campo> e c'è un'eccezione, Spring restituisce
     * un errore generico. Con ResponseEntity gestisci tu il messaggio.
     */
    @GetMapping("/campi")
    public ResponseEntity<?> getCampi() {
        try {
            List<Campo> campi = campoService.getCampi();
            return ResponseEntity.ok(campi);
            // → HTTP 200 + JSON: [{"id":1,"nome":"Campo Centrale",...}, ...]
        } catch (CampoException e) {
            return ResponseEntity
                    .status(500)
                    .body("{\"errore\": \"" + e.getMessage() + "\"}");
            // → HTTP 500 + JSON: {"errore": "messaggio dell'errore"}
        }
    }

    /**
     * GET /api/campi/{id} — Restituisce un singolo campo per ID.
     * 
     * CONCETTO: @PathVariable
     * L'URL /api/campi/3 → Spring estrae "3" e lo passa come parametro "id".
     * È lo stesso di @PathVariable che usi già nei controller Thymeleaf.
     */
    @GetMapping("/campi/{id}")
    public ResponseEntity<?> getCampoById(@PathVariable Integer id) {
        try {
            Campo campo = campoService.getCampoPerId(id);
            return ResponseEntity.ok(campo);
        } catch (CampoException e) {
            return ResponseEntity
                    .status(404)
                    .body("{\"errore\": \"" + e.getMessage() + "\"}");
            // → HTTP 404 Not Found  (il campo non esiste)
        }
    }
}
