package org.bonitasoft.console.common.server.themes;

import java.io.File;
import java.io.FileReader;
import java.net.URI;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ThemeManagerTest {

    @Test
    public void should_compileLess_generate_css() throws Exception {

        final ThemeManager themeManager = new ThemeManager();
        final URI themeFolderURI = getClass().getResource("/theme").toURI();
        final File themeFolder = new File(themeFolderURI);
        final File cssFile = themeManager.compileLess(themeFolder, "main.less", "bonita.css");

        Assert.assertTrue("The CSS file wasn't created", cssFile.exists());
        final FileReader fileReader = new FileReader(cssFile);
        try {
            Assert.assertTrue("The CSS file created is empty", fileReader.read() > -1);
        } finally {
            fileReader.close();
        }
    }
}
