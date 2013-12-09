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
package org.bonitasoft.web.rest.server.engineclient;


import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

/**
 * @author Colin PUY
 * @author Elias Ricken de Medeiros
 * 
 */
public class HumanTaskEngineClient {

    private ProcessAPI processAPI;

    public HumanTaskEngineClient(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    public long countOpenedHumanTasks() {
        SearchOptions search = new SearchOptionsBuilder(0, 0)
                .filter(HumanTaskInstanceSearchDescriptor.STATE_NAME, HumanTaskItem.VALUE_STATE_READY).done();
        try {
            return processAPI.searchHumanTaskInstances(search).getCount();
        } catch (Exception e) {
            throw new APIException("Error when counting opened cases");
        }
    }
    
    public List<ArchivedHumanTaskInstance> searchArchivedHumanTasks(SearchOptions searchOptions) {
        try {
            return processAPI.searchArchivedHumanTasks(searchOptions).getResult();
        } catch (SearchException e) {
            throw new APIException("Error when searching archived human tasks", e);
        }
    }

}
