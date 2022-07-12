package org.bonitasoft.console.common.server.auth.impl.standard;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class StandardAuthenticationManagerImplTest {

    private MockHttpServletRequest request;
    private HttpServletRequestAccessor requestAccessor;

    private StandardAuthenticationManagerImpl standardLoginManagerImpl = spy(new StandardAuthenticationManagerImpl());


    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setContextPath("bonita");
        doReturn(1L).when(standardLoginManagerImpl).getDefaultTenantId();

        requestAccessor = new HttpServletRequestAccessor(request);
    }

    @Test
    public void testGetSimpleLoginpageURL() throws Exception {
        String redirectUrl = "%2Fapps%2FappDirectoryBonita";

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?redirectUrl=%2Fapps%2FappDirectoryBonita");
    }

    @Test
    public void testGetLoginpageURLWithLocale() throws Exception {
        String redirectUrl = "%2Fapps%2FappDirectoryBonita";
        request.setParameter("_l", "es");

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?_l=es&redirectUrl=%2Fapps%2FappDirectoryBonita");
    }

    @Test
    public void testGetLoginpageURLFromPortal() throws Exception {
        String redirectUrl = "%2Fapps%2FappDirectoryBonita";
        request.setServletPath("/portal/");

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?redirectUrl=%2Fapps%2FappDirectoryBonita");
    }
}
