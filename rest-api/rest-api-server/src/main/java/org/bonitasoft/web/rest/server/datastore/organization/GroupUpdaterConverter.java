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
package org.bonitasoft.web.rest.server.datastore.organization;

import java.util.Map;

import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.engine.identity.GroupUpdater;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.engineclient.GroupEngineClient;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

/**
 * @author Colin PUY
 */
public class GroupUpdaterConverter {

    private GroupEngineClient groupEngineClient;

    public GroupUpdaterConverter(GroupEngineClient groupEngineClient) {
        this.groupEngineClient = groupEngineClient;
    }
    
    public GroupUpdater convert(Map<String, String> attributes) {
        GroupUpdater updater = new GroupUpdater();
        if (attributes.containsKey(GroupItem.ATTRIBUTE_DESCRIPTION)) {
            updater.updateDescription(attributes.get(GroupItem.ATTRIBUTE_DESCRIPTION));
        }
        if (attributes.containsKey(GroupItem.ATTRIBUTE_PARENT_PATH)) {
            updater.updateParentPath(attributes.get(GroupItem.ATTRIBUTE_PARENT_PATH));
        }
        if (!MapUtil.isBlank(attributes, GroupItem.ATTRIBUTE_PARENT_GROUP_ID)) {
            Group group = groupEngineClient.get(Long.parseLong(attributes.get(GroupItem.ATTRIBUTE_PARENT_GROUP_ID)));
            updater.updateParentPath(group.getPath());
        }
        if (attributes.containsKey(GroupItem.ATTRIBUTE_ICON)) {
            updater.updateIconPath(attributes.get(GroupItem.ATTRIBUTE_ICON));
        }
        if (attributes.containsKey(GroupItem.ATTRIBUTE_NAME)) {
            updater.updateName(attributes.get(GroupItem.ATTRIBUTE_NAME));
        }
        return updater;
    }
}
