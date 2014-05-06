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
public abstract class AbstractStringComparisonValidator extends AbstractCollectionComparisonValidator {

    public AbstractStringComparisonValidator(final String secondAttributeName) {
        super(secondAttributeName);
    }

    @Override
    protected final void _check(final String[] attributeValue, final String secondAttributeValue) {
        for (int i = 0; i < attributeValue.length; i++) {
            this.check(attributeValue[i], secondAttributeValue);
        }
    }

    /**
     * Function to override to define the checking operation
     * 
     * @param attributeValue
     */
    public final void check(final String attributeValue, final String secondAttributeValue) {
        reset();
        if (attributeValue == null || secondAttributeValue == null || attributeValue.length() == 0 || secondAttributeValue.length() == 0) {
            // Not an error. The null value will be detected by a mandatory validator.
            return;
        }

        this._check(attributeValue, secondAttributeValue);
    }

    protected abstract void _check(String attributeValue, String secondAttributeValue);

}
