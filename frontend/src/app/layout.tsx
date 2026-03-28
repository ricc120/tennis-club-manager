/**
 * RootLayout — Il layout che avvolge TUTTE le pagine del sito.
 * 
 * CONCETTI CHIAVE:
 * - "metadata" è un oggetto speciale di Next.js per la SEO (titolo, descrizione)
 *   Next.js lo usa per generare i tag <title> e <meta> automaticamente
 * - "Inter" è un font di Google Fonts. Next.js lo scarica a build time (non dal browser)
 *   così la pagina è più veloce
 * - "{children}" è dove Next.js inietta la pagina corrente
 *   Es: se navighi a /campi → children = contenuto di app/campi/page.tsx
 * - Struttura: <html> → <body> → Navbar + children + Footer
 *   Il "flex flex-col min-h-screen" fa sì che il Footer stia sempre in fondo
 */

import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

// Configura il font Inter da Google Fonts
// "subsets: ['latin']" limita i caratteri scaricati (più veloce)
// "variable" crea una CSS variable --font-inter utilizzabile in Tailwind
const inter = Inter({
  subsets: ["latin"],
  variable: "--font-inter",
});

// Metadata SEO — visibili nei motori di ricerca e nel tab del browser
export const metadata: Metadata = {
  title: "Tennis Club Manager",
  description: "Gestisci prenotazioni, campi e lezioni del tuo tennis club",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="it" className={`${inter.variable} h-full`}>
      {/* 
        "min-h-screen" = altezza minima 100% dello schermo
        "flex flex-col" = layout verticale (Navbar in alto, Footer in basso)
        il Footer ha "mt-auto" che lo spinge in fondo
      */}
      <body className="min-h-screen flex flex-col bg-gray-50 font-sans antialiased">
        <Navbar />
        {/* 
          "flex-1" fa espandere il main per occupare tutto lo spazio disponibile
          tra Navbar e Footer 
        */}
        <main className="flex-1">
          {children}
        </main>
        <Footer />
      </body>
    </html>
  );
}
