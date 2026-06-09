/**
 * usePrenotazioni — Hooks per CRUD delle prenotazioni con TanStack Query.
 *
 * STEP 9: TanStack Query
 *
 * Questo file contiene TRE hooks:
 *
 * 1. usePrenotazioni()         → useQuery  → GET /api/prenotazioni
 * 2. useCreaPrenotazione()     → useMutation → POST /api/prenotazioni
 * 3. useCancellaPrenotazione() → useMutation → DELETE /api/prenotazioni/{id}
 *
 * CONCETTO: useMutation
 *
 * useQuery è per LEGGERE dati (GET).
 * useMutation è per MODIFICARE dati (POST, PUT, DELETE).
 *
 * La differenza chiave:
 *   useQuery: esegue automaticamente al mount del componente
 *   useMutation: esegue SOLO quando chiami mutate()
 *
 * ANALOGIA:
 *   useQuery ≈ @GetMapping — scatta automaticamente quando apri la pagina
 *   useMutation ≈ @PostMapping — scatta solo quando l'utente invia il form
 *
 * CONCETTO: invalidateQueries (Cache Invalidation)
 *
 * Dopo un POST o DELETE, i dati nella cache sono "vecchi".
 * invalidateQueries(["prenotazioni"]) dice a React Query:
 * "i dati con chiave 'prenotazioni' non sono più validi, rifai il fetch!"
 *
 * PRIMA: router.refresh() → ricaricava TUTTA la pagina dal server
 * ADESSO: invalidateQueries → rifà SOLO il GET delle prenotazioni
 *
 * È come in Spring quando dopo un INSERT fai una nuova SELECT:
 *   prenotazioneDAO.insert(p);
 *   return prenotazioneDAO.findAll();  // rifetch dei dati aggiornati
 */

import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  getPrenotazioni,
  creaPrenotazione,
  cancellaPrenotazione,
  NuovaPrenotazione,
} from "@/services/prenotazioniService";
import { Prenotazione } from "@/types";

// ==================== READ ====================

/**
 * Hook per ottenere la lista delle prenotazioni.
 *
 * Uso:
 *   const { data: prenotazioni, isLoading, error } = usePrenotazioni();
 */
export function usePrenotazioni() {
  return useQuery<Prenotazione[]>({
    queryKey: ["prenotazioni"],
    queryFn: getPrenotazioni,
  });
}

// ==================== CREATE ====================

/**
 * Hook mutation per creare una nuova prenotazione.
 *
 * Uso:
 *   const { mutate: crea, isPending } = useCreaPrenotazione();
 *   crea({ data: "2026-06-01", oraInizio: "10:00", idCampo: 1, idSocio: 1 });
 *
 * CONCETTO: onSuccess callback
 *
 * Dopo che il POST ha successo, invalidateQueries("prenotazioni")
 * forza un refetch automatico della lista.
 * Il componente che usa usePrenotazioni() si aggiorna da solo!
 *
 * FLUSSO:
 * 1. Utente compila il form e clicca "Prenota"
 * 2. useCreaPrenotazione.mutate(dati) → POST /api/prenotazioni
 * 3. Backend crea la prenotazione → risponde con 201
 * 4. onSuccess scatta → invalidateQueries(["prenotazioni"])
 * 5. React Query rifà GET /api/prenotazioni
 * 6. La lista si aggiorna automaticamente con la nuova prenotazione
 */
export function useCreaPrenotazione() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (dati: NuovaPrenotazione) => creaPrenotazione(dati),
    onSuccess: () => {
      // Invalida la cache delle prenotazioni → trigger automatico di refetch
      queryClient.invalidateQueries({ queryKey: ["prenotazioni"] });
    },
  });
}

// ==================== DELETE ====================

/**
 * Hook mutation per eliminare una prenotazione.
 *
 * Uso:
 *   const { mutate: elimina, isPending } = useCancellaPrenotazione();
 *   elimina(prenotazione.id);
 *
 * Stessa logica del create: dopo il DELETE, invalida la cache
 * e la lista si aggiorna automaticamente (la card eliminata scompare).
 */
export function useCancellaPrenotazione() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: number) => cancellaPrenotazione(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["prenotazioni"] });
    },
  });
}
