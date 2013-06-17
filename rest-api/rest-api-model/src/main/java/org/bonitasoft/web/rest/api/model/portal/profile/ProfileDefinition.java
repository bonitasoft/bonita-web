/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.api.model.portal.profile;

import static org.bonitasoft.web.rest.api.model.portal.profile.ProfileItem.ATTRIBUTE_DESCRIPTION;
import static org.bonitasoft.web.rest.api.model.portal.profile.ProfileItem.ATTRIBUTE_NAME;
import static org.bonitasoft.web.toolkit.client.data.item.template.ItemHasIcon.ATTRIBUTE_ICON;
import static org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId.ATTRIBUTE_ID;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Julien Mege
 * @author Séverin Moussel
 */
public class ProfileDefinition extends ItemDefinition<ProfileItem> {

    /**
     * Singleton
     */
    public static final ProfileDefinition get() {
        return (ProfileDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "profile";

    /**
     * the URL of profile resource
     */
    protected static final String API_URL = "../API/userXP/profile";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);

        createAttribute(ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING)
                .isMandatory(true);

        createAttribute(ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);

        createAttribute(ATTRIBUTE_ICON, ItemAttribute.TYPE.IMAGE);
    }

    @Override
    public ProfileItem _createItem() {
        return new ProfileItem();
    }

    @Override
    public APICaller<ProfileItem> getAPICaller() {
        return new APICaller<ProfileItem>(this);
    }

}
