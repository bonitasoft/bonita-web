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
package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedTaskInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedUserTaskInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.TaskInstance;
import org.bonitasoft.engine.bpm.flownode.UserTaskInstance;
import org.bonitasoft.web.rest.api.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.ArchivedActivityItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.ArchivedTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.ArchivedUserTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.FlowNodeItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.UserTaskItem;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.ArchivedActivityDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.ArchivedFlowNodeDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.ArchivedHumanTaskDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.ArchivedTaskDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.ArchivedUserTaskDatastore;

/**
 * @author Séverin Moussel
 * 
 */
public class FlowNodeConverter {

    public static FlowNodeItem convertEngineToConsoleItem(final FlowNodeInstance item) {
        return INSTANCE._convertEngineToConsoleItem(item);
    }

    public static ArchivedFlowNodeItem convertEngineToConsoleItem(final ArchivedFlowNodeInstance item) {
        return INSTANCE._convertEngineToConsoleItem(item);
    }

    protected FlowNodeItem _convertEngineToConsoleItem(final FlowNodeInstance item) {
        if (item instanceof UserTaskInstance) {
            return UserTaskDatastore.fillConsoleItem(new UserTaskItem(), (UserTaskInstance) item);
        } else if (item instanceof HumanTaskInstance) {
            return HumanTaskDatastore.fillConsoleItem(new HumanTaskItem(), (HumanTaskInstance) item);
        } else if (item instanceof TaskInstance) {
            return TaskDatastore.fillConsoleItem(new TaskItem(), (TaskInstance) item);
        } else if (item instanceof ActivityInstance) {
            return ActivityDatastore.fillConsoleItem(new ActivityItem(), (ActivityInstance) item);
        }

        return FlowNodeDatastore.fillConsoleItem(new FlowNodeItem(), item);
    }

    protected ArchivedFlowNodeItem _convertEngineToConsoleItem(final ArchivedFlowNodeInstance item) {
        if (item instanceof ArchivedUserTaskInstance) {
            return ArchivedUserTaskDatastore.fillConsoleItem(new ArchivedUserTaskItem(), (ArchivedUserTaskInstance) item);
        } else if (item instanceof ArchivedHumanTaskInstance) {
            return ArchivedHumanTaskDatastore.fillConsoleItem(new ArchivedHumanTaskItem(), (ArchivedHumanTaskInstance) item);
        } else if (item instanceof ArchivedTaskInstance) {
            return ArchivedTaskDatastore.fillConsoleItem(new ArchivedTaskItem(), (ArchivedTaskInstance) item);
        } else if (item instanceof ArchivedActivityInstance) {
            return ArchivedActivityDatastore.fillConsoleItem(new ArchivedActivityItem(), (ArchivedActivityInstance) item);
        }

        return ArchivedFlowNodeDatastore.fillConsoleItem(new ArchivedFlowNodeItem(), item);
    }

    private static FlowNodeConverter INSTANCE;

    public static void setFlowNodeConverter(final FlowNodeConverter converter) {
        INSTANCE = converter;
    }

}
