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
package org.bonitasoft.console.common.server;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.api.model.ModelFactory;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.server.RestAPIFactory;
import org.bonitasoft.web.toolkit.server.servlet.APIServlet;

/**
 * @author Séverin Moussel
 * 
 */
public class CommonAPIServlet extends APIServlet {

    private static final long serialVersionUID = 525945083859596909L;

    public CommonAPIServlet() {
        super();
    }

    @Override
    protected ItemDefinitionFactory defineApplicatioFactoryCommon() {
        return new ModelFactory();
    }

    @Override
    protected RestAPIFactory defineApplicatioFactoryServer() {
        return new CommonRestAPIFactory();
    }

    @Override
    protected void catchAllExceptions(final Throwable exception, final HttpServletRequest req, final HttpServletResponse resp) {
        if (exception instanceof APIException && exception.getCause() != null && exception.getCause() instanceof InvalidSessionException) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, exception.getMessage(), exception);
            }
            outputException(exception, resp, HttpServletResponse.SC_UNAUTHORIZED);
        } else if (exception.getCause() instanceof NotFoundException) {
            outputException(null, resp, HttpServletResponse.SC_NOT_FOUND);
        } else {
            super.catchAllExceptions(exception, req, resp);
        }
    }

}
