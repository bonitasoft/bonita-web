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

import java.util.Date;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * REST resource to operate on BPM Timer event triggers.
 *
 * @author Emmanuel Duchastenier
 */
public class TimerEventTriggerResource extends CommonResource {

    public static final String ID_PARAM_NAME = "id";

    private final ProcessAPI processAPI;

    public TimerEventTriggerResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Get("json")
    public List<TimerEventTriggerInstance> searchTimerEventTriggers() {
        try {
            final Long caseId = getLongParameter("caseId", true);
            final List<TimerEventTriggerInstance> triggers = runEngineSearch(caseId, buildSearchOptions());
            return triggers;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected List<TimerEventTriggerInstance> runEngineSearch(final long caseId, final SearchOptions searchOptions) throws SearchException {
        final SearchResult<TimerEventTriggerInstance> searchResult = processAPI.searchTimerEventTriggerInstances(caseId, searchOptions);
        setContentRange(searchResult);
        return searchResult.getResult();
    }

    @Put("json")
    public TimerEventTrigger updateTimerEventTrigger(final TimerEventTrigger trigger) throws Exception {
        final String triggerId = getAttribute(ID_PARAM_NAME);
        if (triggerId == null) {
            throw new APIException("Attribute '" + ID_PARAM_NAME + "' is mandatory");
        }
        final long timerEventTriggerInstanceId = Long.parseLong(triggerId);
        final Date executionDate = new Date(trigger.getExecutionDate());
        return createTimerEventTrigger(processAPI.updateExecutionDateOfTimerEventTriggerInstance(timerEventTriggerInstanceId, executionDate)
                .getTime());
    }

    /**
     * Builds the TimerEventTrigger to return to the REST call.
     *
     * @param executionDate the execution date for this TimerEventTrigger.
     * @return the new constructed object.
     */
    private TimerEventTrigger createTimerEventTrigger(final long executionDate) {
        return new TimerEventTrigger(executionDate);
    }

    @Override
    public String getAttribute(final String attributeName) {
        return super.getAttribute(attributeName);
    }

}
