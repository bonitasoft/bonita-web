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
package org.bonitasoft.console.client.model.bpm.flownode;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 */
public class ArchivedFlowNodeItem extends FlowNodeItem implements ArchivedFlowNode {

    public ArchivedFlowNodeItem() {
        super();
    }

    public ArchivedFlowNodeItem(final IItem item) {
        super(item);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS AND SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void setArchivedDate(final String archivedDate) {
        this.setAttribute(ATTRIBUTE_ARCHIVED_DATE, archivedDate);
    }

    @Override
    public final void setArchivedDate(final Date date) {
        this.setAttribute(ATTRIBUTE_ARCHIVED_DATE, date);
    }

    @Override
    public Date getArchivedDate() {
        return getAttributeValueAsDate(ATTRIBUTE_ARCHIVED_DATE);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition getItemDefinition() {
        return new ArchivedFlowNodeDefinition();
    }

    @Override
    public final boolean isArchived() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.bonitasoft.console.client.model.bpm.flownode.ArchivedFlowNode#setSourceObjectId(org.bonitasoft.web.toolkit.client.data.APIID)
     */
    @Override
    public void setSourceObjectId(APIID id) {
        this.setAttribute(ATTRIBUTE_SOURCE_OBJECT_ID, id);
    }

    /* (non-Javadoc)
     * @see org.bonitasoft.console.client.model.bpm.flownode.ArchivedFlowNode#setSourceObjectId(java.lang.String)
     */
    @Override
    public void setSourceObjectId(String id) {
        this.setAttribute(ATTRIBUTE_SOURCE_OBJECT_ID, id);
    }

    /* (non-Javadoc)
     * @see org.bonitasoft.console.client.model.bpm.flownode.ArchivedFlowNode#setSourceObjectId(java.lang.Long)
     */
    @Override
    public void setSourceObjectId(Long id) {
        this.setAttribute(ATTRIBUTE_SOURCE_OBJECT_ID, id);
    }

    /* (non-Javadoc)
     * @see org.bonitasoft.console.client.model.bpm.flownode.ArchivedFlowNode#getSourceObjectId()
     */
    @Override
    public APIID getSourceObjectId() {
        return this.getAttributeValueAsAPIID(ATTRIBUTE_SOURCE_OBJECT_ID);
    }

}
