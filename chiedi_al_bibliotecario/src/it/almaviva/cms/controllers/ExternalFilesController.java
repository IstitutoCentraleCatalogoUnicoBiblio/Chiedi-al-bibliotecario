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

import java.io.File;
import java.nio.file.Files;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.cms.controllers.responseModel.ResponseModel;
import it.almaviva.cms.dbManager.beans.Messaggi;
import it.almaviva.cms.externalFiles.AttachedType;
import it.almaviva.cms.models.tickets.DettaglioTicketModel;
import it.almaviva.cms.services.ServicesImpl;
import it.almaviva.cms.utilities.Util;

@Controller
public class ExternalFilesController {
	@Autowired
	ServicesImpl services;
	private static Logger log = Logger.getLogger(ExternalFilesController.class);

	private void put(MultipartFile file, String name) {
		services.putFile(name, file, AttachedType.ALLEGATO);
	}


	private ResponseEntity<byte[]> searchImage(File file) {
		// File file = new File(filePath);
		try {
			log.info("Downloading file " + file.getName());
			if (!file.exists())
				return null;
			HttpHeaders headers = new HttpHeaders();

			String mimeType = Files.probeContentType(file.toPath());
			if (mimeType != null) {
				MediaType media = MediaType.parseMediaType(mimeType);
				headers.setContentType(media);
			}

			return new ResponseEntity<byte[]>(Files.readAllBytes(file.toPath()), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@GetMapping("/files/get/servizio/logo-{cod_polo:[3]*[A-Z|0-9]+}.png")
	public ResponseEntity<byte[]> logo(@PathVariable String cod_polo) {

		String nomefile = "logo_chiedi_" + cod_polo.toLowerCase();
		File file = services.getFile(nomefile, AttachedType.SERVIZIO);
		log.debug(file);
		return searchImage(file);
	}
	
	@GetMapping("/files/get/servizio/logo-chiedi.png")
	public ResponseEntity<byte[]> logoGenerico() {

		String nomefile = "logo_chiedi";
		File file = services.getFile(nomefile, AttachedType.SERVIZIO);

		return searchImage(file);
	}
	// ticket: allegato_{idTicket}.png 
	//messaggio: allegato_{idTicket}_{idMessaggio}.png
	@GetMapping("/files/get/allegato/messaggio/{idTicket}/{idMessaggio}/{randomStringAvoidImgCache}")
	public ResponseEntity<byte[]> getAllegatoMess(@PathVariable String idTicket, @PathVariable String idMessaggio,  @PathVariable String randomStringAvoidImgCache) {
		File file = services.getFile("allegato_" + idTicket + "_" + idMessaggio, AttachedType.ALLEGATO);
		return searchImage(file);
	}

	@GetMapping("/files/get/allegato/ticket/{idTicket}/{randomStringAvoidImgCache}")
	public ResponseEntity<byte[]> getAllegatoTicket(@PathVariable String idTicket, @PathVariable String randomStringAvoidImgCache) {
		File file = services.getFile("allegato_" + idTicket, AttachedType.ALLEGATO);
		return searchImage(file);
	}

	@PostMapping("/files/put/allegato/ticket/{idTicket}")
	public ModelAndView uploadAllegatoTicket(@RequestAttribute(name = "file") MultipartFile file,
			@PathVariable Long idTicket) {
		ResponseModel ticket = services.database().getTicketDetail(idTicket);
		if (ticket.getResponseObj() instanceof DettaglioTicketModel) {
			String name = file.getOriginalFilename();
			String extention = name.substring(file.getOriginalFilename().indexOf("."));
			DettaglioTicketModel dtkm = (DettaglioTicketModel) ticket.getResponseObj();
			if (Util.isFilled(file) && dtkm.getTicket().getAllegato())
				put(file, "allegato_" + dtkm.getTicket().getId() + extention);

		}
		return new ModelAndView("index");

	}

	@RequestMapping(value = "/files/put/allegato/messaggio/{idTicket}/{idMessaggio}", method = RequestMethod.POST)
	public ModelAndView uploadAllegatoMessage(@RequestAttribute(name = "file") MultipartFile file,
			@PathVariable Long idTicket, @PathVariable Long idMessaggio) {
		Messaggi message = services.database().getMessage(idMessaggio);

		if (Util.isFilled(file) && message.getAllegato()) {
			String extention = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
			put(file, "allegato_" + idTicket + "_" + idMessaggio + extention);

		}
		return new ModelAndView("index");

	}

}
