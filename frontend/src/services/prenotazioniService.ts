/**
 * prenotazioniService.ts — Service CRUD per le Prenotazioni.
 * 
 * STEP 6: Aggiunto creaPrenotazione (POST) e cancellaPrenotazione (DELETE).
 * 
 *   CRUD completo:
 *   getPrenotazioni()        → GET    /api/prenotazioni       (Read all)
 *   getPrenotazioneById(id)  → GET    /api/prenotazioni/{id}  (Read one)
 *   creaPrenotazione(data)   → POST   /api/prenotazioni       (Create)
 *   cancellaPrenotazione(id) → DELETE /api/prenotazioni/{id}  (Delete)
 */

import { Prenotazione } from "@/types";
import { fetchApi } from "./apiClient";

// ==================== READ ====================

export async function getPrenotazioni(): Promise<Prenotazione[]> {
  return fetchApi<Prenotazione[]>("/api/prenotazioni");
}

export async function getPrenotazioneById(id: number): Promise<Prenotazione> {
  return fetchApi<Prenotazione>(`/api/prenotazioni/${id}`);
}

// ==================== CREATE ====================

/**
 * Interfaccia per i dati di creazione prenotazione.
 * Corrisponde a PrenotazioneRequest.java nel backend.
 * 
 * NOTA: inviamo gli ID (idCampo, idSocio), non gli oggetti interi.
 * Il backend si occupa di recuperarli dal database.
 */
export interface NuovaPrenotazione {
  data: string;        // "2026-04-15" formato ISO
  oraInizio: string;   // "14:00" formato HH:mm
  idCampo: number;     // ID del campo scelto
  idSocio: number;     // ID dell'utente che prenota
}

/**
 * Crea una nuova prenotazione.
 * 
 * CONCETTO: fetch POST
 * 
 * A differenza di GET (che non ha body), POST richiede:
 * - method: "POST" 
 * - body: JSON.stringify(dati) — converte l'oggetto JS in stringa JSON
 * 
 * Il backend (ApiPrenotazioneController) riceve il JSON e:
 * 1. Lo deserializza in PrenotazioneRequest (DTO)
 * 2. Valida i dati (data non nel passato, campo disponibile, etc.)
 * 3. Crea la prenotazione nel database
 * 4. Restituisce la prenotazione completa con HTTP 201
 */
export async function creaPrenotazione(dati: NuovaPrenotazione): Promise<Prenotazione> {
  return fetchApi<Prenotazione>("/api/prenotazioni", {
    method: "POST",
    body: JSON.stringify(dati),
  });
}

// ==================== DELETE ====================

/**
 * Cancella una prenotazione per ID.
 * 
 * CONCETTO: fetch DELETE
 * 
 * DELETE è simile a GET: l'ID è nell'URL, non c'è body.
 * 
 * Il backend restituisce HTTP 204 No Content (nessun body).
 * Quindi il tipo di ritorno è void — non c'è nulla da parsare.
 * 
 * Usiamo fetchApi<void> ma dobbiamo gestire il caso speciale
 * in cui la risposta è vuota.
 */
export async function cancellaPrenotazione(id: number): Promise<void> {
  const API_BASE_URL = process.env.API_URL || process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
  
  // STEP 8: Prepara gli headers con il token JWT
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  if (typeof window !== "undefined") {
    const token = localStorage.getItem("jwt_token");
    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }
  }

  const response = await fetch(`${API_BASE_URL}/api/prenotazioni/${id}`, {
    method: "DELETE",
    headers,
  });

  if (!response.ok) {
    const errorBody = await response.json().catch(() => ({}));
    throw new Error(errorBody.errore || `Errore HTTP ${response.status}`);
  }
  // HTTP 204 → nessun body da parsare, ritorno void
}
