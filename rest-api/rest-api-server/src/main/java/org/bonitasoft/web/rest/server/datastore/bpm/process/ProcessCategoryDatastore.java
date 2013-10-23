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
package org.bonitasoft.web.rest.server.datastore.bpm.process;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;

import java.io.Serializable;
import java.util.*;

/**
 * @author Séverin Moussel
 * 
 */
public class ProcessCategoryDatastore extends CommonDatastore<ProcessCategoryItem, Serializable> implements
        DatastoreHasAdd<ProcessCategoryItem>,
        DatastoreHasDelete {

    public ProcessCategoryDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    protected ProcessCategoryItem convertEngineToConsoleItem(final Serializable item) {
        // No converison here
        return null;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.S
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void delete(final List<APIID> ids) {
        MapUtil.iterate(
                buildCategoriesIdByProcessIdMapping(ids),
                new MapUtil.ForEach<Long, List<Long>>() {

                    @Override
                    protected void apply(Long processId, List<Long> categoriesId) {
                        removeCategoriesFromProcess(processId, categoriesId);
                    }
                });
    }

    private void removeCategoriesFromProcess(Long processId, List<Long> categoriesId) {
        try {
            getProcessAPI().removeCategoriesFromProcess(processId, categoriesId);
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    private Map<Long, List<Long>> buildCategoriesIdByProcessIdMapping(List<APIID> ids) {
        Map<Long, List<Long>> categoriesIdByProcessId = new HashMap<Long, List<Long>>();
        for (APIID apiid : ids) {
            Long processId = apiid.getPartAsLong(ProcessCategoryItem.ATTRIBUTE_PROCESS_ID);
            Long categoryId = apiid.getPartAsLong(ProcessCategoryItem.ATTRIBUTE_CATEGORY_ID);
            if (categoriesIdByProcessId.containsKey(processId)) {
                categoriesIdByProcessId.get(processId).add(categoryId);
            } else {
                ArrayList<Long> categoryIds = new ArrayList<Long>();
                categoryIds.add(categoryId);
                categoriesIdByProcessId.put(processId, categoryIds);
            }
        }
        return categoriesIdByProcessId;
    }

    @Override
    public ProcessCategoryItem add(final ProcessCategoryItem item) {
        try {
            getProcessAPI().addCategoriesToProcess(item.getProcessId().toLong(),
                    Arrays.asList(item.getCategoryId().toLong()));

            return item;
        } catch (AlreadyExistsException e) {
            throw new APIForbiddenException(new _("This category has already been added to this apps"), e);
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
