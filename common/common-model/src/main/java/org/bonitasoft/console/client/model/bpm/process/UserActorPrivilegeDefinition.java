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

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Yongtao Guo
 * 
 */
public class UserActorPrivilegeDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final UserActorPrivilegeDefinition get() {
        return (UserActorPrivilegeDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "userActorRight";

    /**
     * the URL of users resource
     */
    private static final String API_URL = "../REST/actorPrivilegeAPI/userActorPrivilege";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ProcessActorPrivilegeItem.ATTRIBUTE_ID, ItemAttribute.TYPE.STRING);
        createAttribute(ProcessActorPrivilegeItem.ATTRIBUTE_ACTOR_ID, ItemAttribute.TYPE.STRING);
        createAttribute(ProcessActorPrivilegeItem.ATTRIBUTE_PRIVILEGE_ID, ItemAttribute.TYPE.STRING);
        createAttribute(ProcessActorPrivilegeItem.ATTRIBUTE_PRIVILEGE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(ProcessActorPrivilegeItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(UserActorPrivilegeItem.USER_ID, ItemAttribute.TYPE.STRING);
        createAttribute(UserActorPrivilegeItem.PROCESS_INSTANCE_ID, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ProcessActorPrivilegeItem.ATTRIBUTE_ID);
    }

    @Override
    protected IItem _createItem() {
        return new UserActorPrivilegeItem();
    }

    @Override
    public APICaller<UserActorPrivilegeItem> getAPICaller() {
        return new APICaller<UserActorPrivilegeItem>(this);
    }

}
