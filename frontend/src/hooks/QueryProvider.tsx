/**
 * QueryProvider — Configurazione globale di TanStack Query.
 *
 * STEP 9: TanStack Query (React Query)
 *
 * CONCETTO: QueryClient e QueryClientProvider
 *
 * TanStack Query ha bisogno di un "contenitore" globale per gestire:
 * - La CACHE dei dati (evita fetch ripetuti)
 * - Lo stato delle richieste (loading, error, success)
 * - L'invalidazione automatica (quando i dati cambiano)
 *
 * QueryClient è il "motore" di TanStack Query — è come il SessionFactory
 * di Hibernate: lo crei una volta e lo condividi con tutta l'app.
 *
 * QueryClientProvider wrappa l'app e rende il QueryClient disponibile
 * a tutti i componenti che usano useQuery/useMutation.
 *
 * ANALOGIA CON SPRING:
 *   Spring: @Bean SessionFactory → una sola istanza condivisa
 *   React:  QueryClientProvider  → un solo QueryClient condiviso
 *
 * CONCETTO: useState(() => new QueryClient(...))
 *
 * Usiamo useState per creare il QueryClient UNA SOLA VOLTA.
 * Senza useState, React ricreerebbe il QueryClient ad ogni re-render,
 * perdendo tutta la cache! Il pattern "lazy initialization" (passare
 * una funzione a useState) garantisce che venga creato solo al primo render.
 */
"use client";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useState, ReactNode } from "react";

export default function QueryProvider({ children }: { children: ReactNode }) {
  // Crea il QueryClient UNA sola volta (lazy initialization)
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            // staleTime: per quanto tempo i dati sono considerati "freschi"
            // Durante questo periodo, React Query NON rifa il fetch.
            // Dopo questo periodo, al prossimo accesso rifa il fetch in background.
            //
            // 30 secondi: un buon compromesso per la nostra app.
            // Se qualcuno prenota un campo, vedremo l'aggiornamento
            // al massimo dopo 30 secondi (o immediatamente se usiamo invalidation).
            staleTime: 30 * 1000, // 30 secondi

            // retry: quante volte ritentare in caso di errore
            // Default è 3, ma 1 è sufficiente per la nostra app
            retry: 1,
          },
        },
      })
  );

  return (
    <QueryClientProvider client={queryClient}>
      {children}
    </QueryClientProvider>
  );
}
