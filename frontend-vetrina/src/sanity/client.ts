/**
 * sanity/client.ts — Client Sanity.io configurato per Next.js App Router.
 *
 * CONCETTO: Lazy Client Pattern
 *
 * Il client Sanity viene creato SOLO quando effettivamente necessario
 * (cioè quando il projectId è configurato). Questo evita errori di
 * validazione durante il build quando le variabili d'ambiente non
 * sono ancora impostate.
 *
 * VARIABILI D'AMBIENTE RICHIESTE:
 *   NEXT_PUBLIC_SANITY_PROJECT_ID → Il Project ID dal pannello sanity.io/manage
 *   NEXT_PUBLIC_SANITY_DATASET    → Il dataset (default: "production")
 *
 * COME OTTENERE IL PROJECT ID:
 *   1. Vai su https://www.sanity.io/manage
 *   2. Crea un nuovo progetto (o seleziona uno esistente)
 *   3. Copia il "Project ID" dalla dashboard
 *   4. Crea un file .env.local nella root di frontend-vetrina/
 *   5. Inserisci: NEXT_PUBLIC_SANITY_PROJECT_ID=abc123xyz
 */

import { createClient } from "next-sanity";

/**
 * Configurazione condivisa — usata sia dal client di fetch che dallo Studio.
 * Esportata separatamente per poterla riutilizzare in sanity.config.ts.
 */
export const sanityConfig = {
  projectId: process.env.NEXT_PUBLIC_SANITY_PROJECT_ID || "preview-placeholder",
  dataset: process.env.NEXT_PUBLIC_SANITY_DATASET || "production",
  apiVersion: "2024-01-01",
};

/**
 * Verifica se Sanity è configurato con un projectId valido.
 */
export function isSanityConfigured(): boolean {
  const pid = process.env.NEXT_PUBLIC_SANITY_PROJECT_ID;
  return (
    typeof pid === "string" &&
    pid.trim().length > 0 &&
    pid !== "preview-placeholder" &&
    pid !== "YOUR_PROJECT_ID_HERE" &&
    pid !== "your_sanity_project_id_here"
  );
}

/**
 * Crea il client Sanity on-demand.
 *
 * CONCETTO: Lazy Initialization
 *
 * Invece di creare il client a livello di modulo (che causerebbe
 * un errore di validazione se projectId è vuoto), lo creiamo
 * solo quando serve. Questo permette al build di passare anche
 * senza variabili d'ambiente configurate.
 *
 * @throws Error se chiamato senza projectId configurato
 */
export function getSanityClient() {
  if (!isSanityConfigured()) {
    throw new Error(
      "[Sanity] NEXT_PUBLIC_SANITY_PROJECT_ID non configurato. " +
      "Crea un file .env.local con il tuo Project ID. " +
      "Vedi .env.local.example per le istruzioni."
    );
  }

  return createClient({
    ...sanityConfig,
    useCdn: false,
  });
}
