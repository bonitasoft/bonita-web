package org.bonitasoft.console.common.server.login;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.ServletException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Vincent Elcrin
 * Date: 30/08/13
 * Time: 17:09
 */
public class TenantIdAccessorTest {

    @Mock
    private HttpServletRequestAccessor request;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testIfWeRetrieveRequestedTenantId() throws Exception {
        doReturn("5").when(request).getTenantId();

        TenantIdAccessor accessor = new TenantIdAccessor(request);

        assertEquals(5L, accessor.getRequestedTenantId());
    }

    @Test(expected = ServletException.class)
    public void testInvalidTenantIdThrowsException() throws Exception {
        doReturn("invalid").when(request).getTenantId();

        new TenantIdAccessor(request);
    }

    @Test
    public void testNullTenantId() throws Exception {
        doReturn(null).when(request).getTenantId();

        TenantIdAccessor accessor = new TenantIdAccessor(request);

        assertEquals(-1L, accessor.getRequestedTenantId());
    }

    @Test
    public void testWeRetrieveDefaultTenantIdWhenRequestedOneIsNull() throws Exception {
        doReturn(null).when(request).getTenantId();

        TenantIdAccessor accessor = spy(new TenantIdAccessor(request));

        // avoid to have to rely on file
        doReturn(6L).when(accessor).getDefaultTenantId();
        assertEquals(6L, accessor.ensureTenantId());
    }
}
