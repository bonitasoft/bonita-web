/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.process;

import static org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_NAME;
import static org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_PROCESS_ID;
import static org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_VERSION;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.connector.ConnectorCriterion;
import org.bonitasoft.engine.bpm.connector.ConnectorImplementationDescriptor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasSearch;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Séverin Moussel
 */
public class ProcessConnectorDatastore extends CommonDatastore<ProcessConnectorItem, ConnectorImplementationDescriptor> implements
        DatastoreHasGet<ProcessConnectorItem>,
        DatastoreHasSearch<ProcessConnectorItem>
{

    public ProcessConnectorDatastore(final APISession engineSession) {
        super(engineSession);
    }

    protected ProcessAPI getProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public ProcessConnectorItem get(final APIID id) {
        try {

            final ProcessConnectorItem connector =
                    convertEngineToConsoleItem(
                    getProcessAPI().getConnectorImplementation(
                            id.getPartAsLong(ATTRIBUTE_PROCESS_ID),
                            id.getPart(ATTRIBUTE_NAME),
                            id.getPart(ATTRIBUTE_VERSION)
                            ));

            // Correct missing element in engine object
            connector.setProcessId(id.getPartAsLong(ATTRIBUTE_PROCESS_ID));

            return connector;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public ItemSearchResult<ProcessConnectorItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        try {
            final Long processId = MapUtil.getValueAsLong(filters, ATTRIBUTE_PROCESS_ID);

            final List<ConnectorImplementationDescriptor> connectors = getProcessAPI().getConnectorImplementations(
                    processId,
                    SearchOptionsBuilderUtil.computeIndex(page, resultsByPage),
                    resultsByPage,
                    ConnectorCriterion.valueOf(orders.toUpperCase().replace(" ", "_")));

            final List<ProcessConnectorItem> results = new ArrayList<ProcessConnectorItem>();
            for (final ConnectorImplementationDescriptor connector : connectors) {
                final ProcessConnectorItem result = convertEngineToConsoleItem(connector);
                // Correct missing element in engine object
                result.setProcessId(processId);

                results.add(result);
            }

            final long numtotalConnectorImplem = getProcessAPI().getNumberOfConnectorImplementations(processId);

            return new ItemSearchResult<ProcessConnectorItem>(page, resultsByPage, numtotalConnectorImplem, results);

        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    protected ProcessConnectorItem convertEngineToConsoleItem(final ConnectorImplementationDescriptor item) {

        final ProcessConnectorItem result = new ProcessConnectorItem();
        result.setName(item.getDefinitionId());
        result.setVersion(item.getDefinitionVersion());
        // setted in get and search because attribute is missing in the engine oject
        // result.setProcessId(...);
        result.setImplementationName(item.getId());
        result.setImplementationVersion(item.getVersion());
        result.setClassname(item.getImplementationClassName());

        return result;
    }
}
