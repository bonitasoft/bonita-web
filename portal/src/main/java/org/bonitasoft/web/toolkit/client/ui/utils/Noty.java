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

import org.bonitasoft.web.toolkit.client.ui.page.MessageTyped;

/**
 * Notification class based on Noty2 : http://needim.github.com/noty/
 *
 * @author Séverin Moussel
 *
 */
public class Noty implements MessageTyped {

    private final TYPE type;

    private final String message;

    private int timeout = 3000;

    private enum layout {
        topCenter,
        topLeft,
        topRight,
        bottomCenter,
        bottomLeft,
        bottomRight,
        center,
        centerLeft,
        centerRight,
        inline
    }

    public Noty(final TYPE type, final String message) {
        super();
        this.type = type;
        this.message = message;
    }

    /**
     * @param timeout
     *            the timeout to set in milliseconds
     */
    public Noty setTimeout(final int timeout) {
        this.timeout = timeout;
        return this;
    }

    public void show(final String layout) {
        _show(type.toString(), message, timeout, layout);
    }

    public void show() {
        _show(type.toString(), message, timeout, layout.topCenter.toString());
    }

    private native void _show(String type, String message, int timeout, String layout)
    /*-{
        $wnd.noty({
            text:message,
            type:type,
            timeout:timeout,
            layout:layout,
            closeWith: ['click', 'button']
        });
    }-*/;

    public static void show(final TYPE type, final String message) {
        new Noty(type, message).show();
    }

    public static void alert(final String message) {
        new Noty(TYPE.ALERT, message).show();
    }

    public static void success(final String message) {
        new Noty(TYPE.SUCCESS, message).show();
    }

    public static void error(final String message) {
        new Noty(TYPE.ERROR, message).show();
    }

    public static void warning(final String message) {
        new Noty(TYPE.WARNING, message).show();
    }

    public static void information(final String message) {
        new Noty(TYPE.INFORMATION, message).show();
    }

    public static void confirm(final String message) {
        new Noty(TYPE.CONFIRM, message).show();
    }

}
