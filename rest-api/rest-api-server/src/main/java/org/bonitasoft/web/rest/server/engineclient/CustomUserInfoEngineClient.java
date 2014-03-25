/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.engineclient;

import java.util.List;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.engine.identity.CustomUserInfoDefinitionCreator;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n._;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoEngineClient {

    private IdentityAPI identity;

    public CustomUserInfoEngineClient(IdentityAPI identity) {
        this.identity = identity;
    }

    public CustomUserInfoDefinition createDefinition(CustomUserInfoDefinitionCreator creator) {
        try {
            return identity.createCustomUserInfoDefinition(creator);
        } catch (CreationException e) {
            throw new APIException(new _("Error while creating definition"), e);
        }
    }

    public void deleteDefinition(long id) {
        try {
            identity.deleteCustomUserInfoDefinition(id);
        } catch (DeletionException e) {
            throw new APIException(new _("Error while deleting item with id <" + id + ">"), e);
        }
    }

    public List<CustomUserInfoDefinition> listDefinitions(int startIndex, int maxResult) {
        return identity.getCustomUserInfoDefinitions(startIndex, maxResult);
    }
}
