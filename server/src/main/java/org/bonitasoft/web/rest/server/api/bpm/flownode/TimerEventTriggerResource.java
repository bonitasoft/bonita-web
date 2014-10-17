/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance;
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstanceNotFoundException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * REST resource to operate on
 *
 * @author Emmanuel Duchastenier
 */
public class TimerEventTriggerResource extends CommonResource {

    private static final String DATE_PARAM_NAME = "date";

    private static final String ID_PARAM_NAME = "id";

    @Get
    public Representation searchTimerEventTriggers() {
        try {
            final Long caseId = getLongParameter("caseId", true);
            final List<TimerEventTriggerInstance> triggers = getEngineProcessAPI().searchTimerEventTriggerInstances(caseId, buildSearchOptions()).getResult();
            //            System.out.println("List of triggers IDs: " + triggers);
            return new StringRepresentation(toJsonArray(triggers), MediaType.APPLICATION_JSON);
        } catch (final Exception e) {
            //            System.out.println("converting exception to APIException");
            throw new APIException(e);
        }
    }

    @Put("json")
    public String updateTimerEventTrigger(final String jsonMsg) throws TimerEventTriggerInstanceNotFoundException, JSONException, ParseException,
            UpdateException {
        final JSONObject jsonObject = new JSONObject(jsonMsg.toString());
        final long timerEventTriggerInstanceId = Long.parseLong(getAttribute(ID_PARAM_NAME));
        final Date executionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString(DATE_PARAM_NAME));
        final Date newDate = getEngineProcessAPI().updateExecutionDateOfTimerEventTriggerInstance(timerEventTriggerInstanceId, executionDate);
        return toJson(newDate);
    }
}
