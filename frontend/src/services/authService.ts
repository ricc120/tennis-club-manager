/**
 * authService.ts — Service per l'autenticazione con JWT.
 * 
 * STEP 8: JWT Authentication
 * 
 * PRIMA (Step 5): login() restituiva solo l'utente
 *   const utente = await login({email, password});
 *   → { id: 1, nome: "Mario", ... }
 * 
 * ADESSO (Step 8): login() restituisce token + utente
 *   const { token, utente } = await login({email, password});
 *   → { token: "eyJhbGci...", utente: { id: 1, nome: "Mario", ... } }
 * 
 * Il token viene salvato in localStorage e usato da apiClient.ts
 * per autenticare tutte le richieste successive.
 */

import { LoginResponse } from "@/types";
import { fetchApi } from "./apiClient";

interface LoginData {
  email: string;
  password: string;
}

/**
 * Esegue il login e salva il JWT token.
 * 
 * FLUSSO:
 * 1. Invia email + password al backend
 * 2. Il backend verifica le credenziali
 * 3. Se valide, il backend genera un JWT token e lo restituisce
 * 4. Noi salviamo il token in localStorage
 * 5. Da ora, apiClient.ts lo includerà in ogni richiesta
 */
export async function login(data: LoginData): Promise<LoginResponse> {
  // Ora il tipo di ritorno è LoginResponse (token + utente)
  // invece di Utente (solo utente)
  const response = await fetchApi<LoginResponse>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(data),
  });

  // STEP 8: Salva il token JWT in localStorage
  // apiClient.ts lo leggerà automaticamente con getToken()
  if (typeof window !== "undefined" && response.token) {
    localStorage.setItem("jwt_token", response.token);
  }

  return response;
}

/**
 * Esegue il logout — rimuove il token da localStorage.
 * 
 * Con JWT, il logout è puramente lato CLIENT:
 * basta eliminare il token dal browser. Il server non tiene traccia
 * delle sessioni (è stateless), quindi non c'è nulla da invalidare.
 * 
 * DIFFERENZA con Spring Session:
 *   Spring: session.invalidate() → il server cancella la sessione
 *   JWT:    localStorage.removeItem("jwt_token") → il client cancella il token
 */
export function logout(): void {
  if (typeof window !== "undefined") {
    localStorage.removeItem("jwt_token");
    localStorage.removeItem("utente");
  }
}
