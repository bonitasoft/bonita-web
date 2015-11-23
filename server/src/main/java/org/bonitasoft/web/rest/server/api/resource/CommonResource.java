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

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonParseException;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.data.CharacterSet;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

/**
 * @author Emmanuel Duchastenier
 */
public class CommonResource extends ServerResource {

    private APISession sessionSingleton = null;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(CommonResource.class.getName());

    /**
     * Get the tenant session to access the engine APIs
     */
    public APISession getEngineSession() {
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

    protected Map<String, String> getSearchFilters() {
        return parseFilters(getParameterAsList(APIServletCall.PARAMETER_FILTER));
    }

    protected String getQueryParameter(final boolean mandatory) {
        return getParameter(APIServletCall.PARAMETER_QUERY, mandatory);
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
        return getQueryValue(parameterName);
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
     * @return The values of a parameter as a list of String, or <code>null</code> if the parameter doesn't exist.
     */
    public List<String> getParameterAsList(final String name) {
        final String values = getQuery().getValues(name);
        if (values != null) {
            final String[] parameterValues = values.split(",");
            if (parameterValues != null && parameterValues.length > 0) {
                return Arrays.asList(parameterValues);
            }
        }
        return null;
    }

    public SearchOptions buildSearchOptions() {
        return new SearchOptionsCreator(getSearchPageNumber(), getSearchPageSize(), getSearchTerm(), new Sorts(
                getSearchOrder()), buildFilters()).create();
    }

    protected Filters buildFilters() {
        return new Filters(getSearchFilters());
    }

    @Override
    protected void doCatch(final Throwable throwable) {
        final Throwable t = throwable.getCause() != null ? throwable.getCause() : throwable;
        // Don't need to log the wrapping exception, the cause itself is more interesting:
        super.doCatch(t);

        final String message = "Error while querying REST resource " + getClass().getName() + " message: " + t.getMessage();
        ErrorMessage errorMessage = new ErrorMessage(t);

        getResponse().setStatus(getStatus(), message);

        if (t instanceof IllegalArgumentException || t instanceof JsonParseException) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "***" + message);
            }
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
        else if (t instanceof FileNotFoundException) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "***" + message);
            }
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            errorMessage.setMessage("File Not Found");
        }
        else if (t instanceof NotFoundException) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "***" + message);
            }
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        } else {
            LOGGER.log(Level.SEVERE, t.getMessage(), t);
        }
        getResponse().setEntity(errorMessage.toEntity());
    }

    @Override
    protected Representation doHandle(final Variant variant) throws ResourceException {
        // Used to ensure output is correctly encoded:
        variant.setCharacterSet(CharacterSet.UTF_8);
        return super.doHandle(variant);
    }

    @Override
    public String getAttribute(final String name) {
        final String attribute = super.getAttribute(name);
        try {
            if (attribute != null) {
                return URLDecoder.decode(attribute, "UTF-8");
            }
        } catch (final UnsupportedEncodingException e) {
        }
        return attribute;
    }

    public Long getPathParamAsLong(final String parameterName) {
        final String value = getAttribute(parameterName);
        return convertToLong(value);
    }

	private Long convertToLong(final String value) {
		try {
            return Long.parseLong(value);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("[ " + value + " ] must be a number");
        }
	}

    public List<Long> getParameterAsLongList(final String parameterName) {
        final String values = getQuery().getValues(parameterName);
        if (values != null) {
            final String[] parameterValues = values.split(",");
            if (parameterValues != null && parameterValues.length > 0) {
            	final List<Long> longValues = new ArrayList<>();
            	for (String parameterValue : parameterValues) {
					longValues.add(convertToLong(parameterValue));
				}
            	return longValues;
            }
        }
        return null;
    }

    public String getPathParam(final String name) {
        return getAttribute(name);
    }

    protected int getSearchPageNumber() {
        try {
            return getIntegerParameter(APIServletCall.PARAMETER_PAGE, true);
        } catch (final APIException e) {
            throw new IllegalArgumentException("query parameter p (page) is mandatory");
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("query parameter p (page) should be a number");
        }
    }

    protected int getSearchPageSize() {
        try {
            return getIntegerParameter(APIServletCall.PARAMETER_LIMIT, true);
        } catch (final APIException e) {
            throw new IllegalArgumentException("query parameter c (count) is mandatory");
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("query parameter c (count) should be a number");
        }
    }

    protected void setContentRange(final SearchResult<?> searchResult) {
        setContentRange(getSearchPageNumber(), getSearchPageSize(), searchResult.getCount());
    }

    protected void setContentRange(int pageNumber, int pageSize, long count) {
        final Series<Header> headers = getResponse().getHeaders();
        headers.add(new Header("Content-range", pageNumber + "-" + pageSize + "/" + count));
    }

    protected void manageContractViolationException(final ContractViolationException e, final String statusErrorMessage) {
        if (getLogger().isLoggable(Level.INFO)) {
            final StringBuilder explanations = new StringBuilder();
            for (final String explanation : e.getExplanations()) {
                explanations.append(explanation);
            }
            getLogger().log(Level.INFO, e.getSimpleMessage() + "\nExplanations:\n" + explanations);
        }
        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, statusErrorMessage);
        final ErrorMessageWithExplanations errorMessage = new ErrorMessageWithExplanations(e);
        errorMessage.setMessage(e.getSimpleMessage());
        errorMessage.setExplanations(e.getExplanations());
        getResponse().setEntity(errorMessage.toEntity());
    }

}
