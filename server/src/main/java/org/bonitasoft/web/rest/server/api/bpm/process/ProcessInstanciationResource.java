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
package org.bonitasoft.web.rest.server.api.bpm.process;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.rest.server.api.resource.ErrorMessageWithExplanations;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.data.Status;
import org.restlet.resource.Post;

/**
 * @author Nicolas Tith
 */
public class ProcessInstanciationResource extends CommonResource {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";

    private final ProcessAPI processAPI;

    public ProcessInstanciationResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Post("json")
    public void instanciateProcess(final Map<String, Serializable> inputs) throws ProcessDefinitionNotFoundException, ProcessActivationException,
            ProcessExecutionException, FlowNodeExecutionException {
        try {
        processAPI.startProcessWithInputs(getProcessDefinitionIdParameter(), inputs);

        } catch (final ContractViolationException e) {
            if (getLogger().isLoggable(Level.INFO)) {
                final StringBuilder explanations = new StringBuilder();
                for (final String explanation : e.getExplanations()) {
                    explanations.append(explanation);
                }
                getLogger().log(Level.INFO, e.getMessage() + "\nExplanations:\n" + explanations);
            }
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Cannot instanciate process task.");
            final ErrorMessageWithExplanations errorMessage = new ErrorMessageWithExplanations(e);
            errorMessage.setExplanations(e.getExplanations());
            getResponse().setEntity(errorMessage.toEntity());
        }

    }

    protected long getProcessDefinitionIdParameter() {
        final String taskId = getAttribute(PROCESS_DEFINITION_ID);
        if (taskId == null) {
            throw new APIException("Attribute '" + PROCESS_DEFINITION_ID + "' is mandatory");
        }
        return Long.parseLong(taskId);
    }
}
