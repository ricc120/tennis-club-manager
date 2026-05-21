export default function Footer() {
  return (
    <footer className="bg-dark py-16">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-12 mb-12">
          {/* Brand */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <span className="text-2xl">🎾</span>
              <span className="text-white font-bold text-xl">TC Carmignano</span>
            </div>
            <p className="text-white/50 text-sm leading-relaxed">
              Tennis Club Carmignano — Dal 1985, tradizione e innovazione
              nel cuore della Toscana. Tennis, Padel e la Stefanini Tennis Academy.
            </p>
          </div>

          {/* Links */}
          <div>
            <h4 className="text-white font-semibold mb-4">Link Rapidi</h4>
            <ul className="space-y-2">
              {[
                { label: "Chi Siamo", href: "#chi-siamo" },
                { label: "I Nostri Campi", href: "#campi" },
                { label: "S.T.A. Academy", href: "#academy" },
                { label: "Tariffe", href: "#tariffe" },
                { label: "Contatti", href: "#contatti" },
              ].map((link) => (
                <li key={link.href}>
                  <a
                    href={link.href}
                    className="text-white/50 hover:text-padel text-sm transition-colors"
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
              <div className="flex justify-between text-white/50">
                <span>Lunedì – Venerdì</span>
                <span className="text-white/70">8:00 – 22:00</span>
              </div>
              <div className="flex justify-between text-white/50">
                <span>Sabato – Domenica</span>
                <span className="text-white/70">8:00 – 20:00</span>
              </div>
              <div className="flex justify-between text-white/50">
                <span>Segreteria</span>
                <span className="text-white/70">9:00 – 19:00</span>
              </div>
            </div>
          </div>
        </div>

        {/* Divider + Copyright */}
        <div className="border-t border-white/10 pt-8 flex flex-col md:flex-row items-center justify-between gap-4">
          <p className="text-white/30 text-sm">
            © {new Date().getFullYear()} Tennis Club Carmignano. Tutti i diritti riservati.
          </p>
          <div className="flex items-center gap-4">
            <span className="text-white/30 text-xs">
              Powered by S.T.A. — Stefanini Tennis Academy
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
}
