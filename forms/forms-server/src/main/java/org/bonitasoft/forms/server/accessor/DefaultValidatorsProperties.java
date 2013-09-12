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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.accessor;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for default validators access (read in a properties file)
 * @author Anthony Birembaut
 *
 */
public class DefaultValidatorsProperties {
    
    /**
     * Default name of the form definition file
     */
    private static final String FORM_VALIDATORS_CONFIG_FILE_NAME = "forms-validators.properties";
    
    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(DefaultValidatorsProperties.class.getName());
    
    /**
     * Instance attribute
     */
    private static DefaultValidatorsProperties INSTANCE;

    /**
     * properties
     */
    private Properties defaultProperties;
    
    /**
     * @return the {@link DefaultValidatorsProperties} instance
     */
    public static synchronized DefaultValidatorsProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultValidatorsProperties();
        }
        return INSTANCE;
    }

    /**
     * Private contructor to prevent instantiation
     */
    private DefaultValidatorsProperties(){
        loadProperties();
    }
    
    /**
     * Load the properties
     */
    void loadProperties() {
        defaultProperties = new Properties();
        // Read properties file.
        try {
            defaultProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(FORM_VALIDATORS_CONFIG_FILE_NAME));
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "default forms config file " + FORM_VALIDATORS_CONFIG_FILE_NAME + " is missing form the classpath");
        }
    }

    /**
     * @param the classname of the required type
     * @return the default validator for the given type
     */
    public String getDefaultValidator(final String className) {
        return defaultProperties.getProperty(className);
    }
}


