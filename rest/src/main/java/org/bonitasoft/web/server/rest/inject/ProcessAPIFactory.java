/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.server.rest.inject;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.glassfish.hk2.api.Factory;

public class ProcessAPIFactory implements Factory<ProcessAPI> {

    private HttpServletRequest httpRequest;
    
    @Inject
    public ProcessAPIFactory(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
    
    @Override
    public void dispose(ProcessAPI processAPI) {
        // DO NOTHING
    }

    @Override
    public ProcessAPI provide() {
        APISession apiSession = (APISession) httpRequest.getSession().getAttribute("apiSession");
        try {
            return TenantAPIAccessor.getProcessAPI(apiSession);
        } catch (Exception e) {
            throw new WebApplicationException(e, 500);
        }
    }
}
