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
package org.bonitasoft.forms.server.provider.impl.util;

/**
 * Utility class dealing with the metadatas for the URLs of the dedicated application
 * 
 * @author Nicolas Chabanoles, Anthony Birembaut
 */
public class ApplicationURLUtils {

    /**
     * Dedicated application URL metadata name
     */
    public static final String DEDICATED_APP_URL_META_NAME = "dedicated_application_URL";

    /**
     * Homepage servlet ID in path
     */
    public static final String HOMEPAGE_SERVLET_ID_IN_PATH = "homepage";

    /**
     * Theme parameter
     */
    public static final String THEME_PARAM = "theme";

    public static final String UI_MODE_PARAM = "ui";

    /**
     * Instance attribute
     */
    private static ApplicationURLUtils INSTANCE = null;

    /**
     * @return the FormExpressionsAPI instance
     */
    public static synchronized ApplicationURLUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationURLUtils();
        }
        return INSTANCE;
    }

    /**
     * Private contructor to prevent instantiation
     */
    private ApplicationURLUtils() {
    }

    /**
     * Get application url depending on the theme to doAuthorize
     * 
     * @param processDefinitionId
     * @return
     */
    public String getDedicatedApplicationUrl(final long processDefinitionId, final String uiMode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HOMEPAGE_SERVLET_ID_IN_PATH);
        stringBuilder.append("?");
        addUrlParameter(UI_MODE_PARAM, uiMode, stringBuilder);
        stringBuilder.append("&");
        addUrlParameter(THEME_PARAM, String.valueOf(processDefinitionId), stringBuilder);
        return stringBuilder.toString();
    }

    private void addUrlParameter(String key, String value, StringBuilder stringBuilder) {
        stringBuilder.append(key);
        stringBuilder.append("=");
        stringBuilder.append(value);
    }

}
