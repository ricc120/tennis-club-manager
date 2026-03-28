/**
 * Navbar — Barra di navigazione principale del Tennis Club.
 * 
 * CONCETTI CHIAVE:
 * - Questo è un "componente React": una funzione che restituisce JSX (HTML in TypeScript)
 * - "export default" lo rende importabile da altri file
 * - Le classi CSS si applicano con "className" (non "class")
 * - Le classi come "bg-emerald-700" sono classi Tailwind:
 *   bg = background, emerald = colore, 700 = tonalità (più alto = più scuro)
 * - "Link" di Next.js sostituisce <a> per navigazione client-side (senza ricaricare la pagina)
 */

import Link from "next/link";

export default function Navbar() {
  return (
    <nav className="bg-emerald-800 text-white shadow-lg">
      {/* Container centrato con padding */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Flexbox: distribuisce gli elementi orizzontalmente */}
        <div className="flex items-center justify-between h-16">

          {/* Logo / Nome del club */}
          <Link href="/" className="flex items-center gap-2 group">
            {/* Emoji come placeholder — in futuro metteremo un logo vero */}
            <span className="text-2xl">🎾</span>
            <span className="text-xl font-bold tracking-tight group-hover:text-emerald-200 transition-colors">
              Tennis Club Manager
            </span>
          </Link>

          {/* Link di navigazione */}
          <div className="flex items-center gap-1">
            {/* 
              Ogni link ha:
              - px-3 py-2 = padding orizzontale e verticale
              - rounded-md = bordi arrotondati
              - hover:bg-emerald-700 = sfondo più chiaro al passaggio del mouse
              - transition-colors = animazione fluida del cambio colore
            */}
            <Link
              href="/campi"
              className="px-3 py-2 rounded-md text-sm font-medium hover:bg-emerald-700 transition-colors"
            >
              Campi
            </Link>
            <Link
              href="/prenotazioni"
              className="px-3 py-2 rounded-md text-sm font-medium hover:bg-emerald-700 transition-colors"
            >
              Prenotazioni
            </Link>
            <Link
              href="/login"
              className="ml-2 px-4 py-2 rounded-md text-sm font-medium bg-white text-emerald-800 hover:bg-emerald-100 transition-colors"
            >
              Accedi
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
}
