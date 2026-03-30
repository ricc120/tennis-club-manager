/**
 * types/index.ts — Definizione dei tipi TypeScript per il Tennis Club.
 * 
 * CONCETTO CHIAVE: le "interfacce" TypeScript descrivono la FORMA dei dati.
 * Non creano oggetti (come le classi Java), ma dicono al compilatore
 * "questo oggetto DEVE avere questi campi con questi tipi".
 * 
 * Se nel codice scrivi: const utente: Utente = { nome: 123 }
 * TypeScript ti dà errore perché "nome" deve essere una stringa, non un numero.
 * 
 * MAPPING CON IL BACKEND:
 * Ogni interfaccia qui corrisponde a una classe Java in domain_model/
 * I tipi Java → TypeScript:
 *   Integer  → number
 *   String   → string  (minuscolo!)
 *   Boolean  → boolean (minuscolo!)
 *   LocalDate → string  (arriva come "2026-03-30" dal JSON)
 *   LocalTime → string  (arriva come "14:00" dal JSON)
 *   enum     → union type ("ADMIN" | "SOCIO" | ...)
 */

// ============================================================
// RUOLO — Corrisponde a Utente.Ruolo nel backend
// ============================================================
/**
 * Union Type: equivale all'enum Java.
 * In Java:       enum Ruolo { ADMIN, MAESTRO, SOCIO, ALLIEVO, MANUTENTORE }
 * In TypeScript:  type Ruolo = "ADMIN" | "MAESTRO" | "SOCIO" | "ALLIEVO" | "MANUTENTORE"
 * 
 * Il "|" significa "oppure": una variabile Ruolo può essere SOLO uno di questi valori.
 */
export type Ruolo = "ADMIN" | "MAESTRO" | "SOCIO" | "ALLIEVO" | "MANUTENTORE";

// ============================================================
// UTENTE — Corrisponde a domain_model/Utente.java
// ============================================================
/**
 * "export" rende l'interfaccia importabile da altri file:
 *   import { Utente } from "@/types";
 * 
 * Nota: NON includiamo "password" — il frontend non deve mai ricevere la password.
 * Il backend dovrà fare attenzione a non includerla nella risposta JSON.
 */
export interface Utente {
  id: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: Ruolo;
}

// ============================================================
// CAMPO — Corrisponde a domain_model/Campo.java
// ============================================================
export interface Campo {
  id: number;
  nome: string;
  tipoSuperficie: string;    // es: "Terra Rossa", "Erba", "Cemento"
  isCoperto: boolean;
}

// ============================================================
// PRENOTAZIONE — Corrisponde a domain_model/Prenotazione.java
// ============================================================
/**
 * Nota i campi "campo" e "socio": sono oggetti NESTED (annidati).
 * In Java avevi: private Campo campo; private Utente socio;
 * Qui è uguale: campo è di tipo Campo, socio è di tipo Utente.
 */
export interface Prenotazione {
  id: number;
  data: string;        // "2026-03-30" — formato ISO date
  oraInizio: string;   // "14:00" — formato HH:mm
  campo: Campo;        // oggetto Campo completo
  socio: Utente;       // oggetto Utente completo
}

// ============================================================
// LEZIONE — Corrisponde a domain_model/Lezione.java
// ============================================================
export interface Lezione {
  id: number;
  prenotazione: Prenotazione;  // contiene data, orario e campo
  maestro: Utente;
  descrizione: string;
}

// ============================================================
// STATO MANUTENZIONE — Corrisponde a Manutenzione.Stato
// ============================================================
export type StatoManutenzione = "IN_CORSO" | "COMPLETATA" | "ANNULLATA";

// ============================================================
// MANUTENZIONE — Corrisponde a domain_model/Manutenzione.java
// ============================================================
export interface Manutenzione {
  id: number;
  campo: Campo;
  manutentore: Utente;
  dataInizio: string;
  dataFine: string | null;  // può essere null se la manutenzione è in corso
  descrizione: string;
  stato: StatoManutenzione;
}
