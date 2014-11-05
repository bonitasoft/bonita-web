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
 * Let the user access and modify only himself
 *
 * can be added to
 * <ul>
 *     <li>identity/user</li>
 *     <li>identity/professionalcontactdata</li>
 *     <li>identity/personalcontactdata</li>
 * </ul>
 *
 * @author Baptiste Mesta
 */
class UserPermissionRule implements PermissionRule {


    @Override
    boolean check(APISession apiSession, APICallContext apiCallContext, APIAccessor apiAccessor, Logger logger) {
        APISession session = apiSession;
        long currentUserId = session.getUserId();
        if (apiCallContext.getResourceId() != null) {
            def resourceId = Long.valueOf(apiCallContext.getResourceId())
            if (resourceId.equals(currentUserId)) {
                return true
            }
        }
        //search by task id for the do for
        def filters = apiCallContext.getFilters()
        if(apiCallContext.isGET() && filters.containsKey("task_id")){
            def taskId = Long.valueOf(filters.get("task_id"))
            def processAPI = apiAccessor.getProcessAPI()
            def flowNodeInstance = processAPI.getFlowNodeInstance(taskId)
            return processAPI.isUserProcessSupervisor(flowNodeInstance.getProcessDefinitionId(),currentUserId)
        }
        return false;
    }
}
