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
package org.bonitasoft.console.client.common.metadata;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;


/**
 * @author Fabio Lombardi
 *
 */
public class PageMetadataBuilder extends MetadataBuilder {

//    public void addConfigurationState() {
//        add(configurationState());
//    }

//    private ItemDetailsMetadata configurationState() {
//        return new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_CONFIGURATION_STATE, _("Configuration state "), _("The configuration state of the process"));
//    }

    
    public void addInstalledDate() {
        add(installedDate());
    }
    
    private ItemDetailsMetadata installedDate() {
        return new ItemDetailsMetadata(new DateAttributeReader(PageItem.ATTRIBUTE_CREATION_DATE), _("Created on"),
                _("The date when this page has been added"));
    }
    
    public void addInstalledBy() {
        add(installedBy());
    }
    
    private ItemDetailsMetadata installedBy() {
        return new ItemDetailsMetadata(new DeployedUserReader(PageItem.ATTRIBUTE_CREATED_BY_USER_ID),
                _("Created by"), _("The user that has added this page"));
    }
    
    public void addLastUpdateDate() {
        add(lastUpdateDate());
    }
    
    private ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(PageItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Updated on"), _("The date when the page was updated"));
    }

    public void addUpdatedBy() {
        add(updatedBy());
    }
    
    private ItemDetailsMetadata updatedBy() {
        return new ItemDetailsMetadata(new DeployedUserReader(PageItem.ATTRIBUTE_UPDATED_BY_USER_ID),
                _("Updated by"), _("The user that updated this page"));
    }

    public void addUrlToken() {
        add(urlToken());
    }

    private ItemDetailsMetadata urlToken() {
        return new ItemDetailsMetadata(new AttributeReader(PageItem.ATTRIBUTE_URL_TOKEN),
                _("URL token"), _("The URL token for accessing this page"));
    }
    
    
}
