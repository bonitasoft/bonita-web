/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.page;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.profile.ProfileEntrySearchDescriptor;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;


/**
 * @author Fabio Lombardi
 *
 */
public class GetUserRightsHelper {

    private APISession apiSession;

    public GetUserRightsHelper(APISession apiSession) {
        this.apiSession = apiSession;
    }
    
    public List<String> getUserRights() throws ServletException {
        List<String> rights = new ArrayList<String>();
        try {
            ProfileAPI profileAPI = TenantAPIAccessor.getProfileAPI(apiSession);
            List<Profile> profilesForUser = profileAPI.getProfilesForUser(apiSession.getUserId());
            for (Profile profile: profilesForUser) {
                List<ProfileEntry> profileEntries = getProfileEntriesByProfile(profile.getId(), profileAPI);
                for (ProfileEntry profileEntry: profileEntries) {
                    final String userRight = profileEntry.getPage();
                    if (userRight != null) {
                        rights.add(userRight);                        
                    }
                }
            }
            
        } catch (Exception e) {
           throw new ServletException();
        } 
        return rights;
    }

    private List<ProfileEntry> getProfileEntriesByProfile(long profileId, ProfileAPI profileAPI) throws Exception {
        SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0,Integer.MAX_VALUE);
        searchOptionsBuilder.filter(ProfileEntrySearchDescriptor.PROFILE_ID, profileId);
        return profileAPI.searchProfileEntries(searchOptionsBuilder.done()).getResult();
    }
}
