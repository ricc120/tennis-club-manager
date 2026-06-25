/**
 * config/site.ts — Configurazione White-Label del sito vetrina.
 *
 * CONCETTO: White-Label Pattern
 *
 * Un template "white-label" è un prodotto software progettato per essere
 * riutilizzato da più clienti, ciascuno con il proprio branding.
 *
 * Invece di scrivere "TC Carmignano" direttamente nei componenti React,
 * centralizziamo TUTTI i dati specifici del cliente in questo file.
 *
 * Per adattare il sito a un nuovo circolo basta:
 *   1. Modificare questo file con i dati del nuovo cliente
 *   2. Aggiornare le CSS Variables in globals.css per i colori
 *   3. Rifare il build → sito pronto
 *
 * NESSUN componente React deve contenere riferimenti hardcoded al nome
 * del club, all'indirizzo, ai recapiti, ecc. Tutto viene da qui.
 */

export const siteConfig = {
  // ========================
  // BRAND
  // ========================
  /** Nome completo del circolo (es. per titoli e testi lunghi) */
  clubName: "Circolo Tennis Colli Alti",
  /** Nome abbreviato (es. per navbar e badge) */
  clubShortName: "CT Colli Alti",
  /** Slogan principale del circolo */
  clubSlogan: "Tennis tra le colline del Chianti",
  /** Anno di fondazione */
  foundedYear: 1992,
  /** Città e provincia */
  location: "Impruneta, Firenze",
  /** Regione (usata nei testi descrittivi) */
  region: "Toscana",

  // ========================
  // CORSI (generico, no academy branding)
  // ========================
  /** Nome della sezione corsi (generico) */
  academyName: "I Nostri Corsi",
  /** Acronimo (vuoto = non mostrato) */
  academyShortName: "",

  // ========================
  // CONTATTI
  // ========================
  address: "Via dei Colli 18, 50023 Impruneta (FI)",
  phone: "+39 055 231 4500",
  email: "info@ctcollialti.it",
  whatsappUrl: "https://wa.me/390552314500",

  // ========================
  // ORARI
  // ========================
  hours: {
    weekday: { label: "Lunedì – Venerdì", time: "8:00 – 22:00" },
    weekend: { label: "Sabato – Domenica", time: "8:00 – 20:00" },
    office: { label: "Segreteria", time: "9:00 – 19:00" },
  },

  // ========================
  // SOCIAL (predisposto per il futuro)
  // ========================
  social: {
    facebook: "",
    instagram: "",
    youtube: "",
  },

  // ========================
  // SEO
  // ========================
  seo: {
    title: "CT Colli Alti — Tennis tra le colline del Chianti | Impruneta",
    description:
      "Circolo Tennis Colli Alti: campi panoramici e corsi per ogni età nel verde delle colline del Chianti a Impruneta. Prenota il tuo campo.",
    keywords:
      "tennis, impruneta, firenze, toscana, chianti, corsi tennis, Colli Alti",
    ogTitle: "CT Colli Alti — Tennis nel Chianti",
    ogDescription: "Dal 1992, tennis tra le colline del Chianti.",
  },

  // ========================
  // IMMAGINI PLACEHOLDER
  // ========================
  images: {
    heroBackground:
      "https://placehold.co/1920x1080/1B4332/FFFFFF?text=CT+Colli+Alti",
  },

  // ========================
  // STRUTTURE (dati per la sezione Campi)
  // ========================
  stats: {
    years: "30+",
    courts: "8",
    members: "350+",
    coaches: "10+",
  },

  // ========================
  // MOCK GARE (dati demo per la sezione Risultati)
  // ========================
  /** Gare di esempio mostrate quando Sanity non è configurato.
   *  Cambiare questi dati per ogni nuovo cliente white-label. */
  mockGare: [
    {
      _id: "mock-1",
      data: "2026-05-18T15:00:00Z",
      campionato: "Serie C Maschile",
      giornata: 12,
      squadraCasa: "TC Carmignano",
      squadraOspite: "CT Firenze",
      punteggioCasa: 4,
      punteggioOspite: 2,
      inCasa: true,
    },
    {
      _id: "mock-2",
      data: "2026-05-11T10:00:00Z",
      campionato: "Serie C Maschile",
      giornata: 11,
      squadraCasa: "CT Prato",
      squadraOspite: "TC Carmignano",
      punteggioCasa: 3,
      punteggioOspite: 3,
      inCasa: false,
    },
    {
      _id: "mock-3",
      data: "2026-05-04T15:00:00Z",
      campionato: "Serie D1 Femminile",
      giornata: 10,
      squadraCasa: "TC Carmignano",
      squadraOspite: "TC Empoli",
      punteggioCasa: 5,
      punteggioOspite: 1,
      inCasa: true,
    },
    {
      _id: "mock-4",
      data: "2026-04-27T10:00:00Z",
      campionato: "Serie C Maschile",
      giornata: 10,
      squadraCasa: "TC Carmignano",
      squadraOspite: "CT Montecatini",
      punteggioCasa: 4,
      punteggioOspite: 2,
      inCasa: true,
    },
    {
      _id: "mock-5",
      data: "2026-04-20T15:00:00Z",
      campionato: "Serie D1 Femminile",
      giornata: 9,
      squadraCasa: "CT Scandicci",
      squadraOspite: "TC Carmignano",
      punteggioCasa: 4,
      punteggioOspite: 2,
      inCasa: false,
    },
  ],

  // ========================
  // STAFF (per la pagina /club)
  // ========================
  staff: [
    { name: "Marco Rossi", role: "Direttore Tecnico", photo: "https://placehold.co/400x500/2C5F2D/FFFFFF?text=MR" },
    { name: "Elena Bianchi", role: "Maestra Nazionale FIT", photo: "https://placehold.co/400x500/2C5F2D/FFFFFF?text=EB" },
    { name: "Luca Verdi", role: "Maestro Padel", photo: "https://placehold.co/400x500/00B4D8/FFFFFF?text=LV" },
    { name: "Giulia Neri", role: "Preparatrice Atletica", photo: "https://placehold.co/400x500/FF6B35/FFFFFF?text=GN" },
    { name: "Andrea Conti", role: "Maestro Agonistica", photo: "https://placehold.co/400x500/2C5F2D/FFFFFF?text=AC" },
    { name: "Sara Martini", role: "Coordinatrice SAT", photo: "https://placehold.co/400x500/FF6B35/FFFFFF?text=SM" },
  ],

  // ========================
  // CORSI (per la pagina /accademia)
  // ========================
  courses: [
    {
      name: "Bambini",
      subtitle: "Dai 4 ai 12 anni",
      price: "€XX",
      period: "/mese",
      features: [
        "2 lezioni settimanali",
        "Gruppi da max 6 allievi",
        "Attrezzatura inclusa",
        "Tornei interni stagionali",
      ],
      highlighted: false,
    },
    {
      name: "Adulti",
      subtitle: "Tutti i livelli",
      price: "€XX",
      period: "/mese",
      features: [
        "2-3 lezioni settimanali",
        "Gruppi per livello",
        "Accesso libero ai campi",
        "Partecipazione tornei sociali",
        "Analisi video del gioco",
      ],
      highlighted: true,
    },
    {
      name: "Agonistica",
      subtitle: "Under 14 / Under 18",
      price: "€XX",
      period: "/mese",
      features: [
        "5 allenamenti settimanali",
        "Preparazione atletica dedicata",
        "Supporto tornei FIT/TPRA",
        "Mental coaching",
        "Analisi match con video",
        "Programmazione annuale",
      ],
      highlighted: false,
    },
  ],

  // ========================
  // STRUTTURE (per la pagina /strutture)
  // ========================
  strutture: [
    { title: "Campo Centrale", category: "Tennis", photo: "https://placehold.co/800x600/1B4332/FFFFFF?text=Campo+Centrale", tall: true },
    { title: "Clubhouse", category: "Clubhouse", photo: "https://placehold.co/800x600/1A1A2E/FFFFFF?text=Clubhouse", tall: true },
    { title: "Ristorante", category: "Ristorante", photo: "https://placehold.co/600x400/FF6B35/FFFFFF?text=Ristorante", tall: false },
    { title: "Palestra", category: "Fitness", photo: "https://placehold.co/600x400/1A1A2E/FFFFFF?text=Palestra", tall: false },
    { title: "Piscina", category: "Piscina", photo: "https://placehold.co/800x600/2EC4B6/FFFFFF?text=Piscina", tall: true },
    { title: "Campi in Terra Rossa", category: "Tennis", photo: "https://placehold.co/600x400/1B4332/FFFFFF?text=Terra+Rossa", tall: false },
  ],
} as const;

/**
 * Tipo derivato dalla configurazione.
 * Utile per tipizzare props di componenti che ricevono parti del config.
 */
export type SiteConfig = typeof siteConfig;
