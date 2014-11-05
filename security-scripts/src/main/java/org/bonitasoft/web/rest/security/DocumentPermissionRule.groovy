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

    @Override
    public boolean check(APISession apiSession, APICallContext apiCallContext, APIAccessor apiAccessor, Logger logger) {
        long currentUserId = apiSession.getUserId();
        if (apiCallContext.isGET()) {
            return checkGetMethod(apiCallContext, apiAccessor, currentUserId)
        } else if (apiCallContext.isPOST()) {
            return checkPostMethod(apiCallContext, apiAccessor, currentUserId, logger)
        }
        return false
    }

    private boolean checkPostMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId, Logger logger) {
        def body = apiCallContext.getBodyAsJSON()
        def processInstanceId = body.optLong(PROCESS_INSTANCE_ID)
        if (processInstanceId <= 0) {
            return false;
        }
        def processAPI = apiAccessor.getProcessAPI()
        return processAPI.isInvolvedInProcessInstance(currentUserId, processInstanceId)
    }

    private boolean checkGetMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId) {
        def filters = apiCallContext.getFilters()
        def processAPI = apiAccessor.getProcessAPI()
        if(filters.containsKey(PROCESS_INSTANCE_ID)){
            return processAPI.isInvolvedInProcessInstance(currentUserId, Long.valueOf(filters.get(PROCESS_INSTANCE_ID)))
        }
        //TODO author id + when resource id is here get the document to check if you are involved in the process
        return false;
    }
}
