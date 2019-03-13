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
package it.almaviva.cms.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.cms.controllers.responseModel.ResponseModel;
import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.beans.Biblioteche;
import it.almaviva.cms.dbManager.beans.Tickets;
import it.almaviva.cms.models.ResponseImpBiblioteche;
import it.almaviva.cms.services.ServicesImpl;
import it.almaviva.cms.utilities.Util;

@RestController
public class BibliotecheImportController {
	@Autowired
	ServicesImpl services;

	protected final static Logger log = Logger.getLogger(BibliotecheImportController.class);

	@GetMapping("biblioteche/import/{cd_polo}/{cd_bib}")
	public ResponseModel importBib(@PathVariable String cd_polo, @PathVariable String cd_bib) {
		Biblioteche bibliotecaImportata = services.importaBibliotecaDaOpac(cd_polo, cd_bib);
		List<Biblioteche> biblioteche = services.database().getBiblioteche();
		List<Biblioteche> filteredBibsByPolo = Biblioteche.filterByCodPolo(biblioteche, cd_polo);


		return (Util.isFilled(bibliotecaImportata)
				? ResponseType.build_response(ResponseType.OK,
						new ResponseImpBiblioteche(filteredBibsByPolo, bibliotecaImportata))
				: ResponseType.build_response(ResponseType.BIB_NOT_IMPORT,
						new ResponseImpBiblioteche(filteredBibsByPolo, bibliotecaImportata)));

	}

	@GetMapping("biblioteche/get/{cod_polo:[3]*[A-Z|0-9]+}")
	public List<Biblioteche> getAll(@PathVariable String cod_polo) {

		List<Biblioteche> filteredBibsByPolo = Biblioteche.filterByCodPolo(services.database().getBiblioteche(), cod_polo);

		return filteredBibsByPolo;
	}

	@PostMapping("biblioteche/delete/{cod_polo}/{cd_bib}")
	public List<Biblioteche> delete(@RequestBody Biblioteche bibToDelete, @PathVariable String cod_polo) {
		services.database().deleteBiblioteca(bibToDelete);
		return getAll(cod_polo);
	}
	@PostMapping("biblioteche/checkTicketsAssociati")
	public List<Tickets> checkTicketsAssociati(@RequestBody Biblioteche bibToDelete) {
		List<Tickets> checkTicketAssociatiBib = services.database().checkTicketAssociatiBib(bibToDelete);
		return checkTicketAssociatiBib;
	}
	@PostMapping("biblioteche/add")
	public ResponseModel add(@RequestBody Biblioteche biblioteca) {
		
		if(!Util.isFilled(biblioteca) || !Util.isFilled(biblioteca.getCod_bib())) {
			List<Biblioteche> biblioteche = services.database().getBiblioteche();
			List<Biblioteche> filteredBibsByPolo = Biblioteche.filterByCodPolo(biblioteche, biblioteca.getCod_polo());

			return ResponseType.build_response(ResponseType.BIB_NOT_IMPORT,
					new ResponseImpBiblioteche(filteredBibsByPolo, null));
		}
			
		Biblioteche bibliotecaImportata = services.database().addBiblioteca(biblioteca);
		List<Biblioteche> bibliotecheUdated = services.database().getBiblioteche();
		List<Biblioteche> filteredBibsByPolo = Biblioteche.filterByCodPolo(bibliotecheUdated, biblioteca.getCod_polo());

		return (Util.isFilled(bibliotecaImportata)
				? ResponseType.build_response(ResponseType.OK,
						new ResponseImpBiblioteche(filteredBibsByPolo, biblioteca))
				: ResponseType.build_response(ResponseType.BIB_NOT_IMPORT,
						new ResponseImpBiblioteche(filteredBibsByPolo, biblioteca)));

	}
}
