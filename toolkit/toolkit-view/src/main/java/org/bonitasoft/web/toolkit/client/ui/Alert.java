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
package org.bonitasoft.web.toolkit.client.ui;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Alert {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ERROR AND INFO MESSAGES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static enum MESSAGE_TYPE {
        ERROR, WARNING, INFO
    };

    private static void addMessage(final Element target, final String message, final MESSAGE_TYPE messageType) {
        final Element parent = (Element) target.getParentElement();
        $("div.alert_message", parent).remove();
        $(target).removeClass("alert");
        $(target).removeClass(messageType.toString());
        $(target).addClass("alert", messageType.toString());
        final String div = HTML.div(new HTMLClass("alert_message").addClass(messageType.toString())) + HTML.text(message) + HTML._div();

        if ($(target).is(":file")) {
            $(".uploader", parent).after(div);
        } else {
            $(target).after(div);
        }
    }

    public final static void addError(final Element target, final String message) {
        addMessage(target, message, MESSAGE_TYPE.ERROR);
    }

    public final static void addWarning(final Element target, final String message) {
        addMessage(target, message, MESSAGE_TYPE.WARNING);
    }

    public final static void addInfo(final Element target, final String message) {
        addMessage(target, message, MESSAGE_TYPE.INFO);
    }

}
