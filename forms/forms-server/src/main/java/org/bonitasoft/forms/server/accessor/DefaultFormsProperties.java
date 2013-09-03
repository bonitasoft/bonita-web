/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server.accessor;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.forms.server.provider.impl.FormServiceProviderImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for default properties access (read in a properties file)
 * 
 * @author Anthony Birembaut
 */
public class DefaultFormsProperties {

    /**
     * Default name of the form definition file
     */
    protected static final String FORM_DEFAULT_CONFIG_FILE_NAME = "forms-config.properties";

    /**
     * Default mandatory field symbol (replaced client side with i18n)
     */
    protected static final String DEFAULT_MANDATORY_FIELD_SYMBOL = "#defaultMandatoryFieldSymbol";

    /**
     * Default mandatory field label (replaced client side with i18n)
     */
    protected static final String DEFAULT_MANDATORY_FIELD_LABEL = "#defaultMandatoryFieldLabel";

    /**
     * Default date format
     */
    protected static final String DEFAULT_DATE_FORMAT = "MMMM dd, yyyy";

    /**
     * Default maximum number of widgets per page
     */
    protected static final int DEFAULT_MAX_WIDGET_PER_PAGE = 5;

    /**
     * Default maximum number of entries in the process map
     */
    protected static final int DEFAULT_CACHE_MAX_PROCESS_ENTRIES = 15;

    /**
     * Default maximum number of entries per language Map
     */
    protected static final int DEFAULT_CACHE_MAX_LANGUAGE_ENTRIES = 5;

    /**
     * Default form definition time to live in cache
     */
    protected final static long DEFAULT_CACHE_PROCESS_EXPIRATION_TIME = 300000;

    /**
     * Default form attachment max size
     */
    protected final static long DEFAULT_ATTACHMENT_MAX_SIZE = 15;

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(DefaultFormsProperties.class.getName());

    /**
     * properties
     */
    protected final Properties defaultProperties;

    /**
     * Private contructor to prevent instantiation
     * 
     * @param tenantId
     *            the tenant Id
     */
    protected DefaultFormsProperties(final long tenantId) {
        defaultProperties = new Properties();
        // Read properties file.
        InputStream inputStream = null;
        try {
            final File propertiesFile = new File(WebBonitaConstantsUtils.getInstance(tenantId).getConfFolder(), FORM_DEFAULT_CONFIG_FILE_NAME);
            inputStream = new FileInputStream(propertiesFile);
            defaultProperties.load(inputStream);
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.WARNING, "default forms config file " + FORM_DEFAULT_CONFIG_FILE_NAME + " is missing form the forms conf directory");
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "default forms config file " + FORM_DEFAULT_CONFIG_FILE_NAME + " stream could not be closed.", e);
                    }
                }
            }
        }
    }

    public String getApplicationLayout() {
        return defaultProperties.getProperty("forms.default.application.layout");
    }

    public String getApplicationMandatorySymbol() {
        return DEFAULT_MANDATORY_FIELD_SYMBOL;
    }

    public String getApplicationMandatoryLabel() {
        return DEFAULT_MANDATORY_FIELD_LABEL;
    }

    public String getPageErrorTemplate() {
        return defaultProperties.getProperty("forms.default.page.template.error");
    }

    public String getPageConfirmationTemplate() {
        return defaultProperties.getProperty("forms.default.page.template.confirm");
    }

    public String getGlobalPageTemplate() {
        return defaultProperties.getProperty("forms.default.page.template");
    }

    public String getDefaultDateFormat() {
        String dateFormat = defaultProperties.getProperty("forms.default.date.format");
        if (dateFormat == null) {
            LOGGER.log(Level.INFO, "the default date format is undefined. Using the default value : " + DEFAULT_DATE_FORMAT);
            dateFormat = DEFAULT_DATE_FORMAT;
        }
        return dateFormat;
    }

    public int getMaxWigdetPerPage() {
        final String maxWidgetPerPage = defaultProperties.getProperty("forms.default.page.maxwidget");
        try {
            return Integer.parseInt(maxWidgetPerPage);
        } catch (final NumberFormatException nfe) {
            LOGGER.log(Level.INFO, "the max number of widgets per page is undefined or incorrectly defined. Using the default value : "
                    + DEFAULT_MAX_WIDGET_PER_PAGE);
            return DEFAULT_MAX_WIDGET_PER_PAGE;
        }
    }

    public int getMaxProcessesInCache() {
        final String maxProcessesInCache = defaultProperties.getProperty("forms.cache.processes.size");
        try {
            return Integer.parseInt(maxProcessesInCache);
        } catch (final NumberFormatException nfe) {
            LOGGER.log(Level.INFO, "the max number of process form definifion in cache is undefined or incorrectly defined. Using the default value : "
                    + DEFAULT_CACHE_MAX_PROCESS_ENTRIES);
            return DEFAULT_CACHE_MAX_PROCESS_ENTRIES;
        }
    }

    public int getMaxLanguagesInCache() {
        final String maxLanguagesInCache = defaultProperties.getProperty("forms.cache.languages.size");
        try {
            return Integer.parseInt(maxLanguagesInCache);
        } catch (final NumberFormatException nfe) {
            LOGGER.log(Level.INFO,
                    "the max number of languages of process form definifion in cache is undefined or incorrectly defined. Using the default value : "
                            + DEFAULT_CACHE_MAX_LANGUAGE_ENTRIES);
            return DEFAULT_CACHE_MAX_LANGUAGE_ENTRIES;
        }
    }

    public long getProcessesTimeToLiveInCache() {
        final String processesTTLInCache = defaultProperties.getProperty("forms.cache.process.ttl");
        try {
            return Long.parseLong(processesTTLInCache);
        } catch (final NumberFormatException nfe) {
            LOGGER.log(Level.INFO, "the processes form definifion time to live in cache is undefined or incorrectly defined. Using the default value : "
                    + DEFAULT_CACHE_PROCESS_EXPIRATION_TIME);
            return DEFAULT_CACHE_PROCESS_EXPIRATION_TIME;
        }
    }

    public long getAttachmentMaxSize() {
        final String attachmentMaxSize = defaultProperties.getProperty("form.attachment.max.size");
        try {
            return Long.parseLong(attachmentMaxSize);
        } catch (final NumberFormatException nfe) {
            LOGGER.log(Level.INFO, "the attachment max size is undefined or incorrectly defined. Using the default value : " + DEFAULT_ATTACHMENT_MAX_SIZE);
            return DEFAULT_ATTACHMENT_MAX_SIZE;
        }
    }

    public String getPortalURL() {
        return defaultProperties.getProperty("forms.user-xp.url");
    }

    /**
     * @return get form service provider implementation
     */
    public String getFormServiceProviderImpl() {
        final String formServiceProviderImpl = defaultProperties.getProperty("form.service.provider");
        if (formServiceProviderImpl == null) {
            final String defaultImpl = FormServiceProviderImpl.class.getName();
            LOGGER.log(Level.INFO, "the form service provider Implementation is undefined or incorrectly defined. Using the default implementation : "
                    + defaultImpl);
            return defaultImpl;
        }
        return formServiceProviderImpl;
    }

    public boolean autoGenerateForms() {
        final String autoGenerateForms = defaultProperties.getProperty("form.generation.auto");
        if (!Boolean.FALSE.toString().equals(autoGenerateForms) && !Boolean.TRUE.toString().equals(autoGenerateForms)) {
            LOGGER.log(Level.INFO,
                    "the property for automatic form generation using the engine when the form is not defined is undefined or incorrectly defined. using the value : false");
            return false;
        }
        return Boolean.valueOf(autoGenerateForms);
    }
}
