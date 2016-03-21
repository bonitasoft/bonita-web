package org.bonitasoft.console.common.server.login;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

@RunWith(MockitoJUnitRunner.class)
public class TenantIdAccessorTest {

    @Mock
    private HttpServletRequestAccessor requestAccessor;
    private TenantIdAccessor tenantIdAccessor;
    private MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        when(this.requestAccessor.asHttpServletRequest()).thenReturn(request);
        tenantIdAccessor =  new TenantIdAccessor(requestAccessor);
    }

    @Test
    public void testIfWeRetrieveRequestedTenantId() throws Exception {
        doReturn("5").when(requestAccessor).getTenantId();

        TenantIdAccessor accessor = new TenantIdAccessor(requestAccessor);

        assertEquals(5L, accessor.getRequestedTenantId());
    }

    @Test(expected = ServletException.class)
    public void testInvalidTenantIdThrowsException() throws Exception {
        doReturn("invalid").when(requestAccessor).getTenantId();

        new TenantIdAccessor(requestAccessor).ensureTenantId();
    }

    @Test
    public void testNullTenantId() throws Exception {
        doReturn(null).when(requestAccessor).getTenantId();

        TenantIdAccessor accessor = new TenantIdAccessor(requestAccessor);

        assertEquals(-1L, accessor.getRequestedTenantId());
    }

    @Test
    public void testWeRetrieveDefaultTenantIdWhenRequestedOneIsNull() throws Exception {
        doReturn(null).when(requestAccessor).getTenantId();

        TenantIdAccessor accessor = spy(new TenantIdAccessor(requestAccessor));

        // avoid to have to rely on file
        doReturn(6L).when(accessor).getDefaultTenantId();
        assertEquals(6L, accessor.ensureTenantId());
    }

    @Test
    public void should_retrieve_tenant_id_from_request_when_set_in_request_parameters() throws Exception {
        request.setCookies(new Cookie("bonita.tenant", "123"));
        doReturn("5").when(requestAccessor).getTenantId();

        long tenantId = tenantIdAccessor.getTenantIdFromRequestOrCookie();

        assertThat(tenantId).isEqualTo(5);
    }

    @Test
    public void should_retrieve_tenant_id_from_cookies_when_not_set_in_request_parameters() throws Exception {
        request.setCookies(new Cookie("bonita.tenant", "123"));

        long tenantId = tenantIdAccessor.getTenantIdFromRequestOrCookie();

        assertThat(tenantId).isEqualTo(123);
    }
}
