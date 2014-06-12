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
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.engine.identity.CustomUserInfoDefinitionCreator;
import org.bonitasoft.engine.identity.CustomUserInfoValue;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

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
            throw new APIException(new _("An error occurred while creating a definition"), e);
        }
    }

    public void deleteDefinition(long id) {
        try {
            identity.deleteCustomUserInfoDefinition(id);
        } catch (DeletionException e) {
            throw new APIException(new _("An error occurred while deleting an item with the id %id%", new Arg("id", id)), e);
        }
    }

    public List<CustomUserInfoDefinition> listDefinitions(int startIndex, int maxResult) {
        return identity.getCustomUserInfoDefinitions(startIndex, maxResult);
    }

    public long countDefinitions() {
        return identity.getNumberOfCustomInfoDefinitions();
    }

    public List<CustomUserInfo> listCustomInformation(long userId, int startIndex, int maxResult) {
        return identity.getCustomUserInfo(userId, startIndex, maxResult);
    }

    public SearchResult<CustomUserInfoValue> searchCustomUserInfoValues(SearchOptions options) {
        return identity.searchCustomUserInfoValues(options);
    }

    public CustomUserInfoValue setCustomUserInfoValue(long definitionId, long userId, String value) {
        try {
            return identity.setCustomUserInfoValue(definitionId, userId, value);
        } catch (UpdateException e) {
            throw new APIItemNotFoundException(org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition.TOKEN, APIID.makeAPIID(definitionId, userId));
        }
    }
}
