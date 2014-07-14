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

import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedActivityItem;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.converter.ArchivedActivitySearchDescriptorConverter;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractArchivedActivityDatastore<CONSOLE_ITEM extends ArchivedActivityItem, ENGINE_ITEM extends ArchivedActivityInstance>
        extends AbstractArchivedFlowNodeDatastore<CONSOLE_ITEM, ENGINE_ITEM> {

    public AbstractArchivedActivityDatastore(final APISession engineSession) {
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
    public static ArchivedActivityItem fillConsoleItem(final ArchivedActivityItem result, final ArchivedActivityInstance item) {
        ArchivedFlowNodeDatastore.fillConsoleItem(result, item);

        result.setReachStateDate(item.getReachedStateDate());
        result.setLastUpdateDate(item.getLastUpdateDate());

        return result;
    }

    @Override
    protected ArchivedActivitySearchDescriptorConverter getSearchDescriptorConverter() {
        return new ArchivedActivitySearchDescriptorConverter();
    }

    @Override
    protected SearchResult<ENGINE_ITEM> runSearch(final SearchOptionsCreator creator, final Map<String, String> filters) {
        try {
            @SuppressWarnings("unchecked")
            final SearchResult<ENGINE_ITEM> result = (SearchResult<ENGINE_ITEM>) getProcessAPI().searchArchivedActivities(
                    creator.create());

            return result;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

}
