/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.web.rest.server.api.bpm.process.script;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Put;

/**
 * @author Nicolas Tith
 */
public class ProcessScriptResource extends CommonResource {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";

    private static final String SCRIPT_ID = "scriptId";

    private final ProcessAPI processAPI;

    public ProcessScriptResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Put("json")
    public void updateScript(final ScriptDefinition script) throws ProcessDefinitionNotFoundException {
        // processAPI.updateProcessDefinitionScript(getProcessDefinitionIdParameter(), getScriptIdParameter(), script.getContent());
        getProcessDefinitionIdParameter();
        getScriptIdParameter();
        script.getContent();
    }

    protected long getProcessDefinitionIdParameter() {
        final String processDefinitionId = getAttribute(PROCESS_DEFINITION_ID);
        if (processDefinitionId == null) {
            throw new APIException("Attribute '" + PROCESS_DEFINITION_ID + "' is mandatory");
        }
        return Long.parseLong(processDefinitionId);
    }

    protected long getScriptIdParameter() {
        final String scriptId = getAttribute(SCRIPT_ID);
        if (scriptId == null) {
            throw new APIException("Attribute '" + SCRIPT_ID + "' is mandatory");
        }
        return Long.parseLong(scriptId);
    }
}
