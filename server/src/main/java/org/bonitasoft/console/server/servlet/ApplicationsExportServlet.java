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
package org.bonitasoft.console.server.servlet;

import java.util.logging.Logger;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.ExportException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Julien MEGE
 */
public class ApplicationsExportServlet extends BonitaExportServlet {

    /**
     * export file name
     */
    private static final String EXPORT_FILE_NAME = "Application_Data.xml";

    /**
     * UID
     */
    private static final long serialVersionUID = 1800666571090128789L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(ApplicationsExportServlet.class.getName());

    @Override
    protected String getFileExportName() {
        return EXPORT_FILE_NAME;
    }

    @Override
    protected byte[] exportResources(final long[] ids, final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, ExecutionException, ExportException {
        final ApplicationAPI applicationAPI = getApplicationAPI(apiSession);
        return applicationAPI.exportApplications(ids);
    }

    protected ApplicationAPI getApplicationAPI(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        final ApplicationAPI applicationAPI = TenantAPIAccessor.getLivingApplicationAPI(apiSession);
        return applicationAPI;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}
