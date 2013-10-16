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
package org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive;

import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstance;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.FlowNodeConverter;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.converter.ArchivedFlowNodeSearchDescriptorConverter;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.converter.EmptyAttributeConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractArchivedFlowNodeDatastore<CONSOLE_ITEM extends ArchivedFlowNodeItem, ENGINE_ITEM extends ArchivedFlowNodeInstance>
        extends CommonDatastore<CONSOLE_ITEM, ENGINE_ITEM> implements
        DatastoreHasGet<CONSOLE_ITEM>,
        DatastoreHasSearch<CONSOLE_ITEM>
{

    public AbstractArchivedFlowNodeDatastore(final APISession engineSession) {
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
    public static final ArchivedFlowNodeItem fillConsoleItem(final ArchivedFlowNodeItem result, final ArchivedFlowNodeInstance item) {

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
        result.setArchivedDate(item.getArchiveDate());
        result.setSourceObjectId(item.getSourceObjectId());

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

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.S
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // GET

    @Override
    public CONSOLE_ITEM get(final APIID id) {
        try {
            final ENGINE_ITEM archivedFlowNodeInstance = runGet(id);
            return convertEngineToConsoleItem(archivedFlowNodeInstance);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected ENGINE_ITEM runGet(final APIID id) {
        try {
            @SuppressWarnings("unchecked")
            final ENGINE_ITEM archivedFlowNodeInstance = (ENGINE_ITEM) getProcessAPI().getArchivedFlowNodeInstance(id.toLong());
            return archivedFlowNodeInstance;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public ItemSearchResult<CONSOLE_ITEM> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        try {
            final SearchOptionsCreator creator = makeSearchOptionCreator(page, resultsByPage, search, orders, filters);

            final SearchResult<ENGINE_ITEM> results = runSearch(creator, filters);

            return new ItemSearchResult<CONSOLE_ITEM>(
                    page,
                    resultsByPage,
                    results.getCount(),
                    convertEngineToConsoleItemsList(results.getResult()));

        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    /**
     * Run the engine API search method
     */
    protected SearchResult<ENGINE_ITEM> runSearch(final SearchOptionsCreator creator, final Map<String, String> filters) {
        try {
            @SuppressWarnings("unchecked")
            final SearchResult<ENGINE_ITEM> result = (SearchResult<ENGINE_ITEM>) getProcessAPI().searchArchivedFlowNodeInstances(
                    creator.create());

            return result;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected SearchOptionsCreator makeSearchOptionCreator(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        return new SearchOptionsCreator(page,
                resultsByPage,
                search,
                new Sorts(orders, getSearchDescriptorConverter()),
                new Filters(filters, new ArchivedFlowNodeFilterCreator(
                        getSearchDescriptorConverter())));
    }

    protected ArchivedFlowNodeSearchDescriptorConverter getSearchDescriptorConverter() {
        return new ArchivedFlowNodeSearchDescriptorConverter();
    }

    protected AttributeConverter getSortAttributeConverter() {
        return new EmptyAttributeConverter();
    }

}
