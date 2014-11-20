package org.bonitasoft.forms.server.accessor.impl.util;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.forms.client.model.ApplicationConfig;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormPage;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.server.cache.CacheUtil;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;

public class FormCacheUtil {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormCacheUtil.class.getName());

    protected static final String FORM_APPLICATION_PERMISSIONS_CACHE = "formApplicationPermissionsCache";

    protected static final String FORM_MIGRATION_PRODUCT_VERSION_CACHE = "formApplicationMigrationProductVersionCache";

    protected static final String FORM_APPLICATION_VERSION_CACHE = "formApplicationVersionCache";

    protected static final String FORM_APPLICATION_NAME_CACHE = "formApplicationNameCache";

    protected static final String FORM_PERMISSIONS_CACHE = "formPermissionsCache";

    protected static final String FORM_NEXT_FORM_CACHE = "formNextFormCache";

    protected static final String FORM_PAGE_LAYOUT_CACHE = "formPageLayoutCache";

    protected static final String FORM_APPLICATION_LAYOUT_CACHE = "formApplicationLayoutCache";

    protected static final String FORM_FIRST_PAGE_CACHE = "formFirstPageCache";

    protected static final String FORM_PAGES_CACHE = "formPagesCache";

    protected static final String FORM_TRANSIENT_DATA_CACHE = "formTransientDataCache";

    protected static final String FORM_PAGE_ACTIONS_CACHE = "formActionsCache";

    protected static final String FORM_CONFIG_CACHE = "formConfigCache";

    protected static final String NEXT_PAGE_ID_EXPRESSION_CACHE = "nextPageIdExpressionCache";

    protected static final String FIELD_VALIDATORS_CACHE = "fieldValidatorsCache";

    protected static final String PAGE_VALIDATORS_CACHE = "pageValidatorsCache";

    protected static final String FORM_WIDGET_CACHE = "formWidgetCache";

    protected static String CACHE_DISK_STORE_PATH = null;

    protected static String DOMAIN_KEY_CONNECTOR = "@";

    protected long tenantID;

    protected FormCacheUtil(final long tenantID) {
        try {
            CACHE_DISK_STORE_PATH = WebBonitaConstantsUtils.getInstance(tenantID).getFormsWorkFolder().getAbsolutePath();
            this.tenantID = tenantID;
        } catch (final Exception e) {
            LOGGER.log(Level.WARNING, "Unable to retrieve the path of the cache disk store directory path.", e);
        }
    }


    protected static String getDateStr(final Date date) {
        if (date != null) {
            return Long.toString(date.getTime());
        }
        return "";
    }

    public Expression getFirstPage(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (Expression) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_FIRST_PAGE_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeFirstPage(final String formID, final String locale, final Date applicationDeployementDate, final Expression firstPage) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_FIRST_PAGE_CACHE, formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID, firstPage);
    }

    public FormPage getPage(final String formID, final String locale, final Date applicationDeployementDate, final String pageId) throws InvalidFormDefinitionException {
        return (FormPage) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_PAGES_CACHE, formID + locale + getDateStr(applicationDeployementDate) + pageId
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storePage(final String formID, final String locale, final Date applicationDeployementDate, final FormPage formPage) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_PAGES_CACHE, formID + locale + getDateStr(applicationDeployementDate) + formPage.getPageId()
                + DOMAIN_KEY_CONNECTOR + tenantID, formPage);
    }

    @SuppressWarnings("unchecked")
    public List<TransientData> getTransientData(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (List<TransientData>) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_TRANSIENT_DATA_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR
                + tenantID);
    }

    public void storeTransientData(final String formID, final String locale, final Date applicationDeployementDate, final List<TransientData> transientData) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_TRANSIENT_DATA_CACHE, formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID, transientData);
    }

    @SuppressWarnings("unchecked")
    public List<FormAction> getPageActions(final String formID, final String locale, final Date applicationDeployementDate, final String activityName, final String pageId) throws InvalidFormDefinitionException {
        return (List<FormAction>) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_PAGE_ACTIONS_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + activityName + pageId
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storePageActions(final String formID, final String locale, final Date applicationDeployementDate, final String activityName, final String pageId, final List<FormAction> actions) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_PAGE_ACTIONS_CACHE, formID + locale + getDateStr(applicationDeployementDate) + activityName + pageId
                + DOMAIN_KEY_CONNECTOR + tenantID, actions);
    }

	public ApplicationConfig getApplicationConfig(final String formID, final String locale, final Date applicationDeployementDate, final boolean includeApplicationTemplate) throws InvalidFormDefinitionException {
        return (ApplicationConfig) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_CONFIG_CACHE,
                formID + locale + getDateStr(applicationDeployementDate) + Boolean.toString(includeApplicationTemplate) + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public final void storeApplicationConfig(final String formID, final String locale, final Date applicationDeployementDate, final boolean includeApplicationTemplate, final ApplicationConfig ApplicationConfig) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_CONFIG_CACHE,
                formID + locale + getDateStr(applicationDeployementDate) + Boolean.toString(includeApplicationTemplate) + DOMAIN_KEY_CONNECTOR + tenantID,
                ApplicationConfig);
    }

    public String getApplicationPermissions(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_APPLICATION_PERMISSIONS_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR
                + tenantID);
    }

    public void storeApplicationPermissions(final String formID, final String locale, final Date applicationDeployementDate, final String applicationPermissions) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_APPLICATION_PERMISSIONS_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID, applicationPermissions);
    }

    public String getMigrationProductVersion(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_MIGRATION_PRODUCT_VERSION_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR
                + tenantID);
    }

    public void storeMigrationProductVersion(final String formID, final String locale, final Date applicationDeployementDate, final String migrationProductVersion) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_MIGRATION_PRODUCT_VERSION_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID, migrationProductVersion);
    }

    public String getFormPermissions(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_PERMISSIONS_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeFormPermissions(final String formID, final String locale, final Date applicationDeployementDate, final String formPermissions) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_PERMISSIONS_CACHE, formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID, formPermissions);
    }

    public String getNextForm(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_NEXT_FORM_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeNextForm(final String formID, final String locale, final Date applicationDeployementDate, final String nextForm) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_NEXT_FORM_CACHE,
                formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID, nextForm);
    }

    public String getFormPageLayout(final String formID, final String locale, final Date applicationDeployementDate,final String pageId) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_PAGE_LAYOUT_CACHE, formID + locale + getDateStr(applicationDeployementDate) + pageId
                + DOMAIN_KEY_CONNECTOR
                + tenantID);
    }

    public void storeFormPageLayout(final String formID, final String locale, final Date applicationDeployementDate,final String pageId, final String formPageLayout) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_PAGE_LAYOUT_CACHE, formID + locale + getDateStr(applicationDeployementDate) + pageId + DOMAIN_KEY_CONNECTOR
                + tenantID, formPageLayout);
    }

    public String getApplicationVersion(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_APPLICATION_VERSION_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR
                + tenantID);
    }

    public void storeApplicationVersion(final String formID, final String locale, final Date applicationDeployementDate, final String applicationVersion) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_APPLICATION_VERSION_CACHE, formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID, applicationVersion);
    }

    public String getApplicationName(final String formID, final String locale, final Date applicationDeployementDate) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_APPLICATION_NAME_CACHE, formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeApplicationName(final String formID, final String locale, final Date applicationDeployementDate, final String applicationName) throws InvalidFormDefinitionException {
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_APPLICATION_NAME_CACHE, formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID, applicationName);
    }

    public FormWidget getFormWidget(final String formWidgetCacheId){
        return (FormWidget) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_WIDGET_CACHE, formWidgetCacheId);
    }

    public FormWidget getFormWidget(final String formID, final String pageID, final String widgetID, final String locale, final Date processDeployementDate) {
        return getFormWidget(formID + pageID + widgetID + locale + getDateStr(processDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public String storeFormWidget(final String formID, final String pageID, final String locale, final Date processDeployementDate, final FormWidget formWidget) {
        final String formWidgetCacheId = formID + pageID + formWidget.getId() + locale + getDateStr(processDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_WIDGET_CACHE, formWidgetCacheId, formWidget);
        return formWidgetCacheId;
    }

    public Expression getNextPageIdExpression(final String nextPageExpressionId) {
        return (Expression) CacheUtil.get(CACHE_DISK_STORE_PATH, NEXT_PAGE_ID_EXPRESSION_CACHE, nextPageExpressionId);
    }

    public String storeNextPageIdExpression(final String formID, final String pageID, final String locale, final Date processDeployementDate, final Expression nextPageIdExpression) {
        final String nextPageExpressionId = formID + pageID + locale + getDateStr(processDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(CACHE_DISK_STORE_PATH, NEXT_PAGE_ID_EXPRESSION_CACHE, nextPageExpressionId, nextPageIdExpression);
        return nextPageExpressionId;
    }

    @SuppressWarnings("unchecked")
    public List<FormValidator> getFieldValidators(final String fieldValidatorsId) {
        return (List<FormValidator>) CacheUtil.get(CACHE_DISK_STORE_PATH, FIELD_VALIDATORS_CACHE, fieldValidatorsId);
    }

    public String storeFieldValidators(final String formID, final String pageID, final String widgetID, final String locale, final Date processDeployementDate, final List<FormValidator> validators) {
        final String validatorsId = formID + pageID + widgetID + locale + getDateStr(processDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(CACHE_DISK_STORE_PATH, FIELD_VALIDATORS_CACHE, validatorsId, validators);
        return validatorsId;
    }

    @SuppressWarnings("unchecked")
    public List<FormValidator> getPageValidators(final String pageValidatorsId) {
        return (List<FormValidator>) CacheUtil.get(CACHE_DISK_STORE_PATH, PAGE_VALIDATORS_CACHE, pageValidatorsId);
    }

    public String storePageValidators(final String formID, final String pageID, final String locale, final Date processDeployementDate, final List<FormValidator> validators) {
        final String validatorsId = formID + pageID + locale + getDateStr(processDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(CACHE_DISK_STORE_PATH, PAGE_VALIDATORS_CACHE, validatorsId, validators);
        return validatorsId;
    }

    public String getPageLayoutContent(final String bodyContentId) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_PAGE_LAYOUT_CACHE, bodyContentId);
    }

    public String storePageLayoutContent(final String formID, final String PageID, final String locale, final Date applicationDeployementDate, final String BodyContent) throws InvalidFormDefinitionException {
        final String bodyContentId = formID + PageID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_PAGE_LAYOUT_CACHE, bodyContentId, BodyContent);
        return bodyContentId;
    }

    public String getApplicationLayoutContent(final String bodyContentId) throws InvalidFormDefinitionException {
        return (String) CacheUtil.get(CACHE_DISK_STORE_PATH, FORM_APPLICATION_LAYOUT_CACHE, bodyContentId);
    }

    public String storeApplicationLayoutContent(final String formID, final String locale, final Date applicationDeployementDate, final String BodyContent) throws InvalidFormDefinitionException {
        final String bodyContentId = formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(CACHE_DISK_STORE_PATH, FORM_APPLICATION_LAYOUT_CACHE, bodyContentId, BodyContent);
        return bodyContentId;
    }

    public void clearAll() {
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_FIRST_PAGE_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_PAGES_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_TRANSIENT_DATA_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_PAGE_ACTIONS_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_CONFIG_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_APPLICATION_PERMISSIONS_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_MIGRATION_PRODUCT_VERSION_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_PERMISSIONS_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_NEXT_FORM_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_PAGE_LAYOUT_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_APPLICATION_VERSION_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_APPLICATION_NAME_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, NEXT_PAGE_ID_EXPRESSION_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FIELD_VALIDATORS_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, PAGE_VALIDATORS_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_WIDGET_CACHE);
        CacheUtil.clear(CACHE_DISK_STORE_PATH, FORM_APPLICATION_LAYOUT_CACHE);
    }

}
