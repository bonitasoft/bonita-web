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

import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiableInput;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiableOutput;
import org.bonitasoft.web.toolkit.client.data.item.attribute.Validable;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.containers.KnowsContainer;

/**
 * @author Séverin Moussel
 * 
 */
public interface ValuedFormEntry extends KnowsContainer, Validable, ModifiableOutput, ModifiableInput {

    public String getValue();

    public void setValue(String value);

    public JsId getJsId();

    public void addError(String message);

    @Override
    public ValuedFormEntry addValidator(Validator validator);

    @Override
    public ValuedFormEntry addValidators(List<Validator> validators);

    @Override
    public List<Validator> getValidators();

    @Override
    public ValuedFormEntry addInputModifier(Modifier modifier);

    @Override
    public ValuedFormEntry addOutputModifier(Modifier modifier);

    @Override
    public ValuedFormEntry addInputModifiers(List<Modifier> modifiers);

    @Override
    public ValuedFormEntry addOutputModifiers(List<Modifier> modifiers);

}
