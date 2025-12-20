-- reset.sql
-- Elimina le tabelle se esistono per resettare il database

DROP TABLE IF EXISTS prenotazione CASCADE;
DROP TABLE IF EXISTS campo CASCADE;
DROP TABLE IF EXISTS utente CASCADE;
DROP TYPE IF EXISTS ruolo_utente;
