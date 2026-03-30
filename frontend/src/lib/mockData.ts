/**
 * lib/mockData.ts — Dati finti per sviluppare l'UI senza backend.
 * 
 * CONCETTO CHIAVE: durante lo sviluppo frontend, usiamo dati hardcoded
 * per costruire e testare i componenti PRIMA di collegare il backend.
 * 
 * Vantaggi:
 * 1. Non serve avviare Spring Boot per lavorare sul frontend
 * 2. Puoi testare casi edge (nomi lunghi, campi coperti/scoperti, etc.)
 * 3. Se sbagli un campo, TypeScript ti avvisa subito
 * 
 * In futuro (Step 4) questi dati verranno sostituiti da chiamate API reali.
 * 
 * NOTA SULLA SINTASSI:
 * "as const" dopo gli array non è necessario qui, ma nota come TypeScript
 * può verificare che ogni oggetto rispetti l'interfaccia importata.
 * Prova a cambiare "nome: 'Mario'" in "nome: 123" — vedrai l'errore!
 */

import { Utente, Campo, Prenotazione } from "@/types";

// ============================================================
// UTENTI MOCK
// ============================================================
/**
 * ": Utente[]" dice a TypeScript: "questa è un array di oggetti Utente".
 * Se un oggetto non ha tutti i campi richiesti → errore di compilazione.
 */
export const mockUtenti: Utente[] = [
  {
    id: 1,
    nome: "Mario",
    cognome: "Rossi",
    email: "mario.rossi@email.com",
    ruolo: "ADMIN",
  },
  {
    id: 2,
    nome: "Laura",
    cognome: "Bianchi",
    email: "laura.bianchi@email.com",
    ruolo: "SOCIO",
  },
  {
    id: 3,
    nome: "Paolo",
    cognome: "Verdi",
    email: "paolo.verdi@email.com",
    ruolo: "MAESTRO",
  },
  {
    id: 4,
    nome: "Giulia",
    cognome: "Neri",
    email: "giulia.neri@email.com",
    ruolo: "ALLIEVO",
  },
];

// ============================================================
// CAMPI MOCK
// ============================================================
export const mockCampi: Campo[] = [
  {
    id: 1,
    nome: "Campo Centrale",
    tipoSuperficie: "Terra Rossa",
    isCoperto: false,
  },
  {
    id: 2,
    nome: "Campo 2",
    tipoSuperficie: "Erba Sintetica",
    isCoperto: true,
  },
  {
    id: 3,
    nome: "Campo 3",
    tipoSuperficie: "Cemento",
    isCoperto: false,
  },
  {
    id: 4,
    nome: "Campo Coperto A",
    tipoSuperficie: "Terra Rossa",
    isCoperto: true,
  },
  {
    id: 5,
    nome: "Campo 5",
    tipoSuperficie: "Erba Sintetica",
    isCoperto: false,
  },
  {
    id: 6,
    nome: "Campo VIP",
    tipoSuperficie: "Erba Naturale",
    isCoperto: true,
  },
];

// ============================================================
// PRENOTAZIONI MOCK
// ============================================================
/**
 * Nota come "campo" e "socio" sono oggetti completi, non semplici ID.
 * Questo è lo stesso pattern che usa il tuo backend Java:
 *   private Campo campo;    // non "private Integer idCampo;"
 *   private Utente socio;   // non "private Integer idSocio;"
 */
export const mockPrenotazioni: Prenotazione[] = [
  {
    id: 1,
    data: "2026-03-30",
    oraInizio: "09:00",
    campo: mockCampi[0],   // Campo Centrale — riutilizziamo gli oggetti!
    socio: mockUtenti[1],  // Laura Bianchi
  },
  {
    id: 2,
    data: "2026-03-30",
    oraInizio: "11:00",
    campo: mockCampi[1],   // Campo 2
    socio: mockUtenti[1],  // Laura Bianchi (stessa persona, due prenotazioni)
  },
  {
    id: 3,
    data: "2026-03-31",
    oraInizio: "15:00",
    campo: mockCampi[2],   // Campo 3
    socio: mockUtenti[0],  // Mario Rossi
  },
  {
    id: 4,
    data: "2026-04-01",
    oraInizio: "10:00",
    campo: mockCampi[3],   // Campo Coperto A
    socio: mockUtenti[3],  // Giulia Neri
  },
];
