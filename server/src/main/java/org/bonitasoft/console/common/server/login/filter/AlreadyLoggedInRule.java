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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.servlet.PlatformLoginServlet;
import org.bonitasoft.console.common.server.utils.LocaleUtils;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;

public class AlreadyLoggedInRule extends AuthenticationRule {

    @Override
    public boolean doAuthorize(final HttpServletRequestAccessor request, HttpServletResponse response) throws ServletException {
        if (isUserAlreadyLoggedIn(request)) {
            ensureUserSession(request.asHttpServletRequest(),
                    request.getHttpSession(),
                    request.getApiSession());
            return true;
        }
        return false;
    }

    protected boolean isUserAlreadyLoggedIn(final HttpServletRequestAccessor request) throws ServletException {
        HttpServletRequest httpServletRequest = request.asHttpServletRequest();
        if (httpServletRequest.getPathInfo() != null) {
            String requestPath = httpServletRequest.getServletPath() + httpServletRequest.getPathInfo();
            if (requestPath.matches(RestAPIAuthorizationFilter.PLATFORM_API_URI_REGEXP)) {
                return request.getHttpSession().getAttribute(PlatformLoginServlet.PLATFORM_SESSION_PARAM_KEY) != null;
            }
        }
        return request.getApiSession() != null;
    }

    private void ensureUserSession(final HttpServletRequest request, final HttpSession session, final APISession apiSession) {
        if (apiSession != null && session.getAttribute(SessionUtil.USER_SESSION_PARAM_KEY) == null) {
            reCreateUser(request, session, apiSession);
        }
    }

    private void reCreateUser(final HttpServletRequest request, final HttpSession session, final APISession apiSession) {
        final String locale = getLocale(request);
        final User user = new User(apiSession.getUserName(), locale);
        session.setAttribute(SessionUtil.USER_SESSION_PARAM_KEY, user);
    }

    private String getLocale(final HttpServletRequest request) {
        return LocaleUtils.getUserLocaleAsString(request);
    }
}
