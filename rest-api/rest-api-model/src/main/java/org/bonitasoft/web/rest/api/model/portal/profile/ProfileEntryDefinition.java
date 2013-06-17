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

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.EnumValidator;

/**
 * @author Julien Mege
 * @author Séverin Moussel
 */
public class ProfileEntryDefinition extends ItemDefinition<ProfileEntryItem> {

    /**
     * Singleton
     */
    public static final ProfileEntryDefinition get() {
        return (ProfileEntryDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "profileentry";

    /**
     * the URL of profile entries resource
     */
    protected static final String API_URL = "../API/userXP/profileEntry";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ProfileEntryItem.ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ProfileEntryItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ProfileEntryItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(ProfileEntryItem.ATTRIBUTE_CUSTOM_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(ProfileEntryItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(ProfileEntryItem.ATTRIBUTE_INDEX, ItemAttribute.TYPE.INTEGER)
                .isMandatory();
        createAttribute(ProfileEntryItem.ATTRIBUTE_PARENT_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ProfileEntryItem.ATTRIBUTE_PROFILE_ID, ItemAttribute.TYPE.ITEM_ID)
                .isMandatory();
        createAttribute(ProfileEntryItem.ATTRIBUTE_TYPE, ItemAttribute.TYPE.ENUM)
                .isMandatory()
                .addValidator(new EnumValidator(
                        ProfileEntryItem.VALUE_TYPE.folder.name(),
                        ProfileEntryItem.VALUE_TYPE.link.name()
                        ));
        createAttribute(ProfileEntryItem.ATTRIBUTE_PAGE, ItemAttribute.TYPE.STRING);
    }

    @Override
    public ProfileEntryItem _createItem() {
        return new ProfileEntryItem();
    }

    @Override
    public APICaller<ProfileEntryItem> getAPICaller() {
        return new APICaller<ProfileEntryItem>(this);
    }

}
