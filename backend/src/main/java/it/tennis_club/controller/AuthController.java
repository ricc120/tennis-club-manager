package it.tennis_club.controller;

import it.tennis_club.business_logic.AuthService;
import it.tennis_club.business_logic.AuthenticationException;
import it.tennis_club.domain_model.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

/**
 * Controller per la gestione dell'autenticazione web.
 * Gestisce login, logout e registrazione tramite pagine HTML.
 */
@Controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Mostra la pagina di login.
     */
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    /**
     * Processa il form di login.
     */
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Utente utente = authService.login(email, password);

            if (utente != null) {
                // Salva l'utente nella sessione HTTP
                session.setAttribute("utente", utente);
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "Credenziali non valide");
                return "redirect:/login";
            }

        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    /**
     * Mostra la pagina di registrazione.
     */
    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    /**
     * Processa il form di registrazione.
     */
    @PostMapping("/register")
    public String processRegister(
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            // Crea un nuovo utente con ruolo SOCIO (registrazione pubblica)
            Utente nuovoUtente = new Utente();
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome(cognome);
            nuovoUtente.setEmail(email);
            nuovoUtente.setPassword(password);
            nuovoUtente.setRuolo(Utente.Ruolo.SOCIO);

            // Registra l'utente (senza creare sessione nel SessionManager legacy)
            Integer id = authService.registrazioneSenzaSessione(nuovoUtente);
            nuovoUtente.setId(id);

            // Salva nella sessione HTTP e redirect alla home
            session.setAttribute("utente", nuovoUtente);
            redirectAttributes.addFlashAttribute("success", "Registrazione completata!");
            return "redirect:/";

        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Effettua il logout.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Logout effettuato con successo");
        return "redirect:/login";
    }

    /**
     * Homepage / Dashboard.
     * Redirect a login se non autenticato.
     */
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente == null) {
            return "redirect:/login";
        }

        model.addAttribute("utente", utente);
        return "home";
    }
}
