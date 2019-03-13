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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.cms.controllers.responseModel.ResponseModel;
import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.models.ReturnModelInt;
import it.almaviva.cms.models.user.ChangePasswordModel;
import it.almaviva.cms.models.user.LoggedinModel;
import it.almaviva.cms.models.user.LostPasswordModel;
import it.almaviva.cms.services.ServicesImpl;
import it.almaviva.cms.services.mailer.MailerModel;
import it.almaviva.cms.services.mailer.MailerModelBuilder;
import it.almaviva.cms.utilities.Util;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

@RestController
public class UserController {
	@Autowired
	ServicesImpl services;

	private ResponseType sendMail(Utenti user, String text) {
		String oggettoMail = services.systemProperty("EMAIL_OGGETTO");
		MailerModel mailerModel = new MailerModelBuilder()
				.setOggetto(oggettoMail)
				.setUsers(user)
				.setText(text)
				.build();
		return services.sendMail(mailerModel);
	}

	protected final static Logger log = Logger.getLogger(UserController.class);

	@PostMapping("/user/register")
	public ResponseModel register(@RequestBody Utenti userToRegister) {
		ResponseModel response = services.database().addUser(userToRegister, false);
		ReturnModelInt responseObj = response.getResponseObj();
		if(responseObj instanceof LoggedinModel) {
			LoggedinModel user = (LoggedinModel) responseObj;
			sendMail(user.getUser(), "Benvenuto su Chiedi al Bibliotecario, grazie per aver effettuato l'iscrizione");

		}
		return response;
	}

	@PostMapping("/user/login")
	public ResponseModel login(@RequestBody Utenti userLogin) {
		ResponseModel response = null;
		SbnwebType loginSbnWeb = services.loginSbnWeb(userLogin);
		if (Util.isFilled(loginSbnWeb)) {
			Utenti userByUsername = services.database().getUserByUsername(loginSbnWeb.getUtente().getUserId(), userLogin.getCod_polo());
			if (!Util.isFilled(userByUsername)) {
				Utenti userToRegister = new Utenti();
				userToRegister.setUsername(loginSbnWeb.getUtente().getUserId());
				userToRegister.setSbnweb(true);
				userToRegister.setCod_polo(userLogin.getCod_polo());
				userToRegister.setPassword(userLogin.getPassword());
				for (String email : loginSbnWeb.getUtente().getEmail()) {
					userToRegister.setEmail(email);
					break;
				}
				response = services.database().addUser(userToRegister, true);
				
				if (response.getResponseKey().equals(ResponseType.OK.toString()))
					sendMail(userToRegister, "Benvenuto su Chiedi al Bibliotecario, grazie per aver effettuato l'accesso con i servizi di SBNWeb");
			} else {
				response = services.database().loginFromSbnweb(userByUsername);
			}
		} else
			response = services.database().login(userLogin);

		return response;
	}

	@PostMapping("/user/update")
	public ResponseModel update(@RequestBody Utenti userToUpdate) {
		ResponseModel response = services.database().updateUser(userToUpdate, false);
		if (response.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = response.getResponseObj();

			if (responseObj instanceof LoggedinModel) {
				Utenti user = ((LoggedinModel) responseObj).getUser();
				ResponseType sendMail = sendMail(user, "Il tuo profilo è stato aggiornato con le modifiche da te effettuate");
				response.setKey(sendMail);;
			}

		}
		return response;
	}

	@PostMapping("/user/delete")
	public ResponseModel delete(@RequestBody Utenti user) {
			services.database().userWasDeleted(user.getUsername());
		user.setFl_canc("S");
		return services.database().updateUser(user, true);
	}

	@PostMapping("/user/changePassword")
	public ResponseModel changePsw(@RequestBody ChangePasswordModel userPsw) {

		ResponseModel changePsw = services.database().changePsw(userPsw);
		if (changePsw.getResponseKey().equals(ResponseType.OK.toString())) {
			ReturnModelInt responseObj = changePsw.getResponseObj();

			if (responseObj instanceof LoggedinModel) {
				Utenti user = ((LoggedinModel) responseObj).getUser();
				ResponseType sendMail = sendMail(user, "Hai cambiato la password del tuo profilo");
				changePsw.setKey(sendMail);;
			}

		}
		return changePsw;
	}


	@PostMapping("/user/resetPassword")
	public ResponseModel resetPsw(@RequestBody Utenti userPsw) {
		if(userPsw.getSbnweb())
			return ResponseType.build_response(ResponseType.NOT_PSW_CAN_CHANGE_SBNWEB, null);
		Utenti utente = services.database().resetPsw(userPsw);
		
		ResponseType sendMail = sendMail(utente,
				"La tua password temporanea è: " + utente.getPassword());
		return ResponseType.build_response(sendMail, null);
	}
	@PostMapping("/user/passwordDimenticata")
	public ResponseModel  resetPsw(@RequestBody LostPasswordModel usrMail) {
		Utenti userByUsername = services.database().getUserByEmail(usrMail.getEmail(), usrMail.getCod_polo());
		if(!Util.isFilled(userByUsername))
			return ResponseType.build_response(ResponseType.USR_NOT_FOUND, null);
		
		if(userByUsername.getSbnweb())
			return ResponseType.build_response(ResponseType.NOT_PSW_CAN_CHANGE_SBNWEB, null);
		
		return resetPsw(userByUsername);
	}
	@PostMapping("/user/checkiIfNotSbnWeb")
	public ResponseModel  checkIfNotSBNWeb(@RequestBody LostPasswordModel usrMail) {
		Utenti userByUsername = services.database().getUserByEmail(usrMail.getEmail(), usrMail.getCod_polo());
		if(!Util.isFilled(userByUsername))
			return ResponseType.build_response(ResponseType.USR_NOT_FOUND, null);
		
		if(!userByUsername.getSbnweb())
			return ResponseType.build_response(ResponseType.UTENZA_NON_SBNWEB, null);

		return ResponseType.build_response(ResponseType.OK, null);
	}
	@PostMapping("/user/getAll")
	public List<Utenti> getUsers(@RequestBody Utenti usr) {
		Utenti userByUsername = services.database().getUserByUsername(usr.getUsername(), usr.getCod_polo());
		return (Util.isFilled(userByUsername) && userByUsername.getIsAdmin()) ? services.database().getAllUsers(userByUsername.getCod_polo())
				: new ArrayList<>();
	}

	@PostMapping("/user/getBibliotecari")
	public List<Utenti> getBibliotecari(@RequestBody Utenti usr) {
		Utenti admin = services.database().getUserByUsername(usr.getUsername(), usr.getCod_polo());
		
		return Util.isFilled(admin) ? services.database().getBibliotecari(admin.getCod_polo()) : new ArrayList<>();
	}
}
