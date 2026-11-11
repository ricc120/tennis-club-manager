import Image from "next/image";
import Link from "next/link";
import { siteConfig } from "@/config/site";

export default function Hero() {
  return (
    <section className="relative pt-24 sm:pt-30 pb-16 lg:pt-30 lg:pb-18 overflow-hidden border-b border-[#E5E0D4] bg-gradient-to-b from-[#FAF9F5] via-[#FAF9F5] to-[#F3EFE6]">
      {/* Delicate background ambient aura - natural warm daylight */}
      <div className="absolute top-0 right-1/4 w-[500px] h-[500px] bg-[#E8EFE9]/40 rounded-full blur-3xl pointer-events-none -z-10" />
      <div className="absolute bottom-0 left-10 w-[400px] h-[400px] bg-[#F7E4D3]/30 rounded-full blur-3xl pointer-events-none -z-10" />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 lg:gap-16 items-center">
          {/* Left Column: Editorial & Brand Narrative */}
          <div className="lg:col-span-7 space-y-7">
            {/* Live Club Status Beacon */}
            <div className="inline-flex items-center gap-2.5 px-3.5 py-1.5 rounded-full bg-white border border-[#E5E0D4] shadow-xs text-xs font-medium text-[#182219]">
              <span className="relative flex h-2 w-2">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75" />
                <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-600" />
              </span>
              <span className="font-semibold text-[#1B3C1F]">Circolo Aperto</span>
              <span className="text-[#5A695C]">•</span>
              <span className="text-[#5A695C]">Oggi {siteConfig.hours.weekday.time}</span>
            </div>

            {/* Brand Header — Solo testo */}
            <div className="space-y-3">
              <div className="flex items-center gap-2">
                <span className="text-xs uppercase font-bold tracking-widest text-[#C05621]">
                  Circolo Storico • Dal {siteConfig.foundedYear}
                </span>
                <span className="text-[#5A695C]">•</span>
                <span className="text-xs sm:text-sm text-[#5A695C] font-medium">
                  {siteConfig.location}
                </span>
              </div>

              {/* Main Headline — Nome del circolo */}
              <h1
                className="text-4xl sm:text-6xl lg:text-7xl font-bold text-[#1B3C1F] tracking-tight leading-[1.08]"
                style={{ fontFamily: "var(--font-serif)" }}
              >
                {siteConfig.clubName.split(" ").slice(0, 2).join(" ")} <br />
                <span className="italic font-normal text-[#C05621]">
                  {siteConfig.clubName.split(" ").slice(2).join(" ")}
                </span>
              </h1>
            </div>

            {/* Subheading */}
            <p className="text-base sm:text-lg text-[#5A695C] max-w-2xl leading-relaxed font-normal">
              Dal {siteConfig.foundedYear}, un&apos;oasi sportiva di prestigio a {siteConfig.location}.
              Quattro campi immersi nel paesaggio collinare, l&apos;eccellenza agonistica della{" "}
              <strong className="text-[#182219] font-semibold">{siteConfig.academyName}</strong>{" "}
              e un&apos;accogliente clubhouse per vivere il vero spirito del club.
            </p>

            {/* Action CTAs */}
            <div className="flex flex-wrap items-center gap-4 pt-2">
              <Link
                href="/contatti"
                className="inline-flex items-center justify-center gap-2.5 px-7 py-3.5 rounded-full bg-[#1B3C1F] hover:bg-[#112614] text-white text-sm font-semibold tracking-wide transition-all duration-200 shadow-sm hover:shadow-md hover:-translate-y-0.5"
              >
                <span>Prenota un Campo</span>
                <span className="text-stone-300">→</span>
              </Link>

              <Link
                href="/accademia"
                className="inline-flex items-center justify-center gap-2 px-6 py-3.5 rounded-full bg-white hover:bg-[#FAF9F5] text-[#1B3C1F] text-sm font-semibold border border-[#E5E0D4] transition-all duration-200 hover:border-[#1B3C1F]/40 shadow-xs"
              >
                <span>Stefanini Tennis Academy</span>
              </Link>
            </div>

            {/* Key Metrics Strip (Clean Component Gallery Style) */}
            <div className="pt-6 border-t border-[#E5E0D4] grid grid-cols-2 sm:grid-cols-4 gap-6">
              <div>
                <span
                  className="block text-2xl sm:text-3xl font-bold text-[#1B3C1F]"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  {siteConfig.stats.courts} Campi
                </span>
                <span className="text-xs text-[#5A695C] font-medium block mt-0.5">
                  Terra rossa e padel
                </span>
              </div>

              <div>
                <span
                  className="block text-2xl sm:text-3xl font-bold text-[#C05621]"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  {siteConfig.stats.members}
                </span>
                <span className="text-xs text-[#5A695C] font-medium block mt-0.5">
                  Soci e allievi attivi
                </span>
              </div>

              <div>
                <span
                  className="block text-2xl sm:text-3xl font-bold text-[#1B3C1F]"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  WTA 99
                </span>
                <span className="text-xs text-[#5A695C] font-medium block mt-0.5">
                  Best ranking accademia
                </span>
              </div>

              <div>
                <span
                  className="block text-2xl sm:text-3xl font-bold text-[#C05621]"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  4.000+
                </span>
                <span className="text-xs text-[#5A695C] font-medium block mt-0.5">
                  Presenze annuali
                </span>
              </div>
            </div>
          </div>

          {/* Right Column: Visual Editorial Composition */}
          <div className="lg:col-span-5 relative">
            {/* Primary Framed Panorama */}
            <div className="relative rounded-3xl overflow-hidden border-4 border-white shadow-xl bg-white">
              <div className="relative h-80 sm:h-[30rem] w-full">
                <Image
                  src={siteConfig.images.heroBackground}
                  alt="Vista panoramica Tennis Club Carmignano"
                  fill
                  priority
                  sizes="(max-width: 1024px) 100vw, 42vw"
                  className="object-cover object-[37%]"
                />
                {/* Subtle soft gradient at base of image */}
                <div className="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent" />
              </div>

              {/* Logo del circolo in sovraimpressione sull'immagine */}
              <div className="absolute top-4 right-4 z-10 w-14 h-14 sm:w-16 sm:h-16 rounded-2xl bg-white/90 backdrop-blur-md p-1.5 shadow-md border border-white/80">
                <Image
                  src="/images/logo_circolo-removebg-preview.png"
                  alt={`Stemma ${siteConfig.clubName}`}
                  fill
                  className="object-contain p-1"
                />
              </div>

              {/* Floating Badge in Image Frame */}
              <div className="absolute bottom-4 left-4 right-4 bg-white/95 backdrop-blur-md p-4 rounded-2xl border border-white/80 shadow-md">
                <div className="flex items-center justify-between">
                  <div>
                    <span className="text-[10px] uppercase tracking-wider font-bold text-[#C05621] block">
                      Impianto Ufficiale FITP
                    </span>
                    <span className="text-xs sm:text-sm font-bold text-[#182219]">
                      La Serra • Carmignano (PO)
                    </span>
                  </div>
                  <Link
                    href="/strutture"
                    className="text-xs font-semibold text-[#1B3C1F] hover:text-[#C05621] transition-colors"
                  >
                    Guarda i campi →
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
