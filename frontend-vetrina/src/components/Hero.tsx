export default function Hero() {
  return (
    <section className="relative h-screen flex items-center justify-center overflow-hidden">
      {/* Background Image */}
      <div
        className="absolute inset-0 bg-cover bg-center bg-no-repeat"
        style={{
          backgroundImage: "url('https://placehold.co/1920x1080/2C5F2D/1A1A2E?text=TC+Carmignano')",
        }}
      />
      {/* Gradient Overlay */}
      <div className="absolute inset-0 bg-gradient-to-b from-dark/70 via-dark/50 to-dark/80" />

      {/* Content */}
      <div className="relative z-10 text-center px-4 max-w-4xl mx-auto">
        <p className="text-padel font-medium tracking-[0.3em] uppercase text-sm mb-4 animate-fade-in">
          Dal 1985 — Carmignano, Prato
        </p>
        <h1 className="text-5xl sm:text-6xl lg:text-7xl font-bold text-white leading-tight mb-6 animate-fade-in-up">
          Tennis Club
          <br />
          <span className="text-tennis-light">Carmignano</span>
        </h1>
        <p className="text-white/70 text-lg sm:text-xl max-w-2xl mx-auto mb-10 animate-fade-in-up delay-200">
          Tradizione e innovazione nel cuore della Toscana.
          Tennis, Padel e la Stefanini Tennis Academy per il tuo gioco migliore.
        </p>

        {/* CTAs */}
        <div className="flex flex-col sm:flex-row gap-4 justify-center animate-fade-in-up delay-300">
          <a
            href="#chi-siamo"
            className="bg-tennis hover:bg-tennis-light text-white px-8 py-4 rounded-full text-lg font-semibold transition-all hover:shadow-xl hover:shadow-tennis/30 hover:-translate-y-0.5"
          >
            Scopri il Club
          </a>
          <a
            href="#tariffe"
            className="border-2 border-white/30 hover:border-padel text-white hover:text-padel px-8 py-4 rounded-full text-lg font-semibold transition-all hover:-translate-y-0.5"
          >
            Vedi Tariffe
          </a>
        </div>
      </div>

      {/* Scroll indicator */}
      <div className="absolute bottom-8 left-1/2 -translate-x-1/2 animate-bounce">
        <svg className="w-6 h-6 text-white/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" />
        </svg>
      </div>
    </section>
  );
}
