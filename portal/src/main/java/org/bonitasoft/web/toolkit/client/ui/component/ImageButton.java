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
package org.bonitasoft.web.toolkit.client.ui.component;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class ImageButton extends Clickable {

    private final String imageUrl;

    public ImageButton(final JsId jsid, final String imageUrl, final String tooltip, final Action action) {
        super(jsid, tooltip, action);
        this.imageUrl = imageUrl;
    }

    public ImageButton(final JsId jsid, final String imageUrl, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        super(jsid, tooltip, token, parameters);
        this.imageUrl = imageUrl;
    }

    public ImageButton(final JsId jsid, final String imageUrl, final String tooltip, final String token) {
        super(jsid, tooltip, token);
        this.imageUrl = imageUrl;
    }

    public ImageButton(final String imageUrl, final String tooltip, final Action action) {
        super(tooltip, action);
        this.imageUrl = imageUrl;
    }

    public ImageButton(final String imageUrl, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        super(tooltip, token, parameters);
        this.imageUrl = imageUrl;
    }

    public ImageButton(final String imageUrl, final String tooltip, final String token) {
        super(tooltip, token);
        this.imageUrl = imageUrl;
    }

    @Override
    protected Element makeElement() {
        this.element = DOM.createImg();
        this.element.setAttribute("src", this.imageUrl.toString());

        return this.element;
    }

}
