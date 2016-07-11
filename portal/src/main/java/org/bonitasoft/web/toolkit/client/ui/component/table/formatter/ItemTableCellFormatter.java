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
package org.bonitasoft.web.toolkit.client.ui.component.table.formatter;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.ui.component.table.TableColumn;
import org.bonitasoft.web.toolkit.client.ui.component.table.TableLine;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class ItemTableCellFormatter extends ItemTableFormatter {

    protected TableColumn column;

    protected TableLine line;

    protected AbstractAttributeReader attributeReader;

    protected IItem item;

    /**
     * @return the item
     */
    public IItem getItem() {
        return this.item;
    }

    /**
     * @param item
     *            the item to set
     */
    public void setItem(final IItem item) {
        this.item = item;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(final AbstractAttributeReader attribute) {
        this.attributeReader = attribute;
        if (this.attributeReader instanceof AttributeReader) {
            ((AttributeReader) this.attributeReader).setApplyDefaultModifiers(false);
        }
    }

    /**
     * @return the data
     */
    public AbstractAttributeReader getAttribute() {
        return this.attributeReader;
    }

    /**
     * @return the column
     */
    public TableColumn getColumn() {
        return this.column;
    }

    /**
     * @param tableColumn
     *            the column to set
     */
    public void setColumn(final TableColumn tableColumn) {
        this.column = tableColumn;
    }

    /**
     * @return the line
     */
    public TableLine getLine() {
        return this.line;
    }

    /**
     * @param line
     *            the line to set
     */
    public void setLine(final TableLine line) {
        this.line = line;
    }

}
