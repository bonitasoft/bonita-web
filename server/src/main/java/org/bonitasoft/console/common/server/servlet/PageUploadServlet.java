/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.common.server.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.InvalidPageTokenException;
import org.bonitasoft.engine.exception.InvalidPageZipContentException;
import org.bonitasoft.engine.session.APISession;

public class PageUploadServlet extends TenantFileUploadServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = -5733037726905793432L;

    protected static final String ACTION_PARAM_NAME = "action";

    protected static final String ADD_ACTION = "add";

    protected static final String EDIT_ACTION = "edit";

    protected static final String PAGE_PROPERTIES = "page.properties";

    protected static final Logger LOGGER = Logger.getLogger(PageUploadServlet.class.getName());

    @Override
    protected String generateResponseString(final HttpServletRequest request, final String fileName, final File uploadedFile) throws Exception {

        final String responseString = super.generateResponseString(request, fileName, uploadedFile);
        final String permissions = getPermissions(request, uploadedFile);
        return responseString + RESPONSE_SEPARATOR + permissions;
    }

    protected String getPermissions(final HttpServletRequest request, final File uploadedFile) throws IOException, BonitaException {

        final String action = request.getParameter(ACTION_PARAM_NAME);
        final boolean checkIfItAlreadyExists = ADD_ACTION.equals(action);
        String permissions;
        try {
            final Set<String> permissionsSet = getPagePermissions(request, uploadedFile, checkIfItAlreadyExists);
            permissions = permissionsSet.toString();
        } catch (final InvalidPageZipContentException e) {
            permissions = getPermissionsError(e);
        } catch (final InvalidPageTokenException e) {
            permissions = getPermissionsError(e);
        } catch (final AlreadyExistsException e) {
            permissions = getPermissionsError(e);
        }
        if (permissions == null) {
            permissions = "[]";
        }
        return permissions;
    }

    protected String getPermissionsError(final Exception e) {
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return e.getClass().getSimpleName();
    }

    protected Set<String> getPagePermissions(final HttpServletRequest request, final File uploadedFile, final boolean checkIfItAlreadyExists)
            throws InvalidPageZipContentException, InvalidPageTokenException, AlreadyExistsException, BonitaException,
            IOException, InvalidPageZipContentException {
        final APISession apiSession = getAPISession(request);
        final CustomPageService customPageService = new CustomPageService();
        final Properties properties = customPageService.getPageProperties(apiSession, FileUtils.readFileToByteArray(uploadedFile),
                checkIfItAlreadyExists);
        return customPageService.getCustomPagePermissions(properties, PropertiesFactory.getResourcesPermissionsMapping(apiSession.getTenantId()), true);
    }

}
