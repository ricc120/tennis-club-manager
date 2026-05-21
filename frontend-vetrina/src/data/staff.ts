export interface MembroStaff {
  nome: string;
  ruolo: string;
  bio: string;
  immagine: string;
}

export const staff: MembroStaff[] = [
  {
    nome: "Marco Stefanini",
    ruolo: "Direttore Tecnico — S.T.A.",
    bio: "Ex professionista ATP, 20 anni di esperienza nell'insegnamento. Fondatore della Stefanini Tennis Academy.",
    immagine: "https://placehold.co/400x400/2C5F2D/FFFFFF?text=M.+Stefanini",
  },
  {
    nome: "Laura Bianchi",
    ruolo: "Maestro Nazionale FIT",
    bio: "Specializzata nella formazione giovanile e nell'avviamento al tennis per bambini dai 4 ai 10 anni.",
    immagine: "https://placehold.co/400x400/4A8B4C/FFFFFF?text=L.+Bianchi",
  },
  {
    nome: "Andrea Rossi",
    ruolo: "Istruttore Padel",
    bio: "Certificato FIT Padel, organizzatore dei tornei sociali del club e delle cliniche per principianti.",
    immagine: "https://placehold.co/400x400/00B8D4/FFFFFF?text=A.+Rossi",
  },
  {
    nome: "Giulia Conti",
    ruolo: "Preparatore Atletico",
    bio: "Laureata in Scienze Motorie, segue la preparazione fisica degli atleti della S.T.A.",
    immagine: "https://placehold.co/400x400/FF6D00/FFFFFF?text=G.+Conti",
  },
];
