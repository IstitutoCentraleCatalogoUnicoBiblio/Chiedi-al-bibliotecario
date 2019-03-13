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

import it.almaviva.cms.dbManager.beans.Faq;
import it.almaviva.cms.services.ServicesImpl;

@RestController
public class FaqController {
	@Autowired
	ServicesImpl services;
	

	
	@GetMapping("/faq/get/{cod_polo:[3]*[A-Z|0-9]+}")
	public List<Faq> get(@PathVariable String cod_polo) {
			
		return Faq.filterByCodPolo(services.database().getFaqs(), cod_polo) ;
	}

	@PostMapping("/faq/update")
	public List<Faq> edit(@RequestBody Faq faqToEdit) {
		List<Faq> updatedFaq = services.database().updateFaq(faqToEdit);
		
		return Faq.filterByCodPolo(updatedFaq, faqToEdit.getCod_polo()) ;
	}

	@PostMapping("/faq/delete")
	public List<Faq> delete(@RequestBody Faq faqToEdit) {
		faqToEdit.setFl_canc("s");
		return edit(faqToEdit);
	}

	@PostMapping("/faq/add")
	public List<Faq> add(@RequestBody Faq faqToAdd) {
		 List<Faq> faqs = services.database().addFaq(faqToAdd);
		return Faq.filterByCodPolo(faqs, faqToAdd.getCod_polo());

	}
}
