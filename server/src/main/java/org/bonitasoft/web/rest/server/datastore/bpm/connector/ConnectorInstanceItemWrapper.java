/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.connector;

import org.bonitasoft.engine.bpm.connector.ConnectorInstance;
import org.bonitasoft.web.rest.model.bpm.connector.ConnectorInstanceItem;

/**
 * Bridge object between engine and console web implementation of an item
 * 
 * @author Vincent Elcrin
 * 
 */
public class ConnectorInstanceItemWrapper extends ConnectorInstanceItem {

    public ConnectorInstanceItemWrapper(ConnectorInstance engineItem) {
        if (engineItem == null) {
            throw new IllegalArgumentException("Can't wrap null item");
        }

        this.setId(engineItem.getId());
        this.setName(engineItem.getName());
        this.setVersion(engineItem.getVersion());
        this.setState(engineItem.getState().toString());
        this.setConnectorId(engineItem.getConnectorId());
        this.setActivationEvent(engineItem.getActivationEvent().toString());
        this.setContainerId(engineItem.getContainerId());
        this.setContainerType(engineItem.getContainerType());
    }
}
