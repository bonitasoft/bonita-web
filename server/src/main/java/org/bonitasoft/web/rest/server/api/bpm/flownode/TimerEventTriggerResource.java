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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * REST resource to operate on BPM Timer event triggers.
 *
 * @author Emmanuel Duchastenier
 */
public class TimerEventTriggerResource extends CommonResource {

    static final String DATE_PARAM_NAME = "date";

    static final String ID_PARAM_NAME = "id";

    @Get
    public Representation searchTimerEventTriggers() {
        try {
            final Long caseId = getLongParameter("caseId", true);
            final List<TimerEventTriggerInstance> triggers = runEngineSearch(caseId, buildSearchOptions());
            //            System.out.println("List of triggers IDs: " + triggers);
            return new StringRepresentation(toJsonArray(triggers), MediaType.APPLICATION_JSON);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected List<TimerEventTriggerInstance> runEngineSearch(final long caseId, final SearchOptions searchOptions) throws SearchException {
        return getEngineProcessAPI().searchTimerEventTriggerInstances(caseId, searchOptions).getResult();
    }

    @Put("json")
    public String updateTimerEventTrigger(final String jsonMsg) throws Exception {
        final JSONObject jsonObject = new JSONObject(jsonMsg.toString());
        final String triggerId = getAttribute(ID_PARAM_NAME);
        if (triggerId == null) {
            throw new APIException("Attribute '" + ID_PARAM_NAME + "' is mandatory");
        }
        final long timerEventTriggerInstanceId = Long.parseLong(triggerId);
        final Date executionDate = new SimpleDateFormat(JSON_DATE_FORMAT).parse(jsonObject.getString(DATE_PARAM_NAME));
        final Date newDate = getEngineProcessAPI().updateExecutionDateOfTimerEventTriggerInstance(timerEventTriggerInstanceId, executionDate);
        return toJson(newDate);
    }
}
