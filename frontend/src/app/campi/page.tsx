/**
 * Pagina /campi — Mostra i campi dal BACKEND (non più dati mock!).
 * 
 * CONCETTO CHIAVE: SERVER COMPONENTS ASYNC
 * 
 * In Next.js App Router, i componenti in app/ sono "Server Components" di default.
 * Questo significa che:
 * 1. Il codice gira sul SERVER Node.js, NON nel browser dell'utente
 * 2. La funzione può essere "async" → puoi usare "await" direttamente
 * 3. Il server esegue il fetch, costruisce l'HTML e lo invia al browser
 * 
 * È come Thymeleaf: il server (Node.js) chiama il backend (Spring Boot),
 * riceve i dati, li inserisce nell'HTML, e invia la pagina completa al browser.
 * 
 * VANTAGGI:
 * - SEO: i motori di ricerca vedono la pagina già completa
 * - Velocità: l'utente non vede uno spinner, la pagina arriva già con i dati
 * - Sicurezza: il codice del service/apiClient NON viene inviato al browser
 * 
 * CONFRONTO CON LO STEP 2 (dati mock):
 * 
 * PRIMA (Step 2):
 *   import { mockCampi } from "@/lib/mockData";
 *   const campi = mockCampi;   // dati hardcoded
 * 
 * ADESSO (Step 4):
 *   import { getCampi } from "@/services/campiService";
 *   const campi = await getCampi();  // dati dal database reale!
 */

import { getCampi } from "@/services/campiService";
import CampoCard from "@/components/CampoCard";
import { Campo } from "@/types";

// NOTA: "async" davanti alla funzione! Questo è possibile SOLO nei Server Components.
// In un Client Component ("use client") NON potresti farlo — vedremo dopo la differenza.
export default async function CampiPage() {
  // Questo codice gira sul SERVER Node.js:
  // 1. Node.js chiama http://localhost:8080/api/campi (verso Spring Boot)
  // 2. Spring Boot risponde con il JSON dei campi
  // 3. Node.js riceve i dati e costruisce l'HTML
  // 4. L'HTML completo viene inviato al browser

  let campi: Campo[];
  let errore = "";

  try {
    campi = await getCampi();
  } catch (error) {
    // Se il backend non è raggiungibile o risponde con errore,
    // mostriamo un messaggio all'utente invece di crashare
    errore = error instanceof Error
      ? error.message
      : "Errore nel caricamento dei campi";
    campi = [];
  }

  return (
    <div className="bg-white dark:bg-gray max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      {/* HEADER */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">
          🏟️ I Nostri Campi
        </h1>
        <p className="mt-2 text-gray-600">
          Visualizza tutti i campi del tennis club e il loro stato attuale.
        </p>
      </div>

      {/* MESSAGGIO DI ERRORE — mostrato solo se c'è un errore */}
      {errore && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
          <p className="font-semibold">⚠️ Errore di connessione</p>
          <p className="text-sm mt-1">{errore}</p>
          <p className="text-sm mt-2 text-red-500">
            Assicurati che il backend sia avviato su {process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"}
          </p>
        </div>
      )}

      {/* GRIGLIA dei campi */}
      {campi.length > 0 ? (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {campi.map((campo) => (
              <CampoCard key={campo.id} campo={campo} />
            ))}
          </div>

          {/* Riepilogo */}
          <div className="mt-8 p-4 bg-emerald-50 rounded-lg border border-emerald-200">
            <p className="text-sm text-emerald-800">
              📊 Totale campi: <strong>{campi.length}</strong> —{" "}
              Coperti: <strong>{campi.filter((c) => c.isCoperto).length}</strong> —{" "}
              Scoperti: <strong>{campi.filter((c) => !c.isCoperto).length}</strong>
            </p>
          </div>
        </>
      ) : (
        // Nessun campo e nessun errore → il database è vuoto
        !errore && (
          <div className="text-center py-16 text-gray-400">
            <p className="text-5xl mb-4">🏟️</p>
            <p className="text-lg">Nessun campo trovato nel database.</p>
          </div>
        )
      )}
    </div>
  );
}
