package org.bonitasoft.console.common.server.login.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class LogoutServletTest {

    LogoutServlet logoutServlet = new LogoutServlet();

    @Test
    public void testBuildEmptyLoginPageUrl() throws Exception {
        String loginPage = logoutServlet.buildLoginPageUrl("");
        assertThat(loginPage).isEqualToIgnoringCase("");
    }

    @Test
    public void testBuildLoginPageUrlFromMaliciousRedirectShouldReturnBrokenUrl() throws Exception {
        String loginPage = logoutServlet.buildLoginPageUrl("http://www.test.com");
        assertThat(loginPage).isEqualToIgnoringCase(":.test.com");
    }

    @Test
    public void testBuildLoginPageUrlFromMaliciousRedirectShouldReturnBrokenUrl2() throws Exception {
        String loginPage = logoutServlet.buildLoginPageUrl("test.com");
        assertThat(loginPage).isEqualToIgnoringCase("test.com");
    }

    @Test
    public void testBuildLoginPageUrlFromCorrectRedirectShouldReturnCorrectUrl() throws Exception {
        String loginPage = logoutServlet.buildLoginPageUrl("portal/homepage?p=test#poutpout");
        assertThat(loginPage).isEqualToIgnoringCase("portal/homepage?p=test#poutpout");
    }

}
