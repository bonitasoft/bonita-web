/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.model.bpm.flownode;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Julien Mege
 */
public class HiddenUserTaskItem extends Item {

    public HiddenUserTaskItem() {
        super();
    }

    public HiddenUserTaskItem(final IItem item) {
        super(item);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES NAMES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ATTRIBUTE_USER_ID = "user_id";

    public static final String ATTRIBUTE_TASK_ID = "task_id";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setUserId(final APIID userId) {
        this.setAttribute(ATTRIBUTE_USER_ID, userId);
    }

    public void setUserId(final String userId) {
        this.setAttribute(ATTRIBUTE_USER_ID, userId);
    }

    public void setUserId(final Long userId) {
        this.setAttribute(ATTRIBUTE_USER_ID, String.valueOf(userId));
    }

    public void setTaskId(final APIID taskId) {
        this.setAttribute(ATTRIBUTE_TASK_ID, taskId);
    }

    public void setTaskId(final String taskId) {
        this.setAttribute(ATTRIBUTE_TASK_ID, taskId);
    }

    public void setTaskId(final Long taskId) {
        this.setAttribute(ATTRIBUTE_TASK_ID, String.valueOf(taskId));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public APIID getUserId() {
        return APIID.makeAPIID(this.getAttributeValue(ATTRIBUTE_USER_ID));
    }

    public APIID getTaskId() {
        return APIID.makeAPIID(this.getAttributeValue(ATTRIBUTE_TASK_ID));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition getItemDefinition() {
        return new HiddenUserTaskDefinition();
    }

}
