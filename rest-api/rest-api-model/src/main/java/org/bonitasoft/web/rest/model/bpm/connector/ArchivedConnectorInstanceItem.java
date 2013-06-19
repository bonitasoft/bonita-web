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
package org.bonitasoft.web.rest.model.bpm.connector;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Julien Mege
 * 
 */
public class ArchivedConnectorInstanceItem extends ConnectorInstanceItem implements ItemHasUniqueId {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ATTRIBUTE_ARCHIVED_DATE = "archivedDate";

    public static final String ATTRIBUTE_SOURCE_OBJECT_ID = "sourceObjectId";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // SETTERS
    public void setArchivedDate(final Date date) {
        this.setAttribute(ATTRIBUTE_ARCHIVED_DATE, date);
    }

    public void setSourceObjectId(final Long id) {
        this.setAttribute(ATTRIBUTE_CONTAINER_ID, id);
    }

    public void setSourceObjectId(final String id) {
        this.setAttribute(ATTRIBUTE_CONTAINER_ID, id);
    }

    public void setSourceObjectId(final APIID id) {
        this.setAttribute(ATTRIBUTE_CONTAINER_ID, id);
    }

    // GETTERS
    public String getArchivedDate() {
        return this.getAttributeValue(ATTRIBUTE_ARCHIVED_DATE);
    }

    public APIID getSourceObjectId(final Long id) {
        return APIID.makeAPIID(this.getAttributeValue(ATTRIBUTE_SOURCE_OBJECT_ID));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition getItemDefinition() {
        return Definitions.get(ArchivedConnectorInstanceDefinition.TOKEN);
    }

}
