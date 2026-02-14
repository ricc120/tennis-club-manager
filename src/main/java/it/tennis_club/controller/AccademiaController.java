package it.tennis_club.controller;

import it.tennis_club.business_logic.AccademiaService;
import it.tennis_club.business_logic.AccademiaException;
import it.tennis_club.business_logic.PrenotazioneException;
import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.domain_model.*;
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
 * Controller per la gestione dell'accademia (lezioni) via web.
 */
@Controller
public class AccademiaController {

    private final AccademiaService accademiaService;
    private final CampoService campoService;

    @Autowired
    public AccademiaController(AccademiaService accademiaService, CampoService campoService) {
        this.accademiaService = accademiaService;
        this.campoService = campoService;
    }

    /**
     * Mostra la lista delle lezioni.
     */
    @GetMapping("/lezioni")
    public String listaLezioni(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            List<Lezione> lezioni;
            if (utente.getRuolo() == Ruolo.MAESTRO) {
                lezioni = accademiaService.getLezioneByMaestro(utente);
            } else {
                lezioni = accademiaService.getAllLezioni();
            }
            model.addAttribute("lezioni", lezioni);
            model.addAttribute("utente", utente);

            // Carica i campi per il form di creazione (solo per MAESTRO)
            if (utente.getRuolo() == Ruolo.MAESTRO) {
                List<Campo> campi = campoService.getAllCampi();
                model.addAttribute("campi", campi);
            }

        } catch (AccademiaException | CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "lezioni";
    }

    /**
     * Mostra il dettaglio di una lezione con la lista degli allievi.
     */
    @GetMapping("/lezioni/{id}")
    public String dettaglioLezione(
            @PathVariable Integer id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            Lezione lezione = accademiaService.getLezioneById(id);
            if (lezione == null) {
                redirectAttributes.addFlashAttribute("errore", "Lezione non trovata.");
                return "redirect:/lezioni";
            }

            List<Utente> allievi = accademiaService.getAllievi(id);
            List<Utente> allieviDisponibili = accademiaService.getUtentiAllievi();

            model.addAttribute("lezione", lezione);
            model.addAttribute("allievi", allievi);
            model.addAttribute("allieviDisponibili", allieviDisponibili);
            model.addAttribute("utente", utente);

        } catch (AccademiaException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
            return "redirect:/lezioni";
        }

        return "lezione-dettaglio";
    }

    /**
     * Crea una nuova lezione (solo MAESTRO).
     */
    @PostMapping("/lezioni/nuova")
    public String creaLezione(
            @RequestParam String data,
            @RequestParam String oraInizio,
            @RequestParam Integer idCampo,
            @RequestParam String descrizione,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            LocalDate date = LocalDate.parse(data);
            LocalTime time = LocalTime.parse(oraInizio);

            Campo campo = campoService.getCampoById(idCampo);
            if (campo == null) {
                redirectAttributes.addFlashAttribute("errore", "Campo non trovato.");
                return "redirect:/lezioni";
            }

            accademiaService.createLezione(date, time, campo, utente, descrizione);
            redirectAttributes.addFlashAttribute("successo", "Lezione creata con successo!");

        } catch (AccademiaException | PrenotazioneException | CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errore", "Errore: " + e.getMessage());
        }

        return "redirect:/lezioni";
    }

    /**
     * Aggiunge un allievo a una lezione.
     */
    @PostMapping("/lezioni/{id}/allievo")
    public String aggiungiAllievo(
            @PathVariable Integer id,
            @RequestParam Integer idAllievo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            Utente allievo = accademiaService.getUtenteById(idAllievo);
            accademiaService.aggiungiAllievo(id, allievo);
            redirectAttributes.addFlashAttribute("successo", "Allievo aggiunto con successo!");

        } catch (AccademiaException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "redirect:/lezioni/" + id;
    }

    /**
     * Rimuove un allievo da una lezione.
     */
    @PostMapping("/lezioni/{id}/allievo/{idAllievo}/rimuovi")
    public String rimuoviAllievo(
            @PathVariable Integer id,
            @PathVariable Integer idAllievo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            accademiaService.rimuoviAllievo(id, idAllievo);
            redirectAttributes.addFlashAttribute("successo", "Allievo rimosso con successo!");

        } catch (AccademiaException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "redirect:/lezioni/" + id;
    }
}
