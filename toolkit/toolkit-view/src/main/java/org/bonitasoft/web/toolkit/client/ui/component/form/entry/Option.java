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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import org.bonitasoft.web.toolkit.client.ui.component.HTMLComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.Element;

/**
 * A standard HTML Option component of the USerXP toolkit.<br>
 * It compose the choice list of a Select Element.<br>
 * Will generate an "Option" HTML tag, that will received the corresponding BOS "css" style.
 * 
 * @author Julien Mege
 */
public class Option extends Component implements HTMLComponent {

    private final String label;

    private final String value;

    private final boolean selected;

    /**
     * Option Constructor.
     * 
     * @param label
     *            the value displayed in the Select
     * @param value
     *            the value to post
     * @param selected
     *            true, if the option is selected
     */
    public Option(final String label, final String value, final boolean selected) {
        this.label = label;
        this.value = value;
        this.selected = selected;
    }

    /**
     * Option Constructor. Default selected value is false.
     * 
     * @param label
     *            the value displayed in the Select
     * @param value
     *            the value to post
     */
    public Option(final String label, final String value) {
        this(label, value, false);
    }

    public String getLabel() {
        return this.label;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    protected Element makeElement() {
        return XML.makeElement(getHtml());
    }

    @Override
    public String getHtml() {
        return HTML.option(this.label, this.value, this.selected);
    }

}
