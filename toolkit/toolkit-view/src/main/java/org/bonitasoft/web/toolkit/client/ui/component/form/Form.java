/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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

import java.util.Date;
import java.util.List;

import org.bonitasoft.web.toolkit.client.common.TreeNode;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidationError;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidationException;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorEngine;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.DateFormatModifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.DateToDisplayModifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;
import org.bonitasoft.web.toolkit.client.ui.component.event.InputCompleteEvent;
import org.bonitasoft.web.toolkit.client.ui.component.event.InputCompleteHandler;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormButtonCancel;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormDisabledSubmitButton;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormSubmitButton;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.AutoCompleteEntry;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Checkbox;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.ColorPickerEntry;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.DatePickerEntry;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Fieldset;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FieldsetCheckbox;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FieldsetRadio;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FormEntry;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Option;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Password;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Radio;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Select;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.StaticText;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Tab;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Text;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Textarea;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.ValuedFormEntry;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * This class allow to manually create forms
 * 
 * @author Séverin Moussel
 */
public class Form extends AbstractForm {

    public Form(final JsId jsid, final FormFiller filler) {
        super(jsid);
        setFiller(filler);
    }

    public Form(final JsId jsid) {
        super(jsid);
    }

    public Form(final FormFiller filler) {
        this(null, filler);
    }

    public Form() {
        super(null);
    }

    @Override
    public Form resetEntries() {
        return (Form) super.resetEntries();
    }

    public Form addFormEntry(FormEntry entry) {
        super.addEntry(entry);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HIDDEN
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form addHiddenEntry(final String name, final String value) {
        this.addHidden(name, value);
        return this;
    }

    public Form addHiddenEntry(final String name, final List<String> values) {
        this.addHidden(name, values);
        return this;
    }

    public Form addHiddenEntry(final String name, final TreeNode<String> values) {
        this.addHidden(name, values);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TEXT INPUT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a single line text input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextEntry(final JsId jsid, final String label, final String tooltip) {
        return this.addTextEntry(jsid, label, tooltip, null, null, null);
    }

    /**
     * Add a single line text input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        return this.addTextEntry(jsid, label, tooltip, defaultValue, null, null);
    }

    /**
     * Add a single line text input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextEntryWithPlaceholder(final JsId jsid, final String label, final String tooltip, final String placeholder) {
        final Text textWithPlaceholder = new Text(jsid, label, tooltip, null, null, null);
        textWithPlaceholder.setPlaceholder(placeholder);
        addEntry(textWithPlaceholder);
        return this;
    }

    /**
     * Add a single line text input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        return this.addTextEntry(jsid, label, tooltip, defaultValue, description, null);
    }

    /**
     * Add a single line text input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @param example
     *            (Optional) An example of valid input.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final String example) {

        addEntry(new Text(jsid, label, tooltip, defaultValue, description, example));

        return this;
    }

    public Form addTextEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final String example, final Long maxLength) {

        addEntry(new Text(jsid, label, tooltip, defaultValue, description, example, maxLength));

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATIC
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a single line Static text
     * 
     * @param jsid
     *            The JsId of the Static text
     * @param label
     *            The label to show beside the Static text
     * @param tooltip
     *            The tooltip that will quickly explain the Static text
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addStaticTextEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        return this.addStaticTextEntry(jsid, label, tooltip, defaultValue, null, null);
    }

    /**
     * Add a single line static text
     * 
     * @param jsid
     *            The JsId of the Static text
     * @param label
     *            The label to show beside the Static text
     * @param tooltip
     *            The tooltip that will quickly explain the Static text
     * @param description
     *            (Optional) A more complete description on how to fill the Static text and what it is for.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addStaticTextEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        return this.addStaticTextEntry(jsid, label, tooltip, defaultValue, description, null);
    }

    /**
     * Add a single line static text
     * 
     * @param jsid
     *            The JsId of the Static text
     * @param label
     *            The label to show beside the Static text
     * @param tooltip
     *            The tooltip that will quickly explain the Static text
     * @param description
     *            (Optional) A more complete description on how to fill the Static text and what it is for.
     * @param example
     *            (Optional) An example of valid Static text.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addStaticTextEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final String example) {
        addEntry(new StaticText(jsid, label, tooltip, defaultValue, description, example));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PASSWORD
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a simple obfuscated password input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addPasswordEntry(final JsId jsid, final String label, final String tooltip) {
        return this.addPasswordEntry(jsid, label, tooltip, null, null);
    }

    /**
     * Add a simple obfuscated password input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addPasswordEntry(final JsId jsid, final String label, final String tooltip, final String description) {
        return this.addPasswordEntry(jsid, label, tooltip, description, null);
    }

    /**
     * Add a simple obfuscated password input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @param example
     *            (Optional) An example of valid input.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addPasswordEntry(final JsId jsid, final String label, final String tooltip, final String description, final String example) {
        addEntry(new Password(jsid, label, tooltip, description, example));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TEXTAREA
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a plain text textarea
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextareaEntry(final JsId jsid, final String label, final String tooltip) {
        return this.addTextareaEntry(jsid, label, tooltip, null, null, null);
    }

    /**
     * Add a plain text textarea
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param defaultValue
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextareaEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        return this.addTextareaEntry(jsid, label, tooltip, defaultValue, null, null);
    }

    /**
     * Add a plain text textarea
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextareaEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        return this.addTextareaEntry(jsid, label, tooltip, defaultValue, description, null);
    }

    /**
     * Add a plain text textarea
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @param example
     *            (Optional) An example of valid input.
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addTextareaEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final String example) {
        addEntry(new Textarea(jsid, label, tooltip, defaultValue, description, example));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // RADIO BUTTON
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a radio button. To have multiple radio button switching each other, create them using the same JsId.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the radio is checked
     * @return This method return the form itself to allow cascading calls
     */
    public Form addRadioEntry(final JsId jsid, final String label, final String tooltip, final String value) {
        return this.addRadioEntry(jsid, label, tooltip, value, null, false);
    }

    /**
     * Add a radio button. To have multiple radio button switching each other, create them using the same JsId.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the radio is checked
     * @param description
     * @return This method return the form itself to allow cascading calls
     */
    public Form addRadioEntry(final JsId jsid, final String label, final String tooltip, final String value, final String description) {
        return this.addRadioEntry(jsid, label, tooltip, value, description, false);
    }

    /**
     * Add a radio button. To have multiple radio button switching each other, create them using the same JsId.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the radio is checked
     * @param checked
     * @return This method return the form itself to allow cascading calls
     */
    public Form addRadioEntry(final JsId jsid, final String label, final String tooltip, final String value, final boolean checked) {
        return this.addRadioEntry(jsid, label, tooltip, value, null, checked);
    }

    /**
     * Add a radio button. To have multiple radio button switching each other, create them using the same JsId.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the radio is checked
     * @param description
     * @param checked
     * @return This method return the form itself to allow cascading calls
     */
    public Form addRadioEntry(final JsId jsid, final String label, final String tooltip, final String value, final String description, final boolean checked) {
        addEntry(new Radio(jsid, label, tooltip, value, description, checked));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CHECKBOX
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a checkBox.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the checkbox is checked
     * @return This method return the form itself to allow cascading calls
     */
    public Form addCheckboxEntry(final JsId jsid, final String label, final String tooltip, final String value) {
        return this.addCheckboxEntry(jsid, label, tooltip, value, null, false);
    }

    /**
     * Add a checkBox.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the checkbox is checked
     * @param description
     * @return This method return the form itself to allow cascading calls
     */
    public Form addCheckboxEntry(final JsId jsid, final String label, final String tooltip, final String value, final String description) {
        return this.addCheckboxEntry(jsid, label, tooltip, value, description, false);
    }

    /**
     * Add a checkBox.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the checkbox is checked
     * @param checked
     * @return This method return the form itself to allow cascading calls
     */
    public Form addCheckboxEntry(final JsId jsid, final String label, final String tooltip, final String value, final boolean checked) {
        return this.addCheckboxEntry(jsid, label, tooltip, value, null, checked);
    }

    /**
     * Add a checkBox.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param value
     *            The value returned if the checkbox is checked
     * @param description
     * @param checked
     * @return This method return the form itself to allow cascading calls
     */
    public Form addCheckboxEntry(final JsId jsid, final String label, final String tooltip, final String value, final String description, final boolean checked) {
        addEntry(new Checkbox(jsid, label, tooltip, value, description, checked));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // BUTTON
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @author Séverin Moussel
     * 
     */
    public final class FormSubmitAction extends Action {

        private final Action action;

        public FormSubmitAction(final Action action) {
            this.action = action;
        }

        @Override
        public void execute() {
            if (isUploadFinished(getElement())) {
                try {
                    validate();
                    // TODO find a better way to unactivate the double click
                    // action.setStarted(true);
                    action.execute();
                } catch (ValidationException e) {
                    for (final ValidationError error : e.getErrors()) {
                        Form.this.addError(new JsId(error.getAttributeName()), error.getMessageTemplate());
                    }
                }
            }
        }

        private native boolean isUploadFinished(Element form)
        /*-{
            return $wnd.$(form).autoUploader('finished');
        }-*/;
    }

    /**
     * Add an action to the form
     * 
     * @param jsid
     * @param label
     *            The label to show in the button
     * @param tooltip
     *            The tooltip that will quickly explain the action
     * @param actionHandler
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addDisabledButton(final JsId jsid, final String label, final String tooltip, final Action actionHandler) {
        if (actionHandler instanceof FormAction) {
            ((FormAction) actionHandler).setForm(this);
        }

        super.addAction(new FormDisabledSubmitButton(jsid, label, tooltip, new FormSubmitAction(actionHandler)));
        return this;
    }

    /**
     * Add an action to the form
     * 
     * @param jsid
     * @param label
     *            The label to show in the button
     * @param tooltip
     *            The tooltip that will quickly explain the action
     * @param actionHandler
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addButton(final JsId jsid, final String label, final String tooltip, final Action actionHandler) {
        if (actionHandler instanceof FormAction) {
            ((FormAction) actionHandler).setForm(this);
        }

        super.addAction(new FormSubmitButton(jsid, label, tooltip, new FormSubmitAction(actionHandler)));
        return this;
    }

    /**
     * Add an action to the form
     * 
     * @param label
     *            The label to show in the button
     * @param tooltip
     *            The tooltip that will quickly explain the action
     * @param actionHandler
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addButton(final String label, final String tooltip, final Action actionHandler) {
        return this.addButton(JsId.getRandom(), label, tooltip, actionHandler);
    }

    public Form addCancelButton() {
        super.addAction(new FormButtonCancel());
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FIELDSETS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form openRadioFieldset(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        return this.openRadioFieldset(jsid, label, tooltip, defaultValue, false);
    }

    public Form openRadioFieldset(final JsId jsid, final String label, final String tooltip, final String defaultValue, final boolean checked) {
        return (Form) openSection(new FieldsetRadio(jsid, label, tooltip, defaultValue, checked));
    }

    public Form openCheckboxFieldset(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        return this.openCheckboxFieldset(jsid, label, tooltip, defaultValue, false);
    }

    public Form openCheckboxFieldset(final JsId jsid, final String label, final String tooltip, final String defaultValue, final boolean checked) {
        return (Form) openSection(new FieldsetCheckbox(jsid, label, tooltip, defaultValue, checked));
    }

    public Form openFieldset(final JsId jsid, final String label) {
        return (Form) openSection(new Fieldset(jsid, label));
    }

    public Form openFieldset(final JsId jsid) {
        return this.openFieldset(jsid, null);
    }

    public Form closeFieldset() {
        for (int i = containers.size() - 1; i > 0; i--) {
            if (containers.get(i) instanceof Fieldset) {
                while (containers.size() > 2 && !(containers.lastElement() instanceof Fieldset)) {
                    closeSection();
                }
                closeSection();
                break;
            }
        }

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TABS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form openTab(final String label) {
        return (Form) openSection(new Tab(label));
    }

    public Form closeTab() {
        for (int i = containers.size() - 1; i > 0; i--) {
            if (containers.get(i) instanceof Tab) {
                while (containers.size() > 2 && !(containers.lastElement() instanceof Tab)) {
                    closeSection();
                }
                closeSection();
                break;
            }
        }

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PAGES (WIZARD)
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form openPage(final String title) {
        return this.openPage(null, title, null, null, null, null);
    }

    public Form openPage(final JsId jsid, final String title) {

        return this.openPage(jsid, title, null, null, null, null);
    }

    public Form openPage(final String title, final Action onNextAction) {
        return this.openPage(null, title, onNextAction, null, null, null);
    }

    public Form openPage(final JsId jsid, final String title, final Action onNextAction) {
        return this.openPage(jsid, title, onNextAction, null, null, null);
    }

    public Form openPage(final String title, final Action onNextAction, final Action onPreviousAction, final Action onCancelAction, final Action onFinishAction) {
        return this.openPage(null, title, onNextAction, onPreviousAction, onCancelAction, onFinishAction);
    }

    public Form openPage(final JsId jsid, final String title, final Action onNextAction, final Action onPreviousAction, final Action onCancelAction,
            final Action onFinishAction) {
        closePage();
        return (Form) openSection(new Page(jsid, title, onNextAction, onPreviousAction, onCancelAction, onFinishAction));
    }

    private Form closePage() {
        for (int i = containers.size() - 1; i > 0; i--) {
            if (containers.get(i) instanceof Page) {
                while (containers.size() > 2 && !(containers.lastElement() instanceof Page)) {
                    closeSection();
                }
                closeSection();
                break;
            }
        }

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILE UPLOAD
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a simple obfuscated File input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @return This function returns the form itself in order to allow cascading calls
     */
    public Form addFileEntry(final JsId jsid, final String label, final String tooltip, final String submissionUrl) {
        return this.addFileEntry(jsid, label, tooltip, null, null, submissionUrl);
    }

    /**
     * Add a simple obfuscated File input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @return This function returns the form itself in order to allow cascading calls
     * 
     * @deprecated Use {@link #addEntry(new FileUpload())}
     */
    @Deprecated
    public Form addFileEntry(final JsId jsid, final String label, final String tooltip, final String description, final String submissionUrl) {
        return this.addFileEntry(jsid, label, tooltip, description, null, submissionUrl);
    }

    /**
     * Add a simple obfuscated File input
     * 
     * @param jsid
     *            The JsId of the input
     * @param label
     *            The label to show beside the input
     * @param tooltip
     *            The tooltip that will quickly explain the entry
     * @param description
     *            (Optional) A more complete description on how to fill the input and what it is for.
     * @param example
     *            (Optional) An example of valid input.
     * @return This function returns the form itself in order to allow cascading calls
     * 
     * @deprecated Use {@link #addEntry(new FileUpload())}
     */
    @Deprecated
    public Form addFileEntry(final JsId jsid, final String label, final String tooltip, final String description, final String example,
            final String submissionUrl) {
        addEntry(new FileUpload(submissionUrl, jsid, label, tooltip, description, example));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SELECT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form addSelectEntry(final JsId jsid, final String label, final String tooltip) {
        this.addSelectEntry(jsid, label, tooltip, null, (Option[]) null);
        return this;
    }

    public Form addSelectEntry(final JsId jsid, final String label, final String tooltip, final String description) {
        this.addSelectEntry(jsid, label, tooltip, description, (Option[]) null);
        return this;
    }

    public Form addSelectEntry(final JsId jsid, final String label, final String tooltip, final Option... options) {
        this.addSelectEntry(jsid, label, tooltip, null, options);
        return this;
    }

    public Form addSelectEntry(final JsId jsid, final String label, final String tooltip, final String description, final Option... options) {
        addEntry(new Select(jsid, label, tooltip, description, options));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AUTOCOMPLETE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @deprecated use addEntry(new {@link AutoCompleteEntry}(...))
     */
    @Deprecated
    public Form addAutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final String labelAttributeName, final String valueAttributeName) {
        return this.addAutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelAttributeName, valueAttributeName, null, null);
    }

    /**
     * @deprecated use addEntry(new {@link AutoCompleteEntry}(...))
     */
    @Deprecated
    public Form addAutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final String labelAttributeName, final String valueAttributeName, final String defaultValue) {
        return this.addAutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelAttributeName, valueAttributeName, defaultValue, null);
    }

    /**
     * @deprecated use addEntry(new {@link AutoCompleteEntry}(...))
     */
    @Deprecated
    public Form addAutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final String labelAttributeName, final String valueAttributeName, final String defaultValue, final String description) {
        AutoCompleteEntry entry = new AutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelAttributeName, valueAttributeName, description);
        entry.addInputHandler(createInputCompleteHandler(), InputCompleteEvent.TYPE);
        addEntry(entry);
        return this;
    }

    /**
     * @deprecated use addEntry(new {@link AutoCompleteEntry}(...))
     */
    @Deprecated
    public Form addAutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final AbstractAttributeReader labelTemplate, final String valueAttributeName) {
        return this.addAutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelTemplate, valueAttributeName, null, null);
    }

    /**
     * @deprecated use addEntry(new {@link AutoCompleteEntry}(...))
     */
    @Deprecated
    public Form addAutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final AbstractAttributeReader labelTemplate, final String valueAttributeName, final String defaultValue) {
        return this.addAutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelTemplate, valueAttributeName, defaultValue, null);
    }

    /**
     * @deprecated use addEntry(new {@link AutoCompleteEntry}(...))
     */
    @Deprecated
    public Form addAutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition itemDefinition,
            final AbstractAttributeReader labelTemplate, final String valueAttributeName, final String defaultValue, final String description) {
        AutoCompleteEntry entry = new AutoCompleteEntry(jsid, label, tooltip, itemDefinition, labelTemplate, valueAttributeName, description);
        entry.addInputHandler(createInputCompleteHandler(), InputCompleteEvent.TYPE);
        addEntry(entry);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATIC
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form addItemAttributeStaticEntry(final ItemAttribute itemAttribute, final String label, final String tooltip) {
        return this.addItemAttributeStaticEntry(itemAttribute, label, tooltip, null, null, null);
    }

    public Form addItemAttributeStaticEntry(final ItemAttribute itemAttribute, final String label, final String tooltip, final String defaultValue) {
        return this.addItemAttributeStaticEntry(itemAttribute, label, tooltip, defaultValue, null, null);
    }

    public Form addItemAttributeStaticEntry(final ItemAttribute itemAttribute, final String label, final String tooltip, final String defaultValue,
            final String description) {
        return this.addItemAttributeStaticEntry(itemAttribute, label, tooltip, defaultValue, description, null);
    }

    public Form addItemAttributeStaticEntry(final ItemAttribute itemAttribute, final String label, final String tooltip, String defaultValue,
            final String description, final String example) {

        if (itemAttribute.getDefaultValue() != null && defaultValue == null) {
            defaultValue = itemAttribute.getDefaultValue();
        }

        final String preparedLabel = label + (itemAttribute.hasValidator(MandatoryValidator.class.getName()) ? " *" : "");

        final JsId jsid = new JsId(itemAttribute.getName());
        switch (itemAttribute.getType()) {
            case STRING:
                this.addStaticTextEntry(jsid, preparedLabel, tooltip, defaultValue, description, example);
                break;
            case TEXT:
                // TODO addStaticTextAreaEntry
                this.addStaticTextEntry(jsid, preparedLabel, tooltip, defaultValue, description, example);
                break;
            case DATE:
                this.addStaticTextEntry(jsid, preparedLabel, tooltip, defaultValue, description, example);
                getEntry(jsid).addOutputModifier(new DateToDisplayModifier());

                break;
            case PASSWORD:
                // TODO addStaticPasswordEntry
                // this.addStaticPasswordEntry(new JsId(itemAttribute.getName()), preparedLabel, tooltip, description, example, GWT.getModuleBaseURL() +
                // "imageUpload");
                break;
            case IMAGE:
                // TODO addStaticImageEntry
                // this.addStaticImageEntry(new JsId(itemAttribute.getName()), preparedLabel, tooltip, description, example, GWT.getModuleBaseURL() +
                // "imageUpload");
                break;
            case FILE:
                // TODO addStaticFileEntry
                // this.addStaticFileEntry(new JsId(itemAttribute.getName()), preparedLabel, tooltip, description, example, GWT.getModuleBaseURL() +
                // "fileUpload");
                break;
            case BOOLEAN:
                // TODO addStaticBooleanEntry
                // this.addStaticBooleanEntry(new JsId(itemAttribute.getName()), preparedLabel, tooltip, description, example, GWT.getModuleBaseURL() +
                // "fileUpload");
                break;
            case ENUM:
                // TODO addStaticMarkerEntry
                // this.addStaticMarkerEntry(new JsId(itemAttribute.getName()), preparedLabel, tooltip, description, example, GWT.getModuleBaseURL() +
                // "fileUpload");
                break;

            default:
                // Not supported
                ;
        }

        final ValuedFormEntry entry = getEntry(jsid);
        if (entry != null) {
            entry.addInputModifiers(itemAttribute.getInputModifiers());
            entry.addOutputModifiers(itemAttribute.getOutputModifiers());
            entry.addValidators(itemAttribute.getValidators());
        }

        return this;

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Doing the same with a JsId .
     */

    public Form addItemAttributeEntry(final JsId jsId, final ItemAttribute itemAttribute, final String label, final String tooltip) {
        return this.addItemAttributeEntry(jsId, itemAttribute, label, tooltip, null, null, null, null);
    }

    public Form addItemAttributeEntryWithMaxLength(final JsId jsId, final ItemAttribute itemAttribute, final String label, final String tooltip,
            final Long maxLength) {
        return this.addItemAttributeEntry(jsId, itemAttribute, label, tooltip, null, null, null, maxLength);
    }

    public Form addItemAttributeEntry(final JsId jsId, final ItemAttribute itemAttribute, final String label, final String tooltip, final String defaultValue) {
        return this.addItemAttributeEntry(jsId, itemAttribute, label, tooltip, defaultValue, null, null, null);
    }

    public Form addItemAttributeEntry(final JsId jsId, final ItemAttribute itemAttribute, final String label, final String tooltip, final String defaultValue,
            final String description) {
        return this.addItemAttributeEntry(jsId, itemAttribute, label, tooltip, defaultValue, description, null, null);
    }

    public Form addItemAttributeEntry(final JsId jsId, final ItemAttribute itemAttribute, final String label, final String tooltip, String defaultValue,
            final String description, final String example, Long maxLength) {

        if (itemAttribute.getDefaultValue() != null && defaultValue == null) {
            defaultValue = itemAttribute.getDefaultValue();
        }

        switch (itemAttribute.getType()) {
            default:
            case STRING:
                this.addTextEntry(jsId, label, tooltip, defaultValue, description, example, maxLength);
                break;
            case TEXT:
                this.addTextareaEntry(jsId, label, tooltip, defaultValue, description, example);
                break;
            case PASSWORD:
                this.addPasswordEntry(jsId, label, tooltip, description, example);
                break;
            case IMAGE:
                this.addFileEntry(jsId, label, tooltip, description, example, GWT.getModuleBaseURL() + "imageUpload");
                break;
            case FILE:
                this.addFileEntry(jsId, label, tooltip, description, example, GWT.getModuleBaseURL() + "fileUpload");
                break;
            case DATE:
                this.addTextEntry(jsId, label, tooltip, defaultValue, description, example);
                getEntry(jsId).addOutputModifier(new DateFormatModifier(DateFormat.FORMAT.SQL, DateFormat.FORMAT.FORM));
                getEntry(jsId).addInputModifier(new DateFormatModifier(DateFormat.FORMAT.FORM, DateFormat.FORMAT.SQL));
                // this.addDatePickerEntry(jsid, label, tooltip, defaultValue, description, example);
                break;
            case BOOLEAN:
                // TODO get the label of possible values in the definition
                this.addRadioEntry(jsId, label, tooltip, description, example);
                break;
            case ENUM:
                // TODO get the list of possible values in the definition
                // this.addSelectEntry(jsid, preparedLabel, tooltip, description, example, GWT.getModuleBaseURL() + "fileUpload");
                break;
        }

        final ValuedFormEntry entry = getEntry(jsId);
        if (entry != null) {
            entry.addInputModifiers(itemAttribute.getInputModifiers());
            // entry.addOutputModifiers(itemAttribute.getOutputModifiers());
            entry.addValidators(itemAttribute.getValidators());
        }

        return this;
    }

    public Form addItemAttributeEntry(final ItemAttribute itemAttribute, final String label, final String tooltip) {
        return this.addItemAttributeEntry(itemAttribute, label, tooltip, null, null, null);
    }

    public Form addItemAttributeEntry(final ItemAttribute itemAttribute, final String label, final String tooltip, final String defaultValue) {
        return this.addItemAttributeEntry(itemAttribute, label, tooltip, defaultValue, null, null);
    }

    public Form addItemAttributeEntry(final ItemAttribute itemAttribute, final String label, final String tooltip, final String defaultValue,
            final String description) {
        return this.addItemAttributeEntry(itemAttribute, label, tooltip, defaultValue, description, null);
    }

    public Form addItemAttributeEntry(final ItemAttribute itemAttribute, final String label, final String tooltip, final String defaultValue,
            final String description, final String example) {

        return this.addItemAttributeEntry(new JsId(itemAttribute.getName()), itemAttribute, label, tooltip, defaultValue, description, example, null);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COLORPICKER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form addColorPickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final String example) {
        addEntry(new ColorPickerEntry(jsid, label, tooltip, defaultValue, description, example));
        return this;
    }

    public Form addColorPickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        return this.addColorPickerEntry(jsid, label, tooltip, defaultValue, description, null);
    }

    public Form addColorPickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        return this.addColorPickerEntry(jsid, label, tooltip, defaultValue, null);
    }

    public Form addColorPickerEntry(final JsId jsid, final String label, final String tooltip) {
        return this.addColorPickerEntry(jsid, label, tooltip, null);

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DATEPICKER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final String example) {
        return this.addDatePickerEntry(jsid, label, tooltip, defaultValue, description, example, null, null);
    }

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        return this.addDatePickerEntry(jsid, label, tooltip, defaultValue, description, null);
    }

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        return this.addDatePickerEntry(jsid, label, tooltip, defaultValue, null);
    }

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip) {
        return this.addDatePickerEntry(jsid, label, tooltip, null);
    }

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final String example, final Date startDate, final Date endDate) {
        final DatePickerEntry entry = new DatePickerEntry(jsid, label, tooltip, defaultValue, description, example).setStartDate(startDate).setEndDate(endDate);
        addEntry(entry);

        return this;
    }

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description,
            final Date startDate, final Date endDate) {
        return this.addDatePickerEntry(jsid, label, tooltip, defaultValue, description, null, startDate, endDate);
    }

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final Date startDate,
            final Date endDate) {
        return this.addDatePickerEntry(jsid, label, tooltip, defaultValue, null, startDate, endDate);
    }

    public Form addDatePickerEntry(final JsId jsid, final String label, final String tooltip, final Date startDate, final Date endDate) {
        return this.addDatePickerEntry(jsid, label, tooltip, null, startDate, endDate);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final void validate() {
        resetErrors();
        ValidatorEngine.validate(this, getValidators());
    }

    /**
     * @Deprecated
     *             Use {@link ValuedFormEntry#addValidator(Validator)} i.e. form.getEntry(jsId).addValidator
     */
    @Deprecated
    @Override
    public Form addValidator(final JsId entryJsId, final Validator validator) {
        return (Form) super.addValidator(entryJsId, validator);
    }

    /**
     * @Deprecated
     *             Use {@link ValuedFormEntry#addValidator(Validator)}
     */
    @Deprecated
    @Override
    public Form addValidator(final String entryJsId, final Validator validator) {
        return (Form) super.addValidator(entryJsId, validator);
    }

    private InputCompleteHandler createInputCompleteHandler() {
        return new InputCompleteHandler() {

            @Override
            public void onComplete(InputCompleteEvent event) {
                Form.this.resetErrors();
            }
        };
    }
}
