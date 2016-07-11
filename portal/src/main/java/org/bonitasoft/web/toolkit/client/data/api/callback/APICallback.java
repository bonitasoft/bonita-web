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

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

/**
 * An APICallback define the action to do once an API called has ended (success and error)
 * <p>
 * This class extends RequestCallback but make it easier to use by parsing the headers and the error message.
 *
 * @author Séverin Moussel
 *
 */
public abstract class APICallback extends HttpCallback {

    @Override
    public void onError(final String message, final Integer errorCode) {
        if (errorCode != null && !handlesErrorCode(message, errorCode)) {
            handlesErrorGenerically(message, errorCode);
        }
    }

    protected boolean handlesErrorCode(final String message, final Integer errorCode) {
        if (isNotFound(errorCode)) {
            on404NotFound(message);
            return true;
        } else if (isForbidden(errorCode)) {
            on403Forbidden(message);
            return true;
        }
        return false;
    }

    /**
     * @param errorCode
     * @return
     */
    private boolean isNotFound(final Integer errorCode) {
        return errorCode != null && errorCode.equals(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * @param errorCode
     * @return
     */
    private boolean isForbidden(final Integer errorCode) {
        return errorCode != null && errorCode.equals(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * @param message
     */
    protected void on404NotFound(final String message) {
    }

    /**
     * @param message
     */
    protected void on403Forbidden(final String message) {
        Message.error(_("Permission denied: you do not have the rights to perform this action."));
    }

    private void handlesErrorGenerically(final String message, final Integer errorCode) {
        final ServerException ex = parseException(message, errorCode);
        if (APISessionInvalidException.class.toString().equals(ex.getOriginalClassName())) {
            Message.error(_("Your session has expired. Please log in again and retry."), new RedirectionAction(new Url("../logoutservice")));
        } else {
            throw ex;
        }
    }
}
