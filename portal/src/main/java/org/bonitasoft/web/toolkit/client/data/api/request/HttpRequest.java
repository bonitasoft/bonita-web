/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.data.api.request;

import org.bonitasoft.web.toolkit.client.RequestBuilder;
import org.bonitasoft.web.toolkit.client.data.api.callback.HttpCallback;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestException;

/**
 * @author Séverin Moussel
 */
public class HttpRequest {

    public final static String CONTENT_TYPE_JSON = "application/json";

    public final static String CONTENT_TYPE_HTML = "text/html";

    public final static String CONTENT_TYPE_BINARY = "application/octet-stream";

    /**
     * Send a GET HTTP request
     *
     * @param callback
     *        The APICallback to call onSuccess or onError.
     * @param url
     *        The URL of the API
     */
    public void send(final String url, final HttpCallback callback) {
        this.send(RequestBuilder.GET, url, null, null, callback);
    }

    /**
     * Send a POST HTTP request
     *
     * @param callback
     *        The APICallback to call onSuccess or onError.
     * @param datas
     *        The data to send
     * @param url
     *        The URL of the API
     */
    public void send(final String url, final String datas, final HttpCallback callback) {
        this.send(RequestBuilder.POST, url, datas, null, callback);
    }

    /**
     * Send the HTTP request
     *
     * @param method
     *        The method to use between RequestBuilder.GET, RequestBuilder.POST, RequestBuilder.PUT, RequestBuilder.DELETE
     * @param callback
     *        The APICallback to call onSuccess or onError.
     * @param url
     *        The URL of the API
     */
    public void send(final Method method, final String url, final HttpCallback callback) {
        this.send(method, url, null, null, callback);
    }

    /**
     * Send the HTTP request with data
     *
     * @param method
     *        The method to use between RequestBuilder.GET, RequestBuilder.POST, RequestBuilder.PUT, RequestBuilder.DELETE
     * @param callback
     *        The APICallback to call onSuccess or onError.
     * @param url
     *        The URL of the API
     * @param datas
     *        The data to send
     */
    public void send(final Method method, final String url, final String datas, final String contentType, final HttpCallback callback) {
        final RequestBuilder builder = new RequestBuilder(method, url);
        if (datas != null) {
            builder.setRequestData(datas);
        }
        if (contentType != null) {
            builder.setHeader("Content-Type", (contentType != null ? contentType : "text/plain") + ";charset=UTF-8");
        }

        builder.setTimeoutMillis(30000);
        builder.setCallback(callback);
        Request request = null;
        try {
            request = builder.send();
        } catch (final RequestException e) {
            callback.onError(request, e);
        }
    }
}
