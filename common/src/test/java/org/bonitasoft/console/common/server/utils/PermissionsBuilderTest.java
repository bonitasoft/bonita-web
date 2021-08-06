package org.bonitasoft.console.common.server.utils;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.session.impl.APISessionImpl;
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

    private APISessionImpl apiSession = new APISessionImpl(1, new Date(), 100000, "myUser", 42L, "default", 1L);
    @Mock
    private CustomPermissionsMapping customPermissionsMapping;
    @Mock
    private CompoundPermissionsMapping compoundPermissionsMapping;
    @Mock
    private SecurityProperties securityProperties;
    @Mock
    private ApplicationAPI applicationAPI;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private PermissionsBuilder permissionsBuilder;

    @Before
    public void setUp() throws Exception {
        init();
    }

    private void init() {
        permissionsBuilder = spy(new PermissionsBuilder(apiSession, applicationAPI, customPermissionsMapping, compoundPermissionsMapping));
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
        final Set<String> permissions = new HashSet<>();
        doReturn(aSet("Page1", "Page2")).when(permissionsBuilder).getAllPagesForUser(permissions);
        doReturn(aSet("Perm2", "Perm1")).when(compoundPermissionsMapping).getPropertyAsSet(eq("Page1"));
        doReturn(aSet("Perm1", "Perm3")).when(compoundPermissionsMapping).getPropertyAsSet(eq("Page2"));

        permissionsBuilder.addProfilesPermissions(permissions);

        assertThat(permissions).containsOnly("Perm1", "Perm2", "Perm3");
    }

    @Test
    public void should_getAllPagesForUser_add_page_and_custom_permissions_of_profiles_of_user() throws Exception {
        final Set<String> permissions = new HashSet<>();
        doNothing().when(permissionsBuilder).addPageAndCustomPermissionsOfProfile(eq(permissions),
                anySetOf(String.class), any());
        apiSession.setProfiles(asList("myProfile1", "myProfile2"));

        permissionsBuilder.getAllPagesForUser(permissions);

        verify(permissionsBuilder).addPageAndCustomPermissionsOfProfile(eq(permissions), anySetOf(String.class),
                eq("myProfile1"));
        verify(permissionsBuilder).addPageAndCustomPermissionsOfProfile(eq(permissions), anySetOf(String.class),
                eq("myProfile2"));
    }
    @Test
    public void should_getPermissions_retrieve_permisions_even_if_secu_not_active() throws Exception {
        //given
        doReturn(false).when(securityProperties).isAPIAuthorizationsCheckEnabled();
        doReturn(singleton("Page1")).when(permissionsBuilder).getAllPagesForUser(anySetOf(String.class));
        doReturn(aSet("Perm2", "Perm1")).when(compoundPermissionsMapping)
                .getPropertyAsSet(eq("Page1"));

        //when
        final Set<String> permissions = permissionsBuilder.getPermissions();

        //then
        assertThat(permissions).containsOnly("Perm1","Perm2");
        verify(permissionsBuilder).addProfilesPermissions(anySetOf(String.class));
    }

    @Test
    public void should_getCustomPermissions_work_with_compound_permissions() {
        doReturn(aSet("Perm1", "Perm2", "taskListing")).when(customPermissionsMapping).getPropertyAsSet("user|myUser");
        doReturn(aSet("Perm3", "Perm4")).when(compoundPermissionsMapping).getPropertyAsSet("taskListing");

        final Set<String> permissions = permissionsBuilder.getCustomPermissions("user", "myUser");

        assertThat(permissions).containsOnly("Perm1","Perm2","Perm3","Perm4");
    }

    @Test
    public void testAddPagesOfApplication() {
        Set<String> permissions = aSet("Perm1", "Perm2");
        doReturn(asList("Perm3", "Perm4")).when(applicationAPI).getAllPagesForProfile("myProfile1");
        doReturn(asList("Perm4", "Perm5")).when(applicationAPI).getAllPagesForProfile("myProfile2");

        permissionsBuilder.addPagesOfApplication("myProfile1", permissions);
        permissionsBuilder.addPagesOfApplication("myProfile2", permissions);

        assertThat(permissions).containsOnly("Perm1", "Perm2", "Perm3", "Perm4", "Perm5");
    }

    private Set<String> aSet(String... elements) {
        return new HashSet<>(asList(elements));
    }
}
