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
package it.almaviva.cms.dbManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.almaviva.cms.controllers.responseModel.ResponseModel;
import it.almaviva.cms.controllers.responseModel.ResponseType;
import it.almaviva.cms.dbManager.beans.Biblioteche;
import it.almaviva.cms.dbManager.beans.Categorie;
import it.almaviva.cms.dbManager.beans.Faq;
import it.almaviva.cms.dbManager.beans.Messaggi;
import it.almaviva.cms.dbManager.beans.PoloConf;
import it.almaviva.cms.dbManager.beans.Tickets;
import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.models.ReturnModelInt;
import it.almaviva.cms.models.tickets.AddAndDeleteTicketModel;
import it.almaviva.cms.models.tickets.CloseTicketModel;
import it.almaviva.cms.models.tickets.DettaglioTicketModel;
import it.almaviva.cms.models.tickets.MessaggioInseritoModel;
import it.almaviva.cms.models.user.ChangePasswordModel;
import it.almaviva.cms.models.user.LoggedinModel;
import it.almaviva.cms.utilities.Util;
import it.almaviva.cms.utilities.costanti.Constants;

@Repository
@Transactional
public class DatabaseQueryDao {
	private static final String SELECT_FROM_TICKETS = "SELECT t FROM tickets t";
	private static final String SELECT_FROM_TICKETS_JOIN_MESSAGES = "SELECT t FROM tickets t LEFT JOIN messaggi m on t.id = m.ticket_id";

	private static Logger log = Logger.getLogger(DatabaseQueryDao.class);

	@PersistenceContext
	private EntityManager em;

	private void insert(Object entity) {
		em.persist(entity);
		em.flush();
	}

	private void update(Object entity) {
		em.merge(entity);
		em.flush();
	}

	private LoggedinModel prepareLoggedIn(Utenti user) {
		LoggedinModel loggedinModel = new LoggedinModel();//getTickets(null, 0, 5, user);
		loggedinModel.setUser(user);

		return loggedinModel;
	}

	private List<Tickets> getAllTickets(Map<String, String> filters, Integer limit, Integer offset,
			Utenti user) {
		// if (!Util.isFilled(filters))
		// return getAllTickets(limit, offset);

		String sql = buildQuery(filters, user);
		log.info("Query: " + sql);
		List<Tickets> resultList = em.createQuery(sql, Tickets.class).setFirstResult(offset).setMaxResults(limit)
				.getResultList();
		Tickets.sorTicketsByTs_ins(resultList);
		return resultList;

	}

	private List<Tickets> findTicketsByUserIns(String user_ins) {
		List<Tickets> resultList = em.createNamedQuery("tickets.findByUserIns", Tickets.class)
				.setParameter("user_ins", user_ins).getResultList();
		return resultList;
	}
	public Boolean isCategoryIsAssignedToTickets(Long idCat) {
		List<Tickets> resultList = em.createNamedQuery("tickets.findByIdCat", Tickets.class)
				.setParameter("id_cat", idCat).getResultList();
		return Util.isFilled(resultList);
	}

	private List<Messaggi> findMessagesByUserIns(String user_ins) {
		List<Messaggi> resultList = em.createNamedQuery("messaggi.findByUserIns", Messaggi.class)
				.setParameter("user_ins", user_ins).getResultList();
		return resultList;
	}

	private String buildQuery(Map<String, String> filters, Utenti user) {

		String whereConditions = " WHERE lower(t.fl_canc) <> 's' ";
		String likeCondition = "" +"";
		boolean isToJoin = false;
		if (filters == null)
			filters = new HashMap<String, String>();

		if (!user.getIsBibliotecario() && !filters.containsKey("testo")) {
			whereConditions += " AND ";
			whereConditions += "(t.pubblico = true ";
			whereConditions  += "OR t.user_ins = '" +user.getUsername() +"'";
			whereConditions += ")";
		}
			
		int i = 1;
		if (Util.isFilled(filters)) {
			if (filters.size() > 0)
				whereConditions += " AND ";
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				if (!entry.getKey().equals("testo"))
					whereConditions += "t." + entry.getKey() + " = '" + entry.getValue() + "'";
				else {
					isToJoin = true;
					likeCondition = buildLike(entry.getValue());
				}
				if (i++ != filters.size() || isToJoin) {
					whereConditions += " AND ";
				}
			}

		}
		if(isToJoin) {
			whereConditions += "(";
			whereConditions += "lower(t.titolo)" + likeCondition + " "
						  + "OR lower(t.ho_gia_fatto)" + likeCondition
						 + " OR lower(t.testo)" + likeCondition+" OR lower(m.testo)"+likeCondition+")";
			if (!user.getIsBibliotecario())  {
				whereConditions += " AND ";
				whereConditions += "(t.pubblico = true ";
				whereConditions  += "OR t.user_ins = '" +user.getUsername() +"'";
				whereConditions += ")";
			}
				
			//whereConditions += " UNION ";
		//	whereConditions += ( SELECT_FROM_TICKETS_JOIN_MESSAGES + 
			//			" WHERE lower(m.testo)" + likeCondition + " AND lower(m.fl_canc) <> 's'");
		}
		
		String sql = (isToJoin) ? SELECT_FROM_TICKETS_JOIN_MESSAGES : SELECT_FROM_TICKETS ;

		sql = sql + " " + whereConditions + " ";
		
		sql = sql.replace("AND  AND", "AND");
		sql = sql + ((isToJoin) ? " GROUP BY t.id  ORDER BY t.ts_ins DESC" : " ORDER BY t.ts_ins DESC");
		log.info("Builded SQL:" +sql);
		return sql;
	
	}

	private String buildLike(String value) {
		value = value.trim().toLowerCase();
		value = value.replace(" ", "%");
		value = "'%" + value + "%'";
		return " LIKE " + value;
	}

	private Integer countTickets(Map<String, String> filters, Utenti user) {

		String sql = buildQuery(filters, user);
		List<Tickets> resultList2 = em.createQuery(sql, Tickets.class).getResultList();
		Tickets.sorTicketsByTs_ins(resultList2);
		Integer resultList = resultList2.size();
		return (Integer) resultList;

	}



	private Utenti utentiLogin(String userEmail, String password, String cod_polo) {
		try {
			Utenti singleResult = em.createNamedQuery("utenti.login", Utenti.class).setParameter("usrMail", userEmail)
					.setParameter("password", password)
					.setParameter("cod", cod_polo).getSingleResult();
			return singleResult;
		} catch (NoResultException e) {
			log.info("user login non trovato:" + userEmail);
			return null;
		}
	}

	private Categorie getCategoryByID(Long id) {
		try {
			return em.createNamedQuery("categorie.findByID", Categorie.class).setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			log.info("categoria non trovato:" + id);
			return null;
		}
	}

	private Faq getFaqByID(Long id) {
		try {
			return em.createNamedQuery("faq.findByID", Faq.class).setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			log.info("categoria non trovato:" + id);
			return null;
		}
	}

	private Tickets getTicketByID(Long id) {
		try {
			return em.createNamedQuery("tickets.findById", Tickets.class).setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			log.info("ticket non trovato id:" + id);
			return null;
		}
	}

	public List<Utenti> getAllUsers(String cod_polo) {
		return em.createNamedQuery("utenti.getAllxPolo", Utenti.class).setParameter("cod_polo", cod_polo).getResultList();

	}

	public List<Utenti> getBibliotecari(String cod_polo) {
		return em.createNamedQuery("utenti.getBibliotecari", Utenti.class).setParameter("cod_polo", cod_polo).getResultList();

	}
	

	public List<Tickets> getAllTickets() {
		return em.createNamedQuery("tickets.AllOrderedById", Tickets.class).getResultList();
	}

	public List<Categorie> getCategorie() {
		return em.createNamedQuery("categorie.All", Categorie.class).getResultList();
	}

	public List<Faq> getFaqs() {
		return em.createNamedQuery("faq.All", Faq.class).getResultList();
	}

	public Biblioteche addBiblioteca(Biblioteche bibToInsert) {
		if (bibToInsert == null)
			return null;
		Biblioteche bibliotecaByCod = getBibliotecaByCod(bibToInsert.getCod_bib());

		if (!Util.isFilled(bibliotecaByCod)) {
			bibToInsert.setFl_canc("n");
			insert(bibToInsert);
			return bibToInsert;
		} else {
			bibliotecaByCod.setFl_canc("n");
			bibliotecaByCod.setNome(bibToInsert.getNome());
			bibliotecaByCod.setCod_bib(bibToInsert.getCod_bib());
			bibliotecaByCod.setCod_polo(bibToInsert.getCod_polo());
			update(bibliotecaByCod);
			return bibliotecaByCod;
		}

	
	}

	public Biblioteche deleteBiblioteca(Biblioteche bibToDelete) {
		Biblioteche bibliotecaByCod = getBibliotecaByCod(bibToDelete.getCod_bib());
		bibliotecaByCod.setFl_canc("s");
		update(bibliotecaByCod);
		return bibliotecaByCod;
	}
	public List<Tickets> checkTicketAssociatiBib(Biblioteche bibToDelete) {
			return em.createNamedQuery("tickets.findByIdBib", Tickets.class).setParameter("cod_biblioteca_ind", bibToDelete.getCod_bib()).getResultList();

	}

	public List<Biblioteche> getBiblioteche() {
		return em.createNamedQuery("biblioteche.All", Biblioteche.class).getResultList();
	}

	public Biblioteche getBibliotecaByCod(String cd_biblioteca) {
		try {
			return em.createNamedQuery("biblioteche.findByCod", Biblioteche.class).setParameter("cd_bib", cd_biblioteca)
					.getSingleResult();
		} catch (NoResultException e) {
			log.info("Biblioteca non trovata:" + cd_biblioteca);
			return null;
		}
	}

	public ResponseModel loginFromSbnweb(Utenti userToLogin) {
		Utenti user;
		try {
			user = getUserByUsername(userToLogin.getUsername().toUpperCase(), userToLogin.getCod_polo());
		} catch (Exception e) {
			return ResponseType.build_response(ResponseType.USR_PSW_WRONG, null);
		}
		return prepareLoggedInResponseModel(user);

	}

	public Utenti getUserByUsername(String username, String cod_polo) {
		try {
			return em.createNamedQuery("utenti.findByUsr", Utenti.class).setParameter("username", username)
					.setParameter("cod_polo", cod_polo).getSingleResult();
		} catch (NoResultException e) {
			log.info("user  non trovato:" + username);
			return null;
		}
	}

	public Utenti getUserByEmail(String email, String cod_polo) {
		try {
			return em.createNamedQuery("utenti.findByEmail", Utenti.class).setParameter("email", email)
					.setParameter("cod_polo", cod_polo).getSingleResult();
		} catch (NoResultException e) {
			log.info("user email non trovato:" + email);
			return null;
		}
	}

	public Messaggi getMessage(Long messageId) {
		try {
			return em.createNamedQuery("messaggi.findByID", Messaggi.class).setParameter("id", messageId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;

		}
	}

	public ResponseModel updateUser(Utenti userUpdated, Boolean userIsToDelete) {
		Utenti userByUsername = getUserByUsername(userUpdated.getUsername(),userUpdated.getCod_polo());
		LoggedinModel loggedinModel = getTickets(null, 0, 5, userByUsername);
		loggedinModel.setUser(userByUsername);

		Utenti checkMailDB = getUserByEmail(userUpdated.getEmail(), userUpdated.getCod_polo());
		// se la mail è di un altro utente
		if (Util.isFilled(checkMailDB) && !userUpdated.getEmail().equals(checkMailDB.getEmail()) ) {
			return ResponseType.build_response(ResponseType.MAIL_ALREADY_REGISTERED, loggedinModel);

		}
		if (userIsToDelete)
			userByUsername.setUsername(Constants.user_deleted + "_" + userByUsername.getUsername());
		userByUsername.setEmail(userUpdated.getEmail());
		userByUsername.setData_nascita(userUpdated.getData_nascita());
		userByUsername.setFl_canc(userUpdated.getFl_canc());
		userByUsername.setIsBibliotecario(userUpdated.getIsBibliotecario());
		update(userByUsername);
		loggedinModel.setUser(userByUsername);
		return (Util.isFilled(userByUsername) ? ResponseType.build_response(ResponseType.OK, loggedinModel)
				: ResponseType.build_response(ResponseType.USR_NOT_FOUND, null));
	}

	public Utenti resetPsw(Utenti userUpdated) {
		Utenti userByUsername = getUserByUsername(userUpdated.getUsername(), userUpdated.getCod_polo());
	
		userByUsername.setPasswordIsToReset(true);
		String resetPassword = userByUsername.resetPassword();
		update(userByUsername);
		//Passo i dati necessari per la mail
		Utenti dummyUser = new Utenti();
		dummyUser.setPassword(resetPassword);
		dummyUser.setUsername(userByUsername.getUsername());
		dummyUser.setEmail(userByUsername.getEmail());
		return dummyUser;
	}

	public List<Categorie> updateCategory(Categorie categoria) {
		Categorie category = getCategoryByID(categoria.getId());
		if (!Util.isFilled(category))
			return getCategorie();
		category.setCd_categoria(categoria.getCd_categoria());
		category.setTesto_it(categoria.getTesto_it());
		category.setTesto_en(categoria.getTesto_it());
		if ("s".equals(categoria.getFl_canc()))
			category.setFl_canc("s");

		update(category);
		return getCategorie();
	}

	public List<Categorie> addCategory(Categorie categoria) {
		Categorie category = getCategoryByID(categoria.getId());
		if (Util.isFilled(category))
			return getCategorie();
		category = new Categorie();
		category.setCd_categoria(categoria.getCd_categoria());
		category.setTesto_it(categoria.getTesto_it());
		category.setTesto_en(categoria.getTesto_it());
		category.setCod_polo(categoria.getCod_polo());
		category.setFl_canc("n");
		category.setTs_ins(Util.now());
		insert(category);
		return getCategorie();
	}

	public List<Faq> addFaq(Faq faq) {
		Faq faqByID = getFaqByID(faq.getId());
		if (Util.isFilled(faqByID))
			return getFaqs();
		faqByID = new Faq();

		faqByID.setDomanda(faq.getDomanda());
		faqByID.setRisposta(faq.getRisposta());
		faqByID.setCd_faq(faq.getCd_faq());
		faqByID.setFl_canc("n");
		faqByID.setTs_ins(Util.now());
		faqByID.setCod_polo(faq.getCod_polo());
		insert(faqByID);
		return getFaqs();
	}

	public List<Faq> updateFaq(Faq faq) {
		Faq faqByID = getFaqByID(faq.getId());
		if (!Util.isFilled(faqByID))
			return getFaqs();

		faqByID.setDomanda(faq.getDomanda());
		faqByID.setRisposta(faq.getRisposta());
		faqByID.setCd_faq(faq.getCd_faq());

		if ("s".equals(faq.getFl_canc()))
			faqByID.setFl_canc("s");
		update(faqByID);
		return getFaqs();
	}

	public ResponseModel changePsw(ChangePasswordModel pswModel) {
		String encryptedOldPsw = Util.encryptData(pswModel.getOld_psw(), pswModel.getOld_psw());
		Utenti userFromLoginUsrPsw = utentiLogin(pswModel.getUsername(), encryptedOldPsw, pswModel.getCod_polo());
		if (!Util.isFilled(userFromLoginUsrPsw))
			return ResponseType.build_response(ResponseType.PSW_WRONG, null);

		if (!pswModel.getNew_psw().equals(pswModel.getRepeat_new_psw()))
			return ResponseType.build_response(ResponseType.PSW_NOT_SAME, null);
		
		userFromLoginUsrPsw.setPasswordIsToReset(false);
		if (userFromLoginUsrPsw.getSbnweb())
			return ResponseType.build_response(ResponseType.NOT_PSW_CAN_CHANGE_SBNWEB, null);
		String newSecurePsw = Util.encryptData(pswModel.getNew_psw(), pswModel.getNew_psw());

		userFromLoginUsrPsw.setPassword(newSecurePsw);
		update(userFromLoginUsrPsw);
		return prepareLoggedInResponseModel(userFromLoginUsrPsw);
	}

	public Boolean canUserAccess(Utenti userToLogin) {

		Utenti user;
		try {
			user = utentiLogin(userToLogin.getUsername(), userToLogin.encriptedPsw(), userToLogin.getCod_polo());
			return Util.isFilled(user) ? true : false;
		} catch (Exception e) {
			return false;
		}

	}

	// Users
	public ResponseModel addUser(Utenti userToRegister, Boolean isSbnWeb) {
		log.info("register. Seracing for: " + userToRegister);
		Utenti checkAlreadyinDB = getUserByUsername(userToRegister.getUsername(), userToRegister.getCod_polo());
		Utenti checkMailDB = getUserByEmail(userToRegister.getEmail(), userToRegister.getCod_polo());
		if (Util.isFilled(checkAlreadyinDB) || Util.isFilled(checkMailDB)) {
			return ResponseType.build_response(ResponseType.USR_EXIST, null);

		}

		Utenti userInsert = new Utenti();

		userInsert.setPassword(userToRegister.encriptedPsw());
		userInsert.setData_nascita(userToRegister.getData_nascita());
		userInsert.setUsername(userToRegister.getUsername().toUpperCase());
		userInsert.setTs_ins(Util.now());
		userInsert.setFl_canc("n");
		userInsert.setIsBibliotecario(false);
		userInsert.setIsAdmin(false);
		userInsert.setPasswordIsToReset(false);
		userInsert.setCod_polo(userToRegister.getCod_polo());

		userInsert.setAcceptNewsLetter(userToRegister.getAcceptNewsLetter());
		if (isSbnWeb) {
			userInsert.setUsername(userToRegister.getUsername().toUpperCase());
			userInsert.setPassword(null);

		}
		userInsert.setSbnweb(isSbnWeb);
		userInsert.setEmail(userToRegister.getEmail());

		try {
			insert(userInsert);
		} catch (Exception e) {
			return ResponseType.build_response(ResponseType.ERROR, null);

		}
		return (isSbnWeb) ? loginFromSbnweb(userToRegister) : login(userToRegister);
	}

	public ResponseModel login(Utenti userToLogin) {
		Utenti user;
		try {
			user = utentiLogin(userToLogin.getUsername(), userToLogin.encriptedPsw(), userToLogin.getCod_polo());
		} catch (Exception e) {
			return ResponseType.build_response(ResponseType.USR_PSW_WRONG, null);
		}
		return prepareLoggedInResponseModel(user);
	}

	private ResponseModel prepareLoggedInResponseModel(Utenti user) {
		log.info(user);
		if (Util.isFilled(user)) {
			LoggedinModel loggedinModel = prepareLoggedIn(user);
			return ResponseType.build_response(ResponseType.OK, loggedinModel);
		} else {
			return ResponseType.build_response(ResponseType.USR_PSW_WRONG, null);
		}
	}

	public LoggedinModel getTickets(Map<String, String> filters, Integer start, Integer numRows,
			Utenti user) {
		LoggedinModel loggedinModel = new LoggedinModel(null, getAllTickets(filters, numRows, start, user),
				countTickets(filters, user), 0, 5);
		return loggedinModel;
	}

	public ResponseModel addTickets(Tickets newTicket) {
		newTicket.setFl_canc("n");
		newTicket.setAperto(true);
		newTicket.setTs_ins(Util.now());
		try {
			insert(newTicket);
			Utenti userinsert = getUserByUsername(newTicket.getUser_ins(), newTicket.getCod_polo());
			List<Tickets> ticketList = getAllTickets(Constants.createMapByCodPolo( newTicket.getCod_polo()), 5, 0, userinsert);
			ReturnModelInt insert = new AddAndDeleteTicketModel(newTicket,
					ticketList, userinsert);
			return ResponseType.build_response(ResponseType.OK, insert);
		} catch (Exception e) {
			return ResponseType.build_response(ResponseType.DB_ERROR, null);

		}

	}

	public ResponseModel addMessageToTicket(Messaggi messageToAdd) {
		try {
			Tickets ticket = getTicketByID(messageToAdd.getTicket_id());
			ResponseType esito = ResponseType.NOT_ALLOWED;
			 Utenti userByUsername = null;

			if ( ticket != null && ticket.getAperto() && ticket.getFl_canc().toLowerCase().equals("n")) {
				messageToAdd.setFl_canc("n");
				messageToAdd.setTs_ins(Util.now());
				insert(messageToAdd);
				//ticket.messaggi().add(messageToAdd);
				 esito = ResponseType.OK;
				  userByUsername = getUserByUsername(ticket.getUser_ins(), ticket.getCod_polo());
			} else {
				 esito = ResponseType.NOT_ALLOWED;
			}
			Tickets dettaglioTicket = getTicketByID(messageToAdd.getTicket_id());

			ResponseModel ticketDetail = getTicketDetail(messageToAdd.getTicket_id());
			MessaggioInseritoModel messaggioInseritoModel = new MessaggioInseritoModel(dettaglioTicket.messaggi(), dettaglioTicket);
			messaggioInseritoModel.setMessaggio(messageToAdd);
			messaggioInseritoModel.setUtenteToMail(userByUsername);
			ticketDetail.setResponseObj(messaggioInseritoModel);
			ticketDetail.setKey(esito);
			return ticketDetail;
		} catch (Exception e) {
			return getTicketDetail(messageToAdd.getTicket_id());

		}

	}
	public ResponseModel updateTicketMessage(Messaggi messageToUpdate) {
		Messaggi message = getMessage(messageToUpdate.getId());
		Tickets ticket = getTicketByID(messageToUpdate.getTicket_id());
		ResponseModel ticketResponse = getTicketDetail(ticket.getId());

		if ( ticket != null && ticket.getAperto() && ticket.getFl_canc().toLowerCase().equals("n") && Util.isFilled(message)) {
			message.setTesto(messageToUpdate.getTesto());
			//Eliminazione
			if("s".equals(messageToUpdate.getFl_canc()))
				message.setFl_canc("s");
			if(messageToUpdate.getAllegato() != message.getAllegato())
				message.setAllegato(messageToUpdate.getAllegato());
			update(message);
			 ticket = getTicketByID(message.getTicket_id());
			MessaggioInseritoModel messaggioInseritoModel = new MessaggioInseritoModel(ticket.messaggi(),
					ticket);
			ticketResponse.setResponseObj(messaggioInseritoModel);

		}else {
			ticketResponse.setKey(ResponseType.NOT_ALLOWED);
		}
			

		return getTicketDetail(messageToUpdate.getTicket_id());
	}

	public ResponseModel getTicketDetail(Long id) {

		Tickets ticket = getTicketByID(id);
		List<Messaggi> messages = ticket.messaggi();
		Utenti userByUsername = getUserByUsername(ticket.getUser_ins(), ticket.getCod_polo());

		return (Util.isFilled(ticket))
				? ResponseType.build_response(ResponseType.OK,
						new DettaglioTicketModel(messages, ticket, userByUsername))
				: ResponseType.build_response(ResponseType.TICKET_NOT_FOUND, null);

	}

	public ResponseModel closeTicket(CloseTicketModel ctm) {
		Tickets ticket = getTicketByID(ctm.getId_ticket());
		Utenti userByUsername = getUserByUsername(ctm.getUsername(), ticket.getCod_polo());
		if (!userByUsername.getIsBibliotecario())
			return ResponseType.build_response(ResponseType.NOT_ALLOWED, null);
		ticket.setAperto(false);
		update(ticket);
		return getTicketDetail(ctm.getId_ticket());

	}

	public ResponseModel updateTicket(Tickets toUpdate) {
		Tickets ticket = getTicketByID(toUpdate.getId());
		ticket = toUpdate;

		try {
			update(ticket);
			if("s".equals(ticket.getFl_canc())) {
				Utenti userByUsername = getUserByUsername(ticket.getUser_ins(), ticket.getCod_polo());

				return ResponseType.build_response(ResponseType.OK, new DettaglioTicketModel(ticket.messaggi(), ticket, userByUsername));
			}
			ResponseModel ticketDetail = getTicketDetail(ticket.getId());
			return ticketDetail;
		} catch (Exception e) {
			return ResponseType.build_response(ResponseType.DB_ERROR, null);

		}
	}

	public void userWasDeleted(String user_ins) {
		// stabilito che se un utente è stato eliminato tutti i suoi contenuti vengono
		// assegnati a cost. utente_cancellato
		List<Tickets> ticketsByUserIns = findTicketsByUserIns(user_ins);
		ticketsByUserIns.forEach(ticket -> {
			ticket.setUser_ins(Constants.user_deleted);
			update(ticket);
		});
		List<Messaggi> messagesByUserIns = findMessagesByUserIns(user_ins);
		messagesByUserIns.forEach(message -> {
			message.setUsername_ins(Constants.user_deleted);
			update(message);

		});

	}
	public PoloConf getPolo(String cod_polo) {
		try {
			PoloConf singleResult = em.createNamedQuery("ConfPolo.findByCodPolo", PoloConf.class).setParameter("cod", cod_polo).getSingleResult();
			return singleResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<PoloConf> getPoli() {
		try {
			List<PoloConf> poli = em.createNamedQuery("ConfPolo.findAll", PoloConf.class).getResultList();
			return poli;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
