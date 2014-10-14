package org.bonitasoft.web.rest.server.api.bpm.flownode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance;
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstanceNotFoundException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.rest.server.datastore.converter.EmptyAttributeConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * REST resource to operate on
 *
 * @author Emmanuel Duchastenier
 */
public class TimerEventTriggerResource extends CommonResource {

    private APISession sessionSingleton = null;

    /**
     * Get the session to access the engine SDK
     */
    @Override
    protected APISession getEngineSession() {
        if (sessionSingleton == null) {
            sessionSingleton = (APISession) ServletUtils.getRequest(getRequest()).getSession().getAttribute("apiSession");
        }
        return sessionSingleton;
    }

    @Get("json")
    public String searchTimerEventTriggers() {
        try {
            final List<TimerEventTriggerInstance> triggers = searchEngineTimerEventTriggers(getMandatoryParameter(Long.class, "caseId"),
                    new SearchOptionsCreator(
                            getSearchPageNumber(),
                            getSearchPageSize(), getSearchTerm(), new Sorts(""),
                            new Filters(Collections.<String, String> emptyMap(), new GenericFilterCreator(new EmptyAttributeConverter()))).create());
            return toJson(triggers);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private List<TimerEventTriggerInstance> searchEngineTimerEventTriggers(final long caseId, final SearchOptions searchOptions) throws SearchException {
        return getEngineProcessAPI().searchTimerEventTriggerInstances(caseId, searchOptions).getResult();
    }

    @Put("json")
    public String updateTimerEventTrigger(final String jsonMsg) throws TimerEventTriggerInstanceNotFoundException, UpdateException, ParseException,
            JSONException {
        final JSONObject jsonObject = new JSONObject(jsonMsg);
        final long timerEventTriggerInstanceId = jsonObject.getLong("ID");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date executionDate = sdf.parse(jsonObject.getString("date"));
        final Date newDate = getEngineProcessAPI().updateExecutionDateOfTimerEventTriggerInstance(timerEventTriggerInstanceId, executionDate);
        return toJson(newDate);
    }
}
