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

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.AbstractStringModifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.Alert;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormNode;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
abstract public class FormEntry extends Component implements FormNode, JsonSerializable, ValuedFormEntry {

    protected String label = null;

    protected String tooltip = null;

    protected String description = null;

    protected String example = null;

    protected Element inputElement = null;

    protected Element labelElement = null;

    protected String defaultValue = null;

    protected String uid = null;

    protected Element getInputElement() {
        return this.inputElement;
    }

    protected void setInputElement(final Element inputElement) {
        this.inputElement = inputElement;
    }

    @Override
    public String getValue() {
        String realValue = _getValue();
        for (final Modifier modifier : getInputModifiers()) {
            if (modifier instanceof AbstractStringModifier) {
                realValue = ((AbstractStringModifier) modifier).clean(realValue);
            }
        }

        return realValue;
    }

    public abstract String _getValue();

    @Override
    public void setValue(final String value) {
        String realValue = value;
        for (final Modifier modifier : getOutputModifiers()) {
            if (modifier instanceof AbstractStringModifier) {
                realValue = ((AbstractStringModifier) modifier).clean(realValue);
            }
        }

        _setValue(realValue);
    }

    public abstract void _setValue(String value);

    public FormEntry(final JsId jsid, final String label, final String tooltip) {
        this(jsid, label, tooltip, null, null, null);
    }

    public FormEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        this(jsid, label, tooltip, defaultValue, null, null);
    }

    public FormEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        this(jsid, label, tooltip, defaultValue, description, null);
    }

    public FormEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final String example) {
        super(jsid);
        this.label = label;
        this.tooltip = tooltip;
        this.description = description;
        this.example = example;
        this.defaultValue = defaultValue;
        this.uid = DOM.createUniqueId();
    }

    protected abstract Element makeInput(String uid2);

    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setExample(final String example) {
        this.example = example;
    }

    @Override
    protected Element makeElement() {
        Boolean isMandatory = false;
        final Element entry = DOM.createDiv();
        entry.addClassName("formentry");
        entry.addClassName(getJsId().toString("formentry").toLowerCase());

        this.labelElement = DOM.createDiv();
        this.labelElement.addClassName("label");

        for (final Validator validator : getValidators()) {
            if (validator instanceof MandatoryValidator) {
                isMandatory = true;
                entry.addClassName("mandatory");
                break;
            }
        }

        final Element label = DOM.createLabel();
        label.setInnerText(this.label);
        label.setTitle(this.tooltip);
        label.setAttribute("for", this.uid.toLowerCase());

        if (isMandatory) {
            final Element mandatorySpan = DOM.createSpan();
            mandatorySpan.addClassName("mandatoryflag");
            mandatorySpan.setInnerText("*");
            label.appendChild(mandatorySpan);
        }

        this.labelElement.appendChild(label);

        if (this.description != null) {
            final Element description = DOM.createDiv();
            description.addClassName("description");
            description.setInnerText(this.description);
            this.labelElement.appendChild(description);
        }

        entry.appendChild(this.labelElement);

        final Element body = DOM.createDiv();
        body.addClassName("input");

        final Element input = makeInput(this.uid);

        if (this.inputElement == null) {
            this.inputElement = input;
        }

        body.appendChild(input);

        if (this.example != null) {
            final Element example = DOM.createDiv();
            example.addClassName("example");
            example.setInnerText(this.example);
            body.appendChild(example);
        }

        entry.appendChild(body);

        return entry;
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();
        if (this.defaultValue != null) {
            setValue(this.defaultValue);
        }
    }

    @Override
    public String toJson() {
        return JSonSerializer.serialize(getJsId().toString(), getValue());
    }

    private Container<FormNode> container = null;

    @SuppressWarnings("unchecked")
    @Override
    public void setContainer(final Container<? extends Node> container) {
        this.container = (Container<FormNode>) container;
    }

    @Override
    public Container<FormNode> getContainer() {
        return this.container;
    }

    @Override
    public void addError(final String message) {
        Alert.addError(getInputElement(), message);
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
    public FormEntry addInputModifier(final Modifier modifier) {
        this.inputModifiers.addModifier(modifier);
        return this;
    }

    /**
     * @param modifiers
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifiers(java.util.List)
     */
    @Override
    public FormEntry addInputModifiers(final List<Modifier> modifiers) {
        this.inputModifiers.addModifiers(modifiers);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#removeModifier(java.lang.String)
     */
    @Override
    public FormEntry removeInputModifier(final String modifierClassName) {
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
    public FormEntry addOutputModifier(final Modifier modifier) {
        this.outputModifiers.addModifier(modifier);
        return this;
    }

    /**
     * @param modifiers
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifiers(java.util.List)
     */
    @Override
    public FormEntry addOutputModifiers(final List<Modifier> modifiers) {
        this.outputModifiers.addModifiers(modifiers);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#removeModifier(java.lang.String)
     */
    @Override
    public FormEntry removeOutputModifier(final String modifierClassName) {
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
    public FormEntry addValidator(final Validator validator) {
        validator.setAttributeName(getJsId().toString());

        this.Validators.addValidator(validator);
        return this;
    }

    /**
     * @param validators
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#addValidators(java.util.List)
     */
    @Override
    public FormEntry addValidators(final List<Validator> validators) {
        this.Validators.addValidators(validators);
        return this;
    }

    /**
     * @param validatorClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorsList#removeValidator(java.lang.String)
     */
    @Override
    public FormEntry removeValidator(final String validatorClassName) {
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
