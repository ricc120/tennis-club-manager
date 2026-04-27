/**
 * Pagina /login — Form di autenticazione.
 * 
 * ⚡ QUESTO È UN CLIENT COMPONENT ⚡
 * 
 * A differenza delle pagine /campi e /prenotazioni (Server Components),
 * questa pagina ha la direttiva "use client" perché:
 * - Ha un FORM con input che l'utente compila
 * - Usa useState per tenere traccia di email, password, errori
 * - Gestisce eventi (onChange, onSubmit)
 * 
 * CONCETTO: useState
 * 
 * In Java, una variabile di istanza si aggiorna e basta:
 *   this.email = "nuova@email.com";
 * 
 * In React, le variabili normali NON causano un re-render.
 * Devi usare useState:
 *   const [email, setEmail] = useState("");
 *   setEmail("nuova@email.com");  // aggiorna E re-renderizza il componente
 * 
 * Perché? Perché React deve sapere QUANDO qualcosa cambia per aggiornare il DOM.
 * 
 * CONCETTO: onChange + onSubmit
 * 
 * onChange: scatta ogni volta che l'utente digita una lettera nell'input
 *   <input onChange={(e) => setEmail(e.target.value)} />
 *   e.target.value = il valore corrente dell'input
 * 
 * onSubmit: scatta quando il form viene inviato (click su bottone o Enter)
 *   e.preventDefault() impedisce al browser di ricaricare la pagina
 *   (comportamento default dei form HTML)
 */
"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/hooks/AuthContext";
import { login } from "@/services/authService";
import Link from "next/link";

export default function LoginPage() {
  // ========== STATO DEL COMPONENTE (useState) ==========

  // Ogni useState crea una coppia [valore, funzionePerAggiornarlo]
  const [email, setEmail] = useState("");           // "" = valore iniziale
  const [password, setPassword] = useState("");
  const [errore, setErrore] = useState("");          // messaggio di errore
  const [isLoading, setIsLoading] = useState(false); // true durante la richiesta

  // Hooks di Next.js e del nostro context
  const router = useRouter();    // per navigare programmaticamente (redirect)
  const { setUtente } = useAuth(); // per salvare l'utente loggato nel context

  // ========== GESTIONE SUBMIT DEL FORM ==========

  /**
   * Questa funzione viene chiamata quando il form viene inviato.
   * 
   * "async" perché dobbiamo aspettare la risposta del backend.
   * "e: React.FormEvent" è il tipo dell'evento del form.
   */
  const handleSubmit = async (e: React.FormEvent) => {
    // Impedisce al browser di ricaricare la pagina (comportamento default dei form)
    e.preventDefault();

    // Reset errore precedente
    setErrore("");
    setIsLoading(true);

    try {
      // Chiama il backend: POST /api/auth/login
      const utente = await login({ email, password });

      // Login riuscito! Salva l'utente nel context (come session.setAttribute)
      setUtente(utente);

      // Redirect alla homepage
      router.push("/");

    } catch (error) {
      // Login fallito — mostra il messaggio di errore
      if (error instanceof Error) {
        setErrore(error.message);
      } else {
        setErrore("Errore durante il login");
      }
    } finally {
      // "finally" viene eseguito sia in caso di successo che di errore
      // (come in Java)
      setIsLoading(false);
    }
  };

  // ========== RENDER (JSX) ==========

  return (
    <div className="bg-white dark:bg-gray min-h-[80vh] flex items-center justify-center px-4">
      <div className="w-full max-w-md">
        {/* HEADER */}
        <div className="text-center mb-8">
          <span className="text-5xl">🎾</span>
          <h1 className="text-3xl font-bold text-gray-800 mt-4">
            Accedi al Tennis Club
          </h1>
          <p className="text-gray-600 mt-2">
            Inserisci le tue credenziali per accedere
          </p>
        </div>

        {/* CARD del form */}
        <div className="bg-white rounded-xl shadow-lg border border-gray-100 p-8">
          {/* 
            MESSAGGIO DI ERRORE — rendering condizionale con &&
            Mostrato solo se "errore" non è una stringa vuota
          */}
          {errore && (
            <div className="mb-6 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
              ⚠️ {errore}
            </div>
          )}

          {/* 
            FORM — onSubmit chiama handleSubmit quando l'utente preme Enter o il bottone.
            In Thymeleaf:  <form th:action="@{/login}" method="post">
            In React:      <form onSubmit={handleSubmit}>
          */}
          <form onSubmit={handleSubmit} className="space-y-5">
            {/* CAMPO EMAIL */}
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Email
              </label>
              {/* 
                "value={email}" lega l'input allo stato → "controlled input"
                "onChange" aggiorna lo stato ad ogni lettera digitata
                In Java: this.email = inputField.getText();
              */}
              <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                placeholder="mario.rossi@email.com"
                className="w-full text-black px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition-colors"
              />
            </div>

            {/* CAMPO PASSWORD */}
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Password
              </label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                placeholder="••••••••"
                className="w-full text-black px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition-colors"
              />
            </div>

            {/* BOTTONE SUBMIT */}
            <button
              type="submit"
              disabled={isLoading}
              className="w-full py-3 bg-emerald-700 text-white font-semibold rounded-lg hover:bg-emerald-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {/* Mostra testo diverso in base allo stato di loading */}
              {isLoading ? "Accesso in corso..." : "Accedi"}
            </button>
          </form>
        </div>

        {/* LINK alla homepage */}
        <p className="text-center text-sm text-gray-500 mt-6">
          <Link href="/" className="text-emerald-700 hover:underline">
            ← Torna alla homepage
          </Link>
        </p>
      </div>
    </div>
  );
}
