/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.ui.component.menu;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * 
 * @author Séverin Moussel
 * 
 */
public class MenuLink extends Component implements MenuItem {

    private Clickable link = null;

    private String linkId = null;

    public MenuLink(final JsId jsid, final String label, final String tooltip, final Action action) {
        this.link = new Link(jsid, label, tooltip, action);
    }

    public MenuLink(final JsId jsid, final String label, final String tooltip, final String token) {
        this.link = new Link(jsid, label, tooltip, token);
    }

    public MenuLink(final String label, final String tooltip, final Action action) {
        this(null, label, tooltip, action);
    }

    public MenuLink(final String label, final String tooltip, final String token) {
        this(null, label, tooltip, token);
    }

    public void setLinkId(String id) {
        this.linkId = id;
    }

    @Override
    protected Element makeElement() {
        final Element rootElement = DOM.createElement("li");
        if (linkId != null) {
            link.setId(this.linkId);
        }
        Element linkElement = this.link.getElement();
        linkElement.setAttribute("href", "#");
        rootElement.appendChild(linkElement);
        if (this.link.getJsId() != null) {
            rootElement.addClassName(this.link.getJsId().toString());
        }

        return rootElement;
    }
}
