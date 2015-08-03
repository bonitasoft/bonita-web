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
package org.bonitasoft.web.toolkit.client.ui.utils;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.page.MessagePage;
import org.bonitasoft.web.toolkit.client.ui.page.MessageTyped;

/**
 * @author Séverin Moussel
 *
 */
public abstract class Message {

    public static void show(final MessageTyped.TYPE type, final String message) {
        show(type, message, null);
    }

    public static void show(final MessageTyped.TYPE type, final String message, final Action onClose) {
        switch (type) {
            case ALERT:
                alert(message, onClose);
                break;
            case ERROR:
                error(message, onClose);
                break;
            case INFORMATION:
                info(message);
                break;
            case WARNING:
                warning(message);
                break;
            case SUCCESS:
                success(message);
                break;
        }
    }

    /**
     * Log the message on the server
     *
     * @param message
     */
    public static void log(final String message) {
        // TODO Activate while logging API will be available
        // Definitions.get(LogDefinition.TOKEN).getAPICaller().add(new LogItem(message));
    }

    /**
     * Display a non modal message of type INFO
     *
     * @param message
     */
    public static void info(final String message) {
        Noty.information(message);
    }

    /**
     * Display a non modal message of type SUCCESS
     *
     * @param message
     */
    public static void success(final String message) {
        Noty.success(message);
    }

    /**
     * Display a non modal message of type WARNING
     *
     * @param message
     */
    public static void warning(final String message) {
        Noty.warning(message);
    }

    /**
     * Display a modal message of type ALERT
     *
     * @param message
     */
    public static void alert(final String message) {
        ViewController.showPopup(new MessagePage(MessagePage.TYPE.ALERT, message));
    }

    /**
     * Display a modal message of type ALERT
     *
     * @param message
     */
    public static void alert(final String message, final Action onClose) {
        ViewController.showPopup(new MessagePage(MessagePage.TYPE.ALERT, message, onClose));
    }

    /**
     * Display a modal message of type ERROR
     *
     * @param message
     */
    public static void error(final String message) {
        ViewController.showPopup(new MessagePage(MessagePage.TYPE.ERROR, message));
    }

    /**
     * Display a modal message of type ERROR
     *
     * @param message
     */
    public static void error(final String message, final Action onClose) {
        ViewController.showPopup(new MessagePage(MessagePage.TYPE.ERROR, message, onClose));
    }

    /**
     * Log the exception's message on the server
     *
     * @param exception
     */

    public static void log(final Throwable exception) {
        log(JSonSerializer.serialize(exception));
    }
}
