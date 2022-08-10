/**
 * Copyright (C) 2018 Bonitasoft S.A.
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ServerProperties {

    private static final String FILENAME = "server.properties";

    private static ServerProperties instance = null;

    private static Properties serverProperties = new Properties();
    
    private static Logger LOGGER = LoggerFactory.getLogger(ServerProperties.class.getName());

    private ServerProperties() {
        InputStream inputStream = getClass().getResourceAsStream(FILENAME);
        if (inputStream != null) {
            try {
                serverProperties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace( "No " + FILENAME + " file found.");
            }
        }
    }

    public static synchronized ServerProperties getInstance() {
        if (instance == null)
            instance = new ServerProperties();
        return instance;
    }

    public String getValue(final String propertyName) {
        return serverProperties.getProperty(propertyName);
    }

}
