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
package org.bonitasoft.web.rest.server.model.builder.bpm.process;

import org.bonitasoft.engine.bpm.actor.ActorMember;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberItem;

/**
 * @author Colin PUY
 * 
 */
public class ActorMemberItemBuilder {

    private Long id;
    private Long actorId;
    private Long userId;
    private Long roleId;
    private Long groupId;

    private ActorMemberItemBuilder() {
    }

    public static ActorMemberItemBuilder anActorMemberItem() {
        return new ActorMemberItemBuilder();
    }

    public ActorMemberItem build() {
        ActorMemberItem item = new ActorMemberItem();
        item.setId(id);
        item.setActorId(actorId);
        item.setUserId(userId);
        item.setRoleId(roleId);
        item.setGroupId(groupId);
        return item;
    }

    public ActorMemberItemBuilder fromActorMember(ActorMember actorMember, long actorId) {
        id = actorMember.getId();
        this.actorId = actorId;
        userId = actorMember.getUserId();
        roleId = actorMember.getRoleId();
        groupId = actorMember.getGroupId();
        return this;
    }
    
    public ActorMemberItemBuilder withActorId(long actorId) {
        this.actorId = actorId;
        return this;
    }
    
    public ActorMemberItemBuilder withuserId(long userId) {
        this.userId = userId;
        return this;
    }
}
