/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.menu.view.navigation;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.menu.Menu;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuItem;
import org.bonitasoft.web.toolkit.client.ui.utils.ComponentSelector;

import com.google.gwt.query.client.GQuery;

/**
 * @author Vincent Elcrin
 * 
 */
public class NavigationMenu extends Menu {

    private ComponentSelector selector;

    public NavigationMenu(final JsId jsid) {
        super(jsid);
        selector = new ComponentSelector(this, true);
    }

    public void addItems(final List<MenuItem> items) {
        for (final MenuItem menuItem : items) {
            addMenuItem(menuItem);
        }
    }

    public void select(final String pageToken) {
        if (isSelectable(pageToken)) {
            this.selector.unselect()
                    .select(getSubMenuElements(pageToken))
                    .select(getMenuElements(pageToken));
        }
    }

    private boolean isSelectable(final String pageToken) {
        return isSubMenu(pageToken) || isMenu(pageToken);
    }

    private boolean isMenu(final String pageToken) {
        return getMenuElements(pageToken).length() > 0;
    }

    private GQuery getMenuElements(final String pageToken) {
        return $("." + pageToken, getElement());
    }

    private boolean isSubMenu(final String pageToken) {
        return getSubMenuElements(pageToken).length() > 0;
    }

    private GQuery getSubMenuElements(final String pageToken) {
        return $(".dropdownitem", getElement()).has("." + pageToken);
    }
}
