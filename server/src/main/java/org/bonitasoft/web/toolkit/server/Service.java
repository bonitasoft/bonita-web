/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.server;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;

/**
 * This class represent a service.<br />
 * It must define a public final static String TOKEN.
 * 
 * @author Séverin Moussel
 */
public abstract class Service {

    /**
     * The ServletCall responsible of this service.
     */
    private ServiceServletCall caller = null;

    /**
     * Set the caller.
     * 
     * @param caller
     *            The ServletCall responsible of this service.
     */
    public void setCaller(final ServiceServletCall caller) {
        this.caller = caller;
    }

    /**
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getHttpSession()
     */
    protected final HttpSession getHttpSession() {
        return this.caller.getHttpSession();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ENTRY POINT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Entry point of the service.<br />
     * The returned object will be send as response.
     * <ul>
     * <li>If the object is a String, Integer, Long, ..., the response content will be the value as is.</li>
     * <li>If the object implements JsonSerializable, the content will be returned as a json String.</li>
     * <li>If the object is a list or a map, the response will be a json String.</li>
     * <li>Otherwise, the response content will be the object.toString().</li>
     * </ul>
     * 
     * @return This method returns the output to respond.
     */
    public abstract Object run();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DELEGATIONS FOR PARAMETERS ACCESS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getRequestURL()
     */
    public final String getRequestURL() {
        return this.caller.getRequestURL();
    }

    /**
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getInputStream()
     */
    public final String getInputStream() {
        return this.caller.getInputStream();
    }

    /**
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#countParameters()
     */
    public final int countParameters() {
        return this.caller.countParameters();
    }

    /**
     * @param name
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getParameterAsList(java.lang.String)
     */
    public final List<String> getParameterAsList(final String name) {
        return this.caller.getParameterAsList(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getParameterAsList(java.lang.String, java.lang.String)
     */
    public final List<String> getParameterAsList(final String name, final String defaultValue) {
        return this.caller.getParameterAsList(name, defaultValue);
    }

    /**
     * @param name
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getParameter(java.lang.String)
     */
    public final String getParameter(final String name) {
        return this.caller.getParameter(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getParameter(java.lang.String, java.lang.String)
     */
    public final String getParameter(final String name, final String defaultValue) {
        return this.caller.getParameter(name, defaultValue);
    }

    /**
     * @return
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getParameters()
     */
    public final Map<String, String[]> getParameters() {
        return this.caller.getParameters();
    }

    public final LOCALE getLocale() {
        try {
            return LOCALE.valueOf(caller.getLocale());
        } catch (IllegalArgumentException e) {
            return AbstractI18n.getDefaultLocale();
        }
    }

}
