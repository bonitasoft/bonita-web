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
package org.bonitasoft.forms.server.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;

import org.bonitasoft.forms.client.model.ActivityAttribute;
import org.bonitasoft.forms.client.model.ApplicationConfig;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormPage;
import org.bonitasoft.forms.client.model.FormType;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.HeadNode;
import org.bonitasoft.forms.client.model.HtmlTemplate;
import org.bonitasoft.forms.client.model.ReducedFormValidator.ValidatorPosition;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.accessor.IApplicationConfigDefAccessor;
import org.bonitasoft.forms.server.accessor.IApplicationFormDefAccessor;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtilFactory;
import org.bonitasoft.forms.server.api.IFormDefinitionAPI;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.FormInitializationException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.exception.FormServiceProviderNotFoundException;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.bonitasoft.forms.server.exception.InvalidFormTemplateException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderFactory;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.w3c.dom.Document;

/**
 * Implementation of {@link IFormDefinitionAPI}
 *
 * @author Anthony Birembaut, Haojie Yuan
 */
public class FormDefinitionAPIImpl implements IFormDefinitionAPI {

    /**
     * onload attribute to evaluate only with ie7 and ie8
     */
    private static final String IE_ONLOAD = "ieonload";

    /**
     * HTML script element name
     */
    private static final String SCRIPT_ELEMENT_NAME = "script";

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormDefinitionAPIImpl.class.getName());

    /**
     * default dateformat pattern
     */
    protected String defaultDateFormatPattern;

    /**
     * The document containing the form definition for the application
     */
    protected Document formDefinitionDocument;

    /**
     * the {@link Date} of the application deployment
     */
    protected Date applicationDeploymentDate;

    /**
     * the user's locale as a String
     */
    protected String locale;

    /**
     * The tenant ID
     */
    protected long tenantID;

    /**
     * Constructor
     *
     * @param document
     * @param applicationDeployementDate
     *            the deployement date of the application
     * @param locale
     *            the user's locale as a String
     * @return the FormDefinitionAPIImpl instance
     * @throws InvalidFormDefinitionException
     */
    public FormDefinitionAPIImpl(final long tenantID, final Document document, final Date applicationDeploymentDate, final String locale)
            throws InvalidFormDefinitionException {
        this.tenantID = tenantID;
        defaultDateFormatPattern = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getDefaultDateFormat();
        formDefinitionDocument = document;
        this.applicationDeploymentDate = applicationDeploymentDate;
        this.locale = locale;
    }

    /**
     * get the application config definition accessor by FormServiceProvider
     *
     * @return an instance of {@link IApplicationConfigDefAccessor}
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     * @throws ApplicationFormDefinitionNotFoundException
     */
    protected IApplicationConfigDefAccessor getApplicationConfigDefinition(final Map<String, Object> context) throws FormServiceProviderNotFoundException,
            SessionTimeoutException, ApplicationFormDefinitionNotFoundException {
        FormServiceProvider formServiceProvider = null;
        formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
        return formServiceProvider.getApplicationConfigDefinition(formDefinitionDocument, context);
    }

    /**
     * get XMLApplicationFormDefAccessorImpl by FormServiceProvider
     *
     * @param formId
     *            the form ID
     * @throws ApplicationFormDefinitionNotFoundException
     * @throws InvalidFormDefinitionException
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     */
    protected IApplicationFormDefAccessor getApplicationFormDefinition(final String formId, final Map<String, Object> context)
            throws ApplicationFormDefinitionNotFoundException, InvalidFormDefinitionException, FormServiceProviderNotFoundException, SessionTimeoutException {
        context.put(FormServiceProviderUtil.APPLICATION_DEPLOYMENT_DATE, applicationDeploymentDate);
        FormServiceProvider formServiceProvider = null;
        formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
        final IApplicationFormDefAccessor applicationDefAccessor = formServiceProvider.getApplicationFormDefinition(formId, formDefinitionDocument, context);
        return applicationDefAccessor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationDelpoymentDate(final Date applicationDefinitionDate) {
        applicationDeploymentDate = applicationDefinitionDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductVersion(final Map<String, Object> context) throws FormServiceProviderNotFoundException, SessionTimeoutException,
            ApplicationFormDefinitionNotFoundException {
        return getApplicationConfigDefinition(context).getProductVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMigrationProductVersion(final String formID, final Map<String, Object> context) throws InvalidFormDefinitionException,
            FormServiceProviderNotFoundException, SessionTimeoutException, ApplicationFormDefinitionNotFoundException {
        String migrationProductVersion = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getMigrationProductVersion(formID, locale,
                applicationDeploymentDate);
        if (migrationProductVersion == null) {
            migrationProductVersion = getApplicationConfigDefinition(context).getMigrationProductVersion();
            if (formID != null) {
                FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeMigrationProductVersion(formID, locale, applicationDeploymentDate,
                        migrationProductVersion);
            }
        }
        return migrationProductVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationPermissions(final String formID, final Map<String, Object> context) throws InvalidFormDefinitionException,
            FormServiceProviderNotFoundException, SessionTimeoutException, ApplicationFormDefinitionNotFoundException {
        String applicationPermissions = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getApplicationPermissions(formID, locale,
                applicationDeploymentDate);
        if (applicationPermissions == null) {
            applicationPermissions = getApplicationConfigDefinition(context).getApplicationPermissions();
            if (formID != null) {
                FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeApplicationPermissions(formID, locale, applicationDeploymentDate,
                        applicationPermissions);
            }
        }
        return applicationPermissions;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormPermissions(final String formID, final Map<String, Object> context) throws ApplicationFormDefinitionNotFoundException,
            InvalidFormDefinitionException, FormServiceProviderNotFoundException, SessionTimeoutException {
        String formPermissions = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getFormPermissions(formID, locale, applicationDeploymentDate);
        if (formPermissions == null) {
            formPermissions = getApplicationFormDefinition(formID, context).getFormPermissions();
            if (formID != null) {
                FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeFormPermissions(formID, locale, applicationDeploymentDate, formPermissions);
            }
        }
        return formPermissions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNextForm(final String formID, final Map<String, Object> context) throws ApplicationFormDefinitionNotFoundException,
            InvalidFormDefinitionException, FormServiceProviderNotFoundException, SessionTimeoutException {
        String nextForm = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getNextForm(formID, locale, applicationDeploymentDate);
        if (nextForm == null) {
            nextForm = getApplicationFormDefinition(formID, context).getNextForm();
            if (formID != null) {
                FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeNextForm(formID, locale, applicationDeploymentDate, nextForm);
            }
        }
        return nextForm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormPageLayout(final String formID, final String pageID, final Map<String, Object> context)
            throws ApplicationFormDefinitionNotFoundException, InvalidFormDefinitionException, FormServiceProviderNotFoundException, SessionTimeoutException {
        String formPageLayout = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getFormPageLayout(formID, locale, applicationDeploymentDate, pageID);
        if (formPageLayout == null) {
            formPageLayout = getApplicationFormDefinition(formID, context).getFormPageLayout(pageID);
            if (formID != null) {
                FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeFormPageLayout(formID, locale, applicationDeploymentDate, pageID, formPageLayout);
            }
        }
        return formPageLayout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression getFormFirstPage(final String formID, final Map<String, Object> context) throws InvalidFormDefinitionException, FormNotFoundException,
            ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException, SessionTimeoutException {
        final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
        Expression firstPage = formCacheUtil.getFirstPage(formID, locale, applicationDeploymentDate);
        if (firstPage == null) {
            final IApplicationFormDefAccessor getApplicationDefinition = getApplicationFormDefinition(formID, context);
            firstPage = getApplicationDefinition.getFirstPageExpression();
            if (formID != null) {
                formCacheUtil.storeFirstPage(formID, locale, applicationDeploymentDate, firstPage);
            }
        }
        return firstPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FormPage getFormPage(final String formID, final String pageId, final Map<String, Object> context) throws InvalidFormDefinitionException,
            FormNotFoundException, FileNotFoundException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException,
            SessionTimeoutException {
        final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
        FormPage formPage = formCacheUtil.getPage(formID, locale, applicationDeploymentDate, pageId);
        if (formPage == null) {
            final IApplicationFormDefAccessor applicationFormDefinition = getApplicationFormDefinition(formID, context);
            try {
                final FormType formType = applicationFormDefinition.getFormType();
                final List<FormWidget> pageWidgets = applicationFormDefinition.getPageWidgets(pageId);
                final List<FormValidator> pageValidators = applicationFormDefinition.getPageValidators(pageId);
                final HtmlTemplate pageLayout = getFormPageLayout(applicationFormDefinition, formType, pageId, context, pageWidgets, pageValidators);
                final Expression pageLabelExpression = applicationFormDefinition.getPageLabelExpression(pageId);
                final boolean allowHTMLInLabel = applicationFormDefinition.isHTMLAllowedInLabel(pageId);
                formPage = new FormPage(pageId, pageLabelExpression, pageLayout, pageWidgets, pageValidators, formType, allowHTMLInLabel);
                final Expression applicationNextPageExpression = applicationFormDefinition.getNextPageExpression(pageId);
                formPage.setNextPageExpression(applicationNextPageExpression);
                if (formID != null) {
                    formPage.setNextPageExpressionId(formCacheUtil.storeNextPageIdExpression(formID, pageId, locale, applicationDeploymentDate,
                            applicationNextPageExpression));
                    formPage.setPageValidatorsId(formCacheUtil.storePageValidators(formID, pageId, locale, applicationDeploymentDate, pageValidators));
                    formCacheUtil.storePage(formID, locale, applicationDeploymentDate, formPage);
                }
            } catch (final InvalidFormDefinitionException e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Failed to parse the forms definition file.");
                }
                formPage = null;
            }
        }
        if (formPage != null) {
            // store bodycontent in the cach using formId PageId
            String bodyContentId = null;
            if (formID != null) {
                bodyContentId = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storePageLayoutContent(formID, pageId, locale, applicationDeploymentDate,
                        formPage.getPageLayout().getBodyContent());
            }
            formPage.getPageLayout().setBodyContentId(bodyContentId);
            formPage.getPageLayout().setBodyContent(null);
        }
        return formPage;
    }

    /**
     * Retrieve the template associated with a instantiation form page
     *
     * @param applicationDefAccessor
     *            the application form definition accessor
     * @param formType
     *            the form type
     * @param pageId
     *            the page ID
     * @param context
     *            the Map of context
     * @param pageWidgets
     * @param pageValidators
     * @return an {@link HtmlTemplate} object containing the elements required to
     *         build the level2 (page template)
     * @throws InvalidFormDefinitionException
     * @throws FileNotFoundException
     * @throws ApplicationFormDefinitionNotFoundException
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     */
    protected HtmlTemplate getFormPageLayout(final IApplicationFormDefAccessor applicationDefAccessor, final FormType formType, final String pageId,
            final Map<String, Object> context, final List<FormWidget> pageWidgets, final List<FormValidator> pageValidators)
            throws InvalidFormDefinitionException, FileNotFoundException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException,
            SessionTimeoutException {

        final String templatePath = applicationDefAccessor.getFormPageLayout(pageId);
        final String defaultTemplatePath = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getGlobalPageTemplate();
        HtmlTemplate htmlTemplate = null;
        try {
            htmlTemplate = getPageLayout(templatePath, applicationDeploymentDate, context);
        } catch (final FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Page layout not found. Using the default page template.");
            }
            boolean isActivity = false;
            @SuppressWarnings("unchecked")
            final Map<String, Object> urlContext = (Map<String, Object>) context.get(FormServiceProviderUtil.URL_CONTEXT);
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                isActivity = true;
            }
            htmlTemplate = buildPageLayout(context, defaultTemplatePath, pageWidgets, pageValidators, formType, isActivity);
        }
        return htmlTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationConfig getApplicationConfig(final Map<String, Object> context, final String formID, final boolean includeApplicationTemplate)
            throws InvalidFormDefinitionException, FormServiceProviderNotFoundException, SessionTimeoutException, ApplicationFormDefinitionNotFoundException {
        ApplicationConfig applicationConfig = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getApplicationConfig(formID, locale,
                applicationDeploymentDate, includeApplicationTemplate);
        if (applicationConfig == null) {
            final IApplicationConfigDefAccessor applicationConfigFormDefinition = getApplicationConfigDefinition(context);
            HtmlTemplate applicationLayout = null;
            Expression applicationLabelExpression = null;
            if (includeApplicationTemplate) {
                try {
                    applicationLayout = getApplicationLayout(context);
                    // store bodycontent in the cach using formId PageId
                    String bodyContentId = null;
                    if (formID != null) {
                        bodyContentId = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeApplicationLayoutContent(formID, locale,
                                applicationDeploymentDate, applicationLayout.getBodyContent());
                    }
                    applicationLayout.setBodyContentId(bodyContentId);
                    applicationLayout.setBodyContent(null);
                } catch (final FileNotFoundException e) {
                    e.printStackTrace();
                } catch (final InvalidFormTemplateException e) {
                    e.printStackTrace();
                } catch (final ApplicationFormDefinitionNotFoundException e) {
                    e.printStackTrace();
                }
                if (formID != null) {
                    applicationLabelExpression = applicationConfigFormDefinition.getApplicationLabelExpression();
                }
            }
            final Expression mandatorySymbolExpression = applicationConfigFormDefinition.getApplicationMandatorySymbolExpression();
            final Expression mandatoryLabelExpression = applicationConfigFormDefinition.getApplicationMandatoryLabelExpression();
            final String mandatorySymbolClasses = applicationConfigFormDefinition.getApplicationMandatorySymbolStyle();
            final String userXPURL = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getPortalURL();
            applicationConfig = new ApplicationConfig(applicationLabelExpression, mandatorySymbolExpression, mandatoryLabelExpression, mandatorySymbolClasses,
                    userXPURL);
            applicationConfig.setApplicationLayout(applicationLayout);
            if (formID != null) {
                FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeApplicationConfig(formID, locale, applicationDeploymentDate,
                        includeApplicationTemplate, applicationConfig);
            }
        }
        return applicationConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransientData> getFormTransientData(final String formID, final Map<String, Object> context) throws InvalidFormDefinitionException,
            FormNotFoundException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException, SessionTimeoutException {
        List<TransientData> transientData = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getTransientData(formID, locale, applicationDeploymentDate);
        if (transientData == null) {
            transientData = getApplicationFormDefinition(formID, context).getTransientData();
            if (formID != null) {
                FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storeTransientData(formID, locale, applicationDeploymentDate, transientData);
            }
        }
        return transientData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormAction> getFormActions(final String formID, final List<String> pageIds, final Map<String, Object> context)
            throws InvalidFormDefinitionException, FormNotFoundException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException,
            SessionTimeoutException {
        final List<FormAction> formActions = new ArrayList<FormAction>();
        final IApplicationFormDefAccessor applicationFormDefinition = getApplicationFormDefinition(formID, context);
        for (final String pageId : pageIds) {
            List<FormAction> pageActions = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).getPageActions(formID, locale, applicationDeploymentDate,
                    formID, pageId);
            if (pageActions == null) {
                pageActions = applicationFormDefinition.getActions(pageId);
                if (formID != null) {
                    FormCacheUtilFactory.getTenantFormCacheUtil(tenantID).storePageActions(formID, locale, applicationDeploymentDate, formID, pageId,
                            pageActions);
                }
            }
            formActions.addAll(pageActions);
        }
        return formActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlTemplate getFormConfirmationLayout(final String formID, final Map<String, Object> context) throws InvalidFormDefinitionException,
            FormNotFoundException, FileNotFoundException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException,
            SessionTimeoutException {
        final IApplicationFormDefAccessor applicationFormDefinition = getApplicationFormDefinition(formID, context);
        final String applicationConfirmationTemplatePath = applicationFormDefinition.getConfirmationLayout();
        final Expression applicationConfirmationMessage = applicationFormDefinition.getConfirmationMessageExpression();
        final HtmlTemplate applicationConfirmationTemplate = getPageLayout(applicationConfirmationTemplatePath, applicationDeploymentDate, context);
        applicationConfirmationTemplate.setDynamicMessageExpression(applicationConfirmationMessage);
        return applicationConfirmationTemplate;
    }

    /**
     * Retrieve the application template data
     *
     * @return a {@link HtmlTemplate} object containing the elements required to
     *         build the level1 (application template)
     * @throws InvalidFormDefinitionException
     * @throws InvalidFormTemplateException
     * @throws FileNotFoundException
     * @throws ApplicationFormDefinitionNotFoundException
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     */
    protected HtmlTemplate getApplicationLayout(final Map<String, Object> context) throws FileNotFoundException, InvalidFormTemplateException,
            InvalidFormDefinitionException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException, SessionTimeoutException {

        final IApplicationConfigDefAccessor applicationConfigFormDefinition = getApplicationConfigDefinition(context);
        final String applicationTemplateLocation = applicationConfigFormDefinition.getApplicationLayout();
        return getLayout(applicationTemplateLocation, "application", applicationDeploymentDate, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String applicationAttributes(final String bodyContent, final Map<String, String> stepAttributes, final Locale locale) {
        ResourceBundle labels = ResourceBundle.getBundle("locale.i18n.ActivityAttributeLabels", locale);
        if (locale.getLanguage() != null && labels.getLocale() != null && !locale.getLanguage().equals(labels.getLocale().getLanguage())) {
            labels = ResourceBundle.getBundle("locale.i18n.ActivityAttributeLabels", Locale.ENGLISH);
        }

        final StringBuilder regex = new StringBuilder();
        regex.append("\\$(");
        final HashMap<String, String> encodedStrings = new HashMap<String, String>();
        for (String stepAttribute : ActivityAttribute.attributeValues()) {
            stepAttribute = encodeString(stepAttribute, encodedStrings);
            if (regex.length() > 3) {
                regex.append("|");
            }
            regex.append(stepAttribute);
            regex.append("|");
            regex.append("label.");
            regex.append(stepAttribute);
        }
        regex.append(")");
        final Pattern pattern = Pattern.compile(regex.toString());
        final Matcher matcher = pattern.matcher(bodyContent);
        final StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            final String itemFound = matcher.group(1);
            if (itemFound.startsWith("label.")) {
                String label = null;
                try {
                    label = labels.getString(itemFound);
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "No step attribute label " + itemFound + " was found.");
                    }
                    label = "";
                }
                matcher.appendReplacement(stringBuffer, label);
            } else {
                final String stepAttribute = ActivityAttribute.valueOfAttibute(itemFound).name();
                if (stepAttribute != null) {
                    final String attributeValue = encodeString(stepAttributes.get(stepAttribute), encodedStrings);
                    if (attributeValue != null) {
                        matcher.appendReplacement(stringBuffer, attributeValue);
                    } else {
                        matcher.appendReplacement(stringBuffer, "");
                    }
                }
            }
        }
        matcher.appendTail(stringBuffer);

        return decodeString(stringBuffer.toString(), encodedStrings);
    }

    @Override
    public Map<String, Serializable> getTransientDataContext(final List<TransientData> transientData, final Locale userLocale, final Map<String, Object> context)
            throws FormNotFoundException, FormServiceProviderNotFoundException, ClassNotFoundException, SessionTimeoutException, FileTooBigException,
            IOException, FormInitializationException {
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
        final Map<String, Serializable> transientDataContext = new HashMap<String, Serializable>();
        final List<Expression> expressionsToEvaluate = new ArrayList<Expression>();
        for (final TransientData data : transientData) {
            final Expression expression = data.getExpression();
            if (expression != null) {
                expressionsToEvaluate.add(expression);
            }
        }
        final Map<String, Serializable> resolvedExpressions = formServiceProvider.resolveExpressions(expressionsToEvaluate, context);
        for (final TransientData data : transientData) {
            final String name = data.getName();
            final String className = data.getClassname();
            final Expression expression = data.getExpression();
            Serializable evaluatedValue = null;
            if (expression != null) {
                try {
                    evaluatedValue = resolvedExpressions.get(data.getExpression().getName());
                    if (evaluatedValue != null) {
                        try {
                            // Load both classes in the current classloader because evaluatedValue comes from another classloader (server side)
                            final Class<?> dataClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                            final Class<?> localEvaluatedValueClass = Thread.currentThread().getContextClassLoader().loadClass(evaluatedValue.getClass().getName());
                            localEvaluatedValueClass.asSubclass(dataClass);
                        } catch (final ClassCastException e) {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (final IllegalArgumentException e) {
                    final String errorMessage = "The value of transient data " + name + " doesn't match with the declared classname " + className;
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, errorMessage);
                    }
                    throw new IllegalArgumentException(errorMessage);
                }
            }
            transientDataContext.put(name, evaluatedValue);
        }
        return transientDataContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlTemplate getApplicationErrorLayout(final Map<String, Object> context) throws InvalidFormDefinitionException, FileNotFoundException,
            ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException, SessionTimeoutException {
        final IApplicationConfigDefAccessor applicationConfigFormDefinition = getApplicationConfigDefinition(context);
        final String applicationErrorTemplatePath = applicationConfigFormDefinition.getApplicationErrorTemplate();
        final HtmlTemplate applicationErrorTemplate = getPageLayout(applicationErrorTemplatePath, applicationDeploymentDate, context);
        return applicationErrorTemplate;
    }

    /**
     * get a page layout
     *
     * @param layoutPath
     *            The path of the layout
     * @param applicationDeploymentDate
     *            The date of the application deployed
     * @param context
     *            Map containing the URL parameters
     * @return HtmlTemplate
     * @throws FileNotFoundException
     * @throws InvalidFormDefinitionException
     * @throws ApplicationFormDefinitionNotFoundException
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     */
    protected HtmlTemplate getPageLayout(final String layoutPath, final Date applicationDeploymentDate, final Map<String, Object> context)
            throws FileNotFoundException, InvalidFormDefinitionException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException,
            SessionTimeoutException {
        try {
            if (layoutPath == null) {
                throw new IOException();
            }
            InputStream htmlFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(layoutPath);
            if (htmlFileStream == null) {
                final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
                final File dir = formServiceProvider.getApplicationResourceDir(applicationDeploymentDate, context);
                final File htmlFile = new File(dir, layoutPath);
                if (htmlFile.exists()) {
                    htmlFileStream = new FileInputStream(htmlFile);
                } else {
                    throw new IOException();
                }
            }

            final Source source = new Source(htmlFileStream);

            final Map<String, String> bodyAttributes = new HashMap<String, String>();

            final Element headElement = source.getFirstElement("head");
            final List<HeadNode> headNodes = new ArrayList<HeadNode>();
            if (headElement != null) {
                for (final Element headChildElement : headElement.getChildElements()) {
                    final Map<String, String> nodeAttributes = new HashMap<String, String>();
                    final Attributes attributes = headChildElement.getAttributes();
                    if (attributes != null) {
                        for (final Attribute attribute : attributes) {
                            nodeAttributes.put(attribute.getKey().toLowerCase(), attribute.getValue());
                        }
                    }
                    // fix for IE7 and IE8 failing to evaluate scripts in inserted HTML pages headers
                    if (SCRIPT_ELEMENT_NAME.equalsIgnoreCase(headChildElement.getName())) {
                        bodyAttributes.put(IE_ONLOAD, headChildElement.getContent().toString());
                    }
                    if (StartTagType.COMMENT.equals(headChildElement.getStartTag().getStartTagType())) {
                        headNodes.add(new HeadNode(null, nodeAttributes, headChildElement.getStartTag().toString()));
                    } else {
                        headNodes.add(new HeadNode(headChildElement.getName(), nodeAttributes, headChildElement.getContent().toString()));
                    }
                }
            }

            String body = null;
            final Element bodyElement = source.getFirstElement("body");
            if (bodyElement != null) {
                final Attributes attributes = bodyElement.getAttributes();
                if (attributes != null) {
                    for (final Attribute attribute : attributes) {
                        bodyAttributes.put(attribute.getKey(), attribute.getValue());
                    }
                }
                body = bodyElement.getContent().toString();
            } else {
                body = source.toString();
            }

            return new HtmlTemplate(body, bodyAttributes, headNodes);

        } catch (final IOException e) {
            String message = null;
            if (layoutPath == null) {
                message = "No page template defined.";
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, message);
                }
            } else {
                message = "Page template file " + layoutPath + " could not be found.";
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message);
                }
            }
            throw new FileNotFoundException(message);
        }
    }

    /**
     * get a layout
     *
     * @param layoutLocation
     *            The location of the layout
     * @param layoutTypeName
     *            The type of the layout
     * @param applicationDeploymentDate
     *            The date of the application deployed
     * @param context
     *            Map containing the URL parameters
     * @return HtmlTemplate
     * @throws FileNotFoundException
     * @throws InvalidFormTemplateException
     * @throws InvalidFormDefinitionException
     * @throws ApplicationFormDefinitionNotFoundException
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     */
    protected HtmlTemplate getLayout(final String layoutpath, final String layoutTypeName, final Date applicationDeploymentDate,
            final Map<String, Object> context) throws FileNotFoundException, InvalidFormTemplateException, InvalidFormDefinitionException,
            ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException, SessionTimeoutException {

        InputStream htmlFileStream = null;
        try {
            if (layoutpath == null) {
                throw new IOException();
            }
            htmlFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(layoutpath);
            if (htmlFileStream == null) {
                final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
                final File dir = formServiceProvider.getApplicationResourceDir(applicationDeploymentDate, context);
                final File htmlFile = new File(dir, layoutpath);
                if (htmlFile.exists()) {
                    htmlFileStream = new FileInputStream(htmlFile);
                } else {
                    throw new IOException();
                }
            }

            final Source source = new Source(htmlFileStream);

            final Map<String, String> bodyAttributes = new HashMap<String, String>();

            final List<HeadNode> headNodes = new ArrayList<HeadNode>();
            final Element headElement = source.getFirstElement("head");
            if (headElement != null) {
                for (final Element headChildElement : headElement.getChildElements()) {
                    final Map<String, String> nodeAttributes = new HashMap<String, String>();
                    final Attributes attributes = headChildElement.getAttributes();
                    if (attributes != null) {
                        for (final Attribute attribute : attributes) {
                            nodeAttributes.put(attribute.getKey(), attribute.getValue());
                        }
                    }
                    // fix for IE7 and IE8 failing to evaluate scripts in inserted HTML pages headers
                    if (SCRIPT_ELEMENT_NAME.equalsIgnoreCase(headChildElement.getName())) {
                        bodyAttributes.put(IE_ONLOAD, headChildElement.getContent().toString());
                    }
                    if (StartTagType.COMMENT.equals(headChildElement.getStartTag().getStartTagType())) {
                        headNodes.add(new HeadNode(null, nodeAttributes, headChildElement.getStartTag().toString()));
                    } else {
                        headNodes.add(new HeadNode(headChildElement.getName(), nodeAttributes, headChildElement.getContent().toString()));
                    }
                }
            } else {
                final String errorMessage = "Non standard HTML for the " + layoutTypeName + " template : missing body element.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage);
                }
                throw new InvalidFormTemplateException(errorMessage);
            }

            String body = null;
            final Element bodyElement = source.getFirstElement("body");
            if (bodyElement != null) {
                final Attributes attributes = bodyElement.getAttributes();
                if (attributes != null) {
                    for (final Attribute attribute : attributes) {
                        bodyAttributes.put(attribute.getKey(), attribute.getValue());
                    }
                }
                body = bodyElement.getContent().toString();
            } else {
                final String errorMessage = "Non standard HTML for the " + layoutTypeName + " template : missing head element.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage);
                }
                throw new InvalidFormTemplateException(errorMessage);
            }
            return new HtmlTemplate(body, bodyAttributes, headNodes);
        } catch (final IOException e) {
            final String message = layoutTypeName + "  template file " + layoutpath + " could not be found";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FileNotFoundException(message);
        } finally {
            if (htmlFileStream != null) {
                try {
                    htmlFileStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "unable to close input stream for template " + layoutpath, e);
                    }
                }
            }
        }
    }

    /**
     * Generates the HTML template when no page template is defined
     *
     * @param defaultTemplatePath
     * @param pageWidgets
     * @param pageValidators
     * @param isActivity
     * @return HtmlTemplate
     * @throws FileNotFoundException
     * @throws InvalidFormDefinitionException
     * @throws ApplicationFormDefinitionNotFoundException
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     */
    protected HtmlTemplate buildPageLayout(final Map<String, Object> context, final String defaultTemplatePath, final List<FormWidget> pageWidgets,
            final List<FormValidator> pageValidators, final FormType formType, final boolean isActivity) throws FileNotFoundException,
            InvalidFormDefinitionException, ApplicationFormDefinitionNotFoundException, FormServiceProviderNotFoundException, SessionTimeoutException {

        HtmlTemplate pageTemplate = null;

        final HtmlTemplate pageBaseTemplate = getPageLayout(defaultTemplatePath, applicationDeploymentDate, context);
        final String baseBody = pageBaseTemplate.getBodyContent();
        final Source source = new Source(baseBody);

        final Element formContainerElement = source.getFirstElementByClass("bonita_form_container");
        final int insertionIndex = formContainerElement.getStartTag().getEnd();

        final OutputDocument outputDocument = new OutputDocument(source);
        final StringBuilder formContent = new StringBuilder();
        formContent.append("\n<div class=\"bonita_form_page_validation_container\">");
        List<FormValidator> bottomPageValidators = null;
        for (final FormValidator pageValidator : pageValidators) {
            if (!ValidatorPosition.BOTTOM.equals(pageValidator.getPosition())) {
                formContent.append("<div id=\"" + pageValidator.getId() + "\"></div>\n");
            } else {
                bottomPageValidators = new ArrayList<FormValidator>();
                bottomPageValidators.add(pageValidator);
            }
        }
        formContent.append("\n</div>\n");
        boolean isButtonContainer = false;
        for (final FormWidget pageWidget : pageWidgets) {
            List<FormValidator> bottomFieldValidators = null;
            if (FormType.entry == formType && pageWidget.getValidators() != null && !pageWidget.getValidators().isEmpty()) {
                for (final FormValidator fieldValidator : pageWidget.getValidators()) {
                    if (!ValidatorPosition.BOTTOM.equals(fieldValidator.getPosition())) {
                        formContent.append("<div id=\"" + fieldValidator.getId() + "\"></div>\n");
                    } else {
                        bottomFieldValidators = new ArrayList<FormValidator>();
                        bottomFieldValidators.add(fieldValidator);
                    }
                }
            }
            if (pageWidget.getType().name().startsWith("BUTTON_")) {
                if (!isButtonContainer) {
                    formContent.append("<div class=\"bonita_form_button_container\">\n");
                }
                isButtonContainer = true;
            } else {
                if (isButtonContainer) {
                    formContent.append("</div>\n");
                }
                isButtonContainer = false;
            }
            formContent.append("<div id=\"" + pageWidget.getId() + "\"></div>\n");

            if (FormType.entry != formType && pageWidget.getType().equals(WidgetType.BUTTON_SUBMIT)) {
                formContent.append("<hr class=\"bonita_clear_float\">");
            }
            if (FormType.entry == formType && bottomFieldValidators != null) {
                for (final FormValidator fieldValidator : bottomFieldValidators) {
                    formContent.append("<div id=\"" + fieldValidator.getId() + "\"></div>\n");
                }
            }
        }
        if (bottomPageValidators != null) {
            formContent.append("\n<div class=\"bonita_form_page_validation_container\">");
            for (final FormValidator pageValidator : bottomPageValidators) {
                formContent.append("<div id=\"" + pageValidator.getId() + "\"></div>\n");
            }
            formContent.append("\n</div>\n");
        }

        outputDocument.insert(insertionIndex, formContent.toString());

        final StringBuilder headerContent = new StringBuilder();
        headerContent.append("\n<div id=\"bonita_form_page_label\" class=\"bonita_form_page_label\"></div>");
        if (isActivity && FormType.entry == formType) {
            headerContent.append("\n<div id=\"bonita_form_step_header\">");
            headerContent.append("\n\t<div id=\"bonita-object-area-top\"></div>");
            headerContent.append("\n\t<div id=\"bonita-object-area-middle\">");
            headerContent.append("\n\t\t<div id=\"bonita-object-area-from\">$label.bonita_step_state $bonita_step_state</div>");
            headerContent.append("\n\t\t<div id=\"bonita-object-area-to\">$label.bonita_step_reachedStateDate $bonita_step_reachedStateDate</div>");
            headerContent
                    .append("\n\t\t<div id=\"bonita-object-area-priority\">$label.bonita_step_priority <span class=\"bonita-priority\">$bonita_step_priority</span></div>");
            headerContent.append("\n\t\t<div id=\"bonita-object-area-description\">$bonita_step_description</div>");
            headerContent.append("\n\t</div>");
            headerContent.append("\n\t<div id=\"bonita-object-area-bottom\"></div>");
            headerContent.append("\n</div>");
        }
        outputDocument.insert(0, headerContent.toString());

        pageTemplate = new HtmlTemplate(outputDocument.toString(), pageBaseTemplate.getBodyAttributes(), pageBaseTemplate.getHeadNodes());

        return pageTemplate;
    }

    private String encodeString(final String source, final HashMap<String, String> encodedStrings) {
        String to = null;
        try {
            if (source != null) {
                if (encodedStrings.containsKey(source)) {
                    to = encodedStrings.get(source);
                } else {
                    to = java.net.URLEncoder.encode(source, "UTF-8").replaceAll("\\+", " ");
                    encodedStrings.put(source, to);
                }
            }
        } catch (final UnsupportedEncodingException e) {
            to = source;
        }
        return to;
    }

    private String decodeString(String source, final HashMap<String, String> encodedStrings) {
        final Iterator<Entry<String, String>> it = encodedStrings.entrySet().iterator();
        while (it.hasNext()) {
            final Entry<String, String> entry = it.next();
            source = source.replaceAll(entry.getValue(), Matcher.quoteReplacement(entry.getKey()));
        }
        return source;
    }
}
