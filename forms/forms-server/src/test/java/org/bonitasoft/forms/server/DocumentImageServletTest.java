package org.bonitasoft.forms.server;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.session.APISession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentImageServletTest {

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

        final DocumentImageServlet documentImageServlet = spy(new DocumentImageServlet());
        when(req.getParameter(DocumentImageServlet.FILE_PATH_PARAM)).thenReturn("../../..");
        when(req.getParameter(DocumentImageServlet.FILE_NAME_PARAM)).thenReturn("file.txt");
        when(req.getParameter(DocumentImageServlet.RESOURCE_FILE_NAME_PARAM)).thenReturn("resources");
        when(req.getParameter(DocumentImageServlet.DOCUMENT_ID_PARAM)).thenReturn("1");
        when(req.getParameter(DocumentImageServlet.CONTENT_STORAGE_ID_PARAM)).thenReturn("2");

        when(req.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(DocumentImageServlet.API_SESSION_PARAM_KEY)).thenReturn(session);
        when(session.getTenantId()).thenReturn(1L);

        try {
            documentImageServlet.doGet(req, res);
        } catch (final ServletException e) {
            assertTrue(e.getMessage().startsWith("Unauthorized access to the file"));
        }
    }
}
