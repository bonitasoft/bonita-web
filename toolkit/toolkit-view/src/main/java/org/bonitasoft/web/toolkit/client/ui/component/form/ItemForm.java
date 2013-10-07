/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.ui.component.form;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Date;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.form.AddItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.action.form.UpdateItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.AutoCompleteEntry;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Option;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.ValuedFormEntry;

import com.google.gwt.user.client.Element;

/**
 * @author Julien Mege
 */

// FIXME this is not an AbstractForm since this object include his own form 
// FIXME need to extend Form and then remove all reference to form class variable.
public class ItemForm<T extends IItem> extends AbstractForm {

    private Form form = null;

    private ItemDefinition itemDefinition = null;

    private String itemId = null;

    public ItemForm(final JsId jsid, final ItemDefinition itemDefinition) {
        super(jsid);
        this.itemDefinition = itemDefinition;
        this.form = new Form(jsid);
    }

    public ItemForm(final JsId jsid, final ItemDefinition itemDefinition, final String itemId) {
        super(jsid);
        this.itemDefinition = itemDefinition;
        if (itemId != null) {
            this.setItemId(itemId);
            this.form = new Form(jsid);

            setFiller(new ItemFormFiller(this, itemId));
        } else {
            this.form = new Form(jsid);
        }
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    private void initValidatorsAndModifiers(final JsId jsid, final String attributeName) {
        final ValuedFormEntry entry = this.form.getEntry(jsid);

        entry.addValidators(this.itemDefinition.getAttribute(attributeName).getValidators());
        entry.addInputModifiers(this.itemDefinition.getAttribute(attributeName).getInputModifiers());
        entry.addOutputModifiers(this.itemDefinition.getAttribute(attributeName).getOutputModifiers());

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ENTRIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemForm<T> addEntry(final String attributeName, final String label, final String tooltip) {
        this.addEntry(attributeName, label, tooltip, null, null, null);
        return this;
    }

    public ItemForm<T> addEntry(final String attributeName, final String label, final String tooltip, final String defaultValue) {
        this.addEntry(attributeName, label, tooltip, defaultValue, null, null);
        return this;
    }

    public ItemForm<T> addEntry(final String attributeName, final String label, final String tooltip, final String defaultValue, final String description) {
        this.addEntry(attributeName, label, tooltip, defaultValue, description, null);
        return this;
    }

    public ItemForm<T> addEntry(final String attributeName, final String label, final String tooltip, final String defaultValue, final String description,
            final String example) {
        this.form.addItemAttributeEntry(this.itemDefinition.getAttribute(attributeName), label, tooltip, defaultValue, description, example);

        // Not needed already set in Form
        // initValidatorsAndModifiers(this.jsid, attributeName);

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HIDDEN ENTRIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemForm<T> addHiddenEntry(final String attributeName, final String value) {
        this.form.addHidden(attributeName, value);
        return this;
    }

    public ItemForm<T> addHiddenEntry(final String attributeName, final APIID value) {
        return this.addHiddenEntry(attributeName, value.toString());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SELECT ENTRIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemForm<T> addEntryAsSelect(final String attributeName, final String label, final String tooltip, final String description) {
        this.addEntryAsSelect(attributeName, label, tooltip, description, (Option[]) null);
        return this;
    }

    public ItemForm<T> addEntryAsSelect(final String attributeName, final String label, final String tooltip, final Option... options) {
        this.addEntryAsSelect(attributeName, label, tooltip, null, options);
        return this;
    }

    public ItemForm<T> addEntryAsSelect(final String attributeName, final String label, final String tooltip, final String description, final Option... options) {

        final JsId jsid = new JsId(attributeName);
        this.form.addSelectEntry(jsid, label, tooltip, description, options);

        initValidatorsAndModifiers(jsid, attributeName);

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AUTO COMPLETE ENTRIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemForm<T> addEntryAsAutoComplete(final String attributeName, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final String labelAttributeName, final String valueAttributeName) {
        this.addEntryAsAutoComplete(attributeName, label, tooltip, itemDefinition, labelAttributeName, valueAttributeName, null);
        return this;
    }

    public ItemForm<T> addEntryAsAutoComplete(final String attributeName, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final String labelAttributeName, final String valueAttributeName, final String description) {
        final JsId jsid = new JsId(attributeName);

        AutoCompleteEntry autoComplete = 
                new AutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelAttributeName, valueAttributeName, description);
        this.form.addEntry(autoComplete);

        initValidatorsAndModifiers(jsid, attributeName);

        return this;
    }

    public ItemForm<T> addEntryAsAutoComplete(final String attributeName, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final AbstractAttributeReader labelTemplate, final String valueAttributeName) {
        this.addEntryAsAutoComplete(attributeName, label, tooltip, itemDefinition, labelTemplate, valueAttributeName, null);
        return this;
    }

    public ItemForm<T> addEntryAsAutoComplete(final String attributeName, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final AbstractAttributeReader labelTemplate, final String valueAttributeName, final String description) {

        final JsId jsid = new JsId(attributeName);
        AutoCompleteEntry autoComplete = 
                new AutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelTemplate, valueAttributeName, description);
        this.form.addEntry(autoComplete);

        initValidatorsAndModifiers(jsid, attributeName);

        return this;
    }

    public void addEntry(final FormNode entry) {
        form.addEntry(entry);
    }
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATIC ENTRIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemForm<T> addEntryAsStatic(final String attributeName, final String label, final String tooltip) {
        this.addEntryAsStatic(attributeName, label, tooltip, null, null, null);
        return this;
    }

    public ItemForm<T> addEntryAsStatic(final String attributeName, final String label, final String tooltip, final String defaultValue) {
        this.addEntryAsStatic(attributeName, label, tooltip, defaultValue, null, null);
        return this;
    }

    public ItemForm<T> addEntryAsStatic(final String attributeName, final String label, final String tooltip, final String defaultValue,
            final String description) {
        this.addEntryAsStatic(attributeName, label, tooltip, defaultValue, description, null);
        return this;
    }

    public ItemForm<T> addEntryAsStatic(final String attributeName, final String label, final String tooltip, final String defaultValue,
            final String description, final String example) {
        this.form.addItemAttributeStaticEntry(this.itemDefinition.getAttribute(attributeName), label, tooltip, defaultValue, description, example);

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DATEPICKER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemForm<T> addEntryAsDatePicker(final String attributeName, final String label, final String tooltip, final Date startDate, final Date endDate) {
        this.addEntryAsDatePicker(attributeName, label, tooltip, null, null, startDate, endDate);
        return this;
    }

    public ItemForm<T> addEntryAsDatePicker(final String attributeName, final String label, final String tooltip, final String defaultValue,
            final Date startDate, final Date endDate) {
        this.addEntryAsDatePicker(attributeName, label, tooltip, defaultValue, null, null, startDate, endDate);
        return this;
    }

    public ItemForm<T> addEntryAsDatePicker(final String attributeName, final String label, final String tooltip, final String defaultValue,
            final String description, final Date startDate, final Date endDate) {
        this.addEntryAsDatePicker(attributeName, label, tooltip, defaultValue, description, null, startDate, endDate);
        return this;
    }

    public ItemForm<T> addEntryAsDatePicker(final String attributeName, final String label, final String tooltip, final String defaultValue,
            final String description, final String example, final Date startDate, final Date endDate) {
        final JsId jsid = new JsId(attributeName);
        this.form.addDatePickerEntry(jsid, label, tooltip, defaultValue, description, example, startDate,
                endDate);
        initValidatorsAndModifiers(jsid, attributeName);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OUTPUT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void buildActions() {
        if (this.form.hasNonStaticEntry()) {
            if (this.itemId == null) {
                final AddItemFormAction<T> addAction = new AddItemFormAction<T>(this.itemDefinition);
                addAction.setForm(this.form);
                this.form.addButton(new JsId("addActionForm"), _("Add"), _("Create a new element"), addAction);
            } else {
                this.form.addHiddenEntry("id", this.itemId);

                final UpdateItemFormAction<T> updateAction = new UpdateItemFormAction<T>(this.itemDefinition);
                updateAction.setForm(this.form);
                this.form.addButton(new JsId("updateActionForm"), _("Save"), _("Save this element"), updateAction);
            }
            this.form.addCancelButton();
        }
    }

    @Override
    protected Element makeElement() {
        this.buildActions();
        return this.form.getElement();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // RETRIEVE VALUES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getEntryValue(final JsId jsid) {
        return this.form.getEntryValue(jsid);
    }

    @Override
    public String getEntryValue(final String jsid) {
        return this.form.getEntryValue(jsid);
    }

    @Override
    public void setEntryValue(final String name, final String value) {
        this.form.setEntryValue(name, value);
    }

    @Override
    public ValuedFormEntry getEntry(final JsId jsid) {
        return this.form.getEntry(jsid);
    }

    @Override
    public void setValues(final Map<String, String> values) {
        this.form.setValues(values);
    }

    /**
     * @return the itemDefinition
     */
    public ItemDefinition getItemDefinition() {
        return this.itemDefinition;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setJson(final String json) {
        this.form.setJson(json);
    }

    @Override
    public boolean hasNonStaticEntry() {
        return this.form.hasNonStaticEntry();
    }

    @Override
    public String toJson() {
        return this.form.toJson();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemForm addValidator(final JsId entryJsId, final Validator validator) {
        return (ItemForm) super.addValidator(entryJsId, validator);
    }

    @Override
    public ItemForm addValidator(final String entryJsId, final Validator validator) {
        return (ItemForm) super.addValidator(entryJsId, validator);
    }
}
