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






package org.bonitasoft.web.rest.security

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
import org.bonitasoft.engine.session.APISession

/**
 *
 * Let a user see process configuration only if he is process owner
 *
 * <ul>
 *     <li>bpm/connectorInstance</li>
 * </ul>
 *
 *
 *
 * @author Anthony Birembaut
 */
class ConnectorInstancePermissionRule implements PermissionRule {

    public static final String CONTAINER_ID = "containerId"

    @Override
    public boolean check(APISession apiSession, APICallContext apiCallContext, APIAccessor apiAccessor, Logger logger) {
        long currentUserId = apiSession.getUserId()
        if (apiCallContext.isGET()) {
            return checkGetMethod(apiCallContext, apiAccessor, currentUserId, logger)
        } else if (apiCallContext.isPUT()) {
            //TODO unable to find a connector instance with the API!
            return false
        }
        return true
    }

    private boolean checkGetMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId, Logger logger) {
        def filters = apiCallContext.getFilters()
        if(filters.containsKey(CONTAINER_ID)){
            def processAPI = apiAccessor.getProcessAPI()
            def flowNodeInstance = processAPI.getFlowNodeInstance(Long.valueOf(filters.get(CONTAINER_ID)))
            def processID = flowNodeInstance.getProcessDefinitionId()
            return processAPI.isUserProcessSupervisor(processID,currentUserId)
        }
        return false
    }
}
