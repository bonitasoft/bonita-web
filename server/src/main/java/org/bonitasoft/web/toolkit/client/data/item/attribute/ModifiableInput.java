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

import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;

/**
 * @author Séverin Moussel
 * 
 */
public interface ModifiableInput {

    public List<Modifier> getInputModifiers();

    public ModifiableInput addInputModifier(final Modifier modifier);

    public ModifiableInput addInputModifiers(final List<Modifier> modifiers);

    public ModifiableInput removeInputModifier(final String modifierClassName);

    public boolean hasInputModifier(final String modifierClassName);

    public Modifier getInputModifier(final String modifierClassName);

}
