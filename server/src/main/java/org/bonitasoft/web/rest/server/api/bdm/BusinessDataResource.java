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
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.command.CommandExecutionException;
import org.bonitasoft.engine.command.CommandNotFoundException;
import org.bonitasoft.engine.command.CommandParameterizationException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.restlet.data.Status;
import org.restlet.resource.Get;

/**
 * @author Matthieu Chaffotte
 */
public class BusinessDataResource extends CommonResource {

    private final CommandAPI commandAPI;

    public BusinessDataResource(final CommandAPI commandAPI) {
        this.commandAPI = commandAPI;
    }

    @Get("json")
    public String getBusinessData() throws CommandNotFoundException, CommandExecutionException, CommandParameterizationException {
        final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        parameters.put("entityClassName", getPathParam("className"));
        parameters.put("businessDataId", getPathParamAsLong("id"));
        parameters.put("businessDataURIPattern", BusinessDataFieldValue.URI_PATTERN);
        final String child = getPathParam("fieldName");
        if (child != null) {
            parameters.put("businessDataChildName", child);
        }
        return (String) commandAPI.execute("getBusinessDataById", parameters);
    }

    @Override
    protected void doCatch(final Throwable throwable) {
        final DataNotFoundException dataNotFoundException = searchInCauseDataNotFoundException(throwable);
        if (dataNotFoundException != null) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, dataNotFoundException.getMessage());
        } else {
            super.doCatch(throwable);
        }
    }

    private DataNotFoundException searchInCauseDataNotFoundException(final Throwable throwable) {
        // Exception are all wrapped so we need to get cause fist
        if (throwable.getCause() != null) {
            final Throwable realException = throwable.getCause();
            if (realException instanceof CommandExecutionException) {
                final CommandExecutionException e = (CommandExecutionException) realException;
                if (e.getCause() instanceof DataNotFoundException) {
                    return (DataNotFoundException) e.getCause();
                }
            }
        }
        return null;
    }
}
