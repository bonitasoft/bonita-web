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
package org.bonitasoft.web.rest.server.api.profile;

import java.util.List;

import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.deployer.DeployerFactory;
import org.bonitasoft.web.rest.server.datastore.ComposedDatastore;
import org.bonitasoft.web.rest.server.datastore.profile.GetProfileHelper;
import org.bonitasoft.web.rest.server.datastore.profile.SearchProfilesHelper;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.ProfileEngineClient;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Nicolas Tith
 * @author Séverin Moussel
 */
public class APIProfile extends ConsoleAPI<ProfileItem> implements APIHasGet<ProfileItem>, APIHasSearch<ProfileItem> {

    @Override
    protected void fillDeploys(ProfileItem item, List<String> deploys) {
        addDeployer(getDeployerFactory().createUserDeployer(PageItem.ATTRIBUTE_CREATED_BY_USER_ID));
        addDeployer(getDeployerFactory().createUserDeployer(PageItem.ATTRIBUTE_UPDATED_BY_USER_ID));
        
        super.fillDeploys(item, deploys);
    }

    protected DeployerFactory getDeployerFactory() {
        return new DeployerFactory(getEngineSession());
    }
    
    @Override
    protected ComposedDatastore<ProfileItem> defineDefaultDatastore() {

        final ProfileEngineClient profileClient = createProfileEngineClient();

        final ComposedDatastore<ProfileItem> datastore = new ComposedDatastore<ProfileItem>();
        datastore.setGetHelper(new GetProfileHelper(profileClient));
        datastore.setSearchHelper(new SearchProfilesHelper(profileClient));
        return datastore;
    }

    private ProfileEngineClient createProfileEngineClient() {
        return new EngineClientFactory(new EngineAPIAccessor(getEngineSession())).createProfileEngineClient();
    }

    @Override
    protected ItemDefinition<ProfileItem> defineItemDefinition() {
        return ProfileDefinition.get();
    }

    @Override
    public String defineDefaultSearchOrder() {
        // FIXME Use an engine descriptor
        return "name ASC";
    }

}
