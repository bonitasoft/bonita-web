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
package org.bonitasoft.console.client.model.bpm.process;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Yongtao Guo
 * 
 */
public class UserActorPrivilegeItem extends ProcessActorPrivilegeItem {

    public UserActorPrivilegeItem() {
        super();
    }

    public UserActorPrivilegeItem(final IItem item) {
        super(item);
    }

    public UserActorPrivilegeItem(final String id, final String privilegeId, final String actorId, final String privilegeName, final String actorPrivilegeValue) {
        super(id, privilegeId, actorId, privilegeName, actorPrivilegeValue);
    }

    public static final String USER_ID = "userId";

    public static final String PROCESS_INSTANCE_ID = "caseId";

    public UserActorPrivilegeItem(final String id, final String privilegeId, final String actorId, final String privilegeName,
            final String actorPrivilegeValue,
            final String userId, final String caseId) {
        super(userId, privilegeId, actorId, privilegeName, actorPrivilegeValue);
        this.setAttribute(USER_ID, userId);
        this.setAttribute(PROCESS_INSTANCE_ID, caseId);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new UserActorPrivilegeDefinition();
    }
}
