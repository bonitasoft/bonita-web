package org.bonitasoft.console.server.datastore.profile.member;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.console.server.api.userXP.profile.APIProfileMember;
import org.bonitasoft.test.toolkit.organization.TestGroup;
import org.bonitasoft.test.toolkit.organization.TestGroupFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.test.toolkit.organization.profiles.TestProfile;
import org.bonitasoft.test.toolkit.organization.profiles.TestProfileFactory;
import org.bonitasoft.test.toolkit.organization.profiles.TestProfileMember;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
import org.junit.Ignore;
import org.junit.Test;

public class ProfileMemberDatastoreIntegrationTest extends AbstractConsoleTest {

    private APIProfileMember apiProfileMember;

    @Override
    public void consoleTestSetUp() throws Exception {
        this.apiProfileMember = new APIProfileMember();
        this.apiProfileMember.setCaller(getAPICaller(getInitiator().getSession(), "API/userXP/profileMember"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Ignore("TestProfileFactory need to be updated to stop using command")
    @Test
    public void testWeCanSearchAUser() {
        TestProfile profile = TestProfileFactory.newProfile(getInitiator().getSession()).create();
        TestProfileMember membership = profile.addMember(getInitiator());
        Map<String, String> filters = createSearchFilters(profile, ProfileMemberItem.VALUE_MEMBER_TYPE_USER);

        ItemSearchResult<ProfileMemberItem> search = apiProfileMember.search(0, 1, null, null, filters);

        Assert.assertEquals(APIID.makeAPIID(membership.getMembershipId()), search.getResults().get(0).getId());
    }

    /*
     * FIXME engine throw npe when trying to add group to a profile
     */
    @Ignore
    @Test
    public void testWeCanSearchAGroup() {
        TestProfile profile = TestProfileFactory.newProfile(getInitiator().getSession()).create();
        TestGroup group = TestGroupFactory.createGroup("name", "desciption");
        TestProfileMember membership = profile.addMember(group);
        Map<String, String> filters = createSearchFilters(profile, ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP);

        ItemSearchResult<ProfileMemberItem> search = apiProfileMember.search(0, 1, null, null, filters);

        Assert.assertEquals(APIID.makeAPIID(membership.getMembershipId()), search.getResults().get(0).getId());
    }

    private Map<String, String> createSearchFilters(TestProfile profile, String memberType) {
        Map<String, String> filters = new HashMap<String, String>();
        filters.put(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, String.valueOf(profile.getId()));
        filters.put(ProfileMemberItem.FILTER_MEMBER_TYPE, memberType);
        return filters;
    }

}
