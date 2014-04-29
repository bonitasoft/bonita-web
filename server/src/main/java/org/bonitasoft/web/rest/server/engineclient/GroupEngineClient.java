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

import org.bonitasoft.engine.api.GroupAPI;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.engine.identity.GroupCreator;
import org.bonitasoft.engine.identity.GroupCreator.GroupField;
import org.bonitasoft.engine.identity.GroupNotFoundException;
import org.bonitasoft.engine.identity.GroupUpdater;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

import java.util.List;

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
            throw new APIException(new _("Unable to find group %groupId%", new Arg("groupId", groupId)));
        }
    }
    
    public String getPath(String groupId) {
        try {
            return groupAPI.getGroup(parseId(groupId)).getPath();
        } catch (GroupNotFoundException e) {
            throw new APIException(new _("Unable to get group path, group not found"));
        }
    }
    
    private long parseId(String groupId) {
        try {
            return Long.parseLong(groupId);
        } catch (NumberFormatException e) {
            throw new APIException("Illegal argument, groupId must be a number");
        }
    }
    
    public void delete(List<Long> groupIds) {
        try {
            groupAPI.deleteGroups(groupIds);
        } catch (DeletionException e) {
            throw new APIException(new _("Error when deleting groups"), e);
        }
    }
    
    public Group update(long groupId, GroupUpdater groupUpdater) {
        try {
            return groupAPI.updateGroup(groupId, groupUpdater);
        } catch (GroupNotFoundException e) {
            throw new APIException(new _("Can't update group. Group not found"));
        } catch (UpdateException e) {
            throw new APIException(new _("Error when updating group"), e);
        }
    }
    
    public Group create(GroupCreator groupCreator) {
        try {
            return groupAPI.createGroup(groupCreator);
        } catch (AlreadyExistsException e) {
            throw new APIForbiddenException(new _(
                    "Can't create group. Group '%groupName%' already exists",
                    new Arg("groupName", groupCreator.getFields().get(GroupField.NAME))));
        } catch (CreationException e) {
            throw new APIException(new _("Error when creating group"), e);
        }
    }
}
