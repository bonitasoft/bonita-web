/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.servlet;

import java.util.Optional;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.identity.Icon;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Anthony Birembaut
 * @author Baptiste Mesta
 */
public class OrganizationIconServlet extends IconServlet {

    @Override
    protected Optional<IconContent> retrieveIcon(Long iconId, APISession apiSession) {
        IdentityAPI identityAPI = getIdentityApi(apiSession);
        Icon icon;
        try {
            icon = identityAPI.getIcon(iconId);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
        return Optional.of(new IconContent(icon.getContent(), icon.getMimeType()));
    }

    IdentityAPI getIdentityApi(APISession apiSession) {
        return new APIClient(apiSession).getIdentityAPI();
    }

}
