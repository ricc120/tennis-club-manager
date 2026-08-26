import { siteConfig } from "@/config/site";

export default function PrivacyPolicy() {
    return (
        <main className="min-h-screen pt-32 pb-20 bg-gray-50">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="bg-white p-8 md:p-12 rounded-2xl shadow-sm prose prose-blue max-w-none text-gray-700">

                    <h1 className="text-3xl font-bold text-gray-900 mb-8">Informativa sulla Privacy</h1>
                    <p className="text-sm text-gray-500 mb-8">Ultimo aggiornamento: Febbraio 2026</p>

                    <p>
                        Benvenuto sul sito del <strong>{siteConfig.clubName} / {siteConfig.academyName}</strong>.
                        La tutela della tua privacy è per noi fondamentale. In questa pagina ti spieghiamo
                        come gestiamo i dati personali in conformità al Regolamento Europeo 2016/679 (GDPR).
                    </p>

                    <h2 className="text-xl font-semibold text-gray-900 mt-8 mb-4">1. Titolare del Trattamento</h2>
                    <p>
                        Il Titolare del trattamento dei dati è il <strong>{siteConfig.clubName}</strong>,
                        con sede in {siteConfig.address}.<br />
                        Per qualsiasi richiesta relativa alla privacy, puoi contattarci all&apos;indirizzo email:{" "}
                        <a href={`mailto:${siteConfig.email}`} className="text-primary hover:underline font-medium">
                            {siteConfig.email}
                        </a>.
                    </p>

                    <h2 className="text-xl font-semibold text-gray-900 mt-8 mb-4">2. Quali dati raccogliamo e perché</h2>
                    <p>
                        Questo sito è una vetrina informativa e <strong>non utilizza moduli di contatto, né richiede registrazioni</strong>.
                        Pertanto, non raccogliamo attivamente dati personali come nomi, numeri di telefono o email tramite il sito web.
                    </p>
                    <p>
                        <strong>Dati di navigazione:</strong> I sistemi informatici che permettono il funzionamento di questo sito
                        (ospitato sui server di Vercel) acquisiscono automaticamente, nel corso del loro normale esercizio,
                        alcuni dati la cui trasmissione è implicita nell'uso dei protocolli di Internet (es. indirizzi IP).
                        Questi dati vengono utilizzati esclusivamente per garantire il corretto funzionamento tecnico del sito e per la sicurezza informatica,
                        senza essere associati a utenti identificati.
                    </p>

                    <h2 className="text-xl font-semibold text-gray-900 mt-8 mb-4">3. Cookie</h2>
                    <p>
                        Questo sito web è progettato per rispettare la tua privacy fin dalla progettazione (Privacy by Design).
                        <strong>Non utilizziamo cookie di profilazione o tracciamento a scopo pubblicitario</strong>.
                        Potrebbero essere utilizzati esclusivamente cookie tecnici di sessione, strettamente necessari al
                        funzionamento dell'infrastruttura di hosting.
                    </p>

                    <h2 className="text-xl font-semibold text-gray-900 mt-8 mb-4">4. Collegamenti a servizi esterni</h2>
                    <p>
                        Sulle nostre pagine sono presenti collegamenti (link) per contattarci direttamente tramite
                        client di posta elettronica o tramite l'applicazione WhatsApp. Cliccando su tali collegamenti,
                        abbandonerai il nostro sito e il trattamento dei tuoi dati sarà soggetto alle informative sulla privacy
                        dei rispettivi fornitori dei servizi (es. Meta per WhatsApp).
                    </p>

                    <h2 className="text-xl font-semibold text-gray-900 mt-8 mb-4">5. I tuoi diritti</h2>
                    <p>
                        Ai sensi degli artt. 15 e seguenti del GDPR, hai il diritto di chiedere in qualunque momento l'accesso
                        ai tuoi dati personali, la rettifica, la cancellazione degli stessi o la limitazione del trattamento.
                        Puoi esercitare questi diritti inviando una comunicazione ai recapiti indicati al punto 1.
                    </p>

                </div>
            </div>
        </main>
    );
}