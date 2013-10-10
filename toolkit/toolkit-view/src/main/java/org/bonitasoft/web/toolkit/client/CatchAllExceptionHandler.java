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
package org.bonitasoft.web.toolkit.client;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.shared.UmbrellaException;
import org.bonitasoft.web.toolkit.client.common.exception.KnownException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.ui.page.UnexpectedErrorPage;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Séverin Moussel
 * 
 */
public class CatchAllExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void onUncaughtException(final Throwable e) {
        e.printStackTrace();
        if (e instanceof APIException || e instanceof KnownException || e instanceof UmbrellaException) {
            final String message = e.getMessage();
            if (message != null && !message.isEmpty()) {
                Message.error(message);
            } else {
                Message.error(_("The application encountered an error."));
            }
        } else {
        	ViewController.showPopup(new UnexpectedErrorPage());
        }
    }
}
