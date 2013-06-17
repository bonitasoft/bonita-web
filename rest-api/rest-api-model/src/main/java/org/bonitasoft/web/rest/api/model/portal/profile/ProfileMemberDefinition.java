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

import static org.bonitasoft.web.rest.api.model.portal.profile.ProfileMemberItem.ATTRIBUTE_PROFILE_ID;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Julien Mege
 * @author Séverin Moussel
 */
public class ProfileMemberDefinition extends AbstractMemberDefinition<ProfileMemberItem> {

    /**
     * Singleton
     */
    public static final ProfileMemberDefinition get() {
        return (ProfileMemberDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "memberProfile";

    /**
     * the URL of UserProfileAssociation resource
     */
    protected static final String API_URL = "../API/userXP/profileMember";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        // FIXME : delete when id in engine is deleted
        setPrimaryKeys(ProfileMemberItem.ATTRIBUTE_ID);

        // FIXME : uncomment when id in engine is deleted
        // setPrimaryKeys(
        // ATTRIBUTE_PROFILE_ID,
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
        createAttribute(ATTRIBUTE_PROFILE_ID, ItemAttribute.TYPE.ITEM_ID).isMandatory();
        super.defineAttributes();
    }

    @Override
    protected void defineDeploys() {
        declareDeployable(ATTRIBUTE_PROFILE_ID, ProfileDefinition.get());
        super.defineDeploys();
    }

    @Override
    public ProfileMemberItem _createItem() {
        return new ProfileMemberItem();
    }

    @Override
    public APICaller<ProfileMemberItem> getAPICaller() {
        return new APICaller<ProfileMemberItem>(this);
    }

}
