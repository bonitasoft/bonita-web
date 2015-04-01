/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.page;

import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.utils.TenantFolder;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.console.common.server.utils.UnzipUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.io.IOUtil;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Fabio Lombardi
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class PageDatastoreTest extends APITestWithMock {

    private PageDatastore pageDatastore;

    @Mock
    APISession engineSession;

    @Mock
    PageAPI pageAPI;

    @Mock
    WebBonitaConstantsUtils constantsValue;

    @Mock
    Page mockedPage;

    @Mock
    TenantFolder tenantFolder;

    @Mock
    CompoundPermissionsMapping compoundPermissionsMapping;

    @Mock
    ResourcesPermissionsMapping resourcesPermissionsMapping;

    @Mock
    CustomPageService customPageService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    PageItem pageToBeAdded;

    File pagesDir = new File("target/bonita-home/client/tenants/1/work/pages");

    URL zipFileUrl;

    private String savedBonitaHomeProperty;

    @Before
    public void setUp() throws Exception {
        savedBonitaHomeProperty = System.getProperty(WebBonitaConstants.BONITA_HOME);
        System.setProperty(WebBonitaConstants.BONITA_HOME, "target/bonita-home/bonita");
        deleteDir(pagesDir);
        // Given
        when(mockedPage.getId()).thenReturn(1l);
        when(mockedPage.getName()).thenReturn("custompage_page1");
        when(mockedPage.getDisplayName()).thenReturn("Page 1");
        when(mockedPage.getDescription()).thenReturn("This is a page description");
        final Date mockedDate = new Date(0l);
        when(mockedPage.getInstallationDate()).thenReturn(mockedDate);
        when(mockedPage.getLastModificationDate()).thenReturn(mockedDate);
        when(mockedPage.getInstalledBy()).thenReturn(1l);
        when(mockedPage.isProvided()).thenReturn(false);
        when(engineSession.getTenantId()).thenReturn(1L);

        zipFileUrl = getClass().getResource("/page.zip");
        FileUtils.copyFileToDirectory(new File(zipFileUrl.toURI()), new File("target"));
        final File zipFile = new File("target/page.zip");
        when(tenantFolder.getTempFile("page.zip", 1L)).thenReturn(zipFile);

        when(constantsValue.getTempFolder()).thenReturn(zipFile.getParentFile());
        when(constantsValue.getPagesFolder()).thenReturn(pagesDir);

        pageDatastore = new PageDatastore(engineSession, constantsValue, pageAPI, customPageService, compoundPermissionsMapping, resourcesPermissionsMapping,
                tenantFolder);

        pageToBeAdded = new PageItem();
        pageToBeAdded.setUrlToken("custompage_page1");
        pageToBeAdded.setDisplayName("Page 1");
        pageToBeAdded.setDescription("This is a page description");

        pageToBeAdded.setAttribute(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, zipFile.getName());
    }

    @After
    public void teardown() throws Exception {
        if (StringUtil.isBlank(savedBonitaHomeProperty)) {
            System.clearProperty(WebBonitaConstants.BONITA_HOME);
        } else {
            System.setProperty(WebBonitaConstants.BONITA_HOME, savedBonitaHomeProperty);
        }
    }

    @Test
    public void should_add_a_page_return_valid_page() throws Exception {

        // Given
        deleteDir(pagesDir);
        when(pageAPI.createPage(any(String.class), any(byte[].class))).thenReturn(mockedPage);

        // When
        final PageItem addedPage = pageDatastore.add(pageToBeAdded);

        // Validate
        assertNotNull(addedPage);
        assertTrue(new File("target/bonita-home/bonita/client/tenants/1/work/pages", addedPage.getUrlToken()).listFiles().length > 0);
    }

    @Test(expected = APIException.class)
    public void should_add_a_not_valid_page_rise_exception() {
        // Given
        deleteDir(pagesDir);
        final URL zipFileUrl = getClass().getResource("/InvalidPage.zip");
        pageToBeAdded.setAttribute(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, zipFileUrl.getPath());

        // When
        final PageItem addedPage = pageDatastore.add(pageToBeAdded);

        // Validate
        assertNotNull(addedPage);
    }

    @Test(expected = APIException.class)
    public void should_get_a_not_existing_page_rise_exception() throws Exception {
        // Given
        deleteDir(pagesDir);
        when(pageAPI.getPage(1l)).thenThrow(new PageNotFoundException("newPage"));

        // When
        pageDatastore.get(makeAPIID(1l));
    }

    @Test
    public void should_delete_a_page_removing_it_from_bonita_home() throws Exception {
        // Given
        deleteDir(pagesDir);
        when(pageAPI.createPage(any(String.class), any(byte[].class))).thenReturn(mockedPage);
        when(pageAPI.getPage(1l)).thenReturn(mockedPage);
        final PageItem pageToRemove = pageDatastore.add(pageToBeAdded);
        final List<APIID> ids = new ArrayList<APIID>();
        ids.add(pageToRemove.getId());

        // When
        pageDatastore.delete(ids);

        // Validate
        verify(customPageService).removePage(engineSession, mockedPage.getName());
    }

    @Test(expected = APIException.class)
    public void should_delete_a_not_existing_page_rise_exception() throws Exception {
        // Given
        deleteDir(pagesDir);
        when(pageAPI.getPage(1l)).thenThrow(new PageNotFoundException("newPage"));
        final List<APIID> ids = new ArrayList<APIID>();
        ids.add(APIID.makeAPIID(1l));

        // When
        pageDatastore.delete(ids);
    }

    @Test
    public void zip_with_index_in_resources_should_be_valid() throws Exception {
       final File zipFileResource = new File(getClass().getResource( "/pageWithIndexInResources.zip").toURI());
        UnzipUtil.unzip(zipFileResource, new File("target"+File.separator+"pageWithIndexInResources").getPath(), false);
        final File unzipFolder = new File("target"+File.separator+"pageWithIndexInResources");

        boolean isValide = pageDatastore.areResourcesAvailable(unzipFolder);

        assertTrue(isValide);
    }

    @Test
    public void should_call_customPageService_for_custom_page_permissions_when_adding_a_page() throws Exception {
        // Given
        deleteDir(pagesDir);
        when(pageAPI.createPage(any(String.class), any(byte[].class))).thenReturn(mockedPage);
        final HashSet<String> resourcePermissions = new HashSet<String>(Arrays.asList("Case Visualization", "Organization Visualization"));
        doReturn(resourcePermissions).when(customPageService)
        .getCustomPagePermissions(any(File.class), eq(resourcesPermissionsMapping), eq(false));

        // When
        pageDatastore.add(pageToBeAdded);

        // Then
        verify(customPageService).getCustomPagePermissions(any(File.class), eq(resourcesPermissionsMapping), eq(false));
        verify(customPageService).addPermissionsToCompoundPermissions(mockedPage.getName(), resourcePermissions, compoundPermissionsMapping,
                resourcesPermissionsMapping);
    }

    @Test
    public void should_delete_compound_permission_when_deleting_page() throws Exception {
        // Given
        deleteDir(pagesDir);
        when(pageAPI.createPage(any(String.class), any(byte[].class))).thenReturn(mockedPage);
        when(pageAPI.getPage(1l)).thenReturn(mockedPage);
        final PageItem pageToRemove = pageDatastore.add(pageToBeAdded);
        final List<APIID> ids = new ArrayList<APIID>();
        ids.add(pageToRemove.getId());

        // When
        pageDatastore.delete(ids);

        // Validate
        verify(compoundPermissionsMapping).removeProperty(mockedPage.getName());
    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_adding_page_with_unauthorized_path() throws IOException {
        // Given
        pageToBeAdded.setAttribute(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, "unauthorized_page.zip");

        doThrow(new UnauthorizedFolderException("error")).when(tenantFolder).getTempFile("unauthorized_page.zip", 1L);

        // When
        pageDatastore.add(pageToBeAdded);

    }

    @Test
    public void it_throws_an_exception_when_cannot_write_file_on_add() throws IOException {
        // Given
        pageToBeAdded.setAttribute(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, "error_page.zip");
        doThrow(new IOException("error")).when(tenantFolder).getTempFile("error_page.zip", 1L);

        // When
        try {
            pageDatastore.add(pageToBeAdded);
        } catch (final APIException e) {
            assertEquals(e.getMessage(), "java.io.IOException: error");
        }

    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_updatting_page_with_unauthorized_path() throws IOException, PageNotFoundException {
        // Given
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, "unauthorized_page.zip");

        pageToBeAdded.setAttribute(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, "unauthorized_page.zip");
        doReturn(mockedPage).when(pageAPI).getPage(1L);
        doThrow(new UnauthorizedFolderException("error")).when(tenantFolder).getTempFile("unauthorized_page.zip", 1L);

        // When
        pageDatastore.update(APIID.makeAPIID(1L), attributes);

    }

    @Test
    public void it_throws_an_exception_when_cannot_write_file_on_update() throws IOException, PageNotFoundException {
        // Given
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, "error_page.zip");

        pageToBeAdded.setAttribute(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, "error_page.zip");
        doReturn(mockedPage).when(pageAPI).getPage(1L);
        doThrow(new IOException("error")).when(tenantFolder).getTempFile("error_page.zip", 1L);

        // When
        try {
            pageDatastore.update(APIID.makeAPIID(1L), attributes);
        } catch (final APIException e) {
            assertEquals(e.getMessage(), "java.io.IOException: error");
        }

    }

    /**
     * @param pagesDir
     */
    private void deleteDir(final File pagesDir) {
        if (pagesDir.exists()) {
            try {
                IOUtil.deleteDir(pagesDir);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

}
