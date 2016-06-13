/**
 * Copyright (C) 2012 BonitaSoft S.A.
 *
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
import java.util.Map;

import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.profile.ProfileMember;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;
import org.bonitasoft.web.toolkit.client.common.i18n._;

/**
 * @author Vincent Elcrin
 *
 */
public class ProfileMemberEngineClient {

    private ProfileAPI profileApi;

    protected ProfileMemberEngineClient(ProfileAPI profileApi) {
        this.profileApi = profileApi;
    }

    public SearchResult<ProfileMember> searchProfileMembers(String memberType, SearchOptions searchOptions) {
        try {
            return profileApi.searchProfileMembers(memberType, searchOptions);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (SearchException e) {
            throw new APIException(e);
        }
    }

    public ProfileMember createProfileMember(Long profileId, Long userId, Long groupId, Long roleId) {
        try {
            return profileApi.createProfileMember(profileId, userId, groupId, roleId);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (AlreadyExistsException e) {
            throw new APIForbiddenException(new _("Profile member already exists"), e);
        } catch (CreationException e) {
            throw new APIException(e);
        }
    }

    public Map<Long, Long> countProfileMembers(List<Long> profileIds) {
        try {
            return profileApi.getNumberOfProfileMembers(profileIds);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    public void deleteProfileMember(Long id) {
        try {
            profileApi.deleteProfileMember(id);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (DeletionException e) {
            throw new APIException(e);
        }
    }

}
