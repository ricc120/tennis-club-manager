/**
 * lib/cms/gare.ts — Data fetching per le gare a squadre via Sanity GROQ.
 *
 * CONCETTO: GROQ (Graph-Relational Object Queries)
 *
 * GROQ è il linguaggio di query nativo di Sanity. La sintassi base è:
 *
 *   *[_type == "garaSquadre"]     → seleziona tutti i documenti di tipo garaSquadre
 *   | order(data desc)            → ordina per data decrescente (più recenti prima)
 *   [0...5]                       → prendi i primi 5 risultati (slice)
 *   { _id, data, ... }           → proietta solo i campi necessari
 *
 * ISR (Incremental Static Regeneration):
 *
 *   La fetch include { next: { revalidate: 3600 } } che dice a Next.js:
 *   "Crea una versione statica, ma rigenerala ogni 3600 secondi (1 ora)."
 *
 *   Questo garantisce:
 *   - Performance: la pagina è pre-renderizzata (nessun loading spinner)
 *   - Freschezza: i risultati si aggiornano al massimo entro 1 ora
 *
 * FALLBACK:
 *   Se il client Sanity non è configurato (projectId mancante),
 *   la funzione restituisce dati mock locali. Questo permette di
 *   sviluppare e testare l'UI senza un progetto Sanity attivo.
 */

import { getSanityClient, isSanityConfigured } from "@/sanity/client";
import type { GaraSanity, Gara } from "./types";
import { arricchisciGara } from "./types";

/**
 * Query GROQ per recuperare le ultime 5 gare a squadre.
 *
 * Struttura della query:
 *   *[_type == "garaSquadre"]  → filtra per tipo documento
 *   | order(data desc)         → ordina: più recenti prima
 *   [0...5]                    → limita a 5 risultati
 *   { ... }                    → proiezione campi
 */
const GARE_QUERY = `
  *[_type == "garaSquadre"] | order(data desc) [0...5] {
    _id,
    data,
    campionato,
    giornata,
    squadraCasa,
    squadraOspite,
    punteggioCasa,
    punteggioOspite,
    inCasa
  }
`;

/**
 * Recupera le ultime gare a squadre da Sanity con ISR.
 *
 * @returns Array di Gara arricchite (con risultato, isVittoria, luogo derivati)
 */
export async function getGareASquadre(): Promise<Gara[]> {
  try {
    const client = getSanityClient();
    const rawGare = await client.fetch<GaraSanity[]>(
      GARE_QUERY,
      {},
      {
        /**
         * ISR: rigenerazione ogni 3600 secondi (1 ora).
         *
         * Questo significa che Next.js:
         * 1. Genera la pagina al primo request
         * 2. Serve la versione cached per 1 ora
         * 3. Dopo 1 ora, rigenera in background al prossimo request
         */
        next: { revalidate: 3600 },
      }
    );

    return rawGare.map(arricchisciGara);
  } catch (error) {
    console.error("[CMS] Errore nel fetch delle gare:", error);
    // In caso di errore, ritorna array vuoto (l'UI mostra l'EmptyState)
    return [];
  }
}
