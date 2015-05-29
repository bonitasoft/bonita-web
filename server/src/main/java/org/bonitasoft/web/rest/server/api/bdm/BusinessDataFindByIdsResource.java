/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
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
 * @author Matthieu Chaffotte
 */
public class BusinessDataFindByIdsResource extends CommonResource {

    private final CommandAPI commandAPI;

    public BusinessDataFindByIdsResource(final CommandAPI commandAPI) {
        this.commandAPI = commandAPI;
    }

    @Get("json")
    public String getBusinessData() throws CommandNotFoundException, CommandExecutionException, CommandParameterizationException {
        final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        parameters.put("entityClassName", getPathParam("className"));
        parameters.put("businessDataIds", (Serializable) getParameterAsLongList("ids"));
        parameters.put("businessDataURIPattern", BusinessDataFieldValue.URI_PATTERN);
    	return (String) commandAPI.execute("getBusinessDataByIds", parameters);
    }

}
