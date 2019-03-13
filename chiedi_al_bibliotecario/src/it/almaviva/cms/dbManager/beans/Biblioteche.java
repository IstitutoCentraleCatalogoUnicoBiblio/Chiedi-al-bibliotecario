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

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name = "biblioteche")
@Table(name = "biblioteche")
@NamedQueries({ @NamedQuery(name = "biblioteche.All", query = "SELECT b FROM biblioteche b WHERE lower(b.fl_canc) <> 's'"),
		@NamedQuery(name = "biblioteche.findByCod", query = "SELECT b FROM biblioteche b WHERE b.cod_bib = :cd_bib") })
public class Biblioteche {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "cd_bib")
	private String cod_bib;
	private String nome;
	private String fl_canc;
	private String cod_polo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCod_bib() {
		return cod_bib;
	}

	public void setCod_bib(String cod_bib) {
		this.cod_bib = cod_bib;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}
public static List<Biblioteche> filterByCodPolo(List<Biblioteche> list, String cod_polo) {
		
		List<Biblioteche> filteredByPolo = list.stream()
		 		.filter(faq -> cod_polo.equals(faq.getCod_polo()))
		 		.collect(Collectors.toList());
		
		return filteredByPolo;
	}
}
