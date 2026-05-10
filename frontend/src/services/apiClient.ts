/**
 * apiClient.ts — Client HTTP centralizzato per comunicare con il backend.
 * 
 * STEP 7: Variabili d'ambiente per Docker
 * STEP 8: JWT Authentication — inietta il token nell'header Authorization
 * 
 * FLUSSO CON JWT:
 * 1. L'utente fa login → il backend restituisce un JWT token
 * 2. Il frontend salva il token in localStorage
 * 3. Ad OGNI richiesta successiva, fetchApi() aggiunge l'header:
 *      Authorization: Bearer eyJhbGciOiJI...
 * 4. Il backend (JwtAuthFilter) valida il token e identifica l'utente
 * 
 * ANALOGIA con Spring Security:
 *   Spring MVC: il browser invia il cookie JSESSIONID automaticamente
 *   Next.js + JWT: NOI inviamo il token manualmente nell'header
 */

// Priorità: API_URL (runtime/Docker) → NEXT_PUBLIC_API_URL (.env.local) → fallback
const API_BASE_URL = process.env.API_URL || process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

/**
 * Recupera il JWT token da localStorage.
 * 
 * NOTA: localStorage è disponibile SOLO nel browser (Client Components).
 * Nei Server Components (che girano su Node.js), localStorage non esiste.
 * Per questo controlliamo typeof window !== "undefined".
 * 
 * Questo è un pattern molto comune in Next.js per codice
 * che deve funzionare sia sul server che nel browser.
 */
function getToken(): string | null {
  if (typeof window !== "undefined") {
    return localStorage.getItem("jwt_token");
  }
  return null;
}

/**
 * Classe per gli errori API.
 */
export class ApiError extends Error {
  status: number;

  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

/**
 * Funzione generica per fare richieste HTTP al backend.
 * 
 * STEP 8: Ora aggiunge automaticamente l'header Authorization
 * con il JWT token, se disponibile.
 * 
 * PRIMA (Step 4):
 *   headers: { "Content-Type": "application/json" }
 * 
 * ADESSO (Step 8):
 *   headers: {
 *     "Content-Type": "application/json",
 *     "Authorization": "Bearer eyJhbGci..."   ← NUOVO!
 *   }
 */
export async function fetchApi<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;

  // Prepara gli headers, includendo il token JWT se disponibile
  const token = getToken();
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...(options.headers as Record<string, string>),
  };

  // STEP 8: Se c'è un token salvato, aggiungilo all'header
  // Il formato è: "Bearer <token>" (standard OAuth2)
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (!response.ok) {
      let errorMessage = `Errore HTTP ${response.status}`;
      try {
        const errorBody = await response.json();
        errorMessage = errorBody.errore || errorMessage;
      } catch {
        // Se il body non è JSON, usa il messaggio generico
      }
      throw new ApiError(errorMessage, response.status);
    }

    const data: T = await response.json();
    return data;

  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(
      "Impossibile contattare il server. Verifica che il backend sia avviato.",
      0
    );
  }
}
