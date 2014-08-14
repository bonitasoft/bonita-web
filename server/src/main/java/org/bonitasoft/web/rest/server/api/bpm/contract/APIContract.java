/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.bpm.contract;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.contract.ContractDefinition;
import org.bonitasoft.web.rest.model.bpm.contract.ContractItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.contract.ContractDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Laurent Leseigneur
 */
public class APIContract extends ConsoleAPI<ContractItem> implements APIHasGet<ContractItem> {

    @Override
    protected ItemDefinition<ContractItem> defineItemDefinition() {
        return ContractDefinition.get();
    }


    @Override
    public ContractItem get(final APIID id) {
        final APISession apiSession = getEngineSession();
        ContractItem contractItem = null;

        try {
            final ContractDatastore dataStore = new ContractDatastore(apiSession);
            contractItem = dataStore.get(id);

        } catch (final Exception e) {
            throw new APIException(e);
        }

        return contractItem;
    }

}
