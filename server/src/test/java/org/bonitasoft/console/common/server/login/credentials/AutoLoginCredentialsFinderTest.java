package org.bonitasoft.console.common.server.login.credentials;

import org.bonitasoft.console.common.server.preferences.properties.ConfigurationFilesManager;
import org.bonitasoft.console.common.server.preferences.properties.ProcessIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


/**
 * Created by julien.mege on 23/06/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class AutoLoginCredentialsFinderTest {

    private static final long TENANT_ID = 43882L;
    private File autoLoginConfiguration;

    @InjectMocks
    public AutoLoginCredentialsFinder autoLoginCredentialsFinder;

    @Mock
    ConfigurationFilesManager configurationFilesManager;

    @Before
    public void setUp() throws Exception {
        URL jsonFileUrl = getClass().getResource("autologin-v6.json");
        autoLoginConfiguration = new File(jsonFileUrl.toURI());
    }

    @Test
    public void should_retrieve_credentials_when_autologin_available_for_the_requested_process_and_tenant() throws Exception{
        when(configurationFilesManager.getTenantAutoLoginConfiguration(TENANT_ID)).thenReturn(autoLoginConfiguration);

        AutoLoginCredentials autoLoginCredentials = autoLoginCredentialsFinder.getCredential(new ProcessIdentifier("my process", "2.0"), TENANT_ID);

        assertThat(autoLoginCredentials.getUserName()).isEqualTo("john.bates");
        assertThat(autoLoginCredentials.getPassword()).isEqualTo("bpm");
    }

    @Test
    public void should_retrieve_empty_credentials_when_autologin_not_available_for_the_requested_process_and_tenant() throws Exception{
        when(configurationFilesManager.getTenantAutoLoginConfiguration(TENANT_ID)).thenReturn(autoLoginConfiguration);

        AutoLoginCredentials autoLoginCredentials = autoLoginCredentialsFinder.getCredential(new ProcessIdentifier("process without autologin", "1.0"), TENANT_ID);

        assertThat(autoLoginCredentials).isEqualTo(null);

    }

    @Test
    public void should_retrieve_empty_credentials_when_cannot_read_configuration() throws Exception{
        when(configurationFilesManager.getTenantAutoLoginConfiguration(TENANT_ID)).thenReturn(null);

        AutoLoginCredentials autoLoginCredentials = autoLoginCredentialsFinder.getCredential(new ProcessIdentifier("process without autologin", "1.0"), TENANT_ID);

        assertThat(autoLoginCredentials).isEqualTo(null);
    }
}
