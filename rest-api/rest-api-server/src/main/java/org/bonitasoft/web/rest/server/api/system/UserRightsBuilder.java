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

import java.util.Arrays;
import java.util.List;

/**
 * @author Vincent Elcrin
 */
public class UserRightsBuilder {

    private org.bonitasoft.engine.session.APISession session;

    private SHA1Generator generator = new SHA1Generator();

    public UserRightsBuilder(org.bonitasoft.engine.session.APISession session) {
        this.session = session;
    }

    public List<String> buildFrom(List<String> tokens) {
        return Arrays.asList(generator.getHash(tokens.get(0).concat(String.valueOf(session.getId()))));
    }
}
