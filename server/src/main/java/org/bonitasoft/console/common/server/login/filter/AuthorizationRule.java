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
import org.bonitasoft.console.common.server.login.TenantIdAccessor;

import javax.servlet.ServletException;

/**
 * Created by Vincent Elcrin
 * Date: 30/08/13
 * Time: 10:28
 */
public interface AuthorizationRule {

    /*
     * @return rather the process need to be aborted or not
     */
    public abstract boolean doAuthorize(HttpServletRequestAccessor request, TenantIdAccessor tenantIdAccessor) throws ServletException;

}
