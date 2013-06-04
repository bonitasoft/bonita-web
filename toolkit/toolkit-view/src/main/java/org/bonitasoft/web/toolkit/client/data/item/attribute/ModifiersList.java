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

import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;
import org.bonitasoft.web.toolkit.client.ui.utils.ListUtils;

/**
 * @author Séverin Moussel
 * 
 */
public class ModifiersList {

    private final List<Modifier> modifiers = new LinkedList<Modifier>();

    public List<Modifier> getModifiers() {
        return this.modifiers;
    }

    public ModifiersList addModifier(final Modifier modifier) {
        ListUtils.removeFromListByClass(this.modifiers, modifier.getClass().toString(), true);
        this.modifiers.add(modifier);
        return this;
    }

    public ModifiersList addModifiers(final List<Modifier> modifiers) {
        for (final Modifier modifier : modifiers) {
            addModifier(modifier);
        }
        return this;
    }

    public ModifiersList removeModifier(final String modifierClassName) {
        ListUtils.removeFromListByClass(this.modifiers, modifierClassName);
        return this;
    }

    public boolean hasModifier(final String modifierClassName) {
        return getModifier(modifierClassName) != null;
    }

    public Modifier getModifier(final String modifierClassName) {
        return (Modifier) ListUtils.getFromListByClass(this.modifiers, modifierClassName);
    }

    public String apply(final String value) {
        return ModifierEngine.modify(value, this.modifiers);
    }
}
