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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.t_;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Séverin Moussel
 * 
 */
public class StringLengthValidator extends AbstractStringValidator {

    int length = 0;

    public StringLengthValidator(final int length) {
        super();
        this.length = length;
    }

    @Override
    protected void _check(final String attributeValue) {
        if (attributeValue.length() != this.length) {
            addError(AbstractI18n.t_("%attribute% must contain exactly %length% characters", new Arg("length", String.valueOf(this.length))));
        }
    }
}
