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
package it.almaviva.cms.services.bibliotecheImp;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.almaviva.cms.dbManager.beans.Biblioteche;
import it.almaviva.cms.services.bibliotecheImp.BibliotecaModel.Status;

public class BibliotecheOpacImporter {
	private Logger log = Logger.getLogger(BibliotecheOpacImporter.class);

	private RestTemplate restService = new RestTemplate();

	private Biblioteche callOpac(String cod_polo, String cod_biblioteca, String opacUrl ) {
		BibliotecaModel imported = null;
		try {
			log.info("Importing from opac: " + cod_polo + cod_biblioteca);
			imported = restService.getForObject(opacUrl, BibliotecaModel.class, cod_polo, cod_biblioteca);
			if(imported.getStatus() == Status.NOT_FOUND)
				return null;
			return convert(imported);
		} catch (RestClientException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	public BibliotecheOpacImporter() {
		
	}

	public Biblioteche startImport(String cod_polo, String cod_bibblioteca, String opacWSUrl) {
		assert cod_polo != null : "Not null";
		return callOpac(cod_polo.toUpperCase(), cod_bibblioteca.toUpperCase(), opacWSUrl);
	}
	private Biblioteche convert(BibliotecaModel bib) {
		Biblioteche bibDb = new Biblioteche();
		bibDb.setCod_bib(bib.getCod_bib());
		bibDb.setCod_polo(bib.getCod_polo());
		bibDb.setFl_canc("n");
		bibDb.setNome(bib.getNome());
		return bibDb;
	}
}
