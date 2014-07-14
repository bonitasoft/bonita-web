/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.provider;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormURLComponents;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.exception.AbortedFormException;
import org.bonitasoft.forms.client.model.exception.CanceledFormException;
import org.bonitasoft.forms.client.model.exception.ForbiddenApplicationAccessException;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.client.model.exception.FormAlreadySubmittedException;
import org.bonitasoft.forms.client.model.exception.FormInErrorException;
import org.bonitasoft.forms.client.model.exception.IllegalActivityTypeException;
import org.bonitasoft.forms.client.model.exception.MigrationProductVersionNotIdenticalException;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.client.model.exception.SkippedFormException;
import org.bonitasoft.forms.client.model.exception.SuspendedFormException;
import org.bonitasoft.forms.server.accessor.IApplicationConfigDefAccessor;
import org.bonitasoft.forms.server.accessor.IApplicationFormDefAccessor;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.FormInitializationException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.exception.FormSubmissionException;
import org.bonitasoft.forms.server.exception.FormValidationException;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.bonitasoft.forms.server.exception.NoCredentialsInSessionException;
import org.bonitasoft.forms.server.exception.TaskAssignationException;
import org.bonitasoft.web.rest.model.user.User;
import org.w3c.dom.Document;

/**
 * This interface can be implemented by anyone willing to use the forms in a different context than the one of the BPM engine.<br/>
 * 
 * The context variable used in the interface described below contains the URL parameters, the username, the values of the form fields (when necessary)...<br/>
 * Context specific keys:<br/>
 * user : the user as a {@link User} object
 * locale : the locale of the client as a {@link Locale} object
 * urlContext : map of the URL parameters (Map<String, String>)
 * fieldValues : map of the form field values (Map<String, {@link FormFieldValue}>). The keys of the map are the Ids of the field.
 * transientDataContext : the context of transient data (Map<String, Object>). The keys of the map are the names of the data.
 * 
 * The implementation of FormServiceProvider to use can be configured in the file:<br/>
 * BONITA_HOME/client/web/forms/conf/forms-config.properties<br/>
 * with the property form.service.provider<br/>
 * If the default implementation of FormServiceProvider is not used, the deployment descriptor (web.xml) of the webapps (both user XP and process applications)
 * needs to be modified in order to remove the filter BPMURLFilter. This filter is used to add the form ID in the URL if it is not present (in order to support
 * the old URL format).<br/>
 * 
 * @author Anthony Birembaut
 */
public interface FormServiceProvider {

    /**
     * Retrieve the Document containing the definition of the forms application.<br/>
     * If you use a dedicated web archive (.war) for each forms application, the forms.xml file will be in the classpath (in WEB-INF/classes), so you can read
     * it from there.
     * 
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the {@link Document} associated to the form definition
     * @throws FormNotFoundException
     *             if no form definition file was found
     * @throws IOException
     *             if an error occurs while reading the form definition file
     * @throws InvalidFormDefinitionException
     *             if the content of the form definition file is invalid
     * @throws SessionTimeoutException
     */
    Document getFormDefinitionDocument(Map<String, Object> context) throws FormNotFoundException, IOException, InvalidFormDefinitionException,
            SessionTimeoutException;

    /**
     * Check if the user is allowed to do an action.
     * 
     * @param formId
     *            the form ID
     * @param permissions
     *            The permission string
     * @param productVersion
     *            The product version
     * @param migrationProductVersion
     *            The migration product version
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @param isFormPermissions
     *            indicates if the permissions are permissions to display a form or not (application permissions)
     * @return true if allowed, otherwise return false
     * @throws ForbiddenFormAccessException
     *             if the user doesn't have the rights to see the form
     * @throws SuspendedFormException
     *             if the access to the form is suspended
     * @throws CanceledFormException
     *             if the form doesn't have to be filled in anymore
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws FormAlreadySubmittedException
     *             if the form has already been submitted and cannot be submitted twice
     * @throws ForbiddenApplicationAccessException
     *             if the user isn't allowed tu access the forms application
     * @throws FormInErrorException
     * @throws MigrationProductVersionNotIdenticalException
     * @throws NoCredentialsInSessionException
     * @throws SkippedFormException
     * @throws SessionTimeoutException
     * @throws TaskAssignationException
     */
    boolean isAllowed(String formId, String permissions, String productVersion, String migrationProductVersion, Map<String, Object> context,
            boolean isFormPermissions) throws ForbiddenFormAccessException, SuspendedFormException, CanceledFormException, FormNotFoundException,
            FormAlreadySubmittedException, ForbiddenApplicationAccessException, FormInErrorException, MigrationProductVersionNotIdenticalException,
            NoCredentialsInSessionException, SkippedFormException, SessionTimeoutException, TaskAssignationException, AbortedFormException;

    /**
     * Resolve an expression (Groovy in the default implementation, but it can be anything in another implementation).
     * 
     * @param expression
     *            The expression to be resolved
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the resolved value
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws FileTooBigException
     * @throws FormInitializationException 
     */
    Serializable resolveExpression(Expression expression, Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException,
            FileTooBigException, IOException, FormInitializationException;

    /**
     * Resolve a group of expressions (Groovy in the default implementation, but it can be anything in another implementation).
     * 
     * @param expressions
     *            The expressions to be resolved
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the Map of resolved values
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws FileTooBigException
     * @throws FormInitializationException 
     */
    Map<String, Serializable> resolveExpressions(List<Expression> expressions, Map<String, Object> context) throws FormNotFoundException,
            SessionTimeoutException, FileTooBigException, IOException, FormInitializationException;

    /**
     * Execute some actions after a form submission.
     * 
     * @param actions
     *            A list of {@link FormAction} to execute at form validation
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the new context
     * @throws FileTooBigException
     *             if a file is too big to be uploaded
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws Exception
     *             if any other kind of exception occurs
     */
    Map<String, Object> executeActions(List<FormAction> actions, Map<String, Object> context) throws FileTooBigException, FormNotFoundException,
            FormAlreadySubmittedException, FormSubmissionException, SessionTimeoutException, IOException;

    /**
     * Retrieve the next form ID and additional parameters required in the URL to display the next form after a form submission.
     * 
     * @param formID
     *            the form ID
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return FormURLComponents the map of URL parameters
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     */
    FormURLComponents getNextFormURLParameters(final String formID, Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException;

    /**
     * Retrieve the attributes to insert in a page.
     * 
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return a {@link Map} of attributes to insert in the page
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     */
    Map<String, String> getAttributesToInsert(Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException;

    /**
     * Validate a form field (the implementation is responsible for instantiating the right validator based on the classname in the FormValidator object).
     * 
     * @param validators
     *            List of validators to use
     * @param fieldId
     *            The id of the field
     * @param fieldValue
     *            The value of the field
     * @param submitButtonId
     *            The submit button ID
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     *             if an error occurs during the field validation
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     */
    List<FormValidator> validateField(List<FormValidator> validators, String fieldId, FormFieldValue fieldValue, String submitButtonId,
            Map<String, Object> context) throws FormValidationException, FormNotFoundException, SessionTimeoutException, FileTooBigException, IOException, BPMExpressionEvaluationException;

    /**
     * Validate a form page with several fields
     * 
     * @param validators
     *            List of validators to use
     * @param fields
     *            Map containing the fields ids and values
     * @param submitButtonId
     *            The submit button ID
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     *             if an error occurs during the page validation
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     */
    List<FormValidator> validatePage(List<FormValidator> validators, Map<String, FormFieldValue> fields, String submitButtonId, Map<String, Object> context)
            throws FormValidationException, FormNotFoundException, SessionTimeoutException, FileTooBigException, IOException, BPMExpressionEvaluationException;

    /**
     * Get the date at which the form application was deployed (this is used to clear the form definition cache in case a new application is deployed).<br/>
     * The implementation of this method can return null but the cache directory in BONITA_HOME will have to be cleared manually and the server restarted in
     * case a new version of the application is deployed
     * 
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the deployment {@link Date}
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws IOException
     * @throws SessionTimeoutException
     */
    Date getDeployementDate(Map<String, Object> context) throws FormNotFoundException, IOException, SessionTimeoutException;

    /**
     * Get an application form definition accessor object (reader to access a form definition for a specific form ID).<br/>
     * In case the form definition is only based on a forms.xml file (valid with the forms.xsd) this method can just return:
     * FormDefAccessorFactory.getXMLApplicationFormDefAccessor(formId, formDefinitionDocument, applicationDeploymentDate)
     * 
     * @param formId
     *            the form Id
     * @param formDefinitionDocument
     *            the document
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return an instance of {@link IApplicationFormDefAccessor}
     * @throws ApplicationFormDefinitionNotFoundException
     *             if the forms application definition cannot be found
     * @throws InvalidFormDefinitionException
     *             if the content of the form definition file is invalid
     * @throws SessionTimeoutException
     */
    IApplicationFormDefAccessor getApplicationFormDefinition(final String formId, final Document formDefinitionDocument, final Map<String, Object> context)
            throws ApplicationFormDefinitionNotFoundException, InvalidFormDefinitionException, SessionTimeoutException;

    /**
     * Get an application configuration definition accessor object (reader to access the forms application configuration).<br/>
     * In case the form definition is only based on an xml file (valid with the forms.xsd) this method can just return: new
     * XMLApplicationConfigDefAccessorImpl(formDefinitionDocument)
     * 
     * @param formDefinitionDocument
     *            the document
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return an instance of {@link IApplicationConfigDefAccessor}
     * @throws SessionTimeoutException
     * @throws ApplicationFormDefinitionNotFoundException
     */
    IApplicationConfigDefAccessor getApplicationConfigDefinition(Document formDefinitionDocument, Map<String, Object> context) throws SessionTimeoutException,
            ApplicationFormDefinitionNotFoundException;

    /**
     * Get the form field value for an attachment widget
     * 
     * @param value
     *            the value returned by the expression evaluation
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the form field value
     * @throws FileTooBigException
     * @throws IOException
     * @throws FormInitializationException 
     */
    FormFieldValue getAttachmentFormFieldValue(Object value, Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException, IOException,
            FileTooBigException, FormInitializationException;

    /**
     * Check if a specific form is in edit mode (entry form) or not (view form).<br/>
     * This method is used mostly to add an attribute in the context Map passed to the the expression resolution and connector execution methods (because you
     * might need this information at this point).<br/>
     * This method is required because if you don't use an XML definition for a form (like we do for the automatic form generation), you need to access your
     * back-end to know if the the form displayed is in edit or view mode.
     * 
     * @param formID
     *            Current form id
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return true if the form is in edit mode, otherwise return false
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     */
    boolean isEditMode(final String formID, final Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException;

    /**
     * Check if the values to display in a specific form should be the current values or previous values (for view forms based on an history for example).<br/>
     * This method is used mostly to add an attribute in the context map passed to the the expression resolution and connector execution methods (because you
     * might need this information at this point).<br/>
     * This method is required because if you don't use an XML definition for a form (like we do for the automatic form generation), you need to access your
     * back-end to know if the the form displayed needs to use current values or previous values.
     * 
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return true if the form contains user input data, otherwise return false
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws SessionTimeoutException
     */
    boolean isCurrentValue(final Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException;

    /**
     * Skip a form.<br/>
     * If the method getFirstPage of the implementation of IApplicationFormDefAccessor used returns null, it means the form needs to be skipped.<br/>
     * This methods should perform any operation required when a form is skipped (in the default BPM implementation, we just execute the task).<br/>
     * The map returned should contain the new URL parameters that will be set client-side.<br/>
     * This method is used in a very specific use case. If you don't need this feature, you can just return an empty map in your implementation of
     * {@link FormServiceProvider}.
     * 
     * @param formID
     *            The form id to be skipped
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return a {@link Map} of URL parameters
     * @throws FormNotFoundException
     *             if the form cannot be found
     * @throws IllegalActivityTypeException
     *             if something prevent the form to be skipped
     * @throws FormSubmissionException
     * @throws SessionTimeoutException
     */
    Map<String, Object> skipForm(final String formID, final Map<String, Object> context) throws FormNotFoundException, FormAlreadySubmittedException,
            IllegalActivityTypeException, FormSubmissionException, SessionTimeoutException;

    /**
     * Get any form from a to do list.<br/>
     * If there is no form ID in the URL this method is used to try to retrieve a form to display.<br/>
     * The map returned should contain the new URL parameters that will be set client-side (including or not a form ID).
     * 
     * @param context
     *            Map of context (containing the URL parameters and other data) (including the formID)
     * @return a {@link Map} of URL parameters. This Map should be empty if there are no forms to display
     * @throws FormNotFoundException
     * @throws SessionTimeoutException
     */
    Map<String, Object> getAnyTodoListForm(final Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException;

    /**
     * Get the directory in which the application resources (like process layout, CSS...) are deployed.<br/>
     * This is not required if you use a dedicated web archive (.war) for each forms application. In this case the HTML files will be loaded from the classpath
     * and the CSS and other web resources in the application directory of the web archive.
     * 
     * @param applicationDeploymentDate
     *            the application deployment date
     * @param context
     *            context Map of context (containing the URL parameters and other data)
     * @return a {@link File} corresponding to the directory containing the application resources
     * @throws IOException
     *             if an error occurs while reading the application resources dir
     * @throws SessionTimeoutException
     * @throws ApplicationFormDefinitionNotFoundException
     */
    File getApplicationResourceDir(Date applicationDeploymentDate, Map<String, Object> context) throws IOException, SessionTimeoutException,
            ApplicationFormDefinitionNotFoundException;

    /**
     * retrieve the right classloader for the context
     * This method is useful if you need to load some classes because some custom java object that are not in the classpath are used in the validators/transient
     * data
     * 
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the {@link ClassLoader} for the given context
     * @throws SessionTimeoutException
     * @throws FormNotFoundException
     */
    ClassLoader getClassloader(final Map<String, Object> context) throws SessionTimeoutException, FormNotFoundException;

    /**
     * Store the transient data context in the HTTP session
     * 
     * @param session
     *            HTTP session
     * @param storageKey
     *            key for the session entry
     * @param transientDataContext
     *            transient data context to store
     * @param context
     *            Map of context (containing the URL parameters and other data)
     */
    void storeFormTransientDataContext(HttpSession session, String storageKey, Map<String, Serializable> transientDataContext, Map<String, Object> context);

    /**
     * Retrieve a transient data context for the current form
     * 
     * @param session
     *            HTTP session
     * @param storageKey
     *            key for the session entry
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the transient data context to use
     */
    Map<String, Serializable> retrieveFormTransientDataContext(HttpSession session, String storageKey, Map<String, Object> context);

    /**
     * remove a transient data context form the HTTP session
     * 
     * @param session
     *            HTTP session
     * @param storageKey
     *            key for the session entry
     * @param context
     *            Map of context (containing the URL parameters and other data)
     * @return the transient data context to use
     */
    void removeFormTransientDataContext(HttpSession session, String storageKey, Map<String, Object> context);
}
