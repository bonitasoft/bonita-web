/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.builder.page;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.datastore.page.PageDatastore;

/**
 * @author Fabio Lombardi
 * 
 */
public class PageItemBuilder {

    protected long id = 1L;

    protected String urlToken = "custompage_aName";

    protected String displayName = "aDisplayName";

    protected String description = "aDescription";

    protected boolean isProvided = false;
    
    protected Date creation_date = new Date(1);

    protected long createdBy = 1L;

    protected Date last_update_date = new Date(1);

    protected String contentName = "page.zip";

    protected long updatedBy = 1L;

    public static PageItemBuilder aPageItem() {
        return new PageItemBuilder();
    }

    public PageItem build() throws IOException, URISyntaxException {
        final PageItem item = new PageItem();
        item.setId(id);
        item.setUrlToken(urlToken);
        item.setDisplayName(displayName);
        item.setDescription(description);
        item.setIsProvided(isProvided);
        item.setCreationDate(creation_date);
        item.setCreatedByUserId(createdBy);
        item.setLastUpdateDate(last_update_date);
        item.setUpdatedByUserId(updatedBy);
        item.setContentName(contentName);
        String zipFileName = "/page.zip";
        final URL zipFileUrl = getClass().getResource(zipFileName);
        final File zipFile = new File(zipFileUrl.toURI());

        FileUtils.copyFileToDirectory(zipFile, WebBonitaConstantsUtils.getTenantInstance().getTempFolder());

        item.setAttribute(PageDatastore.UNMAPPED_ATTRIBUTE_ZIP_FILE, zipFile.getName());
        return item;
    }

    public PageItemBuilder fromEngineItem(final Page page) {
        id = page.getId();
        urlToken = page.getName();
        displayName = page.getDisplayName();
        description = page.getDescription();
        isProvided = page.isProvided();
        creation_date = page.getInstallationDate();
        createdBy = page.getInstalledBy();
        last_update_date = page.getLastModificationDate();
        updatedBy = page.getLastUpdatedBy();
        contentName = page.getContentName();

        return this;
    }

}
