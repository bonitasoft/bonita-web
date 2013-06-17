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
package org.bonitasoft.console.server.datastore.profile;

import org.bonitasoft.console.client.model.portal.profile.ProfileItem;
import org.bonitasoft.console.server.datastore.converter.ItemConverter;
import org.bonitasoft.engine.profile.Profile;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileItemConverter extends ItemConverter<ProfileItem, Profile> {

    @Override
    public ProfileItem convert(Profile profile) {
        final ProfileItem item = new ProfileItem();
        item.setId(profile.getId());
        item.setName(profile.getName());
        item.setDescription(profile.getDescription());
        item.setIcon(profile.getIconPath());
        return item;
    }

}
