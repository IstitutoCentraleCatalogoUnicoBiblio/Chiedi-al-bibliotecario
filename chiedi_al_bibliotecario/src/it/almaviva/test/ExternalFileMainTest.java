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
package it.almaviva.test;

import java.io.File;

import it.almaviva.cms.externalFiles.AttachedType;
import it.almaviva.cms.externalFiles.ExternalFilesManager;
import it.almaviva.cms.utilities.PropertiesLoader;

public class ExternalFileMainTest {
	PropertiesLoader pl = new PropertiesLoader();

	public static void main(String[] args) {
		PropertiesLoader pl = new PropertiesLoader();
		
		String working_dir = pl.getProps("PATH_FILES");
		ExternalFilesManager efm = new ExternalFilesManager(working_dir);
		File file = efm.getAllegato("template_html_mail", AttachedType.SERVIZIO);
		 System.out.println(file.getAbsolutePath());
		 
	}

}
