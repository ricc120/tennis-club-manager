import type { Metadata } from "next";
import Image from "next/image";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Strutture | ${siteConfig.clubName}`,
  description: `Scopri le strutture di ${siteConfig.clubName}: campi in terra rossa, padel, clubhouse, ristorante e palestra a ${siteConfig.location}.`,
};

export default function StrutturePage() {
  return (
    <>
      {/* Page Header */}
      <section className="pt-32 pb-16 lg:pt-40 lg:pb-20 bg-gradient-to-b from-dark to-dark/90">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-secondary font-semibold tracking-widest uppercase text-sm mb-3">
            Esplora il Club
          </p>
          <h1
            className="text-4xl lg:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "'Playfair Display', serif" }}
          >
            Le Strutture
          </h1>
          <p className="text-white/70 text-lg max-w-2xl mx-auto">
            Un ambiente completo per lo sport e il relax: campi, palestra
            e un ristorante con vista sul verde.
          </p>
        </div>
      </section>

      {/* Stats */}
      <section className="py-12 bg-white border-b border-gray-100">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
            {[
              { value: siteConfig.stats.courts, label: "Campi" },
              { value: siteConfig.stats.members, label: "Tesserati" },
              { value: siteConfig.stats.coaches, label: "Professionisti" },
              { value: siteConfig.stats.years, label: "Anni di attività" },
            ].map((stat) => (
              <div key={stat.label}>
                <p className="text-3xl lg:text-4xl font-bold text-primary">{stat.value}</p>
                <p className="text-gray-500 text-sm mt-1">{stat.label}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Masonry Gallery */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Gallery
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              I Nostri Spazi
            </h2>
          </div>

          {/* CSS Grid — pseudo-masonry con altezze variabili */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {siteConfig.strutture.map((item) => {
              const Wrapper = ('url' in item && item.url) ? 'a' : 'div';
              const wrapperProps = ('url' in item && item.url)
                ? { href: item.url as string, target: '_blank', rel: 'noopener noreferrer' }
                : {};
              return (
                <Wrapper
                  key={item.title}
                  {...wrapperProps}
                  className={`group relative rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 ${item.tall ? "sm:row-span-2" : ""
                    } ${'url' in item && item.url ? "cursor-pointer" : ""}`}
                >
                  <div className={`relative w-full ${item.tall ? "h-80 sm:h-full" : "h-60"}`}>
                    <Image
                      src={item.photo}
                      alt={item.title}
                      fill
                      className="object-cover group-hover:scale-105 transition-transform duration-500"
                      sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
                    />
                    {/* Overlay */}
                    <div className="absolute inset-0 bg-gradient-to-t from-dark/70 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
                    <div className="absolute bottom-0 left-0 right-0 p-5 translate-y-4 group-hover:translate-y-0 opacity-0 group-hover:opacity-100 transition-all duration-300">
                      <span className="text-xs font-semibold text-primary-light uppercase tracking-wide">
                        {item.category}
                      </span>
                      <h3 className="text-white font-bold text-lg mt-1">{item.title}</h3>
                      {'url' in item && item.url && (
                        <span className="inline-flex items-center gap-1 text-secondary text-xs font-semibold mt-2">
                          Visita il sito →
                        </span>
                      )}
                    </div>
                  </div>
                </Wrapper>
              );
            })}
          </div>
        </div>
      </section>
    </>
  );
}
