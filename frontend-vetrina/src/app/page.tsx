import Hero from "@/components/Hero";
import TeamResults from "@/components/TeamResults";
import Link from "next/link";
import Image from "next/image";
import { siteConfig } from "@/config/site";

export default function Home() {
  return (
    <div className="bg-[#FAF9F5] text-[#182219]">
      {/* 1. Asymmetric Editorial Hero */}
      <Hero />

      {/* 2. Il Circolo in Sintesi — Bento Editorial Grid (Component Gallery style) */}
      <section className="py-20 lg:py-26 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col md:flex-row md:items-end justify-between mb-14 gap-6">
          <div>
            <span className="status-pill mb-3">
              Vivi il Nostro Club
            </span>
            <h2
              className="text-3xl sm:text-5xl font-bold text-[#1B3C1F] tracking-tight"
              style={{ fontFamily: "var(--font-serif)" }}
            >
              Un&apos;Esperienza Completa di Sport e Relax
            </h2>
          </div>
          <p className="text-sm sm:text-base text-[#5A695C] max-w-md font-normal">
            Dalla terra battuta alla preparazione agonistica, fino alla convivialità della nostra clubhouse con vista panoramica sulle colline.
          </p>
        </div>

        {/* Bento Grid */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-6">
          {/* Card 1: Campi & Strutture (Large Feature, 7 cols) */}
          <div className="md:col-span-7 editorial-card rounded-3xl overflow-hidden flex flex-col justify-between group">
            <div className="relative h-64 sm:h-80 w-full overflow-hidden">
              <Image
                src="/images/2campi.jpg"
                alt="Campi da tennis in terra rossa"
                fill
                sizes="(max-width: 768px) 100vw, 60vw"
                className="object-cover group-hover:scale-105 transition-transform duration-700"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent" />
              <div className="absolute top-4 left-4">
                <span className="bg-white/95 backdrop-blur-md text-[#1B3C1F] text-xs font-bold px-3 py-1.5 rounded-full uppercase tracking-wider shadow-xs">
                  Strutture Ufficiali
                </span>
              </div>
              <div className="absolute bottom-4 left-4 right-4 text-white">
                <p className="text-xs font-semibold text-[#F7E4D3] uppercase tracking-wider">
                  Tennis &amp; Padel
                </p>
                <h3
                  className="text-xl sm:text-2xl font-bold text-white mt-1"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  Terra Rossa d&apos;Autore e Padel Panoramico
                </h3>
              </div>
            </div>

            <div className="p-6 sm:p-8 flex flex-col justify-between flex-1">
              <p className="text-sm sm:text-base text-[#5A695C] leading-relaxed mb-6">
                Superfici curate ogni giorno con cura artigianale. Campi in terra battuta tradizionale, superfici veloci sintetiche e campi da padel panoramici immersi nella quiete de &ldquo;La Serra&rdquo;.
              </p>
              <div className="flex items-center justify-between pt-4 border-t border-[#E5E0D4]">
                <div className="flex gap-2">
                  <span className="text-xs bg-[#F0F5F1] text-[#1B3C1F] font-semibold px-2.5 py-1 rounded-md">
                    Illuminazione LED
                  </span>
                  <span className="text-xs bg-[#FCF5EE] text-[#C05621] font-semibold px-2.5 py-1 rounded-md">
                    Copertura Invernale
                  </span>
                </div>
                <Link
                  href="/strutture"
                  className="text-xs font-bold text-[#1B3C1F] hover:text-[#C05621] flex items-center gap-1.5 transition-colors"
                >
                  <span>Dettagli Campi</span>
                  <span>→</span>
                </Link>
              </div>
            </div>
          </div>

          {/* Card 2: Stefanini Tennis Academy (5 cols) */}
          <div className="md:col-span-5 editorial-card rounded-3xl overflow-hidden flex flex-col justify-between group">
            <div className="relative h-64 sm:h-80 w-full overflow-hidden">
              <Image
                src="/images/lucrezia_stefanini.jpg"
                alt="Stefanini Tennis Academy"
                fill
                sizes="(max-width: 768px) 100vw, 40vw"
                className="object-cover object-top group-hover:scale-105 transition-transform duration-700"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent" />
              <div className="absolute top-4 left-4">
                <span className="bg-white/95 backdrop-blur-md text-[#C05621] text-xs font-bold px-3 py-1.5 rounded-full uppercase tracking-wider shadow-xs">
                  S.T.A. • WTA Top 100
                </span>
              </div>
              <div className="absolute bottom-4 left-4 right-4 text-white">
                <p className="text-xs font-semibold text-[#F7E4D3] uppercase tracking-wider">
                  Scuola Tennis &amp; Agonistica
                </p>
                <h3
                  className="text-xl sm:text-2xl font-bold text-white mt-1"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  Stefanini Tennis Academy
                </h3>
              </div>
            </div>

            <div className="p-6 sm:p-8 flex flex-col justify-between flex-1">
              <p className="text-sm sm:text-base text-[#5A695C] leading-relaxed mb-6">
                Corsi FITP per ogni livello ed età: minitennis, perfezionamento adulti e settore agonistico d&apos;eccellenza con Jacopo Stefanini.
              </p>
              <div className="flex items-center justify-between pt-4 border-t border-[#E5E0D4]">
                <span className="text-xs text-[#5A695C]">Maestri Federali FITP</span>
                <Link
                  href="/accademia"
                  className="text-xs font-bold text-[#1B3C1F] hover:text-[#C05621] flex items-center gap-1.5 transition-colors"
                >
                  <span>Corsi e Tariffe</span>
                  <span>→</span>
                </Link>
              </div>
            </div>
          </div>

          {/* Card 3: Clubhouse & Terrazza Panoramica (5 cols) */}
          <div className="md:col-span-5 editorial-card rounded-3xl overflow-hidden flex flex-col justify-between group">
            <div className="relative h-60 w-full overflow-hidden">
              <Image
                src="/images/bar_pizzeria.jpg"
                alt="Clubhouse e Terrazza Bar"
                fill
                sizes="(max-width: 768px) 100vw, 40vw"
                className="object-cover group-hover:scale-105 transition-transform duration-700"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent" />
              <div className="absolute top-4 left-4">
                <span className="bg-white/95 backdrop-blur-md text-[#182219] text-xs font-bold px-3 py-1.5 rounded-full uppercase tracking-wider shadow-xs">
                  Club Life
                </span>
              </div>
            </div>
            <div className="p-6 sm:p-8 flex flex-col justify-between flex-1">
              <div>
                <h3
                  className="text-xl font-bold text-[#1B3C1F] mb-2"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  Clubhouse e Pizzeria
                </h3>
                <p className="text-sm text-[#5A695C] leading-relaxed mb-4">
                  Il &ldquo;terzo tempo&rdquo; perfetto: bar per la colazione, pizzeria per cene di club e una terrazza con vista diretta sui campi per godersi ogni scambio.
                </p>
              </div>
              <div className="pt-4 border-t border-[#E5E0D4] flex items-center justify-between">
                <span className="text-xs text-[#5A695C]">Aperto tutti i giorni</span>
                <Link
                  href="/club"
                  className="text-xs font-bold text-[#1B3C1F] hover:text-[#C05621] flex items-center gap-1.5 transition-colors"
                >
                  <span>Scopri il Club</span>
                  <span>→</span>
                </Link>
              </div>
            </div>
          </div>

          {/* Card 4: Filosofia & Radici Toscane (7 cols) */}
          <div className="md:col-span-7 editorial-card rounded-3xl p-6 sm:p-8 flex flex-col justify-between bg-gradient-to-br from-white to-[#F3EFE6]/60">
            <div>
              <div className="flex items-center gap-2 mb-4">
                <span className="clay-pill">Passione dal {siteConfig.foundedYear}</span>
                <span className="text-xs text-[#5A695C]">• Carmignano (PO)</span>
              </div>
              <h3
                className="text-2xl sm:text-3xl font-bold text-[#1B3C1F] mb-4"
                style={{ fontFamily: "var(--font-serif)" }}
              >
                Tradizione, Accoglienza e Amore per lo Sport
              </h3>
              <p className="text-sm sm:text-base text-[#5A695C] leading-relaxed mb-6 font-normal">
                Fondato dai maestri nazionali Marco e Roberta Stefanini, il {siteConfig.clubName} è nato con una missione precisa: creare un ambiente sano, cordiale e stimolante dove condividere la passione per il tennis e formare i campioni di domani.
              </p>
            </div>

            <div className="grid grid-cols-3 gap-4 pt-4 border-t border-[#E5E0D4]">
              <div>
                <span className="text-xs font-bold text-[#1B3C1F] block">10+ Anni</span>
                <span className="text-[11px] text-[#5A695C] block">Di storia ufficiale</span>
              </div>
              <div>
                <span className="text-xs font-bold text-[#C05621] block">FITP</span>
                <span className="text-[11px] text-[#5A695C] block">Circolo federale</span>
              </div>
              <div>
                <span className="text-xs font-bold text-[#1B3C1F] block">Toscana</span>
                <span className="text-[11px] text-[#5A695C] block">Scenario collinare</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 3. Grand Slam Heritage Spotlight (Accademia Stefanini) */}
      <section className="py-20 bg-[#F3EFE6] border-y border-[#E5E0D4]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="editorial-card rounded-3xl p-8 sm:p-12 lg:p-16 bg-white">
            <div className="grid grid-cols-1 lg:grid-cols-12 gap-10 items-center">
              <div className="lg:col-span-5 relative rounded-2xl overflow-hidden shadow-md h-80 sm:h-96">
                <Image
                  src="/images/jacopo_stefanini.jpg"
                  alt="Jacopo Stefanini sui campi del TC Carmignano"
                  fill
                  sizes="(max-width: 1024px) 100vw, 40vw"
                  className="object-cover md:object-[50%_20%]"
                />
              </div>

              <div className="lg:col-span-7 space-y-6">
                <span className="clay-pill">
                  Dalla Toscana ai Grand Slam
                </span>

                <h3
                  className="text-2xl sm:text-4xl font-bold text-[#1B3C1F] leading-snug"
                  style={{ fontFamily: "var(--font-serif)" }}
                >
                  Un&apos;Accademia Nata sui Campi del Tennis Mondiale
                </h3>

                <p className="text-sm sm:text-base text-[#5A695C] leading-relaxed">
                  La Stefanini Tennis Academy è guidata dal maestro <strong className="text-[#182219]">Jacopo Stefanini</strong> (vittorie in carriera su Alexander Zverev, Daniil Medvedev e Matteo Berrettini) ed è la casa d&apos;allenamento di <strong className="text-[#C05621]">Lucrezia Stefanini (best ranking WTA 99)</strong>.
                </p>

                {/* Grand Slam Accreditation Badges */}
                <div className="pt-2">
                  <p className="text-xs font-bold uppercase tracking-wider text-[#182219] mb-3">
                    Partecipazioni nei Tabelloni Principali del Grande Slam
                  </p>
                  <div className="flex flex-wrap gap-2.5">
                    {[
                      { name: "Wimbledon", flag: "🇬🇧", style: "bg-[#F0F5F1] text-[#1B3C1F] border-[#DEE9DF]" },
                      { name: "Roland Garros", flag: "🇫🇷", style: "bg-[#FCF5EE] text-[#C05621] border-[#F7E4D3]" },
                      { name: "US Open", flag: "🇺🇸", style: "bg-[#EEF4FF] text-[#1E40AF] border-[#DBEAFE]" },
                      { name: "Australian Open", flag: "🇦🇺", style: "bg-[#ECFDF5] text-[#065F46] border-[#D1FAE5]" },
                      { name: "Roma WTA", flag: "🇮🇹", style: "bg-[#FFFBEB] text-[#92400E] border-[#FDE68A]" },
                    ].map((slam) => (
                      <span
                        key={slam.name}
                        className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border text-xs font-semibold ${slam.style}`}
                      >
                        <span>{slam.flag}</span>
                        <span>{slam.name}</span>
                      </span>
                    ))}
                  </div>
                </div>

                <div className="pt-2">
                  <Link
                    href="/accademia"
                    className="inline-flex items-center gap-2 text-sm font-bold text-[#1B3C1F] hover:text-[#C05621] transition-colors"
                  >
                    <span>Scopri la metodologia e i programmi dell&apos;Accademia</span>
                    <span>→</span>
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 4. TeamResults (Risultati Campionati Federali FITP) */}
      <TeamResults />

      {/* 5. Pre-Footer Invitation Banner (Refined Tuscan Hospitality) */}
      <section className="py-20 lg:py-24 bg-[#1B3C1F] text-white relative overflow-hidden">
        <div className="max-w-4xl mx-auto px-4 text-center relative z-10">
          <span className="text-xs uppercase tracking-widest text-[#F7E4D3] font-bold block mb-3">
            Ti Aspettiamo al Circolo
          </span>
          <h2
            className="text-3xl sm:text-5xl font-bold mb-6 text-white"
            style={{ fontFamily: "var(--font-serif)" }}
          >
            Vieni a Giocare a Carmignano
          </h2>
          <p className="text-[#DEE9DF] text-base sm:text-lg max-w-2xl mx-auto mb-10 font-light leading-relaxed">
            Prenota un campo in terra battuta o padel, oppure richiedi una prova gratuita con i nostri maestri per valutare il corso più adatto alle tue esigenze.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              href="/contatti"
              className="inline-flex items-center justify-center gap-2 px-8 py-4 rounded-full bg-[#C05621] hover:bg-[#973F14] text-white font-semibold text-sm transition-all shadow-md hover:shadow-lg hover:-translate-y-0.5"
            >
              <span>Contatta la Segreteria</span>
              <span>→</span>
            </Link>
            <Link
              href="/strutture"
              className="inline-flex items-center justify-center gap-2 px-8 py-4 rounded-full bg-white/10 hover:bg-white/20 text-white font-semibold text-sm border border-white/20 transition-all hover:-translate-y-0.5"
            >
              <span>Visualizza i Campi</span>
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}
