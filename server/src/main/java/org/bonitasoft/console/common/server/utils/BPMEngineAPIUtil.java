/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.utils;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class to work with the BPM engine API
 *
 * @author Anthony Birembaut
 *
 */
public class BPMEngineAPIUtil {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BPMEngineAPIUtil.class.getName());

    /**
     * Get the engine process API
     *
     * @param session
     *            API session
     * @return an instance of {@link ProcessAPI}
     */
    public ProcessAPI getProcessAPI(final APISession session) throws BPMEngineException, InvalidSessionException {
        try {
            return TenantAPIAccessor.getProcessAPI(session);
        } catch (final BonitaHomeNotSetException e) {
            final String message = "Bonita home system variable is not defined";
             if (LOGGER.isErrorEnabled()) {
                LOGGER.error( message, e);
            }
            throw new BPMEngineException(message);
        } catch (final UnknownAPITypeException e) {
            final String message = "The engine API Implementation is unknown.";
             if (LOGGER.isErrorEnabled()) {
                LOGGER.error( message, e);
            }
            throw new BPMEngineException(message);
        } catch (final ServerAPIException e) {
            final String message = "The engine client was not able to communicate with the engine server.";
             if (LOGGER.isErrorEnabled()) {
                LOGGER.error( message, e);
            }
            throw new BPMEngineException(message);
        }
    }

}