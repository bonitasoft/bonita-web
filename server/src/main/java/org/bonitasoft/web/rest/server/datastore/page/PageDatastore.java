/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.servlet.FileUploadServlet;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.console.common.server.utils.UnzipUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.exception.UpdatingWithInvalidPageTokenException;
import org.bonitasoft.engine.exception.UpdatingWithInvalidPageZipContentException;
import org.bonitasoft.engine.io.IOUtil;
import org.bonitasoft.engine.page.ContentType;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.page.PageCreator;
import org.bonitasoft.engine.page.PageSearchDescriptor;
import org.bonitasoft.engine.page.PageUpdater;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * @author Fabio Lombardi, Anthony Birembaut
 */

public class PageDatastore extends CommonDatastore<PageItem, Page> implements DatastoreHasAdd<PageItem>, DatastoreHasUpdate<PageItem>,
        DatastoreHasGet<PageItem>, DatastoreHasSearch<PageItem>, DatastoreHasDelete {

    private static final String INDEX_GROOVY = "Index.groovy";

    private static final String INDEX_HTML = "index.html";

    private static final String PAGE_PROPERTIES = "page.properties";

    /**
     * page files
     */
    public static final String UNMAPPED_ATTRIBUTE_ZIP_FILE = "pageZip";

    static final String PAGE_TOKEN_PREFIX = "custompage_";

    protected final WebBonitaConstantsUtils constants;

    protected final PageAPI pageAPI;

    protected final CustomPageService customPageService;

    private final CompoundPermissionsMapping compoundPermissionsMapping;

    private final ResourcesPermissionsMapping resourcesPermissionsMapping;

    private final BonitaHomeFolderAccessor tenantFolder;

    public PageDatastore(final APISession engineSession, final WebBonitaConstantsUtils constantsValue, final PageAPI pageAPI,
            final CustomPageService customPageService,
            final CompoundPermissionsMapping compoundPermissionsMapping, final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final BonitaHomeFolderAccessor tenantFolder) {
        super(engineSession);
        constants = constantsValue;
        this.pageAPI = pageAPI;
        this.customPageService = customPageService;
        this.compoundPermissionsMapping = compoundPermissionsMapping;
        this.resourcesPermissionsMapping = resourcesPermissionsMapping;
        this.tenantFolder = tenantFolder;
    }

    @Override
    public PageItem add(final PageItem pageItem) {
        final String zipFileAttribute = pageItem.getAttributeValue(UNMAPPED_ATTRIBUTE_ZIP_FILE);
        final String[] filenames = zipFileAttribute.split(FileUploadServlet.RESPONSE_SEPARATOR);
        final String filename = filenames[0];
        String originalFileName;

        if (filenames.length > 1) {
            originalFileName = filenames[1];
        } else {
            originalFileName = filename;
        }
        pageItem.setContentName(originalFileName);

        try {
            final APISession engineSession = getEngineSession();
            final long tenantId = engineSession.getTenantId();
            final File zipFile = tenantFolder.getTempFile(filename, tenantId);
            final File unzipPageTempFolder = unzipContentFile(zipFile);
            validateZipContent(unzipPageTempFolder);
            final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(new File(unzipPageTempFolder, PAGE_PROPERTIES),
                    resourcesPermissionsMapping, false);
            final Page page = createEnginePage(pageItem, zipFile);
            final PageItem addedPage = convertEngineToConsoleItem(page);
            savePageInBonitahome(addedPage.getUrlToken(), unzipPageTempFolder, engineSession);
            customPageService.addRestApiExtensionPermissions(resourcesPermissionsMapping,
                    customPageService.getPageResourceProvider(page, tenantId), engineSession);
            customPageService.addPermissionsToCompoundPermissions(addedPage.getUrlToken(), customPagePermissions, compoundPermissionsMapping,
                    resourcesPermissionsMapping);
            return addedPage;
        } catch (final UnauthorizedFolderException e) {
            throw new APIForbiddenException(e.getMessage());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected File unzipContentFile(final File zipFile) throws InvalidPageZipContentException {
        File unzipPageTempFolder = null;
        try {
            final Random randomGen = new Random();
            final int tempPageFolder = randomGen.nextInt();
            unzipPageTempFolder = new File(constants.getTempFolder(), String.valueOf(tempPageFolder));
            UnzipUtil.unzip(zipFile, unzipPageTempFolder.getPath(), false);
        } catch (final Exception e) {
            deleteTempDirectory(unzipPageTempFolder);
            throw new InvalidPageZipContentException("Unable to unzip the page content.");
        }
        return unzipPageTempFolder;
    }

    protected void validateZipContent(final File unzipPageFolder) throws InvalidPageZipContentException, IOException {
        if (!areResourcesAvailable(unzipPageFolder)) {
            throw new InvalidPageZipContentException("index file (Index.groovy or index.html) or page.properties is missing.");
        }
    }

    protected boolean isPageTokenValid(final String urlToken) {
        return urlToken.matches(PAGE_TOKEN_PREFIX + "\\p{Alnum}+");
    }

    protected boolean areResourcesAvailable(final File unzipPageFolder) throws IOException {
        final File[] pageFiles = unzipPageFolder.listFiles();
        boolean indexOK = false;
        boolean propertiesOK = false;
        Properties pageProperties = null;
        if (pageFiles != null) {
            for (final File file : pageFiles) {
                final String fileName = file.getName();
                if (fileName.matches(INDEX_HTML) || fileName.matches(INDEX_GROOVY)) {
                    indexOK = true;
                }
                if (fileName.matches(PAGE_PROPERTIES)) {
                    pageProperties = new java.util.Properties();
                    try (InputStream is = new FileInputStream(file)) {
                        pageProperties.load(is);
                    }
                    propertiesOK = true;
                }
            }
        }
        if (!indexOK) {
            if (pageProperties != null
                    && Objects.equals(pageProperties.getProperty(CustomPageService.PROPERTY_CONTENT_TYPE), ContentType.API_EXTENSION)) {
                indexOK = true;
            }
            final File indexInResources = new File(unzipPageFolder.getPath(), CustomPageService.RESOURCES_PROPERTY + File.separator + INDEX_HTML);
            if (indexInResources.exists()) {
                indexOK = true;
            }
        }
        return indexOK && propertiesOK;
    }

    protected void deleteTempDirectory(final File unzipPage) {
        try {
            if (unzipPage.isDirectory()) {
                IOUtilDeleteDir(unzipPage);
            }
        } catch (final IOException e) {
            throw new APIException(e);
        }
    }

    protected void IOUtilDeleteDir(final File unzipPage) throws IOException {
        IOUtil.deleteDir(unzipPage);
    }

    protected Page createEnginePage(final PageItem pageItem, final File zipFile) throws AlreadyExistsException, CreationException, IOException,
            UpdatingWithInvalidPageTokenException, UpdatingWithInvalidPageZipContentException, UpdateException {

        try {
            final byte[] zipContent = readZipFile(zipFile);

            Page page = pageAPI.createPage(pageItem.getContentName(), zipContent);
            if (pageItem.getProcessId() != null) {
                final PageUpdater pageUpdater = new PageUpdater();
                pageUpdater.setProcessDefinitionId(pageItem.getProcessId().toLong());
                if (pageItem.getContentType() != null) {
                    pageUpdater.setContentType(pageItem.getContentType());
                }
                page = pageAPI.updatePage(page.getId(), pageUpdater);
            }
            return page;

        } finally {
            zipFile.delete();
        }
    }

    protected byte[] readZipFile(final File zipFile) throws IOException {
        return FileUtils.readFileToByteArray(zipFile);
    }

    protected PageCreator buildPageCreatorFrom(final PageItem pageItem) {
        final PageCreator pageCreator = new PageCreator(pageItem.getUrlToken(), pageItem.getContentName());
        pageCreator.setDescription(pageItem.getDescription());
        pageCreator.setDisplayName(pageItem.getDisplayName());
        return pageCreator;
    }

    protected void savePageInBonitahome(final String urlToken, final File unzipPageTempFolder, APISession session) throws IOException {
        customPageService.verifyPageClass(unzipPageTempFolder, session);
        final File pagesFolder = new File(constants.getPagesFolder(), urlToken);
        FileUtils.copyDirectory(unzipPageTempFolder, pagesFolder);
        deleteTempDirectory(unzipPageTempFolder);
    }

    @Override
    public PageItem get(final APIID id) {
        try {
            final Page pageItem = pageAPI.getPage(id.toLong());
            return convertEngineToConsoleItem(pageItem);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public void delete(final List<APIID> ids) {
        try {
            for (final APIID id : ids) {
                final Page page = pageAPI.getPage(id.toLong());
                if (!page.isProvided()) {
                    final APISession engineSession = getEngineSession();
                    customPageService.removeRestApiExtensionPermissions(resourcesPermissionsMapping,
                            customPageService.getPageResourceProvider(page, engineSession.getTenantId()), engineSession);
                    pageAPI.deletePage(id.toLong());
                    customPageService.removePage(engineSession, page.getName());
                    compoundPermissionsMapping.removeProperty(page.getName());
                }
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected List<Long> APIIdsToLong(final List<APIID> ids) {
        final List<Long> result = new ArrayList<>(ids.size());
        for (final APIID id : ids) {
            result.add(id.toLong());
        }
        return result;
    }

    @Override
    public ItemSearchResult<PageItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        // Build search
        final SearchOptionsCreator creator = makeSearchOptionCreator(page, resultsByPage, search, orders, filters);

        // Run search depending on filters passed
        SearchResult<Page> searchResult;
        try {
            searchResult = runSearch(filters, creator);
            // Convert to ConsoleItems
            return new ItemSearchResult<>(page, resultsByPage, searchResult.getCount(), convertEngineToConsoleItemsList(searchResult.getResult()));
        } catch (final SearchException e) {
            throw new APIException(e);
        }

    }

    protected PageSearchDescriptorConverter getSearchDescriptorConverter() {
        return new PageSearchDescriptorConverter();
    }

    protected SearchOptionsCreator makeSearchOptionCreator(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        final SearchOptionsCreator searchOptionsCreator = new SearchOptionsCreator(page, resultsByPage, search, new Sorts(orders,
                getSearchDescriptorConverter()), new Filters(filters,
                        new PageFilterCreator(getSearchDescriptorConverter())));
        final SearchOptionsBuilder builder = searchOptionsCreator.getBuilder();

        if (filters.containsKey(PageItem.FILTER_CONTENT_TYPE)
                && "processPage".equalsIgnoreCase(filters.get(PageSearchDescriptor.CONTENT_TYPE))) {
            builder.leftParenthesis().filter(PageSearchDescriptor.CONTENT_TYPE, "form")
                    .or().filter(PageSearchDescriptor.CONTENT_TYPE, "page")
                    .rightParenthesis();
        } else {
            addStringFilterToSearchBuilder(filters, builder, PageItem.FILTER_CONTENT_TYPE, PageSearchDescriptor.CONTENT_TYPE);
        }
        return searchOptionsCreator;
    }

    /**
     * @param filters
     * @param creator
     * @return
     * @throws SearchException
     */
    protected SearchResult<Page> runSearch(final Map<String, String> filters, final SearchOptionsCreator creator) throws SearchException {
        return pageAPI.searchPages(creator.create());
    }

    @Override
    public PageItem update(final APIID id, final Map<String, String> attributes) {
        PageItem updatedPage = null;
        final PageUpdater pageUpdater = new PageUpdater();
        try {

            File zipFile = null;
            final String oldURLToken = pageAPI.getPage(id.toLong()).getName();

            if (attributes.containsKey(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE)) {

                final String zipFileAttribute = attributes.get(UNMAPPED_ATTRIBUTE_ZIP_FILE);
                if (zipFileAttribute != null && !zipFileAttribute.isEmpty()) {
                    final String[] filenames = zipFileAttribute.split(FileUploadServlet.RESPONSE_SEPARATOR);
                    final String filename = filenames[0];
                    String originalFileName;
                    if (filenames.length > 1) {
                        originalFileName = filenames[1];
                    } else {
                        originalFileName = filename;
                    }

                    final APISession engineSession = getEngineSession();
                    final long tenantId = engineSession.getTenantId();
                    zipFile = tenantFolder.getTempFile(filename, tenantId);
                    final File unzipPageTempFolder = unzipContentFile(zipFile);
                    validateZipContent(unzipPageTempFolder);
                    final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(new File(unzipPageTempFolder, PAGE_PROPERTIES),
                            resourcesPermissionsMapping, false);
                    pageUpdater.setContentName(originalFileName);
                    updatePageContent(id, zipFile, oldURLToken);
                    final Page page = pageAPI.updatePage(id.toLong(), pageUpdater);
                    updatedPage = convertEngineToConsoleItem(page);
                    savePageInBonitahome(updatedPage.getUrlToken(), unzipPageTempFolder, engineSession);
                    if (!Objects.equals(oldURLToken, updatedPage.getUrlToken())) {
                        compoundPermissionsMapping.removeProperty(oldURLToken);
                    }
                    customPageService.addRestApiExtensionPermissions(resourcesPermissionsMapping,
                            customPageService.getPageResourceProvider(page, tenantId), engineSession);
                    customPageService.addPermissionsToCompoundPermissions(updatedPage.getUrlToken(), customPagePermissions, compoundPermissionsMapping,
                            resourcesPermissionsMapping);
                }
            }
        } catch (final UnauthorizedFolderException e) {
            throw new APIForbiddenException(e.getMessage());
        } catch (final Exception e) {
            throw new APIException(e);
        }
        return updatedPage;
    }

    protected void updatePageContent(final APIID id, final File zipFile, final String oldURLToken) throws IOException,
            CompilationFailedException, BonitaException {
        if (zipFile != null) {
            final Long pageId = id.toLong();
            customPageService.removeRestApiExtensionPermissions(resourcesPermissionsMapping,
                    customPageService.getPageResourceProvider(pageAPI.getPage(pageId), getEngineSession().getTenantId()), getEngineSession());
            pageAPI.updatePageContent(pageId, FileUtils.readFileToByteArray(zipFile));
            zipFile.delete();
        }
        customPageService.removePage(getEngineSession(), oldURLToken);
    }

    @Override
    protected PageItem convertEngineToConsoleItem(final Page item) {
        if (item != null) {
            return new PageItemConverter().convert(item);
        }
        return null;
    }

}
