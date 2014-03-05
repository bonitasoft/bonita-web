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
package org.bonitasoft.console.client.common.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;
import org.bonitasoft.web.toolkit.client.common.TreeNode;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.AutoCompleteEntry;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class SelectItemAndDoForm extends Component {

    private final List<SelectItemAndDoEntry> entries;

    private final String submitLabel;

    private final String submitTooltip;

    private final Action callback;

    private TreeIndexed<String> hiddenEntries = new TreeIndexed<String>();
    
    public SelectItemAndDoForm(final List<SelectItemAndDoEntry> entries, final String submitLabel, final String submitTooltip, final Action callback) {
        super();
        this.entries = entries;
        this.submitLabel = submitLabel;
        this.submitTooltip = submitTooltip;
        this.callback = callback;
    }

    public SelectItemAndDoForm(final String submitLabel, final String submitTooltip, final Action callback) {
        this(new ArrayList<SelectItemAndDoEntry>(), submitLabel, submitTooltip, callback);
    }

    public SelectItemAndDoForm addEntry(final SelectItemAndDoEntry entry) {
        this.entries.add(entry);
        return this;
    }

    public SelectItemAndDoForm addHiddenEntry(final String name, final String value) {
        this.hiddenEntries.addValue(name, value);
        return this;
    }

    public SelectItemAndDoForm addHiddenEntries(final Map<String, String> entries) {
        this.hiddenEntries.addValues(entries);
        return this;
    }

    public SelectItemAndDoForm setHiddenEntries(final TreeIndexed<String> entries) {
        if (entries != null) {
            this.hiddenEntries = entries.copy();
        } else {
            this.hiddenEntries.clear();
        }
        return this;
    }

    private void addHiddenEntriesToForm(final Form form, final TreeIndexed<String> params) {
        for (final Entry<String, AbstractTreeNode<String>> entry : this.hiddenEntries.getNodes().entrySet()) {
            if (entry.getValue() instanceof TreeLeaf<?>) {
                form.addHiddenEntry(entry.getKey(), ((TreeLeaf<String>) entry.getValue()).getValue());
            } else if (entry.getValue() instanceof TreeNode<?>) {
                form.addHiddenEntry(entry.getKey(), (TreeNode<String>) entry.getValue());
            }
            /*
             * DANGEROUS: what happens if we have twice the same key?!
             * else if (entry.getValue() instanceof TreeIndexed<?>) {
             * addHiddenEntriesToForm(form, (TreeIndexed<String>) entry.getValue());
             * }
             */
        }
    }
    

    @Override
    protected Element makeElement() {
        final Form form = new Form();

        addHiddenEntriesToForm(form, this.hiddenEntries);

        for (final SelectItemAndDoEntry entry : this.entries) {
            AutoCompleteEntry autoCompleteEntry = new AutoCompleteEntry(new JsId(entry.getName()), entry.getLabel(), 
                    entry.getTooltip(), entry.getItemDefinition(), entry.getSuggestionLabel(), 
                    entry.getSuggestionValueAttributeName(), null);
            for (Entry<String, String> filter : entry.getFilters().entrySet()) {
                autoCompleteEntry.addFilter(filter.getKey(), filter.getValue());
            }
            form.addEntry(autoCompleteEntry);
            form.getEntry(new JsId(entry.getName())).addValidator(new MandatoryValidator());
        }

        form
                .addButton(
                        new JsId("submit"),
                        this.submitLabel,
                        this.submitTooltip,
                        this.callback)
                .addCancelButton();
        return form.getElement();
    }

}
