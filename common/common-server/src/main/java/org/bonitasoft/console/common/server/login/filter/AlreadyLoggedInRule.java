/*
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

package org.bonitasoft.console.common.server.login.filter;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;
import org.bonitasoft.web.toolkit.server.utils.LocaleUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AlreadyLoggedInRule implements AuthorizationRule {

    @Override
    public boolean doAuthorize(HttpServletRequestAccessor request, TenantIdAccessor tenantIdAccessor) {
        if (isUserAlreadyLoggedIn(request, tenantIdAccessor)) {
            ensureUserSession(request.asHttpServletRequest(),
                    request.getHttpSession(),
                    request.getApiSession());
            return true;
        }
        return false;
    }

    /**
     * Overridden is Subscription
     */
    protected boolean isUserAlreadyLoggedIn(HttpServletRequestAccessor request, TenantIdAccessor tenantIdAccessor) {
        return request.getApiSession() != null;
    }

    private void ensureUserSession(final HttpServletRequest request, final HttpSession session, final APISession apiSession) {
        if (session.getAttribute(LoginManager.USER_SESSION_PARAM_KEY) == null) {
            reCreateUser(request, session, apiSession);
        }
    }

    private void reCreateUser(final HttpServletRequest request, final HttpSession session, final APISession apiSession) {
        final String locale = getLocale(request);
        User user = new User(apiSession.getUserName(), locale);
        user.setUseCredentialTransmission(useCredentialsTransmission(apiSession));
        session.setAttribute(LoginManager.USER_SESSION_PARAM_KEY, user);
    }

    private String getLocale(final HttpServletRequest request) {
        return LocaleUtils.getUserLocale(request);
    }

    // protected for test stubbing
    protected boolean useCredentialsTransmission(final APISession apiSession) {
        return PropertiesFactory.getSecurityProperties(apiSession.getTenantId())
                .useCredentialsTransmission();
    }
}