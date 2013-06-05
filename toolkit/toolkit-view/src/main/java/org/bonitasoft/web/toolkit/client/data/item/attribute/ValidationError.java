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

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.texttemplate.TextTemplate;

/**
 * @author Séverin Moussel
 * 
 */
public class ValidationError {

    private final String attributeName;

    private final TextTemplate template;

    public ValidationError(final String attributeName, final String template) {
        this.attributeName = attributeName;
        this.template = new TextTemplate(template);
    }

    public String getMessage() {
        final List<Arg> args = new ArrayList<Arg>();
        for (final String parameterName : this.template.getExpectedParameters()) {
            args.add(new Arg(parameterName, parameterName));
        }

        return this.template.toString(args);
    }

    public String getMessage(final Arg... templateValues) {
        return this.template.toString(templateValues);
    }

    /**
     * @param args
     */
    public String getMessage(final List<Arg> args) {
        return this.template.toString(args);
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

    public TextTemplate getMessageTemplate() {
        return this.template;
    }

}
