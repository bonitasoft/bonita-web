package org.bonitasoft.console.common.server.login.impl.standard;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.session.APISession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

    PermissionsBuilder permissionsBuilder;

    @Before
    public void setUp() {
        permissionsBuilder = spy(new PermissionsBuilder(apiSession));
    }

    @Test
    public void should_GetPermissions_work_without_profile() throws Exception {
        doReturn("myUser").when(apiSession).getUserName();
        doReturn(1l).when(apiSession).getTenantId();
        doReturn(true).when(permissionsBuilder).isAuthorizationsChecksEnabled();
        doReturn(customPermissionsMapping).when(permissionsBuilder).getCustomPermissionsMapping();
        doReturn(compoundPermissionsMapping).when(permissionsBuilder).getCompoundPermissionsMapping();
        doReturn(profileAPI).when(permissionsBuilder).getProfileAPI();
        doReturn(new ArrayList<Profile>()).when(permissionsBuilder).getProfilesForUser(profileAPI, 0);

        final List<String> permissions = permissionsBuilder.getPermissions();
        Assert.assertEquals(0, permissions.size());
    }

}
