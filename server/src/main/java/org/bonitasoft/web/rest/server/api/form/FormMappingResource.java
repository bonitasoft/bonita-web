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

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.form.FormMapping;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.filter.FormMappingTypeCreator;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

/**
 * REST resource to operate on Form Mapping.
 *
 * @author Anthony Birembaut
 */
public class FormMappingResource extends CommonResource {

    protected final ProcessAPI processAPI;

    public FormMappingResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
        //Prevent Restlet from setting the status to 404
        //HTTP conditional headers are not supported when setting the entity manually in the response (fix BS-18149)
        setConditional(false);
    }

    @Get("json")
    public void searchFormMapping() throws ResourceException {
        try {
            final SearchResult<FormMapping> searchResult = processAPI.searchFormMappings(buildSearchOptions());
            List<FormMapping> result = searchResult.getResult();
            List<FormMappingItem> resultConverted = convertMapping(result);
            Representation representation = getConverterService().toRepresentation(resultConverted, MediaType.APPLICATION_JSON);
            representation.setCharacterSet(CharacterSet.UTF_8);
            getResponse().setEntity(representation);
            setContentRange(searchResult);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private List<FormMappingItem> convertMapping(List<FormMapping> result) {
        List<FormMappingItem> convertedResult = new ArrayList<>();
        for (FormMapping item: result) {
            convertedResult.add(new FormMappingItem(item));
        }
        return convertedResult;
    }

    @Override
    protected Filters buildFilters() {
        return new Filters(getSearchFilters(), new FormMappingTypeCreator());
    }

}
