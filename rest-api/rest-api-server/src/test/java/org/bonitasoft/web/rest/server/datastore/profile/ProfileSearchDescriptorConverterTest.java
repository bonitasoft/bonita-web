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

import static junit.framework.Assert.assertEquals;

import org.bonitasoft.engine.profile.ProfileSearchDescriptor;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.datastore.profile.ProfileSearchDescriptorConverter;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileSearchDescriptorConverterTest {

    @Test
    public void testWeCanConvertTheProfileId() throws Exception {
        String descriptor = new ProfileSearchDescriptorConverter().convert(ProfileItem.ATTRIBUTE_ID);

        assertEquals(ProfileSearchDescriptor.ID, descriptor);
    }

    @Test
    public void testWeCanConvertTheProfileName() throws Exception {
        String descriptor = new ProfileSearchDescriptorConverter().convert(ProfileItem.ATTRIBUTE_NAME);

        assertEquals(ProfileSearchDescriptor.NAME, descriptor);
    }

    @Test(expected = RuntimeException.class)
    public void testTryingToConvertUnexistingDescriptorThrowAnException() {
        new ProfileSearchDescriptorConverter().convert("someUnsupportedAttribute");
    }
}
