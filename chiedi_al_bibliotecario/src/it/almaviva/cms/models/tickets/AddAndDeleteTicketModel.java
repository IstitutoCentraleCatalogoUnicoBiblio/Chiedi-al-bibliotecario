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

import it.almaviva.cms.dbManager.beans.Tickets;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.models.ReturnModelInt;

public class AddAndDeleteTicketModel implements ReturnModelInt {

	private Tickets newTicket;
	private List<Tickets> tickets;
	
	@JsonIgnore
	private Utenti utenteToMail;
	
	public AddAndDeleteTicketModel(Tickets newTicket, List<Tickets> allTickets, Utenti utenteToMail) {
		this.newTicket = newTicket;
		this.tickets = allTickets;
		this.utenteToMail = utenteToMail;
	}

	public Tickets getNewTicket() {
		return newTicket;
	}

	public void setNewTicket(Tickets newTicket) {
		this.newTicket = newTicket;
	}

	public List<Tickets> getTickets() {
		return tickets;
	}

	public void setTickets(List<Tickets> tickets) {
		this.tickets = tickets;
	}

	public Utenti getUtenteToMail() {
		return utenteToMail;
	}

	public void setUtenteToMail(Utenti utenteToMail) {
		this.utenteToMail = utenteToMail;
	}

}
