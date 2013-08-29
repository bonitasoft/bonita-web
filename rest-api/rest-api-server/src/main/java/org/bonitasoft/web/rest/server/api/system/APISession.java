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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.rest.server.api.CommonAPI;
import org.bonitasoft.web.rest.server.api.profile.APIProfileMember;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.ProfileEngineClient;
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
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

            try {
                session.setAttribute(SessionItem.ATTRIBUTE_SESSIONID, String.valueOf(apiSession.getId()));
                session.setAttribute(SessionItem.ATTRIBUTE_USERID, String.valueOf(apiSession.getUserId()));
                session.setAttribute(SessionItem.ATTRIBUTE_USERNAME, apiSession.getUserName());
                session.setAttribute(SessionItem.ATTRIBUTE_IS_TECHNICAL_USER, String.valueOf(apiSession.isTechnicalUser()));
                session.setAttribute(SessionItem.ATTRIBUTE_CONF, getUserRights(apiSession));
                
            } catch (final Exception e) {
                throw new APIException(new SessionException(e.getMessage(), e));
            }
        }

        return session;
    }
    
    public String getUserRights(org.bonitasoft.engine.session.APISession apiSession) {
        List<Profile> profiles = getProfilesForUser(apiSession.getUserId(), apiSession);
        MessageDigest sha1Generator;
        
        try {
            sha1Generator = MessageDigest.getInstance("SHA-1");
            if (apiSession.isTechnicalUser()) {
                return getUserRightsForTechnicalUser(apiSession, sha1Generator);
            } else {
                return getUserRightsForProfiles(profiles, apiSession, sha1Generator);
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return JSonSerializer.serialize("");
    }


    private List<Profile> getProfilesForUser(long userId, org.bonitasoft.engine.session.APISession apiSession) {
        EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor());
        ProfileEngineClient profileApi = engineClientFactory.createProfileEngineClient(apiSession);
        return profileApi.listProfilesForUser(apiSession.getUserId());
    }
    
    private String getUserRightsForProfiles(List<Profile> profiles, org.bonitasoft.engine.session.APISession apiSession, MessageDigest sha1Generator) {
        List<String> userRights = new ArrayList<String>();
        for (Profile profile: profiles) {
            List<ProfileEntry> profileEntries = getProfileEntriesForProfile(profile.getId(), apiSession);
            for (ProfileEntry profileEntry : profileEntries) {
                
                // User rights are defined by the Profile Entries for a given profile
                String userRight = profileEntry.getPage();
                if (userRight != null) {
                    userRights.add(getStringFromBytes(sha1Generator.digest(((userRight.concat(String.valueOf(apiSession.getId()))).getBytes()))));
                }
                
            }
        }    
        return JSonSerializer.serialize(userRights);
    }

    private String getUserRightsForTechnicalUser(org.bonitasoft.engine.session.APISession apiSession, MessageDigest sha1Generator) {
        List<String> userRights = new ArrayList<String>();
        userRights.add(getStringFromBytes(sha1Generator.digest((("userlistingadmin".concat(String.valueOf(apiSession.getId()))).getBytes()))));
        userRights.add(getStringFromBytes(sha1Generator.digest((("rolelistingadmin".concat(String.valueOf(apiSession.getId()))).getBytes()))));
        userRights.add(getStringFromBytes(sha1Generator.digest((("grouplistingadmin".concat(String.valueOf(apiSession.getId()))).getBytes()))));
        userRights.add(getStringFromBytes(sha1Generator.digest((("importexportorganization".concat(String.valueOf(apiSession.getId()))).getBytes()))));
        userRights.add(getStringFromBytes(sha1Generator.digest((("profilelisting".concat(String.valueOf(apiSession.getId()))).getBytes()))));
        return JSonSerializer.serialize(userRights);
    }

    private List<ProfileEntry> getProfileEntriesForProfile(Long profileId, org.bonitasoft.engine.session.APISession apiSession) {
        EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor());
        ProfileEntryEngineClient profileEntryApi = engineClientFactory.createProfileEntryEngineClient(apiSession);
        return profileEntryApi.getProfileEntriesByProfile(profileId);
    }

    public static String getStringFromBytes(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                           '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buf = new StringBuffer();
        for (int j=0; j<b.length; j++) {
           buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
           buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }
    
    private ItemSearchResult<ProfileMemberItem> getProfiles(final String userId) {
        final Map<String, String> filter = Collections.singletonMap(ProfileItem.FILTER_USER_ID, userId.toString());
      
        return new APIProfileMember().search(0, 100, null, null, filter);
    }

    @Override
    protected void fillDeploys(final SessionItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final SessionItem item, final List<String> counters) {
    }
}
