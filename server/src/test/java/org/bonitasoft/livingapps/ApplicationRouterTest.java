package org.bonitasoft.livingapps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationRouterTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    HttpServletRequest hsRequest;

    @Mock
    HttpServletResponse hsResponse;

    @Mock
    APISession apiSession;

    @Mock
    ApplicationModelFactory applicationModelFactory;

    @Mock
    ApplicationModel applicationModel;

    @Mock
    PageRenderer pageRenderer;

    @InjectMocks
    ApplicationRouter applicationRouter;

    @Before
    public void beforeEach() throws Exception {
        given(hsRequest.getContextPath()).willReturn("/bonita");
    }

    @Test
    public void should_redirect_to_home_page_when_accessing_living_application_root() throws Exception {
        given(applicationModel.getApplicationHomePage()).willReturn("HumanResources/home");
        given(applicationModel.getApplicationLayoutName()).willReturn("layoutPageName");
        given(applicationModelFactory.createApplicationModel("HumanResources")).willReturn(applicationModel);
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/HumanResources");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);

        verify(hsResponse).sendRedirect("HumanResources/home");
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_an_error_when_the_uri_is_malformed() throws Exception {
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);
    }

    @Test
    public void should_forward_to_themeResource_servlet_when_accessing_a_theme_resource() throws Exception {
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/HumanResources/themeResource");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);

        verify(hsRequest).getRequestDispatcher("/portal/themeResource");
    }

    @Test
    public void should_forward_to_pageResource_servlet_when_accessing_a_page_resource() throws Exception {
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/HumanResources/pageResource");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);

        verify(hsRequest).getRequestDispatcher("/portal/pageResource");
    }

    @Test
    public void should_display_layout_page() throws Exception {
        accessAuthorizedPage("HumanResources", "leavingRequests");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);

        verify(pageRenderer).displayCustomPage(hsRequest, hsResponse, apiSession,applicationModel.getApplicationLayoutName());
    }

    @Test
    public void should_not_forward_to_the_application_page_template_when_the_page_is_not_in_the_application() throws Exception {
        accessUnknownPage("HumanResources", "leavingRequests");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);

        assertThat(applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer)).isEqualTo(false);
        verify(hsRequest, never()).getRequestDispatcher("/application-template.jsp");
    }

    @Test
    public void should_not_forward_to_the_application_page_template_when_user_is_not_authorized() throws Exception {
        accessUnauthorizedPage("HumanResources", "leavingRequests");

        assertThat(applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer)).isEqualTo(false);
        verify(hsRequest, never()).getRequestDispatcher("/application-template.jsp");
    }

    @Test
    public void should_add_application_to_request_attributes() throws Exception {
        accessAuthorizedPage("HumanResources", "leavingRequests");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);

        verify(hsRequest).setAttribute("application", applicationModel);
    }

    @Test
    public void should_add_custom_page_to_request_attribute() throws Exception {
        accessAuthorizedPage("HumanResources", "leavingRequests");
        final Page customPage = mock(Page.class);
        given(applicationModel.getCustomPage("leavingRequests")).willReturn(customPage);

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer);

        verify(hsRequest).setAttribute("customPage", customPage);
    }

    @Test
    public void should_sort_menu_for_the_given_application() throws Exception {
        //        ArgumentCaptor<SearchOptions> captor = ArgumentCaptor.forClass(SearchOptions.class);
        //
        //        servlet.getApplicationMenus(livingApplication, apiSession);
        //        verify(api).searchApplicationMenus(captor.capture());
        //
        //        assertThat(captor.getValue().getSorts().get(0).getField()).isEqualTo("index");
        //        assertThat(captor.getValue().getSorts().get(0).getOrder()).isEqualTo(Order.ASC);
    }

    @Test
    public void should_none_authorized_page_been_exclude_from_the_menu() throws Exception {
        //        final List<ApplicationMenuImpl> menus = Arrays.asList(
        //                new ApplicationMenuImpl("Page 1", 1, 1L, 1),
        //                new ApplicationMenuImpl("Page 2", 1, 2L, 1));
        //
        //        doReturn(api).when(servlet).getApplicationAPI(apiSession);
        //        doReturn(new SearchResultImpl<ApplicationMenuImpl>(2, menus)).when(api).searchApplicationMenus(any(SearchOptions.class));
        //        doReturn(new ApplicationPageImpl(8, 1, "customPageName1")).when(api).getApplicationPage(1);
        //        doReturn(new ApplicationPageImpl(8, 2, "customPageName2")).when(api).getApplicationPage(2);
        //        doThrow(new ApplicationPageNotFoundException("")).when(api).getApplicationPage(1);
        //
        //        final List<MenuModel> returnedMenus = servlet.getApplicationMenus(livingApplication, apiSession);
        //
        //        assertThat(returnedMenus.size()).isEqualTo(1);
        //        assertThat(returnedMenus.get(0).getName()).isEqualTo("Page 2");
        //        assertThat(returnedMenus.get(0).getToken()).isEqualTo("customPageName2");
    }

    private void accessAuthorizedPage(final String applicationToken, final String pageToken) throws Exception {
        accessPage(applicationToken, pageToken, true, true);
    }

    private void accessUnauthorizedPage(final String applicationToken, final String pageToken) throws Exception {
        accessPage(applicationToken, pageToken, true, false);
    }

    private void accessUnknownPage(final String applicationToken, final String pageToken) throws Exception {
        accessPage(applicationToken, pageToken, true, false);
    }

    private void accessPage(final String applicationToken, final String pageToken, final boolean hasPage, final boolean isAuthorized) throws Exception {
        given(applicationModel.hasPage(pageToken)).willReturn(hasPage);
        given(applicationModel.authorize(apiSession)).willReturn(isAuthorized);
        given(applicationModelFactory.createApplicationModel(applicationToken)).willReturn(applicationModel);
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/" + applicationToken + "/" + pageToken);
    }
}
