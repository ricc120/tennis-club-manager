-- schema.sql
-- Definizione dello schema del database

-- Tipo enumerato per il ruolo dell'utente
CREATE TYPE ruolo_utente AS ENUM ('ADMIN', 'MAESTRO', 'SOCIO','ALLIEVO','MANUTENTORE');

-- Tabella Utente
CREATE TABLE utente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    ruolo ruolo_utente NOT NULL
);

-- Tabella Campo
CREATE TABLE campo (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo_superficie VARCHAR(50) NOT NULL, -- Es: Terra, Erba, Cemento
    is_coperto BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tabella Prenotazione
CREATE TABLE prenotazione (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    ora_inizio TIME NOT NULL,
    id_campo INTEGER NOT NULL REFERENCES campo(id) ON DELETE CASCADE,
    id_socio INTEGER NOT NULL REFERENCES utente(id) ON DELETE CASCADE
);

-- Tabella Manutenzione
CREATE TABLE manutenzione (
    id SERIAL PRIMARY KEY,
    id_campo INTEGER NOT NULL REFERENCES campo(id) ON DELETE CASCADE,
    id_manutentore INTEGER NOT NULL REFERENCES utente(id) ON DELETE CASCADE,
    data_inizio DATE NOT NULL,
    data_fine DATE,
    descrizione TEXT NOT NULL,
    stato VARCHAR(50) NOT NULL DEFAULT 'IN_CORSO' -- IN_CORSO, COMPLETATA, ANNULLATA
);
