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
package org.bonitasoft.console.client.model.bpm.flownode;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.EnumValidator;

/**
 * @author Séverin Moussel
 */
public class TaskDefinition extends ActivityDefinition {

    public static final String TOKEN = "task";

    public static final String API_URL = "../API/bpm/task";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        super.defineAttributes();

        // Restrict type to tasks
        getAttribute(TaskItem.ATTRIBUTE_TYPE)
                .removeValidator(EnumValidator.class.getName())
                .addValidator(new EnumValidator(
                        TaskItem.VALUE_TYPE_USER_TASK,
                        TaskItem.VALUE_TYPE_AUTOMATIC_TASK,
                        TaskItem.VALUE_TYPE_MANUAL_TASK
                        ));

        // add extra state
        ((EnumValidator) getAttribute(FlowNodeItem.ATTRIBUTE_STATE)
                .getValidator(EnumValidator.class.getName()))
                .addValue(TaskItem.VALUE_STATE_REPLAY);
    }

    @Override
    public ITaskItem _createItem() {
        return new TaskItem();
    }

    @Override
    public APICaller<? extends ITaskItem> getAPICaller() {
        return new APICaller<TaskItem>(this);
    }

    public static TaskDefinition get() {
        return (TaskDefinition) Definitions.get(TOKEN);
    }

}
