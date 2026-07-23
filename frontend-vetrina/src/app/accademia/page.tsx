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
    <>
      {/* ============================================================
          HERO — Vista panoramica dei campi come sfondo
          Immagine: /images/VISTA_TENNIS.jpg
          ============================================================ */}
      <section className="relative pt-32 pb-20 lg:pt-40 lg:pb-28 overflow-hidden">
        {/* Dark overlay for readability */}
        <div className="absolute inset-0 bg-gradient-to-b from-dark to-dark/90" />

        <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-accent font-semibold tracking-widest uppercase text-sm mb-3">
            {siteConfig.academyShortName}
          </p>
          <h1
            className="text-4xl lg:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "'Playfair Display', serif" }}
          >
            {siteConfig.academyName}
          </h1>
          <p className="text-white/80 text-lg lg:text-xl max-w-2xl mx-auto">
            L&apos;eccellenza del tennis a {siteConfig.location} — Programmi per ogni livello,
            dal minitennis all&apos;agonismo professionale.
          </p>
        </div>
      </section>

      {/* ============================================================
          TENNIS ACADEMY — L'Eccellenza
          Contenuto hardcoded fornito dal cliente TC Carmignano.
          ============================================================ */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Header */}
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-accent font-semibold tracking-widest uppercase text-sm mb-3">
              L&apos;Eccellenza
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              La Tennis Academy
            </h2>
          </div>

          {/* Riga 1: Testo a sinistra — Immagine terrazza a destra */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 items-center mb-20">
            <div>
              <div className="space-y-5 text-gray-700 leading-relaxed text-base lg:text-lg">
                <p>
                  Avendo ospitato atleti professionisti da tutta Italia, il circolo ha fatto un
                  salto di qualità creando una vera e propria{" "}
                  <strong className="text-dark">Tennis Academy</strong>.
                </p>
                <p>
                  Alla guida tecnica c&apos;è{" "}
                  <strong className="text-dark">Jacopo Stefanini</strong> (27 anni), maestro ed
                  ex-professionista che in carriera vanta vittorie contro giocatori del calibro di{" "}
                  <strong className="text-accent">Zverev</strong>,{" "}
                  <strong className="text-accent">Medvedev</strong> e{" "}
                  <strong className="text-accent">Berrettini</strong>.
                </p>
                <p>
                  L&apos;obiettivo dell&apos;Academy è offrire allenamento di alto livello senza le
                  pressioni dei circoli tradizionali.
                </p>
              </div>
            </div>
            <div className="relative rounded-2xl overflow-hidden shadow-2xl group">
              <Image
                src="/images/logo_accademia.png"
                alt="Terrazza del TC Carmignano con giocatori"
                width={800}
                height={600}
                className="w-full h-auto object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
          </div>

          {/* Highlight — Lucrezia Stefanini */}
          <div className="relative bg-gradient-to-r from-primary-dark via-primary to-primary-light rounded-3xl p-8 lg:p-12 mb-20 overflow-hidden">
            {/* Decorative */}
            <div className="absolute top-0 right-0 w-64 h-64 bg-white/5 rounded-full -translate-y-1/2 translate-x-1/2" />
            <div className="absolute bottom-0 left-0 w-40 h-40 bg-white/5 rounded-full translate-y-1/2 -translate-x-1/2" />

            <div className="relative z-10 grid grid-cols-1 lg:grid-cols-5 gap-8 items-center">
              <div className="lg:col-span-3 text-white">
                <span className="text-accent font-bold text-sm tracking-widest uppercase">
                  Talento dell&apos;Academy
                </span>
                <h3
                  className="text-2xl lg:text-3xl font-bold mt-2 mb-4"
                  style={{ fontFamily: "'Playfair Display', serif" }}
                >
                  Lucrezia Stefanini
                </h3>
                <p className="text-white/80 leading-relaxed text-base lg:text-lg">
                  Tra i primi talenti dell&apos;Academy spicca{" "}
                  <strong>Lucrezia Stefanini</strong> (Best ranking{" "}
                  <span className="text-accent font-bold">99 WTA</span>), giocatrice di livello
                  mondiale che calca i campi dei tornei del Grande Slam:{" "}
                  <em>Roland Garros, Wimbledon, US Open, Australian Open</em>.
                </p>
              </div>
              <div className="lg:col-span-2 flex justify-center">
                <div className="grid grid-cols-2 gap-3 text-center">
                  {[
                    { value: "99", label: "Best WTA" },
                    { value: "4", label: "Grand Slam" },
                    { value: "20", label: "Giovani Atleti" },
                    { value: "🇮🇹", label: "Tutta Italia" },
                  ].map((stat) => (
                    <div
                      key={stat.label}
                      className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/10"
                    >
                      <p className="text-2xl font-bold text-accent">{stat.value}</p>
                      <p className="text-white/60 text-xs mt-1">{stat.label}</p>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          {/* Riga finale — Obiettivo */}
          <div className="max-w-3xl mx-auto text-center">
            <p className="text-gray-700 text-lg leading-relaxed">
              Oggi l&apos;Academy ospita già{" "}
              <strong className="text-primary">20 giovani promesse</strong> provenienti da tutta
              Italia, pronti a tentare la scalata al professionismo.
            </p>
          </div>
        </div>
      </section>

      {/* ============================================================
          CORSI & TARIFFE — Pricing Cards (invariato da siteConfig)
          ============================================================ */}
      <section className="py-20 lg:py-28 bg-light">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Corsi &amp; Tariffe
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              Scegli il Tuo Percorso
            </h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 items-stretch">
            {siteConfig.courses.map((course) => (
              <div
                key={course.name}
                className={`relative rounded-2xl overflow-hidden transition-all duration-300 hover:-translate-y-2 ${course.highlighted
                  ? "bg-primary text-white shadow-2xl shadow-primary/30 ring-2 ring-primary scale-105"
                  : "bg-white text-dark shadow-sm hover:shadow-xl border border-gray-200"
                  }`}
              >
                {/* Badge "Più Popolare" */}
                {course.highlighted && (
                  <div className="absolute top-0 right-0 bg-accent text-white text-xs font-bold px-3 py-1 rounded-bl-xl">
                    Più Popolare
                  </div>
                )}

                <div className="p-8">
                  <h3
                    className="text-xl font-bold mb-2"
                    style={{ fontFamily: "'Playfair Display', serif" }}
                  >
                    {course.name}
                  </h3>
                  <p
                    className={`text-sm mb-4 ${course.highlighted ? "text-white/70" : "text-gray-500"
                      }`}
                  >
                    {course.subtitle}
                  </p>
                  <div className="flex items-baseline gap-1 mb-6">
                    <span className="text-4xl font-bold">{course.price}</span>
                    <span
                      className={`text-sm ${course.highlighted ? "text-white/60" : "text-gray-400"
                        }`}
                    >
                      /mese
                    </span>
                  </div>

                  {/* Features */}
                  <ul className="space-y-3 mb-8">
                    {course.features.map((feature) => (
                      <li key={feature} className="flex items-start gap-2 text-sm">
                        <span
                          className={`mt-0.5 ${course.highlighted ? "text-accent" : "text-primary"
                            }`}
                        >
                          ✓
                        </span>
                        <span
                          className={
                            course.highlighted ? "text-white/80" : "text-gray-600"
                          }
                        >
                          {feature}
                        </span>
                      </li>
                    ))}
                  </ul>

                  {/* CTA */}
                  <Link
                    href="/contatti"
                    className={`block text-center py-3 rounded-full font-semibold transition-all ${course.highlighted
                      ? "bg-white text-primary hover:bg-white/90"
                      : "bg-primary text-white hover:bg-primary-light hover:shadow-lg hover:shadow-primary/20"
                      }`}
                  >
                    Richiedi Info
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Banner Accademia — Link esterno */}
      {siteConfig.academyUrl && (
        <section className="py-16 lg:py-20 bg-gradient-to-br from-primary-dark via-primary to-primary-light relative overflow-hidden">
          {/* Decorative elements */}
          <div className="absolute top-0 right-0 w-64 h-64 bg-white/5 rounded-full -translate-y-1/2 translate-x-1/2" />
          <div className="absolute bottom-0 left-0 w-48 h-48 bg-white/5 rounded-full translate-y-1/2 -translate-x-1/2" />

          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10">
            <span className="text-accent font-bold text-sm tracking-widest uppercase">
              {siteConfig.academyShortName}
            </span>
            <h2
              className="text-3xl lg:text-4xl font-bold text-white mt-3 mb-4"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              {siteConfig.academyName}
            </h2>
            <p className="text-white/70 text-lg max-w-2xl mx-auto mb-8">
              Scopri tutti i programmi, i coach e le novità dell&apos;accademia
              sul sito ufficiale.
            </p>
            <a
              href={siteConfig.academyUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-2 bg-white text-primary px-8 py-4 rounded-full text-lg font-semibold transition-all hover:shadow-xl hover:shadow-white/20 hover:-translate-y-0.5 hover:bg-white/95"
            >
              Visita {siteConfig.academyShortName}
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
              </svg>
            </a>
          </div>
        </section>
      )}
    </>
  );
}
