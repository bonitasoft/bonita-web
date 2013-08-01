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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;

import org.bonitasoft.engine.api.GroupAPI;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.engine.identity.GroupNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Paul AMAR
 * 
 */
public class GroupEngineClient {

    private GroupAPI groupAPI;

    protected GroupEngineClient(GroupAPI groupAPI) {
        this.groupAPI = groupAPI;
    }

    public Group get(Long groupId) {
        try {
            return groupAPI.getGroup(groupId);
        } catch (GroupNotFoundException e) {
            throw new APIException(_("Unable to find group %groupId%", new Arg("groupId", groupId)));
        }
    }
    
    public void delete(List<Long> groupIds) {
        try {
            groupAPI.deleteGroups(groupIds);
        } catch (DeletionException e) {
            throw new APIException(_("Error when deleting groups"), e);
        }
    }
}
