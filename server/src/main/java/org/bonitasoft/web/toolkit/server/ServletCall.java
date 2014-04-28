/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.server.utils.LocaleUtils;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class ServletCall {

    private String inputStream = null;

    // /**
    // * @param parameter
    // * @return
    // */
    // private List<ItemSearchOrder> parseOrders(final String parameter) {
    // if (this.parameters == null) {
    // return null;
    // }
    // final String[] split = parameter.split("\\s*,\\s*");
    // final List<ItemSearchOrder> results = new ArrayList<ItemSearchOrder>();
    // for (int i = 0; i < split.length; i++) {
    // final String[] orderSplit = split[i].split("\\s");
    // results.add(new ItemSearchOrder(orderSplit[0], orderSplit.length == 1 || orderSplit[1].equalsIgnoreCase("asc")));
    // }
    //
    // return results;
    // }

    /**
     * The parameters of the URL.<br />
     * Result of the parsing of the query string : "?a=b&c=d&..."
     */
    protected Map<String, String[]> parameters = new HashMap<String, String[]>();

    /**
     * The request made to access this servletCall.
     */
    private final HttpServletRequest request;

    /**
     * The response to return.
     */
    private final HttpServletResponse response;

    /**
     * Default constructor.
     * 
     * @param request
     *            The request made to access this servletCall.
     * @param response
     *            The response to return.
     */
    public ServletCall(final HttpServletRequest request, final HttpServletResponse response) {
        super();
        this.request = request;
        this.response = response;

        parseRequest(request);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PARAMETERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the current call's HttpSession
     * 
     * @return This method returns the session from the current call.
     */
    public HttpSession getHttpSession() {
        return request.getSession();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    public String getQueryString() {
        return request.getQueryString();
    }

    /**
     * Reconstruct the URL the client used to make the request.
     * The returned URL contains a protocol, server name, port
     * number, and server path, but it does not include query
     * string parameters.
     * 
     * @return This method returns the reconstructed URL
     */
    public final String getRequestURL() {
        return request.getRequestURL().toString();
    }

    /**
     * Read the input stream and set it in a String
     */
    public final String getInputStream() {
        if (inputStream == null) {
            try {
                final BufferedReader reader = request.getReader();
                final StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    sb.append(line + "\n");
                    line = reader.readLine();
                }
                reader.close();

                inputStream = sb.toString();

            } catch (final IOException e) {
                throw new RuntimeException("Can't read input Stream.", e);
            }
        }

        return inputStream;
    }

    /**
     * Count the number of parameters passed in the URL
     * 
     * @return This method returns the number of parameters in the URL
     */
    public final int countParameters() {
        return parameters.size();
    }

    /**
     * Get a parameter values by its name
     * 
     * @param name
     *            The name of the parameter (case sensitive)
     * @return This method returns the values of a parameter as a list of String or null if the parameter isn't defined
     */
    public final List<String> getParameterAsList(final String name) {
        return getParameterAsList(name, (String) null);
    }

    /**
     * Get a parameter values by its name
     * 
     * @param name
     *            The name of the parameter (case sensitive)
     * @param defaultValue
     *            The value to return if the parameter isn't define
     * @return This method returns the values of a parameter as a list of String
     */
    public final List<String> getParameterAsList(final String name, final String defaultValue) {
        if (parameters.containsKey(name)) {
            return Arrays.asList(parameters.get(name));
        }
        if (defaultValue != null) {
            final List<String> results = new ArrayList<String>();
            results.add(defaultValue);
            return results;
        }
        return null;
    }

    /**
     * Get a parameter first value by its name
     * 
     * @param name
     *            The name of the parameter (case sensitive)
     * @return This method returns the first value of a parameter as a String or null if the parameter isn't define
     */
    public final String getParameter(final String name) {
        return getParameter(name, (String) null);
    }

    /**
     * Get a parameter first value by its name
     * 
     * @param name
     *            The name of the parameter (case sensitive)
     * @param defaultValue
     *            The value to return if the parameter isn't define
     * @return This method returns the first value of a parameter as a String
     */
    public final String getParameter(final String name, final String defaultValue) {
        if (parameters.containsKey(name)) {
            final String[] result = parameters.get(name);
            if (result.length > 0) {
                return result[0];
            }
        }
        return defaultValue;
    }

    /**
     * Get all the parameters
     * 
     * @return This method returns all the parameters as a Map of array of String
     */
    public final Map<String, String[]> getParameters() {
        return parameters;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERATE RESPONSES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Write into the output header.
     * 
     * @param name
     *            The name of the header to write.
     * @param value
     *            The value of the header to write.
     */
    protected final void head(final String name, final String value) {
        response.addHeader(name, value);
    }

    /**
     * Output a file
     * 
     * @param file
     *            The file to output
     */
    protected final void output(final File file) {
        try {
            output(new FileInputStream(file));
        } catch (final FileNotFoundException e) {
            throw new ServerException(e);
        }
    }

    /**
     * Output a stream as a file
     * 
     * @param stream
     *            The stream to output
     * @param filename
     *            The name of the file to retrieve with the stream.
     */
    protected void output(final InputStream stream, final String filename) {
        response.addHeader("Content-Disposition", "attachment; filename=" + filename + ";");
        output(stream);
    }

    /**
     * Output a stream as a file
     * 
     * @param stream
     *            The stream to output
     */
    protected void output(final InputStream stream) {
        response.setContentType("application/octet-stream");
        try {
            IOUtils.copy(stream, response.getOutputStream());
        } catch (final IOException e) {
            throw new ServerException(e);
        }
    }

    /**
     * Write into the output
     * 
     * @param string
     *            The string to output
     */
    protected final void output(final String string) {
        final PrintWriter outputWriter = getOutputWriter();
        outputWriter.print(string);
        outputWriter.flush();
    }

    /**
     * Write into the output
     * 
     * @param object
     *            An object that will be transform into JSon
     */
    protected final void output(final Object object) {
        final PrintWriter outputWriter = getOutputWriter();
        outputWriter.print(JSonSerializer.serialize(object));
        outputWriter.flush();
    }

    /**
     * The outputWriter in which to write the response String.
     */
    private PrintWriter outputWriter = null;

    /**
     * Prepare the output
     * 
     * @param response
     */
    private PrintWriter getOutputWriter() {
        if (outputWriter == null) {
            response.setContentType("application/json;charset=UTF-8");
            try {
                outputWriter = response.getWriter();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return outputWriter;
    }

    /**
     * Read elements form the request
     * <ul>
     * <li>API tokens</li>
     * <li>item id (if defined)</li>
     * <li>parameters</li>
     * </ul>
     * 
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected void parseRequest(final HttpServletRequest request) {
        // Create a new HashMap and copy all the elements in the new one
        parameters.putAll(this.request.getParameterMap());
    }

    public String getLocale() {
        return LocaleUtils.getUserLocale(request);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // REQUEST ENTRY POINTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Entry point for GET and SEARCH
     */
    public abstract void doGet();

    /**
     * Entry point for CREATE
     */
    public abstract void doPost();

    /**
     * Entry point for UPDATE
     */
    public abstract void doPut();

    /**
     * Entry point for DELETE
     */
    public abstract void doDelete();

}
