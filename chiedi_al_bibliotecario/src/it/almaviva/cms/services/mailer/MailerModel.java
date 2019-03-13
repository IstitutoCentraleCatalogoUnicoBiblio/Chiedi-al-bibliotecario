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

import java.util.List;

import it.almaviva.cms.dbManager.beans.Utenti;

public class MailerModel {
	private List<Utenti> users;
	private String text;
	private String oggetto;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public List<Utenti> getUsers() {
		return users;
	}

	public void setUsers(List<Utenti> users) {
		this.users = users;
	}

	public MailerModel(List<Utenti> users, String text, String oggetto) {
		super();
		this.users = users;
		this.text = text;
		this.oggetto = oggetto;
	}

	public MailerModel() {
	}


}
