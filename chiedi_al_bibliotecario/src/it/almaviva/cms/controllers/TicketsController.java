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

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.cms.controllers.responseModel.ResponseModel;
import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.beans.Tickets;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.models.ReturnModelInt;
import it.almaviva.cms.models.tickets.AddAndDeleteTicketModel;
import it.almaviva.cms.models.tickets.CloseTicketModel;
import it.almaviva.cms.models.tickets.DettaglioTicketModel;
import it.almaviva.cms.models.user.LoggedinModel;
import it.almaviva.cms.services.ServicesImpl;
import it.almaviva.cms.services.mailer.MailerModel;
import it.almaviva.cms.services.mailer.MailerModelBuilder;
import it.almaviva.cms.utilities.Util;
import it.almaviva.cms.utilities.costanti.Constants;

@RestController
public class TicketsController {
	protected final static Logger log = Logger.getLogger(TicketsController.class);
	@Autowired
	ServicesImpl services;
	private ResponseType sendEmail(Utenti utenteToMail, String text) {
		String oggettoMail = services.systemProperty("EMAIL_OGGETTO");
		MailerModel mailerModel = new MailerModelBuilder()
				.setOggetto(oggettoMail)
				.setUsers(utenteToMail)
				.setText(text)
				.build();
		return services.sendMail(mailerModel);
	}

	@PostMapping(value = "/tickets/add")
	public ResponseModel add(@RequestBody Tickets ticket) {
		ResponseModel addTickets = services.database().addTickets(ticket);
		if (addTickets.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = addTickets.getResponseObj();

			if (responseObj instanceof AddAndDeleteTicketModel) {
				AddAndDeleteTicketModel response = (AddAndDeleteTicketModel) addTickets.getResponseObj();
				
				ResponseType sent = sendEmail(response.getUtenteToMail(),
						"La segnalazione da te effettuata è stata inserita con il codice: " + response.getNewTicket().getId());
				addTickets.setKey(sent);
				

			}

		}
		 
		return addTickets;
	}

	@PostMapping(value = "/tickets/delete")
	public ResponseModel delete(@RequestBody Tickets ticket) {
		ticket.setFl_canc("s");
		ResponseModel responseQuery = services.database().updateTicket(ticket);
		if (responseQuery.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = responseQuery.getResponseObj();

			if (responseObj instanceof DettaglioTicketModel) {
				DettaglioTicketModel response = (DettaglioTicketModel) responseQuery.getResponseObj();
				ResponseType sent = sendEmail(response.getUtenteToMail(),
						"La segnalazione n." + ticket.getId() + " è stata eliminata correttamente");
				responseQuery.setKey(sent);

			}

		}
		ResponseModel responseModel = get(0, null, ticket.getUser_ins(), ticket.getCod_polo());
		responseModel.setKey(responseQuery.getResponseKey());
		return responseModel;
	}

	@PostMapping(value = "/tickets/assign/{userToAssign}")
	public ResponseModel assign(@RequestBody Tickets ticket, @PathVariable String userToAssign) {
		Utenti userToAssignModel = services.database().getUserByUsername(userToAssign, ticket.getCod_polo());
		
		if (!Util.isFilled(userToAssignModel) && !userToAssignModel.getIsBibliotecario())
			return ResponseType.build_response(ResponseType.NOT_ALLOWED, null);

		ticket.setUser_assegnato(userToAssignModel.getUsername());
		ResponseModel addTickets = services.database().updateTicket(ticket);
		if (addTickets.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = addTickets.getResponseObj();

			if (responseObj instanceof DettaglioTicketModel) {
				DettaglioTicketModel response = (DettaglioTicketModel) addTickets.getResponseObj();
				ResponseType sent = sendEmail(response.getUtenteToMail(),
						"La tua segnalazione n." + ticket.getId() + " è stata assegnata al bibliotecario " +userToAssignModel.getUsername() 
						+" riceverai presto una risposta");
				addTickets.setKey(sent);

			}

		}
		return getDetail(ticket.getId());
	}

	@PostMapping("/tickets/{id}")
	public ResponseModel getDetail(@PathVariable Long id) {
		return services.database().getTicketDetail(id);

	}
	@PostMapping("/tickets/update")
	public ResponseModel update(@RequestBody Tickets ticket) {
		ResponseModel responseQuery = services.database().updateTicket(ticket);
		if (responseQuery.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = responseQuery.getResponseObj();

			if (responseObj instanceof DettaglioTicketModel) {
				DettaglioTicketModel response = (DettaglioTicketModel) responseQuery.getResponseObj();
				ResponseType sent = sendEmail(response.getUtenteToMail(),
						"La segnalazione n." + ticket.getId() + " è stata modificata correttamente");
				responseQuery.setKey(sent);

			}
		}
		return responseQuery;

	}
	@PostMapping("tickets/close")
	public ResponseModel close(@RequestBody CloseTicketModel ctm) {
		ResponseModel addTickets = services.database().closeTicket(ctm);
		if (addTickets.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = addTickets.getResponseObj();

			if (responseObj instanceof DettaglioTicketModel) {
				DettaglioTicketModel response = (DettaglioTicketModel) addTickets.getResponseObj();
				ResponseType sent = sendEmail(response.getUtenteToMail(),
						"La segnalazione n." + response.getTicket().getId() + " è stata evasa correttamente");
				addTickets.setKey(sent);

			}
		}

		return addTickets;
	}

	@PostMapping("tickets/get/{idToStart}/{username:.+}/{cod_polo:[3]*[A-Z|0-9]+}") 
	public ResponseModel get(@PathVariable Integer idToStart, 
			@RequestBody Map<String, String> filters, 
			@PathVariable String username, @PathVariable String cod_polo) {
		LoggedinModel tickets = null;
		Utenti userByUsername = services.database().getUserByUsername(username, cod_polo);
		if (!Util.isFilled(filters))
			filters = Constants.createMapByCodPolo(cod_polo);
		
		if(!Util.isFilled(filters.get("cod_polo"))) {
			filters.put("cod_polo", cod_polo);
		}
			
		tickets = services.database().getTickets(filters, idToStart, 5, userByUsername);
		
		return ResponseType.build_response(ResponseType.OK, tickets);

	}

}
