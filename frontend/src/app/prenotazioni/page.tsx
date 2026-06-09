/**
 * Pagina /prenotazioni — STEP 9: Refactoring con TanStack Query.
 *
 * PRIMA (Step 6 — Server Component):
 *   export default async function PrenotazioniPage() {
 *     const prenotazioni = await getPrenotazioni();  // fetch sul SERVER Node.js
 *     const campi = await getCampi();
 *     return <div>...</div>;
 *   }
 *
 * ADESSO (Step 9 — Client Component con React Query):
 *   export default function PrenotazioniPage() {
 *     const { data, isLoading, error } = usePrenotazioni();  // fetch nel BROWSER
 *     const { data: campi } = useCampi();
 *     return <div>...</div>;
 *   }
 *
 * DIFFERENZE CHIAVE:
 *
 * 1. "use client" — ora è un Client Component (gira nel browser)
 * 2. Non è più "async" — useQuery gestisce l'asincronia internamente
 * 3. Gli stati (loading, error) sono gestiti da React Query, non da try/catch
 * 4. La cache evita fetch ripetuti quando navighi tra pagine
 * 5. Dopo POST/DELETE, la lista si aggiorna senza ricaricare la pagina
 *
 * CONCETTO: Perché convertire da Server a Client Component?
 *
 * Server Component:
 *   ✅ SEO (la pagina arriva già con i dati)
 *   ✅ Nessun spinner di loading
 *   ❌ router.refresh() ricarica TUTTA la pagina dopo POST/DELETE
 *   ❌ Nessun caching (ogni navigazione rifa il fetch)
 *
 * Client Component + React Query:
 *   ✅ Cache automatica (navigazione istantanea)
 *   ✅ Aggiornamento chirurgico dopo POST/DELETE (invalidateQueries)
 *   ✅ Stati di loading/error gestiti dal framework
 *   ❌ Spinner al primo caricamento (la pagina è inizialmente vuota)
 *
 * Per una pagina INTERATTIVA come prenotazioni (con form e bottoni),
 * i vantaggi del Client Component superano quelli del Server Component.
 *
 * NOTA: /campi resta un Server Component — è una pagina di sola lettura,
 * dove il Server Component è la scelta migliore.
 */
"use client";

import { usePrenotazioni } from "@/hooks/usePrenotazioni";
import { useCampi } from "@/hooks/useCampi";
import PrenotazioneCard from "@/components/PrenotazioneCard";
import NuovaPrenotazioneForm from "@/components/NuovaPrenotazioneForm";

export default function PrenotazioniPage() {
  // useQuery gestisce TUTTO: fetch, loading, error, cache, refetch
  const {
    data: prenotazioni = [],  // default a [] se undefined (durante il loading)
    isLoading: isLoadingPrenotazioni,
    error: errorPrenotazioni,
  } = usePrenotazioni();

  const {
    data: campi = [],
    isLoading: isLoadingCampi,
  } = useCampi();

  const isLoading = isLoadingPrenotazioni || isLoadingCampi;

  return (
    <div className="bg-white dark:bg-grey max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      {/* HEADER */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">
          📅 Prenotazioni
        </h1>
        <p className="mt-2 text-gray-600">
          Visualizza, crea e gestisci le prenotazioni dei campi.
        </p>
      </div>

      {/* STATO DI LOADING — STEP 9 */}
      {/*
        PRIMA: la pagina arrivava già con i dati (Server Component).
        ADESSO: mostriamo uno spinner mentre React Query fa il fetch.
        
        Questo è il trade-off: leggermente più lento al primo caricamento,
        ma MOLTO più veloce nelle interazioni successive (cache, invalidation).
      */}
      {isLoading && (
        <div className="flex items-center justify-center py-20">
          <div className="text-center">
            <div className="animate-spin h-10 w-10 border-4 border-emerald-500 border-t-transparent rounded-full mx-auto mb-4"></div>
            <p className="text-gray-500">Caricamento prenotazioni...</p>
          </div>
        </div>
      )}

      {/* ERRORE */}
      {errorPrenotazioni && !isLoading && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
          <p className="font-semibold">⚠️ Errore di connessione</p>
          <p className="text-sm mt-1">
            {errorPrenotazioni instanceof Error
              ? errorPrenotazioni.message
              : "Errore nel caricamento dei dati"}
          </p>
        </div>
      )}

      {/* CONTENUTO — mostrato solo quando i dati sono pronti */}
      {!isLoading && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* COLONNA SINISTRA — Form di creazione */}
          <div className="lg:col-span-1">
            <NuovaPrenotazioneForm campi={campi} />
          </div>

          {/* COLONNA DESTRA — Lista prenotazioni */}
          <div className="lg:col-span-2">
            {prenotazioni.length === 0 && !errorPrenotazioni ? (
              <div className="text-center py-16 text-gray-400">
                <p className="text-5xl mb-4">📭</p>
                <p className="text-lg">Nessuna prenotazione trovata.</p>
                <p className="text-sm mt-2">Usa il form a sinistra per crearne una!</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {prenotazioni.map((prenotazione) => (
                  <PrenotazioneCard
                    key={prenotazione.id}
                    prenotazione={prenotazione}
                  />
                ))}
              </div>
            )}

            {/* Riepilogo */}
            {prenotazioni.length > 0 && (
              <div className="mt-6 p-4 bg-emerald-50 rounded-lg border border-emerald-200">
                <p className="text-sm text-emerald-800">
                  📊 Totale prenotazioni: <strong>{prenotazioni.length}</strong>
                </p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
