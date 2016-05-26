/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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

import java.util.Map;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.IconDescriptor;
import org.bonitasoft.engine.identity.UserUpdater;
import org.bonitasoft.web.rest.model.identity.UserItem;

public class UserUpdaterConverter {

    public UserUpdater convert(Map<String, String> attributes, long tenantId) {
        UserUpdater userUpdater = new UserUpdater();

        if (attributes.containsKey(UserItem.ATTRIBUTE_FIRSTNAME)) {
            userUpdater.setFirstName(attributes.get(UserItem.ATTRIBUTE_FIRSTNAME));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_LASTNAME)) {
            userUpdater.setLastName(attributes.get(UserItem.ATTRIBUTE_LASTNAME));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_PASSWORD)) {
            userUpdater.setPassword(attributes.get(UserItem.ATTRIBUTE_PASSWORD));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_USERNAME)) {
            userUpdater.setUserName(attributes.get(UserItem.ATTRIBUTE_USERNAME));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_MANAGER_ID)) {
            Long managerId = getManagerId(attributes);
            userUpdater.setManagerId(managerId);
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_ICON)) {
            IconDescriptor iconDescriptor = new BonitaHomeFolderAccessor().getIconFromFileSystem(attributes.get(UserItem.ATTRIBUTE_ICON), tenantId);
            userUpdater.setIcon(iconDescriptor.getFilename(), iconDescriptor.getContent());
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_TITLE)) {
            userUpdater.setTitle(attributes.get(UserItem.ATTRIBUTE_TITLE));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_JOB_TITLE)) {
            userUpdater.setJobTitle(attributes.get(UserItem.ATTRIBUTE_JOB_TITLE));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_ENABLED)) {
            userUpdater.setEnabled("true".equals(attributes.get(UserItem.ATTRIBUTE_ENABLED)));
        }
        return userUpdater;
    }

    private Long getManagerId(final Map<String, String> attributes) {
        try {
            return Long.valueOf(attributes.get(UserItem.ATTRIBUTE_MANAGER_ID));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
