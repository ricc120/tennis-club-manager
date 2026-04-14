/**
 * RootLayout — Il layout che avvolge TUTTE le pagine del sito.
 * 
 * STEP 5: Aggiunto AuthProvider per condividere lo stato di autenticazione
 * con tutti i componenti dell'app.
 * 
 * AuthProvider è un Client Component (usa "use client"), ma può essere
 * incluso in un Server Component (questo layout). Next.js gestisce
 * automaticamente il confine tra server e client.
 */

import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import { AuthProvider } from "@/hooks/AuthContext";

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-inter",
});

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
      <body className="min-h-screen flex flex-col bg-gray-50 font-sans antialiased">
        {/* 
          AuthProvider wrappa TUTTO — così Navbar, pagine e Footer
          possono tutti accedere a useAuth() per sapere chi è loggato.
          È come HttpSession che è condivisa tra tutte le richieste.
        */}
        <AuthProvider>
          <Navbar />
          <main className="flex-1">
            {children}
          </main>
          <Footer />
        </AuthProvider>
      </body>
    </html>
  );
}

