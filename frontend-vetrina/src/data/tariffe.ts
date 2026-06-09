export interface Tariffa {
  nome: string;
  prezzo: string;
  periodo: string;
  dettagli: string[];
  evidenziato?: boolean;
  categoria: "tennis" | "padel" | "academy";
}

export const tariffe: Tariffa[] = [
  // Tennis
  {
    nome: "Campo Tennis — Singolo",
    prezzo: "€18",
    periodo: "/ ora",
    dettagli: ["1 campo in terra rossa", "Illuminazione inclusa", "Docce e spogliatoi"],
    categoria: "tennis",
  },
  {
    nome: "Campo Tennis — Abbonamento",
    prezzo: "€120",
    periodo: "/ mese",
    dettagli: ["8 ore mensili garantite", "Prenotazione prioritaria", "Accesso spogliatoi premium"],
    evidenziato: true,
    categoria: "tennis",
  },
  {
    nome: "Corso Tennis Adulti",
    prezzo: "€250",
    periodo: "/ trimestre",
    dettagli: ["2 lezioni settimanali (1h)", "Gruppi max 4 persone", "Maestro federale"],
    categoria: "tennis",
  },
  // Padel
  {
    nome: "Campo Padel — Singolo",
    prezzo: "€24",
    periodo: "/ ora",
    dettagli: ["1 campo panoramico", "Illuminazione LED", "Racchette disponibili al noleggio"],
    categoria: "padel",
  },
  {
    nome: "Campo Padel — Abbonamento",
    prezzo: "€160",
    periodo: "/ mese",
    dettagli: ["8 ore mensili garantite", "Tornei sociali inclusi", "Sconto 10% pro shop"],
    evidenziato: true,
    categoria: "padel",
  },
  // Academy
  {
    nome: "S.T.A. — Agonisti Junior",
    prezzo: "€450",
    periodo: "/ mese",
    dettagli: ["5 allenamenti settimanali", "Preparazione atletica inclusa", "Accompagnamento tornei"],
    evidenziato: true,
    categoria: "academy",
  },
  {
    nome: "S.T.A. — Pre-agonistica",
    prezzo: "€280",
    periodo: "/ mese",
    dettagli: ["3 allenamenti settimanali", "Valutazione tecnica mensile", "Mini-tornei interni"],
    categoria: "academy",
  },
];
