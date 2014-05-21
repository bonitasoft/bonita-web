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
package org.bonitasoft.console.common.server.login.impl.standard;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.datastore.Credentials;
import org.bonitasoft.console.common.server.login.datastore.UserLogger;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;

/**
 * @author Chong Zhao
 * 
 */
public class StandardLoginManagerImpl implements LoginManager {

    @Override
    public String getLoginpageURL(HttpServletRequest request, final long tenantId, final String redirectURL) {
        final StringBuilder url = new StringBuilder();
        String context = request.getContextPath();
        String servletPath = request.getServletPath();
        if (StringUtils.isNotBlank(servletPath) && servletPath.startsWith("/mobile")) {
            context += "/mobile";
        }
        url.append(context).append(LoginManager.LOGIN_PAGE).append("?");
        if (tenantId != -1L) {
            url.append(LoginManager.TENANT).append("=").append(tenantId).append("&");
        }
        url.append(LoginManager.REDIRECT_URL).append("=").append(redirectURL);
        return url.toString();
    }

    @Override
    public void login(final HttpServletRequestAccessor request, Credentials credentials) throws LoginFailedException {
        String local = DEFAULT_LOCALE;
        if (request.getParameterMap().get("_l") != null
                && request.getParameterMap().get("_l").length >= 0) {
            local = request.getParameterMap().get("_l")[0];
        }
        final User user = new User(request.getUsername(), local);
        APISession session = getUserLogger().doLogin(credentials);
        user.setUseCredentialTransmission(useCredentialsTransmission(session));
        SessionUtil.sessionLogin(user, session, request.getHttpSession());
    }

    protected UserLogger getUserLogger() {
        return new UserLogger();
    }

    protected boolean useCredentialsTransmission(final APISession apiSession) {
        return PropertiesFactory.getSecurityProperties(apiSession.getTenantId()).useCredentialsTransmission();
    }

}
