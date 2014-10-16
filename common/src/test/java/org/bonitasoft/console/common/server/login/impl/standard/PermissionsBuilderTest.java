package org.bonitasoft.console.common.server.login.impl.standard;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.profile.impl.ProfileEntryImpl;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionsBuilderTest {

    @Mock
    APISession apiSession;

    @Mock
    ProfileAPI profileAPI;

    @Mock
    CustomPermissionsMapping customPermissionsMapping;

    @Mock
    CompoundPermissionsMapping compoundPermissionsMapping;

    PermissionsBuilder permissionsBuilder;

    @Before
    public void setUp() {
        permissionsBuilder = spy(new PermissionsBuilder(apiSession));
    }

    @Test
    public void should_getPermissions_work_without_profile_and_user_permissions() throws Exception {

        doReturn("myUser").when(apiSession).getUserName();
        doReturn(1l).when(apiSession).getTenantId();
        doReturn(true).when(permissionsBuilder).isAuthorizationsChecksEnabled();
        doReturn(customPermissionsMapping).when(permissionsBuilder).getCustomPermissionsMapping();
        doReturn(compoundPermissionsMapping).when(permissionsBuilder).getCompoundPermissionsMapping();
        doReturn(profileAPI).when(permissionsBuilder).getProfileAPI();
        doReturn(new ArrayList<Profile>()).when(permissionsBuilder).getProfilesForUser(profileAPI, 0);
        doReturn(new ArrayList<String>()).when(permissionsBuilder).getCustomUserPermissions(customPermissionsMapping);

        final List<String> permissions = permissionsBuilder.getPermissions();

        Assert.assertEquals("No permissions should have been returned", 0, permissions.size());
    }

    @Test
    public void should_getPermissions_work_with_profile() throws Exception {

        doReturn("myUser").when(apiSession).getUserName();
        doReturn(1l).when(apiSession).getTenantId();
        doReturn(true).when(permissionsBuilder).isAuthorizationsChecksEnabled();
        doReturn(customPermissionsMapping).when(permissionsBuilder).getCustomPermissionsMapping();
        doReturn(compoundPermissionsMapping).when(permissionsBuilder).getCompoundPermissionsMapping();
        doReturn(profileAPI).when(permissionsBuilder).getProfileAPI();
        doReturn(fillInProfilesList(0, 50)).when(permissionsBuilder).getProfilesForUser(profileAPI, 0);
        doReturn(fillInProfilesList(50, 10)).when(permissionsBuilder).getProfilesForUser(profileAPI, 50);
        doReturn(fillInProfileEntriesList(0, 50)).when(permissionsBuilder).getProfileEntriesForProfile(Matchers.any(ProfileAPI.class),
                Matchers.any(Profile.class),
                Matchers.eq(0));
        doReturn(fillInProfileEntriesList(50, 10)).when(permissionsBuilder).getProfileEntriesForProfile(Matchers.any(ProfileAPI.class),
                Matchers.any(Profile.class),
                Matchers.eq(50));
        doReturn(new ArrayList<String>()).when(permissionsBuilder).getCustomUserPermissions(customPermissionsMapping);
        doReturn(new ArrayList<String>()).when(permissionsBuilder).getCustomProfilePermissions(Matchers.same(customPermissionsMapping),
                Matchers.any(Profile.class));
        final List<String> permissionsList = getSingleValueFakePermissionsList();
        doReturn(permissionsList).when(permissionsBuilder).getCompoundPermissions(Matchers.same(compoundPermissionsMapping), Matchers.anyString());

        final List<String> permissions = permissionsBuilder.getPermissions();

        Assert.assertEquals("60 x 60 permissions should have been returned", 3600, permissions.size());
    }

    @Test
    public void should_getPermissions_work_with_profile_and_user_permissions() throws Exception {

        doReturn("myUser").when(apiSession).getUserName();
        doReturn(1l).when(apiSession).getTenantId();
        doReturn(true).when(permissionsBuilder).isAuthorizationsChecksEnabled();
        doReturn(customPermissionsMapping).when(permissionsBuilder).getCustomPermissionsMapping();
        doReturn(compoundPermissionsMapping).when(permissionsBuilder).getCompoundPermissionsMapping();
        doReturn(profileAPI).when(permissionsBuilder).getProfileAPI();
        doReturn(fillInProfilesList(0, 50)).when(permissionsBuilder).getProfilesForUser(profileAPI, 0);
        doReturn(fillInProfilesList(50, 10)).when(permissionsBuilder).getProfilesForUser(profileAPI, 50);
        doReturn(fillInProfileEntriesList(0, 50)).when(permissionsBuilder).getProfileEntriesForProfile(Matchers.any(ProfileAPI.class),
                Matchers.any(Profile.class),
                Matchers.eq(0));
        doReturn(fillInProfileEntriesList(50, 10)).when(permissionsBuilder).getProfileEntriesForProfile(Matchers.any(ProfileAPI.class),
                Matchers.any(Profile.class),
                Matchers.eq(50));
        doReturn(getSingleValueFakePermissionsList()).when(permissionsBuilder).getCustomUserPermissions(customPermissionsMapping);
        doReturn(getSingleValueFakePermissionsList()).when(permissionsBuilder).getCustomProfilePermissions(Matchers.same(customPermissionsMapping),
                Matchers.any(Profile.class));
        doReturn(getSingleValueFakePermissionsList()).when(permissionsBuilder).getCompoundPermissions(Matchers.same(compoundPermissionsMapping), Matchers.anyString());

        final List<String> permissions = permissionsBuilder.getPermissions();

        Assert.assertEquals("60 x 60 + 60 + 1 permissions should have been returned", 3661, permissions.size());
    }

    @Test
    public void should_getCustomPermissions_work_with_compound_permissions() throws Exception {

        doReturn(compoundPermissionsMapping).when(permissionsBuilder).getCompoundPermissionsMapping();
        doReturn(getSingleValueFakePermissionsList()).when(permissionsBuilder).getCustomPermissionsRaw(Matchers.same(customPermissionsMapping), Matchers.anyString());
        doReturn(getDoubleValueFakePermissionsList()).when(permissionsBuilder).getCompoundPermissions(Matchers.same(compoundPermissionsMapping),
                Matchers.anyString());

        final List<String> permissions = permissionsBuilder.getCustomPermissions(customPermissionsMapping, "user|", "myUser");

        Assert.assertEquals("2 permissions should have been returned", 2, permissions.size());

    }

    protected List<String> getSingleValueFakePermissionsList() {
        final List<String> fakePermissionsList = new ArrayList<String>();
        fakePermissionsList.add(UUID.randomUUID().toString());
        return fakePermissionsList;
    }

    protected List<String> getDoubleValueFakePermissionsList() {
        final List<String> fakePermissionsList = new ArrayList<String>();
        fakePermissionsList.add(UUID.randomUUID().toString());
        fakePermissionsList.add(UUID.randomUUID().toString());
        return fakePermissionsList;
    }

    protected List<Profile> fillInProfilesList(final int startIndex, final int nbOfItems) {
        final List<Profile> userProfiles = new ArrayList<Profile>();
        for (long i = startIndex; i < startIndex + nbOfItems; i++) {
            final Profile profile = mock(Profile.class);
            doReturn("profile" + i).when(profile).getName();
            doReturn(i).when(profile).getId();
            userProfiles.add(profile);
        }
        return userProfiles;
    }

    protected List<ProfileEntry> fillInProfileEntriesList(final int startIndex, final int nbOfItems) {
        final List<ProfileEntry> profileEntries = new ArrayList<ProfileEntry>();
        for (int i = startIndex; i < startIndex + nbOfItems; i++) {
            ProfileEntryImpl page = new ProfileEntryImpl("page"+i);
            page.setType(ProfileEntryItem.VALUE_TYPE.link.name());
            profileEntries.add(page);
        }
        return profileEntries;
    }
}
