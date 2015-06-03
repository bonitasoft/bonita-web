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
package org.bonitasoft.console.common.server.sso.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.sso.InternalSSOManager;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Yongtao Guo
 */
public class InternalSSOFilter implements Filter {

    /**
     * the URL param for the encrypted credentials
     */
    protected static final String USER_TOKEN_PARAM = "token";

    /**
     * API session parameter name in session
     */
    protected static final String APISESSION = "apiSession";

    /**
     * login fail message
     */
    protected static final String LOGIN_FAIL_MESSAGE = "loginFailMessage";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(InternalSSOFilter.class.getName());

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

        try {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final String userToken = httpRequest.getParameter(USER_TOKEN_PARAM);
            final HttpSession session = httpRequest.getSession();
            if (userToken != null && !userToken.isEmpty()) {
                final InternalSSOManager internalSSOManager = InternalSSOManager.getInstance();
                final Object obj = internalSSOManager.get(userToken);
                if (obj != null) {
                    if (TenantsManagementUtils.hasProfileForUser((APISession) obj)) {
                        final APISession aAPISession = (APISession) obj;
                        session.setAttribute(APISESSION, aAPISession);
                        internalSSOManager.remove(userToken);
                    } else {
                        request.setAttribute(LOGIN_FAIL_MESSAGE, "noProfileForUser");
                        long tenantId = -1L;
                        final String tenantIdStr = httpRequest.getParameter(AuthenticationManager.TENANT);
                        if (tenantIdStr != null) {
                            tenantId = Long.parseLong(tenantIdStr);
                        }
                        final String redirectURL = URLEncoder.encode(AuthenticationManager.DEFAULT_DIRECT_URL, "UTF-8");
                        ((HttpServletResponse) response).sendRedirect(getLoginpageURL(tenantId, redirectURL));
                    }
                }
            }
            chain.doFilter(request, response);

        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e);
        }
    }

    protected String getLoginpageURL(final long tenantId, final String redirectURL) {
        final StringBuilder url = new StringBuilder();
        url.append("src/main").append(AuthenticationManager.LOGIN_PAGE).append("?");
        if (tenantId != -1L) {
            url.append(AuthenticationManager.TENANT).append("=").append(tenantId).append("&");
        }
        url.append(AuthenticationManager.REDIRECT_URL).append("=").append(redirectURL);
        return url.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {

    }

}
