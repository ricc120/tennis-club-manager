"use client";

import { useState } from "react";
import { tariffe, Tariffa } from "@/data/tariffe";

type Categoria = "tennis" | "padel" | "academy";

const tabs: { label: string; value: Categoria; colore: string }[] = [
  { label: "🎾 Tennis", value: "tennis", colore: "primary" },
  { label: "🏸 Padel", value: "padel", colore: "secondary" },
  { label: "🏆 Academy", value: "academy", colore: "accent" },
];

export default function Tariffe() {
  const [categoriaAttiva, setCategoriaAttiva] = useState<Categoria>("tennis");

  const tariffeFiltrate = tariffe.filter((t) => t.categoria === categoriaAttiva);

  const coloreAttivo =
    categoriaAttiva === "tennis" ? "primary" : categoriaAttiva === "padel" ? "secondary" : "accent";

  return (
    <section id="tariffe" className="py-20 lg:py-28 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto mb-12">
          <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
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
                  ? tab.colore === "primary"
                    ? "bg-primary text-white shadow-lg shadow-primary/30"
                    : tab.colore === "secondary"
                    ? "bg-secondary-dark text-white shadow-lg shadow-secondary/30"
                    : "bg-accent text-white shadow-lg shadow-accent/30"
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
    ? colore === "primary"
      ? "border-primary"
      : colore === "secondary"
      ? "border-secondary"
      : "border-accent"
    : "border-gray-200";

  const badgeColor =
    colore === "primary"
      ? "bg-primary text-white"
      : colore === "secondary"
      ? "bg-secondary-dark text-white"
      : "bg-accent text-white";

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
            <span className="text-primary mt-0.5">✓</span>
            {det}
          </li>
        ))}
      </ul>
    </div>
  );
}
