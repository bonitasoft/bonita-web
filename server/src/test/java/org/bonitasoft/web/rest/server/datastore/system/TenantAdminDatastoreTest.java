package org.bonitasoft.web.rest.server.datastore.system;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.api.TenantManagementAPI;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.system.TenantAdminItem;
import org.bonitasoft.web.rest.server.engineclient.TenantManagementEngineClient;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class TenantAdminDatastoreTest {

    private TenantAdminDatastore tenantAdminDatastore = null;

    private TenantManagementAPI tenantManagementAPI;

    @Before
    public void setUp() throws Exception {
        tenantManagementAPI = mock(TenantManagementAPI.class);
        tenantAdminDatastore = spy(new TenantAdminDatastore(mock(APISession.class)));
        doReturn(new TenantManagementEngineClient(tenantManagementAPI)).when(tenantAdminDatastore).getTenantManagementEngineClient();
    }

    @Test
    public void testUpdateAlreadyInMaintenance() throws Exception {
        when(tenantManagementAPI.isPaused()).thenReturn(true);
        final APIID apiid = APIID.makeAPIID(1L);

        final TenantAdminItem tenantAdminItem = tenantAdminDatastore.update(apiid,
                ImmutableMap.<String, String> of(TenantAdminItem.ATTRIBUTE_IS_PAUSED, Boolean.TRUE.toString()));

        verify(tenantManagementAPI, times(1)).isPaused();
        verify(tenantManagementAPI, times(0)).resume();
        verify(tenantManagementAPI, times(0)).pause();
        assertTrue(tenantAdminItem.isPaused());
    }

    @Test
    public void testUpdateAlreadyInAvailable() throws Exception {
        when(tenantManagementAPI.isPaused()).thenReturn(false);
        final APIID apiid = APIID.makeAPIID(1L);

        final TenantAdminItem tenantAdminItem = tenantAdminDatastore.update(apiid,
                ImmutableMap.<String, String> of(TenantAdminItem.ATTRIBUTE_IS_PAUSED, Boolean.FALSE.toString()));

        verify(tenantManagementAPI, times(1)).isPaused();
        verify(tenantManagementAPI, times(0)).resume();
        verify(tenantManagementAPI, times(0)).pause();
        assertFalse(tenantAdminItem.isPaused());
    }

    @Test
    public void testUpdateGoInMaintenance() throws Exception {
        when(tenantManagementAPI.isPaused()).thenReturn(false);
        final APIID apiid = APIID.makeAPIID(1L);

        final TenantAdminItem tenantAdminItem = tenantAdminDatastore.update(apiid,
                ImmutableMap.<String, String> of(TenantAdminItem.ATTRIBUTE_IS_PAUSED, Boolean.TRUE.toString()));

        verify(tenantManagementAPI, times(1)).isPaused();
        verify(tenantManagementAPI, times(1)).pause();
        assertTrue(tenantAdminItem.isPaused());
    }

    @Test
    public void testUpdateGoInAvailable() throws Exception {
        when(tenantManagementAPI.isPaused()).thenReturn(true);
        final APIID apiid = APIID.makeAPIID(1L);

        final TenantAdminItem tenantAdminItem = tenantAdminDatastore.update(apiid,
                ImmutableMap.<String, String> of(TenantAdminItem.ATTRIBUTE_IS_PAUSED, Boolean.FALSE.toString()));

        verify(tenantManagementAPI, times(1)).isPaused();
        verify(tenantManagementAPI, times(1)).resume();
        assertFalse(tenantAdminItem.isPaused());
    }

    @Test
    public void testGetTenantAvailable() throws Exception {
        when(tenantManagementAPI.isPaused()).thenReturn(false);
        final APIID apiid = APIID.makeAPIID(1L);

        final TenantAdminItem tenantAdminItem = tenantAdminDatastore.get(apiid);

        verify(tenantManagementAPI, times(1)).isPaused();
        verify(tenantManagementAPI, times(0)).resume();
        verify(tenantManagementAPI, times(0)).pause();
        assertFalse(tenantAdminItem.isPaused());
    }

    @Test
    public void testGetTenantInMaintenance() throws Exception {
        when(tenantManagementAPI.isPaused()).thenReturn(true);
        final APIID apiid = APIID.makeAPIID(1L);

        final TenantAdminItem tenantAdminItem = tenantAdminDatastore.get(apiid);

        verify(tenantManagementAPI, times(1)).isPaused();
        verify(tenantManagementAPI, times(0)).resume();
        verify(tenantManagementAPI, times(0)).pause();
        assertTrue(tenantAdminItem.isPaused());
    }
}
