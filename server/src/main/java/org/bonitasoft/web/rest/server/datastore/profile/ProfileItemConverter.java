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
package org.bonitasoft.web.rest.server.datastore.profile;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileItemConverter extends ItemConverter<ProfileItem, Profile> {

    private static final String USER_PROFILE_NAME = "User";

    private static final String ADMIN_PROFILE_NAME = "Administrator";

    private static final String PROCESSMANAGER_PROFILE_NAME = "Process manager";

    private static final String TEAMMANAGER_PROFILE_NAME = "Team manager";

    private static final String USER_PROFILE_ICONPATH = "/profiles/profileUser.png";

    private static final String ADMIN_PROFILE_ICONPATH = "/profiles/profileAdmin.png";

    private static final String PROCESSMANAGER_PROFILE_ICONPATH = "/profiles/profileProcessManager.png";

    private static final String TEAMMANAGER_PROFILE_ICONPATH = "/profiles/profileTeamManager.png";

    private static final String DEFAULT_PROFILE_ICONPATH = "/profiles/profileDefault.png";

    @Override
    public ProfileItem convert(final Profile profile) {
        final ProfileItem item = new ProfileItem();
        item.setId(profile.getId());
        item.setName(profile.getName());
        item.setDescription(profile.getDescription());
        item.setIsDefault(profile.isDefault());
        item.setIcon(getIconPath(profile.getName()));
        return item;
    }

    protected String getIconPath(final String profileName) {
        if (USER_PROFILE_NAME.equals(profileName)) {
            return USER_PROFILE_ICONPATH;
        } else if (ADMIN_PROFILE_NAME.equals(profileName)) {
            return ADMIN_PROFILE_ICONPATH;
        } else if (PROCESSMANAGER_PROFILE_NAME.equals(profileName)) {
            return PROCESSMANAGER_PROFILE_ICONPATH;
        } else if (TEAMMANAGER_PROFILE_NAME.equals(profileName)) {
            return TEAMMANAGER_PROFILE_ICONPATH;
        } else {
            return DEFAULT_PROFILE_ICONPATH;
        }
    }

}
