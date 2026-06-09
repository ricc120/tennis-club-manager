/**
 * Navbar — Barra di navigazione con stato di autenticazione.
 * 
 * STEP 5: Aggiunto "use client" perché ora la Navbar deve:
 * - Leggere lo stato di autenticazione (useAuth)
 * - Gestire il click su "Logout" (evento click)
 * 
 * CONCETTO: rendering condizionale basato sullo stato
 * 
 * Se utente è loggato:   mostra "Ciao, Mario (ADMIN)" + bottone Logout
 * Se utente NON è loggato: mostra bottone "Accedi"
 * 
 * In Thymeleaf facevi:
 *   <span th:if="${session.utente != null}" th:text="${session.utente.nome}">
 *   <a th:unless="${session.utente != null}" href="/login">Accedi</a>
 * 
 * In React:
 *   {utente ? <span>{utente.nome}</span> : <Link href="/login">Accedi</Link>}
 */
"use client";

import Link from "next/link";
import { useAuth } from "@/hooks/AuthContext";

export default function Navbar() {
  const { utente, logout, isLoading } = useAuth();

  return (
    <nav className="bg-emerald-800 text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link href="/" className="flex items-center gap-2 group">
            <span className="text-2xl">🎾</span>
            <span className="text-xl font-bold tracking-tight group-hover:text-emerald-200 transition-colors">
              Tennis Club Manager
            </span>
          </Link>

          {/* Link di navigazione + area utente */}
          <div className="flex items-center gap-1">
            <Link
              href="/campi"
              className="px-3 py-2 rounded-md text-sm font-medium hover:bg-emerald-700 transition-colors"
            >
              Campi
            </Link>
            <Link
              href="/prenotazioni"
              className="px-3 py-2 rounded-md text-sm font-medium hover:bg-emerald-700 transition-colors"
            >
              Prenotazioni
            </Link>

            {/* 
              RENDERING CONDIZIONALE:
              Se sta ancora caricando (isLoading), non mostrare nulla
              Se l'utente è loggato, mostra nome + logout
              Se non è loggato, mostra "Accedi"
            */}
            {!isLoading && (
              <>
                {utente ? (
                  // ===== UTENTE LOGGATO =====
                  <div className="flex items-center gap-3 ml-3">
                    {/* Badge con ruolo */}
                    <span className="text-xs bg-emerald-600 px-2 py-1 rounded-full">
                      {utente.ruolo}
                    </span>
                    {/* Nome utente */}
                    <span className="text-sm font-medium">
                      {utente.nome} {utente.cognome}
                    </span>
                    {/* Bottone Logout */}
                    <button
                      onClick={logout}
                      className="ml-1 px-3 py-1.5 rounded-md text-sm font-medium bg-red-500/20 text-red-200 hover:bg-red-500/30 transition-colors"
                    >
                      Esci
                    </button>
                  </div>
                ) : (
                  // ===== UTENTE NON LOGGATO =====
                  <Link
                    href="/login"
                    className="ml-2 px-4 py-2 rounded-md text-sm font-medium bg-white text-emerald-800 hover:bg-emerald-100 transition-colors"
                  >
                    Accedi
                  </Link>
                )}
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}
