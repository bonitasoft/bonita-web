/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.organization;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;
import static org.junit.Assert.assertTrue;

import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Test;

/**
 * @author Colin PUY
 */
@SuppressWarnings("unchecked")
public class APIUserIntegrationTest extends AbstractConsoleTest {

    private static final String ASCENDING = " asc";
    private static final String DESCENDING = " desc";

    private APIUser apiUser;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiUser = new APIUser();
        apiUser.setCaller(getAPICaller(getInitiator().getSession(), "API/identity/user"));

    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    private boolean lowerThan(final String string1, final String string2) {
        return string1.compareTo(string2) < 0;
    }

    private boolean upperThan(final String string1, final String string2) {
        return string1.compareTo(string2) > 0;
    }

    @Test
    public void searchCanBeOrderdByFirstNameAscending() throws Exception {
        TestUserFactory.getRidleyScott();
        TestUserFactory.getJohnCarpenter();

        final ItemSearchResult<UserItem> searchResult = apiUser.runSearch(0, 10, null, UserItem.ATTRIBUTE_FIRSTNAME + ASCENDING,
                EMPTY_MAP, EMPTY_LIST, EMPTY_LIST);

        final String firstUserFirstName = searchResult.getResults().get(0).getFirstName();
        final String secondUserFirstName = searchResult.getResults().get(1).getFirstName();
        assertTrue(lowerThan(firstUserFirstName, secondUserFirstName));
    }

    @Test
    public void searchCanBeOrderdByFirstNameDescending() throws Exception {
        TestUserFactory.getRidleyScott();
        TestUserFactory.getJohnCarpenter();

        final ItemSearchResult<UserItem> searchResult = apiUser.runSearch(0, 10, null, UserItem.ATTRIBUTE_FIRSTNAME + DESCENDING,
                EMPTY_MAP, EMPTY_LIST, EMPTY_LIST);

        final String firstUserFirstName = searchResult.getResults().get(0).getFirstName();
        final String secondUserFirstName = searchResult.getResults().get(1).getFirstName();
        assertTrue(upperThan(firstUserFirstName, secondUserFirstName));
    }

    @Test
    public void searchCanBeOrderdByLastNameAscending() throws Exception {
        TestUserFactory.getRidleyScott();
        TestUserFactory.getJohnCarpenter();

        final ItemSearchResult<UserItem> searchResult = apiUser.runSearch(0, 10, null, UserItem.ATTRIBUTE_LASTNAME + ASCENDING,
                EMPTY_MAP, EMPTY_LIST, EMPTY_LIST);

        final String firstUserLastName = searchResult.getResults().get(0).getLastName();
        final String secondUserLastName = searchResult.getResults().get(1).getLastName();
        assertTrue(lowerThan(firstUserLastName, secondUserLastName));
    }

    @Test
    public void searchCanBeOrderdByLastNameDescending() throws Exception {
        TestUserFactory.getRidleyScott();
        TestUserFactory.getJohnCarpenter();

        final ItemSearchResult<UserItem> searchResult = apiUser.runSearch(0, 10, null, UserItem.ATTRIBUTE_LASTNAME + DESCENDING,
                EMPTY_MAP, EMPTY_LIST, EMPTY_LIST);

        final String firstUserLastName = searchResult.getResults().get(0).getLastName();
        final String secondUserLastName = searchResult.getResults().get(1).getLastName();
        assertTrue(upperThan(firstUserLastName, secondUserLastName));
    }

    @Test
    public void searchCanBeOrderdByUserNameAscending() throws Exception {
        TestUserFactory.getRidleyScott();
        TestUserFactory.getJohnCarpenter();

        final ItemSearchResult<UserItem> searchResult = apiUser.runSearch(0, 10, null, UserItem.ATTRIBUTE_USERNAME + ASCENDING,
                EMPTY_MAP, EMPTY_LIST, EMPTY_LIST);

        final String firstUserUserName = searchResult.getResults().get(0).getUserName();
        final String secondUserUserName = searchResult.getResults().get(1).getUserName();
        assertTrue(lowerThan(firstUserUserName, secondUserUserName));
    }

    @Test
    public void searchCanBeOrderdByUserNameDescending() throws Exception {
        TestUserFactory.getRidleyScott();
        TestUserFactory.getJohnCarpenter();

        final ItemSearchResult<UserItem> searchResult = apiUser.runSearch(0, 10, null, UserItem.ATTRIBUTE_USERNAME + DESCENDING,
                EMPTY_MAP, EMPTY_LIST, EMPTY_LIST);

        final String firstUserUserName = searchResult.getResults().get(0).getUserName();
        final String secondUserUserName = searchResult.getResults().get(1).getUserName();
        assertTrue(upperThan(firstUserUserName, secondUserUserName));
    }
}
