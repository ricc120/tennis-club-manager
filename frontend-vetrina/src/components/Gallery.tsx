"use client";

import { useState } from "react";
import { galleria, FotoGallery } from "@/data/gallery";

type Filtro = "tutti" | "tennis" | "padel" | "academy" | "club";

const filtri: { label: string; value: Filtro }[] = [
  { label: "Tutti", value: "tutti" },
  { label: "Tennis", value: "tennis" },
  { label: "Padel", value: "padel" },
  { label: "Academy", value: "academy" },
  { label: "Club", value: "club" },
];

export default function Gallery() {
  const [filtro, setFiltro] = useState<Filtro>("tutti");

  const fotoFiltrate =
    filtro === "tutti" ? galleria : galleria.filter((f) => f.categoria === filtro);

  return (
    <section id="gallery" className="py-20 lg:py-28 bg-light">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto mb-12">
          <p className="text-tennis font-semibold tracking-widest uppercase text-sm mb-3">
            Il Club in Immagini
          </p>
          <h2 className="text-4xl lg:text-5xl font-bold text-dark mb-6">Gallery</h2>
        </div>

        {/* Filtri */}
        <div className="flex flex-wrap justify-center gap-2 mb-10">
          {filtri.map((f) => (
            <button
              key={f.value}
              onClick={() => setFiltro(f.value)}
              className={`px-5 py-2 rounded-full text-sm font-medium transition-all ${
                filtro === f.value
                  ? "bg-tennis text-white shadow-md"
                  : "bg-white text-gray-600 hover:bg-gray-100 border border-gray-200"
              }`}
            >
              {f.label}
            </button>
          ))}
        </div>

        {/* Grid */}
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {fotoFiltrate.map((foto, index) => (
            <GalleryItem key={foto.alt} foto={foto} index={index} />
          ))}
        </div>
      </div>
    </section>
  );
}

function GalleryItem({ foto, index }: { foto: FotoGallery; index: number }) {
  const isLarge = index === 0 || index === 5;

  return (
    <div
      className={`relative group overflow-hidden rounded-xl ${
        isLarge ? "md:col-span-2 md:row-span-2" : ""
      }`}
    >
      <img
        src={foto.src}
        alt={foto.alt}
        className="w-full h-full object-cover aspect-[4/3] group-hover:scale-110 transition-transform duration-500"
      />
      {/* Hover overlay */}
      <div className="absolute inset-0 bg-dark/0 group-hover:bg-dark/50 transition-all duration-300 flex items-end">
        <p className="text-white font-medium p-4 translate-y-full group-hover:translate-y-0 transition-transform duration-300">
          {foto.alt}
        </p>
      </div>
    </div>
  );
}
