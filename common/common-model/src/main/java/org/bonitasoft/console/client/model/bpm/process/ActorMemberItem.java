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

import org.bonitasoft.console.client.model.portal.profile.AbstractMemberItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Séverin Moussel
 */
public class ActorMemberItem extends AbstractMemberItem implements ItemHasUniqueId {

    public ActorMemberItem() {
        super();
    }

    public ActorMemberItem(final IItem item) {
        super(item);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ATTRIBUTE_ACTOR_ID = "actor_id";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Setters

    @Override
    public void setId(final String id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final Long id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    public void setActorId(final APIID id) {
        setAttribute(ATTRIBUTE_ACTOR_ID, id);
    }

    public void setActorId(final Long id) {
        setAttribute(ATTRIBUTE_ACTOR_ID, id);
    }

    public void setActorId(final String id) {
        setAttribute(ATTRIBUTE_ACTOR_ID, id);
    }

    // Getters

    public APIID getActorId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_ACTOR_ID);
    }

    // Deploys

    public ActorItem getActor() {
        return new ActorItem(getDeploy(ATTRIBUTE_ACTOR_ID));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition getItemDefinition() {
        return ActorMemberDefinition.get();
    }

}
