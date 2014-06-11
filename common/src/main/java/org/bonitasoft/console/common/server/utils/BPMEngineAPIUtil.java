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

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.command.CommandExecutionException;
import org.bonitasoft.engine.command.CommandNotFoundException;
import org.bonitasoft.engine.command.CommandParameterizationException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;

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
    private static Logger LOGGER = Logger.getLogger(BPMEngineAPIUtil.class.getName());

    /**
     * Get the engine command API
     *
     * @param session
     *            API session
     * @return an instance of {@link CommandAPI}
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    public CommandAPI getCommandAPI(final APISession session) throws BPMEngineException, InvalidSessionException {
        try {
            return TenantAPIAccessor.getCommandAPI(session);
        } catch (final BonitaHomeNotSetException e) {
            final String message = "Bonita home system variable is not defined";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final UnknownAPITypeException e) {
            final String message = "The engine API Implementation is unknown.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final ServerAPIException e) {
            final String message = "The engine client was not able to communicate with the engine server.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        }
    }

    /**
     * Get the engine process API
     *
     * @param session
     *            API session
     * @return an instance of {@link ProcessAPI}
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    public ProcessAPI getProcessAPI(final APISession session) throws BPMEngineException, InvalidSessionException {
        try {
            return TenantAPIAccessor.getProcessAPI(session);
        } catch (final BonitaHomeNotSetException e) {
            final String message = "Bonita home system variable is not defined";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final UnknownAPITypeException e) {
            final String message = "The engine API Implementation is unknown.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final ServerAPIException e) {
            final String message = "The engine client was not able to communicate with the engine server.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        }
    }

    /**
     * Get the engine identity API
     *
     * @param session
     *            API session
     * @return an instance of {@link ProcessAPI}
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    public IdentityAPI getIdentityAPI(final APISession session) throws BPMEngineException, InvalidSessionException {
        try {
            return TenantAPIAccessor.getIdentityAPI(session);
        } catch (final BonitaHomeNotSetException e) {
            final String message = "Bonita home system variable is not defined";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final UnknownAPITypeException e) {
            final String message = "The engine API Implementation is unknown.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final ServerAPIException e) {
            final String message = "The engine client was not able to communicate with the engine server.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        }
    }

    /**
     * Get the engine profile API
     *
     * @param session
     *        API session
     * @return an instance of {@link ProfileAPI}
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    public ProfileAPI getProfileAPI(final APISession session) throws BPMEngineException, InvalidSessionException {
        try {
            return TenantAPIAccessor.getProfileAPI(session);
        } catch (final BonitaHomeNotSetException e) {
            final String message = "Bonita home system variable is not defined";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final UnknownAPITypeException e) {
            final String message = "The engine API Implementation is unknown.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final ServerAPIException e) {
            final String message = "The engine client was not able to communicate with the engine server.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        }
    }

    public Serializable executeCommand(final CommandAPI commandAPI, final String name, final Map<String, Serializable> parameters) throws BPMEngineException,
            InvalidSessionException {
        try {
            return commandAPI.execute(name, parameters);
        } catch (final CommandNotFoundException e) {
            final String message = "The command " + name + " could not be found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final CommandParameterizationException e) {
            final String message = "The command " + name + " expect different parameters types.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        } catch (final CommandExecutionException e) {
            final String message = "A error occured while executing the command.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new BPMEngineException(message);
        }
    }

}