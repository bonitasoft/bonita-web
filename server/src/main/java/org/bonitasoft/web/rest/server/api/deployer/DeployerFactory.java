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
package org.bonitasoft.web.rest.server.api.deployer;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.server.datastore.profile.GetBonitaPageHelper;
import org.bonitasoft.web.rest.server.datastore.profile.GetProfileHelper;
import org.bonitasoft.web.rest.server.datastore.profile.entry.GetProfileEntryHelper;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.framework.Deployer;

/**
 * @author Vincent Elcrin
 *
 */
public class DeployerFactory {

    private final APISession apiSession;

    private final EngineClientFactory factory;

    public DeployerFactory(final APISession apiSession) {
        this.apiSession = apiSession;
        factory = new EngineClientFactory(new EngineAPIAccessor(apiSession));
    }

    public UserDeployer createUserDeployer(final String attribute) {
        return new UserDeployer(new UserDatastore(apiSession), attribute);
    }

    public Deployer createProfileDeployer(final String attribute) {
        return new GenericDeployer<ProfileItem>(createProfileGetter(), attribute);
    }

    public Deployer createProfileEntryDeployer(final String attribute) {
        return new GenericDeployer<ProfileEntryItem>(createProfileEntryGetter(), attribute);
    }

    public Deployer createBonitaPageDeployer(final String attribute) {
        return new GenericDeployer<BonitaPageItem>(new GetBonitaPageHelper(), attribute);
    }

    private GetProfileHelper createProfileGetter() {
        return new GetProfileHelper(factory.createProfileEngineClient());
    }

    private GetProfileEntryHelper createProfileEntryGetter() {
        return new GetProfileEntryHelper(factory.createProfileEntryEngineClient());
    }

}
