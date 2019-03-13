

/* configurazione database Postgres "chiedi_al_bibliotecarioDB"  */


/* tabella di configurazione del polo  */
INSERT INTO public.conf_polo 
(cod_polo, url_opac, url_sbnweb_reset_psw, url_sbnweb_login_service, nome_polo, fl_canc) VALUES 
('XXX', 'http://IP-OPAC2/opac2/XXX', 'http://IP-SBNWEB/servizi', 'http://IP-SBNWEB/sbn/api/1.0/servizi/utente/dettaglio?uid={user}&pwd={password}', 
 'Nome del polo XXX', 'N');

/* creazione UTENTE AMMINISTRATORE: username=admin, password=admin */
INSERT INTO public.utenti 
(cod_polo, username, email, password, data_nascita, ts_ins, fl_canc, admin, bibliotecario, sbnweb, newsletter, psw_reset) VALUES 
('XXX', 'admin', 'indirizzo email', 'm6qYydS1NgE=', NULL, now(), 'n', true, true, false, NULL, true);










