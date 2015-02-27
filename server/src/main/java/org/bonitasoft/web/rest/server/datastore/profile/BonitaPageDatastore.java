/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Fabio Lombardi
 * 
 */
public class BonitaPageDatastore {

    public BonitaPageDatastore() {
    }

    public List<IItem> fromBonitaPageItemToIItem() {
        List<IItem> pageListDest = new ArrayList<IItem>();
        List<BonitaPageItem> pageListSource = new ArrayList<BonitaPageItem>(new PageLister().getPages());
        for (BonitaPageItem page : pageListSource) {
            pageListDest.add((IItem) page);
        }
        return pageListDest;
    }

    public ItemSearchResult<BonitaPageItem> search(int page, int resultsByPage, String search, Map<String, String> filters, String orders) {
        List<BonitaPageItem> pages = new PageLister().getPages();
        return new ItemSearchResult<BonitaPageItem>(page, resultsByPage, pages.size(), new ArrayList<BonitaPageItem>(pages));
    }

    /**
     * @param id
     * @return
     */
    public BonitaPageItem get(APIID id) {
        return new PageLister().getPage(id.toString());
    }

}
