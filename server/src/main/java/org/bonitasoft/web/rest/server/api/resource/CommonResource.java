/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.web.rest.server.api.resource;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.json.JSONObject;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.ServerResource;

/**
 * @author Emmanuel Duchastenier
 */
public class CommonResource extends ServerResource {

    private APISession sessionSingleton = null;

    protected String toJson(final Object p) {
        return new JSONObject(p).toString();
    }

    /**
     * Get the tenant session to access the engine APIs
     */
    protected APISession getEngineSession() {
        if (sessionSingleton == null) {
            sessionSingleton = (APISession) ServletUtils.getRequest(getRequest()).getSession().getAttribute("apiSession");
        }
        return sessionSingleton;
    }

    protected ProcessAPI getEngineProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected ProfileAPI getEngineProfileAPI() {
        try {
            return TenantAPIAccessor.getProfileAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected int getSearchPageNumber() {
        return getIntegerParameter(APIServletCall.PARAMETER_PAGE);
    }

    protected int getSearchPageSize() {
        return getIntegerParameter(APIServletCall.PARAMETER_LIMIT);
    }

    protected String getSearchTerm() {
        return getParameter(APIServletCall.PARAMETER_SEARCH);
    }

    protected Integer getIntegerParameter(final String parameterName) {
        final String parameterValue = getParameter(parameterName);
        if (parameterValue != null) {
            return Integer.parseInt(parameterValue);
        }
        return null;
    }

    protected Long getLongParameter(final String parameterName) {
        final String parameterValue = getParameter(parameterName);
        if (parameterValue != null) {
            return Long.parseLong(parameterValue);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getMandatoryParameter(final Class<T> parameterType, final String parameterName) {
        final String parameter = getParameter(parameterName);
        verifyNotNullParameter(parameter, parameterName);
        return (T) parameter;
    }

    protected String getParameter(final String parameterName) {
        return ServletUtils.getRequest(getRequest()).getParameter(parameterName);
    }

    protected void verifyNotNullParameter(final Object parameter, final String parameterName) throws APIException {
        if (parameter == null) {
            throw new APIException("Parameter " + parameterName + " is mandatory.");
        }
    }
}
