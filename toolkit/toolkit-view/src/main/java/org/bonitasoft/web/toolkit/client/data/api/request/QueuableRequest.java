/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.UserSessionVariables;
import org.bonitasoft.web.toolkit.client.data.api.callback.HttpCallback;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;

/**
 * @author Séverin Moussel
 * 
 */
abstract public class QueuableRequest {

    private RequestQueue stack = null;

    protected HttpCallback callback = null;

    protected RequestBuilder request = null;

    public QueuableRequest() {
    }

    public QueuableRequest setCallback(final HttpCallback callback) {
        this.callback = callback;
        return this;
    }

    public void run(final HttpCallback callback) {
        this.callback = callback;
        this.run();
    }

    public void run() {
        final HttpCallback localCallback = new HttpCallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                if (QueuableRequest.this.callback != null) {
                    QueuableRequest.this.callback.onSuccess(httpStatusCode, response, headers);
                }
                if (QueuableRequest.this.stack != null) {
                    QueuableRequest.this.stack._next(true);
                }
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                if (QueuableRequest.this.callback != null) {
                    QueuableRequest.this.callback.onError(message, errorCode);
                }
                if (QueuableRequest.this.stack != null) {
                    QueuableRequest.this.stack.addError(errorCode, message);
                    QueuableRequest.this.stack._next(false);
                }
            }

        };

        this.request.setCallback(localCallback);

        try {
            if (UserSessionVariables.getUserVariable("token_api") != null) {
                this.request.setHeader("X-API-Token", UserSessionVariables.getUserVariable("token_api"));    
            }
            this.request.send();
        } catch (final RequestException e) {
            localCallback.onError(e.getMessage(), null);
        }
    }

    public void setStack(final RequestQueue stack) {
        this.stack = stack;
    }

}
