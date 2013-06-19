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

import org.bonitasoft.engine.bpm.connector.ArchivedConnectorInstance;
import org.bonitasoft.web.rest.model.bpm.connector.ArchivedConnectorInstanceItem;

/**
 * Bridge object between engine and console web implementation of an item
 * 
 * @author Julien Mege
 * 
 */
public class ArchivedConnectorInstanceItemWrapper extends ArchivedConnectorInstanceItem {

    public ArchivedConnectorInstanceItemWrapper(final ArchivedConnectorInstance engineItem) {
        if (engineItem == null) {
            throw new IllegalArgumentException("Can't wrap null item");
        }
        this.setId(engineItem.getId());
        setName(engineItem.getName());
        setVersion(engineItem.getVersion());
        setState(engineItem.getState().toString());
        this.setConnectorId(engineItem.getConnectorId());
        setActivationEvent(engineItem.getActivationEvent().toString());
        this.setContainerId(engineItem.getContainerId());
        setContainerType(engineItem.getContainerType());
        this.setSourceObjectId(engineItem.getSourceObjectId());
        setArchivedDate(engineItem.getArchiveDate());
    }
}
