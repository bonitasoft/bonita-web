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
package org.bonitasoft.console.common.server.login.localization;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author Vincent Elcrin, Julien Reboul
 */
public class UrlBuilder {

    private final URIBuilder uriBuilder;

    private List<NameValuePair> parameters = new LinkedList<>();

    public UrlBuilder(final String urlString) {
        URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        uriBuilder = new URIBuilder()
                .setScheme(uri.getScheme())
                .setHost(uri.getHost())
                .setPort(uri.getPort())
                .setPath(uri.getPath())
                .setFragment(uri.getFragment());
        if (uri.getQuery() != null) {
            //avoid NPE thrown by httpClient 4.5.2 regression
            parameters.addAll(URLEncodedUtils.parse(uri.getQuery(), Charset.forName("UTF8")));
        }
    }

    public void appendParameter(final String key, final String value) {
        if (!isParameterAlreadyDefined(parameters, key)) {
            parameters.add(new BasicNameValuePair(key, value));
        }
    }

    private boolean isParameterAlreadyDefined(List<NameValuePair> params, String key) {
        for (NameValuePair param : params) {
            if (param.getName().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public void appendParameters(final Map<String, String[]> parameters) {
        for (Entry<String, String[]> next : parameters.entrySet()) {
            appendParameter(next.getKey(),
                    new UrlValue(next.getValue()).toString());
        }
    }

    public String build() {
        return uriBuilder
                .setQuery(URLEncodedUtils.format(parameters, "UTF-8"))
                .toString();
    }
}
