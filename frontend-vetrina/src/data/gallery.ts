export interface FotoGallery {
  src: string;
  alt: string;
  categoria: "tennis" | "padel" | "academy" | "club";
}

export const galleria: FotoGallery[] = [
  { src: "https://placehold.co/800x600/2C5F2D/FFFFFF?text=Campo+Terra+Rossa", alt: "Campo in terra rossa", categoria: "tennis" },
  { src: "https://placehold.co/800x600/00B8D4/FFFFFF?text=Campo+Padel", alt: "Campo padel panoramico", categoria: "padel" },
  { src: "https://placehold.co/800x600/FF6D00/FFFFFF?text=Allenamento+S.T.A.", alt: "Allenamento accademia", categoria: "academy" },
  { src: "https://placehold.co/800x600/1A1A2E/FFFFFF?text=Club+House", alt: "Club house esterno", categoria: "club" },
  { src: "https://placehold.co/800x600/4A8B4C/FFFFFF?text=Torneo+Sociale", alt: "Torneo sociale", categoria: "tennis" },
  { src: "https://placehold.co/800x600/00E5FF/1A1A2E?text=Padel+Notturno", alt: "Padel in notturna", categoria: "padel" },
  { src: "https://placehold.co/800x600/E65100/FFFFFF?text=Junior+Training", alt: "Allenamento junior", categoria: "academy" },
  { src: "https://placehold.co/800x600/2C5F2D/FFFFFF?text=Vista+Aerea", alt: "Vista aerea del club", categoria: "club" },
  { src: "https://placehold.co/800x600/4A8B4C/FFFFFF?text=Lezione+Gruppo", alt: "Lezione di gruppo", categoria: "tennis" },
  { src: "https://placehold.co/800x600/00B8D4/FFFFFF?text=Finale+Torneo", alt: "Finale torneo padel", categoria: "padel" },
];
