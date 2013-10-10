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

import org.apache.commons.io.IOUtils;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.server.ServiceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Séverin Moussel
 * 
 */
public class OrganizationImportService extends ConsoleService {

    public final static String TOKEN = "/organization/import";

    /**
     * organization data file
     */
    private static final String FILE_UPLOAD = "organizationDataUpload";

    @Override
    public Object run() {
        final APISession apiSession = getSession();
        final File xmlFile = new File(getParameter(FILE_UPLOAD));
        InputStream xmlStream = null;
        try {
            xmlStream = new FileInputStream(xmlFile);
            final byte[] organizationContent = IOUtils.toByteArray(xmlStream);
            final IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession);
            identityAPI.importOrganization(new String(organizationContent));
        } catch (final Exception e) {
            throw new ServiceException(TOKEN, _("Can't import organization. Please check that your file is well-formed",
                    getLocale()), e);
        } finally {
            if (xmlStream != null) {
                try {
                    xmlStream.close();
                } catch (final IOException e) {
                    xmlStream = null;
                }
            }
        }

        return "";
    }

}
