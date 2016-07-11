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

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.utils.ContractTypeConverter;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseItemConverter;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Post;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Nicolas Tith
 */
public class ProcessInstantiationResource extends CommonResource {

    private static final String CASE_ID_ATTRIBUTE = "caseId";

    static final String PROCESS_DEFINITION_ID = "processDefinitionId";

    private static final String USER_PARAM = "user";

    private final ProcessAPI processAPI;

    private final APISession apiSession;

    protected ContractTypeConverter typeConverterUtil = new ContractTypeConverter(ContractTypeConverter.ISO_8601_DATE_PATTERNS);

    public ProcessInstantiationResource(final ProcessAPI processAPI, final APISession apiSession) {
        this.processAPI = processAPI;
        this.apiSession = apiSession;
    }

    @Post("json")
    public String instantiateProcess(final Map<String, Serializable> inputs) throws ProcessDefinitionNotFoundException, ProcessActivationException,
            ProcessExecutionException, FileNotFoundException {
        final String userId = getRequestParameter(USER_PARAM);
        final long processDefinitionId = getProcessDefinitionIdParameter();
        try {
            final ContractDefinition processContract = processAPI.getProcessContract(processDefinitionId);
            final long tenantId = apiSession.getTenantId();
            final long maxSizeForTenant = PropertiesFactory.getConsoleProperties(tenantId).getMaxSize();
            final Map<String, Serializable> processedInputs = typeConverterUtil.getProcessedInput(processContract, inputs, maxSizeForTenant, tenantId, false);
            long processInstanceId;
            if (userId == null) {
                processInstanceId = processAPI.startProcessWithInputs(processDefinitionId, processedInputs).getId();
            } else {
                processInstanceId = processAPI.startProcessWithInputs(Long.parseLong(userId), processDefinitionId, processedInputs).getId();
            }
            //clean temp files
            deleteFiles(processContract, inputs, maxSizeForTenant, tenantId);

            final JsonNodeFactory factory = JsonNodeFactory.instance;
            final ObjectNode returnedObject = factory.objectNode();
            returnedObject.put(CASE_ID_ATTRIBUTE, processInstanceId);
            return returnedObject.toString();
        } catch (final ContractViolationException e) {
            manageContractViolationException(e, "Cannot instantiate process task.");
            return null;
        }
    }

    protected void deleteFiles(final ContractDefinition processContract, final Map<String, Serializable> inputs, final long maxSizeForTenant, final long tenantId) throws FileNotFoundException {
        typeConverterUtil.getProcessedInput(processContract, inputs, maxSizeForTenant, tenantId, true);
    }

    protected CaseItem convertEngineToConsoleItem(final ProcessInstance item) {
        return new CaseItemConverter().convert(item);
    }

    protected long getProcessDefinitionIdParameter() {
        final String processDefinitionId = getAttribute(PROCESS_DEFINITION_ID);
        if (processDefinitionId == null) {
            throw new APIException("Attribute '" + PROCESS_DEFINITION_ID + "' is mandatory");
        }
        return Long.parseLong(processDefinitionId);
    }
}
