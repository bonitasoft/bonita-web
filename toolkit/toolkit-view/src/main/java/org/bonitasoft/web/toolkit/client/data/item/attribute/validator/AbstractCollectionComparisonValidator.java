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
package org.bonitasoft.web.toolkit.client.data.item.attribute.validator;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class AbstractCollectionComparisonValidator extends AbstractComparisonValidator {

    public AbstractCollectionComparisonValidator(final String secondAttributeName) {
        super(secondAttributeName);
    }

    public final void check(final String[] attributeValue, final String secondAttributeValue) {
        reset();
        _check(attributeValue, secondAttributeValue);
    }

    protected abstract void _check(String[] attributeValue, String secondAttributeValue);

}
