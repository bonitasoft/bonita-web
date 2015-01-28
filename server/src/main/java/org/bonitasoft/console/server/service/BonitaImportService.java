/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.server.service;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.utils.TenantFolder;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.ImportException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.common.model.ImportStatusMessages;
import org.bonitasoft.web.rest.server.framework.json.JacksonSerializer;
import org.bonitasoft.web.toolkit.server.ServiceException;

/**
 * Import REST Resources and return the Import Status Messages
 *
 * @author Julien Mege
 */
abstract class BonitaImportService extends ConsoleService {

    /**
     * import policy
     */
    public static final String IMPORT_POLICY_PARAM_NAME = "importPolicy";

    @Override
    public Object run() {
        File xmlFile;
        try {
            xmlFile = getTenantFolder().getTempFile(getFileUploadParamValue(),
                    getTenantId());
        } catch (final IOException e) {
            throw new ServiceException(getToken(), e.getMessage(), e);
        }


        final String importPolicyAsString = getParameter(IMPORT_POLICY_PARAM_NAME);

        try {
            final JacksonSerializer serializer = new JacksonSerializer();
            final ImportStatusMessages importStatusMessages = importFileContent(readImportFile(xmlFile), importPolicyAsString);
            return serializer.serialize(importStatusMessages);
        } catch (final InvalidSessionException e) {
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, _("Session expired. Please log in again."), e);
            }
            throw e;
        } catch (final ExecutionException e) {
            if (getLogger().isLoggable(Level.WARNING)) {
                getLogger().log(Level.WARNING, e.getMessage(), e);
            }
            throw new ServiceException(getToken(), getFileFormatExceptionMessage(), e);
        } catch (final ImportException e) {
            if (getLogger().isLoggable(Level.WARNING)) {
                getLogger().log(Level.WARNING, e.getMessage(), e);
            }
            throw new ServiceException(getToken(), getFileFormatExceptionMessage(), e);
        }  catch (final AlreadyExistsException e) {
            if (getLogger().isLoggable(Level.WARNING)) {
                getLogger().log(Level.WARNING, e.getMessage(), e);
            }
            throw new ServiceException(getToken(), getAlreadyExistsExceptionMessage(e), e);
        } catch (final Exception e) {
            if (getLogger().isLoggable(Level.SEVERE)) {
                getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServiceException(getToken(), e);
        }
    }

    protected byte[] readImportFile(final File xmlFile) {
        InputStream xmlStream = null;
        byte[] content;
        try {
            xmlStream = new FileInputStream(xmlFile);
            content = IOUtils.toByteArray(xmlStream);
        } catch (final Exception e) {
            throw new ServiceException(getFileReadingError(), e);
        } finally {
            if (xmlStream != null) {
                try {
                    xmlStream.close();
                } catch (final IOException e) {
                    xmlStream = null;
                }
            }
        }

        return content;
    }

    protected TenantFolder getTenantFolder() {
        return new TenantFolder();
    }

    protected String getFileUploadParamValue() {
        return getParameter(getFileUploadParamName());
    }

    protected long getTenantId() {
        return getSession().getTenantId();
    }

    protected abstract String getFileReadingError();

    protected abstract String getToken();

    protected abstract String getFileUploadParamName();

    protected abstract ImportStatusMessages importFileContent(final byte[] fileContent, final String importPolicy) throws ExecutionException, ImportException, AlreadyExistsException, InvalidSessionException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException;

    protected abstract String getFileFormatExceptionMessage();

    protected abstract String getAlreadyExistsExceptionMessage(AlreadyExistsException e);

    protected abstract Logger getLogger();

}
