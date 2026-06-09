import { staff } from "@/data/staff";
import { siteConfig } from "@/config/site";

export default function ChiSiamo() {
  return (
    <section id="chi-siamo" className="py-20 lg:py-28 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto mb-16">
          <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
            La Nostra Storia
          </p>
          <h2 className="text-4xl lg:text-5xl font-bold text-dark mb-6">
            Chi Siamo
          </h2>
          <p className="text-gray-600 text-lg leading-relaxed">
            Fondato nel {siteConfig.foundedYear}, il {siteConfig.clubName} è un punto di riferimento
            per gli appassionati di racchetta nella provincia di {siteConfig.location.split(", ")[1]}.
            Immerso nel verde delle colline {siteConfig.region.toLowerCase()}e, il club offre
            un&apos;esperienza unica che unisce la tradizione del tennis alla modernità del padel,
            con un ristorante che completa l&apos;offerta per i soci e i loro ospiti.
          </p>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-6 mb-20">
          {[
            { valore: siteConfig.stats.years, label: "Anni di Storia" },
            { valore: siteConfig.stats.courts, label: "Campi da Gioco" },
            { valore: siteConfig.stats.members, label: "Soci Attivi" },
            { valore: siteConfig.stats.coaches, label: "Istruttori" },
          ].map((stat) => (
            <div
              key={stat.label}
              className="text-center p-6 rounded-2xl bg-primary-50 border border-primary-100"
            >
              <p className="text-3xl lg:text-4xl font-bold text-primary mb-1">
                {stat.valore}
              </p>
              <p className="text-gray-600 text-sm font-medium">{stat.label}</p>
            </div>
          ))}
        </div>

        {/* Staff */}
        <div className="text-center mb-12">
          <h3 className="text-3xl font-bold text-dark mb-4">Il Nostro Staff</h3>
          <p className="text-gray-600 max-w-xl mx-auto">
            Professionisti qualificati e appassionati, pronti ad accompagnarti
            nel tuo percorso sportivo.
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          {staff.map((membro) => (
            <div key={membro.nome} className="group text-center">
              <div className="w-32 h-32 mx-auto mb-4 rounded-full overflow-hidden border-4 border-primary-100 group-hover:border-primary transition-colors duration-300">
                <img
                  src={membro.immagine}
                  alt={membro.nome}
                  className="w-full h-full object-cover"
                />
              </div>
              <h4 className="font-semibold text-dark text-lg">{membro.nome}</h4>
              <p className="text-primary text-sm font-medium mb-2">{membro.ruolo}</p>
              <p className="text-gray-500 text-sm leading-relaxed">{membro.bio}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
