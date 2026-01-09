-- default.sql
-- Inserimento dati di test

-- Inserimento Utenti (1 Admin, 1 Socio)
INSERT INTO utente (nome, cognome, email, password, ruolo) VALUES
('Mario', 'Rossi', 'admin@tennis.it', 'admin123', 'ADMIN'),
('Luigi', 'Verdi', 'socio@tennis.it', 'socio123', 'SOCIO');

-- Inserimento Campi
INSERT INTO campo (nome, tipo_superficie, is_coperto) VALUES
('Campo 1', 'Terra', TRUE),
('Campo 2', 'Cemento', FALSE);

-- Inserimento Prenotazioni di test
-- Nota: Usa le date relative ad oggi quando possibile, ma qui usiamo date fisse per semplicità
INSERT INTO prenotazione (data, ora_inizio, id_campo, id_socio) VALUES
('2026-01-15', '09:00', 1, 2),  -- Luigi prenota Campo 1 alle 9:00
('2026-01-15', '10:00', 2, 2),  -- Luigi prenota Campo 2 alle 10:00
('2026-01-16', '14:00', 1, 2),  -- Luigi prenota Campo 1 alle 14:00
('2026-01-17', '16:00', 2, 1);  -- Mario prenota Campo 2 alle 16:00
