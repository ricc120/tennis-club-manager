import type { NextConfig } from "next";

/**
 * Configurazione Next.js
 * 
 * STEP 7: output: "standalone"
 * 
 * Senza "standalone": il build produce file che RICHIEDONO node_modules (~500MB)
 * Con "standalone":   il build produce un bundle AUTOSUFFICIENTE (~30MB)
 * 
 * In pratica, Next.js copia dentro la cartella .next/standalone SOLO i file
 * necessari per l'esecuzione, inclusa una versione minimale di node_modules.
 * Questo rende l'immagine Docker molto più leggera.
 * 
 * È come il multi-stage build del backend:
 *   Backend: Maven compila → crea un .jar autosufficiente
 *   Frontend: Next.js builda → crea una cartella standalone autosufficiente
 */
const nextConfig: NextConfig = {
  output: "standalone",
};

export default nextConfig;

