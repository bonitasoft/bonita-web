package org.bonitasoft.web.rest.server.api.organization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.test.toolkit.organization.TestGroup;
import org.bonitasoft.test.toolkit.organization.TestGroupFactory;
import org.bonitasoft.test.toolkit.organization.TestMembershipFactory;
import org.bonitasoft.test.toolkit.organization.TestRole;
import org.bonitasoft.test.toolkit.organization.TestRoleFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.identity.MembershipItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Séverin Moussel
 * @author Colin PUY
 */
public class APIMembershipIntegrationTest extends AbstractConsoleTest {

    private APIMembership apiMembership;
    
    @Override
    public void consoleTestSetUp() throws Exception {
        apiMembership = new APIMembership();
        apiMembership.setCaller(getAPICaller(getInitiator().getSession(), "API/identity/membership"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    private void checkSearchResults(final ItemSearchResult<MembershipItem> membershipItems, final int nbResultsByPageExpected, final int nbTotalResultsExpected) {
        Assert.assertTrue("Empty search results", membershipItems.getLength() > 0);
        Assert.assertTrue("Wrong page size", membershipItems.getLength() == nbResultsByPageExpected);
        Assert.assertTrue("Wrong Total size", membershipItems.getTotal() == nbTotalResultsExpected);
    }

    @Test
    public void testAdd() {
        // Add
        final MembershipItem input = new MembershipItem();
        input.setUserId(getInitiator().getId());
        input.setGroupId(TestGroupFactory.getWeb().getId());
        input.setRoleId(TestRoleFactory.getManager().getId());

        final MembershipItem output = apiMembership.runAdd(input);

        Assert.assertNotNull("Failed to add a new membership", input);
        Assert.assertEquals("Wrong membership inserted", input.getUserId(), output.getUserId());
        Assert.assertEquals("Wrong membership inserted", input.getGroupId(), output.getGroupId());
        Assert.assertEquals("Wrong membership inserted", input.getRoleId(), output.getRoleId());
    }

    private void beforeSearch() {
        final List<TestRole> roles = TestRoleFactory.getInstance().createRandomRoles(5);
        final List<TestGroup> groups = TestGroupFactory.createRandomGroups(5);

        final TestUser user1 = getInitiator();
        final TestUser user2 = TestUserFactory.getRidleyScott();

        for (final TestRole role : roles) {
            for (final TestGroup group : groups) {
                TestMembershipFactory.assignMembership(user1, group, role);
                TestMembershipFactory.assignMembership(user2, group, role);
            }
        }
    }

    @Test
    public void testSearch() {
        beforeSearch();

        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(MembershipItem.ATTRIBUTE_USER_ID, String.valueOf(getInitiator().getId()));

        final ItemSearchResult<MembershipItem> searchResults = apiMembership.runSearch(0, 12, null, null, filters, null, null);

        checkSearchResults(searchResults, 12, 25);
    }

    @Test
    public void testDeploys() {
        beforeSearch();

        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(MembershipItem.ATTRIBUTE_USER_ID, String.valueOf(getInitiator().getId()));

        final ItemSearchResult<MembershipItem> searchResults = apiMembership.runSearch(
                0,
                11,
                null,
                null,
                filters,
                Arrays.asList(
                        MembershipItem.ATTRIBUTE_USER_ID,
                        MembershipItem.ATTRIBUTE_ROLE_ID,
                        MembershipItem.ATTRIBUTE_GROUP_ID,
                        MembershipItem.ATTRIBUTE_ASSIGNED_BY_USER_ID),
                null);

        checkSearchResults(searchResults, 11, 25);

        final MembershipItem firstMembership = searchResults.getResults().get(0);

        Assert.assertNotNull("Failed to deploy user_id", firstMembership.getUser());
        Assert.assertEquals("Wrong user deployed", getInitiator().getUser().getUserName(), firstMembership.getUser().getUserName());
        Assert.assertNotNull("Failed to deploy role_id", firstMembership.getRole());
        Assert.assertNotNull("Failed to deploy group_id", firstMembership.getGroup());
        Assert.assertNotNull("Failed to deploy assigned_by_user_id", firstMembership.getAssignedByUser());
    }

    @Test
    public void testDelete() {

        // INIT
        final TestRole roleManager = TestRoleFactory.getManager();
        final TestRole roleDevelopper = TestRoleFactory.getDeveloper();
        final TestGroup groupWeb = TestGroupFactory.createRandomGroups(1).get(0);

        final TestUser user = getInitiator();

        TestMembershipFactory.assignMembership(user, groupWeb, roleManager);
        TestMembershipFactory.assignMembership(user, groupWeb, roleDevelopper);

        // ACTION
        apiMembership.runDelete(
                Arrays.asList(APIID.makeAPIID(
                        getInitiator().getId(),
                        groupWeb.getId(),
                        roleManager.getId()
                        )
                        ));

        // CHECK RESULT

        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(MembershipItem.ATTRIBUTE_USER_ID, String.valueOf(getInitiator().getId()));

        final ItemSearchResult<MembershipItem> searchResults = apiMembership.runSearch(0, 12, null, null, filters, null, null);

        checkSearchResults(searchResults, 12, 1);
    }
    
    @Test(expected = APIForbiddenException.class)
    public void addingTwiceSameMembershipIsForbidden() throws Exception {
        MembershipItem input = new MembershipItem();
        input.setUserId(getInitiator().getId());
        input.setGroupId(TestGroupFactory.getWeb().getId());
        input.setRoleId(TestRoleFactory.getManager().getId());

        apiMembership.runAdd(input);
        apiMembership.runAdd(input);
    }
}
