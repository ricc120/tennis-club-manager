"use client";

import { useState, useEffect } from "react";
import Link from "next/link";

const navLinks = [
  { label: "Chi Siamo", href: "#chi-siamo" },
  { label: "Campi", href: "#campi" },
  { label: "Academy", href: "#academy" },
  { label: "Tariffe", href: "#tariffe" },
  { label: "Gallery", href: "#gallery" },
  { label: "Contatti", href: "#contatti" },
];

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 50);
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <nav
      className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 ${
        scrolled
          ? "bg-dark/95 backdrop-blur-md shadow-lg py-3"
          : "bg-transparent py-5"
      }`}
    >
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between">
        {/* Logo */}
        <Link href="#" className="flex items-center gap-2 group">
          <span className="text-2xl">🎾</span>
          <div>
            <span className="text-white font-bold text-lg tracking-wide group-hover:text-tennis-light transition-colors">
              TC Carmignano
            </span>
          </div>
        </Link>

        {/* Desktop Links */}
        <div className="hidden lg:flex items-center gap-8">
          {navLinks.map((link) => (
            <a
              key={link.href}
              href={link.href}
              className="text-white/80 hover:text-padel text-sm font-medium tracking-wide transition-colors"
            >
              {link.label}
            </a>
          ))}
          <a
            href="#contatti"
            className="bg-tennis hover:bg-tennis-light text-white px-5 py-2 rounded-full text-sm font-semibold transition-all hover:shadow-lg hover:shadow-tennis/30"
          >
            Prenota Ora
          </a>
        </div>

        {/* Mobile Hamburger */}
        <button
          onClick={() => setMenuOpen(!menuOpen)}
          className="lg:hidden text-white p-2"
          aria-label="Menu"
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            {menuOpen ? (
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            ) : (
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            )}
          </svg>
        </button>
      </div>

      {/* Mobile Menu */}
      {menuOpen && (
        <div className="lg:hidden bg-dark/95 backdrop-blur-md border-t border-white/10 animate-fade-in">
          <div className="px-4 py-4 space-y-3">
            {navLinks.map((link) => (
              <a
                key={link.href}
                href={link.href}
                onClick={() => setMenuOpen(false)}
                className="block text-white/80 hover:text-padel py-2 text-base font-medium transition-colors"
              >
                {link.label}
              </a>
            ))}
            <a
              href="#contatti"
              onClick={() => setMenuOpen(false)}
              className="block text-center bg-tennis hover:bg-tennis-light text-white px-5 py-3 rounded-full font-semibold transition-all mt-4"
            >
              Prenota Ora
            </a>
          </div>
        </div>
      )}
    </nav>
  );
}
