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
package org.bonitasoft.console.server.api.userXP.profile;

import java.util.List;

import org.bonitasoft.console.client.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.console.client.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.console.server.api.deployer.DeployerFactory;
import org.bonitasoft.console.server.datastore.ComposedDatastore;
import org.bonitasoft.console.server.datastore.profile.member.AddProfileMemberHelper;
import org.bonitasoft.console.server.datastore.profile.member.DeleteProfileMemberHelper;
import org.bonitasoft.console.server.datastore.profile.member.SearchProfileMembersHelper;
import org.bonitasoft.console.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.console.server.engineclient.EngineClientFactory;
import org.bonitasoft.console.server.engineclient.ProfileMemberEngineClient;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.Datastore;

/**
 * @author Nicolas Tith
 * @author Séverin Moussel
 * 
 */
public class APIProfileMember extends AbstractAPIMember<ProfileMemberItem> {

    @Override
    protected ItemDefinition<ProfileMemberItem> defineItemDefinition() {
        return ProfileMemberDefinition.get();
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        ProfileMemberEngineClient engineClient = createProfileMemberEngineClient();
        ComposedDatastore<ProfileMemberItem> datastore = new ComposedDatastore<ProfileMemberItem>();
        datastore.setAddHelper(new AddProfileMemberHelper(engineClient));
        datastore.setDeleteHelper(new DeleteProfileMemberHelper(engineClient));
        datastore.setSearchHelper(new SearchProfileMembersHelper(engineClient));
        return datastore;
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    @Override
    protected void fillDeploys(final ProfileMemberItem item, final List<String> deploys) {
        /*
         * Need to be done there and not in constructor
         * because need the engine session which is set
         * by setter instead of being injected in API constructor...
         */
        addDeployer(getDeployerFactory().createProfileDeployer(ProfileMemberItem.ATTRIBUTE_PROFILE_ID));
        super.fillDeploys(item, deploys);

    }

    protected DeployerFactory getDeployerFactory() {
        return new DeployerFactory(getEngineSession());
    }

    private ProfileMemberEngineClient createProfileMemberEngineClient() {
        return getEngineClientFactory()
                .createProfileMemberEngineClient(getEngineSession());
    }

    private EngineClientFactory getEngineClientFactory() {
        return new EngineClientFactory(getEngineAPIAccessor());
    }

    private EngineAPIAccessor getEngineAPIAccessor() {
        return new EngineAPIAccessor();
    }
}
