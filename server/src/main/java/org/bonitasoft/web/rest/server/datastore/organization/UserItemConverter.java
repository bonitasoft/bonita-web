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

import org.bonitasoft.engine.identity.User;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;
import org.bonitasoft.web.toolkit.client.data.APIID;

public class UserItemConverter extends ItemConverter<UserItem, User> {

    @Override
    public UserItem convert(User user) {
        if (user == null) {
            return null;
        }

        final UserItem result = new UserItem();
        result.setId(APIID.makeAPIID(user.getId()));
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setPassword(user.getPassword());
        result.setUserName(user.getUserName());
        result.setManagerId(user.getManagerUserId());
        result.setEnabled(user.isEnabled());

        // Add default icon if icon if empty
        final String iconPath = user.getIconPath();
        result.setIcon(iconPath == null || iconPath.isEmpty() ? UserItem.DEFAULT_USER_ICON : iconPath);

        result.setCreationDate(user.getCreationDate());
        result.setCreatedByUserId(user.getCreatedBy());
        result.setLastUpdateDate(user.getLastUpdate());
        result.setLastConnectionDate(user.getLastConnection());
        result.setTitle(user.getTitle());
        result.setJobTitle(user.getJobTitle());


        return result;
    }

}
