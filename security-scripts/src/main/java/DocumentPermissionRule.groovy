/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 **/

import com.fasterxml.jackson.databind.ObjectMapper

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.NotFoundException
import org.bonitasoft.engine.session.APISession

/**
 *
 * Let a user access only document on cases that he is involved in
 *
 * <ul>
 *     <li>bpm/document</li>
 *     <li>bpm/archivedDocument</li>
 *     <li>bpm/caseDocument</li>
 * </ul>
 *
 *
 *
 * @author Baptiste Mesta
 */
class DocumentPermissionRule implements PermissionRule {


    public static final String PROCESS_INSTANCE_ID = "processInstanceId"
    public static final String CASE_ID = "caseId"

    @Override
    public boolean isAllowed(APISession apiSession, APICallContext apiCallContext, APIAccessor apiAccessor, Logger logger) {
        long currentUserId = apiSession.getUserId();
        if (apiCallContext.isGET()) {
            return checkGetMethod(apiCallContext, apiAccessor, currentUserId)
        } else if (apiCallContext.isPOST()) {
            return checkPostMethod(apiCallContext, apiAccessor, currentUserId, logger)
        }
        return false
    }

    private boolean checkPostMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId, Logger logger) {

        ObjectMapper mapper = new ObjectMapper();
        def map = mapper.readValue(apiCallContext.getBody(), Map.class)

        def string = map.get((PROCESS_INSTANCE_ID))
        if (string == null || string.toString().isEmpty()) {
            return true;
        }
        def processInstanceId = Long.valueOf(string.toString())
        if (processInstanceId <= 0) {
            return true;
        }
        try {
            def processAPI = apiAccessor.getProcessAPI()
            return isInvolved(processAPI, currentUserId, processInstanceId)
        } catch (NotFoundException e) {
            return true
        }
    }

    private boolean checkGetMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId) {
        def filters = apiCallContext.getFilters()
        def processAPI = apiAccessor.getProcessAPI()
        def processInstanceIdAsString = filters.get(PROCESS_INSTANCE_ID)
        if (processInstanceIdAsString == null) {
            processInstanceIdAsString = filters.get(CASE_ID)
        }
        if (processInstanceIdAsString != null) {
            def processInstanceId = Long.valueOf(processInstanceIdAsString)
            return isInvolved(processAPI, currentUserId, processInstanceId) ||
                    processAPI.isUserProcessSupervisor(processAPI.getProcessInstance(processInstanceId).getProcessDefinitionId(), currentUserId)
        }
        return false;
    }


    private boolean isInvolved(ProcessAPI processAPI, long currentUserId, long processInstanceId) {
        try {
            return processAPI.isInvolvedInProcessInstance(currentUserId, processInstanceId) || processAPI.isManagerOfUserInvolvedInProcessInstance(currentUserId, processInstanceId)
        } catch (BonitaException e) {
            return true
        }
    }
}
