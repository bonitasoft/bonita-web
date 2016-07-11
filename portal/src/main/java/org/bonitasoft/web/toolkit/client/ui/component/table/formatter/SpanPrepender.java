/**
 * Copyright (C) 2012 BonitaSoft S.A.
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

import org.bonitasoft.web.toolkit.client.ui.component.Image;
import org.bonitasoft.web.toolkit.client.ui.component.Span;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

/**
 * @author Colin PUY
 * 
 * Add a span before attribute value in itemTable
 */
public class SpanPrepender extends DefaultItemTableCellFormatter {

    private String prefix;

    public SpanPrepender(String prefix) {
        this.prefix = prefix;
    }
    
    protected void makeBooleanCell() {
        this.table.addCell(newPrefixSpan(), new Span(getBooleanText(getBooleanValue())));
    }

    protected void makeStringCell() {
        this.table.addCell(newPrefixSpan(), new Span(getStringText()));
    }
    
    protected void makeImageCell() {
        this.table.addCell(newPrefixSpan(), new Image(this.attributeReader.read(this.item), 0, 0, ""));
    }
    
    protected void makeEnumCell() {
        this.table.addCell(newPrefixSpan(), new Span(getEnumText(getStringText())));
    }
    
    protected void makeDateCell() {
        this.table.addCell(newPrefixSpan(), new Span(getDateText()));
    }
    
    protected void makeDateTimeCell() {
        this.table.addCell(newPrefixSpan(), new Span(getDateTimeText()));
    }

    protected Component newPrefixSpan() {
        return new Span(prefix).addClass("prepend");
    }
}
