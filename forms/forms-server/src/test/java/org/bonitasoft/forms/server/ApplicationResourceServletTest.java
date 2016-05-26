package org.bonitasoft.forms.server;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.session.APISession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationResourceServletTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse res;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession session;

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws Exception {

        final ApplicationResourceServlet applicationResourceServlet = spy(new ApplicationResourceServlet());
        when(req.getParameter("process")).thenReturn("processUUIDStr");
        when(req.getParameter("location")).thenReturn("../../../file.txt");
        when(req.getMethod()).thenReturn("GET");

        when(req.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("apiSession")).thenReturn(session);
        when(session.getTenantId()).thenReturn(1L);
        when(req.getParameter("tenant")).thenReturn("1");
        when(applicationResourceServlet.getTenantID(req)).thenReturn(1L);

        final File process = new File(WebBonitaConstantsUtils.getInstance(1L).getFormsWorkFolder().getAbsolutePath(), "processUUIDStr");
        process.mkdirs();
        final File processDeployement = new File(process.getAbsolutePath(), "1");
        processDeployement.mkdirs();

        try {
            applicationResourceServlet.service(req, res);
        } catch (final ServletException e) {
            assertTrue(e.getCause().getMessage().startsWith("For security reasons, access to this file paths"));
        }
    }

}
