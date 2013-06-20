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
package org.bonitasoft.web.rest.server.api.organization;

import static org.bonitasoft.web.rest.model.builder.identity.ContactDataBuilder.aContactData;
import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.junit.Assert.assertEquals;

import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.builder.identity.ContactDataBuilder;
import org.bonitasoft.web.rest.model.identity.ProfessionalContactDataItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.api.organization.APIProfessionalContactData;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

/**
 * @author Paul AMAR
 */
public class APIProfessionalContactDataIntegrationTest extends AbstractConsoleTest {

    private APIProfessionalContactData apiProfessionalContactData;

    @Override
    public void consoleTestSetUp() throws Exception {
        this.apiProfessionalContactData = new APIProfessionalContactData();
        this.apiProfessionalContactData.setCaller(getAPICaller(TestUserFactory.getRidleyScott().getSession(),
                "API/identity/professionalcontactdata"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getRidleyScott();
    }

    protected TestUser createUserWithProfessionnalContactData(ContactDataBuilder aContactData) {
        UserCreator userCreator = new UserCreator("aUser", "aPassword");
        userCreator.setProfessionalContactData(aContactData.toContactDataCreator());
        return getInitiator().createUser(userCreator);
    }

    @Test public void 
    getProfessionalContactData_return_professional_contact_data_of_user_with_given_id() {
        ContactDataBuilder aContactData = aContactData();
        TestUser user = createUserWithProfessionnalContactData(aContactData);
        
        final ProfessionalContactDataItem result = this.apiProfessionalContactData.get(makeAPIID(user.getId()));
        
        ProfessionalContactDataItem expectedItem = aContactData.toProfessionalContactDataItem();
        assertEquals(expectedItem.getEmail(), result.getEmail());
        assertEquals(expectedItem.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expectedItem.getMobileNumber(), result.getMobileNumber());
        assertEquals(expectedItem.getFaxNumber(), result.getFaxNumber());
        assertEquals(expectedItem.getBuilding(), result.getBuilding());
        assertEquals(expectedItem.getRoom(), result.getRoom());
        assertEquals(expectedItem.getAddress(), result.getAddress());
        assertEquals(expectedItem.getZipCode(), result.getZipCode());
        assertEquals(expectedItem.getCity(), result.getCity());
        assertEquals(expectedItem.getState(), result.getState());
        assertEquals(expectedItem.getCountry(), result.getCountry());
        assertEquals(expectedItem.getWebsite(), result.getWebsite());
    }

    @Test public void 
    updateProfessionalContactData_update_professional_contact_data_of_given_user() {
        TestUser user = createUserWithProfessionnalContactData(aContactData());
        ProfessionalContactDataItem contactDataItem = aContactData().withAddress("anOtherAddress").toProfessionalContactDataItem();

        ProfessionalContactDataItem updatedItem = 
                this.apiProfessionalContactData.update(makeAPIID(user.getId()), contactDataItem.getAttributes());

        final ProfessionalContactDataItem expectedItem = this.apiProfessionalContactData.get(makeAPIID(user.getId()));
        assertItemEquals(expectedItem, updatedItem);
    }

    @Test public void 
    addProfessionalContactData_add_professional_contact_data_to_a_user() {
        final TestUser user = getInitiator().createUser("user", "pwd");
        final ProfessionalContactDataItem res = new ProfessionalContactDataItem();
        res.setAddress("New address");
        res.setId(user.getId());
        
        this.apiProfessionalContactData.add(res);

        final ProfessionalContactDataItem result = this.apiProfessionalContactData.get(APIID.makeAPIID(user.getId()));
        assertEquals(result.getCity(), null);
        assertEquals(result.getAddress(), "New address");
        assertEquals(result.getBuilding(), null);
    }
}
