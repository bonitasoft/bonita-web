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
package org.bonitasoft.web.rest.server.api.system;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.server.api.CommonAPI;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.ProfileEngineClient;
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.session.SessionDefinition;
import org.bonitasoft.web.toolkit.client.common.session.SessionItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Julien Mege
 */
public class APISession extends CommonAPI<SessionItem> {

    public static final String APISESSION = "apiSession";

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(SessionDefinition.TOKEN);
    }

    @Override
    public SessionItem get(final APIID unusedId) {
        final org.bonitasoft.engine.session.APISession apiSession = getEngineSession();
        final SessionItem session = new SessionItem();

        if (apiSession != null) {
            session.setAttribute(SessionItem.ATTRIBUTE_SESSIONID, String.valueOf(apiSession.getId()));
            session.setAttribute(SessionItem.ATTRIBUTE_USERID, String.valueOf(apiSession.getUserId()));
            session.setAttribute(SessionItem.ATTRIBUTE_USERNAME, apiSession.getUserName());
            session.setAttribute(SessionItem.ATTRIBUTE_IS_TECHNICAL_USER, String.valueOf(apiSession.isTechnicalUser()));
            session.setAttribute(SessionItem.ATTRIBUTE_CONF, getUserRights(apiSession));
        }

        return session;
    }
    
    public String getUserRights(org.bonitasoft.engine.session.APISession apiSession) {
        List<Profile> profiles = getProfilesForUser(apiSession.getUserId(), apiSession);
        if (apiSession.isTechnicalUser()) {
            return getUserRightsForTechnicalUser(apiSession);
        } else {
            return getUserRightsForProfiles(profiles, apiSession);
        }
    }

    private List<Profile> getProfilesForUser(long userId, org.bonitasoft.engine.session.APISession apiSession) {
        EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor());
        ProfileEngineClient profileApi = engineClientFactory.createProfileEngineClient(apiSession);
        return profileApi.listProfilesForUser(apiSession.getUserId());
    }
    
    private String getUserRightsForProfiles(List<Profile> profiles, org.bonitasoft.engine.session.APISession apiSession) {
        List<String> userRights = new ArrayList<String>();
        SHA1Generator sha1Generator = new SHA1Generator();
        for (Profile profile: profiles) {
            List<ProfileEntry> profileEntries = getProfileEntriesForProfile(profile.getId(), apiSession);
            for (ProfileEntry profileEntry : profileEntries) {
                
                // User rights are defined by the Profile Entries of a profile
                String userRight = profileEntry.getPage();
                if (userRight != null) {
                    userRights.add(sha1Generator.getHash(userRight.concat(String.valueOf(apiSession.getId()))));
                }
                
            }
        }    
        return JSonSerializer.serialize(userRights);
    }

    private List<ProfileEntry> getProfileEntriesForProfile(Long profileId, org.bonitasoft.engine.session.APISession apiSession) {
        EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor());
        ProfileEntryEngineClient profileEntryApi = engineClientFactory.createProfileEntryEngineClient(apiSession);
        return profileEntryApi.getProfileEntriesByProfile(profileId);
    }

    private String getUserRightsForTechnicalUser(org.bonitasoft.engine.session.APISession apiSession) {
        List<String> userRights = new ArrayList<String>();
        SHA1Generator sha1Generator = new SHA1Generator();
        userRights.add(sha1Generator.getHash("userlistingadmin".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("rolelistingadmin".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("grouplistingadmin".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("importexportorganization".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("profilelisting".concat(String.valueOf(apiSession.getId()))));
        return JSonSerializer.serialize(userRights);
    }

}
