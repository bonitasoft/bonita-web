/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.server.framework.search.ISearchDirection;

/**
 * @author Séverin Moussel
 */
public class APITask extends AbstractAPITask<TaskItem> {

    @Override
    protected boolean isAllowedState(final String state) {
        return TaskItem.VALUE_STATE_REPLAY.equals(state)
                || super.isAllowedState(state);
    }

    @Override
    public String defineDefaultSearchOrder() {
        return TaskItem.ATTRIBUTE_LAST_UPDATE_DATE + ISearchDirection.SORT_ORDER_DESCENDING;
    }

}
