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

import com.google.gwt.regexp.shared.RegExp;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class AbstractStringFormatValidator extends AbstractStringValidator {

    private final RegExp regexp;

    private Boolean exclude;

    /**
     * Default Constructor.
     * 
     * @param regexp
     */
    public AbstractStringFormatValidator(final String regexp) {
        this(regexp, "", false);
    }

    /**
     * Default Constructor.
     * 
     * @param regexp
     */
    public AbstractStringFormatValidator(final String regexp, final String flags) {
        this(regexp, flags, false);
    }

    /**
     * Default Constructor.
     * 
     * @param regexp
     */
    public AbstractStringFormatValidator(final String regexp, final boolean exclude) {
        this(regexp, "", exclude);
    }

    /**
     * Default Constructor.
     * 
     * @param regexp
     */
    public AbstractStringFormatValidator(final String regexp, final String flags, final boolean exclude) {
        super();
        this.regexp = RegExp.compile(regexp, flags);
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

        final boolean match = regexp.test(attributeValue);
        if (exclude && match || !exclude && !match) {
            addError(defineErrorMessage());
        }
    }

    abstract protected String defineErrorMessage();

}
