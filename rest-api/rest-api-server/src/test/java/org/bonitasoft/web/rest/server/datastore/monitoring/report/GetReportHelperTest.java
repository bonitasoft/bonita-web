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

import org.bonitasoft.web.rest.model.monitoring.report.ReportItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.monitoring.report.DefaultReports;
import org.bonitasoft.web.rest.server.datastore.monitoring.report.GetReportHelper;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class GetReportHelperTest extends APITestWithMock {

    @Test
    public void testWeGetRetrieveADefaultReport() {
        DefaultReports defaultReports = new DefaultReports();
        GetReportHelper getHelper = new GetReportHelper(defaultReports);
        ReportItem expectedReport = defaultReports.asList().get(0);

        ReportItem fetchedReport = getHelper.get(expectedReport.getId());

        areEquals(expectedReport, fetchedReport);
    }
}
