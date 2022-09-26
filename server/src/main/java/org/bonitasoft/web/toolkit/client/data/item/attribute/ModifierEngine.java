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
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.AbstractStringModifier;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;

/**
 * @author Séverin Moussel
 * 
 */
public class ModifierEngine {

    public static void modify(final Map<String, String> values, final Map<String, List<Modifier>> modifiers) {
        values.replaceAll((n, v) -> modify(values.get(n), modifiers.get(n)));
    }

    public static String modify(final String value, final List<Modifier> modifiers) {
        if (modifiers == null) {
            return value;
        }

        String result = value;
        for (final Modifier modifier : modifiers) {
            if (result == null) {
                return null;
            }
            if (modifier instanceof AbstractStringModifier) {
                result = ((AbstractStringModifier) modifier).clean(result);
            }
        }

        return result;
    }

}
