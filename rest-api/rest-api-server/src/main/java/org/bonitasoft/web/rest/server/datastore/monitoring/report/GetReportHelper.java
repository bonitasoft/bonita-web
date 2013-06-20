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
package org.bonitasoft.web.rest.server.datastore.monitoring.report;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.web.rest.model.monitoring.report.ReportItem;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 * 
 */
public class GetReportHelper implements DatastoreHasGet<ReportItem> {

    private static Logger LOGGER = Logger.getLogger(GetReportHelper.class.getName());

    private final DefaultReports defaultReports;

    public GetReportHelper(DefaultReports reports) {
        this.defaultReports = reports;
    }

    @Override
    public ReportItem get(APIID id) {
        return searchReport(id);
    }

    private ReportItem searchReport(APIID id) {
        for (ReportItem report : defaultReports.asList()) {
            if (isRequestedReport(id, report)) {
                return report;
            }
        }
        LOGGER.log(Level.FINE, "The report " + id.getPart(0) + " was not founnd");
        return null;
    }

    private boolean isRequestedReport(APIID id, ReportItem report) {
        return report.getId().equals(id);
    }

}
