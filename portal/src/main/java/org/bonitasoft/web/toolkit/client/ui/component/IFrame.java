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

import com.google.gwt.user.client.Element;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XMLAttributes;

import static com.google.gwt.query.client.GQuery.$;

/**
 * 
 * @author Séverin Moussel
 * 
 */
public class IFrame extends Component {

    private String id;
    private String url = null;

    public IFrame(final String id, final String url) {
        super();
        this.id = id;
        this.url = url;
    }

    @Override
    protected Element makeElement() {
        return (Element) $(
                HTML.iFrame(this.url, new XMLAttributes("id", id).add("frameborder", "0")))
                .get(0);
    }
}
