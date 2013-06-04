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
package org.bonitasoft.web.toolkit.client.ui.component.dashboard;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Gadget extends Component {

    public Gadget(final String gadgetId) {
        this(null, gadgetId);
    }

    public Gadget(final JsId jsid, final String gadgetId) {
        super(jsid);
        setId(gadgetId);
    }

    @Override
    protected Element makeElement() {
        return XML.makeElement(HTML.div(new HTMLClass("gadgets-gadget-chrome")));
    }

}
