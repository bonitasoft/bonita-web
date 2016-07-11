package org.bonitasoft.web.rest.server.api.bpm.flownode.archive;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.engine.expression.ExpressionEvaluationException;
import org.bonitasoft.web.rest.server.FinderFactory;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Get;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fabio Lombardi
 */
public class ArchivedUserTaskContextResource extends CommonResource {
    static final String ARCHIVED_TASK_ID = "archivedTaskId";

    private final ProcessAPI processAPI;
    private FinderFactory resourceHandler;

   public ArchivedUserTaskContextResource(final ProcessAPI processAPI, FinderFactory resourceHandler) {
       this.processAPI = processAPI;
       this.resourceHandler = resourceHandler;
   }

    @Get("json")
    public Map<String, Serializable> getArchivedUserTaskContext() throws UserTaskNotFoundException, ExpressionEvaluationException {
        final Map<String, Serializable> resultMap = new HashMap<String, Serializable>();

        Map<String, Serializable> archivedUserTaskExecutionContext = processAPI.getArchivedUserTaskExecutionContext(getArchivedTaskIdParameter());

        for (Map.Entry<String, Serializable> executionContextElement : archivedUserTaskExecutionContext.entrySet()) {
            resultMap.put(executionContextElement.getKey(), getContextResultElement(executionContextElement.getValue()));
        }
        return resultMap;
    }

    private Serializable getContextResultElement(Serializable executionContextElementValue) {
        return resourceHandler.getContextResultElement(executionContextElementValue);
    }

    protected long getArchivedTaskIdParameter() {
        final String taskId = getAttribute(ARCHIVED_TASK_ID);
        if (taskId == null) {
            throw new APIException("Attribute '" + ARCHIVED_TASK_ID + "' is mandatory in order to get the archived task context");
        }
        return Long.parseLong(taskId);
    }
}
