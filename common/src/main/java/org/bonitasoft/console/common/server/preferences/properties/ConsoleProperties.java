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
package org.bonitasoft.console.common.server.preferences.properties;

import java.util.Properties;

/**
 * @author Yang zhiheng
 */
public class ConsoleProperties {

    /**
     * Document max size
     */
    private static final String ATTACHMENT_MAX_SIZE = "form.attachment.max.size";

    /**
     * Custom page debug mode
     */
    private static final String CUSTOM_PAGE_DEBUG = "custom.page.debug";

    private static final String PROPERTIES_FILE = "console-config.properties";

    private long tenantId;

    ConsoleProperties(long tenantId) {
        this.tenantId = tenantId;
    }

    public Properties getProperties() {
        return ConfigurationFilesManager.getInstance().getTenantProperties(PROPERTIES_FILE, tenantId);
    }

    public String getProperty(final String propertyName) {
        return getProperties().getProperty(propertyName);
    }

    public String getProperty(final String propertyName, final String defaultValue) {
        return getProperties().getProperty(propertyName, defaultValue);
    }

    public long getMaxSize() {
        final String maxSize = this.getProperty(ATTACHMENT_MAX_SIZE);
        if (maxSize != null) {
            return Long.valueOf(maxSize);
        }
        return 15;
    }

    public boolean isPageInDebugMode() {
        final String debugMode = this.getProperty(CUSTOM_PAGE_DEBUG);
        return Boolean.parseBoolean(debugMode);
    }
}
