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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "tickets")
@Entity(name = "tickets")
@NamedQueries({

		@NamedQuery(name = "tickets.All", query = " SELECT t FROM tickets t WHERE lower(t.fl_canc) <> 's' "),
		@NamedQuery(name = "tickets.AllOrderedById", query ="SELECT t FROM tickets t WHERE lower(t.fl_canc) <> 's' ORDER BY t.id "),
		@NamedQuery(name = "tickets.AllOrderedTsIns", query ="SELECT t FROM tickets t WHERE lower(t.fl_canc) <> 's' ORDER BY t.ts_ins "),

		@NamedQuery(name = "tickets.findById", query = "SELECT t FROM tickets t WHERE t.id = :id"),
		@NamedQuery(name = "tickets.findByUserIns", query = "SELECT t FROM tickets t WHERE t.user_ins = :user_ins"),
		@NamedQuery(name = "tickets.findByIdBib", query = "SELECT t FROM tickets t WHERE t.cod_biblioteca_ind = :cod_biblioteca_ind"),
		@NamedQuery(name = "tickets.findByIdCat", query = "SELECT t FROM tickets t WHERE t.id_cat = :id_cat")})
public class Tickets {
	//Attenzione se modificate i campi qui vanno modificati anche nella query con la join per la ricerca!
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long id_cat;
	private String user_ins;
	private String titolo;
	private String testo;
	private Boolean allegato, aperto, pubblico;
	private Timestamp ts_ins;
	private String user_assegnato;
	private String fl_canc;
	private String motivo_richiesta;
	private String ho_gia_fatto;
	private String cod_biblioteca_ind;
	private String cod_polo;
	@OneToMany(mappedBy = "ticket")
	private List<Messaggi> messaggi;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser_ins() {
		return user_ins;
	}

	public void setUser_ute_ins(String user_ins) {
		this.user_ins = user_ins;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}


	public Boolean getAllegato() {
		return allegato;
	}

	public void setAllegato(Boolean allegato) {
		this.allegato = allegato;
	}

	public Boolean getAperto() {
		return aperto;
	}

	public void setAperto(Boolean aperto) {
		this.aperto = aperto;
	}

	public Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(Timestamp ts_ins) {
		this.ts_ins = ts_ins;
	}

	public Long getId_cat() {
		return id_cat;
	}

	public void setId_cat(Long id_cat) {
		this.id_cat = id_cat;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public String getUser_assegnato() {
		return user_assegnato;
	}

	public void setUser_assegnato(String user_assegnato) {
		this.user_assegnato = user_assegnato;
	}

	public String getMotivo_richiesta() {
		return motivo_richiesta;
	}

	public void setMotivo_richiesta(String motivo_richiesta) {
		this.motivo_richiesta = motivo_richiesta;
	}

	public String getHo_gia_fatto() {
		return ho_gia_fatto;
	}

	public void setHo_gia_fatto(String ho_gia_fatto) {
		this.ho_gia_fatto = ho_gia_fatto;
	}

	public void setUser_ins(String user_ins) {
		this.user_ins = user_ins;
	}

	public Boolean getPubblico() {
		return pubblico;
	}

	public void setPubblico(Boolean pubblico) {
		this.pubblico = pubblico;
	}

	public String getCod_biblioteca_ind() {
		return cod_biblioteca_ind;
	}

	public void setCod_biblioteca_ind(String cod_biblioteca_ind) {
		this.cod_biblioteca_ind = cod_biblioteca_ind;
	}

	public List<Messaggi> messaggi() {
		List<Messaggi> notDeletedMessages = messaggi.stream()
	    .filter(m -> "n".equals(m.getFl_canc())).collect(Collectors.toList());
		Collections.sort(notDeletedMessages, new Comparator<Messaggi>() {

			@Override
			public int compare(Messaggi o1, Messaggi o2) {
				return o1.getTs_ins().compareTo(o2.getTs_ins());
			}
			
		});
		return notDeletedMessages;
		
	}

	public void setMessaggi(List<Messaggi> messaggi) {
		
		this.messaggi = messaggi;
	}
	public Integer getMessagesCount() {
		List<Messaggi> filteredMessage = messaggi();
		return (filteredMessage != null) ? filteredMessage.size() : 0;
	}
	public static void sorTicketsByTs_ins(List<Tickets> resultList2) {
		Collections.sort(resultList2, new Comparator<Tickets>() {
			@Override
			public int compare(Tickets o1, Tickets o2) {
				return - (o1.getTs_ins().compareTo(o2.getTs_ins()));
			}
			
		} );
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}
public static List<Tickets> filterByCodPolo(List<Tickets> tickets, String cod_polo) {
		
		List<Tickets> filteredByPolo = tickets.stream()
		 		.filter(faq -> cod_polo.equals(faq.getCod_polo()))
		 		.collect(Collectors.toList());
		
		return filteredByPolo;
	}
}
