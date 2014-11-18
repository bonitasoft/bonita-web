/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.themes;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.commons.io.IOUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ThemeArchiveTest {

    @Rule
    public TemporaryFolder testDirectory = new TemporaryFolder();

    private File themeDirectory;

    @Before
    public void setup() {
        themeDirectory = new File(testDirectory.getRoot(), "portal");
    }

    @Test
    public void should_extract_the_given_zip_into_destination_folder() throws Exception {
        final ThemeArchive themeArchive = new ThemeArchive(createStyleFiles("bonita.css"));

        themeArchive.extract(themeDirectory);

        assertThat(themeDirectory.list()).containsOnly("bonita.css");
    }

    @Test
    public void should_add_given_file_to_theme_directory() throws Exception {
        final ThemeArchive themeArchive = new ThemeArchive(createStyleFiles("bonita.css"));

        themeArchive.extract(themeDirectory).add("style.css", "body {}".getBytes());

        assertThat(themeDirectory.list()).containsOnly("bonita.css", "style.css");
    }

    @Test
    public void should_compile_given_file_into_theme_directory() throws Exception {
        final ThemeArchive themeArchive = new ThemeArchive(
                createStyleFiles("style1.less", "style2.less", "style3.css"));

        themeArchive.extract(themeDirectory).compile(
                new CompilableFile("style1.less", "style1.css"),
                new CompilableFile("style2.less", "style2.css"));

        assertThat(themeDirectory.list()).containsOnly(
                "style1.less", "style1.css", "style2.less", "style2.css", "style3.css");
    }

    @Test
    public void should_replace_old_directory_with_zip_content() throws Exception {
        themeDirectory.mkdir();
        new File(themeDirectory, "old-style.css").createNewFile();
        final ThemeArchive themeArchive = new ThemeArchive(createStyleFiles("bonita.css"));

        themeArchive.extract(themeDirectory);

        assertThat(themeDirectory.list()).containsOnly("bonita.css");
    }

    @Test
    public void should_return_zip_as_byte_array() throws Exception {
        final byte[] content = createStyleFiles("bonita.css");
        final ThemeArchive themeArchive = new ThemeArchive(content);

        assertThat(themeArchive.asByteArray()).isEqualTo(content);
    }

    private byte[] createStyleFiles(String... fileNames) throws IOException {
        Map<String, byte[]> lessFiles = new HashMap<String, byte[]>();
        for (String fileName : fileNames) {
            lessFiles.put(fileName, "body {}".getBytes());
        }
        return IOUtil.zip(lessFiles);
    }
}
