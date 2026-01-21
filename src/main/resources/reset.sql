-- reset.sql
-- Elimina le tabelle se esistono per resettare il database
DROP TABLE IF EXISTS allievo_lezione CASCADE;
DROP TABLE IF EXISTS lezione CASCADE;
DROP TABLE IF EXISTS manutenzione CASCADE;
DROP TABLE IF EXISTS prenotazione CASCADE;
DROP TABLE IF EXISTS campo CASCADE;
DROP TABLE IF EXISTS utente CASCADE;
DROP TYPE IF EXISTS ruolo_utente;
