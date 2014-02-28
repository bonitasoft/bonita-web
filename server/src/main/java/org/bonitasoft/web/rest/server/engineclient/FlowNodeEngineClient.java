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
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstanceNotFoundException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;


/**
 * @author Elias Ricken de Medeiros
 *
 */
public class FlowNodeEngineClient {
    
    protected ProcessAPI processAPI;

    public FlowNodeEngineClient(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }
    
    public List<ArchivedFlowNodeInstance> searchArchivedFlowNodes(SearchOptions searchOptions) {
        try {
            return processAPI.searchArchivedFlowNodeInstances(searchOptions).getResult();
        } catch (Exception e) {
            throw new APIException("Error when searching arquived flow nodes", e);
        }
    }
    
    public FlowNodeInstance getFlowNodeInstance(long flowNodeInstanceId) {
        try {
            return processAPI.getFlowNodeInstance(flowNodeInstanceId);
        } catch (FlowNodeInstanceNotFoundException e) {
            throw new APIItemNotFoundException(FlowNodeDefinition.TOKEN, APIID.makeAPIID(flowNodeInstanceId));
        } catch (Exception e) {
            throw new APIException("Error when getting flow node instance", e);
        }
    }

}
