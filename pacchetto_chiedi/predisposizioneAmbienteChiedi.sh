#!/bin/bash
# Debug variable
#set -x
################################################################################################
#               
# Descrizione: PREDISPOSIZIONE AMBIENTE PER MODULO "CHIEDI AL BIBLIOTECARIO"
# 
# Parametri da impostare:  
# 	"userhome" (percorso a partire da "pacchetto_chiedi") 
# 	"apache" (path corretto in base alla versione installata)
#
#
################################################################################################

userhome=..
apache=/tomcat/apache-tomcat-8.0.39


echo copio chiedi_al_bibliotecario.properties e chiedi.war ...
#----------configurazione e war
cp chiedi_al_bibliotecario.properties $userhome/.
cp chiedi.war $userhome/$apache/webapps/.

echo copio cms chiedi ...
#----------copiare cartella cmschiedialbibliotecario
cp -r cmschiedialbibliotecario $userhome/.


echo ----------------------------
echo FINE PROCEDURA
echo ----------------------------
