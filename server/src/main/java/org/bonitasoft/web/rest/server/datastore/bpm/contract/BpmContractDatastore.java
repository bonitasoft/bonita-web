/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.contract;

import java.io.IOException;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.model.bpm.contract.BpmContractItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.json.JacksonSerializer;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author Laurent Leseigneur
 */
public class BpmContractDatastore extends CommonDatastore<BpmContractItem, ContractDefinition> implements
DatastoreHasGet<BpmContractItem>

{

    private static final String NULL_AS_STRING = "null";

    public BpmContractDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    protected BpmContractItem convertEngineToConsoleItem(final ContractDefinition engineItem) {
        final BpmContractItem result = new BpmContractItem();

        result.setInputs(toJson(engineItem.getInputs()));
        result.setRules(toJson(engineItem.getRules()));
        return result;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @throws InvalidSessionException
     * @throws BonitaHomeNotSetException
     * @throws ServerAPIException
     * @throws UnknownAPITypeException
     */
    protected ProcessAPI getProcessAPI() throws InvalidSessionException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getProcessAPI(getEngineSession());
    }

    public String toJson(final Object object)
    {
        final JacksonSerializer jacksonSerializer = new JacksonSerializer();
        try {
            return jacksonSerializer.serialize(object);
        } catch (final JsonGenerationException e) {
            return NULL_AS_STRING;
        } catch (final JsonMappingException e) {
            return NULL_AS_STRING;
        } catch (final IOException e) {
            return NULL_AS_STRING;
        }

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CRUDS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public BpmContractItem get(final APIID id) {
        try {
            return convertEngineToConsoleItem(getProcessAPI().getUserTaskContract(id.toLong()));
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

}
