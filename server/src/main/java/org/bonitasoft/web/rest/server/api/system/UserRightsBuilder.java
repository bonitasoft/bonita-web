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

/**
 * @author Vincent Elcrin
 */
public class UserRightsBuilder {

    private org.bonitasoft.engine.session.APISession session;

    private TokenProvider provider;

    private SHA1Generator generator = new SHA1Generator();

    interface TokenProvider {
        List<String> getTokens();
    }

    public UserRightsBuilder(org.bonitasoft.engine.session.APISession session, TokenProvider provider) {
        this.session = session;
        this.provider = provider;
    }

    public List<String> build() {
        List<String> rights = new ArrayList<String>();
        for (String token : provider.getTokens()) {
            rights.add(generator.getHash(token.concat(String.valueOf(session.getId()))));
        }
        return rights;
    }
}
