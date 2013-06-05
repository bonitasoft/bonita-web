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

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A standard HTML Text component of the USerXP toolkit.<br>
 * Display Text Element, and allows to replace "%%" by the corresponding parameters.<br>
 * Will generate an "p" HTML tag, that will received the corresponding BOS "css" style.
 * 
 * @author Séverin Moussel
 */
public class Text extends Component {

    private String text = "";

    private AbstractComponent[] components = null;

    protected String rootTagName = "p";

    protected Element rootElement = null;

    /**
     * Default Constructor.
     * 
     * @param text
     *            The text to display
     */
    public Text(final String text) {
        this(text, (AbstractComponent) null);
    }

    /**
     * Default Constructor.
     * 
     * @param text
     *            The text to display
     * @param components
     *            The list of argument values to replace in the text.
     */
    public Text(final String text, final AbstractComponent... components) {
        super(null);
        this.setText(text, components);
    }

    public Text setText(final String text) {
        this.setText(text, (AbstractComponent) null);
        return this;
    }

    public AbstractComponent[] getComponents() {
        return this.components;
    }

    public Text setText(final String text, final AbstractComponent... components) {
        this.text = text != null ? text : "";
        if (components == null || components.length > 1 || components.length > 0 && components[0] != null) {
            this.components = components;
        }
        if (isGenerated()) {
            makeTextHtml();
        }
        return this;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Generate the DOM Element corresponding to the current Text Element.
     */
    @Override
    protected Element makeElement() {
        if (this.rootTagName == "p" && !this.text.contains("\n")) {
            this.rootTagName = "span";
        }

        this.rootElement = DOM.createElement(this.rootTagName);

        makeTextHtml();

        return this.rootElement;
    }

    // @Override
    // public String toString() {
    //
    //
    // // Look for paragraphs
    // final String htmlString = HTML.encode(this.text).replaceAll("\r\n", "\n").replaceAll("\n\n", "\r");
    //
    // final String[] html = this.text.split("\r");
    // final StringBuilder output1 = new StringBuilder();
    //
    // if (html.length > 1) {
    // for (int i = 0; i < html.length; i++) {
    // output1.append("<p>");
    // output1.append(html[i]);
    // output1.append("</p>");
    // }
    // } else {
    // output1.append(htmlString);
    // }
    //
    // // Look for new lines
    // final String output2 =
    // HTML.openTag(this.rootTagName)
    // + output1.toString().replaceAll("\n", "<br />")
    // + HTML.closeTag(this.rootTagName);
    //
    // // Look for args
    // }

    protected void makeTextHtml() {

        $(this.rootElement).empty();

        final String text = this.text == null ? "" : this.text;
        final String html = text.replaceAll("\r?\n", "<br />");
        final boolean hasHtmlContent = html != text;

        // TODO : Add clean paragraph detection (double \r\n)
        // TODO : Add detection of *...* for bold and _..._ for underline

        // If no components set
        if (this.components == null || this.components.length == 0) {
            // TODO remove unnecessary span
            // TODO secure HTML input
            if (hasHtmlContent) {
                this.rootElement.setInnerHTML(html);
            } else {
                this.rootElement.setInnerText(html);
            }
            return;
        }

        // Insert components
        final String[] textArray = html.split("%\\S*%");

        if (textArray.length > 0) {
            this.rootElement.appendChild(Document.get().createTextNode(textArray[0]));
        }

        int i = 1;
        for (i = 1; i < textArray.length; i++) {
            if (i <= this.components.length && this.components[i - 1] != null) {
                appendComponentToHtml(this.rootElement, this.components[i - 1]);
                this.components[i - 1] = null;
            }
            this.rootElement.appendChild(Document.get().createTextNode(textArray[i]));
        }

        // Insert remaining components
        for (int j = i - 1; j < this.components.length; j++) {
            appendComponentToHtml(this.rootElement, this.components[j]);
        }

    }

    public Text setRootTagName(final String rootTagName) {
        this.rootTagName = rootTagName;
        return this;
    }

}
