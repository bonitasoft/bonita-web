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
package org.bonitasoft.platform.server;

import org.bonitasoft.console.common.server.api.system.APISession;
import org.bonitasoft.platform.server.api.platform.APIPlatform;
import org.bonitasoft.web.toolkit.client.common.exception.api.APINotFoundException;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.server.API;
import org.bonitasoft.web.toolkit.server.RestAPIFactory;
import org.bonitasoft.web.toolkit.server.Service;
import org.bonitasoft.web.toolkit.server.api.system.APII18nLocale;
import org.bonitasoft.web.toolkit.server.api.system.APII18nTranslation;

/**
 * @author Séverin Moussel
 * 
 */
public class PlatformFactoryServer extends RestAPIFactory {

    @Override
    public API<? extends IItem> defineApis(final String apiToken, final String resourceToken) {
        if ("platform".equals(apiToken)) {
            if ("platform".equals(resourceToken)) {
                return new APIPlatform();
            }
        } else if ("system".equals(apiToken)) {
            if ("i18nlocale".equals(resourceToken)) {
                return new APII18nLocale();
            } else if ("i18ntranslation".equals(resourceToken)) {
                return new APII18nTranslation();
            } else if ("session".equals(resourceToken)) {
                return new APISession();
            }
        }

        throw new APINotFoundException(apiToken, resourceToken);
    }

    @Override
    public Service defineServices(final String calledToolToken) {
        return null;
    }
}
