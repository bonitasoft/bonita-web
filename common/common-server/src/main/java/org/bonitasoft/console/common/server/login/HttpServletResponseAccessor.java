/**
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

import org.bonitasoft.console.common.server.login.localization.Locator;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vincent Elcrin
 * 
 */
public class HttpServletResponseAccessor {

    private HttpServletResponse response;

    public HttpServletResponseAccessor(HttpServletResponse response) {
        this.response = response;
    }

    public void redirect(Locator location) throws ServletException {
        try {
            response.sendRedirect(location.getLocation());
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    public ServletResponse asServletResponse() {
        return response;
    }
}
