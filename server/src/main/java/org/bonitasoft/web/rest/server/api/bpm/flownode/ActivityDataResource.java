package org.bonitasoft.web.rest.server.api.bpm.flownode;

import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

public class ActivityDataResource extends CommonResource {

    public static final String ACTIVITYDATA_ACTIVITY_ID = "activityid";
    public static final String ACTIVITYDATA_DATA_NAME = "dataname";

    @Get
    public Representation doGet() {
        try {
            final String taskId = getAttribute(ACTIVITYDATA_ACTIVITY_ID);
            final String dataName = getAttribute(ACTIVITYDATA_DATA_NAME);
            final DataInstance dataInstance = getTaskVariableInstance(dataName, Long.valueOf(taskId));
            return new StringRepresentation(toJson(dataInstance), MediaType.APPLICATION_JSON);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }


    private DataInstance getTaskVariableInstance(final String dataName, final Long activityInstanceId) throws DataNotFoundException {
            return getEngineProcessAPI().getActivityDataInstance(dataName, activityInstanceId);
    }
}
