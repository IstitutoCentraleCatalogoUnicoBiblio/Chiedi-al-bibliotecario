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

import org.springframework.web.multipart.MultipartFile;

import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.DatabaseQueryDao;
import it.almaviva.cms.dbManager.beans.Biblioteche;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.externalFiles.AttachedType;
import it.almaviva.cms.services.mailer.MailerModel;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

public interface ServicesInt {

	DatabaseQueryDao database();

	String systemProperty(String props);

	String toJson(Object obj);

	ResponseType sendMail(MailerModel mailer);

	File getFile(String nomefile, AttachedType allegatoType);

	ResponseType putFile(String nomefile, MultipartFile file, AttachedType allegatoType);

	void reloadProps();
	
	SbnwebType loginSbnWeb(Utenti sbnUser);
	
	Biblioteche importaBibliotecaDaOpac(String cod_polo, String cod_biblioteca);
	

}
