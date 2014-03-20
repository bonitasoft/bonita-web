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

import org.bonitasoft.console.common.server.login.LoginManagerProperties;
import org.bonitasoft.console.common.server.login.LoginManagerPropertiesFactory;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
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
public class APISession extends ConsoleAPI<SessionItem> {

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

    public String getUserRights(final org.bonitasoft.engine.session.APISession apiSession) {
        final List<Profile> profiles = getProfilesForUser(apiSession.getUserId(), apiSession);
        if (apiSession.isTechnicalUser()) {
            return getUserRightsForTechnicalUser(apiSession);
        } else {
            return getUserRightsForProfiles(profiles, apiSession);
        }
    }

    private List<Profile> getProfilesForUser(final long userId, final org.bonitasoft.engine.session.APISession apiSession) {
        final EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor(apiSession));
        final ProfileEngineClient profileApi = engineClientFactory.createProfileEngineClient();
        return profileApi.listProfilesForUser(apiSession.getUserId());
    }

    private String getUserRightsForProfiles(final List<Profile> profiles, final org.bonitasoft.engine.session.APISession apiSession) {
        final List<String> userRights = new ArrayList<String>();
        final SHA1Generator sha1Generator = new SHA1Generator();
        for (final Profile profile : profiles) {
            final List<ProfileEntry> profileEntries = getProfileEntriesForProfile(profile.getId(), apiSession);
            for (final ProfileEntry profileEntry : profileEntries) {

                // User rights are defined by the Profile Entries of a profile
                final String userRight = profileEntry.getPage();
                if (userRight != null) {
                    userRights.add(sha1Generator.getHash(userRight.concat(String.valueOf(apiSession.getId()))));
                }

            }
        }
        // TODO restrict the current user from being able to call the logout directly as a profileEntry (is it possible)?
        if (isLogoutDisabled(apiSession.getTenantId())) {
            userRights.add(LoginManagerProperties.LOGOUT_DISABLED);
        }
        return JSonSerializer.serialize(userRights);
    }

    private List<ProfileEntry> getProfileEntriesForProfile(final Long profileId, final org.bonitasoft.engine.session.APISession apiSession) {
        final EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor(apiSession));
        final ProfileEntryEngineClient profileEntryApi = engineClientFactory.createProfileEntryEngineClient();
        return profileEntryApi.getProfileEntriesByProfile(profileId);
    }

    private String getUserRightsForTechnicalUser(final org.bonitasoft.engine.session.APISession apiSession) {
        final List<String> userRights = new ArrayList<String>();
        final SHA1Generator sha1Generator = new SHA1Generator();
        userRights.add(sha1Generator.getHash("userlistingadmin".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("rolelistingadmin".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("grouplistingadmin".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("importexportorganization".concat(String.valueOf(apiSession.getId()))));
        userRights.add(sha1Generator.getHash("profilelisting".concat(String.valueOf(apiSession.getId()))));
        return JSonSerializer.serialize(userRights);
    }

    /**
     * enable to know if the logout button is visible or not
     * 
     * @param tenantId
     *            the current user tenant id
     */
    protected boolean isLogoutDisabled(long tenantId) {
        return LoginManagerPropertiesFactory.getProperties(tenantId).isLogoutDisabled();
    }

}
