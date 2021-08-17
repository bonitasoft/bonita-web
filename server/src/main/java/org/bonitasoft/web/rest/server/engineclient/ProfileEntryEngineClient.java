/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * <p>
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.engineclient;

import java.util.Collections;
import java.util.List;

import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.impl.SearchResultImpl;

/**
 * @author Vincent Elcrin
 *
 */
public class ProfileEntryEngineClient {

    protected ProfileEntryEngineClient() {
    }

    public ProfileEntry getProfileEntry(final Long id) {
        return null;
    }

    public SearchResult<ProfileEntry> searchProfiles(final SearchOptions options) {
        return new SearchResultImpl<>(0, Collections.emptyList());
    }

    public List<ProfileEntry> getProfileEntriesByProfile(final Long profileId) {
        return Collections.emptyList();
    }

    public List<ProfileEntry> getAllChildsOfAProfileEntry(final Long id) {
        return Collections.emptyList();
    }
}
