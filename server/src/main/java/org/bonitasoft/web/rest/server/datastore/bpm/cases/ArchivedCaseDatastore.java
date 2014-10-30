/**
 * Copyright (C) 2011, 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstancesSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessInstanceState;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
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
 * @author Celine Souchet
 */
public class ArchivedCaseDatastore extends CommonDatastore<ArchivedCaseItem, ArchivedProcessInstance> implements DatastoreHasGet<ArchivedCaseItem>,
        DatastoreHasSearch<ArchivedCaseItem>, DatastoreHasDelete {

    public ArchivedCaseDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    protected ArchivedCaseItem convertEngineToConsoleItem(final ArchivedProcessInstance item) {
        final ArchivedCaseItem result = new ArchivedCaseItem();
        result.setId(item.getId());
        result.setLastUpdateDate(item.getLastUpdate());
        result.setState(item.getState());
        result.setStartDate(item.getStartDate());
        result.setEndDate(item.getEndDate());
        result.setProcessId(item.getProcessDefinitionId());
        result.setArchivedDate(item.getArchiveDate());
        result.setSourceObjectId(item.getSourceObjectId());
        result.setRootCaseId(item.getRootProcessInstanceId());
        result.setStartedByUserId(item.getStartedBy());
        result.setStartedBySubstituteUserId(item.getStartedBySubstitute());
        return result;
    }

    @Override
    public ItemSearchResult<ArchivedCaseItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        try {
            // Build search
            final SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, orders, search);
            addFilterToSearchBuilder(filters, builder, ArchivedCaseItem.ATTRIBUTE_PROCESS_ID, ProcessInstanceSearchDescriptor.PROCESS_DEFINITION_ID);
            addFilterToSearchBuilder(filters, builder, ArchivedCaseItem.ATTRIBUTE_STARTED_BY_USER_ID, ProcessInstanceSearchDescriptor.STARTED_BY);
            addFilterToSearchBuilder(filters, builder, ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID, ArchivedProcessInstancesSearchDescriptor.SOURCE_OBJECT_ID);

            // Run search depending on filters passed
            final SearchResult<ArchivedProcessInstance> searchResult = runSearch(filters, builder);

            // Convert to ConsoleItems
            return new ItemSearchResult<ArchivedCaseItem>(
                    page,
                    resultsByPage,
                    searchResult.getCount(),
                    convertEngineToConsoleItemsList(searchResult.getResult()));
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private SearchResult<ArchivedProcessInstance> runSearch(final Map<String, String> filters, final SearchOptionsBuilder builder) throws BonitaException {
        final ProcessAPI processAPI = getProcessApi();

        if (filters.containsKey(ArchivedCaseItem.FILTER_USER_ID)) {
            return processAPI.searchArchivedProcessInstancesInvolvingUser(MapUtil.getValueAsLong(filters, ArchivedCaseItem.FILTER_USER_ID),
                    builder.done());
        }

        if (filters.containsKey(ArchivedCaseItem.FILTER_SUPERVISOR_ID)) {
            return processAPI
                    .searchArchivedProcessInstancesSupervisedBy(MapUtil.getValueAsLong(filters, ArchivedCaseItem.FILTER_SUPERVISOR_ID), builder.done());
        }

        if (filters.containsKey(CaseItem.FILTER_CALLER) && "any".equalsIgnoreCase(filters.get(CaseItem.FILTER_CALLER))) {
            builder.filter(ArchivedProcessInstancesSearchDescriptor.STATE_ID, ProcessInstanceState.COMPLETED.getId());
            return processAPI.searchArchivedProcessInstancesInAllStates(builder.done());
        }

        return processAPI.searchArchivedProcessInstances(builder.done());
    }

    @Override
    public ArchivedCaseItem get(final APIID id) {
        try {
            return convertEngineToConsoleItem(getProcessApi().getArchivedProcessInstance(id.toLong()));
            // .getFinalArchivedProcessInstance(id.toLong()));
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public void delete(final List<APIID> ids) {
        try {
            final ProcessAPI processAPI = getProcessApi();
            final List<Long> toDeleteIds = getSourceObjectIdOfArchivedProcessInstancesToDelete(ids, processAPI);
            processAPI.deleteArchivedProcessInstancesInAllStates(toDeleteIds);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private List<Long> getSourceObjectIdOfArchivedProcessInstancesToDelete(final List<APIID> ids, final ProcessAPI processAPI) throws SearchException {
        if (!ids.isEmpty()) {
            final SearchOptions searchOptionsBuilder = buildSearchOptionsToFilterOnId(ids);
            final List<ArchivedProcessInstance> result = processAPI.searchArchivedProcessInstances(searchOptionsBuilder).getResult();
            return getSourceObjectIds(result);
        }
        return new ArrayList<Long>();
    }

    SearchOptions buildSearchOptionsToFilterOnId(final List<APIID> ids) {
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, ids.size());
        searchOptionsBuilder.filter(ArchivedProcessInstancesSearchDescriptor.ID, ids.get(0).toLong());
        for (int i = 1; i < ids.size(); i++) {
            searchOptionsBuilder.or().filter(ArchivedProcessInstancesSearchDescriptor.ID, ids.get(i).toLong());
        }
        return searchOptionsBuilder.done();
    }

    private List<Long> getSourceObjectIds(final List<ArchivedProcessInstance> result) {
        final List<Long> toDeleteIds = new ArrayList<Long>();
        for (final ArchivedProcessInstance archivedProcessInstance : result) {
            toDeleteIds.add(archivedProcessInstance.getSourceObjectId());
        }
        return toDeleteIds;
    }

    public ProcessAPI getProcessApi() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getProcessAPI(getEngineSession());
    }
}
