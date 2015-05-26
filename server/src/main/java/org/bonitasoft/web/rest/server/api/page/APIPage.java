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
package org.bonitasoft.web.rest.server.api.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.deployer.DeployerFactory;
import org.bonitasoft.web.rest.server.datastore.page.PageDatastore;
import org.bonitasoft.web.rest.server.datastore.page.PageDatastoreFactory;
import org.bonitasoft.web.rest.server.datastore.profile.entry.ProfileEntryItemConverter;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Fabio Lombardi
 *
 */
public class APIPage extends ConsoleAPI<PageItem> implements APIHasGet<PageItem>, APIHasSearch<PageItem>, APIHasAdd<PageItem>, APIHasUpdate<PageItem>,
APIHasDelete {

    public static final String PAGES_ALLOWED_FOR_MENU = "pagesallowedformenu";

    public static final String PAGES_ADDED_FOR_MENU = "pagesaddedformenu";

    @Override
    protected ItemDefinition<PageItem> defineItemDefinition() {
        return PageDefinition.get();
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return getPageDatastore();
    }

    @Override
    public PageItem get(final APIID id) {
        return getPageDatastore().get(id);
    }

    @Override
    public PageItem add(final PageItem item) {
        return getPageDatastore().add(item);
    }

    @Override
    public PageItem update(final APIID id, final Map<String, String> attributes) {
        return getPageDatastore().update(id, attributes);
    }

    @Override
    public void delete(final List<APIID> ids) {
        getPageDatastore().delete(ids);
    }

    @Override
    public ItemSearchResult<PageItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        if (filters.containsKey(PAGES_ALLOWED_FOR_MENU)) {
            return searchWithoutPagesAlreadyAdded(page, resultsByPage, search, filters, orders);
        } else if (filters.containsKey(PAGES_ADDED_FOR_MENU)) {
            return searchPagesAlreadyAdded(page, resultsByPage, search, filters, orders);
        }

        return getPageDatastore().search(page, resultsByPage, search, orders, filters);
    }

    private ItemSearchResult<PageItem> searchPagesAlreadyAdded(final int pageId, final int resultsByPage, final String search,
            final Map<String, String> filters, final String orders) {
        final List<String> pagesAlreadyAdded = getPagesAlreadyAdded(filters.get(PAGES_ADDED_FOR_MENU));
        final ItemSearchResult<PageItem> results = getPageDatastore().search(pageId, resultsByPage, search, orders, new HashMap<String, String>());
        final List<PageItem> pages = new ArrayList<PageItem>();

        for (final PageItem page : results.getResults()) {
            if (pagesAlreadyAdded.contains(page.getUrlToken())) {
                pages.add(page);
            }
        }
        results.setResults(pages);
        results.setTotal(pages.size());

        return results;
    }

    private ItemSearchResult<PageItem> searchWithoutPagesAlreadyAdded(final int pageId, final int resultsByPage, final String search,
            final Map<String, String> filters, final String orders) {
        final List<String> pagesAlreadyAdded = getPagesAlreadyAdded(filters.get(PAGES_ALLOWED_FOR_MENU));
        filters.remove(PAGES_ALLOWED_FOR_MENU);
        final ItemSearchResult<PageItem> results = getPageDatastore().search(pageId, resultsByPage, search, orders, filters);

        final List<PageItem> pages = new ArrayList<PageItem>();

        for (final PageItem page : results.getResults()) {
            if (!pagesAlreadyAdded.contains(page.getUrlToken())) {
                pages.add(page);
            }
        }
        results.setResults(pages);
        results.setTotal(pages.size());

        return results;
    }

    private List<String> getPagesAlreadyAdded(final String profileId) {
        final List<ProfileEntry> profileEntries = createProfileEntryEngineClient().getAllChildsOfAProfileEntry(Long.valueOf(profileId));
        final List<ProfileEntryItem> profileEntyItems = new ProfileEntryItemConverter().convert(profileEntries);

        final List<String> pages = new ArrayList<String>();
        for (final ProfileEntryItem entryItem : profileEntyItems) {
            pages.add(entryItem.getPage());
        }
        return pages;
    }

    private ProfileEntryEngineClient createProfileEntryEngineClient() {
        return new EngineClientFactory(new EngineAPIAccessor(getEngineSession())).createProfileEntryEngineClient();

    }

    @Override
    protected void fillDeploys(final PageItem item, final List<String> deploys) {
        /*
         * Need to be done there and not in constructor
         * because need the engine session which is set
         * by setter instead of being injected in API constructor...
         */
        addDeployer(getDeployerFactory().createUserDeployer(PageItem.ATTRIBUTE_CREATED_BY_USER_ID));
        addDeployer(getDeployerFactory().createUserDeployer(PageItem.ATTRIBUTE_UPDATED_BY_USER_ID));
        super.fillDeploys(item, deploys);
    }

    protected DeployerFactory getDeployerFactory() {
        return new DeployerFactory(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return PageItem.ATTRIBUTE_URL_TOKEN;
    }

    private PageDatastore getPageDatastore() {
        PageAPI pageAPI;
        try {
            pageAPI = TenantAPIAccessor.getCustomPageAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
        final WebBonitaConstantsUtils constants = WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId());
        final PageDatastoreFactory pageDatastoreFactory = new PageDatastoreFactory();
        return pageDatastoreFactory.create(getEngineSession(), constants, pageAPI);
    }

}
