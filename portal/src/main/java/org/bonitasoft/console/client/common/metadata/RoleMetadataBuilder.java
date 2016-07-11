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
package org.bonitasoft.console.client.common.metadata;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

public class RoleMetadataBuilder extends MetadataBuilder {

    public void addName() {
        add(name());
    }
    
    private ItemDetailsMetadata name() {
        return new ItemDetailsMetadata(new AttributeReader(RoleItem.ATTRIBUTE_NAME),
                _("Name"), _("Name of the role"));
    }
    
    public void addCreationDate() {
        add(creationDate());
    }
    
    private ItemDetailsMetadata creationDate() {
        return new ItemDetailsMetadata(new DateAttributeReader(RoleItem.ATTRIBUTE_CREATION_DATE),
                _("Creation date"), _("The date of the creation of the group"));
    }

    public void addLastUpdateDate() {
        add(lastUpdateDate());
    }
    
    private ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(new DateAttributeReader(RoleItem.ATTRIBUTE_LAST_UPDATE_DATE),
                _("Last update"), _("The date of the last update of the group"));
    }
}
