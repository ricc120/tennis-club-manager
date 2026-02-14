package it.tennis_club.controller;

import it.tennis_club.business_logic.CampoService;
import it.tennis_club.business_logic.CampoException;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller per la gestione dei campi e delle manutenzioni via web.
 */
@Controller
public class CampoController {

    private final CampoService campoService;

    @Autowired
    public CampoController(CampoService campoService) {
        this.campoService = campoService;
    }

    /**
     * Mostra la lista dei campi e, per ADMIN/MANUTENTORE, anche le manutenzioni.
     */
    @GetMapping("/campi")
    public String listaCampi(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            List<Campo> campi = campoService.getAllCampi();
            model.addAttribute("campi", campi);
            model.addAttribute("utente", utente);

            // Mostra le manutenzioni solo ad ADMIN e MANUTENTORE
            if (utente.getRuolo() == Ruolo.ADMIN || utente.getRuolo() == Ruolo.MANUTENTORE) {
                List<Manutenzione> manutenzioni = campoService.getAllManutenzioni();
                model.addAttribute("manutenzioni", manutenzioni);
            }

        } catch (CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "campi";
    }

    /**
     * Crea una nuova manutenzione.
     */
    @PostMapping("/manutenzioni/nuova")
    public String creaManutenzione(
            @RequestParam Integer idCampo,
            @RequestParam String dataInizio,
            @RequestParam String descrizione,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            LocalDate inizio = LocalDate.parse(dataInizio);
            campoService.creaManutenzione(utente, idCampo, inizio, descrizione);
            redirectAttributes.addFlashAttribute("successo", "Manutenzione creata con successo!");

        } catch (CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errore", "Errore: " + e.getMessage());
        }

        return "redirect:/campi";
    }

    /**
     * Completa una manutenzione.
     */
    @PostMapping("/manutenzioni/{id}/completa")
    public String completaManutenzione(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            campoService.completaManutenzione(utente, id, LocalDate.now());
            redirectAttributes.addFlashAttribute("successo", "Manutenzione completata!");

        } catch (CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "redirect:/campi";
    }

    /**
     * Annulla una manutenzione.
     */
    @PostMapping("/manutenzioni/{id}/annulla")
    public String annullaManutenzione(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            campoService.annullaManutenzione(utente, id);
            redirectAttributes.addFlashAttribute("successo", "Manutenzione annullata!");

        } catch (CampoException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }

        return "redirect:/campi";
    }
}
