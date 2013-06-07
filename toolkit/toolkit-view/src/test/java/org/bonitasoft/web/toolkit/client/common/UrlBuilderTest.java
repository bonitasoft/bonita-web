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
package org.bonitasoft.web.toolkit.client.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class UrlBuilderTest {

    @Test
    public void testWeCanBuildAUrlWithParameters() throws Exception {
        UrlBuilder builder = createHackedUrlBuilder("rootUrl");
        builder.addParameter("aParameter", "itsValue");
        builder.addParameter("anotherParameter", "itsOwnValue");

        assertThat(builder.toString(),
                either(equalTo("rootUrl?aParameter=itsValue&anotherParameter=itsOwnValue"))
                        .or(equalTo("rootUrl?anotherParameter=itsOwnValue&aParameter=itsValue")));
    }

    private UrlBuilder createHackedUrlBuilder(String rootUrl) {
        return new UrlBuilder(rootUrl) {

            @Override
            protected String encode(String decodedURL) {
                return decodedURL;
            }
        };
    }

}
