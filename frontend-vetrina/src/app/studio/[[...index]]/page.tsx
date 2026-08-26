"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import { NextStudio } from "next-sanity/studio";
import config from "../../../../sanity.config";
import { isSanityConfigured } from "@/sanity/client";

export default function StudioPage() {
  const [configured, setConfigured] = useState<boolean | null>(null);
  const [copied, setCopied] = useState(false);

  useEffect(() => {
    setConfigured(isSanityConfigured());
  }, []);

  // In fase di caricamento iniziale client-side
  if (configured === null) {
    return (
      <div className="min-h-screen bg-slate-950 flex items-center justify-center text-slate-400">
        <div className="flex items-center gap-3">
          <div className="w-5 h-5 border-2 border-emerald-500 border-t-transparent rounded-full animate-spin" />
          <span className="text-sm">Inizializzazione Sanity Studio...</span>
        </div>
      </div>
    );
  }

  // Se Sanity non è configurato o il Project ID manca/è placeholder
  if (!configured) {
    const envSnippet = `NEXT_PUBLIC_SANITY_PROJECT_ID="il_tuo_project_id"\nNEXT_PUBLIC_SANITY_DATASET="production"`;

    const handleCopy = () => {
      navigator.clipboard.writeText(envSnippet);
      setCopied(true);
      setTimeout(() => setCopied(false), 2500);
    };

    return (
      <div className="min-h-screen bg-[#0d1117] text-slate-100 flex flex-col items-center justify-center p-4 sm:p-8">
        <div className="max-w-2xl w-full bg-[#161b22] border border-slate-700/60 rounded-2xl p-6 sm:p-10 shadow-2xl space-y-8">
          {/* Header */}
          <div className="flex items-start gap-4">
            <div className="w-12 h-12 rounded-xl bg-amber-500/10 border border-amber-500/30 flex items-center justify-center text-amber-400 shrink-0">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
            </div>
            <div>
              <h1 className="text-2xl font-bold text-white tracking-tight">Sanity Studio — Configurazione Mancante</h1>
              <p className="text-sm text-slate-400 mt-1">
                Il CMS non è collegato ad alcun progetto Sanity.io.
              </p>
            </div>
          </div>

          {/* Alert Box */}
          <div className="bg-amber-500/10 border border-amber-500/20 rounded-xl p-4 text-xs text-amber-200/90 leading-relaxed">
            Per abilitare la gestione dei risultati gare, campionati e contenuti via CMS, è necessario configurare le chiavi di accesso nel file <strong className="text-amber-100 font-mono">.env.local</strong> nella cartella principale <strong className="text-amber-100 font-mono">frontend-vetrina/</strong>.
          </div>

          {/* Snippet Card */}
          <div className="space-y-2">
            <div className="flex items-center justify-between text-xs text-slate-400 px-1">
              <span className="font-semibold">Contenuto richiesto in <code className="text-slate-300">frontend-vetrina/.env.local</code>:</span>
              <button
                onClick={handleCopy}
                className="text-xs px-2.5 py-1 rounded bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 transition-colors flex items-center gap-1.5 cursor-pointer"
              >
                {copied ? (
                  <>
                    <svg className="w-3.5 h-3.5 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                    </svg>
                    <span>Copiato!</span>
                  </>
                ) : (
                  <>
                    <svg className="w-3.5 h-3.5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                    </svg>
                    <span>Copia snippet</span>
                  </>
                )}
              </button>
            </div>
            <pre className="bg-[#0b0e14] border border-slate-800 rounded-xl p-4 text-xs font-mono text-emerald-400 overflow-x-auto leading-relaxed">
              <code>{envSnippet}</code>
            </pre>
          </div>

          {/* Step by Step */}
          <div className="space-y-3">
            <h2 className="text-sm font-semibold text-slate-200">Guida rapida in 4 passi:</h2>
            <ol className="space-y-2.5 text-xs text-slate-300">
              <li className="flex items-start gap-2.5">
                <span className="w-5 h-5 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center text-slate-400 font-bold shrink-0 text-[10px]">1</span>
                <span>Accedi alla dashboard di <a href="https://www.sanity.io/manage" target="_blank" rel="noreferrer" className="text-emerald-400 hover:underline font-medium">Sanity.io/manage</a> e seleziona il tuo progetto.</span>
              </li>
              <li className="flex items-start gap-2.5">
                <span className="w-5 h-5 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center text-slate-400 font-bold shrink-0 text-[10px]">2</span>
                <span>Copia il tuo <strong className="text-white">Project ID</strong> (codice alfanumerico di 8 caratteri).</span>
              </li>
              <li className="flex items-start gap-2.5">
                <span className="w-5 h-5 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center text-slate-400 font-bold shrink-0 text-[10px]">3</span>
                <span>Nel tab <strong className="text-white">API → CORS Origins</strong> di Sanity, aggiungi <code className="bg-slate-800 px-1 py-0.5 rounded text-amber-300">http://localhost:3000</code> spuntando <em className="text-slate-400">Allow credentials</em>.</span>
              </li>
              <li className="flex items-start gap-2.5">
                <span className="w-5 h-5 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center text-slate-400 font-bold shrink-0 text-[10px]">4</span>
                <span>Incolla il file <code className="bg-slate-800 px-1 py-0.5 rounded text-white">.env.local</code> nella root di <code className="text-white">frontend-vetrina/</code> e riavvia il server (<code className="bg-slate-800 px-1 py-0.5 rounded text-white">npm run dev</code>).</span>
              </li>
            </ol>
          </div>

          {/* Actions */}
          <div className="pt-4 border-t border-slate-800 flex flex-col sm:flex-row items-center justify-between gap-3">
            <Link
              href="/"
              className="text-xs text-slate-400 hover:text-white transition-colors flex items-center gap-1.5"
            >
              ← Torna alla Home del Club
            </Link>
            <div className="flex items-center gap-3">
              <a
                href="https://www.sanity.io/manage"
                target="_blank"
                rel="noreferrer"
                className="px-4 py-2 bg-emerald-600 hover:bg-emerald-500 text-white font-medium text-xs rounded-lg transition-colors flex items-center gap-1.5"
              >
                Apri Sanity Manage ↗
              </a>
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Quando configurato correttamente, renderizza lo Studio di Sanity
  return <NextStudio config={config} />;
}
