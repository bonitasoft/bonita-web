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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.AbstractStringModifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.Alert;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormNode;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class FieldsetCheckbox extends Fieldset implements ValuedFormEntry {

    private String tooltip = null;

    private String value = null;

    private boolean checked = false;

    private Element inputElement = null;

    private Container<? extends FormNode> myContainer = null;

    public FieldsetCheckbox(final JsId jsid, final String label, final String tooltip, final String value) {
        this(jsid, label, tooltip, value, false);
    }

    public FieldsetCheckbox(final JsId jsid, final String label, final String tooltip, final String value, final boolean checked) {
        super(jsid, label);
        this.tooltip = tooltip;
        this.checked = checked;
        this.value = value;
    }

    @Override
    protected Element makeLabel() {
        final Element legend = DOM.createElement("legend");
        legend.setTitle(this.tooltip);

        final String uid = Document.get().createUniqueId();

        this.inputElement = createInput();
        this.inputElement.setId(uid);
        this.inputElement.setAttribute("value", this.value);

        if (this.checked) {
            this.inputElement.setAttribute("checked", "checked");
        }

        final Element label = DOM.createLabel();
        label.setInnerText(this.label);
        label.setAttribute("for", uid);

        legend.appendChild(this.inputElement);
        legend.appendChild(label);

        return legend;
    }

    protected Element createInput() {
        final Element input = DOM.createInputCheck();
        input.setAttribute("name", getJsId().toString());
        return input;
    }

    @Override
    public String getValue() {
        String realValue = this.val(this.inputElement);

        for (final Modifier modifier : getOutputModifiers()) {
            if (modifier instanceof AbstractStringModifier) {
                realValue = ((AbstractStringModifier) modifier).clean(realValue);
            }
        }

        return realValue;
    }

    protected native String val(Element e)
    /*-{
        return $wnd.$(e).val();
    }-*/;

    @Override
    public void setValue(final String value) {
        String realValue = value;

        for (final Modifier modifier : getInputModifiers()) {
            if (modifier instanceof AbstractStringModifier) {
                realValue = ((AbstractStringModifier) modifier).clean(realValue);
            }
        }

        this.val(this.inputElement, realValue);
    }

    protected native void val(Element e, final String value)
    /*-{
        $wnd.$(e).val(value);
    }-*/;

    @SuppressWarnings("unchecked")
    @Override
    public void setContainer(final Container<? extends Node> container) {
        this.myContainer = (Container<? extends FormNode>) container;
    }

    @Override
    public Container<? extends FormNode> getContainer() {
        return this.myContainer;
    }

    @Override
    public void addError(final String message) {
        Alert.addError(getElement(), message);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATORS AND MODIFIERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final ModifiersList inputModifiers = new ModifiersList();

    private final ModifiersList outputModifiers = new ModifiersList();

    private final ValidatorsList Validators = new ValidatorsList();

    /**
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#getModifiers()
     */
    @Override
    public List<Modifier> getInputModifiers() {
        return this.inputModifiers.getModifiers();
    }

    /**
     * @param modifier
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifier(org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier)
     */
    @Override
    public FieldsetCheckbox addInputModifier(final Modifier modifier) {
        this.inputModifiers.addModifier(modifier);
        return this;
    }

    /**
     * @param modifiers
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifiers(java.util.List)
     */
    @Override
    public FieldsetCheckbox addInputModifiers(final List<Modifier> modifiers) {
        this.inputModifiers.addModifiers(modifiers);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#removeModifier(java.lang.String)
     */
    @Override
    public FieldsetCheckbox removeInputModifier(final String modifierClassName) {
        this.inputModifiers.removeModifier(modifierClassName);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#hasModifier(java.lang.String)
     */
    @Override
    public boolean hasInputModifier(final String modifierClassName) {
        return this.inputModifiers.hasModifier(modifierClassName);
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#getModifier(java.lang.String)
     */
    @Override
    public Modifier getInputModifier(final String modifierClassName) {
        return this.inputModifiers.getModifier(modifierClassName);
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#getModifiers()
     */
    @Override
    public List<Modifier> getOutputModifiers() {
        return this.outputModifiers.getModifiers();
    }

    /**
     * @param modifier
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifier(org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier)
     */
    @Override
    public FieldsetCheckbox addOutputModifier(final Modifier modifier) {
        this.outputModifiers.addModifier(modifier);
        return this;
    }

    /**
     * @param modifiers
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifiers(java.util.List)
     */
    @Override
    public FieldsetCheckbox addOutputModifiers(final List<Modifier> modifiers) {
        this.outputModifiers.addModifiers(modifiers);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#removeModifier(java.lang.String)
     */
    @Override
    public FieldsetCheckbox removeOutputModifier(final String modifierClassName) {
        this.outputModifiers.removeModifier(modifierClassName);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#hasModifier(java.lang.String)
     */
    @Override
    public boolean hasOutputModifier(final String modifierClassName) {
        return this.outputModifiers.hasModifier(modifierClassName);
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#getModifier(java.lang.String)
     */
    @Override
    public Modifier getOutputModifier(final String modifierClassName) {
        return this.outputModifiers.getModifier(modifierClassName);
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#getValidators()
     */
    @Override
    public List<Validator> getValidators() {
        return this.Validators.getValidators();
    }

    /**
     * @param validator
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#addValidator(org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator)
     */
    @Override
    public FieldsetCheckbox addValidator(final Validator validator) {
        validator.setAttributeName(getJsId().toString());

        this.Validators.addValidator(validator);
        return this;
    }

    /**
     * @param validators
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#addValidators(java.util.List)
     */
    @Override
    public FieldsetCheckbox addValidators(final List<Validator> validators) {
        this.Validators.addValidators(validators);
        return this;
    }

    /**
     * @param validatorClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#removeValidator(java.lang.String)
     */
    @Override
    public FieldsetCheckbox removeValidator(final String validatorClassName) {
        this.Validators.removeValidator(validatorClassName);
        return this;
    }

    /**
     * @param validatorClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#hasValidator(java.lang.String)
     */
    @Override
    public boolean hasValidator(final String validatorClassName) {
        return this.Validators.hasValidator(validatorClassName);
    }

    /**
     * @param validatorClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#getValidator(java.lang.String)
     */
    @Override
    public Validator getValidator(final String validatorClassName) {
        return this.Validators.getValidator(validatorClassName);
    }
}
