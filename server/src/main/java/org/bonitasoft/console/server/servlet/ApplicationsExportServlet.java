/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 34 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/

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
        final ApplicationAPI applicationAPI = TenantAPIAccessor.getApplicationAPI(apiSession);
        return applicationAPI;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}
