/*
 * Copyright (C) 2014 BonitaSoft S.A.
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

package org.bonitasoft.web.rest.server.framework.utils;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIMalformedUrlException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 *
 * Simple parser that extract parameters from a call to the REST api
 *
 * @author Baptiste Mesta
 */
public class RestRequestParser {

    private final HttpServletRequest request;
    private String apiName;
    private String resourceName;
    private APIID id;

    public RestRequestParser(final HttpServletRequest request) {
        this.request = request;
    }

    public String getApiName() {
        return apiName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public APIID getId() {
        return id;
    }

    public RestRequestParser invoke() {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            // it's not an URL like API/bpm/...
            pathInfo = request.getServletPath();
        }
        final String[] path = pathInfo.split("/");
        // Read API tokens
        if (path.length < 3) {
            throw new APIMalformedUrlException("Missing API or resource name [" + request.getRequestURL() + "]");
        }
        apiName = path[1];
        resourceName = path[2];
        // Read id (if defined)
        if (path.length > 3) {
            final List<String> pathList = Arrays.asList(path);
            id = APIID.makeAPIID(pathList.subList(3, pathList.size()));
        } else {
            id = null;
        }
        return this;
    }
}
