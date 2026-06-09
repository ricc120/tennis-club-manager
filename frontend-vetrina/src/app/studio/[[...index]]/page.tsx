/**
 * app/studio/[[...index]]/page.tsx — Rotta catch-all per Sanity Studio.
 *
 * CONCETTO: Rotta Catch-All in Next.js App Router
 *
 * La sintassi [[...index]] cattura TUTTE le sotto-rotte:
 *   /studio           → pagina principale dello Studio
 *   /studio/desk      → pannello documenti
 *   /studio/desk/garaSquadre → lista delle gare
 *   /studio/desk/garaSquadre/abc123 → singola gara
 *
 * NextStudio è un componente di next-sanity che renderizza
 * l'intero Sanity Studio come una pagina React.
 *
 * NOTA: "use client" è necessario perché lo Studio è un'app
 * React interattiva che gira interamente nel browser.
 */

"use client";

import { NextStudio } from "next-sanity/studio";
import config from "../../../../sanity.config";

export default function StudioPage() {
  return <NextStudio config={config} />;
}
