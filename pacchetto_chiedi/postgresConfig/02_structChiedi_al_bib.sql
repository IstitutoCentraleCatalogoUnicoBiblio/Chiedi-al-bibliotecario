

/* creazione delle tabelle */

CREATE TABLE public.conf_polo (
	id serial NOT NULL,
	cod_polo varchar(3) NOT NULL,
	nome_polo varchar NOT NULL,
	url_opac varchar NULL,  
	url_sbnweb_reset_psw varchar NULL, 
	url_sbnweb_login_service varchar NULL, 
	fl_canc varchar(1) NOT NULL DEFAULT 'n'::character varying,
	CONSTRAINT conf_polo_pkey PRIMARY KEY (cod_polo)
);
COMMENT ON TABLE public.conf_polo IS 'Tabella di configurazione del polo';
-- Column comments
COMMENT ON COLUMN public.conf_polo.id IS 'Id progressivo del record';
COMMENT ON COLUMN public.conf_polo.cod_polo IS 'Codice del polo';
COMMENT ON COLUMN public.conf_polo.nome_polo IS 'Nome del polo';
COMMENT ON COLUMN public.conf_polo.url_opac IS 'Indirizzo dell''applicativo OPAC di Polo';
COMMENT ON COLUMN public.conf_polo.url_sbnweb_reset_psw IS 'Link applicativo dei servizi SBNWeb del polo per reset password';
COMMENT ON COLUMN public.conf_polo.url_sbnweb_login_service IS 'Link applicativo dei servizi SBNWeb del polo per credenziali di login';
COMMENT ON COLUMN public.conf_polo.fl_canc IS 'Flag di cancellazione logica';



CREATE TABLE public.tickets (
	id serial NOT NULL,
	cod_polo varchar(3) NOT NULL,
	titolo varchar NOT NULL,
	testo varchar NOT NULL,
	id_cat int8 NOT NULL,
	allegato bool NOT NULL DEFAULT false,
	aperto bool NOT NULL DEFAULT true,
	motivo_richiesta varchar NULL,
	ho_gia_fatto varchar NULL,
	pubblico bool NOT NULL DEFAULT false,
	cod_biblioteca_ind varchar(5) NULL,
	user_ins varchar NOT NULL,
	user_assegnato varchar(50) NULL, -- Utente a cui è assegnato il ticket
	ts_ins timestamp NOT NULL,
	fl_canc varchar(1) NOT NULL DEFAULT 'n'::character varying,
	CONSTRAINT tickets_pk PRIMARY KEY (id),
	CONSTRAINT faq_cod_polo_fkey FOREIGN KEY (cod_polo) REFERENCES conf_polo(cod_polo)

);
COMMENT ON TABLE public.tickets IS 'Richieste inserite sul modulo Chiedi al bibliotecario';
-- Column comments
COMMENT ON COLUMN public.tickets.id IS 'Id progressivo del record';
COMMENT ON COLUMN public.tickets.cod_polo IS 'Codice del polo';
COMMENT ON COLUMN public.tickets.titolo IS 'Oggetto della richiesta';
COMMENT ON COLUMN public.tickets.testo IS 'Testo descrittivo della richiesta';
COMMENT ON COLUMN public.tickets.id_cat IS 'Id della categoria di appartenenza della richiesta';
COMMENT ON COLUMN public.tickets.allegato IS 'Flag che indica se è presente un allegato (Default false)';
COMMENT ON COLUMN public.tickets.aperto IS 'Flag che indica se la richiesta è ancora aperta (Default true)';
COMMENT ON COLUMN public.tickets.motivo_richiesta IS 'Motivo della richiesta';
COMMENT ON COLUMN public.tickets.ho_gia_fatto IS 'Testo descrittivo di eventuali ricerche effettuate in precedenza';
COMMENT ON COLUMN public.tickets.pubblico IS 'Flag che indica se la richiesta è pubblica (Default false)';
COMMENT ON COLUMN public.tickets.cod_biblioteca_ind IS 'Codice biblioteca a cui indirizzare la richiesta';
COMMENT ON COLUMN public.tickets.user_ins IS 'Utente che ha effettuato l''inserimento della richiesta';
COMMENT ON COLUMN public.tickets.user_assegnato IS 'Utente bibliotecario a cui è assegnato il ticket';
COMMENT ON COLUMN public.tickets.ts_ins IS 'Timestamp di inserimento della richiesta';
COMMENT ON COLUMN public.tickets.fl_canc IS 'Flag di cancellazione logica';



CREATE TABLE public.messaggi (
	id serial NOT NULL,
	testo varchar NOT NULL,
	ticket_id int4 NOT NULL,
	username_ins varchar NOT NULL,
	allegato bool NOT NULL DEFAULT false,
	ts_ins timestamp NOT NULL,
	fl_canc varchar(1) NOT NULL DEFAULT 'n'::character varying,
	CONSTRAINT messaggi_pk PRIMARY KEY (id),
	CONSTRAINT messaggi_tickets_fk FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);

COMMENT ON TABLE public.messaggi IS 'Messaggi, segnalazioni o risposte inseriti per un dato ticket';
-- Column comments
COMMENT ON COLUMN public.messaggi.id IS 'Id progressivo del record';
COMMENT ON COLUMN public.messaggi.testo IS 'Testo del messaggio';
COMMENT ON COLUMN public.messaggi.ticket_id IS 'Id del ticket a cui si riferisce il messaggio'; 
COMMENT ON COLUMN public.messaggi.username_ins IS 'Username utente che ha inserito il messaggio'; 
COMMENT ON COLUMN public.messaggi.allegato IS 'Flag che indica se per il messaggio è presente un allegato (Default false)'; 
COMMENT ON COLUMN public.messaggi.ts_ins IS 'Timestamp di inserimento del messaggio';
COMMENT ON COLUMN public.messaggi.fl_canc IS 'Flag di cancellazione logica';



--FAQ
CREATE TABLE public.faq (
	id serial NOT NULL,
	cod_polo varchar(3) NOT NULL,
	cd_faq varchar NOT NULL,
	domanda varchar NOT NULL,
	risposta varchar NOT NULL,
	ts_ins timestamp NOT NULL,
	fl_canc varchar(1) NOT NULL DEFAULT 'n'::character varying,

	CONSTRAINT faq_cod_polo_fkey FOREIGN KEY (cod_polo) REFERENCES conf_polo(cod_polo)
);

COMMENT ON TABLE public.faq IS 'Tabella delle FAQ - Frequently Asked Questions';
-- Column comments
COMMENT ON COLUMN public.faq.id IS 'Id progressivo del record';
COMMENT ON COLUMN public.faq.cod_polo IS 'Codice del polo';
COMMENT ON COLUMN public.faq.cd_faq IS 'Codice assegnato alla FAQ'; 
COMMENT ON COLUMN public.faq.domanda IS 'Domanda della FAQ'; 
COMMENT ON COLUMN public.faq.risposta IS 'Risposta della FAQ'; 
COMMENT ON COLUMN public.faq.ts_ins IS 'Timestamp di inserimento della FAQ';
COMMENT ON COLUMN public.faq.fl_canc IS 'Flag di cancellazione logica';


CREATE TABLE public.utenti (
	id serial NOT NULL,
	cod_polo varchar(3) NOT NULL,
	email varchar NOT NULL,
	username varchar NOT NULL,
	password varchar NULL,
	data_nascita varchar NULL,
	bibliotecario bool NOT NULL DEFAULT false,
	newsletter bool NULL DEFAULT false,
	psw_reset bool NOT NULL DEFAULT false,
	"admin" bool NOT NULL DEFAULT false,
	sbnweb bool NOT NULL DEFAULT false,
	ts_ins timestamp NOT NULL,
	fl_canc varchar(1) NOT NULL DEFAULT 'n'::character varying,
	CONSTRAINT utenti_pk PRIMARY KEY (cod_polo,username,email),
	CONSTRAINT faq_cod_polo_fkey FOREIGN KEY (cod_polo) REFERENCES conf_polo(cod_polo)
);

COMMENT ON TABLE public.utenti IS 'Tabella delle FAQ - Frequently Asked Questions';
-- Column comments
COMMENT ON COLUMN public.utenti.id IS 'Id progressivo del record';
COMMENT ON COLUMN public.utenti.cod_polo IS 'Codice del polo';
COMMENT ON COLUMN public.utenti.email IS 'Indirizzo di posta elettronica'; 
COMMENT ON COLUMN public.utenti.username IS 'Nome utente'; 
COMMENT ON COLUMN public.utenti.password IS 'Password scelta dall''utente'; 
COMMENT ON COLUMN public.utenti.data_nascita IS 'Anno di nascita dell''utente';
COMMENT ON COLUMN public.utenti.bibliotecario IS 'Flag che indica se l''utente è stato nominato bibliotecario (Default false)';
COMMENT ON COLUMN public.utenti.newsletter IS 'Flag che indica se l''utente intende ricevere informazioni via email (Default false)';
COMMENT ON COLUMN public.utenti.psw_reset IS 'Flag che indica se la password è stata resettata (Default false)';
COMMENT ON COLUMN public.utenti.admin IS 'Flag che indica se l''utente ha il ruolo di amministratore (Default false)';
COMMENT ON COLUMN public.utenti.sbnweb IS 'Flag che indica se l''utente sta usando le credenziali di SBNWeb (Default false)';
COMMENT ON COLUMN public.utenti.ts_ins IS 'Timestamp di inserimento del record utente';
COMMENT ON COLUMN public.utenti.fl_canc IS 'Flag di cancellazione logica';


CREATE TABLE public.categorie (
	id serial NOT NULL,
	cod_polo varchar(3) NOT NULL,
	cd_categoria varchar NOT NULL,
	testo_it varchar NOT NULL, 
	testo_en varchar NULL, 
	ts_ins timestamp NOT NULL,
	fl_canc varchar(1) NOT NULL DEFAULT 'n'::character varying,
	CONSTRAINT categorie_pk PRIMARY KEY (id)
);

COMMENT ON TABLE public.utenti IS 'Tabella delle categorie da assegnare ai ticket';
-- Column comments
COMMENT ON COLUMN public.categorie.id IS 'Id progressivo del record';
COMMENT ON COLUMN public.categorie.cod_polo IS 'Codice del polo';
COMMENT ON COLUMN public.categorie.cd_categoria IS 'Codice assegnato alla categoria';
COMMENT ON COLUMN public.categorie.testo_it IS 'Testo descrittivo in italiano';
COMMENT ON COLUMN public.categorie.testo_en IS 'testo descrittivo in inglese';
COMMENT ON COLUMN public.categorie.ts_ins IS 'Timestamp di inserimento del record categoria';
COMMENT ON COLUMN public.categorie.fl_canc IS 'Flag di cancellazione logica';



CREATE TABLE public.biblioteche (
	id serial NOT NULL,
	cod_polo varchar(3) NOT NULL, -- Codice del polo
	cd_bib varchar(5) NOT NULL,
	nome varchar NULL,
	fl_canc varchar(1) NOT NULL DEFAULT 'n'::character varying,
	CONSTRAINT biblioteche_cod_polo_fkey FOREIGN KEY (cod_polo) REFERENCES conf_polo(cod_polo)
);

COMMENT ON TABLE public.biblioteche IS 'Tabella delle biblioteche del Polo';
-- Column comments
COMMENT ON COLUMN public.biblioteche.id IS 'Id progressivo del record';
COMMENT ON COLUMN public.biblioteche.cod_polo IS 'Codice del polo';
COMMENT ON COLUMN public.biblioteche.cd_bib IS 'Codice della biblioteca';
COMMENT ON COLUMN public.biblioteche.nome IS 'Denominazione della biblioteca';
COMMENT ON COLUMN public.biblioteche.fl_canc IS 'Flag di cancellazione logica';



/* creazione del role per l'accesso al db */

CREATE ROLE chiedi NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT LOGIN PASSWORD 'chiediadm';
-- Permissions
GRANT ALL ON ALL TABLES IN SCHEMA public TO chiedi;
