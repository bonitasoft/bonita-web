/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.server.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.web.toolkit.server.APIServletCall;
import org.bonitasoft.web.toolkit.server.ServletCall;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class APIServlet extends ToolkitHttpServlet {

    private static final long serialVersionUID = 1852124460966605504L;

    @Override
    protected ServletCall defineServletCall(final HttpServletRequest req, final HttpServletResponse resp) {
        return new APIServletCall(req, resp);
    }

}
