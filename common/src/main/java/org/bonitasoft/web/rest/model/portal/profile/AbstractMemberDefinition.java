/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.portal.profile;

import static org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem.ATTRIBUTE_GROUP_ID;
import static org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem.ATTRIBUTE_ROLE_ID;
import static org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem.ATTRIBUTE_USER_ID;
import static org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute.TYPE.ITEM_ID;

import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 */
public abstract class AbstractMemberDefinition<E extends IItem> extends ItemDefinition<E> {

    @Override
    protected void defineAttributes() {
        createAttribute(ATTRIBUTE_GROUP_ID, ITEM_ID);
        createAttribute(ATTRIBUTE_USER_ID, ITEM_ID);
        createAttribute(ATTRIBUTE_ROLE_ID, ITEM_ID);
    }

    @Override
    protected void defineDeploys() {
        declareDeployable(ATTRIBUTE_USER_ID, UserDefinition.get());
        declareDeployable(ATTRIBUTE_GROUP_ID, UserDefinition.get());
        declareDeployable(ATTRIBUTE_ROLE_ID, RoleDefinition.get());
    }

}
