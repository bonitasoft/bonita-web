/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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

public class PlatformPreferencesProperties extends ConfigurationFile {

    /**
     * Default name of the preferences file
     */
    public static final String PROPERTIES_FILENAME = "platform-preferences.properties";

    /**
     * Platform properties instance
     */
    private static volatile PlatformPreferencesProperties instance = new PlatformPreferencesProperties();

    /**
     * @return the PlatformProperties instance
     */
    protected static PlatformPreferencesProperties getInstance() {
        return instance;
    }

    PlatformPreferencesProperties() {
        super(PROPERTIES_FILENAME);
    }
}
