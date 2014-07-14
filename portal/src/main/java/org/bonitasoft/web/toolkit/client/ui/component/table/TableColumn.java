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

import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiableOutput;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * @author Séverin Moussel
 * 
 */
public class TableColumn extends TableComponent implements ModifiableOutput {

    private String sortName = null;

    private boolean sortAscending = false;

    private String label = null;

    private boolean sorted = false;

    public TableColumn(final Table table, final JsId jsid, final String label) {
        this(table, jsid, label, null, true);
    }

    public TableColumn(final Table table, final JsId jsid, final String label, final String sortName) {
        this(table, jsid, label, sortName, false, true);
    }

    public TableColumn(final Table table, final JsId jsid, final String label, final String sortName, final boolean sorted) {
        this(table, jsid, label, sortName, sorted, true);
    }

    /**
     * Constructor
     * 
     * @param table
     *            The table containing this column
     * @param jsid
     *            The jsid of the column
     * @param label
     *            The label displayed for this column
     * @param sortName
     *            The name of the attribute to sort on
     * @param sorted
     *            Indicate whether or not the column is currently sorted
     * @param sortAscending
     *            Indicate if the sort is ascending or descending
     */
    public TableColumn(final Table table, final JsId jsid, final String label, final String sortName, final boolean sorted, final boolean sortAscending) {
        super(table, jsid);
        this.label = label;
        this.sortName = sortName;
        this.sorted = sorted;
        this.sortAscending = sortAscending;
    }

    /**
     * @see Component#makeElement()
     * @return This method returns the generated element
     */
    @Override
    protected final Element makeElement() {
        final Element th = DOM.createElement("div");
        th.addClassName("th " + getJsId().toString("th"));
        th.setInnerText(this.label);

        // Sort on columns
        if (this.sortName != null) {
            th.addClassName("sortable");
            DOM.sinkEvents(th, Event.ONCLICK);
            DOM.setEventListener(th, new Action() {

                @Override
                public void execute() {
                    if (TableColumn.this.sorted) {
                        TableColumn.this.sortAscending = !TableColumn.this.sortAscending;
                    }
                    TableColumn.this.table.setOrder(TableColumn.this.sortName, TableColumn.this.sortAscending);

                    TableColumn.this.setSorted(true);

                }
            });
        }

        return th;
    }

    /**
     * Define if the element is currently sorted
     * 
     * @param sorted
     *            TRUE if sorted, otherwise FALSE
     */
    public final void setSorted(final boolean sorted) {
        this.sorted = sorted;

        if (isGenerated()) {
            if (this.sorted) {
                getElement().addClassName("sorted");
                if (isSortAscending()) {
                    getElement().addClassName("asc");
                } else {
                    getElement().addClassName("desc");
                }
            } else {
                getElement().removeClassName("sorted");
            }
        }
    }

    public void setSortAscending(final boolean asc) {
        this.sortAscending = asc;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MODIFIERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * List of modifiers to apply on output
     */
    private final ModifiersList outputModifiers = new ModifiersList();

    /**
     * @see ModifiableOutput#getOutputModifiers()
     */
    @Override
    public final List<Modifier> getOutputModifiers() {
        return this.outputModifiers.getModifiers();
    }

    /**
     * @see ModifiableOutput#addOutputModifier(Modifier)
     */
    @Override
    public final TableColumn addOutputModifier(final Modifier modifier) {
        this.outputModifiers.addModifier(modifier);
        return this;
    }

    /**
     * @see ModifiableOutput#addOutputModifiers(List)
     */
    @Override
    public final TableColumn addOutputModifiers(final List<Modifier> modifiers) {
        this.outputModifiers.addModifiers(modifiers);
        return this;
    }

    /**
     * @see ModifiableOutput#removeOutputModifier(String)
     */
    @Override
    public final TableColumn removeOutputModifier(final String modifierClassName) {
        this.outputModifiers.removeModifier(modifierClassName);

        return this;
    }

    /**
     * @see ModifiableOutput#hasOutputModifier(String)
     */
    @Override
    public final boolean hasOutputModifier(final String modifierClassName) {
        return this.outputModifiers.hasModifier(modifierClassName);
    }

    /**
     * @see ModifiableOutput#getOutputModifier(String)
     */
    @Override
    public final Modifier getOutputModifier(final String modifierClassName) {
        return this.outputModifiers.getModifier(modifierClassName);
    }

    /**
     * check if the column is currently sorted
     */
    public final boolean getSorted() {
        return this.sorted;
    }

    /**
     * Get the SortName
     */
    public final String getSortName() {
        return this.sortName;
    }

    /**
     * @return the sortAscending
     */
    public final boolean isSortAscending() {
        return this.sortAscending;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Check if this column can be sorted.
     * 
     * @return This method returns TRUE if the column can be sorted, otherwise FALSE.
     */
    public final boolean isSortable() {
        // If sort.getSortName() is NULL, that means the column can't be sorted
        return getSortName() != null;
    }
}
