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
package org.bonitasoft.console.server.api.organization;

import org.bonitasoft.console.client.model.identity.ProfessionalContactDataDefinition;
import org.bonitasoft.console.client.model.identity.ProfessionalContactDataItem;
import org.bonitasoft.console.server.api.ConsoleAPI;
import org.bonitasoft.console.server.datastore.organization.ProfessionalContactDataDatastore;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasAdd;
import org.bonitasoft.web.toolkit.server.api.APIHasGet;
import org.bonitasoft.web.toolkit.server.api.APIHasUpdate;
import org.bonitasoft.web.toolkit.server.api.Datastore;

/**
 * @author Paul AMAR
 * 
 */
public class APIProfessionalContactData extends ConsoleAPI<ProfessionalContactDataItem> implements APIHasGet<ProfessionalContactDataItem>,
        APIHasUpdate<ProfessionalContactDataItem>, APIHasAdd<ProfessionalContactDataItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(ProfessionalContactDataDefinition.TOKEN);
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new ProfessionalContactDataDatastore(getEngineSession());
    }

    @Override
    public ProfessionalContactDataItem add(final ProfessionalContactDataItem item) {
        return super.add(item);
    }

}
