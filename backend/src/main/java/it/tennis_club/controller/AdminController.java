package it.tennis_club.controller;

import it.tennis_club.domain_model.Utente;
import it.tennis_club.domain_model.Utente.Ruolo;
import it.tennis_club.orm.UtenteDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller per la gestione degli utenti da parte dell'amministratore.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UtenteDAO utenteDAO;

    @Autowired
    public AdminController(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /**
     * Mostra la lista di tutti gli utenti (solo ADMIN).
     */
    @GetMapping("/utenti")
    public String listaUtenti(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }
        if (utente.getRuolo() != Ruolo.ADMIN) {
            redirectAttributes.addFlashAttribute("errore", "Accesso non autorizzato.");
            return "redirect:/";
        }

        try {
            List<Utente> utenti = utenteDAO.getAllUtenti();
            model.addAttribute("utenti", utenti);
            model.addAttribute("utente", utente);
            model.addAttribute("ruoli", Ruolo.values());
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errore", "Errore nel caricamento utenti: " + e.getMessage());
            return "redirect:/";
        }

        return "admin";
    }

    /**
     * Modifica il ruolo di un utente (solo ADMIN).
     */
    @PostMapping("/utenti/{id}/ruolo")
    public String modificaRuolo(
            @PathVariable Integer id,
            @RequestParam String nuovoRuolo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }
        if (utente.getRuolo() != Ruolo.ADMIN) {
            redirectAttributes.addFlashAttribute("errore", "Accesso non autorizzato.");
            return "redirect:/";
        }

        try {
            Ruolo ruolo = Ruolo.valueOf(nuovoRuolo);
            boolean aggiornato = utenteDAO.updateRuolo(id, ruolo);
            if (aggiornato) {
                redirectAttributes.addFlashAttribute("successo", "Ruolo aggiornato con successo!");
            } else {
                redirectAttributes.addFlashAttribute("errore", "Utente non trovato.");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errore", "Ruolo non valido: " + nuovoRuolo);
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errore", "Errore nell'aggiornamento: " + e.getMessage());
        }

        return "redirect:/admin/utenti";
    }

    /**
     * Elimina un utente (solo ADMIN).
     */
    @PostMapping("/utenti/{id}/elimina")
    public String eliminaUtente(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }
        if (utente.getRuolo() != Ruolo.ADMIN) {
            redirectAttributes.addFlashAttribute("errore", "Accesso non autorizzato.");
            return "redirect:/";
        }

        // Non permettere all'admin di auto-eliminarsi
        if (utente.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errore", "Non puoi eliminare il tuo stesso account.");
            return "redirect:/admin/utenti";
        }

        try {
            boolean eliminato = utenteDAO.deleteUtente(id);
            if (eliminato) {
                redirectAttributes.addFlashAttribute("successo", "Utente eliminato con successo!");
            } else {
                redirectAttributes.addFlashAttribute("errore", "Utente non trovato.");
            }
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errore", "Errore nell'eliminazione: " + e.getMessage());
        }

        return "redirect:/admin/utenti";
    }
}
