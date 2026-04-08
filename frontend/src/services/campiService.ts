/**
 * campiService.ts — Service per comunicare con le API dei campi.
 * 
 * PATTERN: Service Layer (lo stesso del backend!)
 * 
 * Backend Java:       Controller → CampoService → CampoDAO → Database
 * Frontend TypeScript: Pagina → campiService → apiClient → Backend API
 * 
 * Ogni service wrappa l'apiClient per un'entità specifica.
 * La pagina non sa nulla di URL, fetch, o HTTP — chiama solo getCampi().
 */

import { Campo } from "@/types";
import { fetchApi } from "./apiClient";

/**
 * Recupera tutti i campi dal backend.
 * 
 * Chiama: GET /api/campi
 * Ritorna: Promise<Campo[]>
 * 
 * "Promise<Campo[]>" significa: "questa funzione ritorna UNA PROMESSA
 * che, quando risolta, conterrà un array di oggetti Campo".
 * 
 * In Java sarebbe: CompletableFuture<List<Campo>>
 * Ma in pratica con async/await lo usi come codice sincrono:
 *   const campi = await getCampi();  // aspetta e ottiene Campo[]
 */
export async function getCampi(): Promise<Campo[]> {
  return fetchApi<Campo[]>("/api/campi");
}

/**
 * Recupera un singolo campo per ID.
 * 
 * Chiama: GET /api/campi/{id}
 */
export async function getCampoById(id: number): Promise<Campo> {
  return fetchApi<Campo>(`/api/campi/${id}`);
}
