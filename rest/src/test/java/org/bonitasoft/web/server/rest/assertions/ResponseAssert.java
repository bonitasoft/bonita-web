/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.server.rest.assertions;

import javax.ws.rs.core.Response;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Objects;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

    protected ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
    }

    public static ResponseAssert assertThat(Response actual) {
        return new ResponseAssert(actual);
    }

    public ResponseAssert hasBodyEqual(String body) {
        info.description("Response body is not matching. This might introduce an API break");
        Objects.instance().assertEqual(info, replaceSpaceAndLineBreak(actual.readEntity(String.class)), replaceSpaceAndLineBreak(body)); 
        return this;
    }
    
    public ResponseAssert hasStatus(int status) {
        Objects.instance().assertEqual(info, actual.getStatus(), status);
        return this;
    }
    
    private String replaceSpaceAndLineBreak(String text) {
        return text.replaceAll("\\s+", "");
    }
}
