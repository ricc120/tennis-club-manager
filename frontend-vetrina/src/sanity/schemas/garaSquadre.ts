/**
 * Schema Sanity: garaSquadre
 *
 * CONCETTO: Schema-driven CMS
 *
 * In Sanity.io, lo schema definisce la STRUTTURA dei contenuti.
 * È il contratto tra il CMS (dove l'editor inserisce i dati) e il
 * frontend (dove i dati vengono visualizzati).
 *
 * Ogni campo ha:
 *   - name: identificatore usato nel codice e nelle query GROQ
 *   - title: etichetta visibile nell'interfaccia dello Studio
 *   - type: tipo di dato (string, number, datetime, boolean, ...)
 *   - validation: regole di validazione (required, min, max, ...)
 *
 * QUERY GROQ CORRISPONDENTE:
 *   *[_type == "garaSquadre"] | order(data desc) [0...5] {
 *     _id, data, campionato, giornata,
 *     squadraCasa, squadraOspite,
 *     punteggioCasa, punteggioOspite,
 *     inCasa
 *   }
 */

import { defineType, defineField } from "sanity";

export default defineType({
  name: "garaSquadre",
  title: "Gara a Squadre",
  type: "document",

  fields: [
    defineField({
      name: "data",
      title: "Data della Gara",
      type: "datetime",
      validation: (rule) => rule.required(),
    }),
    defineField({
      name: "campionato",
      title: "Campionato",
      type: "string",
      description: 'Es. "Serie C Maschile", "Serie D1 Femminile"',
      validation: (rule) => rule.required(),
    }),
    defineField({
      name: "giornata",
      title: "Giornata",
      type: "number",
      description: "Numero della giornata di campionato",
      validation: (rule) => rule.required().min(1),
    }),
    defineField({
      name: "squadraCasa",
      title: "Squadra di Casa",
      type: "string",
      validation: (rule) => rule.required(),
    }),
    defineField({
      name: "squadraOspite",
      title: "Squadra Ospite",
      type: "string",
      validation: (rule) => rule.required(),
    }),
    defineField({
      name: "punteggioCasa",
      title: "Punteggio Casa",
      type: "number",
      validation: (rule) => rule.required().min(0),
    }),
    defineField({
      name: "punteggioOspite",
      title: "Punteggio Ospite",
      type: "number",
      validation: (rule) => rule.required().min(0),
    }),
    defineField({
      name: "inCasa",
      title: "Gara in Casa?",
      type: "boolean",
      description: "Se il circolo ha giocato in casa",
      initialValue: true,
    }),
  ],

  // Preview personalizzato nello Studio per facilitare la navigazione
  preview: {
    select: {
      data: "data",
      casa: "squadraCasa",
      ospite: "squadraOspite",
      pCasa: "punteggioCasa",
      pOspite: "punteggioOspite",
    },
    prepare({ data, casa, ospite, pCasa, pOspite }) {
      const dataStr = data
        ? new Date(data).toLocaleDateString("it-IT", { day: "2-digit", month: "short" })
        : "—";
      return {
        title: `${casa ?? "?"} vs ${ospite ?? "?"}`,
        subtitle: `${dataStr} · ${pCasa ?? "?"}-${pOspite ?? "?"}`,
      };
    },
  },

  // Ordinamento di default nello Studio: più recenti prima
  orderings: [
    {
      title: "Data (più recenti)",
      name: "dataDesc",
      by: [{ field: "data", direction: "desc" }],
    },
  ],
});
