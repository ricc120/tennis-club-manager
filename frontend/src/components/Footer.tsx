/**
 * Footer — Piè di pagina del sito.
 * 
 * CONCETTI CHIAVE:
 * - "mt-auto" è un trucco Tailwind: spinge il footer in fondo alla pagina
 *   anche quando il contenuto è poco (funziona perché il body ha "flex flex-col min-h-full")
 * - Le icone sono emoji placeholder — in un progetto reale useresti una libreria come Lucide React
 */

export default function Footer() {
  return (
    <footer className="mt-auto bg-gray-900 text-gray-300">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Grid a 3 colonne su desktop, 1 su mobile */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">

          {/* Colonna 1: Info club */}
          <div>
            <h3 className="text-white font-semibold text-lg mb-3">
              🎾 Tennis Club Manager
            </h3>
            <p className="text-sm leading-relaxed">
              Gestisci prenotazioni, campi e lezioni del tuo tennis club
              in modo semplice e veloce.
            </p>
          </div>

          {/* Colonna 2: Link rapidi */}
          <div>
            <h3 className="text-white font-semibold text-lg mb-3">
              Link Rapidi
            </h3>
            <ul className="space-y-2 text-sm">
              <li>
                <a href="/campi" className="hover:text-emerald-400 transition-colors">
                  Campi
                </a>
              </li>
              <li>
                <a href="/prenotazioni" className="hover:text-emerald-400 transition-colors">
                  Prenotazioni
                </a>
              </li>
              <li>
                <a href="/login" className="hover:text-emerald-400 transition-colors">
                  Accedi
                </a>
              </li>
            </ul>
          </div>

          {/* Colonna 3: Contatti */}
          <div>
            <h3 className="text-white font-semibold text-lg mb-3">
              Contatti
            </h3>
            <ul className="space-y-2 text-sm">
              <li>📍 Via del Tennis, 42 — Roma</li>
              <li>📞 +39 06 1234567</li>
              <li>✉️ info@tennisclub.it</li>
            </ul>
          </div>
        </div>

        {/* Separatore + Copyright */}
        <div className="mt-8 pt-6 border-t border-gray-700 text-center text-sm text-gray-500">
          © {new Date().getFullYear()} Tennis Club Manager. Tutti i diritti riservati.
        </div>
      </div>
    </footer>
  );
}
