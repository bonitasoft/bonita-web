/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.client.rpc;

import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormURLComponents;
import org.bonitasoft.forms.client.model.ReducedApplicationConfig;
import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.model.ReducedFormPage;
import org.bonitasoft.forms.client.model.ReducedFormValidator;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.ReducedHtmlTemplate;
import org.bonitasoft.web.rest.model.user.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async version of the FormFlow service
 *
 * @author Anthony Birembaut
 *
 */
public interface FormsServiceAsync {

    /**
     * Retrieve the application config including the application template
     *
     * @param formID
     * @param urlContext
     * @param includeApplicationTemplate
     * @param callback
     */
    void getApplicationConfig(String formID, Map<String, Object> urlContext, boolean includeApplicationTemplate,
            AsyncCallback<ReducedApplicationConfig> callback);

    /**
     * Retrieve the first page in the page flow associated with the form
     *
     * @param formID
     * @param callback
     * @param urlContext
     */
    void getFormFirstPage(String formID, final Map<String, Object> urlContext, AsyncCallback<ReducedFormPage> callback);

    /**
     * Retrieve the next page in the page flow associated with the form
     *
     * @param formID
     *            form id
     * @param urlContext
     * @param nextPageExpressionId
     * @param fieldValues
     * @param callback
     */
    void getFormNextPage(String formID, Map<String, Object> urlContext, String nextPageExpressionId, Map<String, FormFieldValue> fieldValues,
            AsyncCallback<ReducedFormPage> callback);

    /**
     * Validate some form field values using the validators provided
     *
     * @param formID
     *            a form page id
     * @param urlContext
     *            url parameters map
     * @param validatorsMap
     *            Map of validators to use. Each entry of the map contains the ID of the field to validate and an id allowing to retrieve the field validators
     *            in the cache
     * @param widgetValues
     *            a map of the fields ids and values
     * @param submitButtonId
     *            the submit button ID
     * @param aCallBackHandler
     */
    void validateFormFields(String formID, Map<String, Object> urlContext, Map<String, String> validatorsMap, Map<String, FormFieldValue> widgetValues,
            String submitButtonId, AsyncCallback<Map<String, List<ReducedFormValidator>>> aCallBackHandler);

    /**
     * Validate a form page using the validators provided
     *
     * @param formID
     *            a form page id
     * @param urlContext
     *            combin url param to context
     * @param pageValidatorsId
     *            id allowing to retrieve the page validators in the cache
     * @param fields
     *            a map of the fields ids and values
     * @param submitButtonId
     * @param aCallBackHandler
     */
    void validateFormPage(String formID, Map<String, Object> urlContext, String pageValidatorsId, Map<String, FormFieldValue> fields, String submitButtonId,
            AsyncCallback<List<ReducedFormValidator>> aCallBackHandler);

    /**
     * Retrieve the confirmation page for a form
     *
     * @param formID
     *            the form id
     * @param urlContext
     *            Map containing the URL parameters
     * @param aCallBackHandler
     */
    void getFormConfirmationTemplate(String formID, Map<String, Object> urlContext, Map<String, FormFieldValue> fields, AsyncCallback<ReducedHtmlTemplate> aCallBackHandler);

    /**
     * Retrieve the error page for a application
     *
     * @param formID
     *            form id
     * @param urlContext
     *            Map containing the URL parameters
     * @param callback
     */
    void getApplicationErrorTemplate(String formID, Map<String, Object> urlContext, AsyncCallback<ReducedHtmlTemplate> callback);

    /**
     * start terminate a task and set a number of variables specifying the pressed submit button id
     * (this way, only actions related to this button will be performed)
     *
     * @param formID
     *            current id of form
     * @param urlContext
     *            Map containing the URL parameters
     * @param fieldValues
     *            variables a Map of the fields ids and values
     * @param pageIds
     *            the page flow followed by the user
     * @param submitButtonId
     *            the pressed submit button id
     * @param aCallBackHandler
     */
    void executeActions(String formID, Map<String, Object> urlContext, Map<String, FormFieldValue> fieldValues, List<String> pageIds, String submitButtonId,
            AsyncCallback<Map<String, Object>> aCallBackHandler);

    /**
     * Skip a form
     *
     * @param formID
     *            current form ID
     * @param urlContext
     *            Map containing the URL parameters
     * @param aCallBackHandler
     */
    void skipForm(String formID, Map<String, Object> urlContext, AsyncCallback<Map<String, Object>> aCallBackHandler);

    /**
     * Retrieve the next task uuid if it is in the user task list and form id
     *
     * @param formID
     *            form id
     * @param urlContext
     *            Map containing the URL parameters
     * @param aCallBackHandler
     */
    void getNextFormURL(String formID, Map<String, Object> urlContext, AsyncCallback<FormURLComponents> aCallBackHandler);

    /**
     * Assign the form specified in the URL parameters to the logged in user
     *
     * @param formID form id
     * @param urlContext Map containing the URL parameters
     * @param aCallBackHandler
     */
    void assignForm(String formID, Map<String, Object> urlContext, AsyncCallback<Void> aCallBackHandler);

    /**
     * Get async available values
     *
     * @param formID
     *        form id
     * @param urlContext
     *        Map containing the URL parameters
     * @param formWidget
     *        the widget definition
     * @param currentFieldValue
     *        the current value of the widget
     * @param asyncCallback
     */
    void getFormAsyncAvailableValues(String formID, Map<String, Object> urlContext, ReducedFormWidget formWidget, FormFieldValue currentFieldValue,
            AsyncCallback<List<ReducedFormFieldAvailableValue>> asyncCallback);

    /**
     * Get any todolist form URL
     *
     * @param urlContext
     *            Map containing the URL parameters
     * @param asyncCallback
     */
    void getAnyTodoListForm(Map<String, Object> urlContext, AsyncCallback<FormURLComponents> asyncCallback);

    /**
     * Retrieve the logged in user
     *
     * @param asyncCallback
     */
    void getLoggedInUser(AsyncCallback<User> asyncCallback);

    /**
     * generate a token to change application
     *
     * @param asyncCallback
     */
    void generateTemporaryToken(AsyncCallback<String> asyncCallback);

    /**
     * Logout from the application
     *
     * @param asyncCallback
     */
    void logout(AsyncCallback<Void> asyncCallback);}
