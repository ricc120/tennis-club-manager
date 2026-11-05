import type { Metadata } from "next";
import { Playfair_Display, Inter } from "next/font/google";
import "./globals.css";
import AppLayout from "@/components/AppLayout";
import { siteConfig } from "@/config/site";

const playfair = Playfair_Display({
  subsets: ["latin"],
  variable: "--font-serif",
  display: "swap",
});

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-sans",
  display: "swap",
});

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
    <html
      lang="it"
      className={`h-full scroll-smooth ${playfair.variable} ${inter.variable}`}
    >
      <body className="min-h-full bg-[#FAF9F5] text-[#19221A] font-sans antialiased selection:bg-[#C45A2C]/20 selection:text-[#C45A2C]">
        <AppLayout>{children}</AppLayout>
      </body>
    </html>
  );
}
