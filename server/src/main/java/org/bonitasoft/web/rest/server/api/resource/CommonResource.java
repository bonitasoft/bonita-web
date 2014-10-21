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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.ServerResource;

/**
 * @author Emmanuel Duchastenier
 */
public class CommonResource extends ServerResource {

    /**
     * Json format for dates
     */
    //    protected static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private APISession sessionSingleton = null;

    /**
     * Get the tenant session to access the engine APIs
     */
    protected APISession getEngineSession() {
        if (sessionSingleton == null) {
            final HttpSession session = getHttpSession();
            sessionSingleton = (APISession) session.getAttribute("apiSession");
        }
        return sessionSingleton;
    }

    public HttpSession getHttpSession() {
        return getHttpRequest().getSession();
    }

    public HttpServletRequest getHttpRequest() {
        return ServletUtils.getRequest(getRequest());
    }

    public ProcessAPI getEngineProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected Map<String, String> getSearchFilters() {
        return parseFilters(getParameterAsList(APIServletCall.PARAMETER_FILTER));
    }

    /**
     * Builds a map where keys are Engine constants defining filter keys, and values are values corresponding to those keys.
     *
     * @param parameters The filters passed as string according to the form ["key1=value1", "key2=value2"].
     * @return a map of the form: [key1: value1, key2: value2].
     */
    protected Map<String, String> parseFilters(final List<String> parameters) {
        if (parameters == null) {
            return null;
        }
        final Map<String, String> results = new HashMap<String, String>();
        for (final String parameter : parameters) {
            final String[] split = parameter.split("=");
            if (split.length < 2) {
                results.put(split[0], null);
            } else {
                results.put(split[0], split[1]);
            }
        }
        return results;
    }

    protected String getSearchOrder() {
        return getParameter(APIServletCall.PARAMETER_ORDER, false);
    }

    protected int getSearchPageNumber() {
        return getIntegerParameter(APIServletCall.PARAMETER_PAGE, false);
    }

    protected int getSearchPageSize() {
        return getIntegerParameter(APIServletCall.PARAMETER_LIMIT, false);
    }

    protected String getSearchTerm() {
        return getParameter(APIServletCall.PARAMETER_SEARCH, false);
    }

    public Integer getIntegerParameter(final String parameterName, final boolean mandatory) {
        final String parameterValue = getParameter(parameterName, mandatory);
        if (parameterValue != null) {
            return Integer.parseInt(parameterValue);
        }
        return null;
    }

    public Long getLongParameter(final String parameterName, final boolean mandatory) {
        final String parameterValue = getParameter(parameterName, mandatory);
        if (parameterValue != null) {
            return Long.parseLong(parameterValue);
        }
        return null;
    }

    public String getParameter(final String parameterName, final boolean mandatory) {
        final String parameter = getRequestParameter(parameterName);
        if (mandatory) {
            verifyNotNullParameter(parameter, parameterName);
        }
        return parameter;
    }

    protected String getRequestParameter(final String parameterName) {
        return getHttpRequest().getParameter(parameterName);
    }

    protected void verifyNotNullParameter(final Object parameter, final String parameterName) throws APIException {
        if (parameter == null) {
            throw new APIException("Parameter " + parameterName + " is mandatory.");
        }
    }

    /**
     * Get a list of parameter values by name.
     *
     * @param name
     *        The name of the parameter (case sensitive).
     * @return This method returns the values of a parameter as a list of String.
     */
    public List<String> getParameterAsList(final String name) {
        final String[] parameterValues = getHttpRequest().getParameterValues(name);
        if (parameterValues != null && parameterValues.length > 0) {
            return Arrays.asList(parameterValues);
        }
        return null;
    }

    public SearchOptions buildSearchOptions() {
        return new SearchOptionsCreator(getSearchPageNumber(), getSearchPageSize(), getSearchTerm(), new Sorts(
                getSearchOrder()), new Filters(getSearchFilters())).create();
    }

    @Override
    protected void doCatch(final Throwable throwable) {
        final Throwable t = throwable.getCause() != null ? throwable.getCause() : throwable;
        // Don't need to log the wrapping exception, the cause itself is more interesting:
        super.doCatch(t);

        getLogger().log(Level.SEVERE, "*** problem on " + getClass().getName() + " rest resource: " + t.getMessage());
        getResponse().setStatus(getStatus(), "Cannot execute REST resource " + getClass().getName() + " rest resource: " + t.getMessage());
    }

}
