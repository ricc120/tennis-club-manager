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
    <div className="bg-[#FAF9F5] text-[#182219]">
      {/* 1. Page Header with Heritage Green Banner */}
      <section className="pt-36 pb-20 lg:pt-44 lg:pb-24 bg-[#1B3C1F] text-white relative overflow-hidden">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10">
          <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/10 text-[#F7E4D3] border border-white/20 text-xs font-semibold uppercase tracking-widest mb-4">
            Dal {siteConfig.foundedYear} • {siteConfig.location}
          </span>
          <h1
            className="text-4xl sm:text-6xl font-bold text-white mb-6"
            style={{ fontFamily: "var(--font-serif)" }}
          >
            La Nostra Storia e Il Circolo
          </h1>
          <p className="text-[#DEE9DF] text-lg sm:text-xl max-w-2xl mx-auto font-light leading-relaxed">
            Una passione nata sui campi toscani, cresciuta con dedizione e diventata una casa per tutti gli amanti del tennis e del padel.
          </p>
        </div>
      </section>

      {/* 2. CHI SIAMO — Narrazione Editoriale */}
      <section className="py-20 lg:py-28 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center max-w-3xl mx-auto mb-20">
          <span className="status-pill mb-3">
            Origini &amp; Passione
          </span>
          <h2
            className="text-3xl sm:text-4xl lg:text-5xl font-bold text-[#1B3C1F] tracking-tight mb-4"
            style={{ fontFamily: "var(--font-serif)" }}
          >
            Le Radici del Tennis Club Carmignano
          </h2>
          <p className="text-[#5A695C] text-base">
            Dalla prima pietra posata nel 2009 fino all&apos;attuale struttura d&apos;eccellenza.
          </p>
        </div>

        {/* Row 1: Story & Vialetto */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 items-center mb-20">
          <div className="space-y-6 text-[#5A695C] leading-relaxed text-base lg:text-lg">
            <p>
              Tutto nasce negli anni &apos;90 dal sogno e dalla dedizione di due maestri nazionali,{" "}
              <strong className="text-[#182219] font-semibold">Marco e Roberta Stefanini</strong>, uniti
              dall&apos;amore profondo per il tennis. Dopo anni di ricerche e determinazione,
              nel 2008 trovano il luogo ideale nella splendida area panoramica de{" "}
              <em className="text-[#C05621] font-serif">&ldquo;La Serra&rdquo;</em>, situata fra Carmignano e Poggio a Caiano.
            </p>
            <p>
              Nel <strong className="text-[#182219] font-semibold">2009</strong> viene posata la prima pietra e nel{" "}
              <strong className="text-[#1B3C1F] font-semibold">2011 </strong>si inaugurano i primi campi con l&apos;affiliazione
              ufficiale alla FITP. Nel <strong className="text-[#182219] font-semibold">2017</strong> il circolo completa la propria offerta integrando la club house con bar, ristorante e terrazza panoramica.
            </p>
          </div>

          <div className="relative rounded-3xl overflow-hidden shadow-xl border-4 border-white bg-white group">
            <div className="relative h-80 sm:h-96 w-full">
              <Image
                src="/images/vialetto.jpg"
                alt="Vialetto d'ingresso del Tennis Club Carmignano"
                fill
                sizes="(max-width: 1024px) 100vw, 50vw"
                className="object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
          </div>
        </div>

        {/* Row 2: Present Day & Padel */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 items-center">
          <div className="relative rounded-3xl overflow-hidden shadow-xl border-4 border-white bg-white group order-2 lg:order-1">
            <div className="relative h-80 sm:h-96 w-full">
              <Image
                src="/images/padel2_club_house.jpg"
                alt="Campi da padel panoramici e club house"
                fill
                sizes="(max-width: 1024px) 100vw, 50vw"
                className="object-cover group-hover:scale-105 transition-transform duration-700"
              />
            </div>
          </div>

          <div className="space-y-6 text-[#5A695C] leading-relaxed text-base lg:text-lg order-1 lg:order-2">
            <p>
              Oggi il {siteConfig.clubName} è fiero di ospitare uno{" "}
              <strong className="text-[#182219] font-semibold">staff tecnico tra i più titolati della regione</strong>,
              in grado di accompagnare allievi di qualsiasi età: dai primi scambi con la racchetta fino al professionismo internazionale.
            </p>
            <p>
              La scuola tennis accoglie stabilmente oltre{" "}
              <strong className="text-[#1B3C1F] font-semibold">100 iscritti</strong>. L&apos;offerta è completata da campi da{" "}
              <strong className="text-[#C05621] font-semibold">Padel panoramici</strong>, preparazione atletica qualificata e un calendario di manifestazioni che porta oltre{" "}
              <strong className="text-[#1B3C1F] font-semibold">4.000 presenze l&apos;anno</strong> sui nostri campi.
            </p>
          </div>
        </div>
      </section>

      {/* 3. STAFF & MAESTRI — Card d'Élite */}
      <section className="py-20 lg:py-28 bg-[#F3EFE6] border-t border-[#E5E0D4]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <span className="status-pill mb-3">
              Direzione Tecnica &amp; Preparatori
            </span>
            <h2
              className="text-3xl sm:text-4xl font-bold text-[#1B3C1F] tracking-tight mb-4"
              style={{ fontFamily: "var(--font-serif)" }}
            >
              I Nostri Professionisti
            </h2>
            <p className="text-[#5A695C] text-base">
              Maestri Nazionali FITP, istruttori qualificati e preparatori dedicati ad ogni fase della crescita sportiva.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-6">
            {siteConfig.staff.map((member) => (
              <div
                key={member.name}
                className="editorial-card rounded-2xl overflow-hidden cursor-pointer flex flex-col justify-between group"
                onClick={() => setActiveModalImage({ src: member.photo, alt: member.name })}
              >
                <div className="relative h-72 w-full overflow-hidden bg-gray-100">
                  <Image
                    src={member.photo}
                    alt={member.name}
                    fill
                    sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 20vw"
                    className="object-cover group-hover:scale-105 transition-transform duration-500"
                  />
                  <div className="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                    <span className="bg-white/95 text-gray-900 text-xs font-semibold px-3 py-1.5 rounded-full shadow-md">
                      Visualizza Foto
                    </span>
                  </div>
                </div>

                <div className="p-5 text-center">
                  <h3 className="text-base font-bold text-[#182219] group-hover:text-[#1B3C1F] transition-colors">
                    {member.name}
                  </h3>
                  <p className="text-xs text-[#C05621] font-semibold mt-1 uppercase tracking-wider">
                    {member.role}
                  </p>
                </div>
              </div>
            ))}

            {/* Academy Link Card */}
            <Link
              href="/accademia"
              className="editorial-card rounded-2xl p-6 flex flex-col items-center justify-center text-center group border-dashed hover:border-[#1B3C1F] bg-white min-h-[300px]"
            >
              <div className="relative w-16 h-16 mb-4 transition-transform group-hover:scale-110 duration-300">
                <Image
                  src="/images/logo_accademia-removebg-preview.png"
                  alt="Logo Accademia Stefanini"
                  fill
                  className="object-contain"
                />
              </div>
              <h3
                className="text-lg font-bold text-[#1B3C1F] mb-2"
                style={{ fontFamily: "var(--font-serif)" }}
              >
                ...e molti altri
              </h3>
              <p className="text-[#5A695C] text-xs mb-5 leading-relaxed">
                Scopri tutti i maestri e i programmi formativi dell&apos;accademia.
              </p>
              <span className="inline-flex items-center gap-1.5 text-xs font-semibold text-[#C05621] group-hover:underline">
                Vai all&apos;Accademia →
              </span>
            </Link>
          </div>
        </div>
      </section>

      {/* Lightbox Modal */}
      {activeModalImage && (
        <div
          className="fixed inset-0 z-50 bg-black/85 backdrop-blur-sm flex items-center justify-center p-4 cursor-pointer"
          onClick={() => setActiveModalImage(null)}
        >
          <div className="relative max-h-[85vh] max-w-[90vw] sm:max-w-xl md:max-w-2xl flex items-center justify-center">
            <img
              src={activeModalImage.src}
              alt={activeModalImage.alt}
              className="max-h-[85vh] w-auto max-w-full object-contain rounded-2xl shadow-2xl border-2 border-white"
            />
          </div>
        </div>
      )}
    </div>
  );
}
