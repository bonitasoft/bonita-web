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
package org.bonitasoft.console.server.datastore.monitoring.report;

import java.util.Map;

import org.bonitasoft.web.rest.api.model.monitoring.report.ReportItem;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasSearch;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchReportHelper implements DatastoreHasSearch<ReportItem> {

    private final DefaultReports defaultReports;

    public SearchReportHelper(DefaultReports reports) {
        defaultReports = reports;
    }

    @Override
    public ItemSearchResult<ReportItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        return createItemSearchResult();
    }

    private ItemSearchResult<ReportItem> createItemSearchResult() {
        return new ItemSearchResult<ReportItem>(0,
                defaultReports.asList().size(),
                defaultReports.asList().size(),
                defaultReports.asList());
    }

}
