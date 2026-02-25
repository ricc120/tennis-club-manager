-- V2__Insert_Default_Data.sql
-- Inserimento dati di test

-- Inserimento Utenti (1 Admin, 1 Socio)
INSERT INTO utente (nome, cognome, email, password, ruolo) VALUES
('Mario', 'Rossi', 'admin@tennis.it', 'admin123', 'ADMIN'),
('Luigi', 'Verdi', 'socio@tennis.it', 'socio123', 'SOCIO')
ON CONFLICT (email) DO NOTHING;

-- Inserimento Campi
INSERT INTO campo (nome, tipo_superficie, is_coperto) VALUES
('Campo 1', 'Terra', TRUE),
('Campo 2', 'Cemento', FALSE)
ON CONFLICT DO NOTHING;

-- Inserimento Prenotazioni di test
INSERT INTO prenotazione (data, ora_inizio, id_campo, id_socio) VALUES
('2026-01-15', '09:00', 1, 2),
('2026-01-15', '10:00', 2, 2),
('2026-01-16', '14:00', 1, 2),
('2026-01-17', '16:00', 2, 1)
ON CONFLICT DO NOTHING;
