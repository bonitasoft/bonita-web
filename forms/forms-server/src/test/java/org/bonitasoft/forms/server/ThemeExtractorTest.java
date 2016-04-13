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

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Date;

import org.bonitasoft.console.common.server.themes.CompilableFile;
import org.bonitasoft.console.common.server.themes.ThemeArchive;
import org.bonitasoft.engine.api.ThemeAPI;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.theme.Theme;
import org.bonitasoft.engine.theme.ThemeType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Emmanuel Duchastenier
 */
@RunWith(MockitoJUnitRunner.class)
public class ThemeExtractorTest {

    @Mock
    APISession session;

    @Mock
    ThemeAPI themeAPI;

    @Mock
    ThemeArchive themeArchive;

    @Mock
    ThemeArchive.ThemeModifier themeModifier;

    @Spy
    @InjectMocks
    ThemeExtractor themeExtractor;

    @Rule
    public TemporaryFolder tempFolderRule = new TemporaryFolder();

    @Before
    public void init() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        doReturn(themeAPI).when(themeExtractor).getThemeAPI(session);
        doReturn(new Date()).when(themeAPI).getLastUpdateDate(ThemeType.PORTAL);
    }

    @Test
    public void getThemeLastUpdateDateFromEngine_should_call_getLastUpdateDate_on_Engine() throws Exception {
        themeExtractor.getThemeLastUpdateDateFromEngine(session, ThemeType.PORTAL);
        verify(themeAPI).getLastUpdateDate(ThemeType.PORTAL);
    }

    @Test
    public void updateThemeFromEngine_should_retrieve_theme_from_Engine_and_compile() throws Exception {
        doReturn(mock(Theme.class)).when(themeAPI).getCurrentTheme(ThemeType.PORTAL);
        doReturn(themeModifier).when(themeArchive).extract(any(File.class));
        doReturn(themeModifier).when(themeModifier).compile(CompilableFile.ALWAYS_COMPILED_FILES);
        doReturn(themeModifier).when(themeModifier).add(anyString(), any(byte[].class));

        final File folder = tempFolderRule.newFolder();
        final File lastUpdateFile = tempFolderRule.newFile();

        themeExtractor.updateThemeFromEngine(themeArchive, session, folder, lastUpdateFile, new Date().getTime(), ThemeType.PORTAL);

        verify(themeAPI).getCurrentTheme(ThemeType.PORTAL);
    }

    @Test
    public void retrieveAndExtractCurrentTheme_should_call_updateThemeFromEngine() throws Exception {
        final long lastUpdateTimestamp = 1654L;
        doReturn(lastUpdateTimestamp).when(themeExtractor).getThemeLastUpdateDateFromEngine(session, ThemeType.PORTAL);
        doNothing().when(themeExtractor).updateThemeFromEngine(any(ThemeArchive.class), eq(session), any(File.class), any(File.class), eq(lastUpdateTimestamp),
                eq(ThemeType.PORTAL));

        themeExtractor.retrieveAndExtractCurrentTheme(new File("dummy"), session, ThemeType.PORTAL);

        verify(themeExtractor).updateThemeFromEngine(any(ThemeArchive.class), eq(session), any(File.class), any(File.class), eq(lastUpdateTimestamp),
                eq(ThemeType.PORTAL));
    }

    @Test
    public void retrieveAndExtractCurrentTheme_for_mobile_should_not_call_extractPortalSpecificTheme() throws Exception {
        final long lastUpdateTimestamp = 1654L;
        doReturn(lastUpdateTimestamp).when(themeExtractor).getThemeLastUpdateDateFromEngine(session, ThemeType.MOBILE);
        doNothing().when(themeExtractor).updateThemeFromEngine(any(ThemeArchive.class), eq(session), any(File.class), any(File.class), eq(lastUpdateTimestamp),
                eq(ThemeType.MOBILE));

        themeExtractor.retrieveAndExtractCurrentTheme(new File("dummy"), session, ThemeType.MOBILE);

        verify(themeExtractor).updateThemeFromEngine(any(ThemeArchive.class), eq(session), any(File.class), any(File.class), eq(lastUpdateTimestamp),
                eq(ThemeType.MOBILE));
        verify(themeExtractor, times(0)).extractPortalSpecificTheme(any(ThemeArchive.ThemeModifier.class), any(Theme.class));
    }

}
