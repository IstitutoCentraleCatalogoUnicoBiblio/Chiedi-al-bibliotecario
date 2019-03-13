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
package it.almaviva.cms.utilities.costanti;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	// Pckages to scanner
	public final static String pkg_spring_scanner_boot = "it.almaviva.boot";
	public final static String pkg_spring_scanner_controllers = "it.almaviva.cms.controllers";
	public final static String pkg_spring_scanner_db = "it.almaviva.cms.dbManager";
	public final static String pkg_spring_services = "it.almaviva.cms.services";
	// Properties
	public static final String properties_file = ".properties";
	public static final String properties_file_name = "chiedi_al_bibliotecario";
	public static final String user_home = System.getProperty("user.home");
	public static final String props_file_path = "";
	public static final String user_deleted = "utente_cancellato";
	public static final Object dummyObj = new Object();
	
	
	public static Map<String, String> createMapByCodPolo(String cod_polo) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("cod_polo", cod_polo);
		return hashMap;
		
	}


}
