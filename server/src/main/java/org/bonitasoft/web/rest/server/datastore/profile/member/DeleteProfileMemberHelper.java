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
package org.bonitasoft.web.rest.server.datastore.profile.member;

import java.util.List;

import org.bonitasoft.web.rest.server.engineclient.ProfileMemberEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 * 
 */
public class DeleteProfileMemberHelper implements DatastoreHasDelete {

    private ProfileMemberEngineClient engineClient;

    public DeleteProfileMemberHelper(ProfileMemberEngineClient engineClient) {
        this.engineClient = engineClient;
    }

    @Override
    public void delete(List<APIID> ids) {
        for (APIID id : ids) {
            delete(id);
        }
    }

    private void delete(APIID id) {
        engineClient.deleteProfileMember(id.toLong());
    }

}
