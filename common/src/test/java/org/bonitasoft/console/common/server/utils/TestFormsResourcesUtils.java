package org.bonitasoft.console.common.server.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.bar.BarResource;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.AbstractJUnitTest;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.junit.Test;

public class TestFormsResourcesUtils extends AbstractJUnitTest {

    protected APISession getSession() {
        return getInitiator().getSession();
    }

    @Override
    protected TestToolkitCtx getContext() {
        return TestToolkitCtx.getInstance();
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Override
    protected void testSetUp() throws Exception {
        TenantsManagementUtils.addDirectoryForTenant(TenantsManagementUtils.getDefaultTenantId());
    }

    @Override
    protected void testTearDown() throws Exception {
    }

    @Test
    public void testGetProcessClassLoaderReturnNullIfNoDependencies() throws Exception {
        // upload process archive
        final BusinessArchive businessArchive = new BusinessArchiveBuilder().createNewBusinessArchive()
                .setProcessDefinition(new ProcessDefinitionBuilder().createNewInstance("Test process", "1.0").done()).done();

        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        final ProcessDefinition processDefinition = processAPI.deploy(businessArchive);
        final ProcessDeploymentInfo processDeploymentInfo = processAPI.getProcessDeploymentInfo(processDefinition.getId());
        FormsResourcesUtils.retrieveApplicationFiles(getSession(), processDefinition.getId(), processDeploymentInfo.getDeploymentDate());

        final FormsResourcesUtils formsResourcesUtilsInstance = new FormsResourcesUtils();
        assertNull(formsResourcesUtilsInstance.getProcessClassLoader(getSession(), processDefinition.getId()));
    }

    @Test
    public void testGetProcessClassLoaderReturnNewClassLoaderWithDependencies() throws Exception {
        final ProcessDefinitionBuilder processDefinitionBuilder = new ProcessDefinitionBuilder();
        processDefinitionBuilder.createNewInstance("Test process", "2.0");
        processDefinitionBuilder.addActor("actorName").addUserTask("taskName", "actorName");
        // upload process archive
        final BusinessArchive businessArchive = new BusinessArchiveBuilder().createNewBusinessArchive()
                .setProcessDefinition(processDefinitionBuilder.done())
                .addExternalResource(new BarResource("forms/lib/messages.jar", IOUtils.toByteArray(this.getClass().getResourceAsStream("/messages.jar"))))
                .addExternalResource(new BarResource("forms/messages.jar", IOUtils.toByteArray(this.getClass().getResourceAsStream("/messages.jar"))))
                .addClasspathResource(new BarResource("messages.jar", IOUtils.toByteArray(this.getClass().getResourceAsStream("/messages.jar"))))
                .done();

        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        final ProcessDefinition processDefinition = processAPI.deploy(businessArchive);
        final ProcessDeploymentInfo processDeploymentInfo = processAPI.getProcessDeploymentInfo(processDefinition.getId());

        final Map<String, byte[]> resources = processAPI.getProcessResources(processDefinition.getId(), ".*");
        assertThat(resources.isEmpty(), is(false));

        FormsResourcesUtils.retrieveApplicationFiles(getSession(), processDeploymentInfo.getProcessId(), processDeploymentInfo.getDeploymentDate());

        final FormsResourcesUtils formsResourcesUtilsInstance = new FormsResourcesUtils();
        assertNotNull(formsResourcesUtilsInstance.getProcessClassLoader(getSession(), processDefinition.getId()));
    }
}
