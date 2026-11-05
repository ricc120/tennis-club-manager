import type { Metadata } from "next";
import Image from "next/image";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Strutture | ${siteConfig.clubName}`,
  description: `Scopri le strutture di ${siteConfig.clubName}: campi in terra rossa, padel panoramici, clubhouse e ristorante a ${siteConfig.location}.`,
};

export default function StrutturePage() {
  return (
    <div className="bg-[#FAF9F5] text-[#182219]">
      {/* 1. Page Header */}
      <section className="pt-36 pb-20 lg:pt-44 lg:pb-24 bg-[#1B3C1F] text-white relative overflow-hidden">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10">
          <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/10 text-[#F7E4D3] border border-white/20 text-xs font-semibold uppercase tracking-widest mb-4">
            Gli Spazi del Club
          </span>
          <h1
            className="text-4xl sm:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "var(--font-serif)" }}
          >
            I Campi e Le Strutture
          </h1>
          <p className="text-[#DEE9DF] text-lg sm:text-xl max-w-2xl mx-auto font-light leading-relaxed">
            Un ambiente curato e armonioso, pensato per garantire il massimo livello di comfort a sportivi, atleti e visitatori.
          </p>
        </div>
      </section>

      {/* 2. Stats Ticker Banner */}
      <section className="py-12 bg-white border-b border-[#E5E0D4]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 text-center">
            <div className="p-4">
              <span className="block text-3xl lg:text-4xl font-bold text-[#1B3C1F]" style={{ fontFamily: "var(--font-serif)" }}>
                {siteConfig.stats.courts}
              </span>
              <span className="text-xs uppercase tracking-wider text-[#5A695C] font-semibold mt-1 block">
                Campi Tennis &amp; Padel
              </span>
            </div>

            <div className="p-4">
              <span className="block text-3xl lg:text-4xl font-bold text-[#C05621]" style={{ fontFamily: "var(--font-serif)" }}>
                {siteConfig.stats.members}
              </span>
              <span className="text-xs uppercase tracking-wider text-[#5A695C] font-semibold mt-1 block">
                Tesserati Attivi
              </span>
            </div>

            <div className="p-4">
              <span className="block text-3xl lg:text-4xl font-bold text-[#1B3C1F]" style={{ fontFamily: "var(--font-serif)" }}>
                {siteConfig.stats.coaches}
              </span>
              <span className="text-xs uppercase tracking-wider text-[#5A695C] font-semibold mt-1 block">
                Maestri &amp; Staff FITP
              </span>
            </div>

            <div className="p-4">
              <span className="block text-3xl lg:text-4xl font-bold text-[#C05621]" style={{ fontFamily: "var(--font-serif)" }}>
                4.000+
              </span>
              <span className="text-xs uppercase tracking-wider text-[#5A695C] font-semibold mt-1 block">
                Presenze Annuali
              </span>
            </div>
          </div>
        </div>
      </section>

      {/* 3. Galleria Strutture — Asymmetric Pseudo-Masonry Grid */}
      <section className="py-20 lg:py-28 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center max-w-3xl mx-auto mb-16">
          <span className="status-pill mb-3">
            Esplora il Circolo
          </span>
          <h2
            className="text-3xl sm:text-4xl font-bold text-[#1B3C1F] tracking-tight mb-4"
            style={{ fontFamily: "var(--font-serif)" }}
          >
            Spazi Sportivi e Ambienti Conviviali
          </h2>
          <p className="text-[#5A695C] text-base">
            Campi in terra rossa, sintetico, padel panoramici e la clubhouse con terrazza.
          </p>
        </div>

        {/* Asymmetric Masonry Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 sm:auto-rows-[280px]">
          {siteConfig.strutture.map((item) => {
            const isExternalUrl = item.photo.startsWith("http");
            const isLink = "url" in item && Boolean(item.url);

            const Wrapper = isLink ? "a" : "div";
            const wrapperProps = isLink
              ? { href: item.url as string, target: "_blank", rel: "noopener noreferrer" }
              : {};

            return (
              <Wrapper
                key={item.title}
                {...wrapperProps}
                className={`editorial-card rounded-3xl overflow-hidden group relative flex flex-col justify-end p-6 sm:p-7 border border-[#E5E0D4] shadow-sm hover:shadow-xl transition-all duration-300 min-h-[280px] ${item.tall ? "sm:row-span-2" : "row-span-1"
                  } ${isLink ? "cursor-pointer" : ""}`}
              >
                {/* Full-bleed background image */}
                <Image
                  src={item.photo}
                  alt={item.title}
                  fill
                  unoptimized={isExternalUrl}
                  sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
                  className="object-cover group-hover:scale-105 transition-transform duration-700"
                />

                {/* Soft gradient overlay for crisp text readability */}
                <div className="absolute inset-0 bg-gradient-to-t from-black/95 via-black/60 to-transparent transition-opacity duration-300 -z-10" />

                {/* Top Category Badge */}
                <div className="absolute top-4 left-4 z-10">
                  <span className="bg-white/95 backdrop-blur-md text-[#182219] text-xs font-bold px-3 py-1.5 rounded-full uppercase tracking-wider shadow-xs border border-white/60">
                    {item.category}
                  </span>
                </div>

                {/* Bottom Content */}
                <div className="relative z-10 text-white space-y-2 mb-2">
                  <h3
                    className="text-xl sm:text-2xl font-bold text-white tracking-tight leading-snug drop-shadow-[0_2px_4px_rgba(0,0,0,0.8)]"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    {item.title}
                  </h3>

                  {isLink && (
                    <div className="pt-1">
                      <span className="inline-flex items-center gap-1.5 text-xs font-semibold text-[#F7E4D3] group-hover:text-white drop-shadow-md transition-colors">
                        <span>Visita il sito dedicato</span>
                        <span>→</span>
                      </span>
                    </div>
                  )}
                </div>
              </Wrapper>
            );
          })}
        </div>
      </section>
    </div>
  );
}
