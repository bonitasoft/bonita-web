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

package org.bonitasoft.console.common.server.login;

import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;

import javax.servlet.ServletException;

/**
 * Created by Vincent Elcrin
 * Date: 30/08/13
 * Time: 10:23
 */
public class TenantIdAccessor {

    private final long tenantId;

    public TenantIdAccessor(HttpServletRequestAccessor request) throws ServletException {
        tenantId = parseTenantId(request.getTenantId());
    }

    public long getRequestedTenantId() {
        return tenantId;
    }

    /*
     * Ensure tenant id by fetching default one if request is empty
     */
    public long ensureTenantId() throws ServletException {
        if(tenantId < 0) {
            return getDefaultTenantId();
        }
        return getRequestedTenantId();
    }

    public long getDefaultTenantId() throws ServletException {
        return TenantsManagementUtils.getDefaultTenantId();
    }

    protected long parseTenantId(final String tenantId) throws ServletException {
        if (tenantId != null) {
            try {
                return Long.parseLong(tenantId);
            } catch (final NumberFormatException e) {
                throw new ServletException("Invalid tenant id.", e);
            }
        } else {
            return -1L;
        }
    }
}
