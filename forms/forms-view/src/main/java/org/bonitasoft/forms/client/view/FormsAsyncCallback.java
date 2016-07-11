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
package org.bonitasoft.forms.client.view;

import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.URLUtils;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Vincent Elcrin
 * 
 */
public abstract class FormsAsyncCallback<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        try {
            throw caught;
        } catch (SessionTimeoutException e) {
            handleSessionTimeout();
        } catch (Throwable e) {
            onUnhandledFailure(caught);
        }
    }

    protected void handleSessionTimeout() {
        DOMUtils domUtils = DOMUtils.getInstance();
        URLUtils urlUtils = URLUtils.getInstance();
        String url = urlUtils.removeURLparameters(Window.Location.getHref());
        if(!domUtils.isPageInFrame()) {
            url += "?redirectUrl=" + URL.encodeQueryString(Window.Location.getQueryString()+Window.Location.getHash());
        }
        urlUtils.parentFrameRedirect(url);
    }


    public abstract void onUnhandledFailure(Throwable caught);

}
