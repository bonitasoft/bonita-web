/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.engineclient;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.engine.api.GroupAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.engine.identity.GroupNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

/**
 * @author Paul AMAR
 * 
 */
public class GroupEngineClient {

    private ProcessAPI processAPI;

    protected GroupEngineClient(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    public Group get(Long groupId) {
        try {
            return ((GroupAPI) processAPI).getGroup(groupId);
        } catch (GroupNotFoundException e) {
            throw new APIException(I18n._("Group not found"));
        }
    }
}
