package org.bonitasoft.console.common.server.themes;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

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
        ThemeArchive themeArchive = new ThemeArchive(createContent());

        themeArchive.extract(themeDirectory);

        assertThat(themeDirectory.list()).containsOnly("bonita.css");
    }

    @Test
    public void should_add_given_file_to_theme_directory() throws Exception {
        ThemeArchive themeArchive = new ThemeArchive(empty());

        themeArchive.extract(themeDirectory).add("bonita.css", "body {}".getBytes());

        assertThat(themeDirectory.list()).containsOnly("bonita.css");
    }

    @Test
    public void should_replace_old_directory_with_zip_content() throws Exception {
        themeDirectory.mkdir();
        new File(themeDirectory, "old-style.css").createNewFile();
        ThemeArchive themeArchive = new ThemeArchive(createContent());

        themeArchive.extract(themeDirectory);

        assertThat(themeDirectory.list()).containsOnly("bonita.css");
    }

    @Test
    public void should_return_zip_as_byte_array() throws Exception {
        byte[] content = createContent();
        ThemeArchive themeArchive = new ThemeArchive(content);

        assertThat(themeArchive.asByteArray()).isEqualTo(content);
    }

    private byte[] createContent() throws IOException {
        return IOUtil.zip(singletonMap("bonita.css", "body {}".getBytes()));
    }

    private byte[] empty() throws IOException {
        return IOUtil.zip(Collections.<String, byte[]>emptyMap());
    }
}