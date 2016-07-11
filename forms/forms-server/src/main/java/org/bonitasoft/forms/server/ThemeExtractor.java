/*
 * Copyright (C) 2016 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 */
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

/**
 * @author Emmanuel Duchastenier
 */
public class ThemeExtractor implements Serializable {

    public ThemeExtractor() {
    }

    public void retrieveAndExtractCurrentTheme(File themeFolder, APISession apiSession, ThemeType themeType)
            throws ServletException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, IOException {
        final File specificThemeFolder = new File(themeFolder, themeType.name().toLowerCase());
        final File timestampFile = new File(specificThemeFolder, HomepageServlet.LASTUPDATE_FILENAME);
        final long lastUpdateTimestamp = getThemeLastUpdateDateFromEngine(apiSession, themeType);
        if (specificThemeFolder.exists() && timestampFile.exists()) {
            final String timestampString = FileUtils.readFileToString(timestampFile);
            final long timestamp = Long.parseLong(timestampString);
            if (lastUpdateTimestamp != timestamp) {
                updateThemeFromEngine(new ThemeArchive(), apiSession, specificThemeFolder, timestampFile, lastUpdateTimestamp, themeType);
            }
        } else {
            updateThemeFromEngine(new ThemeArchive(), apiSession, specificThemeFolder, timestampFile, lastUpdateTimestamp, themeType);
        }
    }

    void updateThemeFromEngine(ThemeArchive themeArchive, final APISession apiSession, final File portalThemeDirectory, final File timestampFile,
            final long lastUpdateTimestamp, ThemeType themeType) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, IOException {
        final Theme theme = getThemeAPI(apiSession).getCurrentTheme(themeType);
        themeArchive.setZippedTheme(theme.getContent());
        final ThemeArchive.ThemeModifier themeModifier = themeArchive.extract(portalThemeDirectory);
        switch (themeType) {
            case PORTAL:
                extractPortalSpecificTheme(themeModifier, theme);
                break;
            case MOBILE:
                // nothing specific to do:
                break;
        }

        writeTimeStampToFile(timestampFile, lastUpdateTimestamp);
    }

    void extractPortalSpecificTheme(ThemeArchive.ThemeModifier themeModifier, Theme theme) throws IOException {
        themeModifier.compile(CompilableFile.ALWAYS_COMPILED_FILES).add("bonita.css", theme.getCssContent());
    }

    long getThemeLastUpdateDateFromEngine(final APISession apiSession, ThemeType themeType)
            throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        final ThemeAPI themeAPI = getThemeAPI(apiSession);
        return themeAPI.getLastUpdateDate(themeType).getTime();
    }

    private void writeTimeStampToFile(File timestampFile, long lastUpdateTimestamp) throws IOException {
        FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
    }

    ThemeAPI getThemeAPI(APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getThemeAPI(apiSession);
    }

}
