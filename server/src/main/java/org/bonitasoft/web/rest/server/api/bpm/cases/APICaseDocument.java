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
package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.deployer.DeployerFactory;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseDocumentDatastore;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Fabio Lombardi
 *
 */
public class APICaseDocument extends ConsoleAPI<CaseDocumentItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(CaseDocumentDefinition.TOKEN);
    }

    @Override
    public CaseDocumentItem get(final APIID id) {
        return getCaseDocumentDatastore().get(id);
    }

    @Override
    public CaseDocumentItem add(final CaseDocumentItem item) {
        return getCaseDocumentDatastore().add(item);
    }

    @Override
    public CaseDocumentItem update(final APIID id, final Map<String, String> attributes) {
        return getCaseDocumentDatastore().update(id, attributes);
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    @Override
    protected void fillDeploys(final CaseDocumentItem item, final List<String> deploys) {
        addDeployer(getDeployerFactory().createUserDeployer(CaseDocumentItem.ATTRIBUTE_SUBMITTED_BY_USER_ID));
        addDeployer(getDeployerFactory().createUserDeployer(CaseDocumentItem.ATTRIBUTE_AUTHOR));
        super.fillDeploys(item, deploys);
    }

    protected DeployerFactory getDeployerFactory() {
        return new DeployerFactory(getEngineSession());
    }

    @Override
    protected void fillCounters(final CaseDocumentItem item, final List<String> counters) {
    }

    @Override
    public ItemSearchResult<CaseDocumentItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        final ItemSearchResult<CaseDocumentItem> results = getCaseDocumentDatastore().search(page, resultsByPage, search, filters, orders);

        return results;
    }

    @Override
    public void delete(final List<APIID> ids) {
        getCaseDocumentDatastore().delete(ids);
    }

    protected CaseDocumentDatastore getCaseDocumentDatastore() {
        ProcessAPI processAPI;
        try {
            processAPI = TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
        final WebBonitaConstantsUtils constants = WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId());

        return new CaseDocumentDatastore(getEngineSession(), constants, processAPI, new BonitaHomeFolderAccessor());
    }
}
