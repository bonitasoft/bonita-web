/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.process;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.category.Category;
import org.bonitasoft.engine.bpm.category.CategoryCriterion;
import org.bonitasoft.engine.bpm.category.CategoryUpdater;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.framework.api.*;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

import java.util.List;
import java.util.Map;

/**
 * Category data store
 * 
 * @author Nicolas TITH
 * 
 */
public class CategoryDatastore extends CommonDatastore<CategoryItem, Category> implements
        DatastoreHasSearch<CategoryItem>,
        DatastoreHasGet<CategoryItem>,
        DatastoreHasDelete,
        DatastoreHasAdd<CategoryItem>,
        DatastoreHasUpdate<CategoryItem> {

    public CategoryDatastore(final APISession engineSession) {
        super(engineSession);
    }

    /**
     * Retrieve the total number of categories
     * 
     * @return the total number of categories
     * @throws InvalidSessionException
     *             When session time out throw this exception
     * @throws BonitaHomeNotSetException
     *             When bonita home not set throw this exception
     * @throws ServerAPIException
     *             When access server api have problem throw this exception
     * @throws UnknownAPITypeException
     *             When didn't know the api type throw this exception
     */
    public long getNumberOfCategories() {
        try {
            return getProcessAPI().getNumberOfCategories();
        } catch (final InvalidSessionException e) {
            throw new APIException(e);
        }
    }

    /**
     * Retrieve the total number of categories for a given process
     */
    public long getNumberOfCategories(long processId) {
        try {
            return getProcessAPI().getNumberOfCategories(processId);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public ItemSearchResult<CategoryItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        try {
            CategoryCriterion orderCrit = getOrder(orders);
            if (filters.containsKey(ProcessItem.ATTRIBUTE_ID)) {
                return searchProcessCategories(page, resultsByPage, filters, orderCrit);
            } else {
                return searchCategories(page, resultsByPage, orderCrit);
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private ItemSearchResult<CategoryItem> searchCategories(final int page, final int resultsByPage, CategoryCriterion orderCrit) {
        List<Category> searchResult = getProcessAPI().getCategories(SearchOptionsBuilderUtil.computeIndex(page, resultsByPage),
                resultsByPage,
                orderCrit);
        return new ItemSearchResult<CategoryItem>(page, resultsByPage, getNumberOfCategories(), convertEngineToConsoleItemsList(searchResult));
    }

    private ItemSearchResult<CategoryItem> searchProcessCategories(final int page, final int resultsByPage, final Map<String, String> filters,
            CategoryCriterion orderCrit) {
        final Long processId = Long.valueOf(filters.get(ProcessItem.ATTRIBUTE_ID));
        List<Category> searchResult = getProcessAPI().getCategoriesOfProcessDefinition(processId,
                SearchOptionsBuilderUtil.computeIndex(page, resultsByPage),
                resultsByPage,
                orderCrit);
        return new ItemSearchResult<CategoryItem>(page, resultsByPage, getNumberOfCategories(processId), convertEngineToConsoleItemsList(searchResult));
    }

    private CategoryCriterion getOrder(final String orders) {
        if (CategoryItem.ATTRIBUTE_NAME.equals(orders)) {
            return CategoryCriterion.NAME_ASC;
        } else {
            return CategoryCriterion.valueOf(orders);
        }
    }

    @Override
    public CategoryItem add(final CategoryItem item) {
        try {
            final Category result = getProcessAPI().createCategory(item.getName(), item.getDescription());
            return convertEngineToConsoleItem(result);
        } catch (final AlreadyExistsException e) {
            throw new APIForbiddenException(new _("Category with name %categoryName% already exists", new Arg("categoryName", item.getName())), e);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public CategoryItem get(final APIID id) {
        try {
            final Category result = getProcessAPI().getCategory(id.toLong());
            return convertEngineToConsoleItem(result);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    protected CategoryItem convertEngineToConsoleItem(final Category item) {
        final CategoryItem categoryItem = new CategoryItem();
        categoryItem.setCreatedByUserId(item.getCreator());
        categoryItem.setCreationDate(item.getCreationDate());
        categoryItem.setDescription(item.getDescription());
        categoryItem.setDisplayName(item.getName());
        categoryItem.setId(item.getId());
        categoryItem.setName(item.getName());
        return categoryItem;
    }

    protected final CategoryUpdater createCategoryUpdater(final CategoryItem item) {
        if (item == null) {
            return null;
        }
        final CategoryUpdater updater = new CategoryUpdater();
        if (item.getDescription() != null) {
            updater.setDescription(item.getDescription());
        }
        return updater;
    }

    @Override
    public void delete(final List<APIID> ids) {
        try {
            final ProcessAPI processAPI = getProcessAPI();
            for (final APIID id : ids) {
                final Long idCat = id.toLong();
                processAPI.removeAllProcessDefinitionsFromCategory(idCat);
                processAPI.deleteCategory(idCat);
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public CategoryItem update(final APIID id, final Map<String, String> attributes) {
        try {
            final ProcessAPI processAPI = getProcessAPI();
            final CategoryItem catItem = new CategoryItem();
            catItem.setAttributes(attributes);
            catItem.setId(id);
            processAPI.updateCategory(id.toLong(), createCategoryUpdater(catItem));
            return get(id);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected ProcessAPI getProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

}
