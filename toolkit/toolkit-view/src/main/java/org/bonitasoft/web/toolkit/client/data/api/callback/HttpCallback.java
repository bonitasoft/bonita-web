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
package org.bonitasoft.web.toolkit.client.data.api.callback;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.Window;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.http.HttpException;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.common.json.JSonUnserializerClient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class HttpCallback implements RequestCallback {

    @Override
    public final void onResponseReceived(final Request request, final Response response) {
        
        // Same origne policy violation
        // hack to avoid error on timeout exception generating same origne policy violation
        // Need to deal with specific exception in apps, not in toolkit
        if (response.getStatusCode() == 0) {
            return;
        } 
//        // Session expired
        else if (response.getStatusCode() == 401) {
            Window.Location.reload();
        }
        // Other HTTP problems
        else if (response.getStatusCode() >= 300) {
            onError(response.getText(), response.getStatusCode());
        }
        // Success
        else {

            final Map<String, String> headersMap = new LinkedHashMap<String, String>();
            final Header[] headers = response.getHeaders();
            for (int i = 0; i < headers.length; i++) {
                if (headers[i] != null) {
                    if (headersMap.containsKey(headers[i].getName())) {
                        headersMap.put(headers[i].getName(), headersMap.get(headers[i].getName()) + "," + headers[i].getValue());
                    } else {
                        headersMap.put(headers[i].getName(), headers[i].getValue());
                    }
                }
            }

            onSuccess(response.getStatusCode(), response.getText(), headersMap);
        }
    }

    /**
     * This function must be overridden to catch the success of an API call.
     * 
     * @param httpStatusCode
     *            The HTTP status code returned
     * @param response
     *            The body of the response as a String
     * @param headers
     *            The headers as a HashMap<name, value>
     */
    public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
        // Do nothing by default
    }

    /**
     * Manage HTTP problems while server is not responding
     */
    @Override
    public final void onError(final Request request, final Throwable exception) {
        String exceptionMessage;
        if (exception instanceof RequestTimeoutException) {
            exceptionMessage = _("No response has been received yet, but the background operation might still be in progress. Wait a few seconds then reload the page.");
        } else {
            exceptionMessage = exception.getMessage();
        }
        onError(exceptionMessage, 503);
    }

    /**
     * This function must be overridden to catch errors on an API call.
     * 
     * @param message
     *            The error message
     * @param errorCode
     *            The HTTP status code received from the server
     */
    public void onError(final String message, final Integer errorCode) {
        throw parseException(message, errorCode);
    }

    protected ServerException parseException(final String message, final Integer errorCode) {
        try {
            final AbstractTreeNode<String> tree = JSonUnserializerClient.unserializeTree(message);
    
            if (!(tree instanceof TreeIndexed<?>)) {
                onError(message, errorCode);
            }
    
            final TreeIndexed<String> tree2 = (TreeIndexed<String>) tree;
    
            ServerException ex = null;
            if (tree2.containsKey("api") && tree2.containsKey("resource")) {
                ex = new APIException(tree2.getValue("message"));
                ((APIException) ex).setApi(tree2.getValue("api"));
                ((APIException) ex).setResource(tree2.getValue("resource"));
            } else {
                ex = new ServerException(tree2.getValue("message"));
            }
            final AbstractTreeNode<String> causeNode = tree2.get("cause");
            if (causeNode != null && causeNode instanceof TreeIndexed<?>) {
                ex.setOriginalClassName(((TreeIndexed<String>) causeNode).getValue("exception"));
            }
    
            final AbstractTreeNode<String> stacktrace = tree2.get("stacktrace");
            if (stacktrace != null) {
                if (stacktrace instanceof Tree<?>) {
                    ex.setOriginalStackTrace(((Tree<String>) stacktrace).getValues());
                } else if (stacktrace instanceof TreeLeaf<?>) {
                    ex.setOriginalStackTrace(((TreeLeaf<String>) stacktrace).getValue());
                }
            }
            ex.setStatusCode(errorCode);
    
            return ex;
        } catch (final JSONException e) {
            throw new HttpException(message).setStatusCode(errorCode);
        }
    }
}
