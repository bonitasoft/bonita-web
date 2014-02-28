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
package org.bonitasoft.web.rest.server.datastore.bpm.process;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.Problem;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

/**
 * @author Séverin Moussel
 * 
 */
public class ProcessResolutionProblemDatastore extends CommonDatastore<ProcessResolutionProblemItem, Problem> implements
        DatastoreHasSearch<ProcessResolutionProblemItem> {

    public ProcessResolutionProblemDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    protected ProcessResolutionProblemItem convertEngineToConsoleItem(final Problem item) {
        final ProcessResolutionProblemItem consoleItem = new ProcessResolutionProblemItem();

        consoleItem.setMessage(item.getDescription());
        consoleItem.setTargetType(item.getResource());

        return consoleItem;
    }

    @Override
    public ItemSearchResult<ProcessResolutionProblemItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        try {

            final List<Problem> errors = TenantAPIAccessor.getProcessAPI(getEngineSession()).getProcessResolutionProblems(
                    MapUtil.getValueAsLong(filters, ProcessResolutionProblemItem.FILTER_PROCESS_ID));

            final int startIndex = page * resultsByPage;
            return new ItemSearchResult<ProcessResolutionProblemItem>(
                    page,
                    resultsByPage,
                    errors.size(),
                    convertEngineToConsoleItemsList(
                    errors.subList(
                            startIndex,
                            Math.min(startIndex + resultsByPage, errors.size())
                            )
                    ));

        } catch (final Exception e) {
            throw new APIException(e);
        }
    }
}
