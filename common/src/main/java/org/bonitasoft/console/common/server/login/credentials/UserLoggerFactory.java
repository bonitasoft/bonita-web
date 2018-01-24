/**
 * Copyright (C) 2018 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.credentials;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.properties.ServerProperties;

/**
 * @author Anthony Birembaut
 */
public class UserLoggerFactory {

    private static final Logger LOGGER = Logger.getLogger(UserLoggerFactory.class.getName());
    
    private static final String USERLOGGER_PROPERTY_NAME = "auth.UserLogger";

    public static UserLogger getUserLogger() {
        ServerProperties serverProperties = ServerProperties.getInstance();
        String userLoggerClassName = serverProperties.getValue(USERLOGGER_PROPERTY_NAME);
        UserLogger userLogger;
        if (userLoggerClassName == null || userLoggerClassName.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "auth.UserLogger is undefined. Using default implementation : " + UserLogger.class.getName());
            }
            userLogger = new UserLogger();
        } else {
            try {
                userLogger = (UserLogger) Class.forName(userLoggerClassName).newInstance();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "The UserLogger specified " + userLoggerClassName + " could not be instantiated! Using default implementation : " + UserLogger.class.getName(), e);
                userLogger = new UserLogger();
            }
        }
        return userLogger;
    }
}
