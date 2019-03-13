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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.cms.dbManager.beans.Categorie;
import it.almaviva.cms.services.ServicesImpl;

@RestController
public class CategoriesController {
	@Autowired
	ServicesImpl services;

	@PostMapping("/category/update")
	public List<Categorie> edit(@RequestBody Categorie categoryToEdit) {

		List<Categorie> cats = services.database().updateCategory(categoryToEdit);
		return Categorie.filterByCodPolo(cats, categoryToEdit.getCod_polo());
	}

	@PostMapping("/category/delete")
	public List<Categorie> delete(@RequestBody Categorie categoryToDelete) {
		Boolean categoryIsAssignedToTickets = services.database().isCategoryIsAssignedToTickets(categoryToDelete.getId());
		if(categoryIsAssignedToTickets)
			return get(categoryToDelete.getCod_polo());
		categoryToDelete.setFl_canc("s");
		List<Categorie> cats = services.database().updateCategory(categoryToDelete);
		return Categorie.filterByCodPolo(cats, categoryToDelete.getCod_polo());
	}

	@PostMapping("/category/add")
	public List<Categorie> add(@RequestBody Categorie categoryToAdd) {
		List<Categorie> cats = services.database().addCategory(categoryToAdd);
		return Categorie.filterByCodPolo(cats, categoryToAdd.getCod_polo());
	}

	@GetMapping("/category/get/{cod_polo:[3]*[A-Z|0-9]+}")
	public List<Categorie> get(@PathVariable String cod_polo) {

		return Categorie.filterByCodPolo(services.database().getCategorie(), cod_polo) ;
	}
}
