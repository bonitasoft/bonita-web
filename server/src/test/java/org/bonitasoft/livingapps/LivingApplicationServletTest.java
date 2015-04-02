package org.bonitasoft.livingapps;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.engine.business.application.ApplicationPageNotFoundException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.livingapps.exception.CreationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LivingApplicationServletTest {

    @Mock
    HttpServletRequest hsRequest;

    @Mock
    HttpServletResponse hsResponse;

    @Mock
    APISession session;

    @Mock
    ApplicationRouter router;

    @Mock
    PageRenderer pageRenderer;

    @Spy
    LivingApplicationServlet servlet;

    @Before
    public void beforeEach() throws Exception {
        doReturn(router).when(servlet).createApplicationRouter(any(APISession.class));
        doReturn(pageRenderer).when(servlet).getPageRenderer();
        doReturn(session).when(servlet).getSession(hsRequest);
    }

    @Test
    public void should_send_error_404_when_the_application_page_is_not_found() throws Exception {
        given(router.route(hsRequest, hsResponse, session, pageRenderer))
        .willThrow(new ApplicationPageNotFoundException(""));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(404);
    }

    @Test
    public void should_send_error_404_when_the_custom_page_is_not_associated_to_application() throws Exception {
        given(router.route(hsRequest, hsResponse, session, pageRenderer))
        .willThrow(new ApplicationPageNotFoundException(""));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(404);
    }

    @Test
    public void should_send_error_404_when_the_custom_page_is_not_existing() throws Exception {
        given(router.route(hsRequest, hsResponse, session, pageRenderer))
        .willThrow(new PageNotFoundException(""));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(404);
    }

    @Test
    public void should_send_error_500_on_searchException() throws Exception {
        given(router.route(hsRequest, hsResponse, session, pageRenderer))
        .willThrow(new CreationException(""));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(404);
    }

    @Test
    public void should_send_error_500_on_BonitaHomeNotSetException() throws Exception {
        given(servlet.createApplicationRouter(session))
        .willThrow(new BonitaHomeNotSetException(""));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(500);
    }

    @Test
    public void should_send_error_500_on_ServerAPIException() throws Exception {
        given(servlet.createApplicationRouter(session))
        .willThrow(new ServerAPIException(new Exception("")));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(500);
    }

    @Test
    public void should_send_error_500_on_UnknownAPITypeException() throws Exception {
        given(servlet.createApplicationRouter(session))
        .willThrow(new UnknownAPITypeException(""));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(500);
    }

    @Test
    public void should_send_error_404_when_the_page_is_not_found() throws Exception {
        given(router.route(hsRequest, hsResponse, session, pageRenderer))
        .willThrow(new ApplicationPageNotFoundException(""));

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(404);
    }

    @Test
    public void should_send_error_404_when_the_route_is_false() throws Exception {
        given(router.route(hsRequest, hsResponse, session, pageRenderer))
                .willReturn(false);

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(404);
    }

}
