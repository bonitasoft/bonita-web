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

import java.util.ArrayList;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.texttemplate.TextTemplate;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidationError;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class Validator {

    private final ArrayList<ValidationError> errors = new ArrayList<ValidationError>();

    private String attributeName = null;

    /**
     * @param attributeName
     *            the attributeName to set
     */
    public void setAttributeName(final String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    public final ArrayList<ValidationError> getErrors() {
        return this.errors;
    }

    protected void addError(final String error) {
        this.errors.add(
                new ValidationError(
                        this.attributeName,
                        new TextTemplate(error).toString(new Arg("attribute", "%" + this.attributeName + "%")))
                );
    }

    public final boolean hasError() {
        return this.errors.size() > 0;
    }

    protected void reset() {
        this.errors.clear();
    }

}
