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
package it.almaviva.cms.models.tickets;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.almaviva.cms.dbManager.beans.Messaggi;
import it.almaviva.cms.dbManager.beans.Tickets;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.models.ReturnModelInt;


public class DettaglioTicketModel implements ReturnModelInt{
	private List<Messaggi> messaggi;
	private Tickets ticket;
	@JsonIgnore
	private Utenti utenteToMail;
	
	public DettaglioTicketModel(List<Messaggi> messaggi, Tickets ticket, Utenti utenteToMail) {
		super();
		this.messaggi = messaggi;
		this.ticket = ticket;
		this.utenteToMail = utenteToMail;
	}

	public List<Messaggi> getMessaggi() {
		return messaggi;
	}

	public void setMessaggi(List<Messaggi> messaggi) {
		this.messaggi = messaggi;
	}

	public Tickets getTicket() {
		return ticket;
	}

	public void setTicket(Tickets ticket) {
		this.ticket = ticket;
	}

	public Utenti getUtenteToMail() {
		return utenteToMail;
	}

	public void setUtenteToMail(Utenti utenteToMail) {
		this.utenteToMail = utenteToMail;
	}	

}
