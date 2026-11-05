import Image from "next/image";
import Link from "next/link";
import { siteConfig } from "@/config/site";

export default function Footer() {
  return (
    <footer className="bg-[#112614] text-white border-t border-[#1B3C1F] pt-16 pb-12">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-10 lg:gap-12 mb-16">
          {/* Brand */}
          <div className="space-y-4">
            <Link href="/" className="flex items-center gap-3 group">
              <div className="w-10 h-10 rounded-full bg-white flex items-center justify-center p-1 shadow-xs">
                <Image
                  src="/images/logo_circolo-removebg-preview.png"
                  alt={`Logo ${siteConfig.clubName}`}
                  width={40}
                  height={40}
                  className="object-contain"
                />
              </div>
              <span
                className="text-white font-bold text-lg"
                style={{ fontFamily: "var(--font-serif)" }}
              >
                {siteConfig.clubShortName}
              </span>
            </Link>
            <p className="text-[#DEE9DF]/80 text-sm leading-relaxed font-light">
              {siteConfig.clubName} — Dal {siteConfig.foundedYear}, {siteConfig.clubSlogan.toLowerCase()}.
              Campi in terra battuta, padel panoramico e la {siteConfig.academyName}.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-white font-semibold text-xs uppercase tracking-widest mb-4">
              Navigazione Rapida
            </h4>
            <ul className="space-y-2.5">
              {[
                { label: "Il Club & La Storia", href: "/club" },
                { label: "Stefanini Tennis Academy", href: "/accademia" },
                { label: "Campi & Strutture", href: "/strutture" },
                { label: "Risultati Ufficiali FITP", href: "/#risultati" },
                { label: "Contatti & Segreteria", href: "/contatti" },
              ].map((link) => (
                <li key={link.href}>
                  <Link
                    href={link.href}
                    className="text-[#DEE9DF]/70 hover:text-white text-sm transition-colors"
                  >
                    {link.label}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* Social */}
          <div>
            <h4 className="text-white font-semibold text-xs uppercase tracking-widest mb-4">
              Social Media
            </h4>
            <div className="flex flex-col gap-3">
              {siteConfig.social.instagram && (
                <a
                  href={siteConfig.social.instagram}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="group inline-flex items-center gap-3 text-[#DEE9DF]/80 hover:text-[#DD2A7B] text-sm transition-colors group"
                >
                  <span className="w-8 h-8 rounded-lg bg-white/10 group-hover:bg-white/20 flex items-center justify-center transition-colors">
                    <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z" />
                    </svg>
                  </span>
                  <span>Instagram</span>
                </a>
              )}
              {siteConfig.social.facebook && (
                <a
                  href={siteConfig.social.facebook}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-3 text-[#DEE9DF]/80 hover:text-[#1877F2] text-sm transition-colors group"
                >
                  <span className="w-8 h-8 rounded-lg bg-white/10 group-hover:bg-white/20 flex items-center justify-center transition-colors">
                    <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M9 8H6v4h3v12h5V12h3.642L18 8h-4V6.333C14 5.374 14.5 5 15.5 5H18V0h-3.808C10.592 0 9 1.583 9 4.615V8z" />
                    </svg>
                  </span>
                  <span>Facebook</span>
                </a>
              )}
            </div>
          </div>

          {/* Orari */}
          <div>
            <h4 className="text-white font-semibold text-xs uppercase tracking-widest mb-4">
              Orari Segreteria
            </h4>
            <div className="space-y-2 text-sm">
              {Object.values(siteConfig.hours).map((h) => (
                <div key={h.label} className="flex justify-between text-[#DEE9DF]/70">
                  <span>{h.label}</span>
                  <span className="text-white font-medium">{h.time}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="border-t border-[#1B3C1F] pt-8 flex flex-col md:flex-row items-center justify-between gap-4 text-xs text-[#DEE9DF]/60">
          <div className="flex flex-wrap items-center gap-x-4 gap-y-2">
            <span>© {new Date().getFullYear()} {siteConfig.clubName}. Tutti i diritti riservati.</span>
            <span>|</span>
            <Link href="/privacy" className="hover:text-white transition-colors underline-offset-4 hover:underline">
              Informativa Privacy
            </Link>
          </div>
          <div>
            <span>Stefanini Tennis Academy • S.T.A.</span>
          </div>
        </div>
      </div>
    </footer>
  );
}
