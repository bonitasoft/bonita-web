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
package org.bonitasoft.web.rest.server.api.userXP.profile;

import static org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem.ATTRIBUTE_CUSTOM_NAME;
import static org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem.ATTRIBUTE_NAME;
import static org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem.ATTRIBUTE_PAGE;
import static org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem.ATTRIBUTE_TYPE;
import static org.bonitasoft.web.rest.server.model.builder.profile.entry.ProfileEntryItemBuilder.aProfileEntryItem;
import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem.VALUE_TYPE;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.api.userXP.profile.APIProfileEntry;
import org.bonitasoft.web.rest.server.datastore.ComposedDatastore;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIAttributeMissingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

/**
 * @author Colin PUY
 * 
 */
public class APIProfileEntryTest extends APITestWithMock {

    @Mock
    private ComposedDatastore<ProfileEntryItem> profileEntryDatastore;

    @Spy
    private APIProfileEntry apiProfileEntry;

    @Before
    public void initializeMocks() {
        initMocks(this);

        doReturn(profileEntryDatastore).when(apiProfileEntry).defineDefaultDatastore();
    }

    @Test(expected = APIAttributeMissingException.class)
    public void checkDatasThrowExceptionIfNoNameIsSetInAttributes() throws Exception {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProfileEntryItem.ATTRIBUTE_TYPE, VALUE_TYPE.link.name());
        attributes.put(ATTRIBUTE_PAGE, "aPage");

        apiProfileEntry.checkDatas(new HashMap<String, String>());
    }

    @Test(expected = APIAttributeMissingException.class)
    public void checkDatasThrowExceptionIfTypeIsLinkAndAttributePageIsNotSet() throws Exception {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ATTRIBUTE_NAME, "aName");
        attributes.put(ATTRIBUTE_TYPE, VALUE_TYPE.link.name());

        apiProfileEntry.checkDatas(new HashMap<String, String>());
    }

    @Test(expected = APIAttributeMissingException.class)
    public void checkDatasDontThrowExceptionIfConditionAreReached() throws Exception {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ATTRIBUTE_CUSTOM_NAME, "aCustomName");
        attributes.put(ATTRIBUTE_TYPE, VALUE_TYPE.link.name());
        attributes.put(ATTRIBUTE_PAGE, "aPage");

        apiProfileEntry.checkDatas(new HashMap<String, String>());
    }

    @Test(expected = APIAttributeMissingException.class)
    public void weCheckDataBeforeAdding() throws Exception {
        apiProfileEntry.add(aProfileEntryItem().withName("").withCustomName("").build());
    }

    @Test(expected = APIAttributeMissingException.class)
    public void weCheckDataBeforeUpdating() throws Exception {
        apiProfileEntry.update(makeAPIID(1L), new HashMap<String, String>());
    }
}
