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
package org.bonitasoft.console.client.user.process.view;

import org.bonitasoft.console.client.user.process.action.ProcessInstantiationCallbackBehavior;
import org.bonitasoft.console.client.user.task.action.PostMessageEventListener;

public class ProcessInstantiationEventListener extends PostMessageEventListener {

    /**
     * action type for postMessage to notify parent frame
     */
    public static final String START_PROCESS_ACTION_FOR_NOTIF = "Start process";

    private final ProcessInstantiationCallbackBehavior processInstantiationCallbackBehavior;

    public ProcessInstantiationEventListener(final ProcessInstantiationCallbackBehavior processInstantiationCallbackBehavior) {
        super();
        this.processInstantiationCallbackBehavior = processInstantiationCallbackBehavior;
    }

    @Override
    public String getActionToWatch() {
        return START_PROCESS_ACTION_FOR_NOTIF;
    }

    @Override
    protected void onSuccess(final String dataFromSuccess) {
        processInstantiationCallbackBehavior.onSuccess(dataFromSuccess);
    }

    @Override
    protected void onError(final String dataFromError, final int errorCode) {
        processInstantiationCallbackBehavior.onError(dataFromError, errorCode);
    }
}