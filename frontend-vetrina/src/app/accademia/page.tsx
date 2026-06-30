import type { Metadata } from "next";
import Link from "next/link";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Accademia | ${siteConfig.clubName}`,
  description: `${siteConfig.academyName} (${siteConfig.academyShortName}) — Corsi di tennis per bambini, adulti e agonisti. Scopri i programmi e le tariffe.`,
};

export default function AccademiaPage() {
  return (
    <>
      {/* Page Header */}
      <section className="pt-32 pb-16 lg:pt-40 lg:pb-20 bg-gradient-to-b from-dark to-dark/90">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-accent font-semibold tracking-widest uppercase text-sm mb-3">
            {siteConfig.academyShortName}
          </p>
          <h1
            className="text-4xl lg:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "'Playfair Display', serif" }}
          >
            L&apos;Accademia
          </h1>
          <p className="text-white/70 text-lg max-w-2xl mx-auto">
            {siteConfig.academyName} — Programmi di eccellenza per ogni livello,
            dal minitennis all&apos;agonismo.
          </p>
        </div>
      </section>

      {/* Introduzione */}
      <section className="py-20 lg:py-24 bg-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2
            className="text-3xl lg:text-4xl font-bold text-dark mb-6"
            style={{ fontFamily: "'Playfair Display', serif" }}
          >
            Il Metodo {siteConfig.academyShortName}
          </h2>
          <p className="text-gray-600 text-lg leading-relaxed">
            La nostra accademia propone un approccio integrato alla formazione sportiva:
            tecnica individuale, preparazione atletica, mental coaching e analisi del gioco.
            Ogni percorso è calibrato sull&apos;età, il livello e gli obiettivi dell&apos;allievo,
            con il supporto costante di un team di {siteConfig.stats.coaches} professionisti.
          </p>
        </div>
      </section>

      {/* Pricing Cards */}
      <section className="py-20 lg:py-28 bg-light">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Corsi & Tariffe
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
                className={`relative rounded-2xl overflow-hidden transition-all duration-300 hover:-translate-y-2 ${
                  course.highlighted
                    ? "bg-primary text-white shadow-2xl shadow-primary/30 ring-2 ring-primary scale-105"
                    : "bg-white text-dark shadow-sm hover:shadow-xl border border-gray-200"
                }`}
              >
                {/* Badge "Più Popolare" */}
                {course.highlighted && (
                  <div className="absolute top-0 right-0 bg-accent text-white text-xs font-bold px-4 py-1 rounded-bl-xl">
                    Più Popolare
                  </div>
                )}

                <div className="p-8">
                  {/* Header */}
                  <h3
                    className="text-2xl font-bold mb-1"
                    style={{ fontFamily: "'Playfair Display', serif" }}
                  >
                    {course.name}
                  </h3>
                  <p className={`text-sm mb-6 ${course.highlighted ? "text-white/70" : "text-gray-500"}`}>
                    {course.subtitle}
                  </p>

                  {/* Prezzo */}
                  <div className="mb-8">
                    <span className="text-4xl font-bold">{course.price}</span>
                    <span className={`text-sm ${course.highlighted ? "text-white/60" : "text-gray-400"}`}>
                      {course.period}
                    </span>
                  </div>

                  {/* Features */}
                  <ul className="space-y-3 mb-8">
                    {course.features.map((feat) => (
                      <li key={feat} className="flex items-start gap-3">
                        <span className={`text-sm mt-0.5 ${course.highlighted ? "text-accent" : "text-primary"}`}>
                          ✓
                        </span>
                        <span className={`text-sm ${course.highlighted ? "text-white/90" : "text-gray-600"}`}>
                          {feat}
                        </span>
                      </li>
                    ))}
                  </ul>

                  {/* CTA */}
                  <Link
                    href="/contatti"
                    className={`block text-center py-3 rounded-full font-semibold transition-all ${
                      course.highlighted
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
