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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.rest.server.api.resource.ErrorMessageWithExplanations;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * @author Emmanuel Duchastenier
 * @author Fabio Lombardi
 */
public class TaskResource extends CommonResource {

    private static final String TASK_ID = "taskId";

    private final ProcessAPI processAPI;

    public TaskResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Get("json")
    public ContractDefinition getContract() throws UserTaskNotFoundException {
        return processAPI.getUserTaskContract(getTaskIDParameter());
    }

    @Post("json")
    public void executeTask(final Map<String, Serializable> inputs) throws UserTaskNotFoundException, FlowNodeExecutionException {
        try {
            processAPI.executeUserTask(getTaskIDParameter(), inputs);
        } catch (final ContractViolationException e) {
            if (getLogger().isLoggable(Level.INFO)) {
                final StringBuilder explanations = new StringBuilder();
                for (final String explanation : e.getExplanations()) {
                    explanations.append(explanation);
                }
                getLogger().log(Level.INFO, e.getMessage() + "\nExplanations:\n" + explanations);
            }
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Cannot execute task.");
            final ErrorMessageWithExplanations errorMessage = new ErrorMessageWithExplanations(e);
            errorMessage.setExplanations(e.getExplanations());
            getResponse().setEntity(errorMessage.toEntity());
        }
    }

    protected long getTaskIDParameter() {
        final String taskId = getAttribute(TASK_ID);
        if (taskId == null) {
            throw new APIException("Attribute '" + TASK_ID + "' is mandatory");
        }
        return Long.parseLong(taskId);
    }
}
