"use client";

import { useState, useEffect, useTransition } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import Image from "next/image";
import { siteConfig } from "@/config/site";

const navLinks = [
  { label: "Il Club", href: "/club" },
  { label: "Campi & Strutture", href: "/strutture" },
  { label: "Accademia S.T.A.", href: "/accademia" },
  { label: "Risultati Ufficiali", href: "/#risultati" },
  { label: "Contatti", href: "/contatti" },
];

export default function Navbar() {
  const pathname = usePathname();
  const [scrolled, setScrolled] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [, startTransition] = useTransition();

  useEffect(() => {
      const checkScroll = () => {
        setScrolled(window.scrollY > 20);
      };

      // Check immediate on mount
      checkScroll();

      window.addEventListener("scroll", checkScroll, { passive: true });
      return () => window.removeEventListener("scroll", checkScroll);
    }, []);

  if (pathname?.startsWith("/studio")) {
    return null;
  }

  // Nelle pagine interne (diverse dalla home "/") prima dello scroll,
  // l'header sottostante ha lo sfondo verde scuro #1B3C1F.
  const isDarkHeader = pathname !== "/" && !scrolled;

  const handleNavClick = (e: React.MouseEvent<HTMLAnchorElement>, href: string) => {
    if (href === "/#risultati" && pathname === "/") {
      e.preventDefault();
      document.getElementById("risultati")?.scrollIntoView({ behavior: "smooth" });
    }
    setMobileMenuOpen(false);
  };

  return (
    <header className="fixed top-0 left-0 right-0 z-50 transition-all duration-300">
      <div
        className={`w-full transition-all duration-300 ${scrolled
            ? "bg-[#FAF9F5]/95 backdrop-blur-md border-b border-[#E5E0D4] shadow-[0_2px_12px_rgba(24,34,25,0.05)] py-3"
            : "bg-transparent py-4 sm:py-5 border-b border-transparent"
          }`}
      >
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between">
          {/* Brand Logo & Title */}
              <Link
                href="/"
                prefetch={true}
                className="flex items-center gap-3 group focus:outline-none"
              >
                <div className="relative w-11 h-11 rounded-full overflow-hidden bg-white shadow-sm border border-[#E5E0D4] flex items-center justify-center p-1 group-hover:scale-105 transition-transform duration-200">
                  <Image
                    src="/images/logo_circolo-removebg-preview.png"
                    alt={`Logo ${siteConfig.clubName}`}
                    width={42}
                    height={42}
                    className="object-contain"
                    priority
                  />
                </div>
                <div className="flex flex-col">
                  <span
                    className={`font-bold text-lg sm:text-xl tracking-tight leading-none transition-colors duration-200 ${isDarkHeader ? "text-white" : "text-[#1B3C1F]"
                      }`}
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    {siteConfig.clubShortName}
                  </span>
                    <span
                      className={`text-[10px] uppercase font-semibold tracking-widest mt-1 transition-colors duration-200 ${isDarkHeader ? "text-[#DEE9DF]" : "text-[#5A695C]"
                        }`}
                    >
                      Carmignano • Toscana • Dal {siteConfig.foundedYear}
                    </span>
                </div>
              </Link>

                {/* Desktop Navigation — Segmented Pill Bar Snappy & Responsive */}
                <nav className="hidden lg:flex items-center gap-1 bg-white/95 backdrop-blur-md p-1.5 rounded-full border border-[#E5E0D4] shadow-sm">
                  {navLinks.map((link) => {
                    const isActive =
                      pathname === link.href ||
                      (link.href === "/#risultati" && pathname === "/" && typeof window !== "undefined" && window.location.hash === "#risultati");

                    return (
                      <Link
                        key={link.href}
                        href={link.href}
                        prefetch={true}
                        onClick={(e) => handleNavClick(e, link.href)}
                        className={`px-3.5 py-1.5 text-xs font-semibold uppercase tracking-wider rounded-full transition-all duration-150 ease-out select-none cursor-pointer ${isActive
                            ? "bg-[#1B3C1F] text-white shadow-xs scale-[1.02]"
                            : "text-[#5A695C] hover:text-[#182219] hover:bg-black/5 active:scale-95"
                          }`}
                      >
                        {link.label}
                      </Link>
                    );
                  })}
                </nav>

                {/* Action CTAs */}
                <div className="hidden lg:flex items-center gap-3">
                  <Link
                    href="/contatti"
                    prefetch={true}
                    className={`inline-flex items-center gap-2 px-5 py-2.5 rounded-full text-xs font-semibold uppercase tracking-wider transition-all duration-200 shadow-sm hover:shadow-md hover:-translate-y-0.5 active:scale-95 ${isDarkHeader
                        ? "bg-[#C05621] hover:bg-[#973F14] text-white"
                        : "bg-[#1B3C1F] hover:bg-[#112614] text-white"
                      }`}
                  >
                    <span>Prenota Campo</span>
                    <span className="text-stone-300">→</span>
                  </Link>
                </div>

                {/* Mobile Menu Button */}
                <button
                  onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                  className="lg:hidden p-2 rounded-xl bg-white border border-[#E5E0D4] text-[#1B3C1F] shadow-sm hover:bg-[#F0F5F1] active:scale-95 transition-all"
                  aria-label="Menu di navigazione"
                >
                  <svg
                    className="w-5 h-5"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    {mobileMenuOpen ? (
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M6 18L18 6M6 6l12 12"
                      />
                    ) : (
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M4 6h16M4 12h16M4 18h16"
                      />
                    )}
                  </svg>
                </button>
            </div>

            {/* Mobile Dropdown Menu */}
            {mobileMenuOpen && (
                      < div className="lg:hidden mt-3 mx-4 p-5 bg-white rounded-2xl border border-[#E5E0D4] shadow-xl space-y-2 animate-fade-in">
                    {navLinks.map((link) => {
                      const isActive = pathname === link.href;
                      return (
                        <Link
                          key={link.href}
                          href={link.href}
                          prefetch={true}
                          onClick={(e) => handleNavClick(e, link.href)}
                          className={`block px-4 py-2.5 rounded-xl text-sm font-medium transition-all duration-150 ${isActive
                              ? "bg-[#1B3C1F] text-white font-semibold"
                              : "text-[#182219] hover:bg-[#F0F5F1]"
                            }`}
                >
                    {link.label}
                  </Link>
                );
            })}
                <div className="pt-2 border-t border-[#E5E0D4] flex flex-col gap-2">
                  <Link
                    href="/contatti"
                    prefetch={true}
                    onClick={() => setMobileMenuOpen(false)}
                    className="w-full text-center py-3 rounded-xl bg-[#1B3C1F] text-white text-xs font-semibold uppercase tracking-wider shadow-sm"
                  >
                    Prenota un Campo
                  </Link>
                </div>
              </div>
            )}
        </div>
    </header>
  );
}
