# Installazione dell'applicativo "Chiedi al bibliotecario"

[![License: CC BY 4.0](https://img.shields.io/badge/License-CC%20BY%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by/4.0/)

# Indice
1. [Premessa](#1-Premessa)
   - 1.1 [Sistema Linux - Ambiente e configurazione di base](#11-Sistema-Linux-Ambiente-e-configurazione-di-base)
   - 1.2 [Prerequisiti software del server](#12-Prerequisiti-software-del-server)
   - 1.3 [Configurazione di Apache](#13-Configurazione-di-Apache)
   - 1.4 [Configurazione di PostgreSQL](#14-Configurazione-di-PostgreSQL)
   - 1.5 [Utenti operativi sul sistema](#15-Utenti-operativi-sul-sistema)
2. [Predisposizione dell'ambiente per CHIEDI AL BIBLIOTECARIO](#2-Predisposizione-dell'ambiente-per-CHIEDI-AL-BIBLIOTECARIO)
   - 2.1 [Kit di installazione](#21-Kit-di-installazione)
   - 2.2 [Predisposizione dell'ambiente](#22-Predisposizione-dellambiente)
3. [Creazione del database Postgres "CHIEDI AL BIBLIOTECARIODB"](#3-Creazione-del-database-Postgres-CHIEDI-AL-BIBLIOTECARIODB)
4. [Definizione e impostazione delle politiche di backup](#6-Definizione-e-impostazione-delle-politiche-di-backup)
   - 4.1 [Il backup dell'ambiente](#41-Il-backup-dellambiente)
   - 4.2 [Il backup della base dati Postgres](#42-Il-backup-della-base-dati-Postgres)
   - 4.3 [Le shell di backup](#43-Le-shell-di-backup)

## 1. PREMESSA

Obiettivo del presente documento è descrivere le operazioni da eseguire per l'installazione
dell'applicazione **Chiedi al bibliotecario** e la sua configurazione.

I prodotti da installare e le shell di configurazione sono contenuti nel "kit di installazione", cioè il
file **pacchetto\_chiedi_{versione di rilascio}.zip**. che verrà descritto in dettaglio in seguito e che è salvato sulla
macchina di Collaudo (IP 193.206.221.27) nella posizione _/tomcat_.

### 1.1. Sistema Linux - Ambiente e configurazione di base

L'applicativo prevede che il server sia equipaggiato con il **Sistema Operativo Linux
RedHat 7.X Enterprise Edition**. La licenza di registrazione, pur non essendo di per sé
necessaria, è però consigliabile in quanto permette di effettuare in maniera veloce l'eventuale
installazione di pacchetti software aggiuntivi direttamente da internet. In alternativa si
suggeriscono le distribuzioni **Scientific Linux SL 7.X** o **CENTOS 7.x** le quali, pur avendo le stesse
caratteristiche della RedHat, non necessitano di licenza per l'aggiornamento via internet del
sistema operativo e di eventuali pacchetti software aggiuntivi.

Si raccomanda di scegliere la lingua inglese per il processo di configurazione ed esecuzione
dell'installazione del sistema operativo.

Vediamo in dettaglio i principali prerequisiti per una corretta ed efficiente configurazione di
base del sistema.

**Il Sistema Operativo (da qui SO) dovrebbe essere installato con le seguenti
caratteristiche:**

- indirizzamento 64 bit (scegliere quindi una immagine d'installazione di architettura x86_64 o
    similare), se le caratteristiche del server fisico o virtuale lo consentono;
- installazione in lingua italiana con tastiera italiana;
- installazione il più possibile completa dei pacchetti software (tipologia d'installazione
    consigliata: "Server with GUI"), selezionando poi gli ulteriori pacchetti che possono
    interessare (per es. PostgreSQL Database Server);
- partizionamento manuale del disco di SO (Manual partitioning) di tipo LVM (Logical Volume
    Manager), per consentire una migliore e più efficace gestione dello spazio disco anche
    successivamente all'installazione (si veda sotto);
- impostazione dell'hostname, meglio se con un nome legato alla funzionalità del server (ad
    esempio Opac-codpolo o qualcosa di simile).
- Kdump disabilitato;
- Security Policy impostata a **off** (N.B. successivamente al primo boot del SO appena installato,
    si raccomanda di disabilitare **selinux** )

Nel caso di un server fisico con dotazione dischi standard (di solito due dischi di dimensioni
ridotte per il Sistema Operativo, e due dischi di grandi dimensioni per l'applicativo e i dati), si
raccomanda di definire lo spazio disco nel seguente modo:

- la partizione di sistema, in RAID1, deve avere una dimensione di almeno 50 GByte;
- la partizione dedicata ai dati, in RAID1, deve avere una dimensione di almeno 100 GByte.


Nel caso di un server virtuale. vengono meno le considerazioni sulla dotazione e configurazione
dei dischi fisici mentre restano valide quelle sulle dimensioni suggerite.

Nella partizione di sistema devono essere definiti i seguenti file system con i dimensionamenti
consigliati:
|Directory|Dimensioni
|---|---|
|/ (radice) |2 Gbyte|
|/usr | 5 Gbyte|
|usr/local |5 Gbyte|
|/home |5 Gbyte|
|/var |10 Gbyte|
|/tmp |1 Gbyte|
|/opt |5 Gbyte|

Nella partizione dei dati (che si può creare con LVM successivamente all'installazione del SO),
devono essere definiti i seguenti file system con i dimensionamenti consigliati:
|Directory|Dimensioni
|---|---|
|/backup |20 Gbyte|
|/tomcat |40 Gbyte|

**Nota:** i dimensionamenti sopra riportati vanno considerati come minimali in fase di partenza,
tenendo conto che lo strumento LVM consente di modificarli dinamicamente e in qualsiasi
momento a seconda delle esigenze riscontrate.

### 1.2. Prerequisiti software del server

Il server Linux che deve ospitare l'applicazione richiede una serie di prerequisiti software
sintetizzati nella tabella seguente.

|Requisito| Versione 
|---|---|
|[Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)| 1.8_x|
|[PostgreSQL](https://www.postgresql.org/)| 9.x| Database dati del Polo|
|[Tomcat Server 8](#)| 8.x

**Altri prerequisiti tecnici**

- Se il polo utilizza l'applicativo SBNWeb è necessaria l'apertura del firewall verso il DNS
    dell'applicativo
- Se il polo utilizza l'applicativo OPAC di Polo SBNWeb è necessaria l'apertura del firewall
    verso il DNS dell'applicativo


L'ambiente di base deve essere quindi in parte predisposto in fase di installazione dei prodotti di
cui sopra e viene poi completato utilizzando la shell **predisposizioneAmbienteChiedi.sh** che fa
parte del kit di installazione.

### 1.3. Configurazione di Apache

Apache può essere installato durante l'installazione del SO oppure successivamente con yum.

Le personalizzazioni sono minime.

Come utente root, all'interno del file:

    /etc/httpd/conf/httpd.conf

si consiglia, se non presente, di inserire la riga:

    ServerName 127.0.0.0:

e nella directory _/etc/httpd/conf.d_

creare/modificare il file:

    proxy_ajp.conf

con il seguente contenuto:

    <VirtualHost *:80>
    ServerName _nome_dns_del_server_
    ErrorLog /var/log/httpd/chiedi_bib.error.log
    CustomLog /var/log/httpd/chiedi_bib.log combined
    ProxyPass /chiedi ajp://localhost:8009/chiedi
    </VirtualHost>

### 1.4. Configurazione di PostgreSQL

PostgreSQL Server può essere installato durante l'installazione del SO oppure successivamente
con yum.

Di seguito le personalizzazioni consigliate.

Come utente postgres, nel file:

    /var/lib/pgsql/data/postgresql.conf

modificare - o scommentare - le seguenti righe:

    listen_addresses = '*' # what IP address(es) to listen on;
    shared_buffers = 128MB # min 128kB
    log_destination = 'stderr' # Valid values are combinations of
    log_directory = 'pg_log' # directory where log files are written,
    log_line_prefix = '< %m >' # special values:

Nel file:

    /var/lib/pgsql/data/pg_hba.conf


vanno inserite le informazioni necessarie ad abilitare uno o più indirizzi IP alla connessione col
database server.

Nella sezione _# IPv4 local connections_ :

va commentata la riga

    host all all 127.0.0.1/32 password

e inserite una o più righe con gli IP da abilitare alla connessione al db server, per es.

    host all all 192.168.20.0/24 password

Non dimenticare di inserire anche la riga

    host all all 127.0.0.1/32 md

che è il metodo di autenticazione dell'applicativo

Oltre al role "postgres" per la gestione di sistema del database, va creato anche un role
"applicativo" per gestire le connessioni al database a partire dall'applicativo Chiedi al bibliotecario.
Il nome scelto per il role applicativo (che per semplicità ipotizziamo sia "chiedi") e la relativa
password vanno impostati nel file di properties di Chiedi al bibliotecario ovvero "chiedi_al_bibliotecario.properties" (vedi
paragrafo 2.2).

### 1.5. Utenti operativi sul sistema

Devono esistere i seguenti utenti:

- **tomcat** utente preposto all'utilizzo dell'applicazione;
- **postgres** utente preposto alla gestione del database Postgres.

|Utente| Gruppo| Directory |Password alla creazione|
|---|---|----|---|
|tomcat| tomcat| /tomcat| t9mc1t|
|postgres| postgres| /var/lib/pgsql| p9stgr3s|

## 2. Predisposizione dell'ambiente per CHIEDI AL BIBLIOTECARIO 

### 2.1. Kit di installazione

Il kit di installazione **pacchetto\_chiedi_{versione di rilascio}.zip**. comprende:

-  shell scripting _predisposizioneAmbienteChiedi.sh_ (per la predisposizione di base dell'ambiente operativo a partire dal kit)
-  file "_chiedi.war_" e file "_chiedi_al_bibliotecario.properties_" (eseguibile e file di configurazione dell'applicativo)
-  directory "_postgresConfig_" (contiene script SQL per la creazione e il popolamento del database Postgres "*chiedi_al_bibliotecarioDB*" su cui memorizzare dati e configurazioni). 

***NB:*** nel pacchetto è presente il template _03_insertChiedi_al_bib_poloXXX.template.sql_. Per completare
la configurazione di un polo va predisposto, a partire dal template, ed eseguito uno script
personalizzato per lo specifico polo da inizializzare.
-  directory backup (contiene la shell pg_dump.sh per il backup del database Postgres. Cfr. paragrafo 4.3)
- directory cmschiedialbibliotecario e relative sotto-directory (che ospiteranno i file di servizio e gli allegati)

### 2.2. Predisposizione dell'ambiente

Loggarsi come utente **tomcat**.

Estrarre nella homedir di tomcat (**/tomcat**) il contenuto del file **pacchetto\_chiedi_{versione di rilascio}.zip**.

Verificare e impostare opportunamente i seguenti parametri presenti nel file
"**chiedi_al_bibliotecario.properties**" che configura l'accesso al database Postgres e in particolare:
- **DB_DRIVER_CLASS**=_org.postgresql.Driver_ &rarr; driver per la connessione al database Postgres
- **DB_URL**=jdbc:postgresql://_localhost_:5432/chiedi_al_bibliotecarioDB &rarr; localhost oppure IP della macchina su cui risiede il DB
- **DB_USER**=_chiedi_ / **DB_PASSWORD**=_chiediadm_ &rarr; utenza/password per accedere al DB
- **DB_SCHEMA**=_public_ &rarr; schema utilizzato per il database
- **PATH_FILES**=_cmschiedialbibliotecario_ &rarr; riferimento alla cartella dove saranno presenti file di servizio e allegati
- **EMAIL_DNS**=_localhost_ &rarr; riferimento al mail server (localhost oppure IP della macchina su cui
    risiede il mail server)
- **EMAIL_NO_REPLY**=no-reply@sbnweb.it &rarr; mittente delle mail inviate da OPAC2
- **LOGIN_MAIL**=true/false &rarr; default=false (*)
- **USERNAME_MAIL**=username per login su mail server &rarr; default=vuoto (*)
- **PASSWORD_MAIL**=password per login su mail server &rarr; default=vuoto (*)
- **URL_DOMINIO_ISTANZA**=_http://IPDOMINIO/chiedi/CODICEPOLO_ &rarr; puntamento all'applicativo da utilizzare nelle email inviate all'utente 

(*) Parametri opzionali. Possono esistere le seguenti combinazioni:
<br>- LOGIN_MAIL=false &rarr; il server di posta non necessita di credenziali di accesso e i parametri USERNAME_MAIL/PASSWORD_MAIL possono essere vuoti o assenti;
<br>- LOGIN_MAIL=true &rarr; il server di posta necessita di credenziali di accesso impostate in USERNAME_MAIL e PASSWORD_MAIL;
<br>- parametri non indicati nel file di properties &rarr; equivale a LOGIN_MAIL=false e USERNAME_MAIL/PASSWORD_MAIL vuoti    

Portarsi quindi sotto la directory "pacchetto_chiedi" ed eseguire la shell

    ./predisposizioneAmbienteChiedi.sh

Prima di lanciare la shell verificare - ed eventualmente modificare - il parametro "apache" che
corrisponde al path della directory di installazione di Apache.

La shell provvede a:

- _copiare_ il file di properties "chiedi_al_bibliotecario.properties" in /tomcat e il file
    "chiedi.war" in /tomcat/{apache}/webapps
- _creare la directory_ "cmschiedialbibliotecario" e le relative sotto-directory (in "servizio" ci
    sono le immagini da utilizzare come loghi nelle pagine dell'applicativo o nelle e-mail
    inviate all'utente)

***NB***: Per le pagine dell'applicazione è previsto un logo "generico" con la dicitura "SBNWeb".
Per personalizzarlo occorre creare un logo con il nome del polo da salvare come
"logo_chiedi_{polo}.png" (con il codice del polo in minuscolo) da copiare nella directory
"cmschiedialbibliotecario/servizio".

## 3. Creazione del database Postgres "chiedi_al_bibliotecarioDB"

Il nome scelto per il database Postgres è **"chiedi_al_bibliotecarioDB"**.

Loggarsi come utente **postgres**.

I file di log delle procedure da eseguire verranno creati, a meno di diversa indicazione, nella
home-dir dell'utente (nel caso della macchina di sviluppo **/var/lib/pgsql** ).

I file _sql_ si trovano nel kit di installazione sotto la directory _postgresConfig_ quindi in questa fase
di predisposizione del database sono disponibili nella posizione:

    /tomcat/pacchetto_chiedi/postgresConfig

ma possono essere copiati in una diversa directory di lavoro, che qui per comodità ipotizziamo
coincida con la homedir di postgres. Copiamo quindi i file sql da eseguire nella homedir di
postgres.

Questa la sequenza di esecuzione degli sql:

1. Creare il data base: file _'01_createChiedi_al_bib.sql'_ 

lanciare il seguente comando

    psql -L creaDB.log -f ./01_createChiedi_al_bib.sql

e verificare nel file creaDB.log che il database "chiedi_al_bibliotecarioDB" sia stato
correttamente istanziato.

2. Generare le strutture dati: file _'02 _structChiedi_al_bib.sql'_

lanciare il seguente comando

    psql -d chiedi_al_bibliotecarioDB -L creaDBStrutture.log -f ./02_structChiedi_al_bib.sql 
      
 e verificare nel file _creaDBStrutture.log_ che le strutture dati siano state correttamente        istanziate.

3. Caricare nel DB i dati di inizializzazione:
    creare uno script personalizzato per il polo che deve essere inizializzato a partire dal
    template _'03_insertChiedi_al_bib_template.sql'_.
    L'attività è finalizzata al caricamento nel DB dei dati e delle configurazioni del polo e
    dell'utente gestore di amministrazione.
    Per ciascun polo occorre quindi creare uno script con i dati reali del polo da caricare.

Esempio: per il polo **ABC** si creerà lo script _'03_insertChiedi_al_bib_ABC.sql'_.

lanciare il seguente comando

    psql -d chiedi_al_bibliotecarioDB -L iniDB_ABC.log –f ./03_insertChiedi_al_bib_ABC.sql 

e verificare nel file _iniDB\_ABC.log_ che le tabelle siano state correttamente inizializzate.

A questo punto il database **"chiedi_al_bibliotecarioDB"** può considerarsi
inizializzato.

## 4. Definizione e impostazione delle politiche di backup

Nell'ambito delle politiche di backup del server occorre definire tre aspetti: l'oggetto del backup,
la storicizzazione delle informazioni (ossia per quanto tempo mantenerle), la schedulazione dei
backup.

In generale, in mancanza di specifiche esigenze al riguardo, lo standard è quello di effettuare un
backup giornaliero con ciclo massimo di un mese; in altre parole si sceglie di mantenere le
informazioni salvate per il tempo massimo di un mese.


Solitamente i file di output prodotti dalle procedure riportano nel nome il giorno in cui vengono
scritti e pertanto, al termine del ciclo, il file più vecchio viene sovrascritto. Nella maggioranza dei
casi si può omettere il backup nei giorni di sabato e domenica.

La schedulazione dell'esecuzione dei backup viene effettuata attraverso la _crontab_ di sistema,
nella quale vengono stabiliti i giorni e l'ora di esecuzione di ogni singola shell.

E' oggetto di backup la base dati di Postgres _chiedi_al_bibliotecarioDB_.

### 4.1. Il backup dell'ambiente

Si consiglia di effettuare con la periodicità sopra indicata il backup su supporto esterno (disco
esterno o nastro) dell'intero file system **/tomcat** e dei principali file system di sistema operativo.
All'interno della directory /tomcat è presente anche la directory DUMP_DB dove viene salvato
giornalmente il DB Postgres (cfr. paragrafo successivo).

### 4.2. Il backup della base dati Postgres

La shell preposta al backup della base dati Postgres si trova in **_/home/SCRIPTS_** ed è la
**pg_dump.sh** che sfrutta una utility di _PostgreSQL,_ chiamata appunto _pgdump_ , per effettuare un
backup logico del database indicato nella relativa istruzione.

L'output dell'operazione viene posto in _/tomcat/DUMP_DB_ con la seguente nomenclatura:

    <nomehost>.<giorno>.<nomedb>.bzdump

_Ad es._: storagesrv.Thu.chiedi_al_bibliotecarioDB.bzdump

La shell provvede anche ad effettuare il test sull'esito positivo del backup mediante un'altra
utility chiamata **md5sum.** Questa utility legge il file di output prodotto dalla pgdump e, se il
backup è andato a buon fine, produce un output in _/tomcat/DUMP_DB_ la cui nomenclatura è
stata stabilita come segue:

    <nomehost>.<giorno>.<nomedb>.bzdump.md

In pratica, l'esistenza di quest'ultimo file indica il buon esito del backup per quel giorno.

Per maggior chiarezza la procedura riporta l'esito del backup nel file di log sotto la directory
/backup/logs

    bckdb.-<giorno>.log

### 4.3. Le shell di backup

La predisposizione dell'ambiente operativo si conclude quindi con la messa in linea delle shell di
backup.

Occorre copiare dalla directory _backup_ del kit di installazione la shell _pg_dump.sh_ nella directory
**/home/SCRIPTS** assicurandosi che i proprietari della shell siano _root:root_ e verificando i
permessi di esecuzione.

L'ultimo passo è la configurazione adeguata della crontab di sistema (cioè di root) per la
schedulazione dei backup.


