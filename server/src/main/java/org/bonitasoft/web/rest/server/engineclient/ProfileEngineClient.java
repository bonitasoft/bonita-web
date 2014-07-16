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

import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileCriterion;
import org.bonitasoft.engine.profile.ProfileNotFoundException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 *
 */
public class ProfileEngineClient {

    private ProfileAPI profileApi;

    protected ProfileEngineClient(ProfileAPI profileApi) {
        this.profileApi = profileApi;
    }

    public Profile getProfile(Long id) {
        try {
            return profileApi.getProfile(id);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (RetrieveException e) {
            throw new APIException(e);
        } catch (ProfileNotFoundException e) {
            throw new APIItemNotFoundException(ProfileDefinition.TOKEN, APIID.makeAPIID(id));
        }
    }

    public SearchResult<Profile> searchProfiles(SearchOptions options) {
        try {
            return profileApi.searchProfiles(options);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (SearchException e) {
            throw new APIException(e);
        }
    }

    public List<Profile> listProfilesForUser(long userId) {
        try {
            return profileApi.getProfilesForUser(userId, 0, Integer.MAX_VALUE, ProfileCriterion.ID_ASC);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (RetrieveException e) {
            throw new APIException(e);
        }
    }

}
