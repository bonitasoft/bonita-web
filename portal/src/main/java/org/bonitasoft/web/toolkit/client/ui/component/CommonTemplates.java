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

package org.bonitasoft.web.toolkit.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Created by Vincent Elcrin
 * Date: 01/10/13
 * Time: 10:55
 */
public final class CommonTemplates {

    public interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template(
                "<div class='section'>" +
                        "<div class='header'><h1>{0}</h1></div>" +
                        "<div class='body'>{1}</div>" +
                        "</div></div>")
        SafeHtml section(String title, SafeHtml content);
    }

    private static Templates TEMPLATES = GWT.create(Templates.class);

    public static Templates get() {
        return TEMPLATES;
    }

}
