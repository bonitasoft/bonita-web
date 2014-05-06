/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.bpm.process;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Yongtao Guo
 * 
 */
public class ProcessActorPrivilegeItem extends Item {

    public ProcessActorPrivilegeItem() {
        super();
    }

    public ProcessActorPrivilegeItem(final IItem item) {
        super(item);
    }

    public static final String ATTRIBUTE_ID = "actor_privilege_id";

    public static final String ATTRIBUTE_PRIVILEGE_ID = "privilege_id";

    public static final String ATTRIBUTE_ACTOR_ID = "actor_id";

    public static final String ATTRIBUTE_PRIVILEGE_NAME = "privilege_name";

    public static final String ATTRIBUTE_NAME = "actor_privilege_level";

    public ProcessActorPrivilegeItem(final String id, final String privilegeId, final String actorId, final String privilegeName,
            final String actorPrivilegeValue) {
        this.setAttribute(ATTRIBUTE_ID, id);
        this.setAttribute(ATTRIBUTE_PRIVILEGE_ID, privilegeId);
        this.setAttribute(ATTRIBUTE_ACTOR_ID, actorId);
        this.setAttribute(ATTRIBUTE_PRIVILEGE_NAME, privilegeName);
        this.setAttribute(ATTRIBUTE_NAME, actorPrivilegeValue);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new ProcessActorPrivilegeDefinition();
    }
}
