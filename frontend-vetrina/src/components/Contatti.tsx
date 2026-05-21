"use client";

import { useState } from "react";

export default function Contatti() {
  const [modalAperta, setModalAperta] = useState(false);

  return (
    <>
      <section id="contatti" className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Section Header */}
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-tennis font-semibold tracking-widest uppercase text-sm mb-3">
              Vieni a Trovarci
            </p>
            <h2 className="text-4xl lg:text-5xl font-bold text-dark mb-6">
              Contatti
            </h2>
            <p className="text-gray-600 text-lg">
              Siamo a tua disposizione per informazioni, prenotazioni e visite guidate al club.
            </p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
            {/* Info + Buttons */}
            <div className="space-y-8">
              {/* Info Cards */}
              {[
                { icona: "📍", titolo: "Indirizzo", testo: "Via dello Sport 42, 59015 Carmignano (PO)" },
                { icona: "📞", titolo: "Telefono", testo: "+39 055 XXX XXXX" },
                { icona: "✉️", titolo: "Email", testo: "info@tccarmignano.it" },
                { icona: "🕐", titolo: "Orari", testo: "Lun–Ven: 8:00–22:00 | Sab–Dom: 8:00–20:00" },
              ].map((info) => (
                <div key={info.titolo} className="flex items-start gap-4">
                  <div className="w-12 h-12 rounded-xl bg-tennis-50 flex items-center justify-center text-xl flex-shrink-0">
                    {info.icona}
                  </div>
                  <div>
                    <p className="font-semibold text-dark">{info.titolo}</p>
                    <p className="text-gray-600">{info.testo}</p>
                  </div>
                </div>
              ))}

              {/* Action Buttons */}
              <div className="flex flex-col sm:flex-row gap-4 pt-4">
                <button
                  onClick={() => setModalAperta(true)}
                  className="flex items-center justify-center gap-2 bg-tennis hover:bg-tennis-light text-white px-6 py-4 rounded-xl font-semibold transition-all hover:shadow-lg hover:shadow-tennis/30"
                >
                  📅 Prenota Ora
                </button>
                <a
                  href="https://wa.me/39055XXXXXXX"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center justify-center gap-2 bg-green-500 hover:bg-green-600 text-white px-6 py-4 rounded-xl font-semibold transition-all hover:shadow-lg"
                >
                  💬 WhatsApp
                </a>
              </div>
            </div>

            {/* Contact Form (placeholder) */}
            <div className="bg-light rounded-2xl p-8 border border-gray-200">
              <h3 className="text-xl font-bold text-dark mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>
                Scrivici un Messaggio
              </h3>
              <form className="space-y-4" onSubmit={(e) => e.preventDefault()}>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Nome</label>
                  <input
                    type="text"
                    placeholder="Il tuo nome"
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-tennis focus:border-tennis outline-none transition-all text-dark"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <input
                    type="email"
                    placeholder="la.tua@email.com"
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-tennis focus:border-tennis outline-none transition-all text-dark"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Messaggio</label>
                  <textarea
                    rows={4}
                    placeholder="Come possiamo aiutarti?"
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-tennis focus:border-tennis outline-none transition-all resize-none text-dark"
                  />
                </div>
                <button
                  type="button"
                  className="w-full bg-tennis hover:bg-tennis-light text-white py-3 rounded-xl font-semibold transition-all hover:shadow-lg"
                >
                  Invia Messaggio
                </button>
              </form>
            </div>
          </div>
        </div>
      </section>

      {/* Booking Modal */}
      {modalAperta && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-dark/60 backdrop-blur-sm animate-fade-in"
          onClick={() => setModalAperta(false)}
        >
          <div
            className="bg-white rounded-2xl p-8 max-w-md mx-4 shadow-2xl animate-fade-in-up"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="text-center">
              <span className="text-5xl mb-4 block">🎾</span>
              <h3 className="text-2xl font-bold text-dark mb-3" style={{ fontFamily: "'Playfair Display', serif" }}>
                Prenotazione Online
              </h3>
              <p className="text-gray-600 mb-6">
                Il sistema di prenotazione online è in fase di sviluppo.
                Nel frattempo, contatta la segreteria per prenotare il tuo campo.
              </p>
              <div className="space-y-3">
                <p className="text-sm text-gray-500">
                  📞 +39 055 XXX XXXX
                </p>
                <p className="text-sm text-gray-500">
                  ✉️ info@tccarmignano.it
                </p>
              </div>
              <button
                onClick={() => setModalAperta(false)}
                className="mt-6 bg-tennis hover:bg-tennis-light text-white px-8 py-3 rounded-xl font-semibold transition-all"
              >
                Ho Capito
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
