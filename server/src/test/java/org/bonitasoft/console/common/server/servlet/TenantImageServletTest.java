package org.bonitasoft.console.common.server.servlet;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
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
public class TenantImageServletTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse res;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession session;

    private String savedBonitaHomeProperty;

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws
    Exception {

        final TenantImageServlet tenantImageServlet = spy(new TenantImageServlet());
        savedBonitaHomeProperty = System.getProperty(WebBonitaConstants.BONITA_HOME);
        System.setProperty(WebBonitaConstants.BONITA_HOME, "target/bonita-home");
        when(req.getParameter(ImageDownloadServlet.SRC_PARAM)).thenReturn("../../../file.txt");

        tenantImageServlet.setDirectoryPath("./temp");

        when(req.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(SessionUtil.API_SESSION_PARAM_KEY)).thenReturn(session);
        when(session.getTenantId()).thenReturn(1L);

        try {
            tenantImageServlet.doGet(req, res);
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
