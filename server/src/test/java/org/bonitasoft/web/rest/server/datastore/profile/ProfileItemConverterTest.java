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

import static junit.framework.Assert.assertTrue;
import static org.bonitasoft.web.rest.model.builder.profile.EngineProfileBuilder.anEngineProfile;
import static org.bonitasoft.web.rest.model.builder.profile.ProfileItemBuilder.aProfileItem;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileItemConverterTest extends APITestWithMock {

    @Test
    public void testProfileItemConvertion() {
        final Profile profile = anEngineProfile().withName("aName").withDescription("aDescription").build();

        final ProfileItem item = new ProfileItemConverter().convert(profile);

        assertTrue(areEquals(aProfileItem().fromEngineItem(profile).withIcon(ProfileItemConverter.DEFAULT_PROFILE_ICONPATH).build(), item));
    }

}
