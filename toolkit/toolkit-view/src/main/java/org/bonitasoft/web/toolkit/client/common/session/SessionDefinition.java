/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.common.session;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * User definition
 * 
 * @author Julien Mege
 */
public class SessionDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final SessionDefinition get() {
        return (SessionDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "session";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/system/session";

    private static final String PLURAL_RESOURCES_URL = "../API/system/session";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(SessionItem.ATTRIBUTE_USERID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(SessionItem.ATTRIBUTE_FIRSTNAME, ItemAttribute.TYPE.STRING);
        createAttribute(SessionItem.ATTRIBUTE_LASTNAME, ItemAttribute.TYPE.STRING);
        createAttribute(SessionItem.ATTRIBUTE_ICON, ItemAttribute.TYPE.IMAGE);
        createAttribute(SessionItem.ATTRIBUTE_USERID, ItemAttribute.TYPE.STRING);
        createAttribute(SessionItem.ATTRIBUTE_USERNAME, ItemAttribute.TYPE.STRING);
        createAttribute(SessionItem.ATTRIBUTE_CONF, ItemAttribute.TYPE.STRING);
    }
    
//    @Override
//    protected void defineDeploys() {
//        declareDeployable(SessionItem.ATTRIBUTE_CONF, new SHA1Definition());
//    }

    @Override
    public IItem _createItem() {
        return new SessionItem();
    }

    @Override
    public APICaller<SessionItem> getAPICaller() {
        return new APICaller<SessionItem>(this);
    }

}
