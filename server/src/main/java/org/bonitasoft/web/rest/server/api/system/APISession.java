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

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.console.common.server.login.LoginManagerProperties;
import org.bonitasoft.console.common.server.login.LoginManagerPropertiesFactory;
import org.bonitasoft.engine.profile.Profile;
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

/**
 * @author Julien Mege
 */
public class APISession extends ConsoleAPI<SessionItem> {

    public static final LoginManagerPropertiesFactory loginManagerPropertiesFactory = new LoginManagerPropertiesFactory();

    @Override
    protected SessionDefinition defineItemDefinition() {
        return (SessionDefinition) Definitions.get(SessionDefinition.TOKEN);
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
            session.setAttribute(SessionItem.ATTRIBUTE_VERSION, getVersion());
            session.setAttribute(SessionItem.ATTRIBUTE_COPYRIGHT, getCopyright());
            session.setAttribute(SessionItem.ATTRIBUTE_CONF, getUserRights(apiSession));
        }
        return session;
    }

    public String getUserRights(final org.bonitasoft.engine.session.APISession apiSession) {
        final List<Profile> profiles = getProfilesForUser(apiSession);
        if (apiSession.isTechnicalUser()) {
            return JSonSerializer.serialize(new UserRightsBuilder(apiSession, new TokenListProvider(Arrays.asList(
                    "userlistingadmin",
                    "rolelistingadmin",
                    "grouplistingadmin",
                    "importexportorganization",
                    "profilelisting",
                    "tenantMaintenance",
                    "pagelisting",
                    "businessdatamodelimport"))).build());
        } else {
            return getUserRightsForProfiles(profiles, apiSession);
        }
    }

    private List<Profile> getProfilesForUser(final org.bonitasoft.engine.session.APISession apiSession) {
        final EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor(apiSession));
        final ProfileEngineClient profileApi = engineClientFactory.createProfileEngineClient();
        return profileApi.listProfilesForUser(apiSession.getUserId());
    }

    private String getUserRightsForProfiles(final List<Profile> profiles, final org.bonitasoft.engine.session.APISession session) {
        final List<String> rights = new UserRightsBuilder(session, new TokenProfileProvider(profiles, createProfileEntryEngineClient(session)))
                .build();
        // TODO restrict the current user from being able to call the logout directly as a profileEntry (is it possible)?
        if (isLogoutDisabled(session.getTenantId())) {
            rights.add(LoginManagerProperties.LOGOUT_DISABLED);
        }
        return JSonSerializer.serialize(rights);
    }

    private ProfileEntryEngineClient createProfileEntryEngineClient(final org.bonitasoft.engine.session.APISession session) {
        final EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor(session));
        return engineClientFactory.createProfileEntryEngineClient();
    }

    /**
     * enable to know if the logout button is visible or not
     *
     * @param tenantId
     *            the current user tenant id
     */
    protected boolean isLogoutDisabled(final long tenantId) {
        return loginManagerPropertiesFactory.getProperties(tenantId).isLogoutDisabled();
    }

    public String getVersion() {
        return new BonitaVersion(new VersionFile()).getVersion();
    }

    public String getCopyright() {
        return new BonitaVersion(new VersionFile()).getCopyright();
    }
}
