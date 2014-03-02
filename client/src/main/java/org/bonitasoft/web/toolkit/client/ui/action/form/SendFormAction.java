/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.ui.action.form;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.callback.HttpCallback;
import org.bonitasoft.web.toolkit.client.data.api.request.HttpRequest;

import com.google.gwt.http.client.RequestBuilder;

/**
 * This class is used to ensure the homogeneity and the validity of the Post request for a Form.<br>
 * 
 * @author Julien Mege
 */
public class SendFormAction extends FormAction {

    protected final String resourceUrl;

    protected final HttpCallback callBack;

    /**
     * Constructor for the SendFormAction, using a standard callback that just call historyBack.
     * 
     * @param resourceUrl
     *            URL to call for the POST request
     */
    public SendFormAction(final String resourceUrl) {
        this(resourceUrl, new HttpCallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.getInstance().historyBack();
            }

        });
    }

    /**
     * Constructor for the SendFormAction, using a specific callback.
     * 
     * @param resourceUrl
     *            URL to call for the POST request
     * @param callBack
     *            Callback to use on the request response.
     */
    public SendFormAction(final String resourceUrl, final HttpCallback callBack) {
        this.resourceUrl = resourceUrl;
        this.callBack = callBack;
    }

    /**
     * Function to build the request and execute the POST action.
     */
    @Override
    public void execute() {

        new HttpRequest().send(RequestBuilder.POST, this.resourceUrl, getForm().toJson(), HttpRequest.CONTENT_TYPE_JSON, this.callBack);

    }
}
