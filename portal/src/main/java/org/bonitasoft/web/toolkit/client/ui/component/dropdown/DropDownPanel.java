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
package org.bonitasoft.web.toolkit.client.ui.component.dropdown;

import static com.google.gwt.query.client.GQuery.*;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;

import com.google.gwt.dom.client.Element;

/**
 * @author Séverin Moussel
 *
 */
public class DropDownPanel extends ContainerStyled<DropDownItem> {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DOM GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void initTagNames() {
        setRootTag("div", "dropdown");
        // this.setWrapTagName("div");
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public DropDownPanel open() {
        _open(element);
        // $(this.element).slideDown();
        return this;
    }

    public native void _open(Element e)
    /*-{
        $wnd.$(e).fadeIn(80);
    }-*/;

    public DropDownPanel close() {
        _close(element);
        // $(this.element).slideUp(80);
        return this;
    }

    public native void _close(Element e)
    /*-{
        $wnd.$(e).fadeOut();
    }-*/;

    public DropDownPanel toggle() {
        return isOpened() ? close() : open();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isOpened() {
        return $(element).height() > 0;
    }

    public boolean isClosed() {
        return !isOpened();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ADD ITEMS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public DropDownPanel add(final String label, final String tooltip, final Action action) {
        append(new DropDownItem(label, tooltip, action));
        return this;
    }

    public DropDownPanel add(final String label, final String tooltip, final String token) {
        append(new DropDownItem(label, tooltip, token));
        return this;
    }

    public DropDownPanel add(final String label, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        append(new DropDownItem(label, tooltip, token, parameters));
        return this;
    }

}
