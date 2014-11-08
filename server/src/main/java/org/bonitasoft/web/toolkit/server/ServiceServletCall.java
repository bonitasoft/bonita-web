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

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIMalformedUrlException;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;

/**
 * This class represent a call to a service.
 * 
 * @author Séverin Moussel
 * 
 */
public class ServiceServletCall extends ServletCall {

    private String calledToolToken;
	private ServiceFactory serviceFactory;

    public ServiceServletCall(ServiceFactory serviceFactory, final HttpServletRequest request, final HttpServletResponse response) {
        super(request, response);
		this.serviceFactory = serviceFactory;
    }

    @Override
    protected void parseRequest(final HttpServletRequest request, HttpServletResponse response) {
        super.parseRequest(request, response);

        // Gather all the POST parameters
        final Map<String, String> postParams = JSonItemReader.parseMap(getInputStream());
        for (final Map.Entry<String, String> entry : postParams.entrySet()) {
            this.parameters.put(entry.getKey(), new String[] { entry.getValue() });
        }

        // Read TOOL tokens
        this.calledToolToken = request.getPathInfo();
        if (this.calledToolToken.length() == 0) {
            throw new APIMalformedUrlException(getRequestURL(), "Missing tool name");
        }
    }

    @Override
    public final void doGet() {
        run();
    }

    @Override
    public final void doPost() {
        run();
    }

    @Override
    public final void doPut() {
        run();
    }

    @Override
    public final void doDelete() {
        run();
    }

    /**
     * Instantiate and run the service.
     */
    private void run() {
        final Service service = serviceFactory.getService(this.calledToolToken);
        service.setCaller(this);

        final Object response = service.run();

        if (response instanceof File) {
            output((File) response);
        } else if (response instanceof InputStream) {
            output((InputStream) response);
        } else if (response instanceof JsonSerializable || response instanceof Map<?, ?> || response instanceof List<?>) {
            output(JSonSerializer.serialize(response));
        } else if (response != null) {
            output(response.toString());
        }
    }

}
