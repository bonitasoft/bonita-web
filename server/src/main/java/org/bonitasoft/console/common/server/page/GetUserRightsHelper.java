/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.page;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileCriterion;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.profile.ProfileEntrySearchDescriptor;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Fabio Lombardi
 */
public class GetUserRightsHelper {

    private final APISession apiSession;

    public GetUserRightsHelper(final APISession apiSession) {
        this.apiSession = apiSession;
    }

    public List<String> getUserRights() throws BonitaException {
        final List<String> rights = new ArrayList<>();
        final ProfileAPI profileAPI = TenantAPIAccessor.getProfileAPI(apiSession);
        final List<Profile> profilesForUser = profileAPI.getProfilesForUser(apiSession.getUserId(), 0, Integer.MAX_VALUE, ProfileCriterion.NAME_ASC);
        for (final Profile profile : profilesForUser) {
            final List<ProfileEntry> profileEntries = getProfileEntriesByProfile(profile.getId(), profileAPI);
            for (final ProfileEntry profileEntry : profileEntries) {
                final String userRight = profileEntry.getPage();
                if (userRight != null) {
                    rights.add(userRight);
                }
            }
        }
        return rights;
    }

    private List<ProfileEntry> getProfileEntriesByProfile(final long profileId, final ProfileAPI profileAPI) throws SearchException {
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, Integer.MAX_VALUE);
        searchOptionsBuilder.filter(ProfileEntrySearchDescriptor.PROFILE_ID, profileId);
        return profileAPI.searchProfileEntries(searchOptionsBuilder.done()).getResult();
    }
}
