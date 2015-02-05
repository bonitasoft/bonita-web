/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.applicationpage;

import org.bonitasoft.engine.business.application.ApplicationPage;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageItem;

/**
 * @author Julien Mege
 *
 */
public class ApplicationPageItemConverter {

    public ApplicationPageItem toApplicationPageItem(final ApplicationPage applicationPage) {
        final ApplicationPageItem item = new ApplicationPageItem();
        item.setId(applicationPage.getId());
        item.setToken(applicationPage.getToken());
        item.setPageId(applicationPage.getPageId());
        item.setApplicationId(applicationPage.getApplicationId());
        return item;
    }

}
