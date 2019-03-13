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

@Table(name = "categorie")
@Entity(name = "categorie")
@NamedQueries({ @NamedQuery(name = "categorie.All", query = "SELECT c FROM categorie c WHERE lower(c.fl_canc) <> 's' ORDER by c.cd_categoria, c.testo_it"),
		@NamedQuery(name = "categorie.findByID", query = "SELECT c FROM categorie c WHERE c.id = :id ORDER by c.cd_categoria, c.testo_it") })

public class Categorie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String testo_it;
	private String testo_en;
	private String fl_canc;
	private String cd_categoria;
	private String cod_polo;

	@JsonIgnore //Non mi serve: Dovuto a problema @Controller ritorna la data in formato stringa e non TimeStamp
	private Timestamp ts_ins;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTesto_it() {
		return testo_it;
	}

	public void setTesto_it(String testo_it) {
		this.testo_it = testo_it;
	}

	public String getTesto_en() {
		return testo_en;
	}

	public void setTesto_en(String testo_en) {
		this.testo_en = testo_en;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(Timestamp ts_ins) {
		this.ts_ins = ts_ins;
	}

	public String getCd_categoria() {
		return cd_categoria;
	}

	public void setCd_categoria(String cd_categoria) {
		this.cd_categoria = cd_categoria;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}
	public static List<Categorie> filterByCodPolo(List<Categorie> list, String cod_polo) {
		
		List<Categorie> filteredByPolo = list.stream()
		 		.filter(faq -> cod_polo.equals(faq.getCod_polo()))
		 		.collect(Collectors.toList());
		
		return filteredByPolo;
	}
}
