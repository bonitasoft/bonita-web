/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
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
        pageItem.setUrlToken(engineItem.getName());
        pageItem.setDisplayName(engineItem.getDisplayName());
        pageItem.setIsProvided(engineItem.isProvided());
        pageItem.setDescription(engineItem.getDescription());
        pageItem.setCreatedByUserId(APIID.makeAPIID(engineItem.getInstalledBy()));
        pageItem.setCreationDate(engineItem.getInstallationDate());
        pageItem.setLastUpdateDate(engineItem.getLastModificationDate());
        pageItem.setUpdatedByUserId(engineItem.getLastUpdatedBy());
        pageItem.setContentName(engineItem.getContentName());
        return pageItem;
    }

}
