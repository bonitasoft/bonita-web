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
package org.bonitasoft.console.client.admin.bpm.accessor;

import java.util.Date;

import org.bonitasoft.web.rest.model.bpm.connector.ArchivedConnectorInstanceItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 * 
 *         Convenient class do deal with typing issues
 *         not addressed in the item parsing.
 * 
 */
public class IActivityAccessor {

    private final IFlowNodeItem item;

    public IActivityAccessor(IFlowNodeItem item) {
        if (!item.isActivity()) {
            throw new RuntimeException("Accessing item isn't an IActivityItem");
        }
        this.item = item;
    }

    public IFlowNodeItem getItem() {
        return item;
    }

    public final Date getLastUpdateDate() {
        return item.getAttributeValueAsDate(IActivityItem.ATTRIBUTE_LAST_UPDATE_DATE);
    }

    public String getState() {
        return item.getState();
    }

    public APIID getId() {
        return item.getId();
    }

    public boolean isArchived() {
        return item.isArchived();
    }

    public APIID getSourceObjectId() {
        return item.getAttributeValueAsAPIID(ArchivedConnectorInstanceItem.ATTRIBUTE_SOURCE_OBJECT_ID);
    }

}
