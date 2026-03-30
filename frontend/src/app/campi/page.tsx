/**
 * Pagina /campi — Mostra la griglia di tutti i campi da tennis.
 * 
 * FILE ROUTING: questo file si trova in app/campi/page.tsx
 * → Next.js lo renderizza automaticamente su http://localhost:3000/campi
 * 
 * CONCETTO CHIAVE: .map() per renderizzare liste.
 * In Thymeleaf iteravi con:    <div th:each="campo : ${campi}">
 * In React si usa .map():      {campi.map(campo => <CampoCard ... />)}
 * 
 * .map() prende un array e restituisce un NUOVO array trasformato:
 *   [campo1, campo2, campo3].map(campo => <CampoCard campo={campo} />)
 *   → [<CampoCard campo={campo1}/>, <CampoCard campo={campo2}/>, <CampoCard campo={campo3}/>]
 * 
 * PROP "key": React la richiede quando fai .map(). È un identificatore univoco
 * che aiuta React a capire quale elemento è cambiato quando la lista si aggiorna.
 * Usiamo campo.id perché è univoco per ogni campo.
 */

import { mockCampi } from "@/lib/mockData";
import CampoCard from "@/components/CampoCard";

export default function CampiPage() {
  // Per ora usiamo i dati mock — in futuro verranno dall'API
  const campi = mockCampi;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      {/* HEADER della pagina */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">
          🏟️ I Nostri Campi
        </h1>
        <p className="mt-2 text-gray-600">
          Visualizza tutti i campi del tennis club e il loro stato attuale.
        </p>
      </div>

      {/*
        GRIGLIA RESPONSIVA:
        - grid = layout a griglia CSS
        - grid-cols-1 = 1 colonna su mobile (< 640px)
        - md:grid-cols-2 = 2 colonne su tablet (768px+)
        - lg:grid-cols-3 = 3 colonne su desktop (1024px+)
        - gap-6 = spazio di 1.5rem tra le card
      */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/*
          .map() itera sull'array e crea un CampoCard per ogni elemento.
          "key={campo.id}" è OBBLIGATORIO — senza, React dà un warning.
        */}
        {campi.map((campo) => (
          <CampoCard key={campo.id} campo={campo} />
        ))}
      </div>

      {/* Riepilogo */}
      <div className="mt-8 p-4 bg-emerald-50 rounded-lg border border-emerald-200">
        <p className="text-sm text-emerald-800">
          📊 Totale campi: <strong>{campi.length}</strong> —{" "}
          Coperti: <strong>{campi.filter((c) => c.isCoperto).length}</strong> —{" "}
          Scoperti: <strong>{campi.filter((c) => !c.isCoperto).length}</strong>
        </p>
      </div>
    </div>
  );
}
