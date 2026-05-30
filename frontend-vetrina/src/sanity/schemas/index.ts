/**
 * schemas/index.ts — Registro centrale degli schemi Sanity.
 *
 * Ogni nuovo tipo di documento va importato qui e aggiunto all'array.
 * Questo file viene referenziato da sanity.config.ts.
 *
 * Per aggiungere un nuovo schema:
 *   1. Crea il file in src/sanity/schemas/nomeSchema.ts
 *   2. Importalo qui
 *   3. Aggiungilo all'array schemaTypes
 */

import garaSquadre from "./garaSquadre";

export const schemaTypes = [garaSquadre];
