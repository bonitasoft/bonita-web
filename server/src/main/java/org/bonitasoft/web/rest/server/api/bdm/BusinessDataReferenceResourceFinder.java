/** Copyright (C) 2015 Bonitasoft S.A.
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
package org.bonitasoft.web.rest.server.api.bdm;

import java.io.Serializable;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.business.data.BusinessDataReference;
import org.bonitasoft.web.rest.server.ResourceFinder;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

/**
 * @author Baptiste Mesta
 */
public class BusinessDataReferenceResourceFinder extends ResourceFinder {

    @Override
    public ServerResource create(final Request request, final Response response) {
        final BusinessDataAPI bdmAPI = getBdmAPI(request);
        return new BusinessDataReferenceResource(bdmAPI);
    }

    @Override
    public boolean handlesResource(Serializable object) {
        return object instanceof BusinessDataReference;
    }

    @Override
    public Serializable toClientObject(Serializable object) {
        return BusinessDataReferenceResource.toClient((BusinessDataReference) object);
    }

}
