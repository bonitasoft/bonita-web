/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.forms.server;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.sso.InternalSSOManager;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.ApplicationConfig;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormPage;
import org.bonitasoft.forms.client.model.FormURLComponents;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.HtmlTemplate;
import org.bonitasoft.forms.client.model.ReducedApplicationConfig;
import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.model.ReducedFormPage;
import org.bonitasoft.forms.client.model.ReducedFormValidator;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.ReducedHtmlTemplate;
import org.bonitasoft.forms.client.model.TransientData;
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
import org.bonitasoft.forms.client.rpc.FormsService;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtilFactory;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormDefinitionAPI;
import org.bonitasoft.forms.server.api.impl.util.FormFieldValuesUtil;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.FormInitializationException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.exception.NoCredentialsInSessionException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderFactory;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.bonitasoft.web.rest.model.user.User;
import org.w3c.dom.Document;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Servlet implementing the Forms service for async calls
 * 
 * @author Anthony Birembaut, Qixiang Zhang, Vincent Elcrin
 */
public class FormsServlet extends RemoteServiceServlet implements FormsService {

    /**
     * UID
     */
    private static final long serialVersionUID = -638652293467914223L;

    /**
     * page flow transient data param key inside the session
     */
    public static final String TRANSIENT_DATA_SESSION_PARAM_KEY_PREFIX = "transientData-";

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormsServlet.class.getName());

    /**
     * FormFieldValuesUtil
     */
    private final FormFieldValuesUtil formFieldValuesUtil = new FormFieldValuesUtil();

    /**
     * The cookie name for the forms locale
     */
    public static final String FORM_LOCALE_COOKIE_NAME = "BOS_Locale";

    /**
     * the engine API session param key name
     */
    public static final String API_SESSION_PARAM_KEY = "apiSession";

    /**
     * the user session param key name
     */
    public static final String USER_SESSION_PARAM_KEY = "user";

    /**
     * {@inheritDoc}
     */
    @Override
    public String processCall(final String payload) throws SerializationException {
        try {
            return super.processCall(payload);
        } catch (final SerializationException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "The Object returned by the RPC call is not supported by the client. Complex java types and XML types are not supported as data field's inputs.",
                    e);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReducedApplicationConfig getApplicationConfig(final String formID, final Map<String, Object> urlContext, final boolean includeApplicationTemplate)
            throws RPCException, SessionTimeoutException, ForbiddenApplicationAccessException, MigrationProductVersionNotIdenticalException {
        final String localeStr = getLocale();
        final Map<String, Object> context = initContext(urlContext, resolveLocale(localeStr));
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            final Document document = formServiceProvider.getFormDefinitionDocument(context);
            final Date deployementDate = formServiceProvider.getDeployementDate(context);
            final IFormDefinitionAPI definitionAPI = FormAPIFactory.getFormDefinitionAPI(tenantID, document, deployementDate, localeStr);
            final String permissions = definitionAPI.getApplicationPermissions(formID, context);
            final String productVersion = definitionAPI.getProductVersion(context);
            final String migrationProductVersion = definitionAPI.getMigrationProductVersion(formID, context);
            formServiceProvider.isAllowed(formID, permissions, productVersion, migrationProductVersion, context, false);
            final ApplicationConfig applicationConfig = definitionAPI.getApplicationConfig(context, formID, includeApplicationTemplate);
            return resolveApplicationConfigExpressions(formServiceProvider, context, applicationConfig).getReducedApplicationConfig();
        } catch (final ForbiddenApplicationAccessException e) {
            throw new ForbiddenApplicationAccessException(e);
        } catch (final MigrationProductVersionNotIdenticalException e) {
            throw new MigrationProductVersionNotIdenticalException(e);
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting the Process Config from a process instance", e);
            }
            throw new RPCException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public ReducedFormPage getFormFirstPage(final String formID, final Map<String, Object> urlContext) throws SessionTimeoutException, RPCException,
            SuspendedFormException, CanceledFormException, FormAlreadySubmittedException, ForbiddenFormAccessException, FormInErrorException,
            MigrationProductVersionNotIdenticalException, SkippedFormException, AbortedFormException {
        final String localeStr = getLocale();
        final Locale userLocale = resolveLocale(localeStr);
        final Map<String, Object> context = initContext(urlContext, userLocale);
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            final Document document = formServiceProvider.getFormDefinitionDocument(context);
            final Date deployementDate = formServiceProvider.getDeployementDate(context);
            final boolean isEditMode = formServiceProvider.isEditMode(formID, context);
            context.put(FormServiceProviderUtil.IS_EDIT_MODE, isEditMode);
            final boolean isCurrentValue = formServiceProvider.isCurrentValue(context);
            context.put(FormServiceProviderUtil.IS_CURRENT_VALUE, isCurrentValue);
            final IFormDefinitionAPI definitionAPI = FormAPIFactory.getFormDefinitionAPI(tenantID, document, deployementDate, localeStr);
            final String permissions = definitionAPI.getFormPermissions(formID, context);
            final String productVersion = definitionAPI.getProductVersion(context);
            final String migrationProductVersion = definitionAPI.getMigrationProductVersion(formID, context);
            formServiceProvider.isAllowed(formID, permissions, productVersion, migrationProductVersion, context, true);
            final Expression pageIdExpression = definitionAPI.getFormFirstPage(formID, context);
            FormPage formPage = null;
            if (pageIdExpression != null) {
                setClassloader(formServiceProvider, context);
                final List<TransientData> transientData = definitionAPI.getFormTransientData(formID, context);
                final Map<String, Serializable> transientDataContext = definitionAPI.getTransientDataContext(transientData, userLocale, context);
                setFormTransientDataContext(formServiceProvider, formID, transientDataContext, context);
                context.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, transientDataContext);
                context.put(FormServiceProviderUtil.FIELD_VALUES, new HashMap<String, FormFieldValue>());
                final String pageId = (String) formServiceProvider.resolveExpression(pageIdExpression, context);
                if (pageId != null) {
                    formPage = definitionAPI.getFormPage(formID, pageId, context);
                    if (formPage != null) {
                        formPage.setPageLabel((String) formServiceProvider.resolveExpression(formPage.getPageLabelExpression(), context));
                        formFieldValuesUtil.setFormWidgetsValues(tenantID, formPage.getFormWidgets(), context);
                        formFieldValuesUtil.storeWidgetsInCacheAndSetCacheID(tenantID, formID, pageId, localeStr, deployementDate, formPage.getFormWidgets());
                    }
                } else {
                    throw new IllegalStateException("The first Form page cannot be calculated for " + formID
                            + ". This is more likely to be a design issue of conditional pageflow.");
                }
            }
            if (formPage == null) {
                return null;
            } else {
                return formPage.getReducedFormPage();
            }
        } catch (final ApplicationFormDefinitionNotFoundException e) {
            throw new ForbiddenFormAccessException(e);
        } catch (final ForbiddenFormAccessException e) {
            throw e;
        } catch (final MigrationProductVersionNotIdenticalException e) {
            throw new MigrationProductVersionNotIdenticalException(e);
        } catch (final CanceledFormException e) {
            throw new CanceledFormException(e);
        } catch (final SuspendedFormException e) {
            throw new SuspendedFormException(e);
        } catch (final FormInErrorException e) {
            throw new FormInErrorException(e);
        } catch (final SkippedFormException e) {
            throw new SkippedFormException(e);
        } catch (final AbortedFormException e) {
            throw new AbortedFormException(e);
        } catch (final FormAlreadySubmittedException e) {
            throw new FormAlreadySubmittedException(e);
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting the first page for application " + formID, e);
            }
            throw new RPCException(e.getMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * Set the classloader matching the given context
     * 
     * @param formServiceProvider
     *            the form service provider
     * @param context
     *            the context (including URL parameters)
     * @throws SessionTimeoutException
     * @throws FormNotFoundException
     */
    protected void setClassloader(final FormServiceProvider formServiceProvider, final Map<String, Object> context) throws SessionTimeoutException,
            FormNotFoundException {
        final ClassLoader classloader = formServiceProvider.getClassloader(context);
        if (classloader != null) {
            Thread.currentThread().setContextClassLoader(classloader);
        }
    }

    /**
     * Initialize the context map
     * 
     * @param urlContext
     *            the map of URL parameters
     * @param locale
     *            the user locale
     * @return the context map
     */
    protected Map<String, Object> initContext(final Map<String, Object> urlContext, final Locale locale) {
        final HttpServletRequest request = getThreadLocalRequest();
        final HttpSession httpSession = request.getSession();
        final APISession aAPISession = (APISession) httpSession.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, locale);
        context.put(FormServiceProviderUtil.API_SESSION, aAPISession);
        return context;
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public ReducedFormPage getFormNextPage(final String formID, final Map<String, Object> urlContext, final String nextPageExpressionId,
            final Map<String, FormFieldValue> fieldValues) throws RPCException, SessionTimeoutException, FormAlreadySubmittedException, SuspendedFormException,
            CanceledFormException, ForbiddenFormAccessException, FormInErrorException, SkippedFormException, AbortedFormException {
        final String localeStr = getLocale();
        final Locale userLocale = resolveLocale(localeStr);
        final Map<String, Object> context = initContext(urlContext, userLocale);
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            final Date deployementDate = formServiceProvider.getDeployementDate(context);
            final Document document = formServiceProvider.getFormDefinitionDocument(context);
            final boolean isEditMode = formServiceProvider.isEditMode(formID, context);
            context.put(FormServiceProviderUtil.IS_EDIT_MODE, isEditMode);
            final boolean isCurrentValue = formServiceProvider.isCurrentValue(context);
            context.put(FormServiceProviderUtil.IS_CURRENT_VALUE, isCurrentValue);
            final IFormDefinitionAPI definitionAPI = FormAPIFactory.getFormDefinitionAPI(tenantID, document, deployementDate, localeStr);
            final String permissions = definitionAPI.getFormPermissions(formID, context);
            final String productVersion = definitionAPI.getProductVersion(context);
            final String migrationProductVersion = definitionAPI.getMigrationProductVersion(formID, context);
            formServiceProvider.isAllowed(formID, permissions, productVersion, migrationProductVersion, context, true);
            setClassloader(formServiceProvider, context);
            final Map<String, Serializable> transientDataContext = getFormTransientDataContext(formServiceProvider, formID, context);
            context.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, transientDataContext);
            context.put(FormServiceProviderUtil.FIELD_VALUES, fieldValues);
            final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
            final Expression nextPageIdExpression = formCacheUtil.getNextPageIdExpression(nextPageExpressionId);
            final String pageId = (String) formServiceProvider.resolveExpression(nextPageIdExpression, context);
            if (pageId != null) {
                final FormPage formPage = definitionAPI.getFormPage(formID, pageId, context);
                formPage.setPageLabel((String) formServiceProvider.resolveExpression(formPage.getPageLabelExpression(), context));
                formFieldValuesUtil.setFormWidgetsValues(tenantID, formPage.getFormWidgets(), context);
                formFieldValuesUtil.storeWidgetsInCacheAndSetCacheID(tenantID, formID, pageId, localeStr, deployementDate, formPage.getFormWidgets());
                return formPage.getReducedFormPage();
            } else {
                throw new IllegalStateException("The next Form page cannot be calculated for " + formID
                        + ". This is more likely to be a design issue of conditional pageflow.");
            }
        } catch (final ForbiddenFormAccessException e) {
            throw e;
        } catch (final CanceledFormException e) {
            throw new CanceledFormException(e);
        } catch (final SuspendedFormException e) {
            throw new SuspendedFormException(e);
        } catch (final FormInErrorException e) {
            throw new FormInErrorException(e);
        } catch (final SkippedFormException e) {
            throw new SkippedFormException(e);
        } catch (final AbortedFormException e) {
            throw new AbortedFormException(e);
        } catch (final FormAlreadySubmittedException e) {
            throw new FormAlreadySubmittedException(e);
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting the process instance next form page " + nextPageExpressionId, e);
            }
            throw new RPCException(e.getMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> skipForm(final String formID, final Map<String, Object> urlContext) throws RPCException, SessionTimeoutException,
            FormAlreadySubmittedException, IllegalActivityTypeException {
        final Map<String, Object> context = initContext(urlContext, resolveLocale(getLocale()));
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            return formServiceProvider.skipForm(formID, context);
        } catch (final FormAlreadySubmittedException e) {
            throw new FormAlreadySubmittedException(e.getMessage(), e);
        } catch (final IllegalActivityTypeException e) {
            throw new IllegalActivityTypeException(e);
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while terminating task", e);
            }
            throw new RPCException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<ReducedFormValidator>> validateFormFields(final String formID, final Map<String, Object> urlContext,
            final Map<String, String> validatorsMap, final Map<String, FormFieldValue> widgetValues, final String submitButtonId) throws RPCException,
            SessionTimeoutException {
        final Map<String, Object> context = initContext(urlContext, resolveLocale(getLocale()));
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            setClassloader(formServiceProvider, context);
            final Map<String, List<ReducedFormValidator>> nonCompliantValidators = new HashMap<String, List<ReducedFormValidator>>();
            final Map<String, Serializable> transientDataContext = getFormTransientDataContext(formServiceProvider, formID, context);
            context.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, transientDataContext);
            for (final Entry<String, String> validatorsEntry : validatorsMap.entrySet()) {
                final String fieldId = getFieldId(validatorsEntry.getKey());
                final String fieldValidatorsId = validatorsEntry.getValue();
                final FormFieldValue fieldValue = getFieldValue(validatorsEntry.getKey(), widgetValues);
                final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
                final List<FormValidator> fieldValidators = formCacheUtil.getFieldValidators(fieldValidatorsId);
                final List<FormValidator> nonCompliantFieldValidators = formServiceProvider.validateField(fieldValidators, fieldId, fieldValue, submitButtonId,
                        context);
                if (!nonCompliantFieldValidators.isEmpty()) {
                    final List<ReducedFormValidator> reducedValidators = new ArrayList<ReducedFormValidator>();
                    for (final FormValidator formValidator : nonCompliantFieldValidators) {
                        reducedValidators.add(formValidator.getReducedFormValidator());
                    }
                    nonCompliantValidators.put(validatorsEntry.getKey(), reducedValidators);
                }
            }
            return nonCompliantValidators;
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while validating Field", e);
            }
            throw new RPCException(e.getMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReducedFormValidator> validateFormPage(final String formID, final Map<String, Object> urlContext, final String pageValidatorsId,
            final Map<String, FormFieldValue> fields, final String submitButtonId) throws RPCException, SessionTimeoutException {
        final Map<String, Object> context = initContext(urlContext, resolveLocale(getLocale()));
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            setClassloader(formServiceProvider, context);
            final Map<String, Serializable> transientDataContext = getFormTransientDataContext(formServiceProvider, formID, context);
            context.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, transientDataContext);
            final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
            final List<FormValidator> pageValidators = formCacheUtil.getPageValidators(pageValidatorsId);
            final List<FormValidator> nonCompliantFieldValidators = formServiceProvider.validatePage(pageValidators, fields, submitButtonId, context);
            final List<ReducedFormValidator> reducedValidators = new ArrayList<ReducedFormValidator>();
            for (final FormValidator formValidator : nonCompliantFieldValidators) {
                reducedValidators.add(formValidator.getReducedFormValidator());
            }
            return reducedValidators;
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while validating Page", e);
            }
            throw new RPCException(e.getMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * Retrieve the true ID of the field with the given client ID
     * 
     * @param key
     *            the ID returned by the client part
     * @return the true ID
     */
    protected String getFieldId(final String key) {
        return key;
    }

    /**
     * Retrieve the value of the field with the given ID
     * 
     * @param fieldId
     *            the field ID
     * @param widgetValues
     *            the values of the fields
     * @return a {@link FormFieldValue}
     */
    protected FormFieldValue getFieldValue(final String fieldId, final Map<String, FormFieldValue> widgetValues) {
        return widgetValues.get(fieldId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReducedHtmlTemplate getFormConfirmationTemplate(final String formID, final Map<String, Object> urlContext) throws RPCException,
            SessionTimeoutException {
        final String localeStr = getLocale();
        final Map<String, Object> context = initContext(urlContext, resolveLocale(localeStr));
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            final Date deployementDate = formServiceProvider.getDeployementDate(context);
            final boolean isEditMode = formServiceProvider.isEditMode(formID, context);
            context.put(FormServiceProviderUtil.IS_EDIT_MODE, isEditMode);
            // Set the current value to false in order to evaluate the confirmation message on archived objects
            context.put(FormServiceProviderUtil.IS_CURRENT_VALUE, Boolean.FALSE);
            context.put(FormServiceProviderUtil.IS_CONFIRMATION_PAGE, Boolean.TRUE);
            final Document document = formServiceProvider.getFormDefinitionDocument(context);
            final IFormDefinitionAPI definitionAPI = FormAPIFactory.getFormDefinitionAPI(tenantID, document, deployementDate, localeStr);
            final HtmlTemplate htmlTemplate = definitionAPI.getFormConfirmationLayout(formID, context);
            setClassloader(formServiceProvider, context);
            final Map<String, Serializable> transientDataContext = getFormTransientDataContext(formServiceProvider, formID, context);
            context.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, transientDataContext);
            htmlTemplate.setDynamicMessage((String) formServiceProvider.resolveExpression(htmlTemplate.getDynamicMessageExpression(), context));
            return htmlTemplate.getReducedHtmlTemplate();
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting the Process Confirmation Template", e);
            }
            throw new RPCException(e.getMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReducedHtmlTemplate getApplicationErrorTemplate(final String formID, final Map<String, Object> urlContext) throws RPCException,
            SessionTimeoutException {
        final String localeStr = getLocale();
        final Map<String, Object> context = initContext(urlContext, resolveLocale(localeStr));
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            final Date deployementDate = formServiceProvider.getDeployementDate(context);
            final Document document = formServiceProvider.getFormDefinitionDocument(context);
            final IFormDefinitionAPI definitionAPI = FormAPIFactory.getFormDefinitionAPI(tenantID, document, deployementDate, localeStr);
            final HtmlTemplate htmlTemplate = definitionAPI.getApplicationErrorLayout(context);
            return htmlTemplate.getReducedHtmlTemplate();
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting the Process Error Template", e);
            }
            throw new RPCException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FormURLComponents getNextFormURL(final String formID, final Map<String, Object> urlContext) throws RPCException, SessionTimeoutException {
        final Map<String, Object> context = initContext(urlContext, resolveLocale(getLocale()));
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            context.put(FormServiceProviderUtil.REQUEST, request);
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            removeFormTransientDataContext(formServiceProvider, formID, context);
            return formServiceProvider.getNextFormURLParameters(formID, context);
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting the next task", e);
            }
            throw new RPCException(e.getMessage(), e);
        }
    }

    /**
     * store the transient data context for the current page flow displayed in the session
     * 
     * @param formServiceProvider
     * @param formID
     *            the form ID
     * @param transientDataContext
     *            the transient data context
     * @param context
     */
    protected void setFormTransientDataContext(final FormServiceProvider formServiceProvider, final String formID,
            final Map<String, Serializable> transientDataContext, final Map<String, Object> context) {

        formServiceProvider.storeFormTransientDataContext(getSession(), TRANSIENT_DATA_SESSION_PARAM_KEY_PREFIX + formID, transientDataContext, context);
    }

    /*
     * Code smell. Shouldn't be protected but avoid npe during tests.
     */
    protected HttpSession getSession() {
        return getThreadLocalRequest().getSession();
    }

    /**
     * Get the transient data context for the current page flow displayed from the session
     * 
     * @param formServiceProvider
     * @param formID
     *            the form ID
     * @param context
     * @return a Map<String, Object> containing the context of transient data
     */
    protected Map<String, Serializable> getFormTransientDataContext(final FormServiceProvider formServiceProvider, final String formID,
            final Map<String, Object> context) {

        final HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        return formServiceProvider.retrieveFormTransientDataContext(session, TRANSIENT_DATA_SESSION_PARAM_KEY_PREFIX + formID, context);
    }

    /**
     * Get the transient data context for the current page flow displayed from the session
     * 
     * @param formServiceProvider
     * @param formID
     *            the form ID
     * @param context
     * @return a Map<String, Object> containing the context of transient data
     */
    protected void removeFormTransientDataContext(final FormServiceProvider formServiceProvider, final String formID, final Map<String, Object> context) {

        final HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        formServiceProvider.removeFormTransientDataContext(session, TRANSIENT_DATA_SESSION_PARAM_KEY_PREFIX + formID, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReducedFormFieldAvailableValue> getFormAsyncAvailableValues(final String formID, final Map<String, Object> urlContext,
            final ReducedFormWidget formWidget, final FormFieldValue currentFieldValue) throws RPCException, SessionTimeoutException {
        final Map<String, Object> context = initContext(urlContext, resolveLocale(getLocale()));
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            setClassloader(formServiceProvider, context);
            final Map<String, Serializable> transientDataContext = getFormTransientDataContext(formServiceProvider, formID, context);
            context.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, transientDataContext);
            // put the current value of the field in the field context
            final Map<String, FormFieldValue> fieldContext = new HashMap<String, FormFieldValue>();
            fieldContext.put(formWidget.getId(), currentFieldValue);
            context.put(FormServiceProviderUtil.FIELD_VALUES, fieldContext);
            Object availableValuesObject = null;
            if (formWidget.getFormWidgetCacheId() != null) {
                // evaluate the available values expression
                final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
                final FormWidget cachedFormWidget = formCacheUtil.getFormWidget(formWidget.getFormWidgetCacheId());
                availableValuesObject = formServiceProvider.resolveExpression(cachedFormWidget.getAvailableValuesExpression(), context);
            }
            return formFieldValuesUtil.getAvailableValues(availableValuesObject, formWidget.getId());
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting activity async available values of the widget " + formWidget.getId(), e);
            }
            throw new RPCException(e.getMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * @param localeStr
     *            the user's locale as a string
     * @return the user's {@link Locale}
     */
    protected Locale resolveLocale(final String localeStr) {
        final String[] localeParams = localeStr.split("_");
        final String language = localeParams[0];
        Locale userLocale = null;
        if (localeParams.length > 1) {
            final String country = localeParams[1];
            userLocale = new Locale(language, country);
        } else {
            userLocale = new Locale(language);
        }
        return userLocale;
    }

    /**
     * @return the user's locale as a String
     */
    protected String getLocale() {
        String localeStr = null;
        final HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(USER_SESSION_PARAM_KEY);
        if (user != null) {
            localeStr = getFormLocale();
        }
        if (localeStr == null && user != null) {
            localeStr = user.getLocale();
        }
        if (localeStr == null) {
            localeStr = request.getLocale().toString();
        }
        return localeStr;
    }

    protected String getFormLocale() {
        String userLocaleStr = null;
        final String theLocaleCookieName = FORM_LOCALE_COOKIE_NAME;
        final Cookie theCookies[] = getThreadLocalRequest().getCookies();
        Cookie theCookie = null;
        if (theCookies != null) {
            for (int i = 0; i < theCookies.length; i++) {
                if (theCookies[i].getName().equals(theLocaleCookieName)) {
                    theCookie = theCookies[i];
                    userLocaleStr = theCookie.getValue().toString();
                    break;
                }
            }
        }
        return userLocaleStr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> executeActions(final String formID, final Map<String, Object> urlContext, final Map<String, FormFieldValue> fieldValues,
            final List<String> pageIds, final String submitButtonId) throws RPCException, SessionTimeoutException, FileTooBigException,
            FormAlreadySubmittedException {
        final String localeStr = getLocale();
        final Map<String, Object> context = initContext(urlContext, resolveLocale(getLocale()));
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            context.put(FormServiceProviderUtil.FIELD_VALUES, fieldValues);
            context.put(FormServiceProviderUtil.SUBMIT_BUTTON_ID, submitButtonId);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            final Date deployementDate = formServiceProvider.getDeployementDate(context);
            final Document document = formServiceProvider.getFormDefinitionDocument(context);
            final IFormDefinitionAPI definitionAPI = FormAPIFactory.getFormDefinitionAPI(tenantID, document, deployementDate, localeStr);
            final List<FormAction> actions = definitionAPI.getFormActions(formID, pageIds, context);
            setClassloader(formServiceProvider, context);
            final Map<String, Serializable> transientDataContext = getFormTransientDataContext(formServiceProvider, formID, context);
            context.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, transientDataContext);
            return formServiceProvider.executeActions(actions, context);
        } catch (final FormAlreadySubmittedException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "The form with ID " + formID + " has already been submitted by someone else.", e);
            }
            throw e;
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final org.bonitasoft.forms.server.exception.FileTooBigException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            throw new FileTooBigException(e.getMessage(), e.getFileName(), e.getMaxSize());
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            throw new RPCException(e.getMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getAnyTodoListForm(final Map<String, Object> urlContext) throws RPCException, SessionTimeoutException {
        final Map<String, Object> context = initContext(urlContext, resolveLocale(getLocale()));
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final long tenantID = retrieveCredentialAndReturnTenantID(request, context);
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
            return formServiceProvider.getAnyTodoListForm(context);
        } catch (final FormNotFoundException e) {
            throw new RPCException(e.getMessage(), e);
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting any todolist form", e);
            }
            throw new RPCException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getLoggedInUser() throws RPCException, SessionTimeoutException {
        try {
            final HttpServletRequest request = getThreadLocalRequest();
            final HttpSession session = request.getSession();
            final User user = (User) session.getAttribute(USER_SESSION_PARAM_KEY);
            if (user == null) {
                final String errorMessage = "There is no user in the HTTP session.";
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, errorMessage);
                }
                throw new NoCredentialsInSessionException(errorMessage);
            }
            return user;
        } catch (final NoCredentialsInSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
            throw new SessionTimeoutException(e.getMessage(), e);
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while getting any todolist form", e);
            }
            throw new RPCException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateTemporaryToken() throws RPCException, SessionTimeoutException {
        final HttpServletRequest request = getThreadLocalRequest();
        final HttpSession httpSession = request.getSession();
        final APISession aAPISession = (APISession) httpSession.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        String userToken = null;
        if (aAPISession != null) {
            userToken = InternalSSOManager.getInstance().add(aAPISession);
        }
        return userToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout() throws RPCException, SessionTimeoutException {
        final HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        session.removeAttribute(USER_SESSION_PARAM_KEY);
        session.removeAttribute(API_SESSION_PARAM_KEY);
    }

    /**
     * Retrieve the API session from the HTTP session and return it
     * 
     * @param request
     *            the HTTP request
     * @return the API session
     * @throws NoCredentialsInSessionException
     */
    protected APISession getAPISession(final HttpServletRequest request) throws NoCredentialsInSessionException {
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(API_SESSION_PARAM_KEY);
        if (apiSession == null) {
            final String errorMessage = "There is no engine API session in the HTTP session.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new NoCredentialsInSessionException(errorMessage);
        }
        return apiSession;
    }

    /**
     * Retrieve the API session and the user from the HTTP session and add them in the context
     * 
     * @param request
     *            the HTTP request
     * @param context
     *            the context
     * @return the tenant ID
     * @throws NoCredentialsInSessionException
     */
    protected long retrieveCredentialAndReturnTenantID(final HttpServletRequest request, final Map<String, Object> context)
            throws NoCredentialsInSessionException {
        final HttpSession session = request.getSession();

        final User user = (User) session.getAttribute(USER_SESSION_PARAM_KEY);
        if (user == null) {
            final String errorMessage = "There is no user in the HTTP session.";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw new NoCredentialsInSessionException(errorMessage);
        }
        // The API session is not mandatory (it is when using the default form service provider)
        final APISession apiSession = (APISession) session.getAttribute(API_SESSION_PARAM_KEY);
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);
        context.put(FormServiceProviderUtil.USER, user);
        return apiSession.getTenantId();
    }

    /**
     * Resolve expression from ApplicationConfig and set result into the reduced application config.
     * 
     * @param formServiceProvider
     *            is used to resolve the expressions
     * @param context
     *            needed by applicationConfig
     * @param applicationConfig
     *            contains expressions to resolve as well as the application config reduced
     * @return applicationConfig
     * @throws FormNotFoundException
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws org.bonitasoft.forms.server.exception.FileTooBigException
     * @throws FormInitializationException
     */
    private ApplicationConfig resolveApplicationConfigExpressions(final FormServiceProvider formServiceProvider, final Map<String, Object> context,
            final ApplicationConfig applicationConfig) throws FormNotFoundException, SessionTimeoutException,
            org.bonitasoft.forms.server.exception.FileTooBigException, IOException, FormInitializationException {
        try {
            context.put(FormServiceProviderUtil.IS_CONFIG_CONTEXT, true);

            String resolvedExpression = (String) formServiceProvider.resolveExpression(applicationConfig.getApplicationLabelExpression(), context);
            applicationConfig.setApplicationLabel(resolvedExpression);

            resolvedExpression = (String) formServiceProvider.resolveExpression(applicationConfig.getMandatoryLabelExpression(), context);
            applicationConfig.setMandatoryLabel(resolvedExpression);

            resolvedExpression = (String) formServiceProvider.resolveExpression(applicationConfig.getMandatorySymbolExpression(), context);
            applicationConfig.setMandatorySymbol(resolvedExpression);

        } catch (final FormNotFoundException e) {
            throw new FormNotFoundException(e);
        } catch (final SessionTimeoutException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.INFO, "Invalid Session");
            }
            final HttpServletRequestAccessor httpServletRequestAccessor = new HttpServletRequestAccessor(getThreadLocalRequest());
            SessionUtil.sessionLogout(httpServletRequestAccessor.getHttpSession());
            throw new SessionTimeoutException();
        }
        return applicationConfig;
    }

}
