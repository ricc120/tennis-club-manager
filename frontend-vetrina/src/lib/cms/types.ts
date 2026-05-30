/**
 * lib/cms/types.ts — Interfacce TypeScript per i dati dal CMS.
 *
 * CONCETTO: Type Safety per dati Sanity
 *
 * Le interfacce qui definite corrispondono ai campi dello schema
 * Sanity in src/sanity/schemas/garaSquadre.ts.
 *
 * MAPPING SCHEMA → INTERFACCIA:
 *   Schema Sanity (garaSquadre)    →    TypeScript (GaraSanity)
 *   _id                            →    _id
 *   data (datetime)                →    data (string ISO)
 *   campionato (string)            →    campionato
 *   giornata (number)              →    giornata
 *   squadraCasa (string)           →    squadraCasa
 *   squadraOspite (string)         →    squadraOspite
 *   punteggioCasa (number)         →    punteggioCasa
 *   punteggioOspite (number)       →    punteggioOspite
 *   inCasa (boolean)               →    inCasa
 *
 * CAMPI DERIVATI (calcolati nel codice, NON nello schema):
 *   risultato   → `${punteggioCasa}-${punteggioOspite}`
 *   isVittoria  → il club ha segnato più punti dell'avversario
 *   luogo       → inCasa ? "Casa" : "Trasferta"
 */

/**
 * Dati grezzi restituiti dalla query GROQ di Sanity.
 * Rispecchia esattamente i campi dello schema CMS.
 */
export interface GaraSanity {
  _id: string;
  data: string;
  campionato: string;
  giornata: number;
  squadraCasa: string;
  squadraOspite: string;
  punteggioCasa: number;
  punteggioOspite: number;
  inCasa: boolean;
}

/**
 * Dati arricchiti usati dalla UI (TeamResults.tsx).
 * Estende GaraSanity con i campi derivati.
 */
export interface Gara extends GaraSanity {
  /** Risultato formattato "X-Y" (derivato da punteggioCasa/punteggioOspite) */
  risultato: string;
  /** Se il club ha vinto (derivato dai punteggi + inCasa) */
  isVittoria: boolean;
  /** "Casa" o "Trasferta" (derivato da inCasa) */
  luogo: string;
}

/**
 * Trasforma i dati grezzi da Sanity nei dati arricchiti per la UI.
 *
 * @param raw - Dati grezzi dalla query GROQ
 * @returns Dati con campi derivati pronti per la presentazione
 */
export function arricchisciGara(raw: GaraSanity): Gara {
  const risultato = `${raw.punteggioCasa}-${raw.punteggioOspite}`;
  const luogo = raw.inCasa ? "Casa" : "Trasferta";

  // Il club vince se:
  // - Gioca in casa E ha più punti
  // - Gioca fuori E ha più punti (come ospite)
  const puntiClub = raw.inCasa ? raw.punteggioCasa : raw.punteggioOspite;
  const puntiAvversario = raw.inCasa ? raw.punteggioOspite : raw.punteggioCasa;
  const isVittoria = puntiClub > puntiAvversario;

  return { ...raw, risultato, isVittoria, luogo };
}

/**
 * Rappresenta un evento generico del club.
 * Predisposto per future estensioni del CMS.
 */
export interface Evento {
  _id: string;
  titolo: string;
  descrizione: string;
  data: string;
  luogo: string;
  immagine?: string;
  categoria: "torneo" | "sociale" | "academy" | "altro";
}
