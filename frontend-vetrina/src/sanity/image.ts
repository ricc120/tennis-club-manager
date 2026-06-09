/**
 * sanity/image.ts — Utility per generare URL delle immagini Sanity.
 *
 * CONCETTO: Sanity Image URL Builder
 *
 * In Sanity, le immagini sono memorizzate come "asset references"
 * (oggetti con _type: "image" e un riferimento all'asset).
 * Per ottenere un URL utilizzabile in <img> o <Image>, serve
 * il pacchetto @sanity/image-url che:
 *
 * 1. Converte il riferimento in un URL del CDN Sanity
 * 2. Permette trasformazioni on-the-fly (resize, crop, quality)
 * 3. Supporta hotspot/crop definiti nello Studio
 *
 * UTILIZZO:
 *   import { urlFor } from "@/sanity/image";
 *
 *   // URL base
 *   urlFor(gara.fotoCopertina).url()
 *
 *   // Con dimensioni specifiche
 *   urlFor(gara.fotoCopertina).width(800).height(400).url()
 *
 *   // Con qualità e formato
 *   urlFor(gara.fotoCopertina).width(800).quality(80).format("webp").url()
 */

import imageUrlBuilder from "@sanity/image-url";
import { sanityConfig } from "./client";

/**
 * Builder configurato con il projectId e dataset del progetto.
 * Non usa il client direttamente per evitare la lazy initialization —
 * il builder ha bisogno solo di projectId e dataset, non del client completo.
 */
const builder = imageUrlBuilder({
  projectId: sanityConfig.projectId,
  dataset: sanityConfig.dataset,
});

/**
 * Genera un URL builder per un'immagine Sanity.
 *
 * @param source - Il riferimento immagine dal documento Sanity
 *                 (es. gara.fotoCopertina)
 * @returns Un builder con metodi chainable (.width(), .height(), .url(), ecc.)
 *
 * ESEMPIO:
 *   const url = urlFor(gara.fotoCopertina).width(600).height(400).url();
 *   <Image src={url} alt="..." width={600} height={400} />
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function urlFor(source: any) {
  return builder.image(source);
}
