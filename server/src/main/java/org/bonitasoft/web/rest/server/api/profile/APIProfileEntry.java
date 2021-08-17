/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.profile;

import static org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem.ATTRIBUTE_INDEX;

import java.util.List;

import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.deployer.DeployerFactory;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;

/**
 * @author Nicolas Tith
 * @author Séverin Moussel
 * @deprecated since 7.13.0, this API is deprecated without any replacement
 */
@Deprecated
public class APIProfileEntry extends ConsoleAPI<ProfileEntryItem> implements
        APIHasGet<ProfileEntryItem>,
        APIHasSearch<ProfileEntryItem> {

    protected DeployerFactory factory;

    @Override
    protected ProfileEntryDefinition defineItemDefinition() {
        return ProfileEntryDefinition.get();
    }

    @Override
    public String defineDefaultSearchOrder() {
        return ATTRIBUTE_INDEX;
    }

    @Override
    protected void fillDeploys(final ProfileEntryItem item, final List<String> deploys) {
        super.fillDeploys(item, deploys);
    }

}
