package org.bonitasoft.web.rest.server.api.bpm.cases;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
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
public class ArchivedCaseContextResource extends CommonResource {
    static final String ARCHIVED_CASE_ID = "archivedCaseId";

    private final ProcessAPI processAPI;
    private FinderFactory resourceHandler;

    public ArchivedCaseContextResource (final ProcessAPI processAPI, FinderFactory resourceHandler) {
        this.processAPI = processAPI;
        this.resourceHandler = resourceHandler;
    }

    @Get("json")
    public Map<String, Serializable> getArchivedCaseContext() throws ExpressionEvaluationException, ProcessInstanceNotFoundException {
        final Map<String, Serializable> resultMap = new HashMap<String, Serializable>();

        Map<String, Serializable> caseExecutionContext = processAPI.getArchivedProcessInstanceExecutionContext(getArchivedCaseIdParameter());

        for (Map.Entry<String, Serializable> caseContextElement : caseExecutionContext.entrySet()) {
            resultMap.put(caseContextElement.getKey(), getContextResultElement(caseContextElement.getValue()));
        }
        return resultMap;
    }

    private Serializable getContextResultElement(Serializable executionContextElementValue) {
        return resourceHandler.getContextResultElement(executionContextElementValue);
    }

    protected long getArchivedCaseIdParameter() {
        final String caseId = getAttribute(ARCHIVED_CASE_ID);
        if (caseId == null) {
            throw new APIException("Attribute '" + ARCHIVED_CASE_ID + "' is mandatory in order to get the archived case context");
        }
        return Long.parseLong(caseId);
    }
}
