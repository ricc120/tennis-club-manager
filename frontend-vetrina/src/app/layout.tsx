import type { Metadata } from "next";
import "./globals.css";
import AppLayout from "@/components/AppLayout";
import { siteConfig } from "@/config/site";

/**
 * CONCETTO: Metadata dinamica da configurazione White-Label
 *
 * I dati SEO non sono più hardcoded nel layout ma letti dal file
 * di configurazione centralizzato. Per un nuovo cliente basta
 * aggiornare siteConfig.seo e il build genera i meta tag corretti.
 */
export const metadata: Metadata = {
  title: siteConfig.seo.title,
  description: siteConfig.seo.description,
  keywords: siteConfig.seo.keywords,
  openGraph: {
    title: siteConfig.seo.ogTitle,
    description: siteConfig.seo.ogDescription,
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
        <link
          rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
        />
      </head>
      <body className="h-full bg-dark text-white">
        <AppLayout>{children}</AppLayout>
      </body>
    </html>
  );
}
