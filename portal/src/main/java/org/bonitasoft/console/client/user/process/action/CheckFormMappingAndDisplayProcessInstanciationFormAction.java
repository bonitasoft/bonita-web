/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.process.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.RequestBuilder;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.popup.PopupCloseAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormSubmitButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;

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
        final String processIdFilter = URL.encodeQueryString(PageItem.ATTRIBUTE_PROCESS_ID + "=" + processId);
        final String mappingTypeFilter = URL.encodeQueryString(ATTRIBUTE_FORM_MAPPING_TYPE + "=" + PROCESS_START_FORM_MAPPING);
        final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "../API/form/mapping?c=10&p=0&f=" + processIdFilter + "&f="
                + mappingTypeFilter);
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
        final String processDisplayName = parameters.getValue(ProcessItem.ATTRIBUTE_DISPLAY_NAME);
        ViewController.showPopup(new StartCaseConfirmationPopup(processId, processDisplayName));
    }

    protected class StartCaseConfirmationPopup extends Page {

        public final static String TOKEN = "skipProcessForm";

        private final String processId;

        private final String processDisplayName;

        public StartCaseConfirmationPopup(final String processId, final String processDisplayName) {
            this.processId = processId;
            this.processDisplayName = processDisplayName;
        }

        @Override
        public void defineTitle() {
            this.setTitle(processDisplayName);
        }

        @Override
        public String defineToken() {
            return TOKEN;
        }

        @Override
        public void buildView() {
            addBody(new Paragraph(_("You are going to start a new case. No form is needed.")));
            final Container<Button> formactions = new Container<Button>();
            addBody(formactions.addClass("formactions"));
            formactions.append(
                    new FormSubmitButton(new JsId("btn-primary-action"), _("Start"), _("Start a case"), new Action() {

                        @Override
                        public void execute() {
                            performInstantiationRequest(processId);
                        }
                    }),
                    new Button(_("cancel"), _("Cancel this action"), new PopupCloseAction()));
        }
    }

    protected void performInstantiationRequest(final String processId) {
        final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, "../API/bpm/process/" + processId + "/instantiation");
        final ProcessInstantiationCallbackBehavior processInstantiationCallbackBehavior = new ProcessInstantiationCallbackBehavior();
        requestBuilder.setCallback(new InstantiateProcessCallback(processInstantiationCallbackBehavior));
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
            GWT.log("Error while creating the process instantiation request", e);
        }
    }

    protected class InstantiateProcessCallback extends APICallback {

        private final ProcessInstantiationCallbackBehavior processInstantiationCallbackBehavior;

        public InstantiateProcessCallback(final ProcessInstantiationCallbackBehavior processInstantiationCallbackBehavior) {
            this.processInstantiationCallbackBehavior = processInstantiationCallbackBehavior;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            processInstantiationCallbackBehavior.onSuccess(response);
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            processInstantiationCallbackBehavior.onError(message, errorCode);
        }
    }
}
