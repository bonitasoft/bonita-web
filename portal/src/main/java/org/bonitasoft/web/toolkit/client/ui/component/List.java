/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;

/**
 * A standard HTML List component of the USerXP toolkit.<br>
 * Will generate an "ul" and "li" HTML tag, that will received the corresponding BOS "css" style.<br>
 * <b>Example</b> : <br>
 * My list:
 * <ul>
 * <li>item 1</li>
 * <li>item 2</li>
 * </ul>
 * 
 * @author Julien Mege
 */
public class List extends Container<Node> {

    public List() {
        super();
    }

    public List(final JsId jsid) {
        super(jsid);
    }

    public List(final Node... components) {
        super(components);
    }

    public List(final JsId jsid, final Node... components) {
        super(jsid, components);
    }

    @Override
    protected void initTagNames() {
        this.rootTagName = "ul";
        this.wrapTagName = "li";
    }
}
