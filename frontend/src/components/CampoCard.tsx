/**
 * CampoCard — Mostra le informazioni di un singolo campo da tennis.
 * 
 * CONCETTO CHIAVE: le PROPS (proprietà).
 * In Java passi dati a un oggetto tramite il costruttore:
 *   new CampoCard(campo)
 * In React li passi come attributi JSX:
 *   <CampoCard campo={mioCampo} />
 * 
 * Il componente RICEVE le props come parametro della funzione.
 * TypeScript ci aiuta definendo il tipo delle props con un'interfaccia.
 * 
 * PATTERN "PROPS INTERFACE":
 * Definiamo un'interfaccia per le props del componente.
 * Questo è una best practice — rende esplicito cosa il componente si aspetta.
 */

import { Campo } from "@/types";

// Interfaccia delle props: questo componente richiede un oggetto Campo
interface CampoCardProps {
  campo: Campo;
}

/**
 * Il componente riceve le props come primo parametro.
 * Usiamo la destructuring { campo } per estrarre direttamente l'oggetto.
 * 
 * Equivalente lungo: function CampoCard(props: CampoCardProps) { const campo = props.campo; }
 */
export default function CampoCard({ campo }: CampoCardProps) {
  return (
    <div className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden hover:shadow-lg transition-shadow duration-300">
      {/* 
        HEADER DELLA CARD — sfondo colorato diverso in base alla superficie.
        Usiamo una funzione helper per scegliere il colore.
      */}
      <div className={`px-6 py-4 ${getColoreSuperficie(campo.tipoSuperficie)}`}>
        <h3 className="text-lg font-bold text-white">{campo.nome}</h3>
      </div>

      {/* CORPO DELLA CARD — dettagli del campo */}
      <div className="px-6 py-5 space-y-3">
        {/* Tipo di superficie */}
        <div className="flex items-center gap-2 text-gray-600">
          <span className="text-lg">🏟️</span>
          <span className="text-sm font-medium">{campo.tipoSuperficie}</span>
        </div>

        {/* 
          RENDERING CONDIZIONALE — mostra testo diverso in base a isCoperto.
          Sintassi:  condizione ? "se vero" : "se falso"
          È l'equivalente di:
            <span th:text="${campo.isCoperto} ? 'Coperto' : 'Scoperto'">
        */}
        <div className="flex items-center gap-2">
          <span className="text-lg">{campo.isCoperto ? "🏠" : "☀️"}</span>
          <span
            className={`text-sm font-medium px-2 py-1 rounded-full ${
              campo.isCoperto
                ? "bg-blue-100 text-blue-700"    // sfondo blu se coperto
                : "bg-amber-100 text-amber-700"  // sfondo ambra se scoperto
            }`}
          >
            {campo.isCoperto ? "Coperto" : "Scoperto"}
          </span>
        </div>
      </div>
    </div>
  );
}

// ============================================================
// FUNZIONI HELPER
// ============================================================
/**
 * Restituisce le classi CSS Tailwind per il colore dell'header
 * in base al tipo di superficie del campo.
 * 
 * In Java avresti usato un switch/case — in TypeScript funziona uguale.
 */
function getColoreSuperficie(tipo: string): string {
  switch (tipo) {
    case "Terra Rossa":
      return "bg-orange-600";      // arancione come la terra rossa
    case "Erba Sintetica":
      return "bg-green-600";       // verde come l'erba
    case "Erba Naturale":
      return "bg-emerald-600";     // verde più ricco per erba naturale
    case "Cemento":
      return "bg-slate-600";       // grigio come il cemento
    default:
      return "bg-emerald-700";     // colore di default
  }
}
