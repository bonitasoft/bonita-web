package org.bonitasoft.console.common.server.page;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */

@RunWith(MockitoJUnitRunner.class)
public class PageResourceServletTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse res;

    @Mock
    HttpSession httpSession;

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws Exception {

        final PageResourceServlet pageResourceServlet = spy(new PageResourceServlet());
        when(req.getParameter(pageResourceServlet.getResourceParameterName())).thenReturn("name");
        when(req.getMethod()).thenReturn("GET");

        when(req.getSession()).thenReturn(httpSession);
        when(req.getParameter("tenant")).thenReturn("1");
        when(pageResourceServlet.getResourcesParentFolder(1L)).thenReturn(new File("."));

        when(req.getParameter("location")).thenReturn("../../../file.txt");
        try {
            pageResourceServlet.service(req, res);
        } catch (final ServletException e) {
            assertTrue(e.getMessage().startsWith("For security reasons, access to this file paths"));
        }
    }

}
