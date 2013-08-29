/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General public static License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General public static License for more details.
 * 
 * You should have received a copy of the GNU General public static License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.json.JSonUnserializerClient;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;

import com.google.gwt.user.client.Cookies;

/**
 * @author Séverin Moussel
 */
public class Cookie extends ParametersStorage {

    private static final String COOKIE_NAME = "bos_cookie";

    private static final String LOGIN_COOKIE_NAME = "BOS_Locale";

    private static final long COOKIE_EXPIRE = 6048000000L; // 70 days

    private static Cookie SINGLETON = new Cookie();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COOKIE I/O
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Remove the cookie from the user browser
     */
    @Override
    protected final void resetParameters() {
        Cookies.removeCookie(COOKIE_NAME);
        Cookies.removeCookie(LOGIN_COOKIE_NAME);
    }

    /**
     * Read the cookie from the user browser and parse it
     * 
     * @return This method returns the content of the cookie as an indexed tree
     */
    @Override
    protected final TreeIndexed<String> readParameters() {
        return this.readParameters(true);
    }

    /**
     * Read the cookie from the user browser and parse it
     * 
     * @return This method returns the content of the cookie as an indexed tree
     */
    @Override
    protected final TreeIndexed<String> readParameters(final boolean updateExpiresDate) {
        // Read the parameters as JSON
        final String cookieContent = Cookies.getCookie(COOKIE_NAME);
        TreeIndexed<String> result = null;
        if (cookieContent == null) {
            result = new TreeIndexed<String>();
        } else {
            result = (TreeIndexed<String>) JSonUnserializerClient.unserializeTree(cookieContent);
        }

        // Update the expires date
        if (updateExpiresDate) {
            writeParameters(result);
        }

        return result;
    }

    /**
     * Save the cookie to the
     * 
     * @param parameters
     */
    @Override
    protected final void writeParameters(final TreeIndexed<String> parameters) {
        // Set the expires date
        final Date now = new Date();
        now.setTime(now.getTime() + COOKIE_EXPIRE);

        // Save the parameters as json
        Cookies.setCookie(COOKIE_NAME, parameters.toJson(), now);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATICS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Replace all parameters by the passed ones. Using this method also remove the parameters that are not redefined.
     * 
     * @param params
     *            The new parameters to set.
     */
    public static void setParameters(final HashMap<String, String> params) {
        SINGLETON._setParameters(params);
    }

    /**
     * Replace all parameters by the passed ones. Using this method also remove the parameters that are not redefined.
     * 
     * @param params
     *            The new parameters to set.
     */
    public static void setParameters(final TreeIndexed<String> params) {
        SINGLETON._setParameters(params);
    }

    /**
     * Add a parameter. If this parameter already exists, the value will be changed
     * 
     * @param name
     * @param value
     */
    public static void addParameter(final String name, final String value) {
        SINGLETON._addParameter(name, value);
        // Cookie for the login JSP
        if (UrlOption.LANG.equals(name)) {
            // Set the expires date
            final Date now = new Date();
            now.setTime(now.getTime() + COOKIE_EXPIRE);
            Cookies.setCookie(LOGIN_COOKIE_NAME, value, now, null, "/", false);
        }
    }

    /**
     * Add a parameter. If this parameter already exists, the value will be changed
     * 
     * @param name
     * @param value
     */
    public static void addParameter(final String name, final Map<String, String> value) {
        SINGLETON._addParameter(name, value);
    }

    /**
     * Add a parameter. If this parameter already exists, the value will be changed
     * 
     * @param name
     * @param value
     */
    public static void addParameter(final String name, final List<String> value) {
        SINGLETON._addParameter(name, value);
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @return This method returns the value of a parameter or NULL if the parameter doesn't exist or is an array.
     */
    public static String getParameter(final String name) {
        return SINGLETON._getParameter(name);
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @param defaultValue
     * @return This method returns the value of a parameter or {defaultValue} if the parameter doesn't exist or is an array.
     */
    public static String getParameter(final String name, final String defaultValue) {
        return SINGLETON._getParameter(name, defaultValue);
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @return This method returns the value of a parameter or NULL if the parameter doesn't exist.
     */
    public static List<String> getArrayParameter(final String name) {
        return SINGLETON._getArrayParameter(name);
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @param defaultValue
     * @return This method returns the value of a parameter or {defaultValue} if the parameter doesn't exist.
     */
    public static List<String> getArrayParameter(final String name, final List<String> defaultValue) {
        return SINGLETON._getArrayParameter(name, defaultValue);
    }

    public static TreeIndexed<String> getParameters() {
        return SINGLETON._getParameters();
    }

}
