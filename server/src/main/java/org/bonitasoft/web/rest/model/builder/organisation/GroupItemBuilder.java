/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.builder.organisation;

import org.bonitasoft.web.rest.model.identity.GroupItem;


/**
 * @author Colin PUY
 *
 */
public class GroupItemBuilder {

    private final String name = "Group1";
    private final String displayName = "Group";
    private final String description = "Text for describe the group";
    private final String parentPath = "";
    private final String parentGroupId = "";

    public static GroupItemBuilder aGroup() {
        return new GroupItemBuilder();
    }
    
    public GroupItem build() {
        GroupItem group = new GroupItem();
        group.setName(name);
        group.setDisplayName(displayName);
        group.setDescription(description);
        group.setParentPath(parentPath);
        group.setParentGroupId(parentGroupId);
        return group;
    }
}
