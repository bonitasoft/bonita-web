/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;

/**
 * @author Vincent Elcrin
 */
public class TokenProfileProvider implements UserRightsBuilder.TokenProvider {

    private List<String> tokens = new ArrayList<String>();

    public TokenProfileProvider(List<Profile> profiles, ProfileEntryEngineClient client) {
        for (Profile profile : profiles) {
            for (ProfileEntry entry : client.getProfileEntriesByProfile(profile.getId())) {
                add(entry.getPage());
            }
        }
    }

    private void add(String token) {
        if(token != null) {
            tokens.add(token);
        }
    }

    @Override
    public List<String> getTokens() {
        return tokens;
    }
}
