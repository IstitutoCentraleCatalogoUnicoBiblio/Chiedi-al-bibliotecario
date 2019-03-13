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
package it.almaviva.cms.models;

import java.util.List;

import it.almaviva.cms.dbManager.beans.Biblioteche;

public class ResponseImpBiblioteche implements ReturnModelInt {
	private List<Biblioteche> biblioteche;
	private Biblioteche imported;

	public ResponseImpBiblioteche(List<Biblioteche> biblioteche, Biblioteche imported) {
		super();
		this.setBiblioteche(biblioteche);
		this.setImported(imported);
	}

	public List<Biblioteche> getBiblioteche() {
		return biblioteche;
	}

	public void setBiblioteche(List<Biblioteche> biblioteche) {
		this.biblioteche = biblioteche;
	}

	public Biblioteche getImported() {
		return imported;
	}

	public void setImported(Biblioteche imported) {
		this.imported = imported;
	}

}
