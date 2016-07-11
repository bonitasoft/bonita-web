package org.bonitasoft.web.rest.server.api.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.rest.model.builder.page.PageItemBuilder.aPageItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.datastore.page.PageDatastore;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.After;
import org.junit.Test;

public class APIPageIntegrationTest extends AbstractConsoleTest {

    public static final String PAGE_API_EXTENSION_ZIP = "/pageApiExtension.zip";
    public static final String PAGE_API_EXTENSION_UPDATE_ZIP = "/pageApiExtensionUpdate.zip";
    public static final String NEW_PAGE_ZIP = "/newPage.zip";
    private APIPage apiPage;
    private ResourcesPermissionsMapping resourcesPermissionsMapping;

    @After
    public void cleanPages() throws Exception {
        final SearchOptions searchOptions = new SearchOptionsBuilder(0, 100000).done();
        for (Page page : getPageAPI().searchPages(searchOptions).getResult()) {
            final List<APIID> ids = new ArrayList<>();
            ids.add(APIID.makeAPIID(page.getId()));
            apiPage.delete(ids);
        }
    }

    @Override
    public void consoleTestSetUp() throws Exception {
        apiPage = new APIPage();
        final APISession session = getInitiator().getSession();
        apiPage.setCaller(getAPICaller(session, "API/portal/page"));

        resourcesPermissionsMapping = PropertiesFactory.getResourcesPermissionsMapping(session
                .getTenantId());
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    private PageAPI getPageAPI() throws Exception {
        return TenantAPIAccessor.getCustomPageAPI(getInitiator().getSession());
    }

    @Test
    public void runAdd_a_page_to_the_repository() throws Exception {
        // Given
        final PageItem expectedPage = aPageItem().build(getTenantId());
        final PageItem addedPage = apiPage.add(expectedPage);

        // Validate
        assertNotNull(addedPage);
        assertThat(addedPage.getUrlToken()).isEqualTo(expectedPage.getUrlToken());
        assertThat(addedPage.getDescription()).isEqualTo(expectedPage.getDescription());
        assertThat(addedPage.getDisplayName()).isEqualTo(expectedPage.getDisplayName());
        assertThat(addedPage.getContentName()).isEqualTo(expectedPage.getContentName());

    }

    @Test
    public void runGet_a_page_from_the_repository() throws Exception {

        // Given
        final PageItem expectedItem = addNewPage(NEW_PAGE_ZIP);

        // When
        final PageItem getItem = apiPage.get(expectedItem.getId());

        // Validate
        assertTrue(expectedItem.getUrlToken().equals(getItem.getUrlToken()));
    }

    @Test(expected = APIException.class)
    public void runGet_not_existing_page_rise_exception() throws Exception {
        // When
        final APIID notExistingPageId = aPageItem().build(getTenantId()).getId();
        apiPage.get(notExistingPageId);
    }

    @Test(expected = APIException.class)
    public void runDelete_then_get_page_rise_exception() throws Exception {

        // Given
        final PageItem pageToBeRemoved = addNewPage(NEW_PAGE_ZIP);
        final List<APIID> ids = new ArrayList<>();
        ids.add(pageToBeRemoved.getId());

        // When
        apiPage.delete(ids);
        apiPage.get(pageToBeRemoved.getId());
    }

    @Test
    public void runUpdate_with_new_page_content_change_it() throws Exception {
        // Given
        final PageItem pageToBeUpdated = addNewPage(NEW_PAGE_ZIP);

        // When
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, new File(getClass().getResource(NEW_PAGE_ZIP).toURI()).getName());

        final PageItem updatedPage = apiPage.update(pageToBeUpdated.getId(), attributes);
        // Validate
        assertNotNull(updatedPage);
        assertThat(updatedPage.getUrlToken()).as("url token").isEqualTo("custompage_groovyexampletest");
        assertThat(updatedPage.getDisplayName()).as("display name").isEqualTo("Groovy example page_test");
        assertThat(updatedPage.getDescription()).as("description").isEqualTo("Groovy class example of custom page source structure (in English)._test");

    }

    @Test
    public void should_add_rest_api_extension_update_resource_permission() throws Exception {
        // Given
        final PageItem pageItem = aPageItem().withZip(PAGE_API_EXTENSION_ZIP).build(getTenantId());
        assertThat(resourcesPermissionsMapping.getProperty("GET|extension/rest")).as("should not have permission before add").isNull();

        // When
        apiPage.add(pageItem);

        //then
        assertThat(resourcesPermissionsMapping.getProperty("GET|extension/rest")).as("should add permission").isEqualTo("[permission1,permission2]");
    }

    @Test
    public void should_delete_rest_api_extension_remove_resource_permission() throws Exception {
        // Given
        final PageItem pageItem = apiPage.add(aPageItem().withZip(PAGE_API_EXTENSION_ZIP).build(getTenantId()));

        assertThat(resourcesPermissionsMapping.getProperty("GET|extension/rest")).as("should have permission").isNotNull()
                .isEqualTo("[permission1,permission2]");

        // When
        final List<APIID> ids = new ArrayList<>();
        ids.add(pageItem.getId());
        apiPage.delete(ids);

        // Then
        assertThat(resourcesPermissionsMapping.getProperty("GET|extension/rest")).as("should remove permission").isNull();

    }

    private long getTenantId() {
        return getInitiator().getSession().getTenantId();
    }

    @Test
    public void should_update_rest_api_extension_change_resource_permission() throws Exception {
        // Given
        final PageItem pageItem = apiPage.add(aPageItem().withZip(PAGE_API_EXTENSION_ZIP).build(getTenantId()));
        assertThat(resourcesPermissionsMapping.getProperty("GET|extension/rest")).as("should not have permission before add").isEqualTo(
                "[permission1,permission2]");
        assertThat(resourcesPermissionsMapping.getProperty("POST|extension/restUpdated")).as("should not have permission before add").isNull();

        // When
        final PageItem pageItemUpdate = aPageItem().withZip(PAGE_API_EXTENSION_UPDATE_ZIP).build(getTenantId());
        apiPage.update(pageItem.getId(), pageItemUpdate.getAttributes());

        // Then
        assertThat(resourcesPermissionsMapping.getProperty("GET|extension/rest")).as("should remove old permission").isNull();
        assertThat(resourcesPermissionsMapping.getProperty("POST|extension/restUpdated")).as("should add new permission")
                .isEqualTo("[permission2,permission3]");

    }

    /**
     * @return
     * @throws Exception
     * @param pageFileName
     */
    private PageItem addNewPage(String pageFileName) throws Exception {
        final long tenantId = getTenantId();
        final PageItem pageItem = aPageItem().build(tenantId);
        final URL zipFileUrl = getClass().getResource(pageFileName);

        final File zipFile = new File(zipFileUrl.toURI());
        FileUtils.copyFileToDirectory(zipFile, WebBonitaConstantsUtils.getInstance(tenantId).getTempFolder());

        final byte[] pageContent = FileUtils.readFileToByteArray(new File(zipFileUrl.toURI()));
        final PageItem addedPage = addPageItemToRepository(pageItem.getContentName(), pageContent);
        return addedPage;
    }

    private PageItem addPageItemToRepository(final String pageContentName, final byte[] pageContent) throws Exception {
        return aPageItem().fromEngineItem(getPageAPI().createPage(pageContentName, pageContent)).build(getTenantId());
    }
}
