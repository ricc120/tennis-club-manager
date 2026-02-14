package it.tennis_club.controller;

import it.tennis_club.business_logic.PrenotazioneService;
import it.tennis_club.business_logic.PrenotazioneException;
import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Controller per la gestione delle prenotazioni via web.
 */
@Controller
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;
    private final CampoService campoService;

    @Autowired
    public PrenotazioneController(PrenotazioneService prenotazioneService, CampoService campoService) {
        this.prenotazioneService = prenotazioneService;
        this.campoService = campoService;
    }

    /**
     * Mostra la lista delle prenotazioni.
     * ADMIN e MAESTRO vedono tutte le prenotazioni, gli altri solo le proprie.
     */
    @GetMapping("/prenotazioni")
    public String listaPrenotazioni(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            List<Prenotazione> prenotazioni;
            if (utente.getRuolo() == Ruolo.ADMIN || utente.getRuolo() == Ruolo.MAESTRO) {
                prenotazioni = prenotazioneService.getAllPrenotazioni();
            } else {
                prenotazioni = prenotazioneService.getPrenotazioniPerSocio(utente);
            }
            model.addAttribute("prenotazioni", prenotazioni);
            model.addAttribute("utente", utente);

            // Carica i campi per il form di creazione
            List<Campo> campi = campoService.getAllCampi();
            model.addAttribute("campi", campi);

        } catch (PrenotazioneException | CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "prenotazioni";
    }

    /**
     * Crea una nuova prenotazione.
     */
    @PostMapping("/prenotazioni/nuova")
    public String creaPrenotazione(
            @RequestParam String data,
            @RequestParam String oraInizio,
            @RequestParam Integer idCampo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            LocalDate date = LocalDate.parse(data);
            LocalTime time = LocalTime.parse(oraInizio);

            // Recupera il campo
            Campo campo = campoService.getCampoById(idCampo);
            if (campo == null) {
                redirectAttributes.addFlashAttribute("errore", "Campo non trovato.");
                return "redirect:/prenotazioni";
            }

            prenotazioneService.creaPrenotazione(date, time, campo, utente);
            redirectAttributes.addFlashAttribute("successo", "Prenotazione creata con successo!");

        } catch (PrenotazioneException | CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errore", "Errore durante la creazione: " + e.getMessage());
        }

        return "redirect:/prenotazioni";
    }

    /**
     * Cancella una prenotazione.
     */
    @PostMapping("/prenotazioni/{id}/cancella")
    public String cancellaPrenotazione(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            prenotazioneService.cancellaPrenotazione(id);
            redirectAttributes.addFlashAttribute("successo", "Prenotazione cancellata con successo!");

        } catch (PrenotazioneException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "redirect:/prenotazioni";
    }
}
