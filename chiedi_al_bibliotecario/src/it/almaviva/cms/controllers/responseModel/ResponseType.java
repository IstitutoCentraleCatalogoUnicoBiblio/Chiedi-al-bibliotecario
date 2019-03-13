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

public enum ResponseType {
	OK, UNKNOWN, ERROR, USR_PSW_WRONG, PSW_WRONG, USR_EXIST, USR_NOT_FOUND,  TICKET_NOT_FOUND, DB_ERROR, MESSAGE_ERROR, MESSAGE_MAIL_WRONG,MAIL_ALREADY_REGISTERED, NOT_ALLOWED, PSW_NOT_SAME, BIB_NOT_IMPORT, NOT_PSW_CAN_CHANGE_SBNWEB, UTENZA_NON_SBNWEB;

	public static ResponseModel build_response(ResponseType resp, ReturnModelInt obj) {
		return new ResponseModel(resp.toString(), obj);

	}
}
