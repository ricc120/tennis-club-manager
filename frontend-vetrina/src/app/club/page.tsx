"use client";

import { useState } from "react";
import Image from "next/image";
import Link from "next/link";
import { siteConfig } from "@/config/site";

export default function ClubPage() {
  const [activeModalImage, setActiveModalImage] = useState<{
    src: string;
    alt: string;
  } | null>(null);

  return (
    <>
      {/* Page Header */}
      <section className="pt-32 pb-16 lg:pt-40 lg:pb-20 bg-gradient-to-b from-dark to-dark/90">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-primary-light font-semibold tracking-widest uppercase text-sm mb-3">
            Dal {siteConfig.foundedYear}
          </p>
          <h1
            className="text-4xl lg:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "'Playfair Display', serif" }}
          >
            Il Club
          </h1>
          <p className="text-white/70 text-lg max-w-2xl mx-auto">
            Tradizione sportiva e passione per il tennis nel cuore della {siteConfig.region}.
          </p>
        </div>
      </section>

      {/* ============================================================
          CHI SIAMO — Le Origini e la Passione
          Contenuto hardcoded fornito dal cliente TC Carmignano.
          Layout: testo + immagine a due colonne, inverso su seconda riga.
          ============================================================ */}
      <section className="py-20 lg:py-28 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Header */}
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Le Origini e la Passione
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              La Nostra Storia
            </h2>
          </div>

          {/* Riga 1: Testo a sinistra — Immagine vialetto a destra */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 items-center mb-20">
            <div>
              <div className="space-y-5 text-gray-700 leading-relaxed text-base lg:text-lg">
                <p>
                  Tutto nasce negli anni &apos;90 dal sogno di due maestri nazionali,{" "}
                  <strong className="text-dark">Marco e Roberta Stefanini</strong>, uniti
                  dall&apos;amore per questo sport. Dopo anni di ricerche e ostacoli burocratici,
                  nel 2008 trovano il luogo perfetto nella bellissima zona panoramica de{" "}
                  <em>&ldquo;La Serra&rdquo;</em> (tra Poggio a Caiano e Carmignano).
                </p>
                <p>
                  Nel <strong className="text-dark">2009</strong> posano la prima pietra e nel{" "}
                  <strong className="text-dark">2011</strong> inaugurano i primi campi affiliandosi
                  alla FIT. La struttura è stata poi completata nel{" "}
                  <strong className="text-dark">2017</strong> con l&apos;aggiunta di bar, ristorante
                  e pizzeria.
                </p>
              </div>
            </div>
            <div className="relative rounded-2xl overflow-hidden shadow-2xl group">
              <Image
                src="/images/vialetto.jpg"
                alt="Vialetto di ingresso del Tennis Club Carmignano"
                width={800}
                height={600}
                className="w-full h-auto object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
          </div>

          {/* Riga 2: Immagine padel/clubhouse a sinistra — Testo a destra */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 items-center">
            <div className="relative rounded-2xl overflow-hidden shadow-2xl group order-2 lg:order-1">
              <Image
                src="/images/padel2_club_house.jpg"
                alt="Campi padel e club house del TC Carmignano"
                width={800}
                height={600}
                className="w-full h-auto object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
            <div className="order-1 lg:order-2">
              <div className="space-y-5 text-gray-700 leading-relaxed text-base lg:text-lg">
                <p>
                  Oggi il {siteConfig.clubName} vanta il{" "}
                  <strong className="text-dark">miglior staff tecnico della zona</strong>,
                  capace di formare giocatori di ogni livello. La scuola tennis conta circa{" "}
                  <strong className="text-primary">100 iscritti</strong> tra ragazzi e adulti.
                </p>
                <p>
                  L&apos;offerta si è ampliata introducendo campi da{" "}
                  <strong className="text-dark">Padel</strong>, centri estivi, atletica e tornei
                  che portano oltre{" "}
                  <strong className="text-primary">4.000 presenze l&apos;anno</strong>.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Staff — Griglia con maestri cliccabili */}
      <section className="py-20 lg:py-28 bg-light">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <p className="text-primary font-semibold tracking-widest uppercase text-sm mb-3">
              Il Team
            </p>
            <h2
              className="text-3xl lg:text-4xl font-bold text-dark mb-6"
              style={{ fontFamily: "'Playfair Display', serif" }}
            >
              I Nostri Professionisti
            </h2>
            <p className="text-gray-600 text-lg">
              Un team di maestri qualificati FIT, preparatori atletici e figure specializzate
              per accompagnarti nel tuo percorso sportivo.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-8">
            {siteConfig.staff.map((member) => (
              <div
                key={member.name}
                className="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 hover:-translate-y-1 cursor-pointer"
                onClick={() =>
                  setActiveModalImage({ src: member.photo, alt: member.name })
                }
              >
                <div className="relative h-64 overflow-hidden">
                  <Image
                    src={member.photo}
                    alt={member.name}
                    fill
                    className="object-cover group-hover:scale-105 transition-transform duration-500"
                    sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 20vw"
                  />
                  <div className="absolute inset-0 bg-black/30 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center">
                    <span className="bg-white/90 text-dark text-xs font-semibold px-3 py-1.5 rounded-full shadow-md">
                      Visualizza Foto
                    </span>
                  </div>
                </div>
                <div className="p-6 text-center">
                  <h3 className="text-lg font-bold text-dark">{member.name}</h3>
                  <p className="text-primary text-sm font-medium mt-1">{member.role}</p>
                </div>
              </div>
            ))}

            {/* Card "...e molti altri" con Logo Accademia */}
            <Link
              href="/accademia"
              className="group flex flex-col items-center justify-center bg-gradient-to-br from-primary/5 to-primary/10 border-2 border-dashed border-primary/30 rounded-2xl p-6 hover:border-primary hover:shadow-xl transition-all duration-300 hover:-translate-y-1 text-center min-h-[320px]"
            >
              <div className="relative w-20 h-20 mb-4 transition-transform group-hover:scale-110 duration-300">
                <Image
                  src="/images/logo_accademia-removebg-preview.png"
                  alt="Logo Accademia"
                  fill
                  className="object-contain"
                />
              </div>
              <h3
                className="text-xl font-bold text-dark mb-2"
                style={{ fontFamily: "'Playfair Display', serif" }}
              >
                ...e molti altri
              </h3>
              <p className="text-gray-500 text-sm mb-6 leading-relaxed">
                Il nostro staff comprende numerosi professionisti pronti
                a seguirti nel tuo percorso.
              </p>
              <span className="inline-flex items-center gap-2 bg-primary text-white px-5 py-2.5 rounded-full font-semibold text-sm transition-all group-hover:bg-primary-light group-hover:shadow-lg group-hover:shadow-primary/30">
                Scopri l&apos;Accademia →
              </span>
            </Link>
          </div>
        </div>
      </section>

      {/* Modal / Lightbox per visualizzare la scheda/foto del maestro */}
      {activeModalImage && (
        <div
          className="fixed inset-0 z-50 bg-black/85 backdrop-blur-md flex items-center justify-center p-4 sm:p-6 animate-fade-in cursor-pointer"
          onClick={() => setActiveModalImage(null)}
        >
          <div className="relative max-h-[88vh] max-w-[92vw] sm:max-w-xl md:max-w-2xl flex items-center justify-center">
            <img
              src={activeModalImage.src}
              alt={activeModalImage.alt}
              className="max-h-[88vh] w-auto max-w-full object-contain rounded-2xl shadow-2xl border border-white/10"
            />
          </div>
        </div>
      )}
    </>
  );
}
