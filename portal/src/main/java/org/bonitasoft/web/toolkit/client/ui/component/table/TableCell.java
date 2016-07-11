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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Text;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class TableCell extends ContainerStyled<Node> {

    private String text = null;

    private String columnName = null;

    /**
     * Default Constructor.
     * 
     * @param text
     */
    public TableCell(final String text, final String columnName) {
        super();
        this.text = text;
        this.columnName = columnName;
    }

    /**
     * Default Constructor.
     * 
     * @param components
     */
    public TableCell(final AbstractComponent[] components, final String columnName) {
        super(components);
        this.columnName = columnName;
    }

    @Override
    protected void initTagNames() {
        this.rootTagName = "div";
        this.rootTagClass = "td";
    }

    @Override
    protected Element makeElement() {
        Element rootElement = null;
        if (this.text == null) {
            rootElement = super.makeElement();
            rootElement.addClassName("empty");
        } else {

            rootElement = DOM.createDiv();
            rootElement.addClassName("td");
            if (this.text.isEmpty()) {
                rootElement.addClassName("empty");
            }
            rootElement.setInnerText(this.text);
            if (this.text.length() > Text.NO_TOOLTIP_LIMIT_LENGTH) {
            	rootElement.setTitle(this.text);            	
            }
        }

        rootElement.addClassName("td_" + this.columnName.toLowerCase());

        return rootElement;
    }
}
