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

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.console.common.server.login.datastore.AutoLoginCredentials;
import org.bonitasoft.console.common.server.preferences.properties.ProcessIdentifier;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;

public class AutoLoginRule extends AuthenticationRule {

    @Override
    public boolean doAuthorize(final HttpServletRequestAccessor request, final TenantIdAccessor tenantIdAccessor) throws ServletException {
        final long tenantId = tenantIdAccessor.ensureTenantId();
        return isAutoLogin(request, tenantId)
                && doAutoLogin(request, tenantId);
    }

    private boolean doAutoLogin(final HttpServletRequestAccessor request,
                                final long tenantId) throws ServletException {
        try {
            getLoginManager(tenantId).login(
                    request,
                    new AutoLoginCredentials(getSecurityProperties(request, tenantId), tenantId));
            return true;
        } catch (final LoginFailedException e) {
            return false;
        }

    }

    private boolean isAutoLogin(final HttpServletRequestAccessor request, final long tenantId) {
        return request.isAutoLoginRequested()
                && getSecurityProperties(request, tenantId).allowAutoLogin();
    }

    // protected for testing
    protected SecurityProperties getSecurityProperties(final HttpServletRequestAccessor httpRequest, final long tenantId) {
        return SecurityProperties.getInstance(tenantId,
                new ProcessIdentifier(httpRequest.getAutoLoginScope()));
    }
}