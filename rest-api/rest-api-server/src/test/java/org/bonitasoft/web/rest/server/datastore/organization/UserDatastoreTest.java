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
package org.bonitasoft.web.rest.server.datastore.organization;

import java.util.Collections;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverterException;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.server.engineclient.UserEngineClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * @author Vincent Elcrin
 * 
 */
public class UserDatastoreTest {

    @Mock
    private IdentityAPI mockedIdentityAPI;

    @Spy
    private UserDatastore datastore = new UserDatastore(null);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        UserEngineClient userEngineClient = new UserEngineClient(mockedIdentityAPI);
        Mockito.doReturn(userEngineClient).when(datastore).getUserEngineClient();
    }

    @Test
    public void testSearchWithMultipleSortOrderDontThrowException() throws Exception {
        final String sort = UserItem.ATTRIBUTE_FIRSTNAME + "," + UserItem.ATTRIBUTE_LASTNAME;
        Mockito.doReturn(new SearchResultImpl<User>(0, Collections.<User> emptyList())).when(mockedIdentityAPI).searchUsers(Mockito.any(SearchOptions.class));

        try {
            datastore.search(0, 1, "search", Collections.<String, String> emptyMap(), sort);

        } catch (AttributeConverterException e) {
            Assert.fail("Search should be able to handle multple sort");
        }
    }
}
