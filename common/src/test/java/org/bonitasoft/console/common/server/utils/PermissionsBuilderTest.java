package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.persistence.SBonitaReadException;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileCriterion;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.profile.impl.ProfileEntryImpl;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
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

    @Mock
    SecurityProperties securityProperties;

    @Mock
    private Profile profile;

    @Mock
    private ApplicationAPI applicationAPI;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    PermissionsBuilder permissionsBuilder;

    @Before
    public void setUp() throws Exception {
        init(true);
    }

    private void init(final boolean apiAuthorizationsCheckEnabled) {
        doReturn(apiAuthorizationsCheckEnabled).when(securityProperties).isAPIAuthorizationsCheckEnabled();
        permissionsBuilder = spy(new PermissionsBuilder(apiSession, profileAPI, applicationAPI, customPermissionsMapping, compoundPermissionsMapping,
                securityProperties));
        doReturn("myUser").when(apiSession).getUserName();
        doReturn(1l).when(apiSession).getTenantId();
        doReturn(1l).when(apiSession).getUserId();
    }

    @Test
    public void should_getPermissions_work_without_profile_and_user_permissions() throws Exception {
        doNothing().when(permissionsBuilder).addProfilesPermissions(anySetOf(String.class));
        doNothing().when(permissionsBuilder).addCustomUserPermissions(anySetOf(String.class));

        final Set<String> permissions = permissionsBuilder.getPermissions();

        Assert.assertEquals("No permissions should have been returned", 0, permissions.size());
    }

    @Test
    public void should_getPermissions_add_profile_and_custom_permissions() throws Exception {
        doNothing().when(permissionsBuilder).addProfilesPermissions(anySetOf(String.class));
        doNothing().when(permissionsBuilder).addCustomUserPermissions(anySetOf(String.class));

        permissionsBuilder.getPermissions();

        verify(permissionsBuilder).addProfilesPermissions(anySetOf(String.class));
        verify(permissionsBuilder).addCustomUserPermissions(anySetOf(String.class));
    }

    @Test
    public void should_addProfilesPermissions_add_permissions_to_the_set() throws Exception {
        final HashSet<String> permissions = new HashSet<String>();
        doReturn(new HashSet<String>(Arrays.asList("Page1", "Page2"))).when(permissionsBuilder).getAllPagesForUser(permissions);
        doReturn(new HashSet<String>(Arrays.asList("Perm2", "Perm1"))).when(compoundPermissionsMapping).getPropertyAsSet(eq("Page1"));
        doReturn(new HashSet<String>(Arrays.asList("Perm1", "Perm3"))).when(compoundPermissionsMapping).getPropertyAsSet(eq("Page2"));

        permissionsBuilder.addProfilesPermissions(permissions);

        assertThat(permissions).containsOnly("Perm1", "Perm2", "Perm3");
    }

    @Test
    public void should_addProfilesPermissions_throw_LoginException_when_issue_on_getAllPages() throws Exception {

        final Set<String> permissions = new HashSet<String>();
        doThrow(new SearchException("issue", new SBonitaReadException(""))).when(permissionsBuilder).getAllPagesForUser(permissions);
        try {
            permissionsBuilder.addProfilesPermissions(permissions);
            fail("expecting " + LoginFailedException.class.getName());
        } catch (final LoginFailedException e) {
            if (!(e.getCause() instanceof SearchException)) {
                fail("expecting " + SearchException.class.getName() + " as exception cause");
            }
        }
    }

    @Test
    public void should_getAllPagesForUser_add_page_and_custom_permissions_of_profiles_of_user() throws Exception {
        final List<Profile> profileList1 = fillInProfilesList(0, PermissionsBuilder.MAX_ELEMENTS_RETRIEVED);
        doReturn(profileList1).when(profileAPI).getProfilesForUser(anyLong(), anyInt(), anyInt(), any(ProfileCriterion.class));
        final List<Profile> profileList2 = fillInProfilesList(PermissionsBuilder.MAX_ELEMENTS_RETRIEVED, 10);
        doReturn(profileList2).when(profileAPI).getProfilesForUser(anyLong(), eq(PermissionsBuilder.MAX_ELEMENTS_RETRIEVED), anyInt(),
                any(ProfileCriterion.class));
        final HashSet<String> permissions = new HashSet<String>();
        doNothing().when(permissionsBuilder).addPageAndCustomPermissionsOfProfile(eq(permissions), anySetOf(String.class), any(Profile.class));

        permissionsBuilder.getAllPagesForUser(permissions);

        for (final Profile profile : profileList1) {
            verify(permissionsBuilder).addPageAndCustomPermissionsOfProfile(eq(permissions), anySetOf(String.class), eq(profile));
        }
    }

    @Test
    public void should_addPageAndCustomPermissionsOfProfile_complete_set_of_permission_and_page() throws Exception {
        final HashSet<String> permissions = new HashSet<String>();
        final HashSet<String> pages = new HashSet<String>();
        final Profile profile = mock(Profile.class);
        doReturn("profileName").when(profile).getName();
        doReturn(new HashSet<String>(Arrays.asList("Perm1", "Perm2"))).when(permissionsBuilder).getCustomPermissions(eq("profile"), eq("profileName"));
        doNothing().when(permissionsBuilder).addPagesOfProfile(profile, pages);

        permissionsBuilder.addPageAndCustomPermissionsOfProfile(permissions, pages, profile);

        verify(permissionsBuilder).addPagesOfProfile(profile, pages);
        assertThat(permissions).containsOnly("Perm1", "Perm2", "profile|profileName");
    }

    @Test
    public void should_addPagesOfProfile_complete_set_of_page() throws Exception {
        final HashSet<String> pages = new HashSet<String>();
        final Profile profile = mock(Profile.class);
        final List<ProfileEntry> profileEntryList = fillInProfileEntriesList(PermissionsBuilder.MAX_ELEMENTS_RETRIEVED + 10);
        doReturn(profileEntryList.subList(0, PermissionsBuilder.MAX_ELEMENTS_RETRIEVED)).when(permissionsBuilder).getProfileEntriesForProfile(profile, 0);
        doReturn(profileEntryList.subList(PermissionsBuilder.MAX_ELEMENTS_RETRIEVED, PermissionsBuilder.MAX_ELEMENTS_RETRIEVED + 10)).when(permissionsBuilder)
                .getProfileEntriesForProfile(profile, PermissionsBuilder.MAX_ELEMENTS_RETRIEVED);

        permissionsBuilder.addPagesOfProfile(profile, pages);

        final ArrayList<String> pageNames = new ArrayList<String>();
        for (int i = 0; i < PermissionsBuilder.MAX_ELEMENTS_RETRIEVED + 10; i++) {
            pageNames.add("page" + i);
        }
        assertThat(pages).containsOnly(pageNames.toArray(new String[] {}));
    }

    @Test
    public void should_getPermissions_return_empty_list_if_secu_not_active() throws Exception {
        //given
        init(false);

        //when
        final Set<String> permissions = permissionsBuilder.getPermissions();

        //then
        assertThat(permissions).isEmpty();
        verify(permissionsBuilder, never()).addProfilesPermissions(anySetOf(String.class));
    }

    @Test
    public void should_getCustomPermissions_work_with_compound_permissions() throws Exception {
        doReturn(new HashSet<String>(Arrays.asList("Perm1", "Perm2", "taskListing"))).when(customPermissionsMapping).getPropertyAsSet("user|myUser");
        doReturn(new HashSet<String>(Arrays.asList("Perm3", "Perm4"))).when(compoundPermissionsMapping).getPropertyAsSet("taskListing");

        final Set<String> permissions = permissionsBuilder.getCustomPermissions("user", "myUser");

        assertThat(permissions).containsOnly("Perm1","Perm2","Perm3","Perm4");
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

    protected List<ProfileEntry> fillInProfileEntriesList(final int nbOfItems) {
        final List<ProfileEntry> profileEntries = new ArrayList<ProfileEntry>();
        for (int i = 0; i < nbOfItems; i++) {
            final ProfileEntryImpl page = new ProfileEntryImpl("page" + i);
            page.setType(ProfileEntryItem.VALUE_TYPE.link.name());
            page.setPage("page" + i);
            profileEntries.add(page);
        }
        return profileEntries;
    }

    @Test
    public void testAddPagesOfApplication() throws Exception {
        //given
        final Set<String> permissions = new HashSet<String>(Arrays.asList("Perm1", "Perm2"));
        doReturn(12l).when(profile).getId();
        when(applicationAPI.getAllPagesForProfile(profile.getId())).thenReturn(Arrays.asList("Perm3", "Perm4"));

        //when
        permissionsBuilder.addPagesOfApplication(profile, permissions);

        //then
        assertThat(permissions).containsOnly("Perm1", "Perm2", "Perm3", "Perm4");
    }
}
