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
package it.almaviva.cms.sbnwebLogin;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import it.almaviva.cms.dbManager.beans.PoloConf;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.utilities.Util;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

public class SbnWebServicesImpl implements SbnWebServicesInt {
	private Logger log = Logger.getLogger(SbnWebServicesImpl.class);
	private RestTemplate restService = new RestTemplate();

	@Override
	public SbnwebType login(Utenti user, PoloConf polo) {

		SbnwebType result = null;
		if(polo == null) { 
			return null;
		}
		if(!Util.isFilled(polo.getUrl_sbnweb_login_service()))
			return null;
			
		String urlSbnWebServizi = polo.getUrl_sbnweb_login_service();
		if (urlSbnWebServizi.indexOf("http") == -1)
			urlSbnWebServizi = "http://" + urlSbnWebServizi;
	
		try {
			result = restService.getForObject(urlSbnWebServizi, SbnwebType.class, user.getUsername(),
					user.getPassword());
		} catch (HttpClientErrorException e) {
			log.error("Errore del client", e);
			return null;
		} catch (Exception e) {
			//campo non impostato
			log.error("Exception", e);
			return null;
		}
		return result;
	}

	public SbnWebServicesImpl() {
		super();

		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

}
