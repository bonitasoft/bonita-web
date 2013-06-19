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
package org.bonitasoft.web.rest.server.api.bpm.connector;

import org.bonitasoft.engine.bpm.connector.ConnectorInstanceCriterion;
import org.bonitasoft.web.rest.model.bpm.connector.ArchivedConnectorInstanceDefinition;
import org.bonitasoft.web.rest.model.bpm.connector.ArchivedConnectorInstanceItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.connector.ArchivedConnectorInstanceDatastore;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasSearch;
import org.bonitasoft.web.toolkit.server.api.APIHasUpdate;
import org.bonitasoft.web.toolkit.server.api.Datastore;

/**
 * @author Julien Mege
 * 
 */
public class APIArchivedConnectorInstance extends ConsoleAPI<ArchivedConnectorInstanceItem> implements APIHasSearch<ArchivedConnectorInstanceItem>,
        APIHasUpdate<ArchivedConnectorInstanceItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(ArchivedConnectorInstanceDefinition.TOKEN);
    }

    @Override
    public String defineDefaultSearchOrder() {
        return ConnectorInstanceCriterion.NAME_ASC.name();
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new ArchivedConnectorInstanceDatastore(getEngineSession());
    }
}
