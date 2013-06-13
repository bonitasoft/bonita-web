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

import static org.bonitasoft.console.client.model.bpm.process.ActorMemberItem.ATTRIBUTE_ACTOR_ID;

import org.bonitasoft.console.client.model.portal.profile.AbstractMemberDefinition;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Haojie Yuan
 * @author Séverin Moussel
 * 
 */
public class ActorMemberDefinition extends AbstractMemberDefinition {

    /**
     * Singleton
     */
    public static final ActorMemberDefinition get() {
        return (ActorMemberDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "actormember";

    /**
     * the URL of users resource
     */
    private static final String API_URL = "../API/bpm/actorMember";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {

        // FIXME : Remove after engine has removed the unique ID.
        setPrimaryKeys(ActorMemberItem.ATTRIBUTE_ID);

        // FIXME : Uncomment after engine has removed the unique ID.
        // setPrimaryKeys(
        // ATTRIBUTE_ACTOR_ID,
        // ATTRIBUTE_USER_ID,
        // ATTRIBUTE_ROLE_ID,
        // ATTRIBUTE_GROUP_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ActorMemberItem.ATTRIBUTE_ACTOR_ID, ItemAttribute.TYPE.ITEM_ID);
        super.defineAttributes();
    }

    @Override
    protected void defineDeploys() {
        declareDeployable(ATTRIBUTE_ACTOR_ID, ActorDefinition.get());
        super.defineDeploys();
    }

    @Override
    public IItem _createItem() {
        return new ActorMemberItem();
    }

    @Override
    public APICaller<ActorMemberItem> getAPICaller() {
        return new APICaller<ActorMemberItem>(this);
    }

}
