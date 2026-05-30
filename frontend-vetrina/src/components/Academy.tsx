import { siteConfig } from "@/config/site";

export default function Academy() {
  return (
    <section id="academy" className="py-20 lg:py-28 bg-dark relative overflow-hidden">
      {/* Background decoration */}
      <div className="absolute top-0 right-0 w-96 h-96 bg-accent/10 rounded-full blur-3xl -translate-y-1/2 translate-x-1/2" />
      <div className="absolute bottom-0 left-0 w-72 h-72 bg-accent/5 rounded-full blur-3xl translate-y-1/2 -translate-x-1/2" />

      <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto mb-16">
          <p className="text-accent font-semibold tracking-widest uppercase text-sm mb-3">
            Programma Agonistico
          </p>
          <h2 className="text-4xl lg:text-5xl font-bold text-white mb-6">
            {siteConfig.academyName.split(" ").slice(0, -1).join(" ")}
            <span className="text-accent"> {siteConfig.academyName.split(" ").slice(-1)}</span>
          </h2>
          <p className="text-white/60 text-lg leading-relaxed">
            Il nuovo programma di eccellenza per giovani atleti che puntano al
            professionismo. Metodologia all&apos;avanguardia, staff di primo livello
            e un ambiente pensato per far emergere il talento.
          </p>
        </div>

        {/* Features */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-16">
          {[
            {
              icona: "🏆",
              titolo: "Percorso Agonistico",
              descrizione:
                "Programmi personalizzati per atleti dai 10 ai 18 anni con obiettivi di ranking nazionali e internazionali.",
            },
            {
              icona: "🧠",
              titolo: "Mental Coaching",
              descrizione:
                "Supporto psicologico sportivo per gestire la pressione delle competizioni e sviluppare la resilienza.",
            },
            {
              icona: "💪",
              titolo: "Preparazione Atletica",
              descrizione:
                "Allenamento fisico specifico per il tennis: esplosività, resistenza e prevenzione infortuni.",
            },
          ].map((feature) => (
            <div
              key={feature.titolo}
              className="bg-dark-lighter/50 backdrop-blur-sm border border-white/10 rounded-2xl p-8 hover:border-accent/50 transition-all duration-300 group"
            >
              <span className="text-4xl mb-4 block">{feature.icona}</span>
              <h3 className="text-xl font-bold text-white mb-3 group-hover:text-accent transition-colors">
                {feature.titolo}
              </h3>
              <p className="text-white/50 leading-relaxed">{feature.descrizione}</p>
            </div>
          ))}
        </div>

        {/* CTA */}
        <div className="text-center">
          <div className="inline-flex items-center gap-3 bg-accent/10 border border-accent/30 rounded-full px-6 py-3">
            <span className="text-accent font-semibold">{siteConfig.academyShortName}</span>
            <span className="text-white/60 text-sm">
              Selezioni aperte — Contattaci per una prova gratuita
            </span>
          </div>
        </div>
      </div>
    </section>
  );
}
