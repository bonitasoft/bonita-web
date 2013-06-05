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

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

/**
 * @author Séverin Moussel
 * 
 */
abstract public class TableComponent extends Component {

    protected Table table = null;

    public TableComponent() {
        super();
    }

    public TableComponent(final Table table) {
        super();
        this.table = table;
    }

    public TableComponent(final JsId jsid) {
        super(jsid);
    }

    public TableComponent(final Table table, final JsId jsid) {
        this(jsid);
        this.table = table;
    }

    /**
     * @param table
     *            the table to set
     */
    public final TableComponent setTable(final Table table) {
        this.table = table;
        return this;
    }

    @Override
    protected void preProcessHtml() {
        // Fixme : Make the assert work
        // assert this.table == null : "Table mustn't be null";

        super.preProcessHtml();
    }

}
