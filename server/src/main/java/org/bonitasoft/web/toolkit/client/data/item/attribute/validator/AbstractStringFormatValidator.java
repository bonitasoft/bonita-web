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

import java.util.regex.Pattern;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class AbstractStringFormatValidator extends AbstractStringValidator {

    private final Pattern regexp;

    private Boolean exclude;

    /**
     * Default Constructor.
     * 
     * @param regexp
     */
    public AbstractStringFormatValidator(final String regexp) {
        this(regexp, false);
    }

    /**
     * Default Constructor.
     * 
     * @param regexp
     */
    public AbstractStringFormatValidator(final String regexp, final boolean exclude) {
        super();
        this.regexp = Pattern.compile(regexp);
        this.exclude = exclude;
    }

    protected void setExclude(final Boolean exclude) {
        this.exclude = exclude;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.client.toolkit.item.attribute.checker.AttributeStringChecker#check(java.lang.String)
     */
    @Override
    protected void _check(final String attributeValue) {

        // use `find()` instead of `matches()` because it was the implementation of the original `com.google.gwt.regexp.shared.RegExp#test()` method
        final boolean match = regexp.matcher(attributeValue).find();
        if (attributeValue.contains("HTTP Error")) {
            addError(AbstractI18n.t_("Error uploading the file. Maybe your session expired. You can try to refresh the page."));
        } else if (exclude && match || !exclude && !match) {
            addError(defineErrorMessage());
        }
    }

    abstract protected String defineErrorMessage();

}
