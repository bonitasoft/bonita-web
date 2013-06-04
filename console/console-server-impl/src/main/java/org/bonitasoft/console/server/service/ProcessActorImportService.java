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
package org.bonitasoft.console.server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;
import org.bonitasoft.web.toolkit.client.common.exception.service.ServiceException;

/**
 * @author Paul AMAR
 * 
 */
public class ProcessActorImportService extends ConsoleService {

    public final static String TOKEN = "/bpm/process/importActors";

    @Override
    public Object run() {
        final APISession apiSession = getSession();
        InputStream xmlStream = null;
        try {
            final File xmlFile = new File(getParameter("file"));
            if (!xmlFile.exists()) {
                throw new Exception("File: " + getParameter("file") + " does not exist.");
            }

            xmlStream = new FileInputStream(xmlFile);
            final byte[] actorsXmlContent = IOUtils.toByteArray(xmlStream);
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
            processAPI.importActorMapping(Long.valueOf(getParameter("process_id")), actorsXmlContent);

        } catch (final InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return "";
    }
}
