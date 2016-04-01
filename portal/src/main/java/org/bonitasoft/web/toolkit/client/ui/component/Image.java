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
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.html.XMLAttributes;
import org.bonitasoft.web.toolkit.client.ui.utils.Path;
import org.bonitasoft.web.toolkit.client.ui.utils.TypedString;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * A standard HTML Image component of the USerXP toolkit.<br>
 * Display Image Element.<br>
 * Will generate an "p" HTML tag, that will received the corresponding BOS "css" style.
 * 
 * @author Séverin Moussel
 */
public class Image extends Component {

    /**
     * The Image {@link org.bonitasoft.web.toolkit.client.ui.utils.Url Url} or {@link org.bonitasoft.web.toolkit.client.ui.utils.Path Path}.
     */
    private TypedString urlOrPath;

    /**
     * The width of the image.
     */
    private int width = 0;

    /**
     * The height of the image.
     */
    private int height = 0;

    /**
     * Constructor with URL as String.
     * 
     * @param url
     *        The Image URL to display. If the URL doesn't start with "http://" or "http://", this String will be considered as a Path.
     * @param width
     *        The Image width.
     * @param height
     *        The Image height.
     * @param tooltip
     *        The tooltip text to display over the Image.
     */
    public Image(final String url, final int width, final int height, final String tooltip) {
        this(null, url, width, height, tooltip);
    }

    /**
     * Constructor with URL as String and a JsId.
     * 
     * @param jsid
     *        The JsId to set on the image.
     * @param url
     *        The Image URL to display. If the URL doesn't start with "http://" or "http://", this String will be considered as a Path.
     * @param width
     *        The Image width.
     * @param height
     *        The Image height.
     * @param tooltip
     *        The tooltip text to display over the Image.
     */
    public Image(final JsId jsid, final String url, final int width, final int height, final String tooltip) {
        this(
                jsid,
                url.startsWith("http://") || url.startsWith("https://")
                        ? new Url(url)
                        : new Path(url),
                width,
                height,
                tooltip);
    }

    /**
     * Constructor with URL as a TypedString.
     * 
     * @param url
     *        The Image {@link org.bonitasoft.web.toolkit.client.ui.utils.Url Url} or {@link org.bonitasoft.web.toolkit.client.ui.utils.Path Path} to
     *        display.
     * @param width
     *        The Image width.
     * @param height
     *        The Image height.
     * @param tooltip
     *        The tooltip text to display over the Image.
     */
    public Image(final TypedString url, final int width, final int height, final String tooltip) {
        this(null, url, width, height, tooltip);
    }

    /**
     * Constructor with URL as a TypedString and a JsId.
     * 
     * @param jsid
     *        The JsId to set on the image.
     * @param url
     *        The Image {@link org.bonitasoft.web.toolkit.client.ui.utils.Url Url} or {@link org.bonitasoft.web.toolkit.client.ui.utils.Path Path} to
     *        display.
     * @param width
     *        The Image width.
     * @param height
     *        The Image height.
     * @param tooltip
     *        The tooltip text to display over the Image.
     */
    public Image(final JsId jsid, final TypedString url, final int width, final int height, final String tooltip) {
        super(jsid);
        setUrl(url);
        this.width = width;
        this.height = height;
        this.tooltip = tooltip;
    }

    /**
     * Generate the DOM Element corresponding to the current Image Element.
     * 
     * @return This method returns the root element of this Component.
     */
    @Override
    protected final Element makeElement() {
        final XMLAttributes xmlAttr = new XMLAttributes();
        if (this.width > 0) {
            xmlAttr.add("width", String.valueOf(this.width));
        }
        if (this.height > 0) {
            xmlAttr.add("height", String.valueOf(this.height));
        }

        return XML.makeElement(
                HTML.img(
                        this.urlOrPath.toString(),
                        this.tooltip,
                        xmlAttr));
    }

    /**
     * Set the URL or path to he image to display.
     * 
     * @param url
     *        The {@link org.bonitasoft.web.toolkit.client.ui.utils.Url Url} or {@link org.bonitasoft.web.toolkit.client.ui.utils.Path Path} to set
     * @return This method returns "this" to allow cascading calls.
     */
    public final Image setUrl(final TypedString url) {
        this.urlOrPath = url instanceof Url ? url : new Url(GWT.getModuleBaseURL() + "../theme/" + url.toString());
        if (isGenerated()) {
            getElement().setAttribute("src", this.urlOrPath.toString());
        }
        return this;
    }
}
