/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.document;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.web.rest.model.document.DocumentDefinition;
import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.bpm.cases.APICaseVariable;
import org.bonitasoft.web.rest.server.api.document.api.impl.DocumentDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseVariableDatastore;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.junit.Before;
import org.mockito.Mock;

/**
 * @author Julien Mege, Fabio Lombardi
 */
public class APIDocument extends ConsoleAPI<DocumentItem> {
	 
		private DocumentDatastore documentDatastore = getDocumentDatastore();
	    
		@Override
	    protected ItemDefinition defineItemDefinition() {
	        return DocumentDefinition.get();
	    }

	    @Override
	    protected Datastore defineDefaultDatastore() {
	        return getDocumentDatastore();
	    }

	    @Override
	    public DocumentItem get(final APIID id) {
			return documentDatastore.get(id);
	    }

	    @Override
	    public DocumentItem add(final DocumentItem item) {
	        return documentDatastore.add(item);
	    }

	    @Override
	    public String defineDefaultSearchOrder() {
	        return "";
	    }

	    @Override
	    protected void fillDeploys(final DocumentItem item, final List<String> deploys) {
	    }

	    @Override
	    protected void fillCounters(final DocumentItem item, final List<String> counters) {
	    }
	    
	    private DocumentDatastore getDocumentDatastore() {
	        ProcessAPI processAPI;
	        try {
	            processAPI = TenantAPIAccessor.getProcessAPI(getEngineSession());
	        } catch (final Exception e) {
	            throw new APIException(e);
	        }
	        final WebBonitaConstantsUtils constants = WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId());

	        return new DocumentDatastore(getEngineSession(), constants, processAPI);
	    }
}
