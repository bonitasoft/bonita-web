package org.bonitasoft.web.rest.server.engineclient;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TenantManagementEngineClientTest {

    @Mock
    private TenantAdministrationAPI tenantAdministrationAPI;

    @InjectMocks
    private TenantManagementEngineClient tenantManagementEngineClient;

    private void pauseTenant() {
        when(tenantAdministrationAPI.isPaused()).thenReturn(true);
    }

    private void resumeTenant() {
        when(tenantAdministrationAPI.isPaused()).thenReturn(false);
    }

    @Before
    public void startTenant() {
        resumeTenant();
    }

    @Test
    public void pauseTenant_pause_the_tenant() throws Exception {

        tenantManagementEngineClient.pauseTenant();

        verify(tenantAdministrationAPI).pause();
    }

    @Test
    public void pauseTenant_dont_pause_tenant_if_it_is_already_paused() throws Exception {
        pauseTenant();

        tenantManagementEngineClient.pauseTenant();

        verify(tenantAdministrationAPI, never()).pause();
    }

    @Test(expected = APIException.class)
    public void pauseTenant_throw_APIException_if_error_occurs_when_pausing_tenant() throws Exception {
        doThrow(new UpdateException(new NullPointerException())).when(tenantAdministrationAPI).pause();

        tenantManagementEngineClient.pauseTenant();
    }

    @Test
    public void resumeTenant_resume_the_tenant() throws Exception {
        pauseTenant();

        tenantManagementEngineClient.resumeTenant();

        verify(tenantAdministrationAPI).resume();
    }

    @Test
    public void resumeTenant_dont_resume_tenant_if_it_not_paused() throws Exception {

        tenantManagementEngineClient.resumeTenant();

        verify(tenantAdministrationAPI, never()).resume();
    }

    @Test(expected = APIException.class)
    public void resumeTenant_throw_APIException_if_error_occurs_when_resuming_tenant() throws Exception {
        pauseTenant();
        doThrow(new UpdateException(new NullPointerException())).when(tenantAdministrationAPI).resume();

        tenantManagementEngineClient.resumeTenant();
    }
}
