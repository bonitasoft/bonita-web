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
package org.bonitasoft.web.rest.server.datastore.profile.entry;

import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;
import org.bonitasoft.web.toolkit.server.utils.I18n;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileEntryItemConverter extends ItemConverter<ProfileEntryItem, ProfileEntry> {

    @Override
    public ProfileEntryItem convert(ProfileEntry profileEntry) {
        ProfileEntryItem item = new ProfileEntryItem();
        item.setId(profileEntry.getId());
        setName(item, profileEntry);
        item.setProfileId(profileEntry.getProfileId());
        item.setIndex(profileEntry.getIndex());
        item.setType(profileEntry.getType());
        item.setDescription(profileEntry.getDescription());
        item.setParentId(profileEntry.getParentId());
        item.setPage(profileEntry.getPage());
        return item;
    }

    private void setName(final ProfileEntryItem result, ProfileEntry profile) {
        String entryName = profile.getName();
        result.setName(entryName);
        if (I18n.isSet(entryName)) {
            result.setCustomName(entryName);
        }
    }

}
