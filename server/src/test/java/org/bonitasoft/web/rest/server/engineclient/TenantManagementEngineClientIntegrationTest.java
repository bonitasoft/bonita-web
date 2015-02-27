package org.bonitasoft.web.rest.server.engineclient;

import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.junit.Test;

public class TenantManagementEngineClientIntegrationTest extends AbstractConsoleTest {

    private TenantManagementEngineClient tenantManagementEngineClient;

    @Override
    public void consoleTestSetUp() throws Exception {
        final EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor(getInitiator().getSession()));
        tenantManagementEngineClient = engineClientFactory.createTenantManagementEngineClient();
    }

    @Override
    protected TestUser getInitiator() {
        return getContext().getAdminUser();
    }

    @Test
    public void undeploy_throw_no_exception_if_no_bdm_is_deployed() throws Exception {
        tenantManagementEngineClient.pauseTenant();

        tenantManagementEngineClient.uninstallBusinessDataModel();

        tenantManagementEngineClient.resumeTenant();
    }

}
