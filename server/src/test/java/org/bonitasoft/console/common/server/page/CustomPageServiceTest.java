package org.bonitasoft.console.common.server.page;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.session.APISession;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import groovy.lang.GroovyClassLoader;

@RunWith(MockitoJUnitRunner.class)
public class CustomPageServiceTest {

    @Spy
    private final CustomPageService customPageService = new CustomPageService();

    @Mock
    CompoundPermissionsMapping compoundPermissionsMapping;

    @Mock
    ResourcesPermissionsMapping resourcesPermissionsMapping;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Mock
    APISession apiSession;

    @Mock
    PageResourceProvider pageResourceProvider;

    @Mock
    PageAPI pageAPI;

    @Test
    public void should_load_page_return_page_impl() throws Exception {
        // Given
        when(apiSession.getTenantId()).thenReturn(0L);
        final File pageFile = new File(getClass().getResource("/Index.groovy").toURI());
        final File pageDir = pageFile.getParentFile();
        assertThat(pageFile).as("no file " + pageFile.getAbsolutePath()).exists().canRead();
        when(pageResourceProvider.getPageDirectory()).thenReturn(pageDir);
        doReturn(pageFile).when(customPageService).getGroovyPageFile(any(File.class));
        final File pageLibDir = new File(pageFile.getParentFile(), File.separator + "lib");
        doReturn(pageLibDir).when(customPageService).getCustomPageLibDirectory(any(File.class));
        doReturn(Thread.currentThread().getContextClassLoader()).when(customPageService).getParentClassloader(anyString(), any(File.class), anyString());
        final Page mockedPage = mock(Page.class);
        when(mockedPage.getLastModificationDate()).thenReturn(new Date(0L));
        doReturn(pageAPI).when(customPageService).getPageAPI(apiSession);
        when(pageAPI.getPageByName("")).thenReturn(mockedPage);

        // When
        final GroovyClassLoader classloader = customPageService.getPageClassloader(apiSession, pageResourceProvider);
        final Class<PageController> pageClass = customPageService.registerPage(classloader, pageResourceProvider);
        final PageController pageController = customPageService.loadPage(pageClass);

        // Then
        assertNotNull(pageController);
    }

    @Test
    public void should_load_rest_api_page_return_api_impl() throws Exception {
        // Given
        when(apiSession.getTenantId()).thenReturn(0L);
        final File pageFile = new File(getClass().getResource("/IndexRestApi.groovy").toURI());
        final File pageDir = pageFile.getParentFile();
        assertThat(pageFile).as("no file " + pageFile.getAbsolutePath()).exists().canRead();
        when(pageResourceProvider.getPageDirectory()).thenReturn(pageDir);
        doReturn(pageFile).when(customPageService).getGroovyPageFile(any(File.class));
        final File pageLibDir = new File(pageFile.getParentFile(), File.separator + "lib");
        doReturn(pageLibDir).when(customPageService).getCustomPageLibDirectory(any(File.class));
        doReturn(Thread.currentThread().getContextClassLoader()).when(customPageService).getParentClassloader(anyString(), any(File.class), anyString());
        final Page mockedPage = mock(Page.class);
        when(mockedPage.getLastModificationDate()).thenReturn(new Date(0L));
        doReturn(pageAPI).when(customPageService).getPageAPI(apiSession);
        when(pageAPI.getPageByName("")).thenReturn(mockedPage);

        // When
        final GroovyClassLoader classloader = customPageService.getPageClassloader(apiSession, pageResourceProvider);
        final Class<RestApiController> restApiControllerClass = customPageService.registerRestApiPage(classloader, pageResourceProvider);
        final RestApiController restApiController = customPageService.loadRestApiPage(restApiControllerClass);

        // Then
        assertNotNull(restApiController);
    }

    @Test
    public void should_retrievePageZipContent_save_it_in_bonita_home() throws Exception {
        // Given
        final Page mockedPage = mock(Page.class);
        when(mockedPage.getId()).thenReturn(1l);
        when(mockedPage.getName()).thenReturn("page1");
        when(apiSession.getTenantId()).thenReturn(0L);
        doReturn(pageAPI).when(customPageService).getPageAPI(apiSession);
        final byte[] zipFile = IOUtils.toByteArray(getClass().getResourceAsStream("/page.zip"));
        when(pageAPI.getPageContent(1l)).thenReturn(zipFile);
        when(pageResourceProvider.getPage(pageAPI)).thenReturn(mockedPage);
        when(pageResourceProvider.getTempPageFile()).thenReturn(new File("target/bonita/home/client/tenant/1/temp"));
        when(pageResourceProvider.getPageDirectory()).thenReturn(new File("target/bonita/home/client/tenants/1/pages/page1"));

        // When
        customPageService.retrievePageZipContent(apiSession, pageResourceProvider);

        // Validate
        assertNotNull(pageResourceProvider.getPageDirectory().listFiles());
    }

    @Test
    public void should_get_Custom_Page_permissions_from_PageProperties() throws Exception {
        // Given
        final String fileContent = "name=customPage1\n" +
                "resources=[GET|identity/user, PUT|identity/user]";

        final File pagePropertiesFile = File.createTempFile("page.properties", ".tmp");
        IOUtils.write(fileContent.getBytes(), new FileOutputStream(pagePropertiesFile));
        doReturn(new HashSet<String>(Arrays.asList("Organization Visualization"))).when(resourcesPermissionsMapping).getPropertyAsSet("GET|identity/user");
        doReturn(new HashSet<String>(Arrays.asList("Organization Visualization", "Organization Managment")))
                .when(resourcesPermissionsMapping).getPropertyAsSet("PUT|identity/user");

        // When
        final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(pagePropertiesFile, resourcesPermissionsMapping, true);

        // Then
        assertThat(customPagePermissions).contains("Organization Visualization", "Organization Managment");
    }

    @Test
    public void should_not_return_it_when_unkown_resource_is_declared_in_PageProperties_and_flag_false() throws Exception {
        // Given
        final String fileContent = "name=customPage1\n" +
                "resources=[GET|unkown/resource, PUT|identity/user]";

        final File pagePropertiesFile = File.createTempFile("page.properties", ".tmp");
        IOUtils.write(fileContent.getBytes(), new FileOutputStream(pagePropertiesFile));
        doReturn(Collections.emptySet()).when(resourcesPermissionsMapping).getPropertyAsSet("GET|unkown/resource");
        doReturn(new HashSet<String>(Arrays.asList("Organization Visualization", "Organization Managment")))
                .when(resourcesPermissionsMapping).getPropertyAsSet("PUT|identity/user");

        // When
        final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(pagePropertiesFile, resourcesPermissionsMapping, false);

        // Then
        assertThat(customPagePermissions).containsOnly("Organization Visualization", "Organization Managment");
    }

    @Test
    public void should_return_it_when_unkown_resource_is_declared_in_PageProperties_and_flag_true() throws Exception {
        // Given
        final String fileContent = "name=customPage1\n" +
                "resources=[GET|unkown/resource, GET|identity/user, PUT|identity/user]";

        final File pagePropertiesFile = File.createTempFile("page.properties", ".tmp");
        IOUtils.write(fileContent.getBytes(), new FileOutputStream(pagePropertiesFile));
        doReturn(new HashSet<String>(Arrays.asList("Organization Visualization"))).when(resourcesPermissionsMapping).getPropertyAsSet("GET|identity/user");
        doReturn(new HashSet<String>(Arrays.asList("Organization Visualization", "Organization Managment")))
                .when(resourcesPermissionsMapping).getPropertyAsSet("PUT|identity/user");

        // When
        final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(pagePropertiesFile, resourcesPermissionsMapping, true);

        // Then
        assertThat(customPagePermissions).contains("<GET|unkown/resource>", "Organization Visualization", "Organization Managment");
    }

    @Test
    public void should_get_Custom_Page_permissions_to_CompoundPermissions_with_empty_list() throws Exception {
        // Given
        final String fileContent = "name=customPage1\n" +
                "resources=[]";

        final File pagePropertiesFile = File.createTempFile("page.properties", ".tmp");
        IOUtils.write(fileContent.getBytes(), new FileOutputStream(pagePropertiesFile));
        doReturn(Collections.emptySet()).when(resourcesPermissionsMapping).getPropertyAsSet("GET|unkown/resource");

        // When
        final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(pagePropertiesFile, resourcesPermissionsMapping, false);

        // Then
        assertTrue(customPagePermissions.equals(new HashSet<String>()));
    }

    @Test
    public void should_add_Custom_Page_permissions_to_CompoundPermissions() throws Exception {
        // Given
        final HashSet<String> customPagePermissions = new HashSet<String>(Arrays.asList("Organization Visualization", "Organization Managment"));

        // When
        customPageService.addPermissionsToCompoundPermissions("customPage1", customPagePermissions, compoundPermissionsMapping, resourcesPermissionsMapping);

        // Then
        verify(compoundPermissionsMapping).setPropertyAsSet("customPage1", customPagePermissions);
    }

}
