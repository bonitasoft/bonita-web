/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.localization;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class RedirectUrlBuilderTest {

    @Test
    public void testWeCanBuildHashTaggedParamAreKept() throws Exception {
        final RedirectUrlBuilder redirectUrlBuilder =
                new RedirectUrlBuilder("myredirecturl?parambeforehash=true#hashparam=true");
        final String url = redirectUrlBuilder.build().getUrl();

        assertEquals("myredirecturl?parambeforehash=true#hashparam=true", url);
    }

    @Test
    public void testPostParamsAreNotAddedToTheUrl() {
        final Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("postParam", someValues("true"));

        final RedirectUrlBuilder redirectUrlBuilder = new RedirectUrlBuilder("myredirecturl?someparam=value#hashparam=true");
        final String url = redirectUrlBuilder.build().getUrl();

        assertEquals("myredirecturl?someparam=value#hashparam=true", url);
    }

    private String[] someValues(final String... values) {
        return values;
    }
}
