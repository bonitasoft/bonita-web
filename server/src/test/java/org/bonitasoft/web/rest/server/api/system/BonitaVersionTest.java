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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class BonitaVersionTest {

    @Mock
    private VersionFile file;

    @Mock
    private InputStream stream;

    @Test
    public void should_read_version_stream_to_return_its_content() throws Exception {
        final InputStream stream = IOUtils.toInputStream("1.0.0\nBonitasoft © 2015");
        given(file.getStream()).willReturn(stream);

        final BonitaVersion version = new BonitaVersion(file);

        assertThat(version.getVersion()).isEqualTo("1.0.0");
        assertThat(version.getCopyright()).isEqualTo("Bonitasoft © 2015");
        IOUtils.closeQuietly(stream);
    }

    @Test
    public void should_read_version_stream_to_return_its_version() throws Exception {
        final InputStream stream = IOUtils.toInputStream("1.0.0");
        given(file.getStream()).willReturn(stream);

        final BonitaVersion version = new BonitaVersion(file);

        assertThat(version.getVersion()).isEqualTo("1.0.0");
        IOUtils.closeQuietly(stream);
    }

    @Test
    public void should_trim_extra_new_line_character() throws Exception {
        final InputStream stream = IOUtils.toInputStream("1.0.0\n");
        given(file.getStream()).willReturn(stream);

        final BonitaVersion version = new BonitaVersion(file);

        assertThat(version.getVersion()).isEqualTo("1.0.0");
        IOUtils.closeQuietly(stream);
    }

    @Test
    public void should_return_an_empty_version_when_the_stream_is_null() throws Exception {
        given(file.getStream()).willReturn(null);

        final BonitaVersion version = new BonitaVersion(file);

        assertThat(version.getVersion()).isEqualTo("");
        assertThat(version.getCopyright()).isEqualTo("");
    }

    @Test
    public void should_return_an_empty_version_when_it_is_unable_to_read_the_file() throws Exception {
        given(stream.read()).willThrow(new RuntimeException());
        given(file.getStream()).willReturn(stream);

        final BonitaVersion version = new BonitaVersion(file);

        assertThat(version.getVersion()).isEqualTo("");
        assertThat(version.getCopyright()).isEqualTo("");
    }
}
