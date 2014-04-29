/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.common.server.themes;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bonitasoft.engine.api.ThemeAPI;
import org.bonitasoft.engine.theme.Theme;
import org.bonitasoft.engine.theme.ThemeType;


/**
 * @author Fabio Lombardi
 *
 */
public class ThemeDatastore {

    private ThemeAPI themeApi;

    private ThemeManager themeManager;

    /**
     * Default Constructor.
     * 
     * @param themeApi
     * @param themeManager
     */
    public ThemeDatastore(final ThemeAPI themeApi, final ThemeManager themeManager) {
        this.themeApi = themeApi;
        this.themeManager = themeManager;
    }

    /**
     * @throws IOException
     * @throws FileNotFoundException
     * 
     */
    public void updateCurrentThemeFromEngine() throws FileNotFoundException, IOException {

        final Theme theme = themeApi.getCurrentTheme(ThemeType.PORTAL);
        themeManager.applyAlreadyCompiledTheme(theme.getContent(), theme.getCssContent());

    }

}
