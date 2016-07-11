/*
 * Copyright (C) 2013 BonitaSoft S.A.
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

package org.bonitasoft.console.client;

/**
 * Created by Vincent Elcrin
 * Date: 17/10/13
 * Time: 17:10
 */
public class JsniPublisher {

    public static void publishMethods() {
        initBonitaNamespace();
        publishI18n();
    }

    private static native void initBonitaNamespace() /*-{
        $wnd.bonitasoft = $wnd.bonitasoft || {};
    }-*/;

    private static native void publishI18n() /*-{
        $wnd.bonitasoft.i18n = $wnd.bonitasoft.i18n || {};
        $wnd.bonitasoft.i18n.translate =
            $entry(@org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n::_(Ljava/lang/String;));
    }-*/;
}
