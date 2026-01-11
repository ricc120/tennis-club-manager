package it.tennis_club;

import it.tennis_club.business_logic.CampoException;
import it.tennis_club.business_logic.CampoService;
import it.tennis_club.domain_model.Campo;
import it.tennis_club.domain_model.Manutenzione;
import it.tennis_club.domain_model.Utente;

import java.time.LocalDate;
import java.util.List;

/**
 * Esempio di utilizzo del CampoService con controllo dei permessi.
 */
public class CampoServiceExample {

    public static void main(String[] args) {
        CampoService campoService = new CampoService();

        // Simula utenti con ruoli diversi
        Utente admin = new Utente(1, "Mario", "Rossi", "admin@tennis.it", "password", Utente.Ruolo.ADMIN);
        Utente manutentore = new Utente(2, "Luigi", "Verdi", "manutentore@tennis.it", "password",
                Utente.Ruolo.MANUTENTORE);
        Utente socio = new Utente(3, "Anna", "Bianchi", "socio@tennis.it", "password", Utente.Ruolo.SOCIO);

        System.out.println("=== ESEMPIO DI UTILIZZO DEL CAMPO SERVICE ===\n");

        // ========== OPERAZIONI PUBBLICHE (tutti possono accedere) ==========

        System.out.println("1. OPERAZIONI PUBBLICHE - Consultazione campi");
        System.out.println("-".repeat(50));

        try {
            // Tutti gli utenti possono vedere i campi
            List<Campo> campi = campoService.getAllCampi();
            System.out.println("Campi disponibili: " + campi.size());
            for (Campo campo : campi) {
                System.out.println("  - " + campo.getNome() + " (" + campo.getTipoSuperficie() + ")");
            }

            // Filtra per campi coperti
            List<Campo> campiCoperti = campoService.getCampiCoperti();
            System.out.println("\nCampi coperti: " + campiCoperti.size());

        } catch (CampoException e) {
            System.err.println("Errore: " + e.getMessage());
        }

        // ========== OPERAZIONI RISERVATE - ADMIN ==========

        System.out.println("\n\n2. OPERAZIONI RISERVATE - Utente ADMIN");
        System.out.println("-".repeat(50));

        try {
            // L'admin può creare una manutenzione
            Integer idManutenzione = campoService.creaManutenzione(
                    admin,
                    1, // ID campo
                    LocalDate.now(),
                    "Rifacimento superficie campo centrale");
            System.out.println("✓ ADMIN: Manutenzione creata con ID: " + idManutenzione);

            // L'admin può visualizzare le manutenzioni
            List<Manutenzione> manutenzioni = campoService.getManutenzioniCampo(admin, 1);
            System.out.println("✓ ADMIN: Manutenzioni trovate: " + manutenzioni.size());

        } catch (CampoException e) {
            System.err.println("✗ Errore: " + e.getMessage());
        }

        // ========== OPERAZIONI RISERVATE - MANUTENTORE ==========

        System.out.println("\n\n3. OPERAZIONI RISERVATE - Utente MANUTENTORE");
        System.out.println("-".repeat(50));

        try {
            // Il manutentore può creare una manutenzione
            Integer idManutenzione = campoService.creaManutenzione(
                    manutentore,
                    2, // ID campo
                    LocalDate.now(),
                    "Controllo illuminazione campo 2");
            System.out.println("✓ MANUTENTORE: Manutenzione creata con ID: " + idManutenzione);

            // Il manutentore può completare una manutenzione
            campoService.completaManutenzione(manutentore, idManutenzione, LocalDate.now());
            System.out.println("✓ MANUTENTORE: Manutenzione completata");

        } catch (CampoException e) {
            System.err.println("✗ Errore: " + e.getMessage());
        }

        // ========== TENTATIVO DI ACCESSO NON AUTORIZZATO ==========

        System.out.println("\n\n4. TENTATIVO DI ACCESSO NON AUTORIZZATO - Utente SOCIO");
        System.out.println("-".repeat(50));

        try {
            // Un socio NON può creare manutenzioni
            campoService.creaManutenzione(
                    socio,
                    1,
                    LocalDate.now(),
                    "Tentativo non autorizzato");
            System.out.println("✗ ERRORE: Il socio non dovrebbe poter creare manutenzioni!");

        } catch (CampoException e) {
            // Questo è il comportamento atteso
            System.out.println("✓ CORRETTO: " + e.getMessage());
        }

        try {
            // Un socio NON può visualizzare le manutenzioni
            campoService.getManutenzioniCampo(socio, 1);
            System.out.println("✗ ERRORE: Il socio non dovrebbe poter visualizzare le manutenzioni!");

        } catch (CampoException e) {
            // Questo è il comportamento atteso
            System.out.println("✓ CORRETTO: " + e.getMessage());
        }

        // ========== RIEPILOGO ==========

        System.out.println("\n\n" + "=".repeat(50));
        System.out.println("RIEPILOGO CONTROLLO PERMESSI");
        System.out.println("=".repeat(50));
        System.out.println("✓ Operazioni pubbliche: accessibili a TUTTI");
        System.out.println("  - getAllCampi()");
        System.out.println("  - getCampoById()");
        System.out.println("  - getCampiCoperti()");
        System.out.println("  - getCampiByTipoSuperficie()");
        System.out.println();
        System.out.println("✓ Operazioni riservate: solo ADMIN e MANUTENTORE");
        System.out.println("  - creaManutenzione()");
        System.out.println("  - completaManutenzione()");
        System.out.println("  - annullaManutenzione()");
        System.out.println("  - getManutenzioniCampo()");
        System.out.println("=".repeat(50));
    }
}
