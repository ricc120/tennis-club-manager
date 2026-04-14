/**
 * authService.ts — Service per l'autenticazione.
 * 
 * CONCETTO: POST con fetch()
 * 
 * Finora abbiamo fatto solo GET (leggere dati).
 * Per il login serve POST (inviare dati):
 * 
 *   GET  = "dammi i dati"     → fetch(url)
 *   POST = "ecco i miei dati" → fetch(url, { method: "POST", body: ... })
 * 
 * La differenza:
 *   GET /api/campi                        → nessun body
 *   POST /api/auth/login + body JSON      → il body contiene email e password
 */

import { Utente } from "@/types";
import { fetchApi } from "./apiClient";

/**
 * Interfaccia per i dati di login.
 * È il "DTO" del frontend — corrisponde a LoginRequest.java nel backend.
 */
interface LoginData {
  email: string;
  password: string;
}

/**
 * Esegue il login inviando le credenziali al backend.
 * 
 * Chiama: POST /api/auth/login
 * Body: { "email": "...", "password": "..." }
 * Ritorna: Utente (se le credenziali sono valide)
 * Lancia: ApiError (se le credenziali sono errate o il server non risponde)
 * 
 * NOTA: fetchApi gestisce già la serializzazione del body e gli errori.
 * Noi dobbiamo solo passare le opzioni corrette.
 */
export async function login(data: LoginData): Promise<Utente> {
  return fetchApi<Utente>("/api/auth/login", {
    method: "POST",                          // metodo HTTP
    body: JSON.stringify(data),              // converte l'oggetto JS in stringa JSON
    // headers Content-Type è già impostato da fetchApi
  });
}
