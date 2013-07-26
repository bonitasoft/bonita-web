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
package org.bonitasoft.web.rest.server.engineclient;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.engine.identity.UserCreator.UserField;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.identity.UserUpdater;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

public class UserEngineClient {

    private IdentityAPI identityAPI;

    public UserEngineClient(IdentityAPI identityAPI) {
        this.identityAPI = identityAPI;
    }
    
    public User update(long userId, UserUpdater userUpdater) {
        try {
            return identityAPI.updateUser(userId, userUpdater);
        } catch (UserNotFoundException e) {
            throw new APIException(_("Can't update user. User not found"), e);
        } catch (UpdateException e) {
            throw new APIException(_("Error when updating user"), e);
        }
    }
    
    public User create(UserCreator creator) {
        try {
            return identityAPI.createUser(creator);
        } catch (AlreadyExistsException e) {
            String message = _("Can't create user. User '%userName%' already exists", 
                    new Arg("userName", creator.getFields().get(UserField.NAME)));
            throw new APIForbiddenException(message, e);
        } catch (CreationException e) {
            throw new APIException(_("Error when creating user"), e);
        }
    }
    
    public User get(long userId) {
        try {
            return identityAPI.getUser(userId);
        } catch (UserNotFoundException e) {
            throw new APIException(_("User not found"), e);
        }
    }
    
    public void delete(List<Long> userIds) {
        try {
            identityAPI.deleteUsers(userIds);
        } catch (DeletionException e) {
            throw new APIException(_("Error when deleting users"), e);
        }
    }
    
    public SearchResult<User> search(SearchOptions searchOptions) {
        try {
            return identityAPI.searchUsers(searchOptions);
        } catch (SearchException e) {
            throw new APIException(_("Error when searching users"), e);
        }
    }
}
