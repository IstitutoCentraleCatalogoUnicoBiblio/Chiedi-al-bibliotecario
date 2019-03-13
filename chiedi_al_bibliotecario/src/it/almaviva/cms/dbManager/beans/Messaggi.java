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
package it.almaviva.cms.dbManager.beans;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "messaggi")
@Entity(name = "messaggi")
@NamedQueries({
		@NamedQuery(name = "messaggi.findByTicketID", query = "SELECT m FROM messaggi m WHERE m.ticket_id = :ticket_id AND  lower(m.fl_canc) <> 's' ORDER BY m.ts_ins"),
		@NamedQuery(name = "messaggi.findByID", query = "SELECT m FROM messaggi m WHERE m.id = :id AND  lower(m.fl_canc) <> 's'"),
		@NamedQuery(name = "messaggi.findByUserIns", query = "SELECT m FROM messaggi m WHERE m.username_ins = :user_ins") })
public class Messaggi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long ticket_id;
	private String testo;
	private Timestamp ts_ins;
	private String fl_canc;
	private String username_ins;

	private boolean allegato;
	@ManyToOne

	@JoinColumn(name = "ticket_id", referencedColumnName = "id", updatable = false, insertable = false)
	private Tickets ticket;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTicket_id() {
		return ticket_id;
	}

	public void setTicket_id(Long id_ticket_rif) {
		this.ticket_id = id_ticket_rif;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(Timestamp ts_ins) {
		this.ts_ins = ts_ins;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public String getUsername_ins() {
		return username_ins;
	}

	public void setUsername_ins(String username_ins) {
		this.username_ins = username_ins;
	}

	public boolean getAllegato() {
		return allegato;
	}

	public void setAllegato(boolean allegato) {
		this.allegato = allegato;
	}
}
