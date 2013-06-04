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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bonitasoft.console.client.model.monitoring.report.ReportItem;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 * 
 */
public class DefaultReports {

    private static List<ReportItem> reports = Arrays.asList(
            createReportItem("case_list", _("Case list"), _("List of cases")),
            createReportItem("case_avg_time", _("Case average time"), _("average case duration")),
            createReportItem("task_list", _("Task list"), _("List of tasks"))
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
