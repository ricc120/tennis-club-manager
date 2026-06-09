import type { Metadata } from "next";
import { siteConfig } from "@/config/site";

export const metadata: Metadata = {
  title: `Contatti | ${siteConfig.clubName}`,
  description: `Contatta ${siteConfig.clubName}: indirizzo, telefono, email e orari. Compila il form per informazioni su corsi e iscrizioni.`,
};

export default function ContattiPage() {
  return (
    <>
      {/* Page Header */}
      <section className="pt-32 pb-16 lg:pt-40 lg:pb-20 bg-gradient-to-b from-dark to-dark/90">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-secondary font-semibold tracking-widest uppercase text-sm mb-3">
            Scrivici
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

      {/* Info + Form — Due colonne */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20">
            {/* Colonna sinistra — Info contatto */}
            <div>
              <h2
                className="text-3xl font-bold text-dark mb-8"
                style={{ fontFamily: "'Playfair Display', serif" }}
              >
                Dove Trovarci
              </h2>

              <div className="space-y-6">
                {/* Indirizzo */}
                <div className="flex items-start gap-4">
                  <span className="text-2xl">📍</span>
                  <div>
                    <p className="font-semibold text-dark">Indirizzo</p>
                    <p className="text-gray-600">{siteConfig.address}</p>
                  </div>
                </div>

                {/* Telefono */}
                <div className="flex items-start gap-4">
                  <span className="text-2xl">📞</span>
                  <div>
                    <p className="font-semibold text-dark">Telefono</p>
                    <p className="text-gray-600">{siteConfig.phone}</p>
                  </div>
                </div>

                {/* Email */}
                <div className="flex items-start gap-4">
                  <span className="text-2xl">✉️</span>
                  <div>
                    <p className="font-semibold text-dark">Email</p>
                    <a
                      href={`mailto:${siteConfig.email}`}
                      className="text-primary hover:text-primary-light transition-colors"
                    >
                      {siteConfig.email}
                    </a>
                  </div>
                </div>

                {/* WhatsApp */}
                <div className="flex items-start gap-4">
                  <span className="text-2xl">💬</span>
                  <div>
                    <p className="font-semibold text-dark">WhatsApp</p>
                    <a
                      href={siteConfig.whatsappUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-primary hover:text-primary-light transition-colors"
                    >
                      Scrivici su WhatsApp
                    </a>
                  </div>
                </div>
              </div>

              {/* Orari */}
              <div className="mt-10 p-6 bg-light rounded-2xl">
                <h3 className="font-bold text-dark mb-4 text-lg">🕐 Orari</h3>
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

            {/* Colonna destra — Form */}
            <div>
              <h2
                className="text-3xl font-bold text-dark mb-8"
                style={{ fontFamily: "'Playfair Display', serif" }}
              >
                Scrivici
              </h2>

              <form className="space-y-5" onSubmit={undefined}>
                <div>
                  <label htmlFor="contact-name" className="block text-sm font-medium text-gray-700 mb-1">
                    Nome e Cognome
                  </label>
                  <input
                    type="text"
                    id="contact-name"
                    placeholder="Il tuo nome"
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all text-dark"
                  />
                </div>

                <div>
                  <label htmlFor="contact-email" className="block text-sm font-medium text-gray-700 mb-1">
                    Email
                  </label>
                  <input
                    type="email"
                    id="contact-email"
                    placeholder="la-tua@email.it"
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all text-dark"
                  />
                </div>

                <div>
                  <label htmlFor="contact-subject" className="block text-sm font-medium text-gray-700 mb-1">
                    Oggetto
                  </label>
                  <select
                    id="contact-subject"
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all text-dark bg-white"
                  >
                    <option>Informazioni corsi</option>
                    <option>Prenotazione campo</option>
                    <option>Iscrizione al circolo</option>
                    <option>Altro</option>
                  </select>
                </div>

                <div>
                  <label htmlFor="contact-message" className="block text-sm font-medium text-gray-700 mb-1">
                    Messaggio
                  </label>
                  <textarea
                    id="contact-message"
                    rows={5}
                    placeholder="Scrivi il tuo messaggio..."
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all resize-none text-dark"
                  />
                </div>

                <button
                  type="button"
                  className="w-full bg-primary hover:bg-primary-light text-white py-4 rounded-full font-semibold transition-all hover:shadow-lg hover:shadow-primary/30 text-lg"
                >
                  Invia Messaggio
                </button>
                <p className="text-xs text-gray-400 text-center">
                  Il form è attualmente un mockup. La funzionalità di invio sarà attivata prossimamente.
                </p>
              </form>
            </div>
          </div>
        </div>
      </section>

      {/* Mappa Placeholder */}
      <section className="bg-light">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="rounded-2xl overflow-hidden shadow-sm bg-gray-200 h-64 lg:h-96 flex items-center justify-center">
            <div className="text-center text-gray-500">
              <span className="text-5xl block mb-3">🗺️</span>
              <p className="font-semibold text-lg">Mappa Interattiva</p>
              <p className="text-sm">{siteConfig.address}</p>
              <p className="text-xs mt-2 text-gray-400">
                Integrazione Google Maps disponibile con API key
              </p>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
