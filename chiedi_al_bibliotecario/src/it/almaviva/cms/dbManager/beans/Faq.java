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
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "faq")
@Entity(name = "faq")
@NamedQueries({ @NamedQuery(name = "faq.All", query = "SELECT f FROM faq f WHERE lower(f.fl_canc) <> 's' ORDER BY f.cd_faq, f.ts_ins"),
		@NamedQuery(name = "faq.findByID", query = "SELECT f FROM faq f  WHERE f.id = :id") })
public class Faq {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String domanda;
	private String risposta;
	private String fl_canc;
	private String cd_faq;
	private String cod_polo;

	@JsonIgnore
	private Timestamp ts_ins;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomanda() {
		return domanda;
	}

	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}

	public String getRisposta() {
		return risposta;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public String getCd_faq() {
		return cd_faq;
	}

	public void setCd_faq(String cd_faq) {
		this.cd_faq = cd_faq;
	}

	public Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(Timestamp ts_ins) {
		this.ts_ins = ts_ins;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}
	public static List<Faq> filterByCodPolo(List<Faq> list, String cod_polo) {
		
		List<Faq> filteredFaqsByPolo = list.stream()
		 		.filter(faq -> cod_polo.equals(faq.getCod_polo()))
		 		.collect(Collectors.toList());
		
		return filteredFaqsByPolo;
	}
}
