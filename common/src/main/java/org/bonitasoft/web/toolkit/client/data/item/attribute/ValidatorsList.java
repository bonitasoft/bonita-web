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

import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.utils.ListUtils;

/**
 * @author Séverin Moussel
 * 
 */
public class ValidatorsList implements Validable {

    private final List<Validator> validators = new LinkedList<Validator>();

    @Override
    public List<Validator> getValidators() {
        return this.validators;
    }

    @Override
    public ValidatorsList addValidator(final Validator validator) {
        ListUtils.removeFromListByClass(this.validators, validator.getClass().getName(), true);
        this.validators.add(validator);
        return this;
    }

    @Override
    public ValidatorsList addValidators(final List<Validator> validators) {
        for (final Validator validator : validators) {
            addValidator(validator);
        }
        return this;
    }

    @Override
    public ValidatorsList removeValidator(final String validatorClassName) {
        ListUtils.removeFromListByClass(this.validators, validatorClassName);
        return this;
    }

    @Override
    public boolean hasValidator(final String validatorClassName) {
        return getValidator(validatorClassName) != null;
    }

    @Override
    public Validator getValidator(final String validatorClassName) {
        return (Validator) ListUtils.getFromListByClass(this.validators, validatorClassName);
    }
}
