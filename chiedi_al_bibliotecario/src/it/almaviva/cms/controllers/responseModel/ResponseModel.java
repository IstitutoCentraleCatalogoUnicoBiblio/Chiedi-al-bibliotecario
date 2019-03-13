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
package it.almaviva.cms.controllers.responseModel;

import it.almaviva.cms.models.ReturnModelInt;

public class ResponseModel {
	private String reponse_key;
	private ReturnModelInt responseObj;

	public String getResponseKey() {
		return reponse_key;
	}

	public void setKey(String key) {
		this.reponse_key = key;
	}
	public void setKey(ResponseType key) {
		this.reponse_key = key.toString();
	}

	public ReturnModelInt getResponseObj() {
		return responseObj;
	}

	public void setResponseObj(ReturnModelInt responseObj) {
		this.responseObj = responseObj;
	}

	public ResponseModel(String key, ReturnModelInt responseObj) {
		super();
		this.reponse_key = key;
		this.responseObj = responseObj;
	}


}
