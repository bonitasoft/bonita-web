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

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
class SimpleTable extends Component {

    private final ContainerStyled<TableLine> lines = new ContainerStyled<TableLine>();

    protected ContainerStyled<TableColumn> columns = new ContainerStyled<TableColumn>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LINES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public TableLine getLastLine() {
        return this.lines.getLast();
    }

    public SimpleTable resetLines() {
        this.lines.empty();
        return this;
    }

    public SimpleTable addLine(final TableLine line) {
        this.lines.append(line);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COLUMNS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public SimpleTable addColumn(final TableColumn column) {
        this.columns.append(column);
        return this;
    }

    public TableColumn getLastColumn() {
        return this.columns.get(getLastLine().size());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CELLS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public SimpleTable addCell(final TableCell cell) {
        getLastLine().append(cell);
        return this;
    }

    public TableCell getLastCell() {
        return getLastLine().getLast();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DOM GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Element makeElement() {

        this.element = XML.makeElement(
                HTML.div(new HTMLClass("table"))
                        + HTML.div(new HTMLClass("thead"))
                        + HTML._div()
                );

        // tHead
        this.columns.setRootTag("div", "tr");
        this.element.getChild(0).appendChild(this.columns.getElement());

        // tBody
        this.lines.setRootTag("div", "tbody");
        $(".tbody", this.element).append(this.lines.getElement());

        return this.element;
    }

}
