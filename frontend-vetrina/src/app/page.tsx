import Hero from "@/components/Hero";
import TeamResults from "@/components/TeamResults";
import Link from "next/link";
import { siteConfig } from "@/config/site";

/**
 * Homepage snella — Multi-pagina.
 *
 * La homepage ora funziona come "landing page" con:
 *   1. Hero Section (invariata)
 *   2. 3 Card riassuntive che puntano alle pagine interne
 *   3. TeamResults (dati Sanity, ISR)
 *
 * I contenuti dettagliati (storia, corsi, gallery, contatti)
 * sono stati spostati nelle rispettive pagine dedicate.
 */

const summaryCards = [
  {
    emoji: "🏛️",
    title: "Il Club",
    description: `Dal ${siteConfig.foundedYear}, un punto di riferimento per lo sport nel cuore della ${siteConfig.region}. Scopri la nostra storia e il nostro team.`,
    href: "/club",
    color: "primary",
  },
  {
    emoji: "🎾",
    title: "Corsi e Agonistica",
    description: `Percorsi formativi per ogni età e livello, dall'avviamento al minitennis fino all'agonismo.`,
    href: "/accademia",
    color: "accent",
  },
  {
    emoji: "🏟️",
    title: "Le Strutture",
    description: `${siteConfig.stats.courts} campi, palestra, piscina e ristorante: tutto ciò che serve per lo sport e il relax.`,
    href: "/strutture",
    color: "secondary",
  },
];

export default function Home() {
  return (
    <>
      <Hero />

      {/* Summary Cards */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Benvenuto
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              Scopri {siteConfig.clubShortName}
            </h2>
            <p className="text-gray-600 text-lg">
              Sport, formazione e comunità: esplora tutto ciò che il nostro circolo ha da offrire.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {summaryCards.map((card) => (
              <Link
                key={card.href}
                href={card.href}
                className="group block bg-light rounded-2xl p-8 hover:shadow-xl transition-all duration-300 hover:-translate-y-2 border border-transparent hover:border-gray-200"
              >
                <span className="text-4xl block mb-4">{card.emoji}</span>
                <h3
                  className="text-xl font-bold text-dark mb-3 group-hover:text-primary transition-colors"
                  style={{ fontFamily: "'Playfair Display', serif" }}
                >
                  {card.title}
                </h3>
                <p className="text-gray-600 text-sm leading-relaxed mb-4">
                  {card.description}
                </p>
                <span className="text-primary font-semibold text-sm inline-flex items-center gap-1 group-hover:gap-2 transition-all">
                  Scopri di più →
                </span>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Risultati Gare — Dati Sanity con ISR */}
      <TeamResults />
    </>
  );
}
