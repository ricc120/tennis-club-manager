"use client";

import { useState } from "react";
import { tariffe, Tariffa } from "@/data/tariffe";

type Categoria = "tennis" | "padel" | "academy";

const tabs: { label: string; value: Categoria; colore: string }[] = [
  { label: "🎾 Tennis", value: "tennis", colore: "tennis" },
  { label: "🏸 Padel", value: "padel", colore: "padel" },
  { label: "🏆 Academy", value: "academy", colore: "academy" },
];

export default function Tariffe() {
  const [categoriaAttiva, setCategoriaAttiva] = useState<Categoria>("tennis");

  const tariffeFiltrate = tariffe.filter((t) => t.categoria === categoriaAttiva);

  const coloreAttivo =
    categoriaAttiva === "tennis" ? "tennis" : categoriaAttiva === "padel" ? "padel" : "academy";

  return (
    <section id="tariffe" className="py-20 lg:py-28 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto mb-12">
          <p className={`text-${coloreAttivo} font-semibold tracking-widest uppercase text-sm mb-3`}>
            Prezzi Trasparenti
          </p>
          <h2 className="text-4xl lg:text-5xl font-bold text-dark mb-6">
            Le Nostre Tariffe
          </h2>
          <p className="text-gray-600 text-lg">
            Scegli il piano più adatto a te. Tutti i prezzi includono accesso
            a spogliatoi e docce.
          </p>
        </div>

        {/* Tabs */}
        <div className="flex justify-center gap-2 mb-12">
          {tabs.map((tab) => (
            <button
              key={tab.value}
              onClick={() => setCategoriaAttiva(tab.value)}
              className={`px-6 py-3 rounded-full text-sm font-semibold transition-all ${
                categoriaAttiva === tab.value
                  ? tab.colore === "tennis"
                    ? "bg-tennis text-white shadow-lg shadow-tennis/30"
                    : tab.colore === "padel"
                    ? "bg-padel-dark text-white shadow-lg shadow-padel/30"
                    : "bg-academy text-white shadow-lg shadow-academy/30"
                  : "bg-light text-gray-600 hover:bg-light-darker"
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 max-w-5xl mx-auto">
          {tariffeFiltrate.map((tariffa) => (
            <TariffaCard key={tariffa.nome} tariffa={tariffa} colore={coloreAttivo} />
          ))}
        </div>
      </div>
    </section>
  );
}

function TariffaCard({ tariffa, colore }: { tariffa: Tariffa; colore: string }) {
  const borderColor = tariffa.evidenziato
    ? colore === "tennis"
      ? "border-tennis"
      : colore === "padel"
      ? "border-padel"
      : "border-academy"
    : "border-gray-200";

  const badgeColor =
    colore === "tennis"
      ? "bg-tennis text-white"
      : colore === "padel"
      ? "bg-padel-dark text-white"
      : "bg-academy text-white";

  return (
    <div
      className={`relative rounded-2xl border-2 ${borderColor} bg-white p-8 hover:shadow-xl transition-all duration-300 hover:-translate-y-1 ${
        tariffa.evidenziato ? "shadow-lg" : "shadow-sm"
      }`}
    >
      {tariffa.evidenziato && (
        <div className={`absolute -top-3 left-1/2 -translate-x-1/2 ${badgeColor} px-4 py-1 rounded-full text-xs font-bold tracking-wide`}>
          CONSIGLIATO
        </div>
      )}

      <h3 className="text-lg font-bold text-dark mb-4">{tariffa.nome}</h3>

      <div className="flex items-baseline gap-1 mb-6">
        <span className="text-4xl font-bold text-dark">{tariffa.prezzo}</span>
        <span className="text-gray-500 text-sm">{tariffa.periodo}</span>
      </div>

      <ul className="space-y-3">
        {tariffa.dettagli.map((det) => (
          <li key={det} className="flex items-start gap-3 text-gray-600 text-sm">
            <span className={`text-${colore} mt-0.5`}>✓</span>
            {det}
          </li>
        ))}
      </ul>
    </div>
  );
}
