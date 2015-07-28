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
package org.bonitasoft.console.client.user.task.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Map;

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.user.cases.view.CaseMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
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
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;

/**
 * @author Nicolas Tith, Anthony Birembaut
 */
public class CheckFormMappingAndDisplayProcessInstanciationFormAction extends Action {

    private static final String ALREADY_STARTED_ARRAY_COOKIE_KEY = "AlreadyStartedProcessId";

    private static final String ATTRIBUTE_FORM_MAPPING_TYPE = "type";

    private static final String ATTRIBUTE_FORM_MAPPING_TARGET = "target";

    private static final String PROCESS_START_FORM_MAPPING = "PROCESS_START";

    private static final String NONE_FORM_MAPPING_TARGET = "NONE";

    protected final RawView view;

    public CheckFormMappingAndDisplayProcessInstanciationFormAction(final RawView view) {
        this.view = view;
    }

    @Override
    public void execute() {
        final TreeIndexed<String> parameters = view.getParameters();
        searchFormMappingForProcess(parameters);
    }

    protected void setAlreadyStartedCookie(final String processId) {
        final String processIdArrayAsString = Cookies.getCookie(ALREADY_STARTED_ARRAY_COOKIE_KEY);
        final ArrayList<String> processIdArray = new ArrayList<String>();
        processIdArray.add(processId);
        if (processIdArrayAsString != null) {
            final JSONValue jsonValue = JSONParser.parseStrict(processIdArrayAsString);
            final JSONArray jsonArray = jsonValue.isArray();
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    processIdArray.add(jsonArray.get(i).isString().stringValue());
                }
            }
        }
        final String alreadyStartedCookieValue = JSonSerializer.serialize(processIdArray);
        Cookies.setCookie(ALREADY_STARTED_ARRAY_COOKIE_KEY, alreadyStartedCookieValue);
    }

    protected void searchFormMappingForProcess(final TreeIndexed<String> parameters) {
        final String processId = parameters.getValue(ProcessItem.ATTRIBUTE_ID);
        RequestBuilder requestBuilder;
        requestBuilder = new RequestBuilder(RequestBuilder.GET, "../API/form/mapping?c=10&p=0&f=" + PageItem.ATTRIBUTE_PROCESS_ID + "=" + processId + "&f=" +
                ATTRIBUTE_FORM_MAPPING_TYPE + "=" + PROCESS_START_FORM_MAPPING);
        requestBuilder.setCallback(new FormMappingCallback(processId, parameters));
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
            GWT.log("Error while creating the from mapping request", e);
        }
    }

    protected class FormMappingCallback extends APICallback {

        private final String processId;

        private final TreeIndexed<String> parameters;

        public FormMappingCallback(final String processId, final TreeIndexed<String> parameters) {
            this.processId = processId;
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
                    //skip the form and instantiate the process
                    instantiateProcess(processId, parameters);
                } else {
                    //display the form
                    setAlreadyStartedCookie(processId);
                    ViewController.closePopup();
                    ViewController.showView(view.getToken(), parameters);
                }
            } else {
                GWT.log("There is no form mapping for process " + processId);
            }
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            GWT.log("Error while getting the form mapping for process " + processId);
            super.onError(message, errorCode);
        }
    }

    protected void instantiateProcess(final String processId, final TreeIndexed<String> parameters) {
        RequestBuilder requestBuilder;
        requestBuilder = new RequestBuilder(RequestBuilder.POST, "../API/bpm/process/" + processId + "/instantiation");
        requestBuilder.setCallback(new InstantiateProcessCallback(processId));
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
            GWT.log("Error while creating the process instantiation request", e);
        }
    }

    protected class InstantiateProcessCallback extends APICallback {

        private final String processId;

        public InstantiateProcessCallback(final String processId) {
            this.processId = processId;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final JSONValue root = JSONParser.parseLenient(response);
            final JSONObject processInstance = root.isObject();
            final String caseId = processInstance.get("caseId").toString();
            final String confirmationMessage = _("The case %caseId% has been started.", new Arg(
                    "caseId", caseId));
            ViewController.closePopup();
            showConfirmation(confirmationMessage);
            redirectToCaseMoredetails(caseId);
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            if (errorCode == Response.SC_BAD_REQUEST) {
                GWT.log("Error while instantiating process " + processId + " : " + message);
                final String errorMessage = _("Error while trying to start the case. The process may require some values for its contract input.");
                ViewController.closePopup();
                showError(errorMessage);
            } else {
                super.onError(message, errorCode);
            }
        }
    }

    protected void redirectToCaseMoredetails(final String caseId) {
        String caseMoreDetailsToken;
        if (ProcessListingAdminPage.TOKEN.equals(ClientApplicationURL.getPageToken())) {
            caseMoreDetailsToken = CaseMoreDetailsAdminPage.TOKEN;
        } else {
            caseMoreDetailsToken = CaseMoreDetailsPage.TOKEN;
        }
        History.newItem("?_p=" + caseMoreDetailsToken + "&id=" + caseId + "&_pf=" + ClientApplicationURL.getProfileId());
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
