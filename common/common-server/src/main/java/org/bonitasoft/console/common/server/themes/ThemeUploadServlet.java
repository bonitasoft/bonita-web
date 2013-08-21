/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.themes;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.utils.DateUtil;
import org.bonitasoft.console.common.server.utils.UnZIPUtil;
import org.bonitasoft.engine.session.APISession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class upload theme
 * 
 * @author Minghui.Dai
 */
public class ThemeUploadServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = -7016515158304375336L;

    /**
     * The engine API session param key name
     */
    public static final String API_SESSION_PARAM_KEY = "apiSession";

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ThemeUploadServlet.class.getName());

    @Override
    @SuppressWarnings("unchecked")
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final APISession apiSession = (APISession) request.getSession().getAttribute(API_SESSION_PARAM_KEY);
        response.setContentType("text/html;charset=UTF-8");
        final PrintWriter responsePW = response.getWriter();
        try {
            if (!ServletFileUpload.isMultipartContent(request)) {
                final String theErrorMessage = "Error while using the servlet ThemeUploadServlet to upload theme,it is not MultipartContent";
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, theErrorMessage);
                }
                throw new ServletException(theErrorMessage);
            }
            final FileItemFactory fileItemFactory = new DiskFileItemFactory();
            final ServletFileUpload serviceFileUpload = new ServletFileUpload(fileItemFactory);
            final List<FileItem> items = serviceFileUpload.parseRequest(request);
            for (final Iterator<FileItem> i = items.iterator(); i.hasNext();) {
                final FileItem item = i.next();
                if (item.isFormField()) {
                    continue;
                }
                final String fileName = item.getName();

                String themeTempPath = getCommonTempFolder(apiSession.getTenantId()) + File.separator + fileName;
                File uploadedFile = new File(themeTempPath);
                int suffix = 0;
                while (uploadedFile.exists()) {
                    uploadedFile = new File(getCommonTempFolder(apiSession.getTenantId()), fileName + "." + suffix);
                    suffix++;
                }
                themeTempPath = uploadedFile.getAbsolutePath();
                // extract ZIP file
                UnZIPUtil.unzip(item.getInputStream(), themeTempPath);
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "uploaded File Path: " + themeTempPath);
                }
                final String encodedUploadedFilePath = URLEncoder.encode(themeTempPath, "UTF-8");
                // validate and copy
                validateThemePackage(apiSession.getTenantId(), themeTempPath, true);
                final String installedBy = (String) request.getSession().getAttribute(apiSession.getUserName());

                // add to theme config
                final String idStr = String.valueOf(new Date().getTime());
                final String nameStr = fileName.substring(0, fileName.indexOf(".zip"));
                final String installedByStr = installedBy;
                final String installedDateStr = DateUtil.formatDate(new Date());
                final String applyStr = "false";
                ThemeConfigManager.getInstance(apiSession.getTenantId()).createTheme(idStr, nameStr, installedByStr, installedDateStr, applyStr);
                responsePW.print(encodedUploadedFilePath);
                responsePW.close();
            }
        } catch (final Exception e) {
            final String theErrorMessage = "Error while using the servlet ThemeUploadServlet to upload theme.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, theErrorMessage, e);
            }
            throw new ServletException(theErrorMessage, e);
        }
    }

    protected String getCommonTempFolder(final long tenantId) throws ServletException {
        String themesParentFolder = "";
        try {
            themesParentFolder = WebBonitaConstantsUtils.getInstance(tenantId).getTempFolder().getPath();
        } catch (final RuntimeException e) {
            final String errorMessage = "Error while using the servlet ThemeUploadServlet to get themes parent folder.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        return themesParentFolder;
    }

    public void validateThemePackage(final long tenantId, final String themePath, final boolean deleteWhileError) throws ThemeStructureException {
        final ThemeValidator validator = new ThemeValidator();
        try {
            validator.doValidate(tenantId, themePath, deleteWhileError);
        } catch (final IOException e) {
            final String theErrorMessage = "Exception while getting the themes folder: it may be not exist or path is error.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, theErrorMessage);
            }
        }
    }
}
