/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.auth.impl.standard;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.credentials.Credentials;
import org.bonitasoft.console.common.server.utils.LocaleUtils;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;

/**
 * @author Chong Zhao
 */
public class StandardAuthenticationManagerImpl implements AuthenticationManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(StandardAuthenticationManagerImpl.class.getName());

    @Override
    public String getLoginPageURL(final HttpServletRequestAccessor request, final String redirectURL) throws ServletException {
        final StringBuilder url = new StringBuilder();
        String context = request.asHttpServletRequest().getContextPath();
        url.append(context).append(AuthenticationManager.LOGIN_PAGE).append("?");
        //adds the locale to the login URL if it is set in the requested URL
        String localeFromRequestedURL = LocaleUtils.getLocaleFromRequestURL(request.asHttpServletRequest());
        if (localeFromRequestedURL != null) {
            url.append(LocaleUtils.PORTAL_LOCALE_PARAM).append("=").append(localeFromRequestedURL).append("&");
        }
        url.append(AuthenticationManager.REDIRECT_URL).append("=").append(redirectURL);
        return url.toString();
    }

    /**
     * protected for test purpose
     */
    protected long getDefaultTenantId() {
        return TenantsManagementUtils.getDefaultTenantId();
    }

    @Override
    public Map<String, Serializable> authenticate(final HttpServletRequestAccessor requestAccessor, final Credentials credentials)
            throws AuthenticationFailedException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "#authenticate (this implementation of " + AuthenticationManager.class.getName()
                    + " does nothing. The subsequent engine login is enough to authenticate the user.)");
        }
        return Collections.emptyMap();
    }

    @Override
    public String getLogoutPageURL(final HttpServletRequestAccessor request, final String redirectURL) throws ServletException {
        return null;
    }
}
