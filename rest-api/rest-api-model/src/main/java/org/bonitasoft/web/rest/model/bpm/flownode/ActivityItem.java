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
package org.bonitasoft.web.rest.model.bpm.flownode;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 */
public class ActivityItem extends FlowNodeItem implements IActivityItem {

    public static final String ATTRIBUTE_VARIABLES = "variables";
    
    public ActivityItem() {
        super();
    }

    public ActivityItem(final IItem item) {
        super(item);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void setReachStateDate(final String reachedStateDate) {
        setAttribute(ATTRIBUTE_REACHED_STATE_DATE, reachedStateDate);
    }

    @Override
    public final void setReachStateDate(final Date reachedStateDate) {
        setAttribute(ATTRIBUTE_REACHED_STATE_DATE, reachedStateDate);
    }

    @Override
    public final Date getReachStateDate() {
        return this.getAttributeValueAsDate(ATTRIBUTE_REACHED_STATE_DATE);
    }

    @Override
    public final void setLastUpdateDate(final String date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    @Override
    public final void setLastUpdateDate(final Date date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    @Override
    public final Date getLastUpdateDate() {
        return this.getAttributeValueAsDate(ATTRIBUTE_LAST_UPDATE_DATE);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition getItemDefinition() {
        return ActivityDefinition.get();
    }

}
