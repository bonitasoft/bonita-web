/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.form;

import java.util.List;

import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.form.mapping.FormMapping;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

/**
 * REST resource to operate on Form Mapping.
 *
 * @author Anthony Birembaut
 */
public class FormMappingResource extends CommonResource {

    public static final String ID_PARAM_NAME = "id";

    @Get("json")
    public List<FormMapping> searchFormMappings() {
        try {
            return runEngineSearch(buildSearchOptions());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected List<FormMapping> runEngineSearch(final SearchOptions searchOptions) throws SearchException {
        return getEngineProcessConfigurationAPI().searchFormMappings(searchOptions).getResult();
    }

    @Put("json")
    public void updateFormMapping(final FormMapping formMapping) {
        final String mappingIdAsString = getAttribute(ID_PARAM_NAME);
        if (mappingIdAsString == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing form mapping Id in the URL");
        }
        try {
            final long mappingId = Long.parseLong(mappingIdAsString);
            getEngineProcessConfigurationAPI().updateFormMapping(mappingId, formMapping.getForm(), formMapping.isExternal());
        } catch (final NotFoundException e) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Cannot find form mapping", e);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public String getAttribute(final String attributeName) {
        return super.getAttribute(attributeName);
    }

}
