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

import org.bonitasoft.console.common.server.utils.ContractTypeConverter;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Post;

/**
 * @author Emmanuel Duchastenier
 * @author Fabio Lombardi
 */
public class UserTaskExecutionResource extends CommonResource {

    static final String TASK_ID = "taskId";

	private static final String USER_PARAM = "user";

    private final ProcessAPI processAPI;

    protected ContractTypeConverter typeConverterUtil = new ContractTypeConverter(ContractTypeConverter.ISO_8601_DATE_PATTERNS);

    public UserTaskExecutionResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Post("json")
    public void executeTask(final Map<String, Serializable> inputs) throws UserTaskNotFoundException, FlowNodeExecutionException {
        final String userId = getRequestParameter(USER_PARAM);
        final long taskId = getTaskIdParameter();
        try {
            final ContractDefinition taskContract = processAPI.getUserTaskContract(taskId);
            final Map<String, Serializable> processedInputs = typeConverterUtil.getProcessedInput(taskContract, inputs);
    		if (userId == null) {
                processAPI.executeUserTask(taskId, processedInputs);
            } else {
                processAPI.executeUserTask(Long.parseLong(userId), taskId, processedInputs);
    		}
        } catch (final ContractViolationException e) {
            manageContractViolationException(e, "Cannot execute task.");
        }
    }

    protected long getTaskIdParameter() {
        final String taskId = getAttribute(TASK_ID);
        if (taskId == null) {
            throw new APIException("Attribute '" + TASK_ID + "' is mandatory");
        }
        return Long.parseLong(taskId);
    }
}
