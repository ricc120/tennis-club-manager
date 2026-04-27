/**
 * NuovaPrenotazioneForm — Form per creare una nuova prenotazione.
 * 
 * CONCETTO: Form con campi multipli
 * 
 * Il form di login aveva solo 2 campi (email, password).
 * Questo form ha 3 campi:
 *   - Campo (select dropdown) → simile a <select th:each="campo : ${campi}"> in Thymeleaf
 *   - Data  (date picker)     → <input type="date">
 *   - Ora   (time picker)     → <input type="time">
 * 
 * CONCETTO: Props
 * 
 * Questo componente riceve "campi" come prop (parametro):
 *   <NuovaPrenotazioneForm campi={listaCampi} />
 * 
 * In Java sarebbe come passare un parametro al costruttore:
 *   new NuovaPrenotazioneForm(listaCampi);
 * 
 * CONCETTO: router.refresh()
 * 
 * Dopo aver creato una prenotazione (POST), i dati nella pagina sono "vecchi".
 * router.refresh() dice a Next.js di ri-eseguire il Server Component
 * (la pagina /prenotazioni) per ricaricare i dati aggiornati dal backend.
 * 
 * È come fare "redirect:/prenotazioni" in Spring dopo un POST.
 */
"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/hooks/AuthContext";
import { Campo } from "@/types";
import { creaPrenotazione } from "@/services/prenotazioniService";

interface Props {
  campi: Campo[];  // la lista dei campi disponibili, passata dalla pagina server
}

export default function NuovaPrenotazioneForm({ campi }: Props) {
  // Stato del form — un useState per ogni campo
  const [idCampo, setIdCampo] = useState<number | "">("");
  const [data, setData] = useState("");
  const [oraInizio, setOraInizio] = useState("");
  const [errore, setErrore] = useState("");
  const [successo, setSuccesso] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const router = useRouter();
  const { utente } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrore("");
    setSuccesso("");

    // Validazione lato client (il backend valida anche lui — difesa in profondità)
    if (!utente) {
      setErrore("Devi essere loggato per prenotare.");
      return;
    }

    if (idCampo === "" || !data || !oraInizio) {
      setErrore("Tutti i campi sono obbligatori.");
      return;
    }

    setIsLoading(true);

    try {
      await creaPrenotazione({
        data: data,              // "2026-04-15"
        oraInizio: oraInizio,    // "14:00"
        idCampo: idCampo as number,
        idSocio: utente.id,      // l'utente loggato è chi prenota
      });

      setSuccesso("✅ Prenotazione creata con successo!");

      // Reset del form
      setIdCampo("");
      setData("");
      setOraInizio("");

      // CONCETTO: router.refresh()
      // Dice a Next.js di ri-eseguire la pagina server per aggiornare la lista.
      // Senza questo, la lista mostrerebbe i dati vecchi.
      router.refresh();

    } catch (error) {
      setErrore(error instanceof Error ? error.message : "Errore nella creazione");
    } finally {
      setIsLoading(false);
    }
  };

  // Se non loggato, mostra un messaggio
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

      {/* Messaggi di errore/successo */}
      {errore && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
          ⚠️ {errore}
        </div>
      )}
      {successo && (
        <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
          {successo}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        {/* 
          SELECT per il campo — è un "controlled component":
          il value è legato a idCampo (useState), e onChange lo aggiorna.
          
          In Thymeleaf:
            <select name="idCampo">
              <option th:each="campo : ${campi}" th:value="${campo.id}" th:text="${campo.nome}"/>
            </select>
          
          In React:
            <select value={idCampo} onChange={(e) => setIdCampo(Number(e.target.value))}>
              {campi.map(campo => <option key={campo.id} value={campo.id}>{campo.nome}</option>)}
            </select>
        */}
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

        {/* SUBMIT */}
        <button
          type="submit"
          disabled={isLoading}
          className="w-full py-3 bg-emerald-700 text-white font-semibold rounded-lg hover:bg-emerald-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isLoading ? "Creazione in corso..." : "Prenota Campo"}
        </button>
      </form>
    </div>
  );
}
