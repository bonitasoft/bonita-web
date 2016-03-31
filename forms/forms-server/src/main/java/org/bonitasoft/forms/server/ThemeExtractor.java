package org.bonitasoft.forms.server;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.themes.CompilableFile;
import org.bonitasoft.console.common.server.themes.ThemeArchive;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.ThemeAPI;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.theme.Theme;
import org.bonitasoft.engine.theme.ThemeType;

public class ThemeExtractor implements Serializable {

    public ThemeExtractor() {
    }

    public void retrieveAndExtractCurrentPortalTheme(File themeFolder, APISession apiSession)
            throws ServletException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, IOException {
        final File portalThemeFolder = new File(themeFolder, HomepageServlet.PORTAL_THEME_NAME);
        final File timestampFile = new File(portalThemeFolder, HomepageServlet.LASTUPDATE_FILENAME);
        final long lastUpdateTimestamp = getThemeLastUpdateDateFromEngine(apiSession);
        if (portalThemeFolder.exists() && timestampFile.exists()) {
            final String timestampString = FileUtils.readFileToString(timestampFile);
            final long timestamp = Long.parseLong(timestampString);
            if (lastUpdateTimestamp != timestamp) {
                updateThemeFromEngine(new ThemeArchive(), apiSession, portalThemeFolder, timestampFile, lastUpdateTimestamp);
            }
        } else {
            updateThemeFromEngine(new ThemeArchive(), apiSession, portalThemeFolder, timestampFile, lastUpdateTimestamp);
        }
    }

    protected long getThemeLastUpdateDateFromEngine(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        final ThemeAPI themeAPI = getThemeAPI(apiSession);
        return themeAPI.getLastUpdateDate(ThemeType.PORTAL).getTime();
    }

    protected void updateThemeFromEngine(ThemeArchive themeArchive, final APISession apiSession, final File portalThemeDirectory, final File timestampFile,
            final long lastUpdateTimestamp)
                    throws BonitaHomeNotSetException,
                    ServerAPIException, UnknownAPITypeException, IOException {
        final Theme theme = getThemeAPI(apiSession).getCurrentTheme(ThemeType.PORTAL);
        themeArchive.setZippedTheme(theme.getContent());
        themeArchive.extract(portalThemeDirectory)
                .compile(CompilableFile.ALWAYS_COMPILED_FILES)
                .add("bonita.css", theme.getCssContent());
        FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
    }

    protected ThemeAPI getThemeAPI(APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getThemeAPI(apiSession);
    }

}
