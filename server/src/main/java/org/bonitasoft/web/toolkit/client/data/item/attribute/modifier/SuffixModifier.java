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
package org.bonitasoft.web.toolkit.client.data.item.attribute.modifier;

/**
 * @author Séverin Moussel
 * 
 */
public class SuffixModifier extends AbstractStringModifier {

    private final String suffix;

    public SuffixModifier(final String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String clean(final String value) {
        if (value.length() >= this.suffix.length() && value.substring(value.length() - this.suffix.length()).equals(this.suffix)) {
            return value;
        }

        return value + this.suffix;
    }

}
