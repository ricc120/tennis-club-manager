import type { Metadata } from "next";
import "./globals.css";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

export const metadata: Metadata = {
  title: "TC Carmignano — Tennis Club & Padel | Stefanini Tennis Academy",
  description:
    "Tennis Club Carmignano: campi in terra rossa, padel panoramici e la Stefanini Tennis Academy (S.T.A.) nel cuore della Toscana. Prenota il tuo campo.",
  keywords: "tennis, padel, carmignano, prato, toscana, accademia tennis, S.T.A., Stefanini",
  openGraph: {
    title: "TC Carmignano — Tennis, Padel & Academy",
    description: "Dal 1985, tradizione e innovazione nel cuore della Toscana.",
    type: "website",
    locale: "it_IT",
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="it" className="h-full">
      <head>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin="anonymous" />
        <link
          href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;600;700&family=Inter:wght@300;400;500;600;700&display=swap"
          rel="stylesheet"
        />
      </head>
      <body className="min-h-screen flex flex-col">
        <Navbar />
        <main className="flex-1">{children}</main>
        <Footer />
      </body>
    </html>
  );
}
