/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
 */
package org.bonitasoft.web.rest.server.datastore.organization;

import static org.bonitasoft.web.toolkit.client.common.util.StringUtil.isBlank;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.IconDescriptor;
import org.bonitasoft.engine.identity.GroupCreator;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.engineclient.GroupEngineClient;

/**
 * @author Colin PUY
 */
public class GroupCreatorConverter {

    private GroupEngineClient groupEngineClient;

    public GroupCreatorConverter(GroupEngineClient groupEngineClient) {
        this.groupEngineClient = groupEngineClient;
    }

    public GroupCreator convert(GroupItem item, long tenantId) {
        if (item == null) {
            return null;
        }

        GroupCreator builder = new GroupCreator(item.getName());

        if (!isBlank(item.getDescription())) {
            builder.setDescription(item.getDescription());
        }

        if (!isBlank(item.getDisplayName())) {
            builder.setDisplayName(item.getDisplayName());
        }

        if (!isBlank(item.getIcon())) {
            IconDescriptor iconDescriptor = new BonitaHomeFolderAccessor().getIconFromFileSystem(item.getIcon(), tenantId);
            builder.setIcon(iconDescriptor.getFilename(), iconDescriptor.getContent());
        }

        if (!isBlank(item.getParentGroupId())) {
            String parentGroupPath = groupEngineClient.getPath(item.getParentGroupId());
            builder.setParentPath(parentGroupPath);
        }
        return builder;
    }

}
