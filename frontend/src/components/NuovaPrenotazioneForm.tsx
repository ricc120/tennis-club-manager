/**
 * NuovaPrenotazioneForm — STEP 9: Refactoring con useMutation.
 *
 * PRIMA (Step 6):
 *   await creaPrenotazione(dati);   // fetch manuale
 *   router.refresh();               // ricarica TUTTA la pagina
 *
 * ADESSO (Step 9):
 *   mutate(dati);                   // useMutation gestisce il POST
 *   → onSuccess: invalidateQueries  // aggiorna SOLO la lista
 *
 * CONCETTO: useMutation vs fetch manuale
 *
 * useMutation offre:
 * - isPending: true durante il POST (sostituisce useState isLoading)
 * - isError/error: gestione errori automatica
 * - onSuccess: callback per invalidare la cache
 * - reset(): pulisce lo stato di errore
 *
 * Il form è più semplice: meno useState, meno try/catch, meno codice.
 */
"use client";

import { useState } from "react";
import { useAuth } from "@/hooks/AuthContext";
import { useCreaPrenotazione } from "@/hooks/usePrenotazioni";
import { Campo } from "@/types";

interface Props {
  campi: Campo[];
}

export default function NuovaPrenotazioneForm({ campi }: Props) {
  const [idCampo, setIdCampo] = useState<number | "">("");
  const [data, setData] = useState("");
  const [oraInizio, setOraInizio] = useState("");
  const [successo, setSuccesso] = useState("");

  const { utente } = useAuth();

  // STEP 9: useMutation sostituisce il try/catch manuale
  const { mutate: creaPrenotazione, isPending, error, reset } = useCreaPrenotazione();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSuccesso("");
    reset(); // pulisce eventuali errori precedenti della mutation

    if (!utente) return;
    if (idCampo === "" || !data || !oraInizio) return;

    // mutate() esegue il POST e gestisce tutto automaticamente:
    // - isPending diventa true durante il fetch
    // - onSuccess (definito nel hook) invalida la cache
    // - Se il POST fallisce, error viene popolato
    creaPrenotazione(
      {
        data: data,
        oraInizio: oraInizio,
        idCampo: idCampo as number,
        idSocio: utente.id,
      },
      {
        // Callback locale: si attiva SOLO per questa chiamata
        onSuccess: () => {
          setSuccesso("✅ Prenotazione creata con successo!");
          // Reset del form
          setIdCampo("");
          setData("");
          setOraInizio("");
        },
      }
    );
  };

  if (!utente) {
    return (
      <div className="p-4 bg-amber-50 border border-amber-200 rounded-lg text-amber-700 text-sm">
        🔒 Effettua il login per prenotare un campo.
      </div>
    );
  }

  return (
    <div className="bg-white rounded-xl shadow-lg border border-gray-100 p-6">
      <h2 className="text-lg font-semibold text-gray-800 mb-4">
        ➕ Nuova Prenotazione
      </h2>

      {/* Errore dalla mutation */}
      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
          ⚠️ {error instanceof Error ? error.message : "Errore nella creazione"}
        </div>
      )}
      {successo && (
        <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
          {successo}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        {/* SELECT Campo */}
        <div>
          <label htmlFor="campo" className="block text-sm font-medium text-gray-700 mb-1">
            Campo
          </label>
          <select
            id="campo"
            value={idCampo}
            onChange={(e) => setIdCampo(e.target.value ? Number(e.target.value) : "")}
            required
            className="w-full text-black px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none bg-white"
          >
            <option value="">Seleziona un campo...</option>
            {campi.map((campo) => (
              <option key={campo.id} value={campo.id}>
                {campo.nome} — {campo.tipoSuperficie} {campo.isCoperto ? "(coperto)" : "(scoperto)"}
              </option>
            ))}
          </select>
        </div>

        {/* DATE picker */}
        <div>
          <label htmlFor="data" className="block text-sm font-medium text-gray-700 mb-1">
            Data
          </label>
          <input
            id="data"
            type="date"
            value={data}
            onChange={(e) => setData(e.target.value)}
            min={new Date().toISOString().split("T")[0]}
            required
            className="w-full text-black px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none"
          />
        </div>

        {/* TIME picker */}
        <div>
          <label htmlFor="ora" className="block text-sm font-medium text-gray-700 mb-1">
            Ora di inizio
          </label>
          <input
            id="ora"
            type="time"
            value={oraInizio}
            onChange={(e) => setOraInizio(e.target.value)}
            min="08:00"
            max="22:00"
            step="3600"
            required
            className="w-full text-black px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none"
          />
        </div>

        {/* Riepilogo utente */}
        <div className="text-xs text-gray-500 bg-gray-50 rounded-lg px-3 py-2">
          Prenotazione a nome di: <strong>{utente.nome} {utente.cognome}</strong> ({utente.ruolo})
        </div>

        {/* SUBMIT — isPending sostituisce il vecchio useState isLoading */}
        <button
          type="submit"
          disabled={isPending}
          className="w-full py-3 bg-emerald-700 text-white font-semibold rounded-lg hover:bg-emerald-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isPending ? "Creazione in corso..." : "Prenota Campo"}
        </button>
      </form>
    </div>
  );
}
