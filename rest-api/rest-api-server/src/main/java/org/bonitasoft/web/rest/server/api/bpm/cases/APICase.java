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
package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.bpm.process.ProcessInstanceCriterion;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 */
public class APICase extends ConsoleAPI<CaseItem> implements APIHasGet<CaseItem>, APIHasAdd<CaseItem>, APIHasSearch<CaseItem>, APIHasDelete {
    
    
    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(CaseDefinition.TOKEN);
    }

    @Override
    public CaseItem add(final CaseItem caseItem) {
        return new CaseDatastore(getEngineSession()).add(caseItem);
    }
    
    @Override
    public CaseItem get(final APIID id) {
        return new CaseDatastore(getEngineSession()).get(id);
    }

    @Override
    public ItemSearchResult<CaseItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        // Check that team manager and supervisor filters are not used together
        if (filters.containsKey(CaseItem.FILTER_TEAM_MANAGER_ID) && filters.containsKey(CaseItem.FILTER_SUPERVISOR_ID)) {
            throw new APIException("Can't set those filters at the same time : " + CaseItem.FILTER_TEAM_MANAGER_ID + " and "
                    + CaseItem.FILTER_SUPERVISOR_ID);
        }

        return new CaseDatastore(getEngineSession()).search(page, resultsByPage, search, orders, filters);
    }

    @Override
    public String defineDefaultSearchOrder() {
        return ProcessInstanceCriterion.CREATION_DATE_DESC.name();
    }

    @Override
    protected void fillDeploys(final CaseItem item, final List<String> deploys) {
        if (isDeployable(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID, deploys, item)) {
            item.setDeploy(
                    CaseItem.ATTRIBUTE_STARTED_BY_USER_ID,
                    new UserDatastore(getEngineSession()).get(item.getStartedByUserId()));
        }

        if (isDeployable(CaseItem.ATTRIBUTE_PROCESS_ID, deploys, item)) {
            item.setDeploy(
                    CaseItem.ATTRIBUTE_PROCESS_ID,
                    new ProcessDatastore(getEngineSession()).get(item.getProcessId()));
        }
    }

    @Override
    public void delete(final List<APIID> ids) {
        new CaseDatastore(getEngineSession()).delete(ids);
    }

    @Override
    protected void fillCounters(final CaseItem item, final List<String> counters) {
    }

}
