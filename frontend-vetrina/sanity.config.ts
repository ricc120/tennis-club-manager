/**
 * sanity.config.ts — Configurazione globale di Sanity Studio.
 *
 * CONCETTO: Embedded Studio
 *
 * Sanity Studio è l'interfaccia di amministrazione (CMS) dove
 * gli editor inseriscono e gestiscono i contenuti.
 *
 * Con next-sanity, lo Studio viene integrato DENTRO l'app Next.js
 * come una rotta dedicata (/studio), invece di essere un'app separata.
 *
 * Vantaggi:
 *   - Un solo deploy (frontend + CMS insieme)
 *   - Stessa autenticazione
 *   - Accesso immediato: basta navigare a /studio
 *
 * COME ACCEDERE ALLO STUDIO:
 *   1. Avvia il dev server: npm run dev
 *   2. Vai su http://localhost:3001/studio
 *   3. Fai login con il tuo account Sanity
 *   4. Gestisci i contenuti (gare, eventi, ecc.)
 */

"use client";

import { defineConfig } from "sanity";
import { structureTool } from "sanity/structure";
import { schemaTypes } from "@/sanity/schemas";
import { sanityConfig } from "@/sanity/client";

export default defineConfig({
  /**
   * Riutilizziamo la stessa configurazione (projectId, dataset)
   * definita in sanity/client.ts → unica fonte di verità.
   */
  ...sanityConfig,

  name: "tennis-club-studio",
  title: "Tennis Club — CMS",

  /**
   * basePath: "/studio"
   *
   * Questo dice a Sanity Studio di montarsi sotto /studio.
   * La rotta catch-all in app/studio/[[...index]]/page.tsx
   * intercetta tutte le sotto-rotte dello Studio.
   */
  basePath: "/studio",

  /**
   * plugins: [structureTool()]
   *
   * Lo Structure Tool è il pannello di navigazione dei documenti.
   * È il plugin principale dello Studio che mostra la lista dei
   * tipi di documento (Gara a Squadre, ecc.) e i loro contenuti.
   */
  plugins: [structureTool()],

  /**
   * schema.types: i tipi di documento gestibili dallo Studio.
   * Importati dal registro centrale in sanity/schemas/index.ts.
   */
  schema: {
    types: schemaTypes,
  },
});
