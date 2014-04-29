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

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.texttemplate.TextTemplate;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class AbstractComparisonValidator extends Validator {

    private final String secondAttributeName;

    public AbstractComparisonValidator(final String secondAttributeName) {
        super();
        this.secondAttributeName = secondAttributeName;
    }

    /**
     * @return the secondAttributeName
     */
    public final String getSecondAttributeName() {
        return this.secondAttributeName;
    }

    @Override
    protected final void addError(final String error) {
        super.addError(new TextTemplate(error).toString(new Arg("secondAttribute", "%" + this.secondAttributeName + "%")));
    }

}
