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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.boot.appVersion.Version;
import it.almaviva.boot.appVersion.VersioningReader;
import it.almaviva.cms.dbManager.beans.Biblioteche;
import it.almaviva.cms.dbManager.beans.Categorie;
import it.almaviva.cms.dbManager.beans.Faq;
import it.almaviva.cms.dbManager.beans.PoloConf;
import it.almaviva.cms.services.ServicesImpl;
import it.almaviva.cms.utilities.Util;
import it.almaviva.cms.utilities.costanti.Constants;

@Controller
public class IndexController {
	private Logger log = Logger.getLogger(IndexController.class);
	protected ModelAndView mv = new ModelAndView("index");

	static class SystemProps {
		Version appVersion = VersioningReader.getVersion();
		String applicationName;
	}

	@Autowired
	ServicesImpl services;

	private List<Categorie> cachedCategories;
	private List<Faq> cachedFaq;
	private List<Biblioteche> cachedBiblioteche;
	private List<PoloConf> cachedPoli;

	private SystemProps sp;

	protected void init() {
		if (!Util.isFilled(cachedCategories))
			reloadCategories();

		if (!Util.isFilled(cachedFaq))
			reloadFaqs();
		if (!Util.isFilled(cachedBiblioteche))
			reloadBiblioteche();
		if (!Util.isFilled(sp)) {
			reloadApplicationSettings();
		}
		if(!Util.isFilled(cachedPoli)) {
			reloadPoliConf();
		}

	}
	
	 void prepareMvByCodPolo(String cod_polo) {
		if(!Util.isFilled(cod_polo))
			return;
		
	List<Categorie> filteredCategoriesByPolo = Categorie.filterByCodPolo(cachedCategories, cod_polo);
	
	List<Faq> filteredFaqsByPolo = Faq.filterByCodPolo(cachedFaq, cod_polo);
	
	List<Biblioteche> filteredBibsByPolo = Biblioteche.filterByCodPolo(cachedBiblioteche, cod_polo);
	
	mv.addObject("categories", services.toJson(filteredCategoriesByPolo));
	mv.addObject("faqs", services.toJson(filteredFaqsByPolo));
	mv.addObject("biblioteche", services.toJson(filteredBibsByPolo));
	mv.addObject("poli_disponibili", services.toJson(Constants.dummyObj));

	}
	
	@GetMapping("/")
	public ModelAndView getIndex() {
		init();
		
		mv.addObject("categories", services.toJson(Constants.dummyObj));
		mv.addObject("faqs", services.toJson(Constants.dummyObj));
		mv.addObject("biblioteche", services.toJson(Constants.dummyObj));
		mv.addObject("conf_polo", services.toJson(Constants.dummyObj));
		mv.addObject("poli_disponibili", services.toJson(cachedPoli));
		mv.addObject("appBuild", services.toJson(sp));
		mv.addObject("appTitle", sp.applicationName);
		return mv;
	}
	@GetMapping("/{cod_polo:[3]*[A-Z|0-9]+}")
	public ModelAndView selectPolo(@PathVariable String cod_polo) {
		init();
		PoloConf polo = services.database().getPolo(cod_polo);
		if(!Util.isFilled(polo)) {
			return redirect();
		}
		
		mv = getIndex();
		prepareMvByCodPolo(cod_polo);
		mv.addObject("conf_polo", services.toJson(polo));
		
		return mv;
	}

	@GetMapping({"/restart","/reload"})
	public String restart() {
		services.reloadProps();

		reloadApplicationSettings();
		reloadCategories();
		reloadFaqs();
		reloadBiblioteche();
		reloadPoliConf();
		init();
		return "redirect: ...";
	}



	@GetMapping("/reloadCategories")
	public String reloadCategories() {
		log.info("Richiesto reload cache delle categorie");
		try {
			cachedCategories = services.database().getCategorie();
		} catch (Exception e) {
			log.error(e);
		}

		return "redirect: ...";
	}

	@GetMapping("/reloadFaqs")
	public String reloadFaqs() {
		log.info("Richiesto reload cache delle faq");
		try {
			cachedFaq = services.database().getFaqs();
		} catch (Exception e) {
			log.error(e);
		}

		return "redirect: ...";
	}

	@GetMapping("/reloadBiblioteche")
	public String reloadBiblioteche() {
		log.info("Richiesto reload cache delle biblioteche");
		try {
			cachedBiblioteche = services.database().getBiblioteche();
		} catch (Exception e) {
			log.error(e);
		}

		return "redirect: ...";
	}
	@GetMapping("/reloadPoliConf")
	public String reloadPoliConf() {
		log.info("Richiesto reload cache delle biblioteche");
		try {
			cachedPoli = services.database().getPoli();
		} catch (Exception e) {
			log.error(e);
		}

		return "redirect: ...";
	}

	@GetMapping("/reloadSettings")
	public String reloadApplicationSettings() {

		sp = new SystemProps();
		String nameApp = services.systemProperty("APPLICATION_NAME");
		sp.applicationName = (Util.isFilled(nameApp) ? nameApp : "Chiedi al bibliotecario");
		return "redirect: ...";

	}
	@GetMapping({ "/segnalazioni",
		"/admin",
		"/dettaglio/{id}",
		"/nuovaSegnalazione",
		"/modificaSegnalazione",
		"/profilo/{user}",
		"/FAQ",
		"/benvenuto",
		"/login", 
		"/index"})
	public ModelAndView redirect() {
		return new ModelAndView("redirect:/");
	}
	@GetMapping({ "/{cod_polo}/segnalazioni",
		"/{cod_polo}/admin",
		"/{cod_polo}/dettaglio/{id}",
		"/{cod_polo}/nuovaSegnalazione",
		"/{cod_polo}/modificaSegnalazione",
		"/{cod_polo}/profilo/{user}",
		"/{cod_polo}/FAQ",
		"/{cod_polo}/benvenuto",
		"/{cod_polo}/login", 
		"/{cod_polo}/index"})
	public ModelAndView redirect(@PathVariable String cod_polo) {

		return new ModelAndView("redirect:/" + cod_polo);
	}

}
