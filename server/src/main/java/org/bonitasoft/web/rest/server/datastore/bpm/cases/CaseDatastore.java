/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessInstanceState;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 */
public class CaseDatastore extends CommonDatastore<CaseItem, ProcessInstance> implements DatastoreHasGet<CaseItem>, DatastoreHasSearch<CaseItem>,
DatastoreHasDelete, DatastoreHasAdd<CaseItem> {

    public CaseDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    protected CaseItem convertEngineToConsoleItem(final ProcessInstance item) {
        return new CaseItemConverter().convert(item);
    }

    @Override
    public ItemSearchResult<CaseItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        /*
         * By default we add a caller filter of -1 to avoid having sub processes.
         * If caller is forced to any then we don't need to add the filter.
         */
        if(!filters.containsKey(CaseItem.FILTER_CALLER)) {
            filters.put(CaseItem.FILTER_CALLER, "-1");
        } else if ("any".equalsIgnoreCase(filters.get(CaseItem.FILTER_CALLER))){
            filters.remove(CaseItem.FILTER_CALLER);
        }

        // Build search
        final SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, orders, search);

        addFilterToSearchBuilder(filters, builder, CaseItem.ATTRIBUTE_PROCESS_ID, ProcessInstanceSearchDescriptor.PROCESS_DEFINITION_ID);
        addFilterToSearchBuilder(filters, builder, CaseItem.ATTRIBUTE_PROCESS_NAME, ProcessInstanceSearchDescriptor.NAME);
        addFilterToSearchBuilder(filters, builder, CaseItem.ATTRIBUTE_STARTED_BY_USER_ID, ProcessInstanceSearchDescriptor.STARTED_BY);

        if(filters.containsKey(CaseItem.FILTER_CALLER)) {
            builder.filter(ProcessInstanceSearchDescriptor.CALLER_ID, MapUtil.getValueAsLong(filters, CaseItem.FILTER_CALLER));
        }

        // Run search depending on filters passed
        final SearchResult<ProcessInstance> searchResult = runSearch(filters, builder);

        // Convert to ConsoleItems
        return new ItemSearchResult<CaseItem>(
                page,
                resultsByPage,
                searchResult.getCount(),
                convertEngineToConsoleItemsList(searchResult.getResult()));
    }

    protected SearchResult<ProcessInstance> runSearch(final Map<String, String> filters, final SearchOptionsBuilder builder) {

        try {
            final ProcessAPI processAPI = getProcessAPI();

            builder.differentFrom(ProcessInstanceSearchDescriptor.STATE_ID, ProcessInstanceState.COMPLETED.getId());

            if (filters.containsKey(CaseItem.FILTER_USER_ID)) {
                return processAPI.searchOpenProcessInstancesInvolvingUser(MapUtil.getValueAsLong(filters, CaseItem.FILTER_USER_ID), builder.done());
            }

            if (filters.containsKey(CaseItem.FILTER_SUPERVISOR_ID)) {
                return processAPI.searchOpenProcessInstancesSupervisedBy(MapUtil.getValueAsLong(filters, CaseItem.FILTER_SUPERVISOR_ID), builder.done());
            }

            return processAPI.searchProcessInstances(builder.done());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected ProcessAPI getProcessAPI() throws Exception {
        return TenantAPIAccessor.getProcessAPI(getEngineSession());
    }

    @Override
    public CaseItem get(final APIID id) {
        try {
            return convertEngineToConsoleItem(getProcessAPI()
                    .getProcessInstance(id.toLong()));
        } catch (final ProcessInstanceNotFoundException e) {
            return null;
        } catch (final Exception e) {
            throw new APIException(e);
        }

    }

    @Override
    public void delete(final List<APIID> ids) {
        try {
            final ProcessAPI processApi = getProcessAPI();
            for (final APIID id : ids) {
                processApi.deleteProcessInstance(id.toLong());
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public CaseItem add(final CaseItem caseItem) {
        final EngineClientFactory factory = new EngineClientFactory(new EngineAPIAccessor(getEngineSession()));
        return new CaseSarter(caseItem, factory.createCaseEngineClient(), factory.createProcessEngineClient()).start();
    }

}
