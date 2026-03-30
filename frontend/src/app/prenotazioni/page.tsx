/**
 * Pagina /prenotazioni — Lista delle prenotazioni.
 * 
 * Stessa logica di /campi:
 * 1. Importa i dati mock
 * 2. Usa .map() per renderizzare un PrenotazioneCard per ogni prenotazione
 * 
 * NOVITÀ: questa pagina mostra anche un messaggio quando non ci sono prenotazioni.
 * Usiamo il "rendering condizionale" con l'operatore &&:
 *   {array.length === 0 && <p>Nessun elemento</p>}
 * Si legge: "SE l'array è vuoto, ALLORA mostra il paragrafo"
 */

import { mockPrenotazioni } from "@/lib/mockData";
import PrenotazioneCard from "@/components/PrenotazioneCard";

export default function PrenotazioniPage() {
  const prenotazioni = mockPrenotazioni;

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
        {/* 
          Bottone "Nuova Prenotazione" — per ora non fa nulla.
          Lo collegheremo a un form nello Step 4. 
        */}
        <button
          className="px-5 py-2.5 bg-emerald-700 text-white rounded-lg font-medium hover:bg-emerald-800 transition-colors shadow-sm"
          disabled
          title="Funzionalità disponibile nello Step 4"
        >
          + Nuova Prenotazione
        </button>
      </div>

      {/* 
        RENDERING CONDIZIONALE:
        Se non ci sono prenotazioni, mostra un messaggio.
        "&&" è l'operatore "AND logico" — la parte destra viene renderizzata
        solo se la condizione a sinistra è vera.
      */}
      {prenotazioni.length === 0 && (
        <div className="text-center py-16 text-gray-400">
          <p className="text-5xl mb-4">📭</p>
          <p className="text-lg">Nessuna prenotazione trovata.</p>
        </div>
      )}

      {/* GRIGLIA delle prenotazioni */}
      {prenotazioni.length > 0 && (
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
      <div className="mt-8 p-4 bg-emerald-50 rounded-lg border border-emerald-200">
        <p className="text-sm text-emerald-800">
          📊 Totale prenotazioni: <strong>{prenotazioni.length}</strong>
        </p>
      </div>
    </div>
  );
}
