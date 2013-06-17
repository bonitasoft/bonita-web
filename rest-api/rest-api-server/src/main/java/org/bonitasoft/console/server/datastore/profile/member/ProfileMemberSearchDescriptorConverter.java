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
package org.bonitasoft.console.server.datastore.profile.member;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.server.datastore.converter.AttributeConverter;
import org.bonitasoft.engine.profile.ProfileMemberSearchDescriptor;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileMemberItem;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileMemberSearchDescriptorConverter implements AttributeConverter {

    private static Map<String, String> mapping = new HashMap<String, String>();

    static {
        mapping.put(ProfileMemberItem.ATTRIBUTE_ID, ProfileMemberSearchDescriptor.ID);
        mapping.put(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, ProfileMemberSearchDescriptor.PROFILE_ID);
        mapping.put(ProfileMemberItem.ATTRIBUTE_USER_ID, ProfileMemberSearchDescriptor.USER_ID);
        mapping.put(ProfileMemberItem.ATTRIBUTE_ROLE_ID, ProfileMemberSearchDescriptor.ROLE_ID);
        mapping.put(ProfileMemberItem.ATTRIBUTE_GROUP_ID, ProfileMemberSearchDescriptor.GROUP_ID);
        mapping.put(ProfileMemberItem.FILTER_MEMBER_TYPE, "");
    }

    @Override
    public String convert(String attribute) {
        String descriptor = mapping.get(attribute);
        if (descriptor == null) {
            throw new RuntimeException(attribute + " has no valid search descriptor");
        }
        return descriptor;
    }

}
