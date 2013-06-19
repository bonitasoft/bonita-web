package org.bonitasoft.web.rest.server.api.organization;

import static java.util.Arrays.asList;
import static org.bonitasoft.test.toolkit.organization.TestUserFactory.getJohnCarpenter;
import static org.bonitasoft.test.toolkit.organization.TestUserFactory.getMrSpechar;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.identity.Role;
import org.bonitasoft.test.toolkit.organization.TestGroup;
import org.bonitasoft.test.toolkit.organization.TestGroupFactory;
import org.bonitasoft.test.toolkit.organization.TestMembershipFactory;
import org.bonitasoft.test.toolkit.organization.TestRole;
import org.bonitasoft.test.toolkit.organization.TestRoleFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.api.organization.APIRole;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Séverin Moussel
 */
public class APIRoleIntegrationTest extends AbstractConsoleTest {

    @Override
    public void consoleTestSetUp() throws Exception {
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    /**
     * @return
     */
    private APIRole getAPIRole() {
        final APIRole apiRole = new APIRole();
        apiRole.setCaller(getAPICaller(getInitiator().getSession(), "API/identity/role"));
        return apiRole;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GET / ADD
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void assertItemEquals(final String message, final RoleItem expected, final RoleItem actual) {
        Assert.assertEquals(message, expected.getAttributes(), actual.getAttributes());
    }

    @Test
    public void testAddAndGet() {
        // Add
        RoleItem input = new RoleItem();
        input.setName("Developper");
        input.setDescription("The guys who drink a lot of coffee");
        input = getAPIRole().runAdd(input);

        Assert.assertNotNull("Failed to add a new role", input);

        // Get
        final RoleItem output = getAPIRole().runGet(input.getId(), new ArrayList<String>(), new ArrayList<String>());

        Assert.assertNotNull("Role not found", output);
        assertItemEquals("Wrong role found", input, output);
    }

    @Test
    public void testDeploys() {
        // Add
        RoleItem input = new RoleItem();
        input.setName("Developper");
        input.setDescription("The guys who drink a lot of coffee");
        input = getAPIRole().runAdd(input);

        Assert.assertNotNull("Failed to add a new role", input);

        // Get
        final RoleItem output = getAPIRole().runGet(
                input.getId(),
                Arrays.asList(RoleItem.ATTRIBUTE_CREATED_BY_USER_ID),
                new ArrayList<String>());

        Assert.assertNotNull("Role not found", output);
        assertItemEquals("Wrong role found", input, output);

        Assert.assertNotNull("Failed to deploy intiator user", output.getCreatedByUserId());
        Assert.assertEquals("Wrong process deployed", getInitiator().getUserName(), output.getCreatedByUser().getUserName());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SEARCH
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testSearch() throws Exception {
        TestRoleFactory.createRandomRoles(13);

        final ItemSearchResult<RoleItem> roleItems = getAPIRole().runSearch(0, 10, null, null, null, null, null);

        checkSearchResults(roleItems, 10, 13);
    }

    /**
     * @param roleItems
     */
    private void checkSearchResults(final ItemSearchResult<RoleItem> roleItems, final int nbResultsByPageExpected, final int nbTotalResultsExpected) {
        Assert.assertTrue("Empty search results", roleItems.getLength() > 0);
        Assert.assertTrue("Wrong page size", roleItems.getLength() == nbResultsByPageExpected);
        Assert.assertTrue("Wrong Total size", roleItems.getTotal() == nbTotalResultsExpected);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DELETE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testDeleteOne() throws Exception {
        TestRoleFactory.createRandomRoles(13);

        final ItemSearchResult<RoleItem> roleItems = getAPIRole().runSearch(0, 10, null, null, null, null, null);
        getAPIRole().runDelete(Arrays.asList(roleItems.getResults().get(0).getId()));

        final ItemSearchResult<RoleItem> roleItemsAfter = getAPIRole().runSearch(0, 10, null, null, null, null, null);
        Assert.assertTrue("Failed to delete one role", roleItemsAfter.getTotal() == 12);
    }

    @Test
    public void testDeleteMultiple() throws Exception {
        TestRoleFactory.createRandomRoles(13);

        final ItemSearchResult<RoleItem> roleItems = getAPIRole().runSearch(0, 10, null, null, null, null, null);

        getAPIRole().runDelete(Arrays.asList(
                roleItems.getResults().get(1).getId(),
                roleItems.getResults().get(0).getId()
                ));

        final ItemSearchResult<RoleItem> roleItemsAfter = getAPIRole().runSearch(0, 10, null, null, null, null, null);
        Assert.assertTrue("Failed to delete multiple roles", roleItemsAfter.getTotal() == 11);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UPDATE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testUpdate() throws Exception {
        final String newDescription = "Lorem ipsum dolor sit amet";

        // Add
        RoleItem input = new RoleItem();
        input.setName("Developper");
        input.setDescription("The guys who drink a lot of coffee");
        input = getAPIRole().runAdd(input);

        Assert.assertNotNull("Failed to add a new role", input);

        // Update
        final Map<String, String> updates = new HashMap<String, String>();
        updates.put(RoleItem.ATTRIBUTE_DESCRIPTION, newDescription);
        getAPIRole().runUpdate(input.getId(), updates);

        // Get
        final RoleItem output = getAPIRole().runGet(input.getId(), new ArrayList<String>(), new ArrayList<String>());

        Assert.assertNotNull("Role not found", output);
        Assert.assertEquals("Update of role failed", newDescription, output.getDescription());
    }

    @Test
    public void weCanCountAllUsersInAGroup() throws Exception {
        Role roleWith2Users = createRoleWithAssignedUsers(getJohnCarpenter(), getMrSpechar());
        List<String> counters = asList(RoleItem.COUNTER_NUMBER_OF_USERS);
        
        RoleItem roleItem = getAPIRole().runGet(APIID.makeAPIID(roleWith2Users.getId()), null, counters);
        
        assertEquals(2L, (long) roleItem.getNumberOfUsers());
    }
    
    private Role createRoleWithAssignedUsers(TestUser... users) {
        TestGroup aGroup = TestGroupFactory.getRAndD();
        TestRole aRole = TestRoleFactory.getDeveloper();
        for (TestUser user : users) {
            TestMembershipFactory.assignMembership(user, aGroup, aRole);
        }
        return aRole.getRole();
    }
}
