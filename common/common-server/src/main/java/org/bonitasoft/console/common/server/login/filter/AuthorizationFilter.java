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

import org.bonitasoft.console.common.server.login.*;
import org.bonitasoft.console.common.server.login.localization.LoginUrl;
import org.bonitasoft.console.common.server.login.localization.LoginUrlException;
import org.bonitasoft.console.common.server.login.localization.RedirectUrl;
import org.bonitasoft.console.common.server.login.localization.RedirectUrlBuilder;
import org.bonitasoft.console.common.server.utils.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Vincent Elcrin
 */
public class AuthorizationFilter implements Filter {

    private LinkedList<AuthorizationRule> rules = new LinkedList<AuthorizationRule>();

    public AuthorizationFilter() {
        addRules();
    }

    protected void addRules() {
        addRule(new AlreadyLoggedInRule());
        addRule(new AutoLoginRule());
    }

    public void addRule(AuthorizationRule rule) {
        rules.add(rule);
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor((HttpServletRequest) request);
        doAuthorizationFiltering(requestAccessor,
                new HttpServletResponseAccessor((HttpServletResponse) response),
                new TenantIdAccessor(requestAccessor), chain);
    }

    protected void doAuthorizationFiltering(HttpServletRequestAccessor requestAccessor,
                                            HttpServletResponseAccessor responseAccessor,
                                            TenantIdAccessor tenantIdAccessor,
                                            FilterChain chain) throws ServletException, IOException {

        if(!isAuthorized(requestAccessor, responseAccessor, tenantIdAccessor, chain)) {

            cleanHttpSession(requestAccessor.getHttpSession());
            responseAccessor.redirect(createLoginUrl(
                    makeRedirectUrl(requestAccessor, requestAccessor.getRedirectUrl()).getUrl(),
                    tenantIdAccessor.getRequestedTenantId()));
        }
    }

    /**
     * @return true if one of the rules pass false otherwise
     */
    private boolean isAuthorized(HttpServletRequestAccessor requestAccessor,
                                 HttpServletResponseAccessor responseAccessor,
                                 TenantIdAccessor tenantIdAccessor,
                                 FilterChain chain) throws ServletException, IOException {

        for(AuthorizationRule rule : rules) {
            if(rule.doAuthorize(requestAccessor, tenantIdAccessor)) {
                chain.doFilter(requestAccessor.asHttpServletRequest(), responseAccessor.asServletResponse());
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }


    // protected for test stubbing
    protected LoginManager getLoginManager(final long tenantId) throws ServletException {
        try {
            return LoginManagerFactory.getLoginManager(tenantId);
        } catch (final LoginManagerNotFoundException e) {
            throw new ServletException(e);
        }
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

}
