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
package org.bonitasoft.console.client.admin.process.view;

import org.bonitasoft.console.client.user.task.action.PostMessageEventListener;
import org.bonitasoft.console.client.user.task.action.ProcessInstantiationCallbackBehavior;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;

public class ProcessInstantiationEventListener extends PostMessageEventListener {

    /**
     * action type for postMessage to notify parent frame
     */
    public static final String START_PROCESS_ACTION_FOR_NOTIF = "Start process";

    private final ProcessInstantiationCallbackBehavior processInstantiationCallbackBehavior;

    public ProcessInstantiationEventListener() {
        super();
        processInstantiationCallbackBehavior = new ProcessInstantiationCallbackBehavior() {

            @Override
            public void onSuccess(final String caseId, final String targetUrlOnSuccess) {
                if (targetUrlOnSuccess != null && !targetUrlOnSuccess.isEmpty()) {
                    Window.Location.assign(targetUrlOnSuccess);
                } else {
                    GWT.log("targetUrlOnSuccess attribute is not set in the message.");
                }
            }
        };
    }

    @Override
    public String getActionToWatch() {
        return START_PROCESS_ACTION_FOR_NOTIF;
    }

    @Override
    protected void onSuccess(final String targetUrlOnSuccess) {
        processInstantiationCallbackBehavior.onSuccess(null, targetUrlOnSuccess);
    }

    @Override
    protected void onError(final String dataFromError, final int errorCode) {
        processInstantiationCallbackBehavior.onError(dataFromError, errorCode);
    }
}