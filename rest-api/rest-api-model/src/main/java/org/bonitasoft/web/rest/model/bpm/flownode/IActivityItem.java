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
package org.bonitasoft.web.rest.model.bpm.flownode;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasLastUpdateDate;

/**
 * @author Séverin Moussel
 * 
 */
public interface IActivityItem extends ItemHasLastUpdateDate, IFlowNodeItem {

    // // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ATTRIBUTE_REACHED_STATE_DATE = "reached_state_date";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String FILTER_SUPERVISOR_ID = "supervisor_id";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setReachStateDate(final String reachedStateDate);

    public void setReachStateDate(final Date reachedStateDate);

    public Date getReachStateDate();

    @Override
    public void setLastUpdateDate(final String date);

    @Override
    public void setLastUpdateDate(final Date date);

    @Override
    public Date getLastUpdateDate();
}
