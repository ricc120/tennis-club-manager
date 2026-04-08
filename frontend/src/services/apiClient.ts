/**
 * apiClient.ts — Client HTTP centralizzato per comunicare con il backend.
 * 
 * CONCETTO CHIAVE: questo file è il "ponte" tra frontend e backend.
 * Invece di scrivere `fetch("http://localhost:8080/api/campi")` ovunque,
 * centralizziamo qui l'URL base e la gestione degli errori.
 * 
 * Se un giorno il backend si sposta su un altro URL (es: https://api.tennisclub.it),
 * basta cambiare la variabile d'ambiente — il codice non cambia.
 * 
 * CONCETTI NUOVI:
 * 
 * 1. fetch() — È come "curl" ma dal codice JavaScript.
 *    fetch(url) → invia una richiesta HTTP e ritorna una Promise (promessa di risposta)
 * 
 * 2. async/await — Modo di aspettare una risposta asincrona:
 *    const risposta = await fetch(url);  // aspetta che arrivi la risposta
 *    const dati = await risposta.json(); // aspetta che il JSON venga parsato
 * 
 * 3. process.env.NEXT_PUBLIC_API_URL — Legge la variabile d'ambiente dal file .env.local
 *    Il prefisso NEXT_PUBLIC_ la rende accessibile anche nel browser
 */

// Legge l'URL base dal file .env.local
// Se non è definito, usa localhost:8080 come fallback
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

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
