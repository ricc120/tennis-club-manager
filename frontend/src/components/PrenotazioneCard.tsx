/**
 * PrenotazioneCard — Card con dettagli e bottone Elimina.
 * 
 * STEP 6: Aggiunto bottone "Elimina" con conferma.
 * 
 * "use client" è necessario perché ora questo componente:
 * - Gestisce click (onClick su Elimina)
 * - Usa useState per lo stato di loading
 * - Usa useRouter per aggiornare la pagina dopo l'eliminazione
 * 
 * CONCETTO: window.confirm()
 * Mostra un dialog di conferma nativo del browser.
 * Se l'utente clicca "OK" → ritorna true → procede con l'eliminazione.
 * Se l'utente clicca "Annulla" → ritorna false → non fa nulla.
 */
"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/hooks/AuthContext";
import { Prenotazione } from "@/types";
import { cancellaPrenotazione } from "@/services/prenotazioniService";

interface PrenotazioneCardProps {
  prenotazione: Prenotazione;
}

export default function PrenotazioneCard({ prenotazione }: PrenotazioneCardProps) {
  const [isDeleting, setIsDeleting] = useState(false);
  const [errore, setErrore] = useState("");
  const router = useRouter();
  const { utente } = useAuth();

  /**
   * Gestisce il click su "Elimina".
   * 
   * 1. Mostra un dialog di conferma
   * 2. Se confermato, chiama DELETE /api/prenotazioni/{id}
   * 3. Se successo, router.refresh() aggiorna la lista
   */
  const handleElimina = async () => {
    // Chiede conferma prima di eliminare
    const conferma = window.confirm(
      `Vuoi eliminare la prenotazione del ${formattaData(prenotazione.data)} alle ${prenotazione.oraInizio}?`
    );

    if (!conferma) return;

    setIsDeleting(true);
    setErrore("");

    try {
      await cancellaPrenotazione(prenotazione.id);
      // Dopo l'eliminazione, aggiorna la pagina per ricaricare la lista
      router.refresh();
    } catch (error) {
      setErrore(error instanceof Error ? error.message : "Errore nell'eliminazione");
    } finally {
      setIsDeleting(false);
    }
  };

  // Mostra il bottone elimina solo se l'utente è loggato e è il proprietario o admin
  const puoEliminare = utente && (
    utente.id === prenotazione.socio.id || utente.ruolo === "ADMIN"
  );

  return (
    <div className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden hover:shadow-lg transition-shadow duration-300">
      {/* HEADER */}
      <div className="bg-emerald-700 px-6 py-3 flex items-center justify-between">
        <span className="text-white font-semibold">
          📅 {formattaData(prenotazione.data)}
        </span>
        <span className="text-white-200 font-mono text-sm">
          🕐 {prenotazione.oraInizio}
        </span>
      </div>

      {/* CORPO */}
      <div className="px-6 py-5 space-y-3">
        <div className="flex items-center gap-2 text-gray-700">
          <span className="text-lg">🏟️</span>
          <div>
            <p className="text-sm text-gray-500">Campo</p>
            <p className="font-semibold">{prenotazione.campo.nome}</p>
          </div>
        </div>

        <div className="flex items-center gap-2 text-gray-700">
          <span className="text-lg">🎾</span>
          <div>
            <p className="text-sm text-gray-500">Superficie</p>
            <p className="font-medium text-sm">{prenotazione.campo.tipoSuperficie}</p>
          </div>
        </div>

        <div className="flex items-center gap-2 text-gray-700">
          <span className="text-lg">👤</span>
          <div>
            <p className="text-sm text-gray-500">Prenotato da</p>
            <p className="font-semibold">
              {prenotazione.socio.nome} {prenotazione.socio.cognome}
            </p>
          </div>
        </div>
      </div>

      {/* FOOTER — Bottone Elimina (visibile solo al proprietario o admin) */}
      {puoEliminare && (
        <div className="px-6 py-3 bg-gray-50 border-t border-gray-100">
          {errore && (
            <p className="text-red-600 text-xs mb-2">⚠️ {errore}</p>
          )}
          <button
            onClick={handleElimina}
            disabled={isDeleting}
            className="w-full py-2 text-sm font-medium text-red-600 bg-red-50 rounded-lg hover:bg-red-100 transition-colors disabled:opacity-50"
          >
            {isDeleting ? "Eliminazione..." : "🗑️ Elimina prenotazione"}
          </button>
        </div>
      )}
    </div>
  );
}

function formattaData(dataISO: string): string {
  const data = new Date(dataISO);
  return data.toLocaleDateString("it-IT", {
    day: "numeric",
    month: "long",
    year: "numeric",
  });
}
