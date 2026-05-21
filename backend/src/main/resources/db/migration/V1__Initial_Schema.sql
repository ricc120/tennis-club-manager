-- V1__Initial_Schema.sql
-- Definizione dello schema del database

-- Tipo enumerato per il ruolo dell'utente
DO $$ BEGIN
    CREATE TYPE ruolo_utente AS ENUM ('ADMIN', 'MAESTRO', 'SOCIO','ALLIEVO','MANUTENTORE');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Tabella Utente
CREATE TABLE IF NOT EXISTS utente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    ruolo ruolo_utente NOT NULL
);

-- Tabella Campo
CREATE TABLE IF NOT EXISTS campo (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo_superficie VARCHAR(50) NOT NULL, -- Es: Terra, Erba, Cemento
    is_coperto BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tabella Prenotazione
CREATE TABLE IF NOT EXISTS prenotazione (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    ora_inizio TIME NOT NULL,
    id_campo INTEGER NOT NULL REFERENCES campo(id) ON DELETE CASCADE,
    id_socio INTEGER NOT NULL REFERENCES utente(id) ON DELETE CASCADE,
    CONSTRAINT unique_booking_slot UNIQUE (id_campo, data, ora_inizio)
);

-- Tabella Manutenzione
CREATE TABLE IF NOT EXISTS manutenzione (
    id SERIAL PRIMARY KEY,
    id_campo INTEGER NOT NULL REFERENCES campo(id) ON DELETE CASCADE,
    id_manutentore INTEGER NOT NULL REFERENCES utente(id) ON DELETE CASCADE,
    data_inizio DATE NOT NULL,
    data_fine DATE,
    descrizione TEXT NOT NULL,
    stato VARCHAR(50) NOT NULL DEFAULT 'IN_CORSO' -- IN_CORSO, COMPLETATA, ANNULLATA
);

-- Tabella Lezione
CREATE TABLE IF NOT EXISTS lezione (
    id SERIAL PRIMARY KEY,
    id_prenotazione INTEGER NOT NULL UNIQUE REFERENCES prenotazione(id) ON DELETE CASCADE,
    id_maestro INTEGER NOT NULL REFERENCES utente(id) ON DELETE CASCADE,
    descrizione TEXT
);

-- Tabella Allievo Lezione
CREATE TABLE IF NOT EXISTS allievo_lezione (
    id SERIAL PRIMARY KEY,
    id_lezione INTEGER NOT NULL REFERENCES lezione(id) ON DELETE CASCADE,
    id_allievo INTEGER NOT NULL REFERENCES utente(id) ON DELETE CASCADE,
    presente BOOLEAN DEFAULT TRUE,
    feedback TEXT, -- Feedback specifico del maestro per questo allievo
    data_iscrizione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_lezione, id_allievo)  -- Un allievo non può essere iscritto due volte alla stessa lezione
);
