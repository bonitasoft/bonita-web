/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.page;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.page.PageNotFoundException;

/**
 * This interface provide access to the resources contained in the custom page zip
 * 
 * @deprecated Use org.bonitasoft.web.extension.page.PageResourceProvider instead
 */
@Deprecated
public interface PageResourceProvider {

    InputStream getResourceAsStream(final String resourceName) throws FileNotFoundException;

    File getResourceAsFile(final String resourceName);

    String getResourceURL(final String resourceName);

    String getBonitaThemeCSSURL();

    File getPageDirectory();

    ResourceBundle getResourceBundle(final String name, final Locale locale);

    String getPageName();

    String getFullPageName();

    Page getPage(final PageAPI pageAPI) throws PageNotFoundException;

}
