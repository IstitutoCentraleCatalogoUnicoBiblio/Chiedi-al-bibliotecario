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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "conf_polo")
@Entity(name = "conf_polo")
@NamedQueries({
	@NamedQuery(name = "ConfPolo.findAll", query = "SELECT cp FROM conf_polo cp WHERE lower(cp.fl_canc) <> 's' "),
	@NamedQuery(name = "ConfPolo.findByCodPolo", query = "SELECT cp FROM conf_polo cp WHERE lower(cp.fl_canc) <> 's' AND cp.cod_polo = :cod "),

})
public class PoloConf {
	@Transient
	@JsonIgnore
	private final String path_ws_opac = "/ws/get/{cod_polo}/biblioteca/{cod_biblioteca}";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	
	private String cod_polo;
	private String nome_polo;
	private String url_opac;
	private String url_sbnweb_reset_psw;
	private String url_sbnweb_login_service;
	private String fl_canc;
	
	
	public String getOpacWSImportBiblioteche () {
		return url_opac + path_ws_opac;
	}
	
	public String getCod_polo() {
		return cod_polo;
	}
	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}
	public String getNome_polo() {
		return nome_polo;
	}
	public void setNome_polo(String nome_polo) {
		this.nome_polo = nome_polo;
	}
	public String getUrl_opac() {
		return url_opac;
	}
	public void setUrl_opac(String url_opac) {
		this.url_opac = url_opac;
	}
	public String getUrl_sbnweb_reset_psw() {
		return url_sbnweb_reset_psw;
	}
	public void setUrl_sbnweb_reset_psw(String url_sbnweb_reset_psw) {
		this.url_sbnweb_reset_psw = url_sbnweb_reset_psw;
	}
	public String getUrl_sbnweb_login_service() {
		return url_sbnweb_login_service;
	}
	public void setUrl_sbnweb_login_service(String url_sbnweb_login_service) {
		this.url_sbnweb_login_service = url_sbnweb_login_service;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}
	
	
	
	
}
