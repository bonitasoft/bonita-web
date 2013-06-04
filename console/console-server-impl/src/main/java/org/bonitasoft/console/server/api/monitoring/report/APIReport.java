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
package org.bonitasoft.console.server.api.monitoring.report;

import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.model.monitoring.report.ReportDefinition;
import org.bonitasoft.console.client.model.monitoring.report.ReportItem;
import org.bonitasoft.console.common.server.CommonAPI;
import org.bonitasoft.console.server.api.deployer.DeployerFactory;
import org.bonitasoft.console.server.datastore.ComposedDatastore;
import org.bonitasoft.console.server.datastore.monitoring.report.DefaultReports;
import org.bonitasoft.console.server.datastore.monitoring.report.GetReportHelper;
import org.bonitasoft.console.server.datastore.monitoring.report.SearchReportHelper;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasAdd;
import org.bonitasoft.web.toolkit.server.api.APIHasDelete;
import org.bonitasoft.web.toolkit.server.api.APIHasGet;
import org.bonitasoft.web.toolkit.server.api.APIHasSearch;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Vincent Elcrin
 * 
 */
public class APIReport extends CommonAPI<ReportItem>
        implements APIHasGet<ReportItem>,
        APIHasSearch<ReportItem>,
        APIHasAdd<ReportItem>,
        APIHasDelete {

    @Override
    protected ItemDefinition<ReportItem> defineItemDefinition() {
        return ReportDefinition.get();
    }

    @Override
    protected ComposedDatastore<ReportItem> defineDefaultDatastore() {
        // legacy code
        return new ComposedDatastore<ReportItem>();
    }

    @Override
    public ReportItem get(APIID id) {
        return new GetReportHelper(new DefaultReports()).get(id);
    }

    @Override
    public ItemSearchResult<ReportItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        return new SearchReportHelper(new DefaultReports())
                .search(page,
                        resultsByPage,
                        search,
                        orders,
                        filters);
    }

    @Override
    protected void fillDeploys(final ReportItem item, final List<String> deploys) {
        /*
         * Need to be done there and not in constructor
         * because need the engine session which is set
         * by setter instead of being injected in API constructor...
         */
        addDeployer(getDeployerFactory().createUserDeployer(ReportItem.ATTRIBUTE_INSTALLED_BY));
        super.fillDeploys(item, deploys);
    }

    protected DeployerFactory getDeployerFactory() {
        return new DeployerFactory(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return ReportItem.ATTRIBUTE_NAME;
    }

}
