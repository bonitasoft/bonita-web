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

import org.bonitasoft.engine.bpm.flownode.ArchivedUserTaskInstance;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedUserTaskItem;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractArchivedUserTaskDatastore<CONSOLE_ITEM extends ArchivedUserTaskItem, ENGINE_ITEM extends ArchivedUserTaskInstance>
        extends AbstractArchivedHumanTaskDatastore<CONSOLE_ITEM, ENGINE_ITEM> {

    public AbstractArchivedUserTaskDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    protected SearchResult<ENGINE_ITEM> runSearch(final SearchOptionsCreator creator, final Map<String, String> filters) {
        try {
            if (!filters.containsKey(ArchivedUserTaskItem.ATTRIBUTE_TYPE)) {
                filters.put(ArchivedUserTaskItem.ATTRIBUTE_TYPE, ArchivedUserTaskItem.VALUE_TYPE_USER_TASK);
            }

            return super.runSearch(creator, filters);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    protected ENGINE_ITEM runGet(final APIID id) {
        final ENGINE_ITEM result = super.runGet(id);

        if (!(result instanceof ArchivedUserTaskInstance)) {
            throw new APIItemNotFoundException("User task", id);
        }

        return result;
    }
}
