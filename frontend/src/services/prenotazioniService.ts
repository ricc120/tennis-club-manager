/**
 * prenotazioniService.ts — Service per le API delle prenotazioni.
 * 
 * Stessa struttura di campiService.ts:
 *   Pagina → prenotazioniService → apiClient → Backend Spring Boot
 * 
 * In futuro qui aggiungeremo anche:
 *   creaPrenotazione(data) → POST /api/prenotazioni
 *   cancellaPrenotazione(id) → DELETE /api/prenotazioni/{id}
 */

import { Prenotazione } from "@/types";
import { fetchApi } from "./apiClient";

/**
 * Recupera tutte le prenotazioni dal backend.
 * Chiama: GET /api/prenotazioni
 */
export async function getPrenotazioni(): Promise<Prenotazione[]> {
  return fetchApi<Prenotazione[]>("/api/prenotazioni");
}

/**
 * Recupera una singola prenotazione per ID.
 * Chiama: GET /api/prenotazioni/{id}
 */
export async function getPrenotazioneById(id: number): Promise<Prenotazione> {
  return fetchApi<Prenotazione>(`/api/prenotazioni/${id}`);
}
