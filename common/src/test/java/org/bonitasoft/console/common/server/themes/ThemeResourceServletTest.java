package org.bonitasoft.console.common.server.themes;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class ThemeResourceServletTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse res;

    @Mock
    HttpSession httpSession;

    private String savedBonitaHomeProperty;

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws
    Exception {

        final ThemeResourceServlet themeResourceServlet = spy(new ThemeResourceServlet());
        savedBonitaHomeProperty = System.getProperty(WebBonitaConstants.BONITA_HOME);
        System.setProperty(WebBonitaConstants.BONITA_HOME, "target/bonita-home");
        when(req.getParameter(themeResourceServlet.getResourceParameterName())).thenReturn("theme");
        when(req.getMethod()).thenReturn("GET");

        when(req.getSession()).thenReturn(httpSession);
        when(req.getParameter("tenant")).thenReturn("1");
        when(themeResourceServlet.getResourcesParentFolder(1L)).thenReturn(new File("."));

        when(req.getParameter("location")).thenReturn("../../../file.txt");
        try {
            themeResourceServlet.service(req, res);
        } catch (final ServletException e) {
            assertTrue(e.getMessage().startsWith("For security reasons, access to this file paths"));
        }
    }

    @After
    public void teardown() throws Exception {
        if (StringUtil.isBlank(savedBonitaHomeProperty)) {
            System.clearProperty(WebBonitaConstants.BONITA_HOME);
        } else {
            System.setProperty(WebBonitaConstants.BONITA_HOME, savedBonitaHomeProperty);
        }
    }
}
