/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.bdm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.command.CommandExecutionException;
import org.bonitasoft.engine.command.CommandNotFoundException;
import org.bonitasoft.engine.command.CommandParameterizationException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.restlet.resource.Get;

/**
 * @author Laurent Leseigneur
 */
public class BusinessDataQueryResource extends CommonResource {

    public static final String COMMAND_NAME = "getBusinessDataByQueryCommand";

    private final CommandAPI commandAPI;

    public BusinessDataQueryResource(final CommandAPI commandAPI) {
        this.commandAPI = commandAPI;
    }

    @Get("json")
    public Serializable getProcessBusinessDataQuery() throws CommandNotFoundException, CommandParameterizationException, CommandExecutionException {
        final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        final Integer searchPageNumber = getSearchPageNumber();
        final Integer searchPageSize = getSearchPageSize();
        parameters.put("queryName", getQueryParameter(true));
        parameters.put("queryParameters", (Serializable) getSearchFilters());
        parameters.put("entityClassName", getPathParam("className"));
        parameters.put("startIndex", searchPageNumber * searchPageSize);
        parameters.put("maxResults", searchPageSize);
        parameters.put("businessDataURIPattern", BusinessDataFieldValue.URI_PATTERN);
        return commandAPI.execute(COMMAND_NAME, parameters);
    }

}
