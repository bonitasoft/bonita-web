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
package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstanceSearchDescriptor;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractFlowNodeDatastore<CONSOLE_ITEM extends FlowNodeItem, ENGINE_ITEM extends FlowNodeInstance>
        extends CommonDatastore<CONSOLE_ITEM, ENGINE_ITEM>
        implements DatastoreHasSearch<CONSOLE_ITEM>,
        DatastoreHasGet<CONSOLE_ITEM>,
        DatastoreHasUpdate<CONSOLE_ITEM>
{

    private DatastoreHasUpdate<FlowNodeItem> updateHelper;

    public AbstractFlowNodeDatastore(final APISession engineSession) {
        super(engineSession);
    }

    /**
     * Fill a console item using the engine item passed.
     * 
     * @param result
     *            The console item to fill
     * @param item
     *            The engine item to use for filling
     * @return This method returns the result parameter passed.
     */
    protected static final FlowNodeItem fillConsoleItem(final FlowNodeItem result, final FlowNodeInstance item) {

        result.setId(item.getId());
        result.setName(item.getName());
        result.setDisplayName(item.getDisplayName());
        result.setDescription(item.getDescription());
        result.setDisplayDescription(item.getDisplayDescription());
        result.setExecutedByUserId(item.getExecutedBy());
        result.setCaseId(item.getRootContainerId());
        result.setProcessId(item.getProcessDefinitionId());
        result.setState(item.getState());
        result.setType(item.getType().name());
        result.setRootContainerId(item.getRootContainerId());

        return result;
    }

    protected final ProcessAPI getProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    protected CONSOLE_ITEM convertEngineToConsoleItem(final ENGINE_ITEM item) {

        @SuppressWarnings("unchecked")
        final CONSOLE_ITEM result = (CONSOLE_ITEM) FlowNodeConverter.convertEngineToConsoleItem(item);

        return result;
    }

    public long count(final String search, final String orders, final Map<String, String> filters) {
        return search(0, 0, search, orders, filters).getTotal();
    }

    @Override
    public CONSOLE_ITEM get(final APIID id) {
        try {
            @SuppressWarnings("unchecked")
            final ENGINE_ITEM flowNodeInstance = (ENGINE_ITEM) getProcessAPI().getFlowNodeInstance(id.toLong());
            return convertEngineToConsoleItem(flowNodeInstance);
        } catch (final NotFoundException e) {
            throw new APIItemNotFoundException(FlowNodeDefinition.TOKEN, id);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public ItemSearchResult<CONSOLE_ITEM> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        try {
            final SearchOptionsBuilder builder = makeSearchOptionBuilder(page, resultsByPage, search, orders, filters);

            final SearchResult<ENGINE_ITEM> results = runSearch(builder, filters);

            return new ItemSearchResult<CONSOLE_ITEM>(
                    page,
                    resultsByPage,
                    results.getCount(),
                    convertEngineToConsoleItemsList(results.getResult()));

        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected SearchResult<ENGINE_ITEM> runSearch(final SearchOptionsBuilder builder, final Map<String, String> filters) {
        try {
            @SuppressWarnings("unchecked")
            final SearchResult<ENGINE_ITEM> result = (SearchResult<ENGINE_ITEM>) getProcessAPI().searchFlowNodeInstances(
                    builder.done());

            return result;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected SearchOptionsBuilder makeSearchOptionBuilder(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        final SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, orders, search);

        addFilterToSearchBuilder(filters, builder, FlowNodeItem.ATTRIBUTE_CASE_ID, FlowNodeInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID);
        addFilterToSearchBuilder(filters, builder, FlowNodeItem.ATTRIBUTE_PROCESS_ID, FlowNodeInstanceSearchDescriptor.PROCESS_DEFINITION_ID);
        addFilterToSearchBuilder(filters, builder, FlowNodeItem.ATTRIBUTE_STATE, FlowNodeInstanceSearchDescriptor.STATE_NAME);
        addFilterToSearchBuilder(filters, builder, TaskItem.ATTRIBUTE_LAST_UPDATE_DATE, FlowNodeInstanceSearchDescriptor.LAST_UPDATE_DATE);

        return builder;
    }

    public AbstractFlowNodeDatastore<CONSOLE_ITEM, ENGINE_ITEM> setUpdateHelper(DatastoreHasUpdate<FlowNodeItem> updateHelper) {
        this.updateHelper = updateHelper;
        return this;
    }

    @Override
    public CONSOLE_ITEM update(APIID id, Map<String, String> attributes) {
        if (updateHelper != null) {
            /*
             * Generics are useless in this class.
             * It only result in casting issues.
             * It needs to be badly removed.
             */
            return (CONSOLE_ITEM) updateHelper.update(id, attributes);
        }
        return null;
    }

}
