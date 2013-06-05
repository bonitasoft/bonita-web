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

import com.google.gwt.user.client.Element;

/**
 * 
 * @author Séverin Moussel
 * 
 */
public class Menu extends MenuFolder {

    public Menu(final JsId jsid, final MenuItem... menuItems) {
        super(jsid, "", menuItems);
    }

    public Menu(final JsId jsid) {
        super(jsid, "");
    }

    public Menu(final MenuItem... menuItems) {
        super("", menuItems);
    }

    public Menu() {
        super("");
    }

    @Override
    protected Element makeElement() {
        final StringBuilder classes = new StringBuilder();

        classes.append("dropdownmenu");
        if (getJsId() != null) {
            classes.append(" ").append(getJsId().toString("dropdownmenu"));
        }

        this.items.setRootTag("ul", classes.toString());
        this.items.setWrapTag(null, null);

        return this.items.getElement();
    }
}
