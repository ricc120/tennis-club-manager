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

      {/* Storia — Layout a due colonne */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 items-center">
            {/* Testo */}
            <div>
              <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
                La Nostra Storia
              </p>
              <h2
                className="text-3xl lg:text-4xl font-bold text-dark mb-6"
                style={{ fontFamily: "'Playfair Display', serif" }}
              >
                {siteConfig.stats.years} anni di passione sportiva
              </h2>
              <div className="space-y-4 text-gray-600 leading-relaxed">
                <p>
                  Fondato nel {siteConfig.foundedYear}, {siteConfig.clubName} è nato dalla volontà
                  di creare uno spazio dove lo sport incontra la comunità. Situato nel cuore di{" "}
                  {siteConfig.location}, il circolo si è evoluto nel tempo diventando un punto
                  di riferimento per appassionati di ogni età e livello.
                </p>
                <p>
                  Oggi il club dispone di {siteConfig.stats.courts} campi da tennis,
                  un team di {siteConfig.stats.coaches} professionisti qualificati e una comunità
                  di oltre {siteConfig.stats.members} tesserati. La nostra missione è promuovere
                  lo sport come strumento di crescita personale, aggregazione e benessere.
                </p>
              </div>
              <Link
                href="/accademia"
                className="inline-flex items-center gap-2 mt-8 text-primary font-semibold hover:text-primary-light transition-colors"
              >
                Scopri i Nostri Corsi →
              </Link>
            </div>

            {/* Immagine */}
            <div className="relative rounded-2xl overflow-hidden shadow-2xl">
              <Image
                src="https://placehold.co/800x600/2C5F2D/FFFFFF?text=Il+Circolo"
                alt={`Vista del ${siteConfig.clubName}`}
                width={800}
                height={600}
                className="w-full h-auto object-cover"
              />
              <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-dark/80 to-transparent p-6">
                <p className="text-white font-bold text-lg">{siteConfig.clubName}</p>
                <p className="text-white/70 text-sm">{siteConfig.location}</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Staff — Griglia 3 colonne */}
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
