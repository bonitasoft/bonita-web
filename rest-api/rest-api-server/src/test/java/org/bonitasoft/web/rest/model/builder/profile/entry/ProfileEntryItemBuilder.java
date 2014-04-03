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
package org.bonitasoft.web.rest.model.builder.profile.entry;

import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem.VALUE_TYPE;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileEntryItemBuilder extends AbstractProfileEntryBuilder<ProfileEntryItem> {

    public ProfileEntryItemBuilder() {
        // empty
    }

    public static ProfileEntryItemBuilder aProfileEntryItem() {
        return new ProfileEntryItemBuilder();
    }

    public ProfileEntryItem build() {
        ProfileEntryItem item = new ProfileEntryItem();
        item.setId(id);
        item.setName(name);
        // item.setCustomName(custormName);
        item.setProfileId(profileId);
        item.setIndex(index);
        item.setType(type);
        item.setDescription(description);
        item.setParentId(parentId);
        item.setPage(page);
        item.setIsCustom(false);
        return item;
    }

    public ProfileEntryItemBuilder from(ProfileEntry profileEntry) {
        id = profileEntry.getId();
        name = profileEntry.getName();
        // custormName = profileEntry.getName();
        profileId = profileEntry.getProfileId();
        index = profileEntry.getIndex();
        type = VALUE_TYPE.valueOf(profileEntry.getType());
        description = profileEntry.getDescription();
        parentId = profileEntry.getParentId();
        page = profileEntry.getPage();
        return this;
    }

}
