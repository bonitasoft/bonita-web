/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.model;

import java.io.Serializable;

/**
 * Widget position for widgets inside groups
 * @author Anthony Birembaut
 */
public class WidgetPosition implements Serializable {
    
    /**
     * UID
     */
    private static final long serialVersionUID = 667696206201971953L;

    private int rowInGroup;
    
    private int columnInGroup;
    
    private int rowSpan;
    
    private int colSpan;

    private String cellStyles;

    /**
     * Constructor
     * @param rowInGroup
     * @param columnInGroup
     * @param rowSpan
     * @param colSpan
     * @param cellStyles
     */
    public WidgetPosition(final int rowInGroup, final int columnInGroup, final int rowSpan, final int colSpan, final String cellStyles) {
        this.rowInGroup = rowInGroup;
        this.columnInGroup = columnInGroup;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
        this.cellStyles = cellStyles;
    }
    
    /**
     * Default Constructor
     */
    public WidgetPosition() {
        super();
        // Mandatory for serialization
    }

    public int getRowInGroup() {
        return rowInGroup;
    }

    public void setRowInGroup(final int rowInGroup) {
        this.rowInGroup = rowInGroup;
    }

    public int getColumnInGroup() {
        return columnInGroup;
    }

    public void setColumnInGroup(final int columnInGroup) {
        this.columnInGroup = columnInGroup;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(final int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(final int colSpan) {
        this.colSpan = colSpan;
    }

    public String getCellStyles() {
        return cellStyles;
    }

    public void setCellStyles(final String cellStyles) {
        this.cellStyles = cellStyles;
    }
}
