/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.privileges.datastore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Anthony Birembaut
 * 
 */
@Deprecated
public class PrivilegesDatastoreImpl implements PrivilegesDatastore {

    /**
     * Instance attribute
     */
    private static PrivilegesDatastore INSTANCE = null;

    /**
     * instance uuid
     */
    public static final String INSTANCE_UUID = "instance";

    /**
     * task uuid
     */
    public static final String TASK_UUID = "task";

    /**
     * process definition uuid
     */
    public static final String PROCESS_UUID = "process";

    private static final String USER_ID_KEY = "USER_ID_KEY";

    /**
     * @return the PrivilegesAPIImpl instance
     */
    public static synchronized PrivilegesDatastore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrivilegesDatastoreImpl();
        }
        return INSTANCE;
    }

    /**
     * Private contructor to prevent instantiation
     */
    protected PrivilegesDatastoreImpl() {
    }


    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Set<Long>> getProcessActors(final APISession aAPISession) throws BonitaException {
        Map<Long, Set<Long>> result = null;
        try {
            final CommandAPI commandAPI = TenantAPIAccessor.getCommandAPI(aAPISession);
            final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
            parameters.put(USER_ID_KEY, aAPISession.getUserId());
            result = (Map<Long, Set<Long>>) commandAPI.execute("getActorIdsForUserIdIncludingTeam", parameters);
        } catch (final Exception e) {
            final String msg = "Encountered error while executing this command:";
            throw new BonitaException(msg);
        }
        return result;
    }

}
