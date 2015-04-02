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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

/**
 * This class provide access to the resources contained in the custom page zip
 *
 * @author Anthony Birembaut
 *
 */
public class PageResourceProvider {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(PageResourceProvider.class.getName());

    private static final String VERSION_FILENAME = "VERSION";

    protected final static String THEME_RESOURCE_SERVLET_NAME = "themeResource";

    protected final static String PORTAL_THEME_NAME = "portal";

    protected final static String BONITA_THEME_CSS_FILENAME = "bonita.css";

    protected static String productVersion;

    static {
        final InputStream versionStream = PageResourceProvider.class.getClassLoader().getResourceAsStream(VERSION_FILENAME);
                    if (versionStream != null) {
                        try {
                            productVersion = IOUtils.toString(versionStream);
                        } catch (final Exception e) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to read the file " + VERSION_FILENAME, e);
                            }
                            productVersion = "";
                        } finally {
                            try {
                                versionStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Unable to close the input stream for file " + VERSION_FILENAME, e);
                    }
                }
            }
        } else {
            productVersion = "";
        }
    }

    /**
     * product version param
     */
    protected final static String VERSION_PARAM = "v";

    /**
     * tenant request parameter
     */
    protected final static String TENANT_PARAM = "tenant";

    /**
     * file name
     */
    protected final static String LOCATION_PARAM = "location";

    /**
     * theme name : the page folder's name
     */
    protected final static String PAGE_PARAM = "page";

    /**
     * theme name : the theme folder's name
     */
    protected final static String THEME_PARAM = "theme";

    protected long tenantId;

    protected String pageName;

    protected File pageDirectory;

    private final File pageTempDirectory;

    private ClassLoader resourceClassLoader;

    private final File pageTempFile;

    protected PageResourceProvider(final String pageName, final long tenantId) {
        this.tenantId = tenantId;
        this.pageName = pageName;
        pageDirectory = buildPageDirectory(pageName, tenantId);
        pageTempDirectory = buildPageTempDirectory(pageName, tenantId);
        pageTempFile = buildPageTempFile(pageName, tenantId);
    }

    protected File buildPageTempDirectory(final String pageName, final long tenantId) {
        return new File(WebBonitaConstantsUtils.getInstance(tenantId).getTempFolder(), pageName);
    }

    protected File buildPageTempFile(final String pageName, final long tenantId) {
        return new File(WebBonitaConstantsUtils.getInstance(tenantId).getTempFolder(), pageName + ".zip");
    }

    protected File buildPageDirectory(final String pageName, final long tenantId) {
        return new File(WebBonitaConstantsUtils.getInstance(tenantId).getPagesFolder(), pageName);
    }

    public InputStream getResourceAsStream(final String resourceName) throws FileNotFoundException {
        return new FileInputStream(getResourceAsFile(resourceName));
    }

    public File getResourceAsFile(final String resourceName) {
        return new File(pageDirectory, resourceName);
    }

    public String getResourceURL(final String resourceName) {
        return new StringBuilder(resourceName).append("?").append(TENANT_PARAM).append("=").append(tenantId).append("&").append(VERSION_PARAM).append("=")
                .append(productVersion).toString();
    }

    public String getBonitaThemeCSSURL() {
        return new StringBuilder(THEME_RESOURCE_SERVLET_NAME).append("?").append(TENANT_PARAM).append("=").append(tenantId).append("&").append(THEME_PARAM)
                .append("=").append(PORTAL_THEME_NAME).append("&").append(LOCATION_PARAM).append("=").append(BONITA_THEME_CSS_FILENAME).append("&")
                .append(VERSION_PARAM).append("=").append(productVersion).toString();
    }

    public File getPageDirectory() {
        return pageDirectory;
    }

    protected File getTempPageFile() {
        return pageTempFile;
    }

    protected File getTempPageDirectory() {
        return pageTempDirectory;
    }

    void setResourceClassLoader(final ClassLoader resourceClassLoader) {
        this.resourceClassLoader = resourceClassLoader;
    }

    public ResourceBundle getResourceBundle(final String name, final Locale locale) {
        return ResourceBundle.getBundle(name, locale, resourceClassLoader);
    }

}
