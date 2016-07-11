package org.bonitasoft.web.rest.server.api.bpm.flownode;

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

public class UserTaskContextResource extends CommonResource {
    static final String TASK_ID = "taskId";

    private final ProcessAPI processAPI;
    private final FinderFactory resourceHandler;

    public UserTaskContextResource(final ProcessAPI processAPI, FinderFactory resourceHandler) {
        this.processAPI = processAPI;
        this.resourceHandler = resourceHandler;
    }

    @Get("json")
    public Map<String, Serializable> getUserTaskContext() throws UserTaskNotFoundException, ExpressionEvaluationException {
        final Map<String, Serializable> resultMap = new HashMap<String, Serializable>();

        Map<String, Serializable> userTaskExecutionContext = processAPI.getUserTaskExecutionContext(getTaskIdParameter());

        for (Map.Entry<String, Serializable> executionContextElement : userTaskExecutionContext.entrySet()) {
            resultMap.put(executionContextElement.getKey(), getContextResultElement(executionContextElement.getValue()));
        }
        return resultMap;
    }

    private Serializable getContextResultElement(Serializable executionContextElementValue) {
        return resourceHandler.getContextResultElement(executionContextElementValue);
    }

    protected long getTaskIdParameter() {
        final String taskId = getAttribute(TASK_ID);
        if (taskId == null) {
            throw new APIException("Attribute '" + TASK_ID + "' is mandatory in order to get the task context");
        }
        return Long.parseLong(taskId);
    }
}
