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
