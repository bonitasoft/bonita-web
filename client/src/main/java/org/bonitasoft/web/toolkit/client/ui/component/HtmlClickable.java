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

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Element;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

/**
 * Created by Vincent Elcrin
 * Date: 30/09/13
 * Time: 16:53
 */
public class HtmlClickable extends Clickable {

    private SafeHtml html;

    public HtmlClickable(SafeHtml html, Action action) {
        super(null, action);
        this.html = html;
    }

    @Override
    protected Element makeElement() {
        return XML.makeElement(html.asString());
    }
}
