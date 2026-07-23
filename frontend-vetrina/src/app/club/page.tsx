import type { Metadata } from "next";
import Image from "next/image";
import Link from "next/link";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Il Club | ${siteConfig.clubName}`,
  description: `Scopri la storia, i valori e il team di ${siteConfig.clubName}. Dal ${siteConfig.foundedYear}, un punto di riferimento per il tennis a ${siteConfig.location}.`,
};

export default function ClubPage() {
  return (
    <>
      {/* Page Header */}
      <section className="pt-32 pb-16 lg:pt-40 lg:pb-20 bg-gradient-to-b from-dark to-dark/90">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-primary-light font-semibold tracking-widest uppercase text-sm mb-3">
            Dal {siteConfig.foundedYear}
          </p>
          <h1
            className="text-4xl lg:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "'Playfair Display', serif" }}
          >
            Il Club
          </h1>
          <p className="text-white/70 text-lg max-w-2xl mx-auto">
            Tradizione sportiva e passione per il tennis nel cuore della {siteConfig.region}.
          </p>
        </div>
      </section>

      {/* ============================================================
          CHI SIAMO — Le Origini e la Passione
          Contenuto hardcoded fornito dal cliente TC Carmignano.
          Layout: testo + immagine a due colonne, inverso su seconda riga.
          ============================================================ */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Header */}
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Le Origini e la Passione
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              La Nostra Storia
            </h2>
          </div>

          {/* Riga 1: Testo a sinistra — Immagine vialetto a destra */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 items-center mb-20">
            <div>
              <div className="space-y-5 text-gray-700 leading-relaxed text-base lg:text-lg">
                <p>
                  Tutto nasce negli anni &apos;90 dal sogno di due maestri nazionali,{" "}
                  <strong className="text-dark">Marco e Roberta Stefanini</strong>, uniti
                  dall&apos;amore per questo sport. Dopo anni di ricerche e ostacoli burocratici,
                  nel 2008 trovano il luogo perfetto nella bellissima zona panoramica de{" "}
                  <em>&ldquo;La Serra&rdquo;</em> (tra Poggio a Caiano e Carmignano).
                </p>
                <p>
                  Nel <strong className="text-dark">2009</strong> posano la prima pietra e nel{" "}
                  <strong className="text-dark">2011</strong> inaugurano i primi campi affiliandosi
                  alla FIT. La struttura è stata poi completata nel{" "}
                  <strong className="text-dark">2017</strong> con l&apos;aggiunta di bar, ristorante
                  e pizzeria.
                </p>
              </div>
            </div>
            <div className="relative rounded-2xl overflow-hidden shadow-2xl group">
              <Image
                src="/images/vialetto.jpg"
                alt="Vialetto di ingresso del Tennis Club Carmignano"
                width={800}
                height={600}
                className="w-full h-auto object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
          </div>

          {/* Riga 2: Immagine padel/clubhouse a sinistra — Testo a destra */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 items-center">
            <div className="relative rounded-2xl overflow-hidden shadow-2xl group order-2 lg:order-1">
              <Image
                src="/images/padel2_club_house.jpg"
                alt="Campi padel e club house del TC Carmignano"
                width={800}
                height={600}
                className="w-full h-auto object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
            <div className="order-1 lg:order-2">
              <div className="space-y-5 text-gray-700 leading-relaxed text-base lg:text-lg">
                <p>
                  Oggi il {siteConfig.clubName} vanta il{" "}
                  <strong className="text-dark">miglior staff tecnico della zona</strong>,
                  capace di formare giocatori di ogni livello. La scuola tennis conta circa{" "}
                  <strong className="text-primary">100 iscritti</strong> tra ragazzi e adulti.
                </p>
                <p>
                  L&apos;offerta si è ampliata introducendo campi da{" "}
                  <strong className="text-dark">Padel</strong>, centri estivi, atletica e tornei
                  che portano oltre{" "}
                  <strong className="text-primary">4.000 presenze l&apos;anno</strong>.
                </p>
              </div>
              <Link
                href="/accademia"
                className="inline-flex items-center gap-2 mt-8 bg-primary hover:bg-primary-light text-white px-6 py-3 rounded-full font-semibold transition-all hover:shadow-lg hover:shadow-primary/30 hover:-translate-y-0.5"
              >
                Scopri l&apos;Accademia →
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Staff — Griglia 3 colonne (invariata) */}
      <section className="py-20 lg:py-28 bg-light">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Il Team
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              I Nostri Professionisti
            </h2>
            <p className="text-gray-600 text-lg">
              Un team di maestri qualificati FIT, preparatori atletici e figure specializzate
              per accompagnarti nel tuo percorso sportivo.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
            {siteConfig.staff.map((member) => (
              <div
                key={member.name}
                className="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 hover:-translate-y-1"
              >
                <div className="relative h-64 overflow-hidden">
                  <Image
                    src={member.photo}
                    alt={member.name}
                    fill
                    className="object-cover group-hover:scale-105 transition-transform duration-500"
                    sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
                  />
                </div>
                <div className="p-6 text-center">
                  <h3 className="text-lg font-bold text-dark">{member.name}</h3>
                  <p className="text-primary text-sm font-medium mt-1">{member.role}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}
