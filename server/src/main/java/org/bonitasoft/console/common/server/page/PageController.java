/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PageController {

    /**
     * Let the custom page parse request for specific attribute handling.
     *
     * @param request
     *            the HTTP servlet request intended to be used as in a servlet
     * @param response
     *            the HTTP servlet response intended to be used as in a servlet
     * @param pageResourceProvider
     *            provide access to the resources contained in the custom page zip
     * @param pageContext
     *            provide access to the data relative to the context in which the custom page is displayed
     */
    void doGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext);
}
