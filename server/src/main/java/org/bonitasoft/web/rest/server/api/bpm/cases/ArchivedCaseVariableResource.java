package org.bonitasoft.web.rest.server.api.bpm.cases;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.ArchivedDataNotFoundException;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseVariable;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APINotFoundException;
import org.restlet.resource.Get;

public class ArchivedCaseVariableResource extends CommonResource {
    static final String CASE_ID = "caseId";
    static final String VARIABLE_NAME = "variableName";

    private final ProcessAPI processAPI;

    public ArchivedCaseVariableResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Get("json")
    public ArchivedCaseVariable getArchivedCaseVariable() {
        var name = getAttribute(VARIABLE_NAME);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Attribute '" + VARIABLE_NAME + "' is mandatory in order to get the archived case variable");
        }
        try {
            var archivedProcessDataInstance = processAPI.getArchivedProcessDataInstance(name, getCaseIdParameter());
            return ArchivedCaseVariable.create(archivedProcessDataInstance);
        } catch (ArchivedDataNotFoundException e) {
            throw new APINotFoundException(e);
        }
    }

    private long getCaseIdParameter() {
        final String caseId = getAttribute(CASE_ID);
        if (caseId == null) {
            throw new IllegalArgumentException(
                    "Attribute '" + CASE_ID + "' is mandatory in order to get the archived case variable");
        }
        return Long.parseLong(caseId);
    }
}
