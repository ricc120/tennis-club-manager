/**
 * Pagina /prenotazioni — CRUD completo: visualizza, crea, elimina.
 * 
 * STEP 6: Aggiunto il form di creazione.
 * 
 * CONCETTO: Composizione Server Component + Client Component
 * 
 * Questa pagina è un Server Component (async) che:
 * 1. Carica le prenotazioni dal backend (await getPrenotazioni())
 * 2. Carica i campi dal backend (await getCampi()) — necessari per il form
 * 3. Passa i campi al form (Client Component) come prop
 * 
 * Il form (NuovaPrenotazioneForm) è un Client Component perché ha
 * interattività (form, useState, onClick). Ma la pagina resta Server Component
 * per il fetch iniziale dei dati (SEO-friendly, nessun spinner al caricamento).
 * 
 * ARCHITETTURA:
 *   PrenotazioniPage (Server Component, async)
 *   ├── await getPrenotazioni()  → dati dal backend
 *   ├── await getCampi()         → dati dal backend
 *   ├── NuovaPrenotazioneForm    → Client Component (form interattivo)
 *   └── PrenotazioneCard[]       → Client Component (bottone elimina)
 */

import { getPrenotazioni } from "@/services/prenotazioniService";
import { getCampi } from "@/services/campiService";
import PrenotazioneCard from "@/components/PrenotazioneCard";
import NuovaPrenotazioneForm from "@/components/NuovaPrenotazioneForm";
import { Prenotazione, Campo } from "@/types";

// STEP 7 FIX: impedisce il pre-rendering statico al build time (vedi campi/page.tsx per spiegazione)
export const dynamic = "force-dynamic";

export default async function PrenotazioniPage() {
  // NOTA: in dev mode TypeScript è permissivo, ma "next build" (produzione)
  // richiede tipi espliciti — ecco perché aggiungiamo i tipi qui.
  let prenotazioni: Prenotazione[] = [];
  let campi: Campo[] = [];
  let errore = "";

  try {
    // Carica prenotazioni E campi in parallelo per velocità
    // Promise.all esegue entrambe le richieste contemporaneamente
    [prenotazioni, campi] = await Promise.all([
      getPrenotazioni(),
      getCampi(),
    ]);
  } catch (error) {
    errore = error instanceof Error
      ? error.message
      : "Errore nel caricamento dei dati";
    prenotazioni = [];
    campi = [];
  }

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

      {/* ERRORE */}
      {errore && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
          <p className="font-semibold">⚠️ Errore di connessione</p>
          <p className="text-sm mt-1">{errore}</p>
        </div>
      )}

      {/* 
        LAYOUT A 2 COLONNE:
        - Sinistra: form di creazione (1/3 della larghezza)
        - Destra: lista prenotazioni (2/3 della larghezza)
        
        Su mobile: 1 colonna (form sopra, lista sotto)
      */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* COLONNA SINISTRA — Form di creazione */}
        <div className="lg:col-span-1">
          <NuovaPrenotazioneForm campi={campi} />
        </div>

        {/* COLONNA DESTRA — Lista prenotazioni */}
        <div className="lg:col-span-2">
          {prenotazioni.length === 0 && !errore ? (
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
    </div>
  );
}
