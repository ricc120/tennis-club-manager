import { siteConfig } from "@/config/site";

export const metadata = {
  title: `Informativa sulla Privacy | ${siteConfig.clubName}`,
  description: `Informativa sul trattamento dei dati personali e cookie policy di ${siteConfig.clubName} in conformità al GDPR.`,
};

export default function PrivacyPolicy() {
  return (
            <div className="bg-[#FAF9F5] text-[#182219]">
              {/* 1. Page Header with Heritage Green Banner (Coerente con tutte le pagine interne) */}
              <section className="pt-36 pb-20 lg:pt-44 lg:pb-24 bg-[#1B3C1F] text-white relative overflow-hidden">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10">
                  <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/10 text-[#F7E4D3] border border-white/20 text-xs font-semibold uppercase tracking-widest mb-4">
                    Trasparenza &amp; Tutela GDPR
                  </span>
                  <h1
                    className="text-4xl sm:text-6xl font-bold text-white mb-6"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    Informativa sulla Privacy
                  </h1>
                    <p className="text-[#DEE9DF] text-lg sm:text-xl max-w-2xl mx-auto font-light leading-relaxed">
                      In questa pagina ti spieghiamo come tuteliamo e trattiamo i tuoi dati personali in conformità al Regolamento Europeo 2016/679 (GDPR).
                    </p>
                </div>
              </section>

              {/* 2. Policy Content Card */}
              <section className="py-20 lg:py-28 max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="editorial-card p-8 md:p-14 rounded-3xl text-[#5A695C] leading-relaxed font-normal bg-white">
                  <div className="flex items-center justify-between border-b border-[#E5E0D4] pb-4 mb-8">
                    <span className="status-pill">
                      Conformità GDPR (UE 2016/679)
                    </span>
                    <span className="text-xs text-gray-400">
                      Ultimo aggiornamento: Febbraio 2026
                    </span>
                  </div>

                  <p className="mb-6">
                    Benvenuto sul sito del <strong className="text-[#182219] font-semibold">{siteConfig.clubName} / {siteConfig.academyName}</strong>.
                    La tutela della tua privacy è per noi fondamentale. In questa pagina ti spieghiamo
                    come gestiamo i dati personali in conformità al Regolamento Europeo 2016/679 (GDPR).
                  </p>

                  <h2
                    className="text-xl font-bold text-[#1B3C1F] mt-8 mb-3 pb-2 border-b border-[#E5E0D4]"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    1. Titolare del Trattamento
                  </h2>
                  <p className="mb-6">
                    Il Titolare del trattamento dei dati è il <strong className="text-[#182219] font-semibold">{siteConfig.clubName}</strong>,
                    con sede in {siteConfig.address}.<br />
                    Per qualsiasi richiesta relativa alla privacy, puoi contattarci all&apos;indirizzo email:{" "}
                    <a href={`mailto:${siteConfig.email}`} className="text-[#C05621] hover:underline font-medium">
                      {siteConfig.email}
                    </a>.
                  </p>

                  <h2
                    className="text-xl font-bold text-[#1B3C1F] mt-8 mb-3 pb-2 border-b border-[#E5E0D4]"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    2. Quali dati raccogliamo e perché
                  </h2>
                  <p className="mb-4">
                    Questo sito è una vetrina informativa e <strong className="text-[#182219] font-semibold">non utilizza moduli di contatto invasivi, né richiede registrazioni</strong>.
                    Pertanto, non raccogliamo attivamente dati personali tramite form o profilazione pubblicitaria.
                  </p>
                  <p className="mb-6 text-[#5A695C] text-sm">
                    <strong className="text-[#182219] font-semibold">Dati di navigazione:</strong> I sistemi informatici che permettono il funzionamento di questo sito
                    acquisiscono automaticamente, nel corso del loro normale esercizio,
                    alcuni dati tecnici la cui trasmissione è implicita nell&apos;uso dei protocolli di Internet (es. indirizzi IP).
                    Questi dati vengono utilizzati esclusivamente per garantire il corretto funzionamento tecnico e la sicurezza informatica.
                  </p>

                  <h2
                    className="text-xl font-bold text-[#1B3C1F] mt-8 mb-3 pb-2 border-b border-[#E5E0D4]"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    3. Cookie e Servizi di Terze Parti
                  </h2>
                  <p className="mb-6 text-sm">
                    Questo sito non utilizza cookie di profilazione o tracciamento a fini commerciali.
                    Vengono utilizzati unicamente cookie tecnici strettamente necessari alla navigazione e alle sessioni di visualizzazione.
                  </p>

                  <h2
                    className="text-xl font-bold text-[#1B3C1F] mt-8 mb-3 pb-2 border-b border-[#E5E0D4]"
                    style={{ fontFamily: "var(--font-serif)" }}
                  >
                    4. Diritti dell&apos;Utente
                  </h2>
                  <p className="text-sm">
                    In qualsiasi momento puoi richiedere l&apos;accesso ai tuoi dati personali, la loro rettifica, cancellazione o limitazione scrivendo a{" "}
                      <a href={`mailto:${siteConfig.email}`} className="text-[#C05621] hover:underline font-medium">
                        {siteConfig.email}
                      </a>.
                  </p>
                </div>
        </section>
      </div>
      );
}
