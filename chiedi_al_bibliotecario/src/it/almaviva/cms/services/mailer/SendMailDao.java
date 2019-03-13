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
package it.almaviva.cms.services.mailer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.utilities.Util;

public class SendMailDao {
	static Logger log = Logger.getLogger(SendMailDao.class);
	
	private final String keyMessage = "${message}";
	private final String keyUrl = "${url}";
	private final String keyUsername = "${username}";
	private String htmlMailTemplate = "";
	
	private MailProperties propertiesMail;
	// regex mail
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b",
			Pattern.CASE_INSENSITIVE);

	public SendMailDao(MailProperties props) {
		super();
		this.propertiesMail = props;
	}
	private static Boolean checkEmail(String to) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(to);
		return matcher.find();
	}

	private MimeMessage createMessage(String email, String text, String oggetto, Session session)
			throws AddressException, MessagingException, Exception {
		MimeMessage message = new MimeMessage(session);
		 Multipart multipart = new MimeMultipart();

		// setto le impostazioni delle mail
		message.setFrom(new InternetAddress(propertiesMail.getMittenteMail()));
		BodyPart htmlPart = new MimeBodyPart();
		 htmlPart.setContent(new String(text.getBytes("UTF8"),"ISO-8859-1"), "text/html");
		 multipart.addBodyPart(htmlPart);

		 message.setContent(multipart);
		if (!checkEmail(email))
			throw new Exception("Email formato non corretto");
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

		message.setSubject(oggetto);
		return message;
	}


	private ResponseType sendMail(String email, String text, String oggetto) {
		try {
			if (!Util.isFilled(email))
				return ResponseType.MESSAGE_MAIL_WRONG;
			log.info("Sending email TO: " + email);
			Session session = null;
			Properties systemProps = System.getProperties();
			systemProps.setProperty("mail.smtp.host", propertiesMail.getDnshost());
			
			if (propertiesMail.getIsToLogin()) {
				log.info("Logging mail");
				systemProps.setProperty("mail.smtp.auth", propertiesMail.getIsToLogin().toString());
				session = Session.getInstance(systemProps, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(propertiesMail.getUsername_login(), propertiesMail.getPsw_login());
					}
				});
			} else {
				log.info("No login required");
				systemProps.remove("mail.smtp.auth");
				systemProps.setProperty("mail.smtp.auth", propertiesMail.getIsToLogin().toString());

				session = Session.getInstance(systemProps);
			}
			MimeMessage message = createMessage(email, text, oggetto, session);

			
			Transport.send(message);
			log.info("Sent message successfully....");
			return ResponseType.OK;

		} catch (MessagingException e) {
			log.error("ErrorSendingEmail", e);
			return ResponseType.MESSAGE_ERROR;

		} catch (Exception e) {
			log.error("ErrorSendingEmail", e);
			return ResponseType.MESSAGE_MAIL_WRONG;

		}
	}
	public String createText(String text, String username) {
		return htmlMailTemplate
				.replace(keyMessage, text)
				.replace(keyUrl, propertiesMail.getUrl_istanza())
				.replace(keyUsername, username);
	}
	public ResponseType sendEmails(MailerModel model) {
		if("".equals(htmlMailTemplate)) 
			readTemplateFile();
		ResponseType response = ResponseType.OK;
		for(Utenti user: model.getUsers() ){
			log.info(user);
			String textHtml = createText(model.getText(), user.getUsername());
			response = sendMail(user.getEmail(), textHtml, model.getOggetto());
		}
		return response;
	}
	public void readTemplateFile() {
		try {
			StringBuilder contentBuilder = new StringBuilder();
		    BufferedReader in = new BufferedReader(new FileReader(propertiesMail.getTemplate_html_mail()));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
			this.htmlMailTemplate = contentBuilder.toString();
			log.info("Template mail letto correttamente");
		} catch (IOException e) {
			log.error("Impossibile leggere il template html");
		}
	}
}
