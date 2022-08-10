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
package org.bonitasoft.web.rest.model.identity;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * User definition
 * 
 * @author Séverin Moussel
 * 
 */
public class UserDefinition extends ItemDefinition<UserItem> {

    /**
     * Singleton
     */
    public static final UserDefinition get() {
        return (UserDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "user";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/identity/user";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(UserItem.ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(UserItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(UserItem.ATTRIBUTE_FIRSTNAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(UserItem.ATTRIBUTE_LASTNAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(UserItem.ATTRIBUTE_ICON, ItemAttribute.TYPE.IMAGE);
        createAttribute(UserItem.ATTRIBUTE_USERNAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(UserItem.ATTRIBUTE_PASSWORD, ItemAttribute.TYPE.PASSWORD);

        createAttribute(UserItem.ATTRIBUTE_CREATION_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(UserItem.ATTRIBUTE_CREATED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(UserItem.ATTRIBUTE_LAST_UPDATE_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(UserItem.ATTRIBUTE_LAST_CONNECTION_DATE, ItemAttribute.TYPE.DATETIME);

        createAttribute(UserItem.ATTRIBUTE_TITLE, ItemAttribute.TYPE.STRING);
        createAttribute(UserItem.ATTRIBUTE_JOB_TITLE, ItemAttribute.TYPE.STRING);
        createAttribute(UserItem.ATTRIBUTE_MANAGER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(UserItem.ATTRIBUTE_ENABLED, ItemAttribute.TYPE.BOOLEAN);
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.data.item.ItemDefinition#defineDeploys()
     */
    @Override
    protected void defineDeploys() {
        super.defineDeploys();
        declareDeployable(UserItem.DEPLOY_PROFESSIONAL_DATA, Definitions.get(ProfessionalContactDataDefinition.TOKEN));
        declareDeployable(UserItem.DEPLOY_PERSONNAL_DATA, Definitions.get(PersonalContactDataDefinition.TOKEN));
    }

    @Override
    public UserItem _createItem() {
        return new UserItem();
    }
}
