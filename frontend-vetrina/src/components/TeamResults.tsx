import { getGareASquadre } from "@/lib/cms/gare";
import type { Gara } from "@/lib/cms/types";
import { siteConfig } from "@/config/site";
import { urlFor } from "@/sanity/image";
import Image from "next/image";

export default async function TeamResults() {
  const gare = await getGareASquadre();

  return (
    <section id="risultati" className="py-20 lg:py-28 bg-[#FAF9F5] border-t border-[#E5E0D4]">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="flex flex-col md:flex-row md:items-end justify-between mb-14 gap-6">
          <div>
            <span className="status-pill mb-3">
              Campionati Federali FITP
            </span>
            <h2
              className="text-3xl sm:text-5xl font-bold text-[#1B3C1F] tracking-tight"
              style={{ fontFamily: "var(--font-serif)" }}
            >
              I Risultati Ufficiali del Club
            </h2>
          </div>
          <p className="text-sm sm:text-base text-[#5A695C] max-w-md font-normal">
            Le partite e le prestazioni delle squadre agonistiche del {siteConfig.clubName} nei campionati toscani e nazionali.
          </p>
        </div>

        {/* Fallback or Match Grid */}
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

function GaraCard({ gara }: { gara: Gara }) {
  const dataFormattata = new Date(gara.data).toLocaleDateString("it-IT", {
    day: "numeric",
    month: "short",
    year: "numeric",
  });

  const isClubCasa = gara.squadraCasa.toLowerCase().includes("carmignano");

  return (
    <div className="editorial-card rounded-2xl overflow-hidden flex flex-col justify-between">
      {/* Top Banner / Match Cover */}
      <div className="relative h-40 w-full bg-[#F3EFE6] overflow-hidden">
        {gara.fotoCopertina ? (
          <Image
            src={urlFor(gara.fotoCopertina).width(600).height(320).quality(85).format("webp").url()}
            alt={`${gara.squadraCasa} vs ${gara.squadraOspite}`}
            fill
            sizes="(max-width: 768px) 100vw, (max-width: 1024px) 50vw, 33vw"
            className="object-cover"
          />
        ) : (
          <div className="absolute inset-0 flex items-center justify-center bg-gradient-to-br from-[#F0F5F1] to-[#DEE9DF]">
            <span className="text-3xl">🎾</span>
          </div>
        )}

        {/* Floating Tournament Badge */}
        <div className="absolute top-3 left-3 right-3 flex items-center justify-between">
          <span className="text-[11px] font-bold uppercase tracking-wider text-[#182219] bg-white/95 backdrop-blur-md px-3 py-1 rounded-full shadow-xs border border-[#E5E0D4]">
            {gara.campionato}
          </span>
          <span className="text-[11px] font-semibold text-[#5A695C] bg-white/95 backdrop-blur-md px-2.5 py-1 rounded-full shadow-xs border border-[#E5E0D4]">
            G{gara.giornata}
          </span>
        </div>
      </div>

      {/* Body: Teams & Score */}
      <div className="p-6 flex-1 flex flex-col justify-between">
        {/* Score Board */}
        <div className="flex items-center justify-between gap-3 mb-6 bg-[#FAF9F5] p-4 rounded-xl border border-[#E5E0D4]">
          {/* Home Team */}
          <div className="flex-1 text-center">
            <p className={`font-bold text-sm sm:text-base leading-tight ${isClubCasa ? "text-[#1B3C1F]" : "text-[#182219]"}`}>
              {gara.squadraCasa}
            </p>
            {isClubCasa && <span className="text-[10px] text-[#28542D] uppercase tracking-wider font-semibold block mt-1">Casa</span>}
          </div>

          {/* Central Scoreboard Badge */}
          <div className="px-3.5 py-1.5 rounded-lg bg-white border border-[#E5E0D4] text-lg font-bold text-[#182219] shadow-xs">
            {gara.risultato}
          </div>

          {/* Away Team */}
          <div className="flex-1 text-center">
            <p className={`font-bold text-sm sm:text-base leading-tight ${!isClubCasa ? "text-[#1B3C1F]" : "text-[#182219]"}`}>
              {gara.squadraOspite}
            </p>
            {!isClubCasa && <span className="text-[10px] text-[#28542D] uppercase tracking-wider font-semibold block mt-1">Ospite</span>}
          </div>
        </div>

        {/* Footer Meta: Date, Location & Victory Status */}
        <div className="flex items-center justify-between pt-3 border-t border-[#E5E0D4] text-xs">
          <div className="text-[#5A695C] flex items-center gap-1.5 font-medium">
            <span>{dataFormattata}</span>
            <span>•</span>
            <span className="truncate max-w-[120px]">{gara.luogo}</span>
          </div>

          <span
            className={`text-[10px] font-bold px-2.5 py-0.5 rounded-full uppercase tracking-wider ${
              gara.isVittoria
                ? "bg-[#F0F5F1] text-[#1B3C1F] border border-[#DEE9DF]"
                : gara.punteggioCasa === gara.punteggioOspite
                ? "bg-[#FFFBEB] text-[#92400E] border border-[#FDE68A]"
                : "bg-gray-100 text-gray-600 border border-gray-200"
            }`}
          >
            {gara.isVittoria ? "Vittoria" : gara.punteggioCasa === gara.punteggioOspite ? "Pareggio" : "Terminata"}
          </span>
        </div>
      </div>
    </div>
  );
}

function EmptyState() {
  return (
    <div className="text-center py-16 rounded-2xl bg-white border border-[#E5E0D4] max-w-xl mx-auto p-8 shadow-xs">
      <div className="w-14 h-14 mx-auto mb-3 rounded-full bg-[#F0F5F1] flex items-center justify-center text-2xl">
        🏆
      </div>
      <h3
        className="text-xl font-bold text-[#182219] mb-2"
        style={{ fontFamily: "var(--font-serif)" }}
      >
        Nessun risultato disponibile
      </h3>
      <p className="text-[#5A695C] text-sm">
        I calendari e i punteggi delle gare federali saranno aggiornati con l&apos;avvio della nuova stagione agonistica.
      </p>
    </div>
  );
}
