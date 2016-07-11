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
package org.bonitasoft.web.rest.server.api.bpm.process;

import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessResolutionProblemDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.rest.server.framework.exception.APIFilterMandatoryException;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 */
public class APIProcessResolutionProblem extends ConsoleAPI<ProcessResolutionProblemItem> implements APIHasSearch<ProcessResolutionProblemItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(ProcessResolutionProblemDefinition.TOKEN);
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new ProcessResolutionProblemDatastore(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return ProcessResolutionProblemItem.ATTRIBUTE_TARGET_TYPE;
    }

    @Override
    public ItemSearchResult<ProcessResolutionProblemItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        if (!filters.containsKey(ProcessResolutionProblemItem.FILTER_PROCESS_ID)) {
            throw new APIFilterMandatoryException(ProcessResolutionProblemItem.FILTER_PROCESS_ID);
        }

        return super.search(page, resultsByPage, search, orders, filters);
    }

}
