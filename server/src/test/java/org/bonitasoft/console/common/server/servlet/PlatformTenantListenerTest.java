package org.bonitasoft.console.common.server.servlet;

import static org.mockito.Mockito.*;

import java.io.File;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.theme.ThemeType;
import org.bonitasoft.forms.server.ThemeExtractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Emmanuel Duchastenier
 */
@RunWith(MockitoJUnitRunner.class)
public class PlatformTenantListenerTest {

    @Spy
    ThemeExtractor themeExtractor;

    @InjectMocks
    @Spy
    PlatformTenantListener platformTenantListener;

    @Test
    public void initializeDefaultTenant_should_call_extract_current_portal_theme() throws Exception {
        final APISession session = mock(APISession.class);
        doReturn(session).when(platformTenantListener).login();
        doNothing().when(platformTenantListener).logout(session);

        doNothing().when(themeExtractor).retrieveAndExtractCurrentTheme(any(File.class), eq(session), eq(ThemeType.PORTAL));

        platformTenantListener.initializeDefaultTenant(themeExtractor);

        verify(themeExtractor).retrieveAndExtractCurrentTheme(any(File.class), eq(session), eq(ThemeType.PORTAL));
    }
}
