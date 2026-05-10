/**
 * AuthContext — Stato globale con JWT token.
 * 
 * STEP 8: JWT Authentication
 * 
 * PRIMA (Step 5): salvava solo l'oggetto utente in localStorage
 * ADESSO (Step 8): salva sia l'utente che il JWT token
 * 
 * Il token viene gestito separatamente dall'utente:
 *   - utente → per la UI (mostrare nome, ruolo, bottoni condizionali)
 *   - token  → per le API (inviato nell'header Authorization)
 * 
 * apiClient.ts legge il token direttamente da localStorage,
 * quindi non serve passarlo tramite il context.
 */
"use client";

import { createContext, useContext, useState, useEffect, ReactNode } from "react";
import { Utente } from "@/types";
import { logout as authLogout } from "@/services/authService";

interface AuthContextType {
  utente: Utente | null;
  setUtente: (utente: Utente | null) => void;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType>({
  utente: null,
  setUtente: () => {},
  logout: () => {},
  isLoading: true,
});

export function AuthProvider({ children }: { children: ReactNode }) {
  const [utente, setUtenteState] = useState<Utente | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Al primo caricamento, controlla se c'è un utente salvato
  useEffect(() => {
    try {
      const salvato = localStorage.getItem("utente");
      if (salvato) {
        setUtenteState(JSON.parse(salvato));
      }
    } catch {
      localStorage.removeItem("utente");
      localStorage.removeItem("jwt_token");
    }
    setIsLoading(false);
  }, []);

  const setUtente = (utente: Utente | null) => {
    setUtenteState(utente);
    if (utente) {
      localStorage.setItem("utente", JSON.stringify(utente));
    } else {
      localStorage.removeItem("utente");
    }
  };

  // STEP 8: Logout rimuove sia l'utente che il JWT token
  const logout = () => {
    setUtenteState(null);
    authLogout();  // rimuove jwt_token + utente da localStorage
  };

  return (
    <AuthContext.Provider value={{ utente, setUtente, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
