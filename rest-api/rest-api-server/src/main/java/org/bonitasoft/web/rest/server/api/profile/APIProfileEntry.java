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
package org.bonitasoft.web.rest.server.api.profile;

import static org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem.ATTRIBUTE_CUSTOM_NAME;
import static org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem.ATTRIBUTE_INDEX;
import static org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem.ATTRIBUTE_NAME;
import static org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem.ATTRIBUTE_PAGE;
import static org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem.ATTRIBUTE_TYPE;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.deployer.DeployerFactory;
import org.bonitasoft.web.rest.server.datastore.ComposedDatastore;
import org.bonitasoft.web.rest.server.datastore.profile.entry.GetProfileEntryHelper;
import org.bonitasoft.web.rest.server.datastore.profile.entry.SearchProfileEntriesHelper;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIAttributeMissingException;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Nicolas Tith
 * @author Séverin Moussel
 */
public class APIProfileEntry extends ConsoleAPI<ProfileEntryItem> implements
        APIHasAdd<ProfileEntryItem>,
        APIHasUpdate<ProfileEntryItem>,
        APIHasGet<ProfileEntryItem>,
        APIHasSearch<ProfileEntryItem>,
        APIHasDelete
{

    @Override
    protected ProfileEntryDefinition defineItemDefinition() {
        return ProfileEntryDefinition.get();
    }

    @Override
    public ProfileEntryItem add(final ProfileEntryItem item) {

        checkDatas(item.getAttributes());

        return super.add(item);
    }

    @Override
    public ProfileEntryItem update(final APIID id, final Map<String, String> attributes) {

        checkDatas(attributes);

        return super.update(id, attributes);
    }

    /**
     * Check that at least one name is passed (Default or Custom)
     * 
     * @param attributes
     *            The attributes passed to the API
     */
    protected void checkDatas(final Map<String, String> attributes) {
        if (MapUtil.isBlank(attributes, ATTRIBUTE_NAME) && MapUtil.isBlank(attributes, ATTRIBUTE_CUSTOM_NAME)) {
            throw new APIAttributeMissingException(ATTRIBUTE_NAME);
        }

        if (ProfileEntryItem.VALUE_TYPE.link.equals(attributes.get(ATTRIBUTE_TYPE)) && MapUtil.isBlank(attributes, ATTRIBUTE_PAGE)) {
            throw new APIAttributeMissingException(ATTRIBUTE_PAGE);
        }

    }

    @Override
    public String defineDefaultSearchOrder() {
        return ATTRIBUTE_INDEX;
    }

    @Override
    protected void fillDeploys(final ProfileEntryItem item, final List<String> deploys) {
        /*
         * Need to be done there and not in constructor
         * because need the engine session which is set
         * by setter instead of being injected in API constructor...
         */
        DeployerFactory factory = getDeployerFactory();
        addDeployer(factory.createProfileDeployer(ProfileEntryItem.ATTRIBUTE_PROFILE_ID));
        addDeployer(factory.createProfileEntryDeployer(ProfileEntryItem.ATTRIBUTE_PARENT_ID));
        super.fillDeploys(item, deploys);

    }

    protected DeployerFactory getDeployerFactory() {
        return new DeployerFactory(getEngineSession());
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        ProfileEntryEngineClient profileEntryClient = createProfileEntryEngineClient();
        ComposedDatastore<ProfileEntryItem> datastore = new ComposedDatastore<ProfileEntryItem>();
        datastore.setGetHelper(new GetProfileEntryHelper(profileEntryClient));
        datastore.setSearchHelper(new SearchProfileEntriesHelper(profileEntryClient));
        return datastore;
    }

    private ProfileEntryEngineClient createProfileEntryEngineClient() {
        return getEngineClientFactory()
                .createProfileEntryEngineClient(getEngineSession());
    }

    private EngineClientFactory getEngineClientFactory() {
        return new EngineClientFactory(getEngineAPIAccessor());
    }

    private EngineAPIAccessor getEngineAPIAccessor() {
        return new EngineAPIAccessor();
    }

}
