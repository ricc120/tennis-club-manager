/**
 * TeamResults.tsx — Server Component per i risultati delle gare a squadre.
 *
 * CONCETTO: Separazione Fetching / Presentazione
 *
 * Questo componente è un SERVER Component (async function).
 * Il fetch dei dati avviene sul SERVER (Node.js), MAI nel browser.
 *
 * ARCHITETTURA:
 *   getGareASquadre()     →  lib/cms/gare.ts   (DATA LAYER)
 *   <TeamResults />       →  questo file        (PRESENTATION LAYER)
 *
 * Il componente NON sa da dove vengono i dati (mock? CMS? API?).
 * Sa solo che riceve un array di Gara[] e lo renderizza.
 *
 * QUANDO IL CMS SARÀ COLLEGATO:
 * Non si tocca NIENTE in questo file. Si modifica solo gare.ts
 * per puntare a un endpoint reale. Questo è il vantaggio della
 * separazione netta tra data-fetching e UI.
 */

import { getGareASquadre } from "@/lib/cms/gare";
import type { Gara } from "@/lib/cms/types";
import { siteConfig } from "@/config/site";

export default async function TeamResults() {
  const gare = await getGareASquadre();

  return (
    <section id="risultati" className="py-20 lg:py-28 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto mb-16">
          <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
            Campionati a Squadre
          </p>
          <h2 className="text-4xl lg:text-5xl font-bold text-dark mb-6">
            Risultati Recenti
          </h2>
          <p className="text-gray-600 text-lg">
            Segui le nostre squadre nei campionati federali.
          </p>
        </div>

        {/* Fallback: nessun risultato */}
        {gare.length === 0 ? (
          <EmptyState />
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {gare.map((gara) => (
              <GaraCard key={gara._id} gara={gara} />
            ))}
          </div>
        )}
      </div>
    </section>
  );
}

/**
 * Card per una singola gara.
 * Mostra squadre, risultato, campionato e badge vittoria/sconfitta.
 */
function GaraCard({ gara }: { gara: Gara }) {
  const dataFormattata = new Date(gara.data).toLocaleDateString("it-IT", {
    day: "numeric",
    month: "long",
    year: "numeric",
  });

  // Determina se il club è la squadra di casa o ospite
  const isClubCasa = gara.squadraCasa.includes(siteConfig.clubShortName.split(" ").pop() || "");

  return (
    <div className="rounded-2xl border border-gray-200 bg-white overflow-hidden hover:shadow-lg transition-all duration-300 hover:-translate-y-1">
      {/* Header con campionato e giornata */}
      <div className="bg-light px-6 py-3 flex items-center justify-between border-b border-gray-200">
        <span className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
          {gara.campionato}
        </span>
        <span className="text-xs text-gray-400">
          Giornata {gara.giornata}
        </span>
      </div>

      {/* Corpo della card */}
      <div className="p-6">
        {/* Squadre e risultato */}
        <div className="flex items-center justify-between mb-4">
          {/* Squadra Casa */}
          <div className="flex-1 text-center">
            <p className={`font-bold text-lg ${isClubCasa ? "text-primary" : "text-dark"}`}>
              {gara.squadraCasa}
            </p>
          </div>

          {/* Risultato */}
          <div className="px-4">
            <div className={`text-2xl font-bold px-4 py-2 rounded-xl ${
              gara.isVittoria
                ? "bg-primary-50 text-primary"
                : "bg-gray-100 text-gray-500"
            }`}>
              {gara.risultato}
            </div>
          </div>

          {/* Squadra Ospite */}
          <div className="flex-1 text-center">
            <p className={`font-bold text-lg ${!isClubCasa ? "text-primary" : "text-dark"}`}>
              {gara.squadraOspite}
            </p>
          </div>
        </div>

        {/* Footer con data, luogo e badge */}
        <div className="flex items-center justify-between pt-4 border-t border-gray-100">
          <div className="text-sm text-gray-500">
            <span>{dataFormattata}</span>
            <span className="mx-2">·</span>
            <span>{gara.luogo}</span>
          </div>
          <span
            className={`text-xs font-bold px-3 py-1 rounded-full ${
              gara.isVittoria
                ? "bg-primary-50 text-primary"
                : gara.punteggioCasa === gara.punteggioOspite
                ? "bg-accent-50 text-accent"
                : "bg-gray-100 text-gray-500"
            }`}
          >
            {gara.isVittoria ? "VITTORIA" : gara.punteggioCasa === gara.punteggioOspite ? "PAREGGIO" : "SCONFITTA"}
          </span>
        </div>
      </div>
    </div>
  );
}

/**
 * Stato vuoto — mostrato quando non ci sono risultati.
 * Potrebbe indicare che il CMS non è ancora collegato o che
 * il campionato non è iniziato.
 */
function EmptyState() {
  return (
    <div className="text-center py-16">
      <span className="text-5xl mb-4 block">🏆</span>
      <h3
        className="text-2xl font-bold text-dark mb-3"
        style={{ fontFamily: "'Playfair Display', serif" }}
      >
        Nessun risultato disponibile
      </h3>
      <p className="text-gray-500 max-w-md mx-auto">
        I risultati delle gare a squadre saranno disponibili con l&apos;inizio
        dei campionati federali. Torna a trovarci!
      </p>
    </div>
  );
}
