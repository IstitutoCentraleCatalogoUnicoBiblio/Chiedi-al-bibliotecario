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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import it.almaviva.cms.utilities.Util;

@Entity(name = "utenti")
@Table(name = "utenti")
@NamedQueries({
		@NamedQuery(name = "utenti.findByEmail", query = "SELECT u FROM utenti u WHERE u.email = :email AND u.cod_polo = :cod_polo"),
		@NamedQuery(name = "utenti.findByUsr", query = "SELECT u FROM utenti u WHERE u.username = :username AND u.cod_polo = :cod_polo"),
		@NamedQuery(name = "utenti.getAllxPolo", query = "SELECT u FROM utenti u WHERE  lower(u.fl_canc) <> 's' AND u.cod_polo = :cod_polo"),
		@NamedQuery(name = "utenti.getBibliotecari", query = "SELECT u FROM utenti u WHERE  lower(u.fl_canc) <> 's' and u.isBibliotecario = true AND u.cod_polo = :cod_polo"),
		@NamedQuery(name = "utenti.login", query = "SELECT u FROM utenti u WHERE (lower(u.username) = lower(:usrMail) OR lower(u.email) = lower(:usrMail)) AND u.password = :password AND  lower(u.fl_canc) <> 's' AND u.cod_polo = :cod"),
})
public class Utenti {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username, email;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	private String data_nascita, fl_canc;
	private Timestamp ts_ins;
	@Column(name = "bibliotecario")
	private Boolean isBibliotecario;
	@Column(name = "admin")
	private Boolean isAdmin;
	@Column(name = "newsletter")
	private Boolean acceptNewsLetter;
	@Column(name = "psw_reset")
	private Boolean passwordIsToReset;

	@Column(name = "sbnweb")
	private Boolean sbnweb;
	private String cod_polo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//Return the automatic generated Psw
	public String resetPassword() {
		// psw == randomNum + username reset
		int generateRandomInt = Util.generateRandomInt(1000);
		String resettedPsw = generateRandomInt + getUsername();
		this.password = Util.encryptData(resettedPsw, resettedPsw);
		return resettedPsw;
	}

	public String encriptedPsw() {
		return Util.encryptData(password, password);
	}

	public String getData_nascita() {
		return data_nascita;
	}

	public void setData_nascita(String data_nascita) {
		this.data_nascita = data_nascita;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public Boolean getIsBibliotecario() {
		return isBibliotecario;
	}

	public void setIsBibliotecario(Boolean isBibliotecario) {
		this.isBibliotecario = isBibliotecario;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(Timestamp ts_ins) {
		this.ts_ins = ts_ins;
	}

	public Boolean getSbnweb() {
		return sbnweb;
	}

	public void setSbnweb(Boolean sbnweb) {
		this.sbnweb = sbnweb;
	}

	public Boolean getAcceptNewsLetter() {
		return acceptNewsLetter;
	}

	public void setAcceptNewsLetter(Boolean acceptNewsLetter) {
		this.acceptNewsLetter = acceptNewsLetter;
	}

	public Boolean getPasswordIsToReset() {
		return passwordIsToReset;
	}

	public void setPasswordIsToReset(Boolean passwordIsToReset) {
		this.passwordIsToReset = passwordIsToReset;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}

}
