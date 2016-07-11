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

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.utils.TypedString;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A standard action link component of the USerXP toolkit
 * 
 * @author Séverin Moussel
 */
public class Link extends Clickable {

    private String label = null;

    private Image image = null;

    public static enum IMAGE_POSITION {
        LEFT, RIGHT
    }

    private IMAGE_POSITION imagePosition = IMAGE_POSITION.LEFT;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // With Action
    // /////////////////

    public Link(final JsId jsid, final String label, final String tooltip, final Action action) {
        super(jsid, tooltip, action);
        this.label = label;
    }

    public Link(final String label, final String tooltip, final Action action) {
        this(null, label, tooltip, action);
    }

    // With TypedString
    // /////////////////

    public Link(final JsId jsid, final String label, final String tooltip, final TypedString link) {
        super(jsid, tooltip, link);
        this.label = label;
    }

    public Link(final String label, final String tooltip, final TypedString link) {
        this(null, label, tooltip, link);
    }

    // With token and optional parameters
    // ///////////////////////////////////

    public Link(final JsId jsid, final String label, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        super(jsid, tooltip, token, parameters);
        this.label = label;
    }

    public Link(final String label, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        this(null, label, tooltip, token, parameters);
    }

    public Link(final JsId jsid, final String label, final String tooltip, final String token) {
        super(jsid, tooltip, token);
        this.label = label;
    }

    public Link(final String label, final String tooltip, final String token) {
        this(null, label, tooltip, token, null);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(final String label) {
        this.label = label;
        if (isGenerated()) {
            this.element.setInnerText(this.label);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // IMAGE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Link setImage(final Image image) {
        this.image = image;
        return this;
    }

    public Link setImage(final Image image, final IMAGE_POSITION imagePosition) {
        this.image = image;
        this.imagePosition = imagePosition;
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERATE HTML
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generate the DOM Element corresponding to the current Link.
     */
    @Override
    public final Element makeElement() {
        this.element = DOM.createAnchor();
        this.element.setInnerText(this.label);

        if (this.image != null) {
            if (this.imagePosition == IMAGE_POSITION.RIGHT) {
                HTML.append(this.element, this.image.getElement());
            } else {
                HTML.prepend(this.element, this.image.getElement());
            }
        }

        return this.element;
    }
}
