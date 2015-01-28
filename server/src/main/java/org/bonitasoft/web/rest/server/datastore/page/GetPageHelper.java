/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.page;

import org.bonitasoft.engine.page.Page;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.engineclient.PageEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Fabio Lombardi
 *
 */
public class GetPageHelper implements DatastoreHasGet<PageItem> {

    private final PageEngineClient pageClient;

    public GetPageHelper(final PageEngineClient pageClient) {
        this.pageClient = pageClient;
    }

    @Override
    public PageItem get(final APIID id) {
        final Page page = pageClient.getPageByUrlToken(id.toString());
        return new PageItemConverter().convert(page);
    }

}
