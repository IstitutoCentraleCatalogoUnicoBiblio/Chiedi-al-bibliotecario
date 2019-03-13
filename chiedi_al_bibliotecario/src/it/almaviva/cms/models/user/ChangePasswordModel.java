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

public class ChangePasswordModel {
	private String username, new_psw, old_psw, repeat_new_psw, cod_polo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNew_psw() {
		return new_psw;
	}

	public void setNew_psw(String new_psw) {
		this.new_psw = new_psw;
	}

	public String getOld_psw() {
		return old_psw;
	}

	public void setOld_psw(String old_psw) {
		this.old_psw = old_psw;
	}

	public String getRepeat_new_psw() {
		return repeat_new_psw;
	}

	public void setRepeat_new_psw(String repeat_new_psw) {
		this.repeat_new_psw = repeat_new_psw;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}

}
