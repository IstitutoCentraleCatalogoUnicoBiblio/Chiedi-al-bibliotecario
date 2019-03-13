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
package it.almaviva.test;

import java.util.HashMap;
import java.util.Map;

import it.almaviva.cms.dbManager.beans.Utenti;
import it.almaviva.cms.utilities.Util;

public class QueryBuilderTest {
	private static final String SELECT_FROM_TICKETS = "SELECT t FROM tickets t";
	private static final String SELECT_FROM_TICKETS_JOIN_MESSAGES = "SELECT t FROM tickets t LEFT JOIN messaggi m on t.id = m.ticket_id";
	private static String buildLike(String value) {
		value = value.trim().toLowerCase();
		value = value.replace(" ", "%");
		value = "'%" + value + "%'";
		return " LIKE " + value;
	}
	
	//SELECT * FROM tickets t INNER JOIN messaggi m on t.id = m.ticket_id WHERE lower(m.testo) LIKE '%prova%' AND lower(m.fl_canc) <> 's' and lower(t.fl_canc) <> 's'  AND t.cod_biblioteca_ind = 'IC' AND (lower(t.titolo) LIKE '%prova%' OR lower(t.ho_gia_fatto) LIKE '%prova%' OR lower(t.testo) LIKE '%prova%')
	//group by t.id
	public static String buildQuery (Map<String, String> filters, Utenti user) {

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
		//bug, poi capisco perchè
		sql = sql.replace("AND  AND", "AND");
		sql = sql + ((isToJoin) ? " GROUP BY t.id  ORDER BY t.ts_ins DESC" : "ORDER BY t.ts_ins DESC");
		System.out.println("Builded SQL:" +sql);
		return sql;
	
	}
	public static void main(String[] args) {
		Utenti user = new Utenti();
		user.setIsBibliotecario(true);
		user.setUsername("IC0000090");
		Map<String, String> filters = new HashMap<String, String>();
			filters.put("testo", "vorrei");
			//filters.put("cod_biblioteca_ind", "IC");
		filters.put("user_ins", "admin");

		String sql = buildQuery(filters, user) +";";
		System.out.println(sql);

	}

}
