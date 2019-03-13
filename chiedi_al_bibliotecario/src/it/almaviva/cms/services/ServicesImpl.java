/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.almaviva.cms.services;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.DatabaseQueryDao;
import it.almaviva.cms.dbManager.beans.Biblioteche;
import it.almaviva.cms.dbManager.beans.PoloConf;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.externalFiles.AttachedType;
import it.almaviva.cms.externalFiles.ExternalFilesManager;
import it.almaviva.cms.sbnwebLogin.SbnWebServicesImpl;
import it.almaviva.cms.services.bibliotecheImp.BibliotecheOpacImporter;
import it.almaviva.cms.services.mailer.MailProperties;
import it.almaviva.cms.services.mailer.MailerModel;
import it.almaviva.cms.services.mailer.SendMailDao;
import it.almaviva.cms.utilities.PropertiesLoader;
import it.almaviva.cms.utilities.Util;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

@Service
public class ServicesImpl implements ServicesInt {
	private Logger log = Logger.getLogger(ServicesInt.class);

	@Autowired
	private DatabaseQueryDao databaseManager;
	private PropertiesLoader properties = new PropertiesLoader();
	private Gson gson = new Gson();
	private ExternalFilesManager fileManager;
	private SendMailDao emailSender;
	private SbnWebServicesImpl sbnwebservice;
	private BibliotecheOpacImporter importerBib;

	private MailProperties readEmailProps() {
		
		MailProperties mailProperties = new  MailProperties();
		String dns = properties.getProps("EMAIL_DNS");
		String mittente_mail = properties.getProps("EMAIL_NO_REPLY");
		String mailIsToLogin = properties.getProps("LOGIN_MAIL");
		String username_login = properties.getProps("USERNAME_MAIL");
		String psw_login = properties.getProps("PASSWORD_MAIL"); //template_html_mail
		String url_dominio_istanza = properties.getProps("URL_DOMINIO_ISTANZA");
		
		mailProperties.setDnshost((Util.isFilled(dns)) ? dns : "");
		mailProperties.setMittenteMail((Util.isFilled(mittente_mail)) ? mittente_mail : "");
		mailProperties.setTemplate_html_mail(fileManager.getAllegato("template_html_mail", AttachedType.SERVIZIO));
		mailProperties.setUrl_istanza((Util.isFilled(url_dominio_istanza)) ? url_dominio_istanza : "");
		
		if (Util.isFilled(mailIsToLogin)) {
			log.info("Configurazione login mail server");
			
			mailProperties.setIsToLogin(MailProperties.parseBoolean(mailIsToLogin));
			mailProperties.setUsername_login((Util.isFilled(username_login)) ? dns : "");
			mailProperties.setPsw_login((Util.isFilled(psw_login)) ? psw_login : "");
		}
		return mailProperties;
	}
	
	@Override
	public DatabaseQueryDao database() {
		log.info("Richiesto servizio database()");
		return databaseManager;
	}

	@Override
	public String systemProperty(String props) {
		log.info("Richiesto servizio properties()");

		return properties.getProps(props);
	}

	@Override
	public String toJson(Object obj) {
		return gson.toJson(obj);
	}

	@Override
	public ResponseType sendMail(MailerModel mailer) {
		if (!Util.isFilled(emailSender)) {
			
			emailSender = new SendMailDao(readEmailProps());
		}
		return emailSender.sendEmails(mailer);
	}
	

	@Override
	public File getFile(String nomefile, AttachedType allegatoType) {
		if (!Util.isFilled(fileManager)) {
			fileManager = new ExternalFilesManager(properties.getProps("PATH_FILES"));
		}
		return fileManager.getAllegato(nomefile, allegatoType);
	}

	@Override
	public ResponseType putFile(String nomefile, MultipartFile file, AttachedType allegatoType) {
		if (!Util.isFilled(fileManager)) {
			fileManager = new ExternalFilesManager(properties.getProps("PATH_FILES"));
		}
		return fileManager.put(nomefile, file, allegatoType);
	}

	@Override
	public void reloadProps() {
		log.info("Reloading properties...");
		properties = new PropertiesLoader();
		fileManager = new ExternalFilesManager(properties.getProps("PATH_FILES"));
		
		emailSender = new SendMailDao(readEmailProps());
		sbnwebservice = new SbnWebServicesImpl();
		importerBib = new BibliotecheOpacImporter();
	}

	@Override
	public SbnwebType loginSbnWeb(Utenti sbnUser) {
		if (!Util.isFilled(sbnwebservice))
			sbnwebservice = new SbnWebServicesImpl();

		PoloConf polo = databaseManager.getPolo(sbnUser.getCod_polo());
		return sbnwebservice.login(sbnUser, polo);
	}

	@Override
	public Biblioteche importaBibliotecaDaOpac(String cod_polo, String cod_biblioteca) {
		if (!Util.isFilled(importerBib))
			importerBib = new BibliotecheOpacImporter();
		PoloConf polo = database().getPolo(cod_polo);
		if(!Util.isFilled(polo))
			return null;
		Biblioteche imported = importerBib.startImport(cod_polo, cod_biblioteca, polo.getOpacWSImportBiblioteche());
		imported = database().addBiblioteca(imported);
		return imported;
	}

}
