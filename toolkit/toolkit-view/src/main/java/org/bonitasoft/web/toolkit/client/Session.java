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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.session.SessionItem;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public class Session extends ParametersStorage {

    private final TreeIndexed<String> parameters = new TreeIndexed<String>();

    private static Cookie SINGLETON = new Cookie();

    public static String getCurrentProfile() {
        return URLUtils.getInstance().getHashParameter(UrlOption.PROFILE);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // I/O
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Remove the cookie from the user browser
     */
    @Override
    protected final void resetParameters() {
        this.parameters.clear();
    }

    /**
     * Read the cookie from the user browser and parse it
     * 
     * @return This method returns the content of the cookie as an indexed tree
     */
    @Override
    protected final TreeIndexed<String> readParameters(final boolean updateExpiresDate) {
        return this.parameters;
    }

    /**
     * Save the cookie to the
     * 
     * @param parameters
     */
    @Override
    protected final void writeParameters(final TreeIndexed<String> parameters) {
        // Not needed
        // TODO send the values to the session API to synchronize client and server session
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

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EASY GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static APIID getUserId() {
        return APIID.makeAPIID(getParameter(SessionItem.ATTRIBUTE_USERID));
    }

}
