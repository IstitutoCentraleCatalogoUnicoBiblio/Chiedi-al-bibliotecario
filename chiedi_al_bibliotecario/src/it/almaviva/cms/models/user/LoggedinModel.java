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
package it.almaviva.cms.models.user;

import java.util.List;

import it.almaviva.cms.dbManager.beans.Tickets;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.models.ReturnModelInt;

public class LoggedinModel implements ReturnModelInt {
	private Utenti user;
	private List<Tickets> allTickets;
	private Integer totalTickets;
	private Integer start;
	private Integer numViews;

	public Integer getTotalTickets() {
		return totalTickets;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getNumViews() {
		return numViews;
	}

	public void setNumViews(Integer numViews) {
		this.numViews = numViews;
	}

	public void setTotalTickets(Integer totalTickets) {
		this.totalTickets = totalTickets;
	}

	public Utenti getUser() {
		return user;
	}

	public void setUser(Utenti user) {
		this.user = user;
	}

	public List<Tickets> getAllTickets() {
		return allTickets;
	}

	public void setAllTickets(List<Tickets> allTickets) {
		this.allTickets = allTickets;
	}

	public LoggedinModel(Utenti user, List<Tickets> allTickets, Integer totalTickets, Integer start, Integer numViews) {
		super();
		this.user = user;
		this.allTickets = allTickets;
		this.totalTickets = totalTickets;
		this.start = start;
		this.numViews = numViews;
	}
	public LoggedinModel() {
		
	}


}
