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

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Elcrin
 */
public class BonitaVersionTest {

    @Test
    public void should_read_version_stream_to_return_its_content() throws Exception {
        InputStream stream = IOUtils.toInputStream("1.0.0");

        BonitaVersion version = new BonitaVersion(stream);

        assertThat(version.toString()).isEqualTo("1.0.0");
    }

    @Test
    public void should_return_an_empty_version_when_the_stream_is_null() throws Exception {

        BonitaVersion version = new BonitaVersion(null);

        assertThat(version.toString()).isEqualTo("");
    }

    @Test
    public void should_return_an_empty_version_when_it_is_unable_to_read_the_file() throws Exception {
        InputStream stream = mock(InputStream.class);
        given(stream.read()).willThrow(new RuntimeException());

        BonitaVersion version = new BonitaVersion(stream);

        assertThat(version.toString()).isEqualTo("");
    }
}
