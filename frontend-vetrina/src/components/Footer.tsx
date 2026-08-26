import Image from "next/image";
import Link from "next/link";
import { siteConfig } from "@/config/site";

export default function Footer() {
  return (
    <footer className="bg-dark py-16">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8 lg:gap-12 mb-12">
          {/* Brand */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <Image src="/images/logo_circolo-removebg-preview.png" alt="Logo" width={50} height={50} className="text-2xl" />
              <span className="text-white font-bold text-xl">{siteConfig.clubShortName}</span>
            </div>
            <p className="text-white/50 text-sm leading-relaxed">
              {siteConfig.clubName} — Dal {siteConfig.foundedYear}, {siteConfig.clubSlogan.toLowerCase()}.
              {" "}Tennis, Padel e la {siteConfig.academyName}.
            </p>
          </div>

          {/* Links */}
          <div>
            <h4 className="text-white font-semibold mb-4">Link Rapidi</h4>
            <ul className="space-y-2">
              {[
                { label: "I Nostri Campi", href: "/strutture" },
                { label: "Tariffe", href: "/accademia" },
                { label: "Contatti", href: "/contatti" },
                { label: "Privacy Policy", href: "/privacy" },
              ].map((link) => (
                <li key={link.href}>
                  <Link
                    href={link.href}
                    className="text-white/50 hover:text-secondary text-sm transition-colors"
                  >
                    {link.label}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* Social */}
          <div>
            <h4 className="text-white font-semibold mb-4">Seguici Su</h4>
            <div className="flex flex-col gap-3">
              {siteConfig.social.instagram && (
                <a
                  href={siteConfig.social.instagram}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-2.5 text-white/60 hover:text-pink-400 text-sm transition-colors group"
                >
                  <span className="w-8 h-8 rounded-lg bg-white/5 group-hover:bg-pink-500/10 flex items-center justify-center transition-colors">
                    <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/>
                    </svg>
                  </span>
                  Instagram
                </a>
              )}
              {siteConfig.social.facebook && (
                <a
                  href={siteConfig.social.facebook}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-2.5 text-white/60 hover:text-blue-400 text-sm transition-colors group"
                >
                  <span className="w-8 h-8 rounded-lg bg-white/5 group-hover:bg-blue-500/10 flex items-center justify-center transition-colors">
                    <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M9 8H6v4h3v12h5V12h3.642L18 8h-4V6.333C14 5.374 14.5 5 15.5 5H18V0h-3.808C10.592 0 9 1.583 9 4.615V8z"/>
                    </svg>
                  </span>
                  Facebook
                </a>
              )}
            </div>
          </div>

          {/* Orari */}
          <div>
            <h4 className="text-white font-semibold mb-4">Orari di Apertura</h4>
            <div className="space-y-2 text-sm">
              {Object.values(siteConfig.hours).map((h) => (
                <div key={h.label} className="flex justify-between text-white/50">
                  <span>{h.label}</span>
                  <span className="text-white/70">{h.time}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Divider + Copyright + Privacy */}
        <div className="border-t border-white/10 pt-8 flex flex-col md:flex-row items-center justify-between gap-4">
          <div className="flex flex-wrap items-center gap-x-4 gap-y-2 text-white/40 text-sm">
            <span>© {new Date().getFullYear()} {siteConfig.clubName}. Tutti i diritti riservati.</span>
            <span className="hidden sm:inline text-white/20">|</span>
            <Link href="/privacy" className="hover:text-secondary transition-colors underline-offset-4 hover:underline">
              Informativa Privacy
            </Link>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-white/30 text-xs">
              Powered by {siteConfig.academyShortName} — {siteConfig.academyName}
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
}
