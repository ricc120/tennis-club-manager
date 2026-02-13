-- default.sql
-- Inserimento dati di test

-- Inserimento Utenti (1 Admin, 1 Socio, 1 Maestro, 1 Allievo, 1 Manutentore)
INSERT INTO utente (nome, cognome, email, password, ruolo) VALUES
('Mario', 'Rossi', 'admin@tennis.it', 'admin123', 'ADMIN'),
('Luigi', 'Verdi', 'socio@tennis.it', 'socio123', 'SOCIO'),
('Paolo', 'Bianchi', 'maestro@tennis.it', 'maestro123', 'MAESTRO'),
('Giulia', 'Neri', 'allievo@tennis.it', 'allievo123', 'ALLIEVO'),
('Giorgio', 'Viola', 'allievo2@tennis.it', 'allievo2123', 'ALLIEVO'),
('Roberto', 'Gialli', 'manutentore@tennis.it', 'manutentore123', 'MANUTENTORE');

-- Inserimento Campi
INSERT INTO campo (nome, tipo_superficie, is_coperto) VALUES
('Campo 1', 'Terra', TRUE),
('Campo 2', 'Cemento', FALSE);

-- Inserimento Prenotazioni di test
INSERT INTO prenotazione (data, ora_inizio, id_campo, id_socio) VALUES
('2026-01-15', '09:00', 1, 2),  -- Luigi prenota Campo 1 alle 9:00
('2026-01-15', '10:00', 2, 2),  -- Luigi prenota Campo 2 alle 10:00
('2026-01-16', '14:00', 1, 2),  -- Luigi prenota Campo 1 alle 14:00
('2026-01-17', '16:00', 2, 1);  -- Mario prenota Campo 2 alle 16:00