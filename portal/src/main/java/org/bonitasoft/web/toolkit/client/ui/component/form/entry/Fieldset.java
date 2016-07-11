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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Fieldset extends Section {

    protected String label = null;

    /**
     * Default Constructor.
     * 
     * @param jsid
     */
    public Fieldset(final JsId jsid, final String label) {
        super(jsid);
        setRootTagName("fieldset");
        this.label = label;
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     */
    public Fieldset(final JsId jsid) {
        this(jsid, null);
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();

        if (this.label != null) {
            HTML.prepend(this.element, makeLabel());
        }
    }

    protected Element makeLabel() {
        final Element label = DOM.createElement("legend");
        label.setInnerText(this.label);
        return label;
    }

}
