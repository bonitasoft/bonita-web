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
 * Let the user access only himself
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
        return false;
    }
}
