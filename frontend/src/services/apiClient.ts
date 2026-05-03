/**
 * apiClient.ts — Client HTTP centralizzato per comunicare con il backend.
 * 
 * STEP 7 FIX: Variabili d'ambiente per Docker
 * 
 * PROBLEMA: NEXT_PUBLIC_* viene "bruciata" nel JS al momento del BUILD.
 * In Docker, al build-time non esiste ancora il backend come "app:8080".
 * 
 * SOLUZIONE: usiamo DUE variabili:
 *   - API_URL (senza NEXT_PUBLIC_): letta a RUNTIME dal server Node.js
 *     → In Docker: http://app:8080 (networking tra container)
 *     → In locale: non definita → usa il fallback
 *   - NEXT_PUBLIC_API_URL: fallback per sviluppo locale e browser
 *     → http://localhost:8080
 * 
 * Questa funzione gira nei Server Components (sul server Node.js),
 * quindi può leggere variabili d'ambiente di runtime.
 */

// Priorità: API_URL (runtime/Docker) → NEXT_PUBLIC_API_URL (.env.local) → fallback
const API_BASE_URL = process.env.API_URL || process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

/**
 * Classe per gli errori API — estende Error con informazioni HTTP aggiuntive.
 * 
 * Quando il backend risponde con un errore (es: 404 Not Found, 500 Server Error),
 * lanciamo questa eccezione con lo status code per gestirla nel componente.
 * 
 * È simile alle tue eccezioni Java (PrenotazioneException, CampoException)
 * ma con lo status HTTP incluso.
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
 * PARAMETRI:
 * - endpoint: il percorso dell'API (es: "/api/campi")
 * - options: opzioni aggiuntive per fetch (metodo, headers, body)
 * 
 * RITORNA: i dati JSON parsati come oggetto TypeScript
 * 
 * TIPO GENERICO <T>:
 * Il <T> è un "generics" TypeScript (come i generics Java: List<Campo>).
 * Significa: "questa funzione restituisce un tipo che il chiamante decide".
 * 
 * Esempio:
 *   fetchApi<Campo[]>("/api/campi")   → ritorna Promise<Campo[]>
 *   fetchApi<Campo>("/api/campi/1")   → ritorna Promise<Campo>
 */
export async function fetchApi<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  // Costruisce l'URL completo: "http://localhost:8080" + "/api/campi"
  const url = `${API_BASE_URL}${endpoint}`;

  try {
    // fetch() invia la richiesta HTTP
    // "await" aspetta che il server risponda (potrebbe impiegare millisecondi o secondi)
    const response = await fetch(url, {
      // Spread operator (...) copia tutte le opzioni passate dal chiamante
      ...options,
      headers: {
        "Content-Type": "application/json",
        // Importa gli headers aggiuntivi (es: Authorization per JWT in futuro)
        ...options.headers,
      },
    });

    // Controlla se la risposta è un errore HTTP (status 4xx o 5xx)
    // response.ok è true solo se lo status è 200-299
    if (!response.ok) {
      // Prova a leggere il messaggio d'errore dal body JSON
      let errorMessage = `Errore HTTP ${response.status}`;
      try {
        const errorBody = await response.json();
        errorMessage = errorBody.errore || errorMessage;
      } catch {
        // Se il body non è JSON, usa il messaggio generico
      }
      throw new ApiError(errorMessage, response.status);
    }

    // Parsa il body della risposta da JSON a oggetto TypeScript
    // response.json() è come ObjectMapper.readValue() in Jackson
    const data: T = await response.json();
    return data;

  } catch (error) {
    // Se è già un ApiError (dal blocco sopra), rilancialo
    if (error instanceof ApiError) {
      throw error;
    }
    // Altrimenti è un errore di rete (backend non raggiungibile, DNS fallito, etc.)
    throw new ApiError(
      "Impossibile contattare il server. Verifica che il backend sia avviato.",
      0
    );
  }
}
