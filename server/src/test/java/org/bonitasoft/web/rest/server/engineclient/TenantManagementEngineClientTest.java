package org.bonitasoft.web.rest.server.engineclient;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.business.data.BusinessDataRepositoryDeploymentException;
import org.bonitasoft.engine.business.data.InvalidBusinessDataModelException;
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
    public void install_should_deploy_data_model_content() throws Exception {
        final byte[] expectedContent = "".getBytes();

        tenantManagementEngineClient.installBusinessDataModel(expectedContent);

        verify(tenantAdministrationAPI).installBusinessDataModel(expectedContent);
    }

    @Test(expected = APIException.class)
    public void install_should_throw_APIException_if_InvalidBusinessDataModelException_occurs() throws Exception {
        doThrow(new InvalidBusinessDataModelException(new NullPointerException())).when(tenantAdministrationAPI).installBusinessDataModel(any(byte[].class));

        tenantManagementEngineClient.installBusinessDataModel("".getBytes());
    }

    @Test(expected = APIException.class)
    public void install_should_throw_APIException_if_BusinessDataRepositoryDeploymentException_occurs() throws Exception {
        doThrow(new BusinessDataRepositoryDeploymentException(new NullPointerException())).when(tenantAdministrationAPI)
        .installBusinessDataModel(any(byte[].class));

        tenantManagementEngineClient.installBusinessDataModel("".getBytes());
    }

    @Test
    public void uninstall_should_undeploy_business_data_model() throws Exception {
        pauseTenant();

        tenantManagementEngineClient.uninstallBusinessDataModel();

        verify(tenantAdministrationAPI).uninstallBusinessDataModel();
    }

    @Test(expected = APIException.class)
    public void uninstall_should_throw_APIException_if_error_occurs() throws Exception {
        pauseTenant();

        doThrow(new BusinessDataRepositoryDeploymentException(new NullPointerException())).when(tenantAdministrationAPI).uninstallBusinessDataModel();

        tenantManagementEngineClient.uninstallBusinessDataModel();
    }

    @Test(expected = APIException.class)
    public void cant_uninstall_bdm_if_tenant_is_not_paused() throws Exception {

        tenantManagementEngineClient.uninstallBusinessDataModel();
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
