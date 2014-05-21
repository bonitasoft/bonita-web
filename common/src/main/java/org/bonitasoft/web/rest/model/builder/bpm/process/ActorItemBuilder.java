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
package org.bonitasoft.web.rest.model.builder.bpm.process;

import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.web.rest.model.bpm.process.ActorItem;

/**
 * @author Colin PUY
 * 
 */
public class ActorItemBuilder {

    private Long id;
    private Long processId;
    private String name;
    private String displayName;
    private String description;

    private ActorItemBuilder() {
    }

    public static ActorItemBuilder anActorItem() {
        return new ActorItemBuilder();
    }

    public ActorItem build() {
        ActorItem actorItem = new ActorItem();
        actorItem.setId(id);
        actorItem.setProcessId(processId);
        actorItem.setName(name);
        actorItem.setDisplayName(displayName);
        actorItem.setDescription(description);
        return actorItem;
    }
    
    public ActorItemBuilder fromActorInstance(ActorInstance engineItem) {
        id = engineItem.getId();
        processId = engineItem.getProcessDefinitionId();
        name = engineItem.getName();
        displayName = engineItem.getDisplayName();
        description = engineItem.getDescription();
        return this;
    }
}
