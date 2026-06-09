import { siteConfig } from "@/config/site";

const campi = [
  {
    tipo: "Tennis",
    colore: "primary",
    icona: "🎾",
    items: [
      { nome: "4 Campi in Terra Rossa", dettaglio: "Superficie tradizionale, manutenzione quotidiana" },
      { nome: "Illuminazione Serale", dettaglio: "Gioca fino alle 22:00 tutto l'anno" },
      { nome: "Tribuna Coperta", dettaglio: "80 posti per eventi e tornei" },
    ],
  },
  {
    tipo: "Padel",
    colore: "secondary",
    icona: "🏸",
    items: [
      { nome: "2 Campi Panoramici", dettaglio: "Vetro temperato con vista collinare" },
      { nome: "Illuminazione LED", dettaglio: "Luce uniforme e a basso consumo" },
      { nome: "Noleggio Attrezzatura", dettaglio: "Racchette e palline disponibili in segreteria" },
    ],
  },
];

export default function Campi() {
  return (
    <section id="campi" className="py-20 lg:py-28 bg-light">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto mb-16">
          <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
            Le Nostre Strutture
          </p>
          <h2 className="text-4xl lg:text-5xl font-bold text-dark mb-6">
            I Nostri Campi
          </h2>
          <p className="text-gray-600 text-lg">
            Strutture moderne immerse nel verde, per un&apos;esperienza di gioco unica.
          </p>
        </div>

        {/* Cards */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {campi.map((campo) => (
            <div
              key={campo.tipo}
              className={`rounded-3xl overflow-hidden border-2 ${
                campo.colore === "primary"
                  ? "border-primary-100 hover:border-primary"
                  : "border-secondary-50 hover:border-secondary"
              } bg-white shadow-lg hover:shadow-xl transition-all duration-300 hover:-translate-y-1`}
            >
              {/* Card Header */}
              <div
                className={`px-8 py-6 ${
                  campo.colore === "primary"
                    ? "bg-gradient-to-r from-primary to-primary-light"
                    : "bg-gradient-to-r from-secondary-dark to-secondary"
                }`}
              >
                <div className="flex items-center gap-3">
                  <span className="text-4xl">{campo.icona}</span>
                  <div>
                    <h3 className="text-2xl font-bold text-white">{campo.tipo}</h3>
                    <p className="text-white/70 text-sm">{siteConfig.clubShortName}</p>
                  </div>
                </div>
              </div>

              {/* Card Body */}
              <div className="p-8 space-y-5">
                {campo.items.map((item) => (
                  <div key={item.nome} className="flex gap-4">
                    <div
                      className={`w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0 ${
                        campo.colore === "primary"
                          ? "bg-primary-50 text-primary"
                          : "bg-secondary-50 text-secondary-dark"
                      }`}
                    >
                      ✓
                    </div>
                    <div>
                      <p className="font-semibold text-dark">{item.nome}</p>
                      <p className="text-gray-500 text-sm">{item.dettaglio}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
