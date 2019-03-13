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
package it.almaviva.cms.externalFiles;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.utilities.Util;
import it.almaviva.cms.utilities.costanti.Constants;

public class ExternalFilesManager {
	private String working_dir;
	private static Logger log = Logger.getLogger(ExternalFilesManager.class);

	public ExternalFilesManager(String working_dir) {
		super();
		this.working_dir = working_dir;
	}

	private String build_path(AttachedType allegatoType) {
		String allegato_dir = File.separator + allegatoType.toString().toLowerCase() + File.separator;
		String path = Constants.user_home + File.separator + working_dir + allegato_dir;
		return path;
	}

	private File get(String file_path) {
		file_path = cleanPath(file_path);
		File file = new File(file_path);
		if (file.exists()) {
			log.info("Trovato file: " + file_path);

			return file;
		} else {
			log.error("NON trovato file: " + file_path);
			return null;
		}
	}

	
	public ResponseType put(String nomefile, MultipartFile file, AttachedType allegatoType) {
		String destination_path = build_path(allegatoType) + "" + nomefile+ "";
		File presente = getAllegato(nomefile.substring(0, nomefile.indexOf(".")), allegatoType);
		if (Util.isFilled(presente) && presente.exists()) {
			presente.delete();
		}
		destination_path = cleanPath(destination_path);
		
		put(destination_path, file );
		return ResponseType.OK;
	}

	public File getAllegato(String nomeNoPrefix, AttachedType allegatoType) {
		//String filename =  + nomeNoPrefix + "";
		String file_path = build_path(allegatoType);
		String complete_path = file_path  + nomeNoPrefix;
		file_path = cleanPath(file_path);
		complete_path = cleanPath(complete_path);
		File folder = new File(file_path);
		File[] listOfFiles = folder.listFiles();
		log.info("Richiesto file: " + file_path);

		for(File file: listOfFiles) {
			String name = file.getName();
			String extention = name.substring(name.indexOf(".") + 1);
			if(name.indexOf(nomeNoPrefix) != -1)
				return get(complete_path + "." + extention);
		
		}
	
		return null;
	}

	private String cleanPath(String path) {
		path = path.replace("/", File.separator);
		return path.replace("\\", File.separator).toLowerCase();
	}

	private Boolean put(String destination_path, MultipartFile file) {
		try {
		File destinationFile = new File(destination_path.toLowerCase());

			// copio l immagine, attenzione sovrascrive il file
			file.transferTo(destinationFile);
			//ImageIO.write(bfimg, extention.replace(".", ""), destinationFile);
		} catch (IOException e) {
			log.error(e);
			return false;
		}
		return true;
	}

}
