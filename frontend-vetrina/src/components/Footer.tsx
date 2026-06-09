import { siteConfig } from "@/config/site";

export default function Footer() {
  return (
    <footer className="bg-dark py-16">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-12 mb-12">
          {/* Brand */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <span className="text-2xl">🎾</span>
              <span className="text-white font-bold text-xl">{siteConfig.clubShortName}</span>
            </div>
            <p className="text-white/50 text-sm leading-relaxed">
              {siteConfig.clubName} — Dal {siteConfig.foundedYear}, {siteConfig.clubSlogan.toLowerCase()}.
              {" "}Tennis, Padel e la {siteConfig.academyName}.
            </p>
          </div>

          {/* Links */}
          <div>
            <h4 className="text-white font-semibold mb-4">Link Rapidi</h4>
            <ul className="space-y-2">
              {[
                { label: "I Nostri Campi", href: "/strutture" },
                { label: "Tariffe", href: "/accademia" },
                { label: "Contatti", href: "/contatti" },
              ].map((link) => (
                <li key={link.href}>
                  <a
                    href={link.href}
                    className="text-white/50 hover:text-secondary text-sm transition-colors"
                  >
                    {link.label}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* Orari */}
          <div>
            <h4 className="text-white font-semibold mb-4">Orari di Apertura</h4>
            <div className="space-y-2 text-sm">
              {Object.values(siteConfig.hours).map((h) => (
                <div key={h.label} className="flex justify-between text-white/50">
                  <span>{h.label}</span>
                  <span className="text-white/70">{h.time}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Divider + Copyright */}
        <div className="border-t border-white/10 pt-8 flex flex-col md:flex-row items-center justify-between gap-4">
          <p className="text-white/30 text-sm">
            © {new Date().getFullYear()} {siteConfig.clubName}. Tutti i diritti riservati.
          </p>
          <div className="flex items-center gap-4">
            <span className="text-white/30 text-xs">
              Powered by {siteConfig.academyShortName} — {siteConfig.academyName}
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
}
