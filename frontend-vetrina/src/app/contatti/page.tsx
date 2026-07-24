import type { Metadata } from "next";
import Image from "next/image";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Contatti | ${siteConfig.clubName}`,
  description: `Contatta ${siteConfig.clubName}: indirizzo, telefono, email e orari. Vieni a trovarci a ${siteConfig.location}.`,
};

/* ------------------------------------------------------------------ */
/* Card dati per i recapiti diretti                                     */
/* ------------------------------------------------------------------ */
const contactCards = [
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
      </svg>
    ),
    title: "Telefono",
    value: siteConfig.phone,
    href: `tel:${siteConfig.phone.replace(/\s/g, "")}`,
    cta: "Chiama ora",
    color: "bg-primary/10 text-primary",
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
      </svg>
    ),
    title: "Email",
    value: siteConfig.email,
    href: `mailto:${siteConfig.email}`,
    cta: "Invia email",
    color: "bg-accent/10 text-accent",
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
        <path d="M.057 24l1.687-6.163c-1.041-1.804-1.588-3.849-1.587-5.946.003-6.556 5.338-11.891 11.893-11.891 3.181.001 6.167 1.24 8.413 3.488 2.245 2.248 3.481 5.236 3.48 8.414-.003 6.557-5.338 11.892-11.893 11.892-1.99-.001-3.951-.5-5.688-1.448l-6.305 1.654zm6.597-3.807c1.676.995 3.276 1.591 5.392 1.592 5.448 0 9.886-4.434 9.889-9.885.002-5.462-4.415-9.89-9.881-9.892-5.452 0-9.887 4.434-9.889 9.884-.001 2.225.651 3.891 1.746 5.634l-.999 3.648 3.742-.981zm11.387-5.464c-.074-.124-.272-.198-.57-.347-.297-.149-1.758-.868-2.031-.967-.272-.099-.47-.149-.669.149-.198.297-.768.967-.941 1.165-.173.198-.347.223-.644.074-.297-.149-1.255-.462-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.297-.347.446-.521.151-.172.2-.296.3-.495.099-.198.05-.372-.025-.521-.075-.148-.669-1.611-.916-2.206-.242-.579-.487-.501-.669-.51l-.57-.01c-.198 0-.52.074-.792.372s-1.04 1.016-1.04 2.479 1.065 2.876 1.213 3.074c.149.198 2.095 3.2 5.076 4.487.709.306 1.263.489 1.694.626.712.226 1.36.194 1.872.118.572-.085 1.758-.719 2.006-1.413.248-.695.248-1.29.173-1.414z"/>
      </svg>
    ),
    title: "WhatsApp",
    value: "Scrivici in chat",
    href: siteConfig.whatsappUrl,
    cta: "Apri WhatsApp",
    color: "bg-green-100 text-green-700",
    external: true,
  },
];

export default function ContattiPage() {
  return (
    <>
      {/* Page Header */}
      <section className="pt-32 pb-16 lg:pt-40 lg:pb-20 bg-gradient-to-b from-dark to-dark/90">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-secondary font-semibold tracking-widest uppercase text-sm mb-3">
            Vieni a trovarci
          </p>
          <h1
            className="text-4xl lg:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "'Playfair Display', serif" }}
          >
            Contatti
          </h1>
          <p className="text-white/70 text-lg max-w-2xl mx-auto">
            Vuoi informazioni sui corsi, prenotare un campo o venirci a trovare?
            Siamo a tua disposizione.
          </p>
        </div>
      </section>

      {/* ============================================================
          CONTATTI DIRETTI + MAPPA — Due colonne
          Colonna sx: recapiti in card + orari
          Colonna dx: mappa statica cliccabile
          ============================================================ */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20">

            {/* ---- Colonna sinistra: Recapiti ---- */}
            <div>
              <h2
                className="text-3xl font-bold text-dark mb-8"
                style={{ fontFamily: "'Playfair Display', serif" }}
              >
                Dove Trovarci
              </h2>

              {/* Indirizzo */}
              <div className="flex items-start gap-4 mb-8">
                <span className="p-3 bg-primary/10 text-primary rounded-xl flex items-center justify-center shrink-0">
                  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                  </svg>
                </span>
                <div>
                  <p className="font-semibold text-dark">Indirizzo</p>
                  <p className="text-gray-600">{siteConfig.address}</p>
                </div>
              </div>

              {/* Contact Cards — CTA grandi e touch-friendly */}
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-10">
                {contactCards.map((card) => (
                  <a
                    key={card.title}
                    href={card.href}
                    {...(card.external
                      ? { target: "_blank", rel: "noopener noreferrer" }
                      : {})}
                    className="group flex flex-col items-center text-center p-5 rounded-2xl border border-gray-100 bg-white shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300"
                  >
                    <span
                      className={`w-12 h-12 flex items-center justify-center rounded-full text-xl mb-3 ${card.color}`}
                    >
                      {card.icon}
                    </span>
                    <p className="font-semibold text-dark text-sm mb-1">
                      {card.title}
                    </p>
                    <p className="text-gray-500 text-xs mb-3 break-all">
                      {card.value}
                    </p>
                    <span className="text-primary text-xs font-semibold group-hover:underline">
                      {card.cta} →
                    </span>
                  </a>
                ))}
              </div>

              {/* Orari */}
              <div className="p-6 bg-light rounded-2xl">
                <div className="flex items-center gap-2 mb-4">
                  <svg className="w-5 h-5 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <h3 className="font-bold text-dark text-lg">Orari</h3>
                </div>
                <div className="space-y-3">
                  {Object.values(siteConfig.hours).map((h) => (
                    <div key={h.label} className="flex justify-between text-sm">
                      <span className="text-gray-600">{h.label}</span>
                      <span className="font-semibold text-dark">{h.time}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* ---- Colonna destra: Mappa statica cliccabile ---- */}
            <div className="flex flex-col justify-center">
              <h2
                className="text-3xl font-bold text-dark mb-8"
                style={{ fontFamily: "'Playfair Display', serif" }}
              >
                La Mappa
              </h2>

              <a
                href={siteConfig.mapsUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="group relative block rounded-2xl overflow-hidden shadow-lg hover:shadow-2xl transition-shadow duration-300"
              >
                {/* Immagine statica della mappa */}
                <div className="relative aspect-video lg:aspect-[4/3]">
                  <Image
                    src="/images/mappa_statica.png"
                    alt={`Posizione del ${siteConfig.clubName} — ${siteConfig.address}`}
                    fill
                    className="object-cover"
                    sizes="(max-width: 1024px) 100vw, 50vw"
                  />

                  {/* Hover overlay */}
                  <div className="absolute inset-0 bg-black/0 group-hover:bg-black/40 transition-all duration-300 flex items-center justify-center">
                    <div className="opacity-0 group-hover:opacity-100 transform scale-90 group-hover:scale-100 transition-all duration-300">
                      <span className="inline-flex items-center gap-2 bg-white text-dark px-6 py-3 rounded-full font-semibold shadow-xl text-sm lg:text-base">
                        <svg className="w-5 h-5 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                        </svg>
                        Clicca per avviare il navigatore
                      </span>
                    </div>
                  </div>
                </div>

                {/* Didascalia sotto la mappa */}
                <div className="bg-light p-4 text-center">
                  <p className="text-dark font-semibold text-sm">
                    {siteConfig.clubName}
                  </p>
                  <p className="text-gray-500 text-xs mt-1">
                    {siteConfig.address}
                  </p>
                </div>
              </a>
            </div>

          </div>
        </div>
      </section>
    </>
  );
}
