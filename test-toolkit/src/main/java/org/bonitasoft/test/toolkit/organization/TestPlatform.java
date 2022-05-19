/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.test.toolkit.organization;

import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.platform.PlatformLoginException;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestPlatform {

    // protected boolean obsolete = false;

    protected static final String PLATFORM_ADMIN = "platformAdmin";

    protected static final String PLATFORM_PASSWORD = "platform";

    private PlatformAPI platformAPI;

    public PlatformAPI getPlatformAPI() {
        if (this.platformAPI == null) {
            PlatformLoginAPI platformLoginAPI;
            try {
                platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
                final PlatformSession platformSession = platformLoginAPI.login(PLATFORM_ADMIN, PLATFORM_PASSWORD);
                this.platformAPI = PlatformAPIAccessor.getPlatformAPI(platformSession);
            } catch (final BonitaHomeNotSetException e) {
                throw new TestToolkitException("Bonita home isn't set", e);
            } catch (final ServerAPIException e) {
                throw new TestToolkitException("Failed to call server API", e);
            } catch (final UnknownAPITypeException e) {
                throw new TestToolkitException("Invalid session to create tenant", e);
            } catch (final PlatformLoginException e) {
                throw new TestToolkitException("Invalid session to create tenant", e);
            } catch (final InvalidSessionException e) {
                throw new TestToolkitException("Invalid session to create tenant", e);
            }
        }
        return this.platformAPI;
    }

    /**
     * Stop and delete platform
     */
    protected void destroy() {
        try {
            getPlatformAPI().stopNode();
        } catch (final Exception e) {
            throw new TestToolkitException("Can't stop platform", e);
        }
    }
}
