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
package org.bonitasoft.console.server.datastore.profile.entry;

import static junit.framework.Assert.assertTrue;
import static org.bonitasoft.console.server.model.builder.profile.entry.EngineProfileEntryBuilder.anEngineProfileEntry;
import static org.bonitasoft.console.server.model.builder.profile.entry.ProfileEntryItemBuilder.aProfileEntryItem;

import org.bonitasoft.console.client.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.console.server.APITestWithMock;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileEntryItemConverterTest extends APITestWithMock {

    @Test
    public void testWeCanConverteAProfileEntry() throws Exception {
        ProfileEntry profileEntry = anEngineProfileEntry().withCustomName("customName").build();

        ProfileEntryItem item = new ProfileEntryItemConverter().convert(profileEntry);

        assertTrue(areEquals(aProfileEntryItem().from(profileEntry).build(), item));
    }
}
