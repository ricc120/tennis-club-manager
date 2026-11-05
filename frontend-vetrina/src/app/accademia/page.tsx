import type { Metadata } from "next";
import Image from "next/image";
import Link from "next/link";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Accademia | ${siteConfig.clubName}`,
  description: `${siteConfig.academyName} (${siteConfig.academyShortName}) — Corsi di tennis per bambini, adulti e agonisti. Scopri i programmi e le tariffe.`,
};

export default function AccademiaPage() {
  return (
    <div className="bg-[#FAF9F5] text-[#182219]">
      {/* 1. Header */}
      <section className="pt-36 pb-20 lg:pt-44 lg:pb-24 bg-[#1B3C1F] text-white relative overflow-hidden">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10">
          <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/10 text-[#F7E4D3] border border-white/20 text-xs font-semibold uppercase tracking-widest mb-4">
            {siteConfig.academyShortName} • Scuola Tennis Riconosciuta FITP
          </span>
          <h1
            className="text-4xl sm:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "var(--font-serif)" }}
          >
            {siteConfig.academyName}
          </h1>
          <p className="text-[#DEE9DF] text-lg sm:text-xl max-w-2xl mx-auto font-light leading-relaxed">
            Metodologie moderne, attenzione individuale e preparazione atletica: dal minitennis promozionale fino al circuito professionistico mondiale.
          </p>
        </div>
      </section>

      {/* 2. Pro Showcase: Jacopo & Lucrezia Stefanini */}
      <section className="py-20 lg:py-28 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center max-w-3xl mx-auto mb-20">
          <span className="clay-pill mb-3">
            Dalla Toscana ai Grand Slam
          </span>
          <h2
            className="text-3xl sm:text-4xl lg:text-5xl font-bold text-[#1B3C1F] tracking-tight mb-4"
            style={{ fontFamily: "var(--font-serif)" }}
          >
            Istruttori ed Atleti d&apos;Élite
          </h2>
          <p className="text-[#5A695C] text-base">
            Una scuola fondata sull&apos;esperienza diretta del tennis di vertice.
          </p>
        </div>

        {/* Row 1: Jacopo Stefanini */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 items-center mb-24">
          <div className="space-y-6">
            <span className="status-pill">
              Direzione Tecnica Agonistica
            </span>
            <h3
              className="text-2xl sm:text-4xl font-bold text-[#1B3C1F]"
              style={{ fontFamily: "var(--font-serif)" }}
            >
              Jacopo Stefanini
            </h3>
            <div className="space-y-4 text-[#5A695C] leading-relaxed text-base lg:text-lg">
              <p>
                Alla guida dell&apos;accademia c&apos;è <strong className="text-[#182219] font-semibold">Jacopo Stefanini</strong>, maestro ed ex-professionista che in carriera vanta prestigiose vittorie contro giocatori del calibro di{" "}
                <strong className="text-[#C05621]">Alexander Zverev</strong>,{" "}
                <strong className="text-[#C05621]">Daniil Medvedev</strong> e{" "}
                <strong className="text-[#C05621]">Matteo Berrettini</strong>.
              </p>
              <p>
                La filosofia della Stefanini Tennis Academy mira ad offrire una preparazione completa — tecnica, tattica, fisica e mentale — lontano dalle pressioni esasperate e valorizzando la crescita armonica dell&apos;atleta.
              </p>
            </div>
          </div>

          <div className="relative rounded-3xl overflow-hidden shadow-xl border-4 border-white bg-white group">
            <div className="relative h-80 sm:h-96 w-full">
              <Image
                src="/images/jacopo_stefanini_2.jpg"
                alt="Jacopo Stefanini sui campi del TC Carmignano"
                fill
                sizes="(max-width: 1024px) 100vw, 50vw"
                className="object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
          </div>
        </div>

        {/* Row 2: Lucrezia Stefanini */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 items-center">
          <div className="relative rounded-3xl overflow-hidden shadow-xl border-4 border-white bg-white group order-2 lg:order-1">
            <div className="relative h-80 sm:h-96 w-full">
              <Image
                src="/images/lucrezia_stefanini_2.jpg"
                alt="Lucrezia Stefanini campionessa WTA"
                fill
                sizes="(max-width: 1024px) 100vw, 50vw"
                className="object-cover object-top group-hover:scale-105 transition-transform duration-700"
              />
              <div className="absolute top-4 left-4">
                <span className="bg-[#C05621] text-white text-xs font-bold px-3 py-1.5 rounded-full uppercase tracking-wider shadow-md">
                  WTA Top 100
                </span>
              </div>
            </div>
          </div>

          <div className="space-y-6 order-1 lg:order-2">
            <span className="clay-pill">
              Orgoglio del Nostro Circolo
            </span>
            <h3
              className="text-2xl sm:text-4xl font-bold text-[#1B3C1F]"
              style={{ fontFamily: "var(--font-serif)" }}
            >
              Lucrezia Stefanini
            </h3>
            <p className="text-[#5A695C] text-base lg:text-lg leading-relaxed">
              Cresciuta fin da bambina sui campi in terra rossa di Carmignano, Lucrezia è oggi una delle tenniste di punta del circuito maggiore WTA mondiale, protagonista nei tabelloni principali dei quattro tornei più prestigiosi al mondo.
            </p>

            {/* Ranking Pill */}
            <div className="inline-flex items-center gap-4 bg-white p-4 rounded-2xl border border-[#E5E0D4] shadow-xs">
              <span className="text-3xl font-extrabold text-[#C05621]" style={{ fontFamily: "var(--font-serif)" }}>
                #99
              </span>
              <div>
                <p className="text-xs uppercase font-bold text-[#5A695C]">Best Ranking Mondiale</p>
                <p className="text-sm font-semibold text-[#182219]">Classifica Ufficiale WTA Tour</p>
              </div>
            </div>

            {/* Slam badges */}
            <div>
              <p className="text-xs font-semibold uppercase tracking-wider text-[#5A695C] mb-3">
                Tornei del Grande Slam disputati
              </p>
              <div className="flex flex-wrap gap-2">
                {[
                  { name: "Wimbledon", flag: "🇬🇧", tag: "bg-[#F0F5F1] text-[#1B3C1F] border-[#DEE9DF]" },
                  { name: "Roland Garros", flag: "🇫🇷", tag: "bg-[#FCF5EE] text-[#C05621] border-[#F7E4D3]" },
                  { name: "US Open", flag: "🇺🇸", tag: "bg-[#EEF4FF] text-[#1E40AF] border-[#DBEAFE]" },
                  { name: "Australian Open", flag: "🇦🇺", tag: "bg-[#ECFDF5] text-[#065F46] border-[#D1FAE5]" },
                  { name: "Roma WTA", flag: "🇮🇹", tag: "bg-[#FFFBEB] text-[#92400E] border-[#FDE68A]" },
                ].map((slam) => (
                  <span
                    key={slam.name}
                    className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border text-xs font-semibold ${slam.tag}`}
                  >
                    <span>{slam.flag}</span>
                    <span>{slam.name}</span>
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 3. CORSI & TARIFFE — Editorial Cards */}
      <section className="py-20 lg:py-28 bg-[#F3EFE6] border-t border-[#E5E0D4]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <span className="status-pill mb-3">
              I Corsi
            </span>
            <h2
              className="text-3xl sm:text-4xl font-bold text-[#1B3C1F] tracking-tight mb-4"
              style={{ fontFamily: "var(--font-serif)" }}
            >
              Percorsi Formativi e Tariffe
            </h2>
            <p className="text-[#5A695C] text-base">
              Programmi su misura per ogni fascia d&apos;età e livello di gioco.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 items-stretch max-w-6xl mx-auto">
            {siteConfig.courses.map((course) => (
              <div
                key={course.name}
                className={`editorial-card rounded-2xl p-8 flex flex-col justify-between transition-all duration-300 ${course.highlighted
                  ? "border-2 border-[#1B3C1F] shadow-xl relative -translate-y-2 bg-white"
                  : "bg-white"
                  }`}
              >
                <div>
                  {course.highlighted && (
                    <div className="mb-4">
                      <span className="bg-[#1B3C1F] text-white text-xs font-bold px-3 py-1 rounded-full uppercase tracking-wider">
                        Più Richiesto
                      </span>
                    </div>
                  )}
                  <h3
                    className="text-2xl font-bold text-[#1B3C1F] mb-1"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    {course.name}
                  </h3>
                  <p className="text-[#5A695C] text-sm mb-6">{course.subtitle}</p>

                  <div className="flex items-baseline gap-1 mb-8 pb-6 border-b border-[#E5E0D4]">
                    <span className="text-4xl font-extrabold text-[#182219]">{course.price}</span>
                    <span className="text-sm text-[#5A695C]">{course.period}</span>
                  </div>

                  <ul className="space-y-3.5 mb-8">
                    {course.features.map((feature) => (
                      <li key={feature} className="flex items-start gap-3 text-sm text-[#182219]">
                        <span className="text-[#1B3C1F] font-bold mt-0.5">✓</span>
                        <span>{feature}</span>
                      </li>
                    ))}
                  </ul>
                </div>

                <Link
                  href="/contatti"
                  className={`w-full inline-flex items-center justify-center py-3.5 px-6 rounded-xl font-semibold text-sm transition-all ${course.highlighted
                    ? "bg-[#1B3C1F] hover:bg-[#112614] text-white shadow-md hover:shadow-lg"
                    : "bg-[#F0F5F1] hover:bg-[#DEE9DF] text-[#1B3C1F]"
                    }`}
                >
                  Richiedi Informazioni o Prova
                </Link>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* 4. Official Academy External Link Banner */}
      {siteConfig.academyUrl && (
        <section className="py-20 bg-[#1B3C1F] text-white">
          <div className="max-w-4xl mx-auto px-4 sm:px-6 text-center">
            <span className="text-xs font-bold uppercase tracking-widest text-[#F7E4D3] block mb-3">
              Sito Ufficiale Academy
            </span>
            <h2
              className="text-3xl sm:text-4xl font-bold mb-4"
              style={{ fontFamily: "var(--font-serif)" }}
            >
              Scopri Tutti i Programmi Agonistici
            </h2>
            <p className="text-[#DEE9DF] text-base sm:text-lg max-w-2xl mx-auto mb-8 font-light">
              Per il calendario tornei FITP, il ranking degli atleti e gli aggiornamenti della Stefanini Tennis Academy.
            </p>
            <a
              href={siteConfig.academyUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-2 rounded-full bg-white text-[#1B3C1F] px-8 py-4 text-base font-semibold shadow-lg hover:bg-white/95 hover:scale-[1.02] transition-all"
            >
              <span>Visita stefaninitennisacademy.it</span>
              <span>→</span>
            </a>
          </div>
        </section>
      )}
    </div>
  );
}
