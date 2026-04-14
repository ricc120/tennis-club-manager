/**
 * AuthContext — Stato globale dell'utente autenticato.
 * 
 * CONCETTO CHIAVE: React Context
 * 
 * PROBLEMA: Quando l'utente fa login, la Navbar deve mostrare il suo nome,
 * la pagina prenotazioni deve sapere chi sta prenotando, etc.
 * Come condividi l'info "chi è loggato?" tra componenti diversi?
 * 
 * SOLUZIONE IN SPRING BOOT:
 *   session.setAttribute("utente", utente);       // salva
 *   Utente utente = (Utente) session.getAttribute("utente");  // legge
 *   La sessione HTTP è condivisa tra tutte le richieste.
 * 
 * SOLUZIONE IN REACT:
 *   React Context crea un "contenitore globale" che wrappa l'intera app.
 *   Qualsiasi componente dentro il Provider può leggere e modificare il valore.
 * 
 * STRUTTURA:
 *   1. createContext() → crea il contenitore
 *   2. AuthProvider    → componente che wrappa l'app e fornisce il valore
 *   3. useAuth()       → hook che i componenti usano per leggere il valore
 * 
 * "use client" — OBBLIGATORIO qui perché usiamo useState e useEffect.
 * I Client Components girano nel browser (non sul server Node.js).
 */
"use client";

import { createContext, useContext, useState, useEffect, ReactNode } from "react";
import { Utente } from "@/types";

// ============================================================
// 1. TIPO DEL CONTEXT — definisce cosa contiene
// ============================================================
interface AuthContextType {
  utente: Utente | null;        // l'utente loggato (null = non loggato)
  setUtente: (utente: Utente | null) => void;  // funzione per aggiornare l'utente
  logout: () => void;           // funzione per fare logout
  isLoading: boolean;           // true mentre controlla localStorage
}

// ============================================================
// 2. CREAZIONE DEL CONTEXT — il "contenitore vuoto"
// ============================================================
/**
 * createContext crea un context con un valore di default.
 * Il valore di default è usato SOLO se un componente prova a leggere
 * il context senza essere wrappato da un Provider (errore di programmazione).
 */
const AuthContext = createContext<AuthContextType>({
  utente: null,
  setUtente: () => {},
  logout: () => {},
  isLoading: true,
});

// ============================================================
// 3. PROVIDER — il componente che "fornisce" il valore a tutta l'app
// ============================================================
/**
 * AuthProvider wrappa l'intera app in layout.tsx.
 * Tutti i componenti figli possono accedere a utente/setUtente/logout.
 * 
 * CONCETTO: useState
 * 
 * useState è il modo di React per gestire variabili che cambiano nel tempo.
 * In Java avresti:  private Utente utente = null;
 * In React:         const [utente, setUtente] = useState<Utente | null>(null);
 * 
 * Quando chiami setUtente(nuovoValore), React:
 * 1. Aggiorna il valore
 * 2. Re-renderizza tutti i componenti che usano quel valore
 * 
 * CONCETTO: useEffect
 * 
 * useEffect esegue codice DOPO che il componente è stato renderizzato.
 * Lo usiamo per leggere localStorage (disponibile solo nel browser).
 * 
 * useEffect(() => { ... }, []) ← l'array vuoto [] significa "esegui solo al mount"
 * (mount = quando il componente appare per la prima volta)
 */
export function AuthProvider({ children }: { children: ReactNode }) {
  const [utente, setUtenteState] = useState<Utente | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Al primo caricamento, controlla se c'è un utente salvato in localStorage
  useEffect(() => {
    try {
      const salvato = localStorage.getItem("utente");
      if (salvato) {
        setUtenteState(JSON.parse(salvato));
      }
    } catch {
      // Se il JSON è corrotto, ignora
      localStorage.removeItem("utente");
    }
    setIsLoading(false);
  }, []);

  // Wrapper che salva anche in localStorage
  const setUtente = (utente: Utente | null) => {
    setUtenteState(utente);
    if (utente) {
      localStorage.setItem("utente", JSON.stringify(utente));
    } else {
      localStorage.removeItem("utente");
    }
  };

  // Logout: rimuove l'utente dallo stato e da localStorage
  const logout = () => {
    setUtente(null);
  };

  return (
    // Il Provider wrappa i children (tutta l'app) e fornisce il valore del context
    <AuthContext.Provider value={{ utente, setUtente, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

// ============================================================
// 4. HOOK CUSTOM — per leggere il context dai componenti
// ============================================================
/**
 * useAuth() — Hook custom per accedere al context di autenticazione.
 * 
 * Uso nei componenti:
 *   const { utente, logout } = useAuth();
 *   if (utente) { ... utente è loggato ... }
 * 
 * È come session.getAttribute("utente") in Java, ma lato frontend.
 */
export function useAuth() {
  return useContext(AuthContext);
}
