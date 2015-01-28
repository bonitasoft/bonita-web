/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.utils.TenantFolder;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.console.common.server.utils.UnzipUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.io.IOUtil;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.page.PageCreator;
import org.bonitasoft.engine.page.PageUpdater;
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
 *
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

    private static final String PAGE_TOKEN_PREFIX = "custompage_";

    protected final WebBonitaConstantsUtils constants;

    protected final PageAPI pageAPI;

    protected final CustomPageService customPageService;


    private final CompoundPermissionsMapping compoundPermissionsMapping;

    private final ResourcesPermissionsMapping resourcesPermissionsMapping;

    private final TenantFolder tenantFolder;

    public PageDatastore(final APISession engineSession, final WebBonitaConstantsUtils constantsValue, final PageAPI pageAPI,
            final CustomPageService customPageService,
            final CompoundPermissionsMapping compoundPermissionsMapping, final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final TenantFolder tenantFolder) {
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
        final String[] filenames = zipFileAttribute.split(":");
        final String filename = filenames[0];
        String originalFileName;

        if (filenames.length > 1) {
            originalFileName = filenames[1];
        } else {
            originalFileName = filename;
        }
        pageItem.setContentName(originalFileName);

        try {
            final File zipFile = tenantFolder.getTempFile(filename, getEngineSession().getTenantId());
            final File unzipPageTempFolder = unzipContentFile(zipFile);
            validateZipContent(unzipPageTempFolder);
            final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(new File(unzipPageTempFolder, PAGE_PROPERTIES),
                    resourcesPermissionsMapping, false);
            final PageItem addedPage = convertEngineToConsoleItem(createEnginePage(pageItem, zipFile));
            savePageInBonitahome(addedPage.getUrlToken(), unzipPageTempFolder);
            customPageService.addPermissionsToCompoundPermissions(addedPage.getUrlToken(), customPagePermissions, compoundPermissionsMapping,
                    resourcesPermissionsMapping);
            return addedPage;
        } catch (final UnauthorizedFolderException e) {
            throw new APIForbiddenException(e.getMessage());
        }catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private File unzipContentFile(final File zipFile) throws InvalidPageZipContentException {
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

        if (pageFiles != null) {
            for (final File file : pageFiles) {
                final String fileName = file.getName();
                if (fileName.matches(INDEX_HTML) || fileName.matches(INDEX_GROOVY)) {
                    indexOK = true;
                }
                if (fileName.matches(PAGE_PROPERTIES)) {
                    propertiesOK = true;

                }
            }
        }
        return indexOK && propertiesOK;
    }

    protected void deleteTempDirectory(final File unzipPage) {
        try {
            if (unzipPage.isDirectory()) {
                IOUtil.deleteDir(unzipPage);
            }
        } catch (final IOException e) {
            throw new APIException(e);
        }
    }

    protected Page createEnginePage(final PageItem pageItem, final File zipFile) throws AlreadyExistsException, CreationException, IOException {

        try {
            final byte[] zipContent = FileUtils.readFileToByteArray(zipFile);

            return pageAPI.createPage(pageItem.getContentName(), zipContent);

        } finally {
            zipFile.delete();
        }
    }

    protected PageCreator buildPageCreatorFrom(final PageItem pageItem) {
        final PageCreator pageCreator = new PageCreator(pageItem.getUrlToken(), pageItem.getContentName());
        pageCreator.setDescription(pageItem.getDescription());
        pageCreator.setDisplayName(pageItem.getDisplayName());
        return pageCreator;
    }

    protected void savePageInBonitahome(final String urlToken, final File unzipPageTempFolder) throws IOException {
        customPageService.verifyPageClass(urlToken, unzipPageTempFolder);
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
                    pageAPI.deletePage(id.toLong());
                    customPageService.removePage(getEngineSession(), page.getName());
                    compoundPermissionsMapping.removeProperty(page.getName());
                }
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected List<Long> APIIdsToLong(final List<APIID> ids) {
        final List<Long> result = new ArrayList<Long>(ids.size());
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
            return new ItemSearchResult<PageItem>(page, resultsByPage, searchResult.getCount(), convertEngineToConsoleItemsList(searchResult.getResult()));
        } catch (final SearchException e) {
            throw new APIException(e);
        }

    }

    protected PageSearchDescriptorConverter getSearchDescriptorConverter() {
        return new PageSearchDescriptorConverter();
    }

    protected SearchOptionsCreator makeSearchOptionCreator(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        return new SearchOptionsCreator(page, resultsByPage, search, new Sorts(orders, getSearchDescriptorConverter()), new Filters(filters,
                new PageFilterCreator(getSearchDescriptorConverter())));
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
                    final String[] filenames = zipFileAttribute.split(":");
                    final String filename = filenames[0];
                    String originalFileName;
                    if (filenames.length > 1) {
                        originalFileName = filenames[1];
                    } else {
                        originalFileName = filename;
                    }

                    zipFile = tenantFolder.getTempFile(filename, getEngineSession().getTenantId());
                    final File unzipPageTempFolder = unzipContentFile(zipFile);
                    validateZipContent(unzipPageTempFolder);
                    final Set<String> customPagePermissions = customPageService.getCustomPagePermissions(new File(unzipPageTempFolder, PAGE_PROPERTIES),
                            resourcesPermissionsMapping, false);
                    pageUpdater.setContentName(originalFileName);
                    updatePageContent(id, zipFile, oldURLToken);
                    updatedPage = convertEngineToConsoleItem(pageAPI.updatePage(id.toLong(), pageUpdater));
                    savePageInBonitahome(updatedPage.getUrlToken(), unzipPageTempFolder);
                    if (oldURLToken != updatedPage.getUrlToken()) {
                        compoundPermissionsMapping.removeProperty(oldURLToken);
                    }
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
            pageAPI.updatePageContent(id.toLong(), FileUtils.readFileToByteArray(zipFile));
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
