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
package org.bonitasoft.web.toolkit.client.ui.component;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;

/**
 * A standard Tabs component of the USerXP toolkit. <BR>
 * It is composed by a list of Sections presented as a tab.
 * 
 * @author Séverin Moussel
 */
public class Tabs extends Container<Section> {

    public Tabs() {
        super();
    }

    public Tabs(final JsId jsid, final Section... components) {
        super(jsid, components);
    }

    public Tabs(final JsId jsid) {
        super(jsid);
    }

    public Tabs(final Section... components) {
        super(components);
    }

    @Override
    protected void postProcessHtml() {
        this.element.addClassName("tabs");
        super.postProcessHtml();
    }

}
