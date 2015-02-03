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
        return TenantAPIAccessor.getLivingApplicationAPI(getSession());
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