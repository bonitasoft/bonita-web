package org.bonitasoft.livingapps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.console.common.server.page.ResourceRenderer;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationRouterTest {

    public static final String LAYOUT_PAGE_NAME = "layoutPageName";
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

    @Spy
    ResourceRenderer resourceRenderer;

    @Mock
    PageResourceProvider pageResourceProvider;

    @Mock
    BonitaHomeFolderAccessor bonitaHomeFolderAccessor;

    @InjectMocks
    ApplicationRouter applicationRouter;

    @Before
    public void beforeEach() throws Exception {
        given(apiSession.getTenantId()).willReturn(1L);
        given(hsRequest.getContextPath()).willReturn("/bonita");
    }

    @Test
    public void should_redirect_to_home_page_when_accessing_living_application_root() throws Exception {
        given(applicationModel.getApplicationHomePage()).willReturn("home/");
        given(applicationModel.getApplicationLayoutName()).willReturn(LAYOUT_PAGE_NAME);
        given(applicationModelFactory.createApplicationModel("HumanResources")).willReturn(applicationModel);
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/HumanResources");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);
        verify(hsResponse).sendRedirect("home/");
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_an_error_when_the_uri_is_malformed() throws Exception {
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);
    }

/*
    @Test
    public void should_forward_to_themeResource_servlet_when_accessing_a_theme_resource() throws Exception {
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/HumanResources/themeResource");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);

        verify(hsRequest).getRequestDispatcher("/portal/themeResource");
    }

    @Test
    public void should_forward_to_pageResource_servlet_when_accessing_a_page_resource() throws Exception {
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/HumanResources/pageResource");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);

        verify(hsRequest).getRequestDispatcher("/portal/pageResource");
    }
*/

    @Test
    public void should_display_layout_page() throws Exception {
        accessAuthorizedPage("HumanResources", "leavingRequests");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);

        verify(pageRenderer).displayCustomPage(hsRequest, hsResponse, apiSession, applicationModel.getApplicationLayoutName());
    }

    @Test
    public void should_access_Layout_resource() throws Exception {
        accessAuthorizedPage("HumanResources", "AnyPage/css/file.css");
        File layoutFolder = new File("layout");
        given(applicationModel.getApplicationLayoutName()).willReturn("layout");
        given(pageRenderer.getPageResourceProvider("layout", 1L)).willReturn(pageResourceProvider);
        given(pageResourceProvider.getPageDirectory()).willReturn(layoutFolder);
        given(bonitaHomeFolderAccessor.isInFolder(any(File.class), any(File.class))).willReturn(true);

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);

        verify(resourceRenderer).renderFile(hsRequest, hsResponse, new File("layout/resources/css/file.css"));
    }

    @Test
    public void should_access_Theme_resource() throws Exception {
        accessAuthorizedPage("HumanResources", "theme/css/file.css");
        File themeFolder = new File("theme");
        given(applicationModel.getApplicationThemeName()).willReturn("theme");
        given(pageRenderer.getPageResourceProvider("theme", 1L)).willReturn(pageResourceProvider);
        given(pageResourceProvider.getPageDirectory()).willReturn(themeFolder);
        given(bonitaHomeFolderAccessor.isInFolder(any(File.class), any(File.class))).willReturn(true);

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);

        verify(resourceRenderer).renderFile(hsRequest, hsResponse, new File("theme/resources/css/file.css"));
    }

    @Test
    public void should_not_forward_to_the_application_page_template_when_the_page_is_not_in_the_application() throws Exception {
        accessUnknownPage("HumanResources", "leavingRequests");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);

        assertThat(applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor)).isEqualTo(false);
        verify( pageRenderer, never()).displayCustomPage(hsRequest, hsResponse, apiSession, LAYOUT_PAGE_NAME);
    }

    @Test
    public void should_not_forward_to_the_application_page_template_when_user_is_not_authorized() throws Exception {
        accessUnauthorizedPage("HumanResources", "leavingRequests");

        applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor);

        assertThat(applicationRouter.route(hsRequest, hsResponse, apiSession, pageRenderer, resourceRenderer, bonitaHomeFolderAccessor)).isEqualTo(false);
        verify( pageRenderer, never()).displayCustomPage(hsRequest, hsResponse, apiSession, LAYOUT_PAGE_NAME);
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
        given(hsRequest.getRequestURI()).willReturn("/bonita/apps/" + applicationToken + "/" + pageToken + "/");
        given(hsRequest.getPathInfo()).willReturn("/" + applicationToken + "/" + pageToken + "/");
    }
}
