package org.bonitasoft.console.common.server.themes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
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

    @Spy
    ThemeResourceServlet themeResourceServlet = new ThemeResourceServlet();

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws Exception {
        final ThemeResourceServlet themeResourceServlet = spy(new ThemeResourceServlet());

        when(req.getParameter(themeResourceServlet.getResourceParameterName())).thenReturn("theme");
        doReturn("GET").when(req).getMethod();

        doReturn(httpSession).when(req).getSession();
        doReturn("1").when(req).getParameter("tenant");
        doReturn(new File(".")).when(themeResourceServlet).getResourcesParentFolder(1L);

        doReturn("../../../file.txt").when(req).getParameter("location");

        try {
            themeResourceServlet.service(req, res);
        } catch (final ServletException e) {
            assertThat(e.getMessage()).startsWith("For security reasons, access to this file paths");
        }
    }
}
