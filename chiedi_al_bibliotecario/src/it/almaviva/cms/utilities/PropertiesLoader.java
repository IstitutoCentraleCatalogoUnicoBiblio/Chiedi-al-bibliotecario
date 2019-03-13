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
package it.almaviva.cms.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.almaviva.cms.utilities.costanti.Constants;

public class PropertiesLoader {
	private Map<String, String> props = new HashMap<>();
	private final Properties p = new Properties();
	private final static Logger log = Logger.getLogger(PropertiesLoader.class);

	public PropertiesLoader() {
			load_props();
	}

	public String getProps(String key) {
		key = key.toUpperCase().trim();
		return props.get(key);
	}

	private void load_props() {
		try {
			p.load(new FileInputStream(
					Constants.user_home + File.separator + Constants.properties_file_name + Constants.properties_file));
			log.debug("------> Reading properties <----");
			Enumeration<?> e = p.propertyNames();
		
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = p.getProperty(key);
				log.debug("Key : " + key + ", Value : " + value);
				props.put(key, value);
			}
			log.debug("------> Finish properties <----");
		} catch (Exception e) {
			//NullPointer in seguito
			props = null;
			log.error("Error load props", e);
		}
	}
}
