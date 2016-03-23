/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.task.view;

import org.bonitasoft.console.client.user.task.action.PostMessageEventListener;
import org.bonitasoft.console.client.user.task.action.TaskExecutionCallbackBehavior;

public class TaskExecutionEventListener extends PostMessageEventListener {

    /**
     * action type for postMessage to notify parent frame
     */
    public static final String SUBMIT_TASK_ACTION_FOR_NOTIF = "Submit task";

    private final TaskExecutionCallbackBehavior taskExecutionCallbackBehavior;

    public TaskExecutionEventListener(final TaskExecutionCallbackBehavior taskExecutionCallbackBehavior) {
        super();
        this.taskExecutionCallbackBehavior = taskExecutionCallbackBehavior;
    }

    @Override
    public String getActionToWatch() {
        return SUBMIT_TASK_ACTION_FOR_NOTIF;
    }

    @Override
    protected void onSuccess(final String dataFromSuccess) {
        taskExecutionCallbackBehavior.onSuccess(dataFromSuccess);
    }

    @Override
    protected void onError(final String dataFromError, final int errorCode) {
        taskExecutionCallbackBehavior.onError(dataFromError, errorCode);
    }
}