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
package org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage;

import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Html;
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemDetailsMetadata {

    private AbstractAttributeReader attributeReader;

    private String label;

    private String tooltip;

    private JsId jsId;

    private Html representation;

    public ItemDetailsMetadata(final AbstractAttributeReader attributeReader, final String label, final String tooltip) {
        this.attributeReader = attributeReader;
        this.label = label;
        this.tooltip = tooltip;
    }

    public ItemDetailsMetadata(final String attributeName, final String label, final String tooltip) {
        this(new AttributeReader(attributeName), label, tooltip);
    }

    public ItemDetailsMetadata(final JsId id, final AbstractAttributeReader attributeReader, final String label, final String tooltip) {
        this(attributeReader, label, tooltip);
        this.jsId = id;
    }

    public ItemDetailsMetadata(final String attributeName, Html html) {
        this.attributeReader = new AttributeReader(attributeName);
        representation = html;
    }

    /**
     * @return the attributeName
     */
    public AbstractAttributeReader getAttributeReader() {
        return this.attributeReader;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return this.tooltip;
    }

    /**
     * @return the jsid
     */
    public JsId getJsId() {
        return this.jsId;
    }

    public Node createDefinition(final String cssClass, final String value) {
        if(representation != null) {
            return representation;
        }
        final Definition definition = new Definition(getLabel() + ": ", getTooltip(), value);
        if (cssClass != null) {
            definition.addClass(cssClass);
        }
        return definition;
    }
}
