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
