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
package org.bonitasoft.web.toolkit.server.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.web.toolkit.server.ServiceFactory;
import org.bonitasoft.web.toolkit.server.ServiceServletCall;
import org.bonitasoft.web.toolkit.server.ServletCall;

/**
 * This class is the entry point of all server calls
 * 
 * @author Séverin Moussel
 * 
 */
@SuppressWarnings("serial")
public abstract class ServiceServlet extends ToolkitHttpServlet {

	private ServiceFactory serviceFactory;

	public ServiceServlet(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
    @Override
    protected ServletCall defineServletCall(final HttpServletRequest req, final HttpServletResponse resp) {
        return new ServiceServletCall(serviceFactory, req, resp);
    }
}
