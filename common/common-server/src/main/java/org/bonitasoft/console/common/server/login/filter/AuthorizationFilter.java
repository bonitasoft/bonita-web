/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.filter;

import org.bonitasoft.console.common.client.user.User;
import org.bonitasoft.console.common.server.login.*;
import org.bonitasoft.console.common.server.login.datastore.AutoLoginCredentials;
import org.bonitasoft.console.common.server.login.localization.LoginUrl;
import org.bonitasoft.console.common.server.login.localization.LoginUrlException;
import org.bonitasoft.console.common.server.login.localization.RedirectUrl;
import org.bonitasoft.console.common.server.login.localization.RedirectUrlBuilder;
import org.bonitasoft.console.common.server.preferences.properties.ProcessIdentifier;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.session.APISession;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Vincent Elcrin
 */
public class AuthorizationFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor((HttpServletRequest) request);
        final HttpServletResponseAccessor responseAccessor = new HttpServletResponseAccessor((HttpServletResponse) response);

        final HttpSession session = requestAccessor.getHttpSession();
        final APISession apiSession = requestAccessor.getApiSession();
        final long requestedTenantId = getTenantId(requestAccessor);
        final long tenantId = ensureTenantId(requestedTenantId);

        if (isUserLoggedIn(apiSession, requestedTenantId)) {
            ensureUserSession(request, session, apiSession);
            chain.doFilter(request, response);

        } else if (isAutoLogin(requestAccessor, tenantId)
                && doAutoLogin(requestAccessor, tenantId)) {
            chain.doFilter(request, response);
        } else {
            cleanHttpSession(session);
            responseAccessor.redirect(createLoginUrl(
                    makeRedirectUrl(requestAccessor, requestAccessor.getRedirectUrl()).getUrl(),
                    requestedTenantId));
        }
    }

    /**
     * Ensure tenant id by fetching default one if no requested.
     * @param requestedTenantId
     * @return
     */
    private long ensureTenantId(long requestedTenantId) {
        if(requestedTenantId < 0) {
            return TenantsManagementUtils.getDefaultTenantId();
        }
        return requestedTenantId;
    }

    @Override
    public void destroy() {
    }

    protected long getTenantId(final HttpServletRequestAccessor request) throws ServletException {
        return TenantsManagementUtils.getDefaultTenantId();
    }

    protected boolean isUserLoggedIn(final APISession apiSession, final long requestedTenantId) {
        return apiSession != null;
    }

    private SecurityProperties getSecurityProperties(final HttpServletRequestAccessor httpRequest, final long tenantId) {
        return SecurityProperties.getInstance(tenantId,
                new ProcessIdentifier(httpRequest.getAutoLoginScope()));
    }

    private LoginManager getLoginManager(final long tenantId) throws ServletException {
        try {
            return LoginManagerFactory.getLoginManager(tenantId);
        } catch (final LoginManagerNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private boolean doAutoLogin(final HttpServletRequestAccessor request,
            final long tenantId) throws ServletException {
        try {
            getLoginManager(tenantId)
                    .login(request,
                            new AutoLoginCredentials(getSecurityProperties(request, tenantId), tenantId));
            return true;
        } catch (final LoginFailedException e) {
            return false;
        }

    }

    private void reCreateUser(final ServletRequest request, final HttpSession session, final APISession apiSession) {
        User user;
        final String locale = getLocale(request);
        user = new User(apiSession.getUserName(), locale);
        user.setUseCredentialTransmission(useCredentialsTransmission(apiSession));
        session.setAttribute(LoginManager.USER_SESSION_PARAM_KEY, user);
    }

    private String getLocale(final ServletRequest request) {
        String userLocaleStr = null;
        final String theLocaleCookieName = "BOS_Locale";
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final Cookie theCookies[] = httpRequest.getCookies();
        if (theCookies != null) {
            for (int i = 0; i < theCookies.length; i++) {
                if (theCookies[i].getName().equals(theLocaleCookieName)) {
                    final Cookie theCookie = theCookies[i];
                    userLocaleStr = theCookie.getValue().toString();
                    break;
                }
            }
        }
        if (userLocaleStr == null) {
            userLocaleStr = request.getLocale().toString();
        }
        return userLocaleStr;
    }

    private boolean isAutoLogin(final HttpServletRequestAccessor request, final long requestedTenantId) {
        return request.isAutoLoginRequested()
                && getSecurityProperties(request, requestedTenantId).allowAutoLogin();
    }

    private boolean useCredentialsTransmission(final APISession apiSession) {
        return PropertiesFactory.getSecurityProperties(apiSession.getTenantId())
                .useCredentialsTransmission();
    }

    private LoginUrl createLoginUrl(final String redirectUrl, final long requestedTenantId) throws ServletException {
        try {
            return new LoginUrl(getLoginManager(requestedTenantId),
                    requestedTenantId,
                    redirectUrl);
        } catch (final LoginUrlException e) {
            throw new ServletException(e);
        }
    }

    private RedirectUrl makeRedirectUrl(final HttpServletRequestAccessor httpRequest, final String url) {
        final RedirectUrlBuilder builder = new RedirectUrlBuilder(url);
        builder.appendParameters(httpRequest.getParameterMap());
        return builder.build();
    }

    private void cleanHttpSession(final HttpSession session) {
        SessionUtil.sessionLogout(session);
    }

    private void ensureUserSession(final ServletRequest request, final HttpSession session, final APISession apiSession) {
        final User user = (User) session.getAttribute(LoginManager.USER_SESSION_PARAM_KEY);
        if (user == null) {
            reCreateUser(request, session, apiSession);
        }
    }
}
