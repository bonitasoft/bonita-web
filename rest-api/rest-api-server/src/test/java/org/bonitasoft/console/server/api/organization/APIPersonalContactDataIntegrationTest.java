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
package org.bonitasoft.console.server.api.organization;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.api.model.identity.PersonalContactDataItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

/**
 * @author Paul AMAR
 */
public class APIPersonalContactDataIntegrationTest extends AbstractConsoleTest {

    private APIPersonalContactData apiPersonalContactData;

    @Override
    public void consoleTestSetUp() throws Exception {
        this.apiPersonalContactData = new APIPersonalContactData();
        this.apiPersonalContactData.setCaller(getAPICaller(TestUserFactory.getRidleyScott().getSession(),
                "API/identity/personalcontactdata"));
    }

    @Test
    public void getPersonalContactData() {
        final PersonalContactDataItem result = this.apiPersonalContactData.get(APIID.makeAPIID(TestUserFactory.getRidleyScott().getId()));
        assertEquals(result.getAttributes().size(), 13);
    }

    @Test
    public void updatePersonalContactData() {

        final Map<String, String> attributes = new HashMap<String, String>();

        // Set all the fields.
        attributes.put(PersonalContactDataItem.ATTRIBUTE_EMAIL, "email");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_PHONE, "phone");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_MOBILE, "mobile");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_FAX, "fax");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_BUILDING, "building");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_ROOM, "room");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_ADDRESS, "address");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_ZIPCODE, "zipcode");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_CITY, "city");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_STATE, "state");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_COUNTRY, "country");
        attributes.put(PersonalContactDataItem.ATTRIBUTE_WEBSITE, "website");

        this.apiPersonalContactData.update(APIID.makeAPIID(TestUserFactory.getRidleyScott().getId()), attributes);

        final PersonalContactDataItem result = this.apiPersonalContactData.get(APIID.makeAPIID(TestUserFactory.getRidleyScott().getId()));

        assertEquals(result.getBuilding(), "building");
        assertEquals(result.getPhoneNumber(), "phone");
        assertEquals(result.getMobileNumber(), "mobile");
        assertEquals(result.getFaxNumber(), "fax");
        assertEquals(result.getRoom(), "room");
        assertEquals(result.getAddress(), "address");
        assertEquals(result.getZipCode(), "zipcode");
        assertEquals(result.getCountry(), "country");
        assertEquals(result.getState(), "state");
        assertEquals(result.getEmail(), "email");
        assertEquals(result.getWebsite(), "website");

    }

    @Test
    public void addPersonalContactData() {
        final TestUser user = getInitiator().createUser("user", "pwd");
        final PersonalContactDataItem res = new PersonalContactDataItem();
        res.setAddress("New address");
        res.setId(user.getId());
        this.apiPersonalContactData.add(res);

        final PersonalContactDataItem result = this.apiPersonalContactData.get(APIID.makeAPIID(user.getId()));

        assertEquals(result.getCity(), null);
        assertEquals(result.getAddress(), "New address");
        assertEquals(result.getBuilding(), null);
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getRidleyScott();
    }

}
