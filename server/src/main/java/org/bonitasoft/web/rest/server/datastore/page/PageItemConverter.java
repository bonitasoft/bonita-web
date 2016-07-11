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

import org.bonitasoft.engine.page.Page;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;
import org.bonitasoft.web.toolkit.client.data.APIID;

public class PageItemConverter extends ItemConverter<PageItem, Page> {

    @Override
    public PageItem convert(final Page engineItem) {
        final PageItem pageItem = new PageItem();
        pageItem.setId(engineItem.getId());
        pageItem.setProcessId(engineItem.getProcessDefinitionId());
        pageItem.setUrlToken(engineItem.getName());
        pageItem.setDisplayName(engineItem.getDisplayName());
        pageItem.setIsProvided(engineItem.isProvided());
        pageItem.setDescription(engineItem.getDescription());
        pageItem.setCreatedByUserId(APIID.makeAPIID(engineItem.getInstalledBy()));
        pageItem.setCreationDate(engineItem.getInstallationDate());
        pageItem.setLastUpdateDate(engineItem.getLastModificationDate());
        pageItem.setUpdatedByUserId(engineItem.getLastUpdatedBy());
        pageItem.setContentName(engineItem.getContentName());
        pageItem.setContentType(engineItem.getContentType());
        return pageItem;
    }

}
