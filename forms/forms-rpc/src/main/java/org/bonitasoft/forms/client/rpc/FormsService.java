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
import org.bonitasoft.forms.client.model.exception.AbortedFormException;
import org.bonitasoft.forms.client.model.exception.CanceledFormException;
import org.bonitasoft.forms.client.model.exception.FileTooBigException;
import org.bonitasoft.forms.client.model.exception.ForbiddenApplicationAccessException;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.client.model.exception.FormAlreadySubmittedException;
import org.bonitasoft.forms.client.model.exception.FormInErrorException;
import org.bonitasoft.forms.client.model.exception.IllegalActivityTypeException;
import org.bonitasoft.forms.client.model.exception.MigrationProductVersionNotIdenticalException;
import org.bonitasoft.forms.client.model.exception.RPCException;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.client.model.exception.SkippedFormException;
import org.bonitasoft.forms.client.model.exception.SuspendedFormException;
import org.bonitasoft.web.rest.model.user.User;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Form flow service Helps building the forms application
 *
 * @author Anthony Birembaut
 *
 */
public interface FormsService extends RemoteService {

    /**
     * Retrieve the application config including the application template
     *
     * @param formID
     * @param urlContext
     * @param includeApplicationTemplate
     * @throws SessionTimeoutException
     * @throws RPCException
     * @throws ForbiddenFormAccessException
     * @throws MigrationProductVersionNotIdenticalException
     */
    ReducedApplicationConfig getApplicationConfig(String formID, Map<String, Object> urlContext, boolean includeApplicationTemplate) throws RPCException,
            SessionTimeoutException, ForbiddenApplicationAccessException, MigrationProductVersionNotIdenticalException;

    /**
     * Retrieve the first page in the page flow associated with the form
     *
     * @param formID
     * @param urlContext
     * @return
     * @throws SessionTimeoutException
     * @throws RPCException
     * @throws SuspendedFormException
     * @throws CanceledFormException
     * @throws FormAlreadySubmittedException
     * @throws ForbiddenFormAccessException
     * @throws FormInErrorException
     * @throws MigrationProductVersionNotIdenticalException
     * @throws SkippedFormException
     * @throws AbortedFormException
     */
    ReducedFormPage getFormFirstPage(String formID, final Map<String, Object> urlContext) throws SessionTimeoutException, RPCException, SuspendedFormException,
            CanceledFormException, FormAlreadySubmittedException, ForbiddenFormAccessException, FormInErrorException,
            MigrationProductVersionNotIdenticalException, SkippedFormException, AbortedFormException;

    /**
     * Retrieve the next page in the page flow associated with the form
     *
     * @param nextPageExpressionId
     *            next form id expression Id
     * @param urlContext
     *            Map containing the URL parameters
     * @param pageId
     *            the current page Id
     * @param fieldValues
     *            the current page's fields values
     *
     * @throws SessionTimeoutException
     * @throws RPCException
     * @throws SuspendedFormException
     * @throws CanceledFormException
     * @throws FormAlreadySubmittedException
     * @throws ForbiddenFormAccessException
     * @throws FormInErrorException
     * @throws SkippedFormException
     * @throws AbortedFormException
     */
    ReducedFormPage getFormNextPage(String formID, Map<String, Object> urlContext, String nextPageExpressionId, Map<String, FormFieldValue> fieldValues)
            throws RPCException, SessionTimeoutException, FormAlreadySubmittedException, SuspendedFormException, CanceledFormException,
            ForbiddenFormAccessException, FormInErrorException,
            SkippedFormException, AbortedFormException;

    /**
     * Validate a form field value using the validators provided
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
     * @return The list of validators whose validate method returned false
     * @throws RPCException
     * @throws SessionTimeoutException
     */
    Map<String, List<ReducedFormValidator>> validateFormFields(String formID, Map<String, Object> urlContext, Map<String, String> validatorsMap,
            Map<String, FormFieldValue> widgetValues, String submitButtonId) throws RPCException, SessionTimeoutException;

    /**
     * Validate a form page using the validators provided
     *
     * @param formID
     *            a form page id
     * @param urlContext
     *            url parameters map
     * @param pageValidatorsId
     *            id allowing to retrieve the page validators in the cache
     * @param fields
     *            a map of the fields ids and values
     * @param submitButtonId
     *            the submit button ID
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws RPCException
     * @throws SessionTimeoutException
     */
    List<ReducedFormValidator> validateFormPage(String formID, Map<String, Object> urlContext, String pageValidatorsId, Map<String, FormFieldValue> fields,
            String submitButtonId) throws RPCException, SessionTimeoutException;

    /**
     * Retrieve the confirmation page for a form
     *
     * @param formID
     *        the form id
     * @param urlContext
     *        Map containing the URL parameters
     * @param fields a map of the fields ids and values
     * @return an {@link ReducedHtmlTemplate} object representing the page flow confirmation page
     * @throws RPCException
     * @throws SessionTimeoutException
     */
    ReducedHtmlTemplate getFormConfirmationTemplate(String formID, Map<String, Object> urlContext, Map<String, FormFieldValue> fields) throws RPCException,
            SessionTimeoutException;

    /**
     * Retrieve the error page for a formID
     *
     * @param formID
     *            form id
     * @param urlContext
     *            Map containing the URL parameters
     * @return an {@link ReducedHtmlTemplate} object representing the error page
     * @throws RPCException
     * @throws SessionTimeoutException
     */
    ReducedHtmlTemplate getApplicationErrorTemplate(String formID, Map<String, Object> urlContext) throws RPCException, SessionTimeoutException;

    /**
     * Skip a form
     *
     * @param formID
     *            current form ID
     * @param urlContext
     *            Map containing the URL parameters
     * @return a Map containing the new URL parameters
     * @throws RPCException
     * @throws SessionTimeoutException
     * @throws FormAlreadySubmittedException
     * @throws IllegalActivityTypeException
     */
    Map<String, Object> skipForm(String formID, Map<String, Object> urlContext) throws RPCException, SessionTimeoutException, FormAlreadySubmittedException,
            IllegalActivityTypeException;

    /**
     * start terminate a task and execute a number of actions specifying the pressed submit button id (this way, only actions
     * related to this button will be performed)
     *
     * @param formID
     *            form id
     * @param urlContext
     *            Map containing the URL parameters
     * @param fieldValues
     *            a Map of the fields ids and values
     * @param submitButtonId
     *            the pressed submit button id
     * @throws RPCException
     * @throws FileTooBigException
     * @throws SessionTimeoutException
     * @throws FormAlreadySubmittedException
     */
    Map<String, Object> executeActions(String formID, Map<String, Object> urlContext, Map<String, FormFieldValue> fieldValues, List<String> pageIds,
            String submitButtonId) throws RPCException, SessionTimeoutException, FileTooBigException,
            FormAlreadySubmittedException;

    /**
     * Retrieve the next task uuid if it is in the user task list and form id
     *
     * @param formID
     *            form id
     * @param urlContext
     *            Map containing the URL parameters
     * @return the next Form URL components or null there is no next task or if the next task is not in the user todolist
     * @throws RPCException
     * @throws SessionTimeoutException
     */
    FormURLComponents getNextFormURL(String formID, Map<String, Object> urlContext) throws RPCException, SessionTimeoutException;

    /**
     * Get async available values
     *
     * @param formID
     *            form id
     * @param urlContext
     *            Map containing the URL parameters
     * @param formWidget
     *            the widget definition
     * @param currentFieldValue
     *            the current value of the widget
     * @return the new list of available values
     * @throws RPCException
     * @throws SessionTimeoutException
     */
    List<ReducedFormFieldAvailableValue> getFormAsyncAvailableValues(String formID, Map<String, Object> urlContext, ReducedFormWidget formWidget,
            FormFieldValue currentFieldValue) throws RPCException, SessionTimeoutException;

    /**
     * Get any todolist form URL
     *
     * @param urlContext
     *        Map containing the URL parameters
     * @return new FormURLComponents containing the URL parameters
     * @throws RPCException
     * @throws SessionTimeoutException
     */
    FormURLComponents getAnyTodoListForm(Map<String, Object> urlContext) throws RPCException, SessionTimeoutException;

    /**
     * Assign the form specified in the URL parameters to the logged in user
     *
     * @param formID form Id
     * @param urlContext Map containing the URL parameters
     * @throws RPCException
     * @throws SessionTimeoutException
     * @throws ForbiddenFormAccessException
     */
    void assignForm(String formID, Map<String, Object> urlContext) throws RPCException, SessionTimeoutException, ForbiddenFormAccessException;

    /**
     * Retrieve the logged in user
     *
     * @return the logged in {@link User}
     */
    User getLoggedInUser() throws RPCException, SessionTimeoutException;

    /**
     * generate a token to change application
     *
     * @return the token
     */
    String generateTemporaryToken() throws RPCException, SessionTimeoutException;

    /**
     * Logout from the application
     */
    void logout() throws RPCException, SessionTimeoutException;

}
