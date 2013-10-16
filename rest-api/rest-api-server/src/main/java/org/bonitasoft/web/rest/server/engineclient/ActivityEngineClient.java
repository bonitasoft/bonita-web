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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.Serializable;
import java.util.HashMap;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceSearchDescriptor;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Colin PUY
 * 
 */
public class ActivityEngineClient {

    private ProcessAPI processAPI;

    public ActivityEngineClient(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    public long countFailedActivities() {
        SearchOptions search = new SearchOptionsBuilder(0, 0)
                .filter(ActivityInstanceSearchDescriptor.STATE_NAME, ActivityItem.VALUE_STATE_FAILED).done();
        try {
            return processAPI.searchActivities(search).getCount();
        } catch (Exception e) {
            throw new APIException("Error when counting failed activities", e);
        }
    }
    
    public DataInstance getDataInstance(String dataName, long activityId) {
        try {
            return processAPI.getActivityDataInstance(dataName, activityId);
        } catch (DataNotFoundException e) {
            throw new APIException(_("Unable to find data instance %dataName% for activity %activityId%", 
                    new Arg("dataName", dataName), new Arg("activityId", activityId)), e);
        }
    }
    
    public void updateVariables(long activityId, HashMap<String, Serializable> variables) {
        try {
            processAPI.updateActivityInstanceVariables(activityId, variables);
        } catch (UpdateException e) {
            throw new APIException(_("Error when updating %activityId% activity variables", 
                    new Arg("activityId", activityId)), e);
        }
    }
}
