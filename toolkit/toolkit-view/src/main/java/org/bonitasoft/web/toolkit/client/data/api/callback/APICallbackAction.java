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
package org.bonitasoft.web.toolkit.client.data.api.callback;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Vincent Elcrin
 * 
 *         Wrap an action into API callback.
 *         Action is executed on callback success.
 * 
 */
public class APICallbackAction extends APICallback {

    private Action onSuccess;

    private Action onError;

    public APICallbackAction(final Action onSuccess) {
        this(onSuccess, null);
    }

    public APICallbackAction(final Action onSuccess, final Action onError) {
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
        this.onSuccess.execute();
    }

    @Override
    public void onError(final String message, final Integer errorCode) {
        if (this.onError != null) {
            this.onError.execute();
        } else {
            super.onError(message, errorCode);
        }
    }
}
