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
package org.bonitasoft.web.toolkit.client.data.item.attribute;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.AbstractStringComparisonValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.AbstractStringValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

/**
 * @author Séverin Moussel
 * 
 */
public class ValidatorEngine {

    public static void validateAttribute(final String attributeName, final IItem item) throws ValidationException {
        validateAttribute(attributeName, item, true);
    }

    public static void validateAttribute(final String attributeName, final IItem item, final boolean applyMandatory) throws ValidationException {
        validateAttribute(attributeName, item.getAttributes(), item.getItemDefinition().getAttribute(attributeName).getValidators(), applyMandatory);
    }

    public static void validateAttribute(final String attributeName, final AbstractForm form) throws ValidationException {
        validateAttribute(attributeName, form, true);
    }

    public static void validateAttribute(final String attributeName, final AbstractForm form, final boolean applyMandatory) throws ValidationException {
        validateAttribute(attributeName, form.getValues(), form.getEntry(new JsId(attributeName)).getValidators(), applyMandatory);
    }

    public static void validateAttribute(final String attributeName, final TreeIndexed<String> values, final List<Validator> validators)
            throws ValidationException {
        validateAttribute(attributeName, values, validators, true);

    }

    public static void validateAttribute(final String attributeName, final TreeIndexed<String> values, final List<Validator> validators,
            final boolean applyMandatory)
            throws ValidationException {
        validateAttribute(attributeName, values.getValues(), validators, applyMandatory);

    }

    public static void validateAttribute(final String attributeName, final Map<String, String> values, final List<Validator> validators) {
        validateAttribute(attributeName, values, validators, true);
    }

    public static void validateAttribute(final String attributeName, final Map<String, String> values, final List<Validator> validators,
            final boolean applyMandatory)
            throws ValidationException {

        final List<ValidationError> errors = new LinkedList<ValidationError>();

        // Get validators
        if (validators != null) {
            // Check validators
            for (final Validator validator : validators) {
                // force attribute name as it could be different from the one set in the item definition (case of the deploys)
                validator.setAttributeName(attributeName);

                // Check mandatory validator
                if (validator instanceof MandatoryValidator) {
                    if (applyMandatory) {
                        ((MandatoryValidator) validator).check(values.get(attributeName));
                    }
                }
                // Check String based validator
                else if (validator instanceof AbstractStringValidator) {
                    ((AbstractStringValidator) validator).check(values.get(attributeName));
                }
                // Check Comparison validator
                else if (validator instanceof AbstractStringComparisonValidator) {
                    ((AbstractStringComparisonValidator) validator).check(
                            values.get(attributeName),
                            values.get(((AbstractStringComparisonValidator) validator).getSecondAttributeName())
                            );
                }
                errors.addAll(validator.getErrors());
            }
        }
        if (errors.size() > 0) {
            throw new ValidationException(errors);
        }
    }

    /**
     * Validate an Item
     * 
     * @throws ValidationException
     */
    public static void validate(final IItem item) throws ValidationException {
        validate(item, true);
    }

    /**
     * Validate an Item
     * 
     * @throws ValidationException
     */
    public static void validate(final IItem item, final boolean applyMandatory) throws ValidationException {
        validate(item.getAttributes(), item.getItemDefinition().getValidators(), applyMandatory);
    }

    /**
     * Validate a Form
     * 
     * @throws ValidationException
     */
    public static void validate(final AbstractForm form, final Map<String, List<Validator>> validators) throws ValidationException {
        validate(form, validators, true);
    }

    /**
     * Validate a Form
     * 
     * @throws ValidationException
     */
    public static void validate(final AbstractForm form, final Map<String, List<Validator>> validators, final boolean applyMandatory)
            throws ValidationException {
        validate(form.getValues(), validators, applyMandatory);
    }

    /**
     * Validate a Tree
     * 
     * @throws ValidationException
     */
    public static void validate(final TreeIndexed<String> tree, final Map<String, List<Validator>> validators) throws ValidationException {
        validate(tree, validators, true);
    }

    /**
     * Validate a Tree
     * 
     * @throws ValidationException
     */
    public static void validate(final TreeIndexed<String> tree, final Map<String, List<Validator>> validators, final boolean applyMandatory)
            throws ValidationException {
        validate(tree.getValues(), validators, applyMandatory);
    }

    /**
     * Validate a Map
     * 
     * @throws ValidationException
     */
    public static void validate(final Map<String, String> values, final Map<String, List<Validator>> validators) throws ValidationException {
        validate(values, validators, true);
    }

    /**
     * Validate a Map
     * 
     * @throws ValidationException
     */
    public static void validate(final Map<String, String> values, final Map<String, List<Validator>> validators, final boolean applyMandatory)
            throws ValidationException {
        final List<ValidationError> errors = new LinkedList<ValidationError>();
        for (final String attributeName : values.keySet()) {
            try {
                validateAttribute(attributeName, values, validators.get(attributeName), applyMandatory);
            } catch (final ValidationException e) {
                errors.addAll(e.getErrors());
            }
        }
        if (errors.size() > 0) {
            throw new ValidationException(errors);
        }
    }

    // public void validate(final TreeIndexed<String> tree, final Map<String, List<Validator>> validators) {
    //
    // tree.getValues()
    //
    // final List<ValidationError> errors = new LinkedList<ValidationError>();
    // for (final String attributeName : tree.keySet()) {
    // for (final Validator validator : this.getAttribute(attributeName).getValidators()) {
    // if (validator instanceof AbstractStringValidator) {
    // ((AbstractStringValidator) validator).check(tree.getValue(attributeName));
    // } else if (validator instanceof MandatoryValidator) {
    // ((MandatoryValidator) validator).check(tree.getValue(attributeName));
    // } else if (validator instanceof AbstractStringComparisonValidator) {
    // ((AbstractStringComparisonValidator) validator).check(
    // tree.getValue(attributeName),
    // tree.getValue(((AbstractStringComparisonValidator) validator).getSecondAttributeName())
    // );
    // }
    // errors.addAll(validator.getErrors());
    // }
    // }
    // if (errors.size() > 0) {
    // throw new ItemValidationException(errors);
    // }
    // }

}
