package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.bdm.BusinessObjectModelConverter;
import org.bonitasoft.engine.bdm.model.BusinessObject;
import org.bonitasoft.engine.bdm.model.BusinessObjectModel;
import org.bonitasoft.engine.bdm.model.field.FieldType;
import org.bonitasoft.engine.bdm.model.field.SimpleField;
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

public class FormsResourcesUtilsIT extends AbstractJUnitTest {

    private static final String PERSON_QUALIFIED_NAME = "com.company.pojo.Person";

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
                .done();

        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        final ProcessDefinition processDefinition = processAPI.deploy(businessArchive);
        final ProcessDeploymentInfo processDeploymentInfo = processAPI.getProcessDeploymentInfo(processDefinition.getId());

        FormsResourcesUtils.retrieveApplicationFiles(getSession(), processDeploymentInfo.getProcessId(), processDeploymentInfo.getDeploymentDate());

        final FormsResourcesUtils formsResourcesUtilsInstance = new FormsResourcesUtils();
        assertNotNull(formsResourcesUtilsInstance.getProcessClassLoader(getSession(), processDefinition.getId()));
    }

    @Test
    public void testGetProcessClassLoaderReturnSameClassLoaderWhenCalledSeveralTimes() throws Exception {
        final ProcessDefinitionBuilder processDefinitionBuilder = new ProcessDefinitionBuilder();
        processDefinitionBuilder.createNewInstance("Test process", "3.0");
        processDefinitionBuilder.addActor("actorName").addUserTask("taskName", "actorName");
        // upload process archive
        final BusinessArchive businessArchive = new BusinessArchiveBuilder().createNewBusinessArchive()
                .setProcessDefinition(processDefinitionBuilder.done())
                .addExternalResource(new BarResource("forms/lib/messages.jar", IOUtils.toByteArray(this.getClass().getResourceAsStream("/messages.jar"))))
                .done();

        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        final ProcessDefinition processDefinition = processAPI.deploy(businessArchive);
        final ProcessDeploymentInfo processDeploymentInfo = processAPI.getProcessDeploymentInfo(processDefinition.getId());

        FormsResourcesUtils.retrieveApplicationFiles(getSession(), processDeploymentInfo.getProcessId(), processDeploymentInfo.getDeploymentDate());

        final FormsResourcesUtils formsResourcesUtilsInstance = new FormsResourcesUtils();
        final ClassLoader processClassloader = formsResourcesUtilsInstance.getProcessClassLoader(getSession(), processDefinition.getId());
        assertNotNull(processClassloader);

        final FormsResourcesUtils formsResourcesUtilsInstance2 = new FormsResourcesUtils();
        final ClassLoader processClassloader2 = formsResourcesUtilsInstance2.getProcessClassLoader(getSession(), processDefinition.getId());
        assertNotNull(processClassloader2);

        assertEquals(processClassloader, processClassloader2);
    }

    @Test
    public void testGetProcessClassLoaderReturnSameClassLoaderWhenCalledSeveralTimesWithBDM() throws Exception {

        final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
        APISession technicalUserSession = loginAPI.login(TenantsManagementUtils.getTechnicalUserUsername(),
                TenantsManagementUtils.getTechnicalUserPassword());
        TenantAdministrationAPI tenantAdministrationAPI = TenantAPIAccessor.getTenantAdministrationAPI(technicalUserSession);
        final BusinessObjectModelConverter converter = new BusinessObjectModelConverter();
        final byte[] zip = converter.zip(createBusinessObjectModel());
        tenantAdministrationAPI.pause();
        tenantAdministrationAPI.cleanAndUninstallBusinessDataModel();
        tenantAdministrationAPI.installBusinessDataModel(zip);
        tenantAdministrationAPI.resume();
        loginAPI.logout(technicalUserSession);

        try {
            final ProcessDefinitionBuilder processDefinitionBuilder = new ProcessDefinitionBuilder();
            processDefinitionBuilder.createNewInstance("Test process", "4.0");
            processDefinitionBuilder.addActor("actorName").addUserTask("taskName", "actorName");
            // upload process archive
            final BusinessArchive businessArchive = new BusinessArchiveBuilder().createNewBusinessArchive()
                    .setProcessDefinition(processDefinitionBuilder.done())
                    .addExternalResource(new BarResource("forms/lib/messages.jar", IOUtils.toByteArray(this.getClass().getResourceAsStream("/messages.jar"))))
                    .done();

            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getSession());
            final ProcessDefinition processDefinition = processAPI.deploy(businessArchive);
            final ProcessDeploymentInfo processDeploymentInfo = processAPI.getProcessDeploymentInfo(processDefinition.getId());

            FormsResourcesUtils.retrieveApplicationFiles(getSession(), processDeploymentInfo.getProcessId(), processDeploymentInfo.getDeploymentDate());

            final FormsResourcesUtils formsResourcesUtilsInstance = new FormsResourcesUtils();
            final ClassLoader processClassloader = formsResourcesUtilsInstance.getProcessClassLoader(getSession(), processDefinition.getId());

            assertThat(processClassloader).as("should retrieve class loader").isNotNull();
            assertThat(processClassloader.loadClass(PERSON_QUALIFIED_NAME)).as("should load BDM class").isNotNull();

            final FormsResourcesUtils formsResourcesUtilsInstance2 = new FormsResourcesUtils();
            final ClassLoader processClassloader2 = formsResourcesUtilsInstance2.getProcessClassLoader(getSession(), processDefinition.getId());

            assertThat(processClassloader2).as("should retrieve class loader").isNotNull().isEqualTo(processClassloader2);
            assertThat(processClassloader2.loadClass(PERSON_QUALIFIED_NAME)).as("should load BDM class").isNotNull();

        } finally {
            technicalUserSession = loginAPI.login(TenantsManagementUtils.getTechnicalUserUsername(),
                    TenantsManagementUtils.getTechnicalUserPassword());
            tenantAdministrationAPI = TenantAPIAccessor.getTenantAdministrationAPI(technicalUserSession);
            tenantAdministrationAPI.pause();
            tenantAdministrationAPI.uninstallBusinessDataModel();
            tenantAdministrationAPI.resume();
            loginAPI.logout(technicalUserSession);
        }
    }

    protected BusinessObjectModel createBusinessObjectModel() {
        final BusinessObjectModel businessObjectModel = new BusinessObjectModel();
        final BusinessObject person = new BusinessObject();
        person.setQualifiedName(PERSON_QUALIFIED_NAME);
        final SimpleField name = new SimpleField();
        name.setName("name");
        name.setType(FieldType.STRING);
        name.setLength(Integer.valueOf(10));
        person.addField(name);
        businessObjectModel.addBusinessObject(person);
        return businessObjectModel;
    }
}
