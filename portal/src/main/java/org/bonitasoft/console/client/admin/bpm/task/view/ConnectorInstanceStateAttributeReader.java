/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.admin.bpm.task.view;

import org.bonitasoft.web.rest.model.bpm.connector.ConnectorInstanceItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;

/**
 * Replace underscore by space in  ConnectorInstanceItem state attribute
 */
public class ConnectorInstanceStateAttributeReader extends AbstractAttributeReader {
	
	public ConnectorInstanceStateAttributeReader() {
		// set lead attribute to set column jsID
		super(ConnectorInstanceItem.ATTRIBUTE_STATE);
	}
	
	@Override
	protected String _read(IItem item) {
		String state = item.getAttributeValue(ConnectorInstanceItem.ATTRIBUTE_STATE);
		return state == null ? "" : state.replaceAll("_", " ").toLowerCase();
	}

}
