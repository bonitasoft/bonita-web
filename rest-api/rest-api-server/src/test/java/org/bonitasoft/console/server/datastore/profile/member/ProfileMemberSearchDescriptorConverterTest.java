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

import static junit.framework.Assert.assertTrue;

import org.bonitasoft.engine.profile.ProfileMemberSearchDescriptor;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileMemberItem;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileMemberSearchDescriptorConverterTest {

    @Test
    public void testWeCanConvertProfileMemberId() {
        assertTrue(testConversion(ProfileMemberSearchDescriptor.ID,
                ProfileMemberItem.ATTRIBUTE_ID));
    }

    @Test
    public void testWeCanConvertProfileId() {
        assertTrue(testConversion(ProfileMemberSearchDescriptor.PROFILE_ID,
                ProfileMemberItem.ATTRIBUTE_PROFILE_ID));
    }

    @Test
    public void testWeCanConvertUserId() {
        assertTrue(testConversion(ProfileMemberSearchDescriptor.USER_ID,
                ProfileMemberItem.ATTRIBUTE_USER_ID));
    }

    @Test
    public void testWeCanConvertGroupId() {
        assertTrue(testConversion(ProfileMemberSearchDescriptor.GROUP_ID,
                ProfileMemberItem.ATTRIBUTE_GROUP_ID));
    }

    @Test
    public void testWeCanConvertRoleId() {
        assertTrue(testConversion(ProfileMemberSearchDescriptor.ROLE_ID,
                ProfileMemberItem.ATTRIBUTE_ROLE_ID));
    }

    public boolean testConversion(String expected, String actual) {
        ProfileMemberSearchDescriptorConverter converter = new ProfileMemberSearchDescriptorConverter();

        String descriptor = converter.convert(actual);

        return expected.equals(descriptor);
    }
}
