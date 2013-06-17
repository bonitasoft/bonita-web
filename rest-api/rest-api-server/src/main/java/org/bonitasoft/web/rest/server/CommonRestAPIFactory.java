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
package org.bonitasoft.web.rest.server;

import org.bonitasoft.web.rest.server.api.document.APIArchivedDocument;
import org.bonitasoft.web.rest.server.api.document.APIDocument;
import org.bonitasoft.web.toolkit.client.common.exception.api.APINotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.service.ServiceNotFoundException;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.server.API;
import org.bonitasoft.web.toolkit.server.RestAPIFactory;
import org.bonitasoft.web.toolkit.server.Service;

/**
 * @author Séverin Moussel
 * 
 */
public class CommonRestAPIFactory extends RestAPIFactory {

    @Override
    public API<? extends IItem> defineApis(final String apiToken, final String resourceToken) {
        if ("bpm".equals(apiToken)) {
            if ("document".equals(resourceToken)) {
                return new APIDocument();
            } else if ("archiveddocument".equals(resourceToken)) {
                return new APIArchivedDocument();
            }
        }
        throw new APINotFoundException(apiToken, resourceToken);
    }
}
