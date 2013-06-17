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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bonitasoft.web.rest.api.model.monitoring.report.ReportItem;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 * 
 */
public class DefaultReports {

    private static List<ReportItem> reports = Arrays
            .asList(
                    createReportItem(
                            "case_list",
                            _("Case list"),
                            _("This report, display the list of cases available in the different status (open or closed) for a given period and/or apps. You can access the portail, by clicking on the link in the below table.")),
                    createReportItem(
                            "case_avg_time",
                            _("Case average time"),
                            _("This report, display the average time to close a case. 2 data are available, 1 for the mean and one for the median for a given period and/or apps. You can access the portal, by clicking on the link in the below table.")),
                    createReportItem(
                            "task_list",
                            _("Task list"),
                            _("This report, display the list of human tasks available in the different status (opened, failed or closed) for a given period and/or apps. You can access the portail, by clicking on the link in the below table."))
            );

    public List<ReportItem> asList() {
        return reports;
    }

    private static ReportItem createReportItem(final String id, final String name, final String description) {
        final ReportItem item = new ReportItem();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setInstalledBy(APIID.makeAPIID("-1"));
        item.setInstalledOn(new Date(0L));
        return item;
    }
}
