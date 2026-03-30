/**
 * PrenotazioneCard — Mostra i dettagli di una singola prenotazione.
 * 
 * CONCETTO CHIAVE: oggetti NESTED (annidati).
 * Una Prenotazione contiene un Campo e un Utente (socio).
 * Per accedere al nome del campo scrivi: prenotazione.campo.nome
 * È lo stesso di Java: prenotazione.getCampo().getNome()
 * 
 * FORMATTAZIONE DATE:
 * Le date arrivano come stringhe ISO ("2026-03-30").
 * Usiamo formattaData() per mostrarle in formato italiano ("30 marzo 2026").
 */

import { Prenotazione } from "@/types";

interface PrenotazioneCardProps {
  prenotazione: Prenotazione;
}

export default function PrenotazioneCard({ prenotazione }: PrenotazioneCardProps) {
  return (
    <div className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden hover:shadow-lg transition-shadow duration-300">
      {/* HEADER — data e orario */}
      <div className="bg-emerald-700 px-6 py-3 flex items-center justify-between">
        <span className="text-white font-semibold">
          📅 {formattaData(prenotazione.data)}
        </span>
        <span className="text-emerald-200 font-mono text-sm">
          🕐 {prenotazione.oraInizio}
        </span>
      </div>

      {/* CORPO — dettagli */}
      <div className="px-6 py-5 space-y-3">
        {/* 
          Oggetto NESTED: prenotazione.campo.nome
          In Java: prenotazione.getCampo().getNome()
        */}
        <div className="flex items-center gap-2 text-gray-700">
          <span className="text-lg">🏟️</span>
          <div>
            <p className="text-sm text-gray-500">Campo</p>
            <p className="font-semibold">{prenotazione.campo.nome}</p>
          </div>
        </div>

        {/* Superficie del campo */}
        <div className="flex items-center gap-2 text-gray-700">
          <span className="text-lg">🎾</span>
          <div>
            <p className="text-sm text-gray-500">Superficie</p>
            <p className="font-medium text-sm">{prenotazione.campo.tipoSuperficie}</p>
          </div>
        </div>

        {/* 
          Utente che ha prenotato: prenotazione.socio.nome + cognome
          In Java: prenotazione.getSocio().getNome() + " " + prenotazione.getSocio().getCognome()
          
          In TypeScript/JSX usiamo i template literals con ${}:
          `${prenotazione.socio.nome} ${prenotazione.socio.cognome}`
          Oppure concateniamo direttamente nel JSX.
        */}
        <div className="flex items-center gap-2 text-gray-700">
          <span className="text-lg">👤</span>
          <div>
            <p className="text-sm text-gray-500">Prenotato da</p>
            <p className="font-semibold">
              {prenotazione.socio.nome} {prenotazione.socio.cognome}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

// ============================================================
// FUNZIONI HELPER
// ============================================================
/**
 * Formatta una data ISO ("2026-03-30") in formato italiano ("30 marzo 2026").
 * 
 * Usiamo l'API nativa Intl.DateTimeFormat, che supporta la localizzazione.
 * Non servono librerie esterne come moment.js o date-fns.
 */
function formattaData(dataISO: string): string {
  const data = new Date(dataISO);
  return data.toLocaleDateString("it-IT", {
    day: "numeric",
    month: "long",
    year: "numeric",
  });
}
