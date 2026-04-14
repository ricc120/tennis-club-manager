/**
 * Pagina /prenotazioni — Lista prenotazioni dal BACKEND.
 * 
 * Stessa logica della pagina /campi:
 * 1. Server Component async → il fetch avviene sul server
 * 2. try/catch → gestisce errori di connessione
 * 3. .map() → renderizza un PrenotazioneCard per ogni elemento
 * 
 * NOTA SULLA GESTIONE ERRORI:
 * Il pattern try/catch qui è identico a quello Java:
 * 
 * Java:
 *   try {
 *       List<Prenotazione> prenotazioni = service.getPrenotazioni();
 *   } catch (PrenotazioneException e) {
 *       // gestisci errore
 *   }
 * 
 * TypeScript:
 *   try {
 *       const prenotazioni = await getPrenotazioni();
 *   } catch (error) {
 *       // gestisci errore
 *   }
 */

import { getPrenotazioni } from "@/services/prenotazioniService";
import PrenotazioneCard from "@/components/PrenotazioneCard";
import { Prenotazione } from "@/types";

export default async function PrenotazioniPage() {
  let prenotazioni: Prenotazione[];
  let errore = "";

  try {
    prenotazioni = await getPrenotazioni();
  } catch (error) {
    errore = error instanceof Error
      ? error.message
      : "Errore nel caricamento delle prenotazioni";
    prenotazioni = [];
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      {/* HEADER */}
      <div className="mb-8 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">
            📅 Prenotazioni
          </h1>
          <p className="mt-2 text-gray-600">
            Visualizza e gestisci le prenotazioni dei campi.
          </p>
        </div>
        {/* Bottone disabilitato — lo attiveremo quando creeremo il form */}
        <button
          className="px-5 py-2.5 bg-emerald-700 text-white rounded-lg font-medium hover:bg-emerald-800 transition-colors shadow-sm disabled:opacity-50 disabled:cursor-not-allowed"
          disabled
          title="Funzionalità in arrivo"
        >
          + Nuova Prenotazione
        </button>
      </div>

      {/* MESSAGGIO DI ERRORE */}
      {errore && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
          <p className="font-semibold">⚠️ Errore di connessione</p>
          <p className="text-sm mt-1">{errore}</p>
          <p className="text-sm mt-2 text-red-500">
            Assicurati che il backend sia avviato su {process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"}
          </p>
        </div>
      )}

      {/* LISTA VUOTA */}
      {prenotazioni.length === 0 && !errore && (
        <div className="text-center py-16 text-gray-400">
          <p className="text-5xl mb-4">📭</p>
          <p className="text-lg">Nessuna prenotazione trovata.</p>
        </div>
      )}

      {/* GRIGLIA delle prenotazioni */}
      {prenotazioni.length > 0 && (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {prenotazioni.map((prenotazione) => (
              <PrenotazioneCard
                key={prenotazione.id}
                prenotazione={prenotazione}
              />
            ))}
          </div>

          {/* Riepilogo */}
          <div className="mt-8 p-4 bg-emerald-50 rounded-lg border border-emerald-200">
            <p className="text-sm text-emerald-800">
              📊 Totale prenotazioni: <strong>{prenotazioni.length}</strong>
            </p>
          </div>
        </>
      )}
    </div>
  );
}
