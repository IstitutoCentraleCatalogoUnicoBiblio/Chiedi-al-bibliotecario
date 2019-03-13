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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.cms.controllers.responseModel.ResponseModel;
import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.beans.Messaggi;
import it.almaviva.cms.dbManager.beans.Tickets;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.models.ReturnModelInt;
import it.almaviva.cms.models.tickets.MessaggioInseritoModel;
import it.almaviva.cms.services.ServicesImpl;
import it.almaviva.cms.services.mailer.MailerModel;
import it.almaviva.cms.services.mailer.MailerModelBuilder;
import it.almaviva.cms.utilities.Util;

@RestController
public class TicketMessagesController {
	@Autowired
	ServicesImpl services;
	 
	protected final static Logger log = Logger.getLogger(TicketMessagesController.class);

	private void sendMail(ResponseModel ticketDetail, String text) {
		String oggettoMail = services.systemProperty("EMAIL_OGGETTO");
		if (ticketDetail.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = ticketDetail.getResponseObj();

			if (responseObj instanceof MessaggioInseritoModel) {
				MessaggioInseritoModel response = (MessaggioInseritoModel) responseObj;
				Tickets ticket = response.getTicket();
				// Long message_id = response.getMessaggio().getId();
				Utenti utenteToMail = response.getUtenteToMail();
				MailerModel mailerModel = new MailerModelBuilder()
						.setOggetto(oggettoMail)
						.setUsers(utenteToMail)
						.setText(text)
						.build();

				ResponseType sent = services.sendMail(mailerModel);
				ticketDetail.setKey(sent);

			}

		}
	}

	@PostMapping("/messaggi/add")
	public ResponseModel addMessageToTicket(@RequestBody Messaggi messageToinsert) {

		ResponseModel ticketDetail = services.database().addMessageToTicket(messageToinsert);
		sendMail(ticketDetail, "E' stato aggiunto un commento al ticket n." + messageToinsert.getTicket_id());

		return ticketDetail;
	}

	@PostMapping("messaggi/update")
	public ResponseModel updateMessage(@RequestBody Messaggi messageToUpdate, String textMail) {

		ResponseModel ticketDetail = services.database().updateTicketMessage(messageToUpdate);
		String text = (Util.isFilled(textMail) ? textMail :"Il commento associato al ticket n." + 
								messageToUpdate.getTicket_id() + " + stato modificato con successo!");
		sendMail(ticketDetail, text);
		return ticketDetail;
	}

	@PostMapping("messaggi/delete")
	public ResponseModel deleteMessage(@RequestBody Messaggi messageToDelete) {
		messageToDelete.setFl_canc("s");
		return updateMessage(messageToDelete, "Il commento associato al ticket n." + 
				messageToDelete.getTicket_id() + " + stato eliminato con successo!");
	}

}
