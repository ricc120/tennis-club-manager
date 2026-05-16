/**
 * useCampi — Hook per caricare i campi con TanStack Query.
 *
 * STEP 9: TanStack Query
 *
 * CONCETTO: useQuery
 *
 * useQuery è il cuore di TanStack Query per LEGGERE dati.
 * Sostituisce il pattern manuale: useState + useEffect + try/catch + fetch.
 *
 * PRIMA (senza React Query):
 *   const [campi, setCampi] = useState([]);
 *   const [isLoading, setIsLoading] = useState(true);
 *   const [error, setError] = useState(null);
 *   useEffect(() => {
 *     fetch("/api/campi")
 *       .then(res => res.json())
 *       .then(data => { setCampi(data); setIsLoading(false); })
 *       .catch(err => { setError(err); setIsLoading(false); });
 *   }, []);
 *
 * ADESSO (con React Query):
 *   const { data: campi, isLoading, error } = useQuery({
 *     queryKey: ["campi"],
 *     queryFn: getCampi,
 *   });
 *
 * React Query gestisce automaticamente:
 * - Loading state (isLoading)
 * - Error state (error)
 * - Caching (non rifa il fetch se i dati sono "freschi")
 * - Refetch in background (aggiorna i dati quando diventano "stale")
 * - Retry automatico (riprova se il fetch fallisce)
 *
 * CONCETTO: Query Key
 *
 * ["campi"] è la "chiave di cache" — identifica UNIVOCAMENTE questa query.
 * Come una chiave in una HashMap:
 *   Java:    cache.put("campi", listaCampi);
 *   React Q: queryKey: ["campi"]
 *
 * Se due componenti usano la stessa queryKey, condividono la stessa cache.
 * Il fetch viene fatto UNA SOLA VOLTA, e il risultato è condiviso.
 */

import { useQuery } from "@tanstack/react-query";
import { getCampi } from "@/services/campiService";
import { Campo } from "@/types";

/**
 * Hook per ottenere la lista dei campi.
 *
 * Uso nei componenti:
 *   const { data: campi, isLoading, error } = useCampi();
 *
 *   if (isLoading) return <Spinner />;
 *   if (error) return <ErrorMessage />;
 *   return <CampiList campi={campi} />;
 */
export function useCampi() {
  return useQuery<Campo[]>({
    queryKey: ["campi"],
    queryFn: getCampi,
  });
}
