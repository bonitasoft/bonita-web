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
public class ReplaceRegexpModifier extends AbstractStringModifier {

    protected final String regexp;

    protected final String replace;

    public ReplaceRegexpModifier(final String regexp, final String replace) {
        super();
        this.regexp = regexp;
        this.replace = replace;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.client.toolkit.item.attribute.modifier.AbstractStringModifier#clean(java.lang.String)
     */
    @Override
    public String clean(final String value) {
        return value.replaceAll(this.regexp, this.replace);
    }

}
