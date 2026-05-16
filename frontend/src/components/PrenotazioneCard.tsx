/**
 * PrenotazioneCard — STEP 9: Refactoring con useMutation.
 *
 * PRIMA (Step 6):
 *   await cancellaPrenotazione(id);  // fetch manuale
 *   router.refresh();                // ricarica TUTTA la pagina
 *
 * ADESSO (Step 9):
 *   elimina(id);                     // useMutation gestisce il DELETE
 *   → onSuccess: invalidateQueries   // la card scompare automaticamente
 *
 * Il vantaggio più visibile: dopo l'eliminazione, la card scompare
 * dalla lista SENZA ricaricare l'intera pagina. React Query rifà
 * solo il GET delle prenotazioni e React aggiorna il DOM.
 */
"use client";

import { useAuth } from "@/hooks/AuthContext";
import { useCancellaPrenotazione } from "@/hooks/usePrenotazioni";
import { Prenotazione } from "@/types";

interface PrenotazioneCardProps {
  prenotazione: Prenotazione;
}

export default function PrenotazioneCard({ prenotazione }: PrenotazioneCardProps) {
  const { utente } = useAuth();

  // STEP 9: useMutation per il DELETE
  const { mutate: elimina, isPending: isDeleting, error } = useCancellaPrenotazione();

  const handleElimina = () => {
    const conferma = window.confirm(
      `Vuoi eliminare la prenotazione del ${formattaData(prenotazione.data)} alle ${prenotazione.oraInizio}?`
    );

    if (!conferma) return;

    // mutate() esegue il DELETE.
    // onSuccess (definito nel hook) invalida la cache ["prenotazioni"].
    // React Query rifà il GET → la lista si aggiorna → questa card scompare.
    elimina(prenotazione.id);
  };

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

      {/* FOOTER — Bottone Elimina */}
      {puoEliminare && (
        <div className="px-6 py-3 bg-gray-50 border-t border-gray-100">
          {error && (
            <p className="text-red-600 text-xs mb-2">
              ⚠️ {error instanceof Error ? error.message : "Errore nell'eliminazione"}
            </p>
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
