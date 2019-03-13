package it.almaviva.cms.services.mailer;

import java.io.File;

import org.apache.log4j.Logger;

public class MailProperties {

	static Logger log = Logger.getLogger(MailProperties.class);

	private String dnshost;
	private String mittenteMail;
	private String username_login;
	private String psw_login;
	private Boolean isToLogin = false;
	private File template_html_mail;
	private String url_istanza;
	private final String charset = "text/html; charset=\"utf-8\"";

	public MailProperties() {
		super();
	}

	public static boolean parseBoolean(String value) {
		Boolean val = Boolean.valueOf(value);
		return val;
	}

	public String getDnshost() {
		return dnshost;
	}

	public void setDnshost(String dnshost) {
		this.dnshost = dnshost;
	}

	public String getMittenteMail() {
		return mittenteMail;
	}

	public void setMittenteMail(String mittenteMail) {
		this.mittenteMail = mittenteMail;
	}

	public String getUsername_login() {
		return username_login;
	}

	public void setUsername_login(String username_login) {
		this.username_login = username_login;
	}

	public String getPsw_login() {
		return psw_login;
	}

	public void setPsw_login(String psw_login) {
		this.psw_login = psw_login;
	}

	public Boolean getIsToLogin() {
		return isToLogin;
	}

	public void setIsToLogin(Boolean isToLogin) {
		this.isToLogin = isToLogin;
	}

	public String getCharset() {
		return charset;
	}

	public File getTemplate_html_mail() {
		return template_html_mail;
	}

	public void setTemplate_html_mail(File template_html_mail) {
		this.template_html_mail = template_html_mail;
	}

	public String getUrl_istanza() {
		return url_istanza;
	}

	public void setUrl_istanza(String url_istanza) {
		this.url_istanza = url_istanza;
	}

	@Override
	public String toString() {
		return "MailProperties [dnshost=" + dnshost + ", mittenteMail=" + mittenteMail + ", username_login="
				+ username_login + ", psw_login=" + psw_login + ", isToLogin=" + isToLogin + "]";
	}

}
