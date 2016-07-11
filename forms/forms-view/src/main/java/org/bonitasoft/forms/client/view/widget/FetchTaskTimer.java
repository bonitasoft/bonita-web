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
package org.bonitasoft.forms.client.view.widget;

import java.util.Map;

import org.bonitasoft.forms.client.model.FormURLComponents;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;

import com.google.gwt.user.client.Timer;

/**
 * @author Vincent Elcrin
 * 
 */
public class FetchTaskTimer extends Timer {

    /**
     * max number of refresh
     */
    private static final int MAX_REFRESH = 20;

    /**
     * The refresh delay
     */
    protected static final int REFRESH_DELAY = 500;

    /**
     * number of times the query was done
     */
    protected int refreshCount = 0;

    private String formId;

    private Map<String, Object> urlContext;

    public FetchTaskTimer(final String formId, final Map<String, Object> urlContext) {
        this.formId = formId;
        this.urlContext = urlContext;

    }

    @Override
    public void run() {
        fetchTaskList();
    }

    public void start() {
        scheduleTimer();
    }

    protected void onTaskFound(final FormURLComponents nextFormURL) {

    }

    protected void onNoTaskFound() {

    }

    protected void onTaskFetchingFailed(final Throwable caught) {

    }

    private void fetchTaskList() {
        getFormsService().getNextFormURL(formId, urlContext, new FormsAsyncCallback<FormURLComponents>() {

            @Override
            public void onSuccess(final FormURLComponents nextFormURL) {
                cancel();
                if (nextFormURL != null) {
                    onTaskFound(nextFormURL);
                } else {
                    scheduleTimer();
                }
            }

            @Override
            public void onUnhandledFailure(Throwable caught) {
                onTaskFetchingFailed(caught);
            }

        });
    }

    private void scheduleTimer() {
        if (refreshCount < MAX_REFRESH) {
            schedule(REFRESH_DELAY);
            refreshCount++;
        } else {
            onNoTaskFound();
        }
    }

    private FormsServiceAsync getFormsService() {
        return RpcFormsServices.getFormsService();
    }

}
