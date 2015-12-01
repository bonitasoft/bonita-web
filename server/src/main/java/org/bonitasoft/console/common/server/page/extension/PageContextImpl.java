/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.page.extension;

import java.util.Locale;

import org.bonitasoft.console.common.server.page.PageContext;
import org.bonitasoft.engine.session.APISession;

/**
 * This class provide access to the data relative to the context in which the custom page is displayed
 *
 * @author Anthony Birembaut
 *
 */
public class PageContextImpl implements org.bonitasoft.web.extension.page.PageContext,org.bonitasoft.console.common.server.page.PageContext{

    protected APISession apiSession;

    protected Locale locale;

    protected String profileID;

    public PageContextImpl(final APISession apiSession, final Locale locale, final String profileID) {
        super();
        this.apiSession = apiSession;
        this.locale = locale;
        this.profileID = profileID;
    }

    /**
     * @return the engine {@link APISession}
     */
    public APISession getApiSession() {
        return apiSession;
    }

    /**
     * @return the user locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return the ID of the profile in which the page is currently displayed
     */
    public String getProfileID() {
        return profileID;
    }

}
