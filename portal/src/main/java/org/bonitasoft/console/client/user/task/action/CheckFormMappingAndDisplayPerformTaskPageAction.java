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
package org.bonitasoft.console.client.user.task.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeItem;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


public class CheckFormMappingAndDisplayPerformTaskPageAction extends Action {

    private static final String ATTRIBUTE_FORM_MAPPING_TASK = "task";

    private static final String ATTRIBUTE_FORM_MAPPING_TARGET = "target";

    private static final String NONE_FORM_MAPPING_TARGET = "NONE";

    protected final RawView performTaskview;

    protected final String taskName;

    protected final String processDefinitionId;

    public CheckFormMappingAndDisplayPerformTaskPageAction(final String processDefinitionId, final String taskName, final RawView performTaskview) {
        this.processDefinitionId = processDefinitionId;
        this.taskName = taskName;
        this.performTaskview = performTaskview;
    }

    @Override
    public void execute() {
        final TreeIndexed<String> parameters = performTaskview.getParameters();
        searchFormMappingForTask(parameters);
    }

    protected void searchFormMappingForTask(final TreeIndexed<String> parameters) {
        RequestBuilder requestBuilder;
        requestBuilder = new RequestBuilder(RequestBuilder.GET, "../API/form/mapping?c=10&p=0&f=" + PageItem.ATTRIBUTE_PROCESS_ID + "=" + processDefinitionId
                + "&f=" + ATTRIBUTE_FORM_MAPPING_TASK + "=" + taskName);
        requestBuilder.setCallback(new FormMappingCallback(parameters));
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
            GWT.log("Error while creating the from mapping request", e);
        }
    }

    protected class FormMappingCallback extends APICallback {

        private final TreeIndexed<String> parameters;

        public FormMappingCallback(final TreeIndexed<String> parameters) {
            this.parameters = parameters;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final JSONValue root = JSONParser.parseLenient(response);
            final JSONArray formMappings = root.isArray();
            if (formMappings.size() == 1) {
                final JSONValue formMappingValue = formMappings.get(0);
                final JSONObject formMapping = formMappingValue.isObject();
                if (formMapping.containsKey(ATTRIBUTE_FORM_MAPPING_TARGET)
                        && NONE_FORM_MAPPING_TARGET.equals(formMapping.get(ATTRIBUTE_FORM_MAPPING_TARGET).isString().stringValue())) {
                    //skip the form and execute the task
                    final String taskId = parameters.getValue(FlowNodeItem.ATTRIBUTE_ID);
                    executeTask(taskId, parameters);
                } else {
                    //display the form
                    ViewController.closePopup();
                    ViewController.showView(performTaskview);
                }
            } else {
                GWT.log("There is no form mapping for process " + processDefinitionId + " and task " + taskName);
            }
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            GWT.log("Error while getting the form mapping for process " + processDefinitionId + " and task " + taskName);
            super.onError(message, errorCode);
        }
    }

    protected void executeTask(final String taskId, final TreeIndexed<String> parameters) {
        RequestBuilder requestBuilder;
        requestBuilder = new RequestBuilder(RequestBuilder.POST, "../API/bpm/userTask/" + taskId + "/execution");
        requestBuilder.setCallback(new ExecuteTaskCallback(taskId));
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
            GWT.log("Error while creating the task execution request", e);
        }
    }

    protected class ExecuteTaskCallback extends APICallback {

        private final String taskId;

        public ExecuteTaskCallback(final String taskId) {
            this.taskId = taskId;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final String confirmationMessage = _("The task has been executed. The page is going to be refreshed...");
            //TODO: timer + refresh the page
            ViewController.closePopup();
            showConfirmation(confirmationMessage);
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            if (errorCode == Response.SC_BAD_REQUEST) {
                GWT.log("Error while executing the task " + taskId + " : " + message);
                final String errorMessage = _("Error while trying to execute the task. The task may require some values for its contract input.");
                ViewController.closePopup();
                showError(errorMessage);
            } else {
                super.onError(message, errorCode);
            }
        }
    }

    protected void showConfirmation(final String confirmationMessage) {
        Message.info(confirmationMessage);
        //final MessagePage messagePopup = new MessagePage(TYPE.SUCCESS, confirmationMessage);
        //ViewController.showPopup(messagePopup);
    }

    protected void showError(final String errorMessage) {
        Message.alert(errorMessage);
    }
}
