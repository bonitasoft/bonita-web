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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;

/**
 * @author Séverin Moussel
 */
public class Tab extends Fieldset {

    private boolean tabVisibility = true;

    private Element header;

    /**
     * Default Constructor.
     * 
     * @param title
     */
    public Tab(final String title) {
        super(null, title);
        setRootTagName("div");
    }

    @Override
    protected void postProcessHtml() {
        this.element.addClassName("body");

        final Element newRoot = DOM.createElement("div");
        newRoot.addClassName("tab");
        newRoot.appendChild(this.element);

        this.element = newRoot;

        super.postProcessHtml();
    }

    public void setTabVisibility(boolean visibility) {
        this.tabVisibility = visibility;
        if(header != null) {
            UIObject.setVisible(header, tabVisibility);
        }
    }

    @Override
    protected Element makeLabel() {
        header = XML.makeElement(
                HTML.div(new HTMLClass("header"))
                        + HTML.h1() + this.label + HTML._h1()
                        + HTML._div()
        );
        UIObject.setVisible(header, tabVisibility);
        return header;
    }
}
