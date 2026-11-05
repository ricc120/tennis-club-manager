import type { Metadata } from "next";
import Image from "next/image";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Contatti | ${siteConfig.clubName}`,
  description: `Contatta ${siteConfig.clubName}: indirizzo, telefono, email e orari. Vieni a trovarci a ${siteConfig.location}.`,
};

const contactCards = [
  {
    icon: (
        <svg className="w-6 h-6 text-[#1B3C1F]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.8} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
        </svg>
        ),
        title: "Telefono",
        value: siteConfig.phone,
        href: `tel:${siteConfig.phone.replace(/\s/g, "")}`,
        cta: "Chiama Ora",
        badgeBg: "bg-[#F0F5F1]",
  },
        {
          icon: (
          <svg className="w-6 h-6 text-[#C05621]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.8} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
          </svg>
          ),
          title: "Email",
          value: siteConfig.email,
          href: `mailto:${siteConfig.email}`,
          cta: "Invia Email",
          badgeBg: "bg-[#FCF5EE]",
  },
          {
            icon: (
            <svg className="w-6 h-6 text-[#15803D]" fill="currentColor" viewBox="0 0 24 24">
              <path d="M.057 24l1.687-6.163c-1.041-1.804-1.588-3.849-1.587-5.946.003-6.556 5.338-11.891 11.893-11.891 3.181.001 6.167 1.24 8.413 3.488 2.245 2.248 3.481 5.236 3.48 8.414-.003 6.557-5.338 11.892-11.893 11.892-1.99-.001-3.951-.5-5.688-1.448l-6.305 1.654zm6.597-3.807c1.676.995 3.276 1.591 5.392 1.592 5.448 0 9.886-4.434 9.889-9.885.002-5.462-4.415-9.89-9.881-9.892-5.452 0-9.887 4.434-9.889 9.884-.001 2.225.651 3.891 1.746 5.634l-.999 3.648 3.742-.981zm11.387-5.464c-.074-.124-.272-.198-.57-.347-.297-.149-1.758-.868-2.031-.967-.272-.099-.47-.149-.669.149-.198.297-.768.967-.941 1.165-.173.198-.347.223-.644.074-.297-.149-1.255-.462-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.297-.347.446-.521.151-.172.2-.296.3-.495.099-.198.05-.372-.025-.521-.075-.148-.669-1.611-.916-2.206-.242-.579-.487-.501-.669-.51l-.57-.01c-.198 0-.52.074-.792.372s-1.04 1.016-1.04 2.479 1.065 2.876 1.213 3.074c.149.198 2.095 3.2 5.076 4.487.709.306 1.263.489 1.694.626.712.226 1.36.194 1.872.118.572-.085 1.758-.719 2.006-1.413.248-.695.248-1.29.173-1.414z" />
            </svg>
            ),
            title: "WhatsApp",
            value: "Chat Istantanea",
            href: siteConfig.whatsappUrl,
            cta: "Scrivici",
            badgeBg: "bg-[#F0FDF4]",
            external: true,
  },
            ];

            export default function ContattiPage() {
  return (
            <div className="bg-[#FAF9F5] text-[#182219]">
              {/* 1. Header */}
              <section className="pt-36 pb-20 lg:pt-44 lg:pb-24 bg-[#1B3C1F] text-white relative overflow-hidden">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10">
                  <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/10 text-[#F7E4D3] border border-white/20 text-xs font-semibold uppercase tracking-widest mb-4">
                    Siamo a Tua Disposizione
                  </span>
                  <h1
                    className="text-4xl sm:text-6xl font-bold text-white mb-6"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    Contatti e Prenotazioni
                  </h1>
                  <p className="text-[#DEE9DF] text-lg sm:text-xl max-w-2xl mx-auto font-light leading-relaxed">
                    Per prenotare un campo, richiedere informazioni sui corsi della Stefanini Tennis Academy o visitare il circolo.
                  </p>
                </div>
              </section>

              {/* 2. Main Content Grid */}
              <section className="py-20 lg:py-28 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 lg:gap-16 items-start">
                  {/* Left Column: Direct Action Cards */}
                  <div className="lg:col-span-6 space-y-8">
                    <div>
                      <span className="status-pill mb-3">
                        Recapiti Ufficiali
                      </span>
                      <h2
                        className="text-3xl font-bold text-[#1B3C1F] tracking-tight mb-3"
                        style={{ fontFamily: "var(--font-serif)" }}
                      >
                        Mettiti in Contatto con Noi
                      </h2>
                      <p className="text-[#5A695C] text-sm leading-relaxed">
                        La nostra segreteria è aperta tutti i giorni per assisterti nella prenotazione dei campi o rispondere a qualsiasi domanda.
                      </p>
                    </div>

                    {/* Quick Contact Cards — SVG al centro e testo centrato */}
                    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                      {contactCards.map((card) => (
                        <div
                          key={card.title}
                          className="editorial-card rounded-2xl p-6 flex flex-col items-center text-center justify-between group hover:border-[#1B3C1F]/40 transition-all duration-200"
                        >
                              <div className="flex flex-col items-center w-full">
                                {/* SVG al centro della card */}
                                <div className={`w-13 h-13 rounded-2xl ${card.badgeBg} flex items-center justify-center mb-4 mx-auto shadow-2xs group-hover:scale-105 transition-transform duration-200`}>
                                  {card.icon}
                                </div>
                                <h3 className="font-bold text-base text-[#182219] mb-1">{card.title}</h3>
                                <p className="text-xs text-[#5A695C] truncate w-full mb-5 font-normal">{card.value}</p>
                              </div>
                              <a
                                href={card.href}
                                {...(card.external ? { target: "_blank", rel: "noopener noreferrer" } : {})}
                                className="w-full inline-flex items-center justify-center py-2.5 px-3 rounded-xl bg-[#FAF9F5] hover:bg-[#F0F5F1] text-xs font-semibold text-[#1B3C1F] border border-[#E5E0D4] transition-all hover:border-[#1B3C1F]/30 active:scale-95"
                              >
                                {card.cta}
                              </a>
                            </div>
              ))}
                          </div>

                          {/* Orari Box — icona SVG invece di emoji */}
                          <div className="editorial-card rounded-2xl p-6 sm:p-8">
                            <h3
                              className="text-lg font-bold text-[#1B3C1F] mb-4 flex items-center gap-3"
                              style={{ fontFamily: "var(--font-serif)" }}
                            >
                              <span className="w-8 h-8 rounded-lg bg-[#F0F5F1] border border-[#DEE9DF] flex items-center justify-center text-[#1B3C1F] shrink-0">
                                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                              </span>
                              <span>Orari di Apertura Circolo e Segreteria</span>
                            </h3>
                            <div className="divide-y divide-[#E5E0D4] text-sm">
                              {Object.values(siteConfig.hours).map((h) => (
                                <div key={h.label} className="py-3 flex justify-between items-center first:pt-0 last:pb-0">
                                  <span className="text-[#5A695C] font-medium">{h.label}</span>
                                  <span className="text-[#182219] font-bold">{h.time}</span>
                                </div>
                              ))}
                            </div>
                          </div>

                          {/* Address Banner — icona SVG al posto dell'emoji */}
                          <div className="editorial-card rounded-2xl p-6 flex items-start gap-4 bg-gradient-to-br from-white to-[#FAF9F5]">
                              <div className="w-12 h-12 rounded-2xl bg-[#FCF5EE] border border-[#F7E4D3] flex items-center justify-center text-[#C05621] shrink-0 shadow-2xs">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.8} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.8} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                              </div>
                              <div className="flex-1">
                                <h4 className="font-bold text-sm text-[#182219] mb-1">Come Raggiungerci</h4>
                                <p className="text-xs text-[#5A695C] leading-relaxed mb-3">
                                  {siteConfig.address}
                                </p>
                                <a
                                  href={siteConfig.mapsUrl}
                                  target="_blank"
                                  rel="noopener noreferrer"
                                  className="inline-flex items-center gap-1.5 text-xs font-semibold text-[#C05621] hover:underline"
                                >
                                  <span>Apri in Google Maps</span>
                                  <span>→</span>
                                </a>
                              </div>
                            </div>
                          </div>

                          {/* Right Column: Static Map Frame */}
                          <div className="lg:col-span-6 relative">
                            <div className="editorial-card rounded-3xl overflow-hidden p-3 bg-white">
                              <div className="relative h-96 sm:h-[32rem] w-full rounded-2xl overflow-hidden group">
                                <Image
                                  src="/images/mappa_statica.png"
                                  alt="Mappa Tennis Club Carmignano"
                                  fill
                                  sizes="(max-width: 1024px) 100vw, 50vw"
                                  className="object-cover group-hover:scale-102 transition-transform duration-500"
                                />
                                <div className="absolute inset-0 bg-black/15 group-hover:bg-black/5 transition-colors" />

                                {/* Floating Map Pin Overlay */}
                                <div className="absolute bottom-6 left-6 right-6 bg-white/95 backdrop-blur-md p-4 rounded-2xl shadow-lg border border-[#E5E0D4] flex items-center justify-between">
                                  <div>
                                    <p className="text-xs font-bold text-[#182219]">{siteConfig.clubName}</p>
                                    <p className="text-[11px] text-[#5A695C]">La Serra • Carmignano</p>
                                  </div>
                                  <a
                                    href={siteConfig.mapsUrl}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="inline-flex items-center gap-1 px-4 py-2 rounded-xl bg-[#1B3C1F] text-white text-xs font-semibold hover:bg-[#112614] transition-colors"
                                  >
                                    <span>Naviga</span>
                                    <span>→</span>
                                  </a>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
      </section>
                  </div>
                  );
}
