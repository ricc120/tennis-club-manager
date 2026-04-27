/**
 * Home — La pagina principale (rotta "/").
 * 
 * CONCETTI CHIAVE:
 * - Questo file si chiama "page.tsx" dentro "app/" → corrisponde a http://localhost:3000/
 * - Se creassi "app/campi/page.tsx" → corrisponderebbe a http://localhost:3000/campi
 * - Le classi Tailwind "md:" e "sm:" sono breakpoint responsive:
 *   sm = 640px+, md = 768px+, lg = 1024px+
 *   Senza prefisso = mobile first (si applica sempre)
 */

import Link from "next/link";

export default function Home() {
  return (
    <div>
      {/* HERO SECTION — La prima cosa che vede l'utente */}
      <section className="relative bg-emerald-800 text-white overflow-hidden">
        {/* Pattern decorativo di sfondo */}
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-10 left-10 text-9xl">🎾</div>
          <div className="absolute bottom-10 right-10 text-9xl">🏆</div>
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-[12rem]">🎾</div>
        </div>

        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 md:py-32">
          <div className="text-center">
            <h1 className="text-4xl md:text-6xl font-bold tracking-tight mb-6">
              Il Tuo Tennis Club,
              <br />
              <span className="text-emerald-300">Sempre a Portata di Mano</span>
            </h1>
            <p className="text-lg md:text-xl text-emerald-100 max-w-2xl mx-auto mb-10">
              Prenota campi, scopri le lezioni e gestisci la tua attività tennistica
              in modo semplice e veloce.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                href="/campi"
                className="px-8 py-3 bg-white text-emerald-800 font-semibold rounded-lg hover:bg-emerald-50 transition-colors shadow-lg"
              >
                Vedi i Campi
              </Link>
              <Link
                href="/register"
                className="px-8 py-3 border-2 border-white text-white font-semibold rounded-lg hover:bg-white hover:text-emerald-800 transition-colors"
              >
                Registrati Ora
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* FEATURES SECTION — Le 3 funzionalità principali */}
      <section className="bg-white dark:bg-gray max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-12">
          Cosa Puoi Fare
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* Card 1 */}
          <div className="bg-white rounded-xl shadow-md p-8 hover:shadow-lg transition-shadow border border-gray-100">
            <div className="text-4xl mb-4">📅</div>
            <h3 className="text-xl font-semibold text-gray-800 mb-2">
              Prenota un Campo
            </h3>
            <p className="text-gray-600">
              Scegli data, orario e campo da tennis. Conferma in pochi click.
            </p>
          </div>
          {/* Card 2 */}
          <div className="bg-white rounded-xl shadow-md p-8 hover:shadow-lg transition-shadow border border-gray-100">
            <div className="text-4xl mb-4">🏟️</div>
            <h3 className="text-xl font-semibold text-gray-800 mb-2">
              Stato dei Campi
            </h3>
            <p className="text-gray-600">
              Visualizza in tempo reale quali campi sono liberi e quali occupati.
            </p>
          </div>
          {/* Card 3 */}
          <div className="bg-white rounded-xl shadow-md p-8 hover:shadow-lg transition-shadow border border-gray-100">
            <div className="text-4xl mb-4">🎓</div>
            <h3 className="text-xl font-semibold text-gray-800 mb-2">
              Accademia
            </h3>
            <p className="text-gray-600">
              Iscriviti alle lezioni, segui i progressi e ricevi feedback dal maestro.
            </p>
          </div>
        </div>
      </section>
    </div>
  );
}
