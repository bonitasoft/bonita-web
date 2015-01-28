/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.server.service;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.ImportStatus;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.business.application.ApplicationImportPolicy;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.ImportException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.common.model.ImportStatusMessages;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Julien Mege
 */
public class ApplicationsImportService extends BonitaImportService {

    public final static String TOKEN = "/application/import";

    /**
     * organization data file
     */
    public static final String FILE_UPLOAD = "applicationsDataUpload";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(ApplicationsImportService.class.getName());

    protected ApplicationAPI getApplicationAPI() throws InvalidSessionException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getApplicationAPI(getSession());
    }

    @Override
    public String getToken() {
        return TOKEN;
    }

    @Override
    public String getFileUploadParamName() {
        return FILE_UPLOAD;
    }

    @Override
    public ImportStatusMessages importFileContent(final byte[] fileContent, final String importPolicyAsString) throws ExecutionException, ImportException, AlreadyExistsException, InvalidSessionException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        final ApplicationImportPolicy importPolicy = ApplicationImportPolicy.valueOf(importPolicyAsString);
        final List<ImportStatus> ImportStatusList = getApplicationAPI().importApplications(fileContent, importPolicy);
        return new ImportStatusMessages(ImportStatusList);
    }

    @Override
    protected String getFileFormatExceptionMessage() {
        return _("Can't import Applications.", getLocale());
    }

    @Override
    protected String getAlreadyExistsExceptionMessage(final AlreadyExistsException e) {
        return _("Can't import applications. An application '%token%' already exists", getLocale(), new Arg("token", e.getName()));
    }

    @Override
    protected String getFileReadingError() {
        return _("Error during Application import file reading.");
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}