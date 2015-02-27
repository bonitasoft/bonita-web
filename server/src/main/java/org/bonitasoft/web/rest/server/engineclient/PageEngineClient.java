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
package org.bonitasoft.web.rest.server.engineclient;

import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;


/**
 * @author Fabio Lombardi
 *
 */
public class PageEngineClient {

    private PageAPI pageAPI;

    /**
     * Default Constructor.
     * @param pageAPI
     */
    public PageEngineClient(PageAPI pageAPI) {
        this.pageAPI = pageAPI;       
    }

    /**
     * @param pageId
     * @return
     */
    public Page getPage(Long pageId) {
        try {
            return pageAPI.getPage(pageId);
        } catch (PageNotFoundException e) {
            throw new APIException(e);
        }
    }

    /**
     * @param urlToken
     * @return
     */
    public Page getPageByUrlToken(String urlToken) {
        try {
            return pageAPI.getPageByName(urlToken);
        } catch (PageNotFoundException e) {
            throw new APIException(e);
        }
    }
}
